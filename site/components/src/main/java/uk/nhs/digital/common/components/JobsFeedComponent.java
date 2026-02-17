package uk.nhs.digital.common.components;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceCollection;
import org.onehippo.cms7.crisp.api.resource.ResourceException;
import org.onehippo.cms7.crisp.api.resource.ValueMap;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.JobsFeedComponentInfo;
import uk.nhs.digital.model.JobDetails;

import java.util.ArrayList;
import java.util.List;

@ParametersInfo(type = JobsFeedComponentInfo.class)
public class JobsFeedComponent extends CommonComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobsFeedComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        final JobsFeedComponentInfo paramInfo = getComponentParametersInfo(request);

        String button1Text = paramInfo.getButton1Text();
        String button1Url = paramInfo.getButton1Url();
        String button2Text = paramInfo.getButton2Text();
        String button2Url = paramInfo.getButton2Url();
        String feedHeader = paramInfo.getHeader();

        request.setAttribute("feedHeader", feedHeader);
        request.setAttribute("button1Text", button1Text);
        request.setAttribute("button2Text", button2Text);
        request.setAttribute("button1Url", button1Url);
        request.setAttribute("button2Url", button2Url);

        String feedUrl = paramInfo.getFeedMasterUri();
        String recordLimit = paramInfo.getNumToDisplay();
        String keywordRule = paramInfo.getKeywordRule();
        String keywords = paramInfo.getKeywords();

        StringBuilder queryString = new StringBuilder(feedUrl);
        queryString.append("?Format=xml");
        queryString.append("&recordLimit=" + (StringUtils.isNotBlank(recordLimit) ? recordLimit : 4));
        if (StringUtils.isNotBlank(keywords)) {
            queryString.append("&Keywords=" + keywords);
            queryString.append("&KeywordSearchType=" + (StringUtils.isNotBlank(keywordRule) ? keywordRule : "ALL"));
        }
        String postcode = paramInfo.getPostcode();
        if (StringUtils.isNotBlank(postcode)) {
            queryString.append("&Postcode=" + postcode);
            queryString.append("&Radius=30");
        }

        String vacancyType = paramInfo.getVacancyType();
        if ("Internal only".equalsIgnoreCase(vacancyType)) {
            queryString.append("&IntApp=true");
        } else if ("External only".equalsIgnoreCase(vacancyType)) {
            queryString.append("&ExtApp=true&IntApp=false");
        } else {
            queryString.append("&ExtApp=true&IntApp=true");
        }

        List<JobDetails> jobDetails = new ArrayList<>();
        request.setAttribute("jobList", jobDetails);

        ResourceServiceBroker broker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        if (broker == null) {
            LOGGER.debug("No ResourceServiceBroker configured; skipping jobs feed call.");
            return;
        }

        String requestUri = queryString.toString();
        final Resource data;
        try {
            data = broker.findResources("eployApi", requestUri);
        } catch (ResourceException ex) {
            LOGGER.debug("Failed to retrieve jobs feed from {}.", requestUri, ex);
            return;
        }

        if (data == null) {
            return;
        }

        ResourceCollection vacancies = data.getChildren();
        if (vacancies == null || vacancies.getCollection() == null) {
            return;
        }

        for (Resource vacancy : vacancies.getCollection()) {
            if (vacancy == null) {
                continue;
            }
            JobDetails jobDetail = new JobDetails();
            jobDetail.setTitle(getFieldValue(vacancy, "Title"));
            jobDetail.setLocation(getFieldValue(vacancy, "Location"));
            jobDetail.setSalary(getFieldValue(vacancy, "DisplaySalary"));
            jobDetail.setLink(getFieldValue(vacancy, "Link"));
            jobDetails.add(jobDetail);
        }
    }

    private String getFieldValue(Resource vacancy, String fieldName) {
        if (vacancy == null) {
            return null;
        }
        ValueMap valueMap = vacancy.getValueMap();
        if (valueMap == null) {
            return null;
        }
        Object fieldValue = valueMap.get(fieldName);
        if (fieldValue instanceof Resource) {
            Object defaultValue = ((Resource) fieldValue).getDefaultValue();
            return defaultValue != null ? defaultValue.toString() : null;
        }
        return fieldValue != null ? fieldValue.toString() : null;
    }
}
