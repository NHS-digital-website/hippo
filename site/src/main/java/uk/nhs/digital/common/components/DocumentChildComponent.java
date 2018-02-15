package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import org.onehippo.cms7.essentials.components.*;
import org.slf4j.*;

import java.util.*;

public class DocumentChildComponent extends EssentialsContentComponent {

    private static Logger log = LoggerFactory.getLogger(DocumentChildComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        Object bean = request.getAttribute(REQUEST_ATTR_DOCUMENT);
        if (bean != null && bean instanceof HippoBean) {
            HippoBean document = (HippoBean) bean;
            HippoBean parentBean = document.getParentBean();
            //getting all the children folders of the parent
            List<HippoFolder> childFolders = parentBean.getChildBeans(HippoFolder.class);
            List<HippoDocumentBean> childPages = new ArrayList<>();
            for (HippoFolder childFolder : childFolders) {
                //retrieving child documents having name "content"
                Optional<HippoDocumentBean> optDocument =
                    childFolder.getDocuments().stream().filter(childDocument -> childDocument.getName().equals("content")).findFirst();
                //in case the childFolder contains a document with the same name, include it in the childPages
                if (optDocument.isPresent()) {
                    childPages.add(optDocument.get());
                }
            }
            request.setAttribute("childPages", childPages.subList(0, Math.min(childPages.size(), 6)));
        } else {
            log.debug("Document missing or not referring to HippoBean {}", bean);
        }
    }
}
