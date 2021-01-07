import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node

/**
 * This script sets a value for threat severity compound in cyber alerts items.
 */
class MigrateThreatCyberAlertsSeverityValues extends BaseNodeUpdateVisitor {

    private static final String THREAT_SEVERITY = 'website:severity'
    private static final String NOMINAL_DATE = 'publicationsystem:NominalDate'

    private static final String THREAT_SEVERITY_COMPOUND = 'website:severitystatuschange'
    private static final String THREAT_SEVERITY_COMPOUND_SEVERITY = 'website:severity'
    private static final String THREAT_SEVERITY_COMPOUND_DATE = 'website:date'

    boolean doUpdate(Node node) {
        try {
            if (node.hasNodes()) {
                return updateNode(node)
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean updateNode(Node node) {
        def path = node.getPath()
        def nodeType = node.getPrimaryNodeType().getName()
        if ("website:cyberalert" == nodeType ) {
            log.info("checking node: " + path + " => current node type: " + nodeType)
            JcrUtils.ensureIsCheckedOut(node)

            def severity = node.getProperty(THREAT_SEVERITY)
            def nominalDate = node.getProperty(NOMINAL_DATE)

            def threatCompound = node.addNode("website:severitystatuschanges", THREAT_SEVERITY_COMPOUND);
            threatCompound.setProperty(THREAT_SEVERITY_COMPOUND_SEVERITY, severity.getValue())
            threatCompound.setProperty(THREAT_SEVERITY_COMPOUND_DATE, nominalDate.getValue())

            return true
        }
        return false
    }

    boolean undoUpdate(Node node) {
        def path = node.getPath()
        def nodeType = node.getPrimaryNodeType().getName()
        if ("website:cyberalert" == nodeType ) {
            log.info("checking node: " + path + " => current node type: " + nodeType)
            JcrUtils.ensureIsCheckedOut(node)

            def threatCompound = node.getNode("website:severitystatuschanges")
            threatCompound.remove()
            return true
        }
        return false

    }
}
