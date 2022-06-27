package uk.nhs.digital.arc.transformer.abs;

import static org.onehippo.cm.engine.Constants.HIPPO_PREFIX;

import org.apache.jackrabbit.JcrConstants;
import org.onehippo.forge.content.pojo.model.BinaryValue;
import org.onehippo.forge.content.pojo.model.ContentNode;
import org.onehippo.forge.content.pojo.model.ContentPropertyType;
import uk.nhs.digital.arc.storage.ArcStorageManager;
import uk.nhs.digital.externalstorage.s3.S3ObjectMetadata;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jcr.Session;

/**
 * This class is the parent of all transformation classes used by the system
 * It contains a number of useful methods that descendent classes will use for
 * manipulation of properties and shortcut methods for adding new child nodes
 * to a main {@link ContentNode}
 */

public abstract class AbstractTransformer {

    public static final String EXTERNAL_STORAGE = "externalstorage:";

    public static final String EXTERNALSTROAGE_REFERENCE = EXTERNAL_STORAGE + "reference";
    public static final String EXTERNALSTORAGE_RESOURCE = EXTERNAL_STORAGE + "resource";
    public static final String EXTERNALSTORAGE_SIZE = EXTERNAL_STORAGE + "size";
    public static final String EXTERNALSTORAGE_URL = EXTERNAL_STORAGE + "url";

    public static final String HIPPO = HIPPO_PREFIX + ":";

    public static final String HIPPO_DOCBASE = HIPPO + "docbase";
    public static final String HIPPO_FACETS = HIPPO + "facets";
    public static final String HIPPO_FILENAME = HIPPO + "filename";
    public static final String HIPPO_MIRROR = HIPPO + "mirror";
    public static final String HIPPO_MODES = HIPPO + "modes";
    public static final String HIPPO_TEXT = HIPPO + "text";
    public static final String HIPPO_VALUES = HIPPO + "values";

    public static final String HIPPOSTD_CONTENT = "hippostd:content";
    public static final String HIPPOSTD_HTML = "hippostd:html";

    public static final String HIPPOGALLERYPICKER_IMAGELINK = "hippogallerypicker:imagelink";

    public static final String PUBLICATION_SYSTEM_NOCOLON = "publicationsystem";
    public static final String PUBLICATION_SYSTEM = PUBLICATION_SYSTEM_NOCOLON + ":";

    public static final String PUBLICATIONSYSTEM_ACCESSIBLE = PUBLICATION_SYSTEM + "accessible";
    public static final String PUBLICATIONSYSTEM_ACCESSIBLELINK = PUBLICATION_SYSTEM + "accessiblelink";
    public static final String PUBLICATIONSYSTEM_ACCESSIBLELOCATION = PUBLICATION_SYSTEM + "accessiblelocation";
    public static final String PUBLICATIONSYSTEM_ADMINISTRATIVESOURCES = PUBLICATION_SYSTEM + "AdministrativeSources";
    public static final String PUBLICATIONSYSTEM_ATTACHMENTRESOURCE = PUBLICATION_SYSTEM + "attachmentResource";
    public static final String PUBLICATIONSYSTEM_ALTTEXT = PUBLICATION_SYSTEM + "altText";
    public static final String PUBLICATIONSYSTEM_BODYSECTIONS = PUBLICATION_SYSTEM + "bodySections";
    public static final String PUBLICATIONSYSTEM_CAPTION = PUBLICATION_SYSTEM + "caption";
    public static final String PUBLICATIONSYSTEM_CHANGENOTICE = PUBLICATION_SYSTEM + "changenotice";
    public static final String PUBLICATIONSYSTEM_CHARTCONFIG = PUBLICATION_SYSTEM + "chartConfig";
    public static final String PUBLICATIONSYSTEM_CHARTSECTION = PUBLICATION_SYSTEM + "chartSection";
    public static final String PUBLICATIONSYSTEM_CONTENT = PUBLICATION_SYSTEM + "content";
    public static final String PUBLICATIONSYSTEM_COVERAGESTART = PUBLICATION_SYSTEM + "CoverageStart";
    public static final String PUBLICATIONSYSTEM_COVERAGEEND = PUBLICATION_SYSTEM + "CoverageEnd";
    public static final String PUBLICATIONSYSTEM_DATAFILE = PUBLICATION_SYSTEM + "dataFile";
    public static final String PUBLICATIONSYSTEM_DISPLAYNAME = PUBLICATION_SYSTEM + "displayName";
    public static final String PUBLICATIONSYSTEM_DATE = PUBLICATION_SYSTEM + "date";
    public static final String PUBLICATIONSYSTEM_EXTATTACHMENT = PUBLICATION_SYSTEM + "extattachment";
    public static final String PUBLICATIONSYSTEM_GEOGRAPHICCOVERAGE = "GeographicCoverage";
    public static final String PUBLICATIONSYSTEM_GRANULARITY = PUBLICATION_SYSTEM + "Granularity";
    public static final String PUBLICATIONSYSTEM_HEADING = PUBLICATION_SYSTEM + "heading";
    public static final String PUBLICATIONSYSTEM_IMAGE = PUBLICATION_SYSTEM + "image";
    public static final String PUBLICATIONSYSTEM_IMAGESECTION = PUBLICATION_SYSTEM + "imageSection";
    public static final String PUBLICATIONSYSTEM_IMAGESIZE = PUBLICATION_SYSTEM + "imageSize";
    public static final String PUBLICATIONSYSTEM_INFORMATIONTYPE = PUBLICATION_SYSTEM + "InformationType";
    public static final String PUBLICATIONSYSTEM_INTERACTIVETOOL = PUBLICATION_SYSTEM + "interactivetool";
    public static final String PUBLICATIONSYSTEM_KEYFACTSHEAD = PUBLICATION_SYSTEM + "KeyFactsHead";
    public static final String PUBLICATIONSYSTEM_KEYFACTINFOGRAPHICS = PUBLICATION_SYSTEM + "keyFactInfographics";
    public static final String PUBLICATIONSYSTEM_KEYFACTSTAIL = PUBLICATION_SYSTEM + "KeyFactsTail";
    public static final String PUBLICATIONSYSTEM_LINK = PUBLICATION_SYSTEM + "link";
    public static final String PUBLICATIONSYSTEM_LINKTEXT = PUBLICATION_SYSTEM + "linkText";
    public static final String PUBLICATIONSYSTEM_LINKURL = PUBLICATION_SYSTEM + "linkUrl";
    public static final String PUBLICATIONSYSTEM_NEXTPUBLICATIONDATE = PUBLICATION_SYSTEM + "NextPublicationDate";
    public static final String PUBLICATIONSYSTEM_PUBLICALLYACCESSIBLE = PUBLICATION_SYSTEM + "PublicallyAccessible";
    public static final String PUBLICATIONSYSTEM_PUBLICATIONPAGE = PUBLICATION_SYSTEM + "publicationPage";
    public static final String PUBLICATIONSYSTEM_NOMINALDATE = PUBLICATION_SYSTEM + "NominalDate";
    public static final String PUBLICATIONSYSTEM_RELATEDLINK = PUBLICATION_SYSTEM + "relatedlink";
    public static final String PUBLICATIONSYSTEM_RESOURCE = PUBLICATION_SYSTEM + "resource";
    public static final String PUBLICATIONSYSTEM_RESOURCELINKS = PUBLICATION_SYSTEM + "ResourceLinks";
    public static final String PUBLICATIONSYSTEM_RELATEDLINKS = PUBLICATION_SYSTEM + "RelatedLinks";
    public static final String PUBLICATIONSYSTEM_SEOSUMMARY = PUBLICATION_SYSTEM + "seosummary";
    public static final String PUBLICATIONSYSTEM_SUMMARY = PUBLICATION_SYSTEM + "Summary";
    public static final String PUBLICATIONSYSTEM_SURVEY = PUBLICATION_SYSTEM + "survey";
    public static final String PUBLICATIONSYSTEM_TEXT = PUBLICATION_SYSTEM + "text";
    public static final String PUBLICATIONSYSTEM_TEXTSECTION = PUBLICATION_SYSTEM + "textSection";
    public static final String PUBLICATIONSYSTEM_TITLE = PUBLICATION_SYSTEM + "title";
    public static final String PUBLICATIONSYSTEM_TITLE_UC = PUBLICATION_SYSTEM + "Title";
    public static final String PUBLICATIONSYSTEM_TYPE = PUBLICATION_SYSTEM + "type";
    public static final String PUBLICATIONSYSTEM_YTITLE = PUBLICATION_SYSTEM + "yTitle";

    public static final String JCR_LASTMOD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    protected ArcStorageManager storageManager;
    protected String docbase;
    protected Session session;
    protected List<String> filesToAdd = new ArrayList<>();

    protected AbstractTransformer() {
    }

    public void setStorageManager(ArcStorageManager storageManger) {
        this.storageManager = storageManger;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public abstract ContentNode process();

    protected void setMultipleProp(ContentNode cn, String property, List<String> values) {
        if (values != null) {
            cn.setProperty(property, values.toArray(new String[0]));
        }
    }

    /**
     * Some properties exist beneath their own node (attachments etc) so we create that node and set the property
     * @param contentNode is the parent node to which we will attach or new node
     * @param nodeName is the name of the new node
     * @param primaryType is the type of the new node
     * @param propertyName is teh property we are setting
     * @param propertyValue is that value
     * @return is the reference to the node that is created (although it is already going to be attached)
     */
    protected ContentNode setSingleNodeLevelProperty(ContentNode contentNode,
                                                     String nodeName,
                                                     String primaryType,
                                                     String propertyName,
                                                     String propertyValue) {
        if (propertyValue != null) {
            ContentNode newNode = new ContentNode(nodeName, primaryType);
            newNode.setProperty(propertyName, propertyValue);
            contentNode.addNode(newNode);
            return newNode;
        }
        return null;
    }

    public void setDocbase(String docbase) {
        this.docbase = docbase;
    }

    /**
     * External files are stored in S3 and this method will create the structure of the Nodes to support that
     * @param contentNode is the {@link ContentNode} to which we will add this information
     * @param nodeTypeName is the modeType, as known by the JCR. It is part of the PUBLICATION_SYSTEM namespace
     * @param displayName is a displayName used for the node
     * @param resource is the location, as an S3 URL, of the source file that is being uploaded
     * @param childNodeName is the name of the subordinate node with resource details
     */
    protected void populateAndCreateExternalAttachmentNode(ContentNode contentNode,
                                                           String nodeTypeName,
                                                           String displayName,
                                                           String resource,
                                                           String childNodeName) {
        ContentNode attachmentNode = new ContentNode(PUBLICATION_SYSTEM + nodeTypeName, PUBLICATIONSYSTEM_EXTATTACHMENT);
        attachmentNode.setProperty(PUBLICATIONSYSTEM_DISPLAYNAME, displayName);
        contentNode.addNode(attachmentNode);

        S3ObjectMetadata s3meta = storageManager.uploadFileToS3(docbase, resource);

        ContentNode resourceNode = new ContentNode(childNodeName, EXTERNALSTORAGE_RESOURCE);
        resourceNode.setProperty(EXTERNALSTORAGE_SIZE, ContentPropertyType.LONG, "10000");
        resourceNode.setProperty(EXTERNALSTORAGE_URL, s3meta.getUrl());
        resourceNode.setProperty(EXTERNALSTROAGE_REFERENCE, s3meta.getReference());

        addFileRelatedProperties(resourceNode, new BinaryValue(new byte[0]), s3meta.getMimeType(), s3meta.getFileName());
        attachmentNode.addNode(resourceNode);
    }

    /**
     * A number of external file related activities use this method to set required properties. The node itself is really used
     * as a placeholder for a file that is being stored in S3 and as such, it contains the S3 path information but the Binary data
     * held by this node is set to an empty value.
     * @param resourceNode the {@link ContentNode} instance to which we will add these properties
     * @param data the binary data that is being added. Generally set to an empty value as it's used as a placeholder
     *             when S3 storage is being used
     * @param mimeType required for the reference in the JCR
     * @param fileName is a reference ot the filename
     */
    protected void addFileRelatedProperties(ContentNode resourceNode, BinaryValue data, String mimeType, String fileName) {
        resourceNode.setProperty(JcrConstants.JCR_DATA, data);
        resourceNode.setProperty(HIPPO_TEXT, new BinaryValue(new byte[0], mimeType, StandardCharsets.UTF_8.displayName()));

        resourceNode.setProperty(HIPPO_FILENAME, fileName);

        resourceNode.setProperty(JcrConstants.JCR_ENCODING, StandardCharsets.UTF_8.displayName());
        resourceNode.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
        resourceNode.setProperty(JcrConstants.JCR_LASTMODIFIED, ContentPropertyType.DATE,
            new SimpleDateFormat(JCR_LASTMOD_DATE_FORMAT).format(new Date()));
    }

    /**
     * Shortcut method to check data exists in a list
     * @param list is what we are checking
     * @return is a boolean to indicate the outcome
     */
    protected boolean listExists(List<?> list) {
        return list != null && list.size() > 0;
    }

}
