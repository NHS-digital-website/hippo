import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import javax.jcr.Node



/**
 * A lot of urlrewriter rules contain a urlcondition which is not required
 *
 * [name="urlrewriter:conditionvalue"] = digital.nhs.uk
 * [name="urlrewriter:conditiontype"] = server-name
 *
 */
class RemoveUnnecessaryRuleCondition extends BaseNodeUpdateVisitor {

    private final String PROPERTY_CONDITION_TYPE = "urlrewriter:conditiontype"
    private final String PROPERTY_CONDITION_VALUE = "urlrewriter:conditionvalue"


    boolean doUpdate(Node node) {
        try {
            return updateDocument(node)
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }


    boolean updateDocument(Node node) {

        if (node.getProperty(PROPERTY_CONDITION_TYPE).getString() != "server-name") {
            return false
        }

        if (node.getProperty(PROPERTY_CONDITION_VALUE).getString() == "digital.nhs.uk") {
            JcrUtils.ensureIsCheckedOut(node)
            log.debug("Removing... " + node.getPath())
            node.remove()
            return true
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException('Updater does not implement undoUpdate method')
    }

}
