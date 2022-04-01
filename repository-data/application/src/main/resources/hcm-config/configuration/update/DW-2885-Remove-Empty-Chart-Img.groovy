package org.hippoecm.frontend.plugins.cms.admin.updater

import org.hippoecm.repository.impl.PropertyDecorator
import org.onehippo.repository.update.BaseNodeUpdateVisitor

import javax.jcr.Node
import javax.jcr.PropertyIterator
import javax.jcr.RepositoryException
import javax.jcr.Session

class RemoveEmptyChartImgTag extends BaseNodeUpdateVisitor {
    private static final String PROPERTY_CHART_SECTION ="publicationsystem:chartSection"

    void initialize(Session session) throws RepositoryException {
        log.info "Initialized script ${this.getClass().getName()}"
    }

    @Override
    boolean doUpdate(Node node) throws RepositoryException {
        def nodeType = node.getPrimaryNodeType().getName()

        final nodeIterator = node.getNodes();
        while (nodeIterator.hasNext()) {
            Node nd = nodeIterator.nextNode()
            if("website:sections".equalsIgnoreCase(nd.getName())){
                def chartNode = nd.getPrimaryNodeType().getName()
                if(chartNode.equalsIgnoreCase("publicationsystem:chartSection") ||
                    chartNode.equalsIgnoreCase("website:dynamicchartsection")){
                    PropertyIterator iter1 = nd.properties;
                    while (iter1.hasNext()) {
                        PropertyDecorator pd = iter1.nextProperty();
                        if("website:htmlCode".equalsIgnoreCase(pd.name)){
                            String htmlCode = pd.getValue().getString();
                            if(htmlCode.contains("<img src=\"\"")){
                                log.debug("Path is  " + pd.path)
                                log.debug("Content is  " + pd.getValue().getString())
                                pd.setValue("");
                            }
                        }
                    }
                }

            }
        }
        return false
    }

    @Override
    boolean undoUpdate(Node node) throws RepositoryException, UnsupportedOperationException {
        return false
    }
}