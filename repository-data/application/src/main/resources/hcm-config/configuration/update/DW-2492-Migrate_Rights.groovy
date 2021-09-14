package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.commons.lang.StringUtils
import org.hippoecm.repository.impl.PropertyDecorator
import org.onehippo.forge.content.pojo.model.ContentProperty
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.query.Query


class MigrateRights extends BaseNodeUpdateVisitor {

    private Session session

    void initialize(Session session) throws RepositoryException {

        this.session = session
        log.info "Initialized script ${this.getClass().getName()}"
    }

    boolean updateNode(Node node) {

        def nodeType = node.getPrimaryNodeType().getName()


        if (nodeType.equals("website:gdprtransparency")) {
            def beinformed = node.addNode("website:beinformed", "website:gdprrights")
            beinformed.addNode("website:qualification", "hippostd:html").setProperty("hippostd:content", "");
            beinformed.setProperty("website:gdprrightsyesno", "no")

            def computerdecisionorperson = node.addNode("website:computerdecisionorperson", "website:gdprrights")
            computerdecisionorperson.addNode("website:qualification", "hippostd:html").setProperty("hippostd:content", "");
            computerdecisionorperson.setProperty("website:gdprrightsyesno", "no")

            def eraseorremove = node.addNode("website:eraseorremove", "website:gdprrights")
            eraseorremove.addNode("website:qualification", "hippostd:html").setProperty("hippostd:content", "");
            eraseorremove.setProperty("website:gdprrightsyesno", "no")

            def getaccesstoit = node.addNode("website:getaccesstoit", "website:gdprrights")
            getaccesstoit.addNode("website:qualification", "hippostd:html").setProperty("hippostd:content", "");
            getaccesstoit.setProperty("website:gdprrightsyesno", "no")

            def objecttoit = node.addNode("website:objecttoit", "website:gdprrights")
            objecttoit.addNode("website:qualification", "hippostd:html").setProperty("hippostd:content", "");
            objecttoit.setProperty("website:gdprrightsyesno", "no")

            def movecopytransfer = node.addNode("website:movecopytransfer", "website:gdprrights")
            movecopytransfer.addNode("website:qualification", "hippostd:html").setProperty("hippostd:content", "");
            movecopytransfer.setProperty("website:gdprrightsyesno", "no")

            def rectifyorchange = node.addNode("website:rectifyorchange", "website:gdprrights")
            rectifyorchange.addNode("website:qualification", "hippostd:html").setProperty("hippostd:content", "");
            rectifyorchange.setProperty("website:gdprrightsyesno", "no")

            def restrictstoppro = node.addNode("website:restrictstoppro", "website:gdprrights")
            restrictstoppro.addNode("website:qualification", "hippostd:html").setProperty("hippostd:content", "");
            restrictstoppro.setProperty("website:gdprrightsyesno", "no")


            if (node.hasProperty("website:rights")) {
                def valuex = node.getProperty("website:rights").getProperties().get("values")
                for (int i = 0; i < valuex.size(); i++) {
                    log.debug "Value is " + valuex[i].getAt("string")
                    if ("be-informed".equalsIgnoreCase(valuex[i].getAt("string"))) {
                        beinformed.setProperty("website:gdprrightsyesno", "yes")
                    }
                    if ("get-access-to-it".equalsIgnoreCase(valuex[i].getAt("string"))) {
                        getaccesstoit.setProperty("website:gdprrightsyesno", "yes")
                    }
                    if ("rectify-or-change-it".equalsIgnoreCase(valuex[i].getAt("string"))) {
                        rectifyorchange.setProperty("website:gdprrightsyesno", "yes")
                    }
                    if ("erase-or-remove-it".equalsIgnoreCase(valuex[i].getAt("string"))) {
                        eraseorremove.setProperty("website:gdprrightsyesno", "yes")
                    }
                    if ("restrict-or-stop-processing-it".equalsIgnoreCase(valuex[i].getAt("string"))) {
                        restrictstoppro.setProperty("website:gdprrightsyesno", "yes")
                    }
                    if ("move- copy-or-transfer-it".equalsIgnoreCase(valuex[i].getAt("string"))) {
                        movecopytransfer.setProperty("website:gdprrightsyesno", "yes")
                    }
                    if ("object-to-it-being-processed-or-used".equalsIgnoreCase(valuex[i].getAt("string"))) {
                        objecttoit.setProperty("website:gdprrightsyesno", "yes")
                    }
                    if ("know-if-a-decision-was-made-by-a-computer-rather-than-a-person".equalsIgnoreCase(valuex[i].getAt("string"))) {
                        computerdecisionorperson.setProperty("website:gdprrightsyesno", "yes")
                    }
                }
            }
            if (node.hasProperty("website:howuseinformation")) {
                def newNode = node.addNode("website:howuseinformation", "hippostd:html");
                newNode.setProperty("hippostd:content", "<p>" + node.getProperty("website:howuseinformation").getString() + "</p>");
            }

            return true;
        }
    }

    @Override
    boolean doUpdate(Node node) throws RepositoryException {
        try {

            return updateNode(node)

        } catch (e) {
            log.error("Failed to process record.", e)
        }

        return false
    }

    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        return false
    }

}
