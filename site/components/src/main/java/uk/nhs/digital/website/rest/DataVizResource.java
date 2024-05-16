package uk.nhs.digital.website.rest;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.PathUtils;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.ChartSection;

import java.util.List;
import javax.jcr.Node;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Path("/DataViz/")
public class DataVizResource extends BaseRestResource {
    private static final Logger log = LoggerFactory.getLogger(DataVizResource.class);

    @GET
    @Path("/{pageId}/{chartId}")
    public String index(@Context HttpServletRequest request, @PathParam("pageId") String pageId, @PathParam("chartId") String chartId) {
        String chartConfig = null;
        try {
            HstRequestContext requestContext = RequestContextProvider.get();

            String mountContentPath = requestContext.getResolvedMount().getMount().getContentPath();
            Node mountContentNode = requestContext.getSession().getRootNode().getNode(PathUtils.normalizePath(mountContentPath));

            HstQuery hstQuery = HstQueryBuilder.create(mountContentNode)
                .where(constraint("jcr:uuid").equalTo(pageId))
                .build();

            HstQueryResult result = hstQuery.execute();
            HippoBeanIterator iterator = result.getHippoBeans();
            if (!iterator.hasNext()) {
                return null;
            }
            HippoBean document = iterator.nextHippoBean();
            List<ChartSection> sections = document.getChildBeans(ChartSection.class);

            ChartSection section = sections.stream()
                .filter(s -> s.getUniqueId().equals(chartId))
                .findFirst()
                .orElse(null);

            if (section != null) {
                chartConfig = section.getChartConfig();
            }
        } catch (Exception queryException) {
            log.warn("QueryException ", queryException);
        }

        return chartConfig;
    }
}
