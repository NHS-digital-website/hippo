package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import org.onehippo.cms7.essentials.components.*;
import org.slf4j.*;

import java.util.*;

public class ServiceComponent extends EssentialsContentComponent {

    private static Logger log = LoggerFactory.getLogger(ServiceComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        Object bean = request.getAttribute(REQUEST_ATTR_DOCUMENT);
        if (bean != null && bean instanceof HippoBean) {
            HippoBean document = (HippoBean) bean;
            HippoBean parentBean = document.getParentBean();
            //getting all the children folders of the parent
            List<HippoFolder> childFolders = parentBean.getChildBeans(HippoFolder.class);
            //retrieving child folders having similar document name (starts with same name)
            Optional<HippoFolder> optFolder =
                childFolders.stream().filter(f -> f.getName().startsWith(document.getName())).findFirst();
            if (optFolder.isPresent()) {
                HippoFolder childFolder = optFolder.get();
                log.debug("Using this children folder: {}", childFolder.getPath());
                List<HippoDocumentBean> childPages = childFolder.getDocuments();
                request.setAttribute("childPages", childPages.subList(0, Math.min(childPages.size(), 6)));
            } else {
                log.debug("Children folder not found based on this name: {}", document.getName());
            }
        } else {
            log.debug("Document missing or not referring to HippoBean {}", bean);
        }
    }
}
