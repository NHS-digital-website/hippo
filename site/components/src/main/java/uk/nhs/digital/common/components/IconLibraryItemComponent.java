package uk.nhs.digital.common.components;

import static java.lang.String.format;
import static uk.nhs.digital.svg.library.NodeToIconConverter.convert;
import static uk.nhs.digital.svg.library.NodeToIconConverter.urlPathFinder;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.svg.library.Icon;

import java.io.IOException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.RowIterator;

public class IconLibraryItemComponent extends BaseHstComponent {

    private static final Logger log = LoggerFactory.getLogger(IconLibraryItemComponent.class);
    private static final String query = "/jcr:root/content/gallery/website/icons//element(*, hippogallery:imageset)[@hippo:paths='%s']";

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        HstRequestContext requestContext = request.getRequestContext();
        try {
            QueryManager jcrQueryManager = requestContext.getSession().getWorkspace().getQueryManager();
            Query jcrQuery = jcrQueryManager.createQuery(format(query, getTarget(requestContext)), "xpath");
            QueryResult queryResult = jcrQuery.execute();
            request.setAttribute("icon", getIcon(queryResult.getRows(), requestContext, response));
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    private static String getTarget(HstRequestContext requestContext) {
        String[] path = requestContext.getBaseURL().getRequestPath().split("/");
        return path[path.length - 1];
    }

    private static Icon getIcon(RowIterator rowIterator, HstRequestContext requestContext, HstResponse response) throws RepositoryException {
        if (!rowIterator.hasNext()) {
            try {
                response.setStatus(404);
                response.forward("/error/404");
            } catch (IOException e) {
                log.debug("Unable to find error 404 template.", e);
            }
        }
        Node node = rowIterator.nextRow().getNode();
        return convert(node, urlPathFinder.apply(requestContext, node));
    }

}
