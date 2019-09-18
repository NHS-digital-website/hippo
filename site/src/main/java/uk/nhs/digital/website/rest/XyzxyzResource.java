package uk.nhs.digital.website.rest;

import org.hippoecm.hst.content.annotations.Persistable;
import org.hippoecm.hst.content.beans.manager.workflow.WorkflowPersistenceManager;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.onehippo.cms7.essentials.components.rest.BaseRestResource;
import org.onehippo.cms7.essentials.components.rest.ctx.DefaultRestContext;
import uk.nhs.digital.website.beans.Xyzxyz;
import uk.nhs.digital.website.beans.XyzxyzRepresentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;


@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED})
@Path("/Xyzxyz/")
public class XyzxyzResource extends BaseRestResource {

    @GET
    @Path("/")
    public Pageable<Xyzxyz> index(@Context HttpServletRequest request) {
        System.out.println("-----------index-----------------------");
        return findBeans(new DefaultRestContext(this, request), Xyzxyz.class);
    }


    @Persistable
    @Path("/testing/")
    @POST
    public XyzxyzRepresentation createXyzxyzResources(@Context HttpServletRequest servletRequest, @Context HttpServletResponse servletResponse, @Context UriInfo uriInfo,
        XyzxyzRepresentation xyzxyzRepresentation) {

        HstRequestContext requestContext = getRequestContext(servletRequest);

        try {
            WorkflowPersistenceManager wpm = (WorkflowPersistenceManager) getPersistenceManager(requestContext);
            HippoFolderBean contentBaseFolder = getMountContentBaseBean(requestContext);
            String productFolderPath = contentBaseFolder.getPath() + "/XYZXYZ";
            String beanPath = wpm.createAndReturn(productFolderPath, "website:xyzxyz", xyzxyzRepresentation.getDocumentName(), true);

            Xyzxyz instanceOfXyzxyz = (Xyzxyz) wpm.getObject(beanPath);
            instanceOfXyzxyz.setName(xyzxyzRepresentation.getName());
            instanceOfXyzxyz.setDescription(xyzxyzRepresentation.getDescription());
            instanceOfXyzxyz.setApiMethod(xyzxyzRepresentation.getApiMethod());
            wpm.update(instanceOfXyzxyz);
            wpm.save();

            instanceOfXyzxyz = (Xyzxyz) wpm.getObject(instanceOfXyzxyz.getPath());
            xyzxyzRepresentation = new XyzxyzRepresentation().represent(instanceOfXyzxyz);

        } catch (Exception e) {
            System.out.println(e);
        }
        return xyzxyzRepresentation;
    }

}
