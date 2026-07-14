package uk.nhs.digital.common.components;

import static uk.nhs.digital.svg.library.NodeToIconConverter.convert;
import static uk.nhs.digital.svg.library.NodeToIconConverter.urlPathFinder;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.svg.library.Icon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.RowIterator;

public class IconLibraryComponent extends BaseHstComponent {

    private static final Logger log = LoggerFactory.getLogger(IconLibraryComponent.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        HstRequestContext requestContext = request.getRequestContext();
        try {
            QueryManager jcrQueryManager = requestContext.getSession().getWorkspace().getQueryManager();
            Query jcrQuery = jcrQueryManager.createQuery("/jcr:root/content/gallery/website/icons//element(*, hippogallery:imageset)[jcr:contains(., '.svg/')]", "xpath");
            QueryResult queryResult = jcrQuery.execute();
            request.setAttribute("icons", buildListOfIcons(queryResult.getRows(), requestContext, getSanitisedSearchTerm(request)));
            request.setAttribute("requestContext", requestContext);
        } catch (RepositoryException e) {
            log.error("Failed to load icon library icons", e);
        }
    }

    private static String getSanitisedSearchTerm(final HstRequest request) {
        final String searchTerm = request.getRequestContext().getServletRequest().getParameter("icon-search");

        return SearchInputParsingUtils.parse(searchTerm, false);
    }

    private static List<Icon> buildListOfIcons(RowIterator rowIterator, HstRequestContext requestContext, String search) throws RepositoryException {
        List<Icon> icons = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Node node = rowIterator.nextRow().getNode();
            if (search == null || search.isEmpty()) {
                icons.add(convert(node, urlPathFinder.apply(requestContext, node)));
            } else {
                Icon icon = convert(node, urlPathFinder.apply(requestContext, node));
                List<String> keywords = Stream.concat(
                    icon.getKeywords().stream(),
                    Arrays.stream(icon.getName().substring(0, icon.getName().length() - 4).split("-|\\s"))
                ).collect(Collectors.toList());
                if (keywordsContainsWord(keywords, search)) {
                    icons.add(icon);
                }
            }
        }
        return icons;
    }

    public static boolean keywordsContainsWord(List<String> items, String word) {
        for (String item : items) {
            if (!item.isEmpty() && word.toLowerCase().contains(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
