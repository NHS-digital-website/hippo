package org.hippoecm.frontend.plugins.cms.admin.updater

import org.apache.commons.lang.StringUtils
import org.hippoecm.repository.impl.PropertyDecorator
import org.onehippo.repository.update.BaseNodeUpdateVisitor
import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.query.Query


class UpdatePublicationTier extends BaseNodeUpdateVisitor {

    private Session session

    void initialize(Session session) throws RepositoryException {

        this.session = session
        log.info "Initialized script ${this.getClass().getName()}"
    }

    boolean updateNode(Node node) {

        def nodeType = node.getPrimaryNodeType().getName()


        if (nodeType.equals("publicationsystem:publication")) {
            log.debug "Set Tier is  -->" + node.getPath()
            log.debug "Get Tier from  -->" + node.getParent().getParent().getParent().getPath()
            def pubTier = "Tier 2"
            def pubIter = session
                .getWorkspace()
                .getQueryManager()
                .createQuery("/jcr:root"
                    + node.getParent().getParent().getParent().getPath()
                    + "//*[(@jcr:primaryType='publicationsystem:series')]", Query.XPATH)
                .execute()
                .getNodes();

            log.debug "**********  pubIter.hasNext() Series tier size   -->  " + pubIter.hasNext().toString();
            def count = 0;
            while (pubIter.hasNext()) {
                Node node1 = pubIter.nextNode();
                log.debug " Node " + count + " outside Path   -->" + node1.path;
                log.debug " Node " + count + " outside properties   -->" + node1.properties;
                def itr = node1.properties.iterator();


                if (node1.hasProperty("publicationsystem:publicationTier")) {
                    log.debug " Node 1 Path   -->" + node1.path;
                    pubTier = node1.getProperty("publicationsystem:publicationTier")
                        .getValue()
                        .getString()
                        .trim();
                    log.debug " Tier form the parent Series    -->" + pubTier;
                } else {
                    log.debug " Series does not contain publication tier   -->" + node1.path;
                }
            }
            if (node.hasProperty("publicationsystem:publicationtier")) {
                if (node.getProperty("publicationsystem:publicationtier") == null
                    || StringUtils.isBlank(node.getProperty("publicationsystem:publicationtier").getValue().getString())) {
                    log.debug " Tier not already setting to    -->" + pubTier;
                    node.setProperty("publicationsystem:publicationtier", pubTier);
                }
            } else {
                node.setProperty("publicationsystem:publicationtier", pubTier);
            }
            //log.debug "Pub Node Type is  pubTier  -->" + pubTier
            return true;
        }
        return false;
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
