package org.hippoecm.frontend.plugins.cms.admin.updater

import org.onehippo.forge.content.exim.core.DocumentManager
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import org.apache.jackrabbit.value.StringValue
import javax.jcr.*

class Migration20180313UpdateGeographicCoverage extends BaseNodeUpdateVisitor {

    private static final String GEOGRAPHIC_COVERAGE_PROPERTY = "publicationsystem:GeographicCoverage"

    def geographicMap = ['England':['England'],
                         'England and Northern Ireland':['England','Northern Ireland'],
                         'England and Scotland':['England','Scotland'],
                         'England and Wales':['England','Wales'],
                         'England Scotland and Northern Ireland':['England','Scotland','Northern Ireland'],
                         'England Wales and Northern Ireland':['England','Wales','Northern Ireland'],
                         'Great Britain':['Great Britain'],
                         'International':['International'],
                         'Northern Ireland':['Northern Ireland'],
                         'Scotland':['Scotland'],
                         'UK':['UK'],
                         'Wales':['Wales']
    ]

    boolean doUpdate(Node node) {

        try {
            if (node.hasNodes()) {
                return updateFieldAndSaveDocument(node)
            }

        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean updateFieldAndSaveDocument(Node node) {

        NodeIterator nodeIterator = node.getNodes()
        Node n
        int variantsUpdated = 0

        while(nodeIterator.hasNext()) {
            n = nodeIterator.nextNode()
            String path = n.getPath()

            if (!n.hasProperty(GEOGRAPHIC_COVERAGE_PROPERTY)) {
                continue
            }

            String currentValue
            try {
                currentValue = n.getProperty(GEOGRAPHIC_COVERAGE_PROPERTY).getString()
            } catch (ValueFormatException vfe) {

                // when the config changes are applied, the geographic coverage field will
                // change from single value to multi value.  This script will then be run to
                // update all the single values in repository. However, if before this script
                // is run, a user updates a document the field will already be in multi-value
                // format and exception "publicationsystem:GeographicCoverage is a multi-valued
                // property, so it's values can only be retrieved as an array" occurs.
                log.info("Geographic Coverage field already multi-value, skipping " + path)
                continue
            }

            log.info("attempting to update node: " + path)

            // Remove old single value property
            n.getProperty(GEOGRAPHIC_COVERAGE_PROPERTY).remove()

            // Construct new property Value array and populate with mapped values
            def mappedValues = geographicMap.get(currentValue)

            Value[] values = new Value[mappedValues.size()]
            for (int i=0; i<values.length; i++) {
                values[i] = new StringValue(mappedValues.get(i))
            }

            // Add new multi value property
            n.setProperty(GEOGRAPHIC_COVERAGE_PROPERTY, values)

            log.info("UPDATED Geographic Coverage Values for:" + path)
            variantsUpdated += 1
        }

        return variantsUpdated > 0
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }
}
