package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import org.onehippo.cms7.essentials.components.*;
import org.slf4j.*;

import java.util.*;

public class DocumentChildComponent extends EssentialsContentComponent {

    private static Logger log = LoggerFactory.getLogger(DocumentChildComponent.class);

    public List<HippoDocumentBean> getChildrenDocuments(HippoBean bean) {
        HippoBean parentBean = bean.getParentBean();
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
        return childPages;
    }

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        Object bean = request.getAttribute(REQUEST_ATTR_DOCUMENT);
        if (bean != null && bean instanceof HippoBean) {
            List<HippoDocumentBean> childPages = getChildrenDocuments((HippoBean) bean);
            request.setAttribute("childPages", childPages.subList(0, Math.min(childPages.size(), 6)));
        } else {
            log.debug("Document missing or not referring to HippoBean {}", bean);
        }
    }
}
