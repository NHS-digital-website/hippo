package uk.nhs.digital.common.components;

import org.hippoecm.hst.container.*;
import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.manager.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.request.*;
import org.slf4j.*;
import uk.nhs.digital.website.beans.*;

import java.util.*;
import javax.jcr.*;

public class DocumentChildComponent extends BaseGaContentComponent {

    private static Logger log = LoggerFactory.getLogger(DocumentChildComponent.class);

    public List getRelatedDocuments(HippoBean bean) {
        List<HippoBean> relatedDocuments = new ArrayList<>();
        HstRequestContext context = RequestContextProvider.get();
        ObjectConverter objectConverter = context.getContentBeansTool().getObjectConverter();
        //this 'generic' method assumes that the all the document types involved (like Hub, Services and General) are using website:items as content block field name
        for (HippoCompound item : bean.getChildBeansByName("website:items", HippoCompound.class)) {
            if (item instanceof Internallink) {
                //getting the docbase property of the link node
                Optional docbase = item.getChildBeansByName("website:link", HippoBean.class)
                    .stream().map(internallink -> internallink.getProperty("hippo:docbase")).findFirst();

                if (docbase.isPresent()) {
                    try {
                        //using the ObjectConverter to get the bean version of that node
                        final Object document = objectConverter.getObject(context.getSession().getNodeByIdentifier((String) docbase.get()));
                        if (document instanceof HippoBean) {
                            relatedDocuments.add((HippoBean) document);
                        }
                    } catch (RepositoryException repositoryEx) {
                        log.warn("Repository exception while fetching child nodes", repositoryEx);
                    } catch (ObjectBeanManagerException beanManagerEx) {
                        log.warn("Bean manager exception while converting linked node", beanManagerEx);
                    }
                }
            } else if (item instanceof Externallink || item instanceof Assetlink) {
                relatedDocuments.add(item);
            }
        }
        return relatedDocuments;
    }

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        Object bean = request.getAttribute(REQUEST_ATTR_DOCUMENT);
        if (bean != null && bean instanceof HippoBean) {
            List<HippoDocumentBean> childPages = getRelatedDocuments((HippoBean) bean);
            request.setAttribute("childPages", childPages);
        } else {
            log.debug("Document missing or not referring to HippoBean {}", bean);
        }
    }
}
