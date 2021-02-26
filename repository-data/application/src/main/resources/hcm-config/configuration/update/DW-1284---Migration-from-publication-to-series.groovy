package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/*
This script takes the series fields (Granularity, Geographic Coverage, Administrative sources, Taxonomy and Information Types) from a file and finds
the corresponding series in the JCR and then overides the existing values with the ones coming from the file.
Bear in mind if a field of a series in the input is empty or blank, it will remove it in the JCR.


When runnin this script "XPath query" should be for example:
/jcr:root/content/documents/corporate-website//*[(@jcr:primaryType='publicationsystem:series')]

This script has a parameter called "inputFile", that is passed in the "parameters" field in the CMS script runner
(Setup icon/ Update editor).
An example would be:
{
    "inputFile" :  "/content/assets/assetsmiguel/seriesinfoexport.json/seriesinfoexport.json/hippogallery:asset"
}
As you can see, the value is a path in the JCR. That path is a file in the assets folder.
The last bit of the path "seriesinfoexport.json/hippogallery:asset" is the actual node that contains the content.
The content of that file should be a file in json format.

To upload that file in the CMS, sign-in in the CMS, go to "Content" section, in the dropdown change to "assets". Once in the assets,
if needed, create a folder and then in that folder choose "add file" and choose the file to upload.

Now to find out the path, go to the CMS console, sign-in, open /content/assets folder and try to find the node that represents
the file you have just uploaded in the CMS, once in the file, there is a property called path on top of the window,
it should look like this, for example:
"/content/assets/assetsmiguel/seriesinfoexport.json/seriesinfoexport.json/hippogallery:asset"

Once imported you can remove that file from the CMS and if you created a subfolder in assets, you can remove that too.

*/

class DW1284MigrationFromPublicationToSeries extends BaseNodeUpdateVisitor {

    private final static JSON_FIELD_TITLE = "Current Series Page Title"
    private final static JSON_FIELD_GRANULARITY = "Granularity"
    private final static JSON_FIELD_GEOGRAPHIC_COVERAGE = "Geographic Coverage"
    private final static JSON_FIELD_INFORMATION_TYPES = "Information Type"
    private final static JSON_FIELD_TAXONOMY = "Taxonomy topics"
    private final static JSON_FIELD_ADMINISTRATIVE_SOURCES = "AdministrativeSources"

    private final static JCR_PROPERTY_TITLE = "publicationsystem:Title"
    private final static JCR_PROPERTY_GRANULARITY = "publicationsystem:Granularity"
    private final static JCR_PROPERTY_GEOGRAPHIC_COVERAGE = "publicationsystem:GeographicCoverage"
    private final static JCR_PROPERTY_INFORMATION_TYPE = "publicationsystem:InformationType"
    private final static JCR_PROPERTY_FULL_TAXONOMY = "common:FullTaxonomy"
    private final static JCR_PROPERTY_TAXONOMY_KEYS = "hippotaxonomy:keys"
    private final static JCR_PROPERTY_ADMINISTRATIVE_SOURCES = "publicationsystem:AdministrativeSources"

    Session session
    JSONArray jsonArray

    void initialize(Session session) {
        this.session = session

        try {
            String fileInJsonPath //= "/content/assets/assetsmiguel/seriesinfoexport.json/seriesinfoexport.json/hippogallery:asset"
            if (parametersMap.containsKey("inputFile")) {
                fileInJsonPath = parametersMap.get("inputFile")
            }
            Node nodeInputFileResource = session.getNode(fileInJsonPath)
            log.debug("Loading input file in path=" + nodeInputFileResource.getPath())
            String jsonString = nodeInputFileResource.getProperty("jcr:data").getString()

            jsonArray = new JSONArray(jsonString)
            log.info("\nInput data contains [" + jsonArray.length() + "] series")
            log.debug("\n\n\n")

        } catch (JSONException e) {
            log.error("Exception converting json: ", e)
        }
    }

    boolean doUpdate(Node node) {
        // Query returns the hippo:handle node for the document
        // (which has the 3 variants)
        try {
            if (node.hasNodes()) {
                return updateNode(node)
            }
        } catch (e) {
            log.error("Failed to process node: " + node.getPath() + ", exception: " + e)
        }
        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

    boolean updateNode(Node n) {
        JcrUtils.ensureIsCheckedOut(n)

        def path = n.getPath()
        def nodeType = n.getPrimaryNodeType().getName()
        def title = n.getProperty(JCR_PROPERTY_TITLE).getString()

        log.info("\n\nProcessing node")
        log.info("  path=" + path)
        log.info("  title=" + title)

        if (("publicationsystem:series".equals(nodeType)))  {
            SeriesNewFields series = getSeriesFieldsByTitle(title, jsonArray)
            if (series == null) {
                return false
            } else {
                return updateProperties(n, series)
            }
        }

        return false
    }

    class SeriesNewFields {
        public String title
        public String[] granularity
        public String[] geographicCoverage
        public String[] informationTypes
        public String[] taxonomy
        public String administrativeSources

        public SeriesNewFields(JSONObject jsonObject) {
            this.title                 = jsonObject.get(JSON_FIELD_TITLE).toString()

            this.granularity           = jsonObject.get(JSON_FIELD_GRANULARITY).toString().replace('[', '').replace(']', '').split(",")
            if (this.granularity != null && this.granularity.length == 1 && this.granularity[0].trim().equals("")) {
                this.granularity = null
            }

            this.geographicCoverage    = jsonObject.get(JSON_FIELD_GEOGRAPHIC_COVERAGE).toString().replace('[', '').replace(']', '').split(",")
            if (this.geographicCoverage != null && this.geographicCoverage.length == 1 && this.geographicCoverage[0].trim().equals("")) {
                this.geographicCoverage = null
            }

            this.informationTypes      = jsonObject.get(JSON_FIELD_INFORMATION_TYPES).toString().replace('[', '').replace(']', '').split(",")
            if (this.informationTypes != null && this.informationTypes.length == 1 && this.informationTypes[0].trim().equals("")) {
                this.informationTypes = null
            }

            this.taxonomy              = jsonObject.get(JSON_FIELD_TAXONOMY).toString().replace('[', '').replace(']', '').split(",")
            if (this.taxonomy != null && this.taxonomy.length == 1 && this.taxonomy[0].trim().equals("")) {
                this.taxonomy = null
            }

            this.administrativeSources = jsonObject.get(JSON_FIELD_ADMINISTRATIVE_SOURCES).toString()
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\ntitle: ").append(title)
            sb.append("\n granularity: ").append(arrayToString(granularity))
            sb.append("\n geographicCoverage: ").append(arrayToString(geographicCoverage))
            sb.append("\n informationTypes: ").append(arrayToString(informationTypes))
            sb.append("\n taxonomy: ").append(arrayToString(taxonomy))
            sb.append("\n administrativeSources: ").append(administrativeSources)
        }

        private String arrayToString(String[] myArray) {
            StringBuilder sb = new StringBuilder()
            for (int i=0; i < myArray.length; i++) {
                sb.append(myArray[i])
                sb.append(", ")
            }
            if(sb.length() > 0) {
                return sb.substring(0, sb.length()-2)
            } else {
                return ""
            }
        }
    }

    SeriesNewFields getSeriesFieldsByTitle(title, myArray) {
        try {
            Iterator iterator = myArray.iterator()
            Boolean found = false
            JSONObject item
            while (iterator.hasNext() && !found) {
                item = (JSONObject) iterator.next();
                String titleItem = item.get(JSON_FIELD_TITLE)
                if (title.equals(titleItem)) found = true;
            }
            if (found) {
                SeriesNewFields series = new SeriesNewFields(item)
//        log.debug("  series found=" + series.toString())
                log.debug("  series found")
                return series
            } else {
                log.debug("  series not found")
            }
        } catch (Exception ex) {
            log.error("  Exception in getSeriesFieldsByTitle=" + ex)
            ex.printStackTrace()
        }
        return null
    }

    boolean updateProperties(Node node, SeriesNewFields series) {
        if (series.granularity == null || series.granularity.length == 0) {
            node.setProperty(JCR_PROPERTY_GRANULARITY, (String[]) null)
        } else {
            node.setProperty(JCR_PROPERTY_GRANULARITY, series.granularity)
        }

        if (series.geographicCoverage == null || series.geographicCoverage.length == 0) {
            node.setProperty(JCR_PROPERTY_GEOGRAPHIC_COVERAGE, (String[]) null)
        } else {
            node.setProperty(JCR_PROPERTY_GEOGRAPHIC_COVERAGE, series.geographicCoverage)
        }

        if (series.informationTypes == null || series.informationTypes.length == 0) {
            node.setProperty(JCR_PROPERTY_INFORMATION_TYPE, (String[]) null)
        } else {
            node.setProperty(JCR_PROPERTY_INFORMATION_TYPE, series.informationTypes)
        }

        if (series.taxonomy == null || series.taxonomy.length == 0) {
            node.setProperty(JCR_PROPERTY_FULL_TAXONOMY, (String[]) null)
            node.setProperty(JCR_PROPERTY_TAXONOMY_KEYS, (String[]) null)
        } else {
            node.setProperty(JCR_PROPERTY_FULL_TAXONOMY, series.taxonomy)
            node.setProperty(JCR_PROPERTY_TAXONOMY_KEYS, series.taxonomy)
        }

        node.setProperty(JCR_PROPERTY_ADMINISTRATIVE_SOURCES, series.administrativeSources)

        return true
    }
}
