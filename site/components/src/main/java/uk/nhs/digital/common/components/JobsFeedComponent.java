package uk.nhs.digital.common.components;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceCollection;
import org.onehippo.cms7.crisp.hst.module.CrispHstServices;
import org.onehippo.cms7.essentials.components.CommonComponent;
import uk.nhs.digital.common.components.info.JobsFeedComponentInfo;
import uk.nhs.digital.model.JobDetails;

import java.util.ArrayList;
import java.util.List;

@ParametersInfo(type = JobsFeedComponentInfo.class)
public class JobsFeedComponent extends CommonComponent {

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
        }

        String vacancyType = paramInfo.getVacancyType();
        if ("Internal only".equalsIgnoreCase(vacancyType)) {
            queryString.append("&IntApp=true");
        } else if ("External only".equalsIgnoreCase(vacancyType)) {
            queryString.append("&ExtApp=true&IntApp=false");
        } else {
            queryString.append("&ExtApp=true&IntApp=true");
        }

        ResourceServiceBroker broker = CrispHstServices.getDefaultResourceServiceBroker(HstServices.getComponentManager());
        Resource data = broker.findResources("eployApi", queryString.toString());
        ResourceCollection vacancies = data.getChildren();

        List<JobDetails> jobDetails = new ArrayList<>();
        for (Resource vacancy : vacancies.getCollection()) {
            JobDetails jobDetail = new JobDetails();

            String title = (String) ((Resource) vacancy.getValueMap().get("Title")).getDefaultValue();
            String location = (String) ((Resource) vacancy.getValueMap().get("Location")).getDefaultValue();
            String displaySalary = (String) ((Resource) vacancy.getValueMap().get("DisplaySalary")).getDefaultValue();
            String link = (String) ((Resource) vacancy.getValueMap().get("Link")).getDefaultValue();

            jobDetail.setTitle(title);
            jobDetail.setLocation(location);
            jobDetail.setSalary(displaySalary);
            jobDetail.setLink(link);
            jobDetails.add(jobDetail);
        }
        request.setAttribute("jobList", jobDetails);
    }

}
