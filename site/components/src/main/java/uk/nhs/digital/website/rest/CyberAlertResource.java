package uk.nhs.digital.website.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.hippoecm.hst.container.*;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryManager;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.util.PathUtils;
import org.json.simple.JSONObject;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.onehippo.cms7.essentials.components.rest.ctx.DefaultRestContext;
import org.onehippo.cms7.essentials.components.rest.ctx.RestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.website.beans.CyberAcknowledgement;
import uk.nhs.digital.website.beans.CyberAlert;
import uk.nhs.digital.website.beans.ThreatIdDate;
import uk.nhs.digital.website.beans.ThreatIds;

import java.io.IOException;
import java.util.*;
import javax.jcr.Node;

/**
 * @version "$Id$"
 */

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/CyberAlert/")
public class CyberAlertResource extends BaseRestResource {

    private static final Logger log = LoggerFactory.getLogger(CyberAlertResource.class);

    @Override
    public HstQuery createQuery(final RestContext context, final Class<? extends HippoBean> clazz, final Subtypes subtypes) {
        final HstQuery query = super.createQuery(context, clazz, subtypes);
        query.addOrderByDescending("hippostdpubwf:lastModificationDate");
        return query;
    }

    @GET
    @Path("/")
    public Pageable<CyberAlert> index(@Context HttpServletRequest request) {
        return findBeans(new DefaultRestContext(this, request), CyberAlert.class);
    }

    @GET
    @Path("/getAllThreatIds/")
    public ThreatIds fetchAllThreatIds(@Context HttpServletRequest request, @Context HttpServletResponse servletResponse) {

        ThreatIds threatId = new ThreatIds();
        List<ThreatIdDate> threatIdDateList = new ArrayList<ThreatIdDate>();

        try {
            final HstQuery query = createQuery(new DefaultRestContext(this, request), CyberAlert.class, Subtypes.INCLUDE);
            query.setLimit(100);
            final HstQueryResult result = query.execute();

            HippoBeanIterator iterator = result.getHippoBeans();
            while (iterator.hasNext()) {
                CyberAlert cyberAlert = (CyberAlert) iterator.nextHippoBean();
                List<Calendar> calList = new ArrayList<Calendar>();
                ThreatIdDate threDate = new ThreatIdDate();

                if (cyberAlert != null) {
                    List<HippoBean> cyberAcknowledgementList = (List<HippoBean>) cyberAlert.getCyberAcknowledgements();
                    for (HippoBean cyberAckn : cyberAcknowledgementList) {
                        if (cyberAckn instanceof CyberAcknowledgement) {
                            CyberAcknowledgement cybAck = (CyberAcknowledgement) cyberAckn;
                            calList.add(cybAck.getResponseDatetime());
                        }
                    }
                    threDate.setResponsedates(calList);
                    threDate.setThreatid(cyberAlert.getThreatId());
                }
                threatIdDateList.add(threDate);
            }
        } catch (Exception e) {
            log.error("Error finding beans", e);
        }
        threatId.setThreatids(threatIdDateList);
        return threatId;
    }

    @GET
    @Path("/page/")
    public Pageable<CyberAlert> fetchPage(@Context HttpServletRequest servletRequest, @Context HttpServletResponse servletResponse, @PathParam("page") int page) {
        if (servletRequest.getParameter("page") != null) {
            page = Integer.parseInt(servletRequest.getParameter("page"));
        }
        return findBeans(new DefaultRestContext(this, servletRequest, page, DefaultRestContext.PAGE_SIZE), CyberAlert.class);
    }

    @GET
    @Path("/single/")
    public CyberAlert fetchCyberAlert(@Context HttpServletRequest servletRequest,
                                      @Context HttpServletResponse servletResponse, @Context UriInfo uriInfo, @PathParam("threatid") String threatid) {

        CyberAlert cyberAlert = null;
        try {
            threatid = servletRequest.getParameter("threatid");

            if (threatid != null) {
                HstRequestContext requestContext = RequestContextProvider.get();
                HstQueryManager hstQueryManager = getHstQueryManager(requestContext.getSession(), requestContext);

                String mountContentPath = requestContext.getResolvedMount().getMount().getContentPath();

                Node mountContentNode = requestContext.getSession().getRootNode().getNode(PathUtils.normalizePath(mountContentPath));

                HstQuery hstQuery = hstQueryManager.createQuery(mountContentNode, CyberAlert.class);

                Filter filter = hstQuery.createFilter();
                filter.addEqualTo("website:threatid", threatid);
                hstQuery.setFilter(filter);
                hstQuery.setLimit(1);

                HstQueryResult result = hstQuery.execute();
                HippoBeanIterator iterator = result.getHippoBeans();

                if (iterator.hasNext()) {
                    cyberAlert = (CyberAlert) iterator.nextHippoBean();
                } else {

                    JSONObject json = new JSONObject();
                    json.put("error", "The threatid=" + threatid + " is not found");

                    setServletResponse(servletResponse, json);
                }

            } else {

                JSONObject json = new JSONObject();
                json.put("error", "The URL is not correct. Use /single?threatid=<threatid>");

                setServletResponse(servletResponse, json);
            }

        } catch (Exception queryException) {
            log.warn("QueryException ", queryException);
        }
        return cyberAlert;
    }

    private void setServletResponse(HttpServletResponse servletResponse, JSONObject json) throws IOException {
        servletResponse.resetBuffer();
        servletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        servletResponse.setHeader("Content-Type", "application/json");
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.getWriter().write(json.toString());
        servletResponse.flushBuffer();
    }
}
