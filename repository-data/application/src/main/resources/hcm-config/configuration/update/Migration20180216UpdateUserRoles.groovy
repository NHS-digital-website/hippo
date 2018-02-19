package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.util.JcrUtils
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.Value

/**
 * This migration makes the users under the author and editor groups publication system authors and editors instead
 */
class Migration20180216UpdateUserRoles extends BaseNodeUpdateVisitor {

    boolean doUpdate(Node node) {
        try {
            def name = node.getName()
            if (name == "author" || name == "editor") {
                def newMembers = node.getProperty("hipposys:members").getValues()

                def newGroup = node.getParent().getNode("statistical-publications-and-clinical-indicators-" + name)
                def existingMembers = newGroup.hasProperty("hipposys:members") ? newGroup.getProperty("hipposys:members").getValues() : new Value[0]

                Value[] members = newMembers + existingMembers;

                JcrUtils.ensureIsCheckedOut(newGroup)
                newGroup.setProperty("hipposys:members", members);

                JcrUtils.ensureIsCheckedOut(node)
                node.setProperty("hipposys:members", new Value[0])

                return true
            }
        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) {
        throw new UnsupportedOperationException();
    }
}
