package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class OdsComponent extends CommonComponent {

    private static Logger log = LoggerFactory.getLogger(OdsComponent.class);
    private static final String RESOURCE_SPACE_ODS_RESOLVER  = "odsResourceResolver";

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        Resource odsResults;
        final String orgId =  getAnyParameter(request, "orgId");
        final String query =  getAnyParameter(request, REQUEST_PARAM_QUERY);

        if (orgId != null && !orgId.isEmpty()) {
            odsResults = getOrganisationEndpoint(orgId);
        } else {
            odsResults = searchOrganisations(query);
        }

        if (odsResults != null) {
            request.setAttribute("odsResults", odsResults);
        }

    }

    private Resource getOrganisationEndpoint(final String orgId) {

        if (orgId == null) {
            return null;
        }

        Resource odsResources = null;

        try {
            ResourceServiceBroker resourceServiceBroker = CrispHstServices.getDefaultResourceServiceBroker();
            final Map<String, Object> pathVars = new HashMap<>();
            pathVars.put("orgId", orgId);
            odsResources = resourceServiceBroker.findResources(RESOURCE_SPACE_ODS_RESOLVER,
                "/ORD/2-0-0/organisations/{orgId}", pathVars);

        } catch (Exception e) {
            log.warn("Failed to find resources from '{}' resource space, {}.",
                RESOURCE_SPACE_ODS_RESOLVER, orgId);
        }

        return odsResources;
    }

    private Resource searchOrganisations(final String name) {

        Resource odsResources = null;

        if (name == null) {
            return null;
        }

        try {
            ResourceServiceBroker resourceServiceBroker = CrispHstServices.getDefaultResourceServiceBroker();
            final Map<String, Object> pathVars = new HashMap<>();
            pathVars.put("name", name);
            odsResources = resourceServiceBroker.findResources(RESOURCE_SPACE_ODS_RESOLVER,
                "/ORD/2-0-0/organisations?Limit=1000&Name={name}", pathVars);

        } catch (Exception e) {
            log.warn("Failed to find resources from '{}' resource space, {}.",
                RESOURCE_SPACE_ODS_RESOLVER, name);
        }

        return odsResources;
    }


}
