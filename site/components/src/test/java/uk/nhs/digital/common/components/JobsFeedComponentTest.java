package uk.nhs.digital.common.components;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.hippoecm.hst.mock.core.container.MockComponentManager;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.onehippo.cms7.crisp.api.broker.ResourceServiceBroker;
import org.onehippo.cms7.crisp.api.resource.Resource;
import org.onehippo.cms7.crisp.api.resource.ResourceCollection;
import org.onehippo.cms7.crisp.api.resource.ValueMap;
import org.onehippo.cms7.crisp.core.resource.ListResourceCollection;
import org.onehippo.cms7.crisp.mock.module.MockCrispHstServices;
import uk.nhs.digital.common.components.info.JobsFeedComponentInfo;
import uk.nhs.digital.model.JobDetails;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class JobsFeedComponentTest {

    private MockHstRequest request;
    private MockHstResponse response;
    private MockHstRequestContext requestContext;

    @Mock
    private ResourceServiceBroker resourceServiceBroker;

    @Before
    public void setUp() {
        HstServices.setComponentManager(new MockComponentManager());
        requestContext = new MockHstRequestContext();
        ModifiableRequestContextProvider.set(requestContext);
        MockCrispHstServices.setDefaultResourceServiceBroker(resourceServiceBroker);
        request = new MockHstRequest();
        response = new MockHstResponse();
    }

    @After
    public void tearDown() {
        ModifiableRequestContextProvider.clear();
    }

    @Test
    public void doBeforeRenderPopulatesJobsAndButtonsFromFeedResponse() {
        Resource vacancy = mock(Resource.class);
        ValueMap valueMap = mock(ValueMap.class);
        when(vacancy.getValueMap()).thenReturn(valueMap);
        when(valueMap.get("Title")).thenReturn(resourceWithDefault("Clinical Lead"));
        when(valueMap.get("Location")).thenReturn(resourceWithDefault("Leeds"));
        when(valueMap.get("DisplaySalary")).thenReturn(resourceWithDefault("£80k"));
        when(valueMap.get("Link")).thenReturn(resourceWithDefault("https://example/job"));

        Resource dataResource = mock(Resource.class);
        ResourceCollection resourceCollection = new ListResourceCollection(Collections.singletonList(vacancy));
        when(dataResource.getChildren()).thenReturn(resourceCollection);
        when(resourceServiceBroker.findResources(anyString(), anyString())).thenReturn(dataResource);

        JobsFeedComponentInfo componentInfo = new StubJobsFeedComponentInfo();
        TestJobsFeedComponent component = new TestJobsFeedComponent(componentInfo);
        component.doBeforeRender(request, response);

        assertThat(request.getAttribute("feedHeader"), is(equalTo(componentInfo.getHeader())));
        assertThat(request.getAttribute("button1Text"), is(equalTo(componentInfo.getButton1Text())));
        assertThat(request.getAttribute("button2Text"), is(equalTo(componentInfo.getButton2Text())));
        assertThat(request.getAttribute("button1Url"), is(equalTo(componentInfo.getButton1Url())));
        assertThat(request.getAttribute("button2Url"), is(equalTo(componentInfo.getButton2Url())));

        Object jobsAttribute = request.getAttribute("jobList");
        assertThat(jobsAttribute, is(notNullValue()));
        assertTrue(jobsAttribute instanceof List);
        List<JobDetails> jobDetails = (List<JobDetails>) jobsAttribute;
        assertEquals(1, jobDetails.size());
        JobDetails job = jobDetails.get(0);
        assertThat(job.getTitle(), is(equalTo("Clinical Lead")));
        assertThat(job.getLocation(), is(equalTo("Leeds")));
        assertThat(job.getSalary(), is(equalTo("£80k")));
        assertThat(job.getLink(), is(equalTo("https://example/job")));
    }

    @Test
    public void doBeforeRenderDoesNotThrowWhenFeedOmitsOptionalFields() {
        Resource vacancy = mock(Resource.class);
        ValueMap missingValues = mock(ValueMap.class);
        when(vacancy.getValueMap()).thenReturn(missingValues);
        when(missingValues.get(anyString())).thenReturn(null);

        Resource dataResource = mock(Resource.class);
        when(dataResource.getChildren()).thenReturn(new ListResourceCollection(Collections.singletonList(vacancy)));
        when(resourceServiceBroker.findResources(anyString(), anyString())).thenReturn(dataResource);

        JobsFeedComponentInfo componentInfo = new StubJobsFeedComponentInfo();
        TestJobsFeedComponent component = new TestJobsFeedComponent(componentInfo);
        component.doBeforeRender(request, response);

        Object jobsAttribute = request.getAttribute("jobList");
        assertThat(jobsAttribute, is(notNullValue()));
    }

    private Resource resourceWithDefault(String defaultValue) {
        return new TestValueResource(defaultValue);
    }

    private static class TestJobsFeedComponent extends JobsFeedComponent {
        private final JobsFeedComponentInfo info;

        TestJobsFeedComponent(JobsFeedComponentInfo info) {
            this.info = info;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected <T> T getComponentParametersInfo(HstRequest request) {
            return (T) info;
        }
    }

    private static class StubJobsFeedComponentInfo implements JobsFeedComponentInfo {

        @Override
        public String getHeader() {
            return "Job Listings";
        }

        @Override
        public String getNumToDisplay() {
            return "4";
        }

        @Override
        public String getButton1Text() {
            return "View all";
        }

        @Override
        public String getButton1Url() {
            return "https://example/button-1";
        }

        @Override
        public String getButton2Text() {
            return "Apply";
        }

        @Override
        public String getButton2Url() {
            return "https://example/button-2";
        }

        @Override
        public String getFeedMasterUri() {
            return "https://example/api";
        }

        @Override
        public String getVacancyType() {
            return "Internal and external";
        }

        @Override
        public String getKeywords() {
            return "NHS";
        }

        @Override
        public String getKeywordRule() {
            return "All";
        }

        @Override
        public String getPostcode() {
            return null;
        }
    }

    private static class TestValueResource implements Resource {
        private static final long serialVersionUID = 1L;

        private final Object defaultValue;

        private TestValueResource(Object defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public Object getDefaultValue() {
            return defaultValue;
        }

        @Override
        public <T> T getDefaultValue(Class<T> type) {
            return type.cast(defaultValue);
        }

        @Override
        public String getResourceType() {
            return null;
        }

        @Override
        public boolean isResourceType(String resourceType) {
            return false;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getPath() {
            return null;
        }

        @Override
        public ValueMap getMetadata() {
            return null;
        }

        @Override
        public ValueMap getValueMap() {
            return null;
        }

        @Override
        public Object getValue(String name) {
            return null;
        }

        @Override
        public <T> T getValue(String name, Class<T> type) {
            return null;
        }

        @Override
        public Resource getParent() {
            return null;
        }

        @Override
        public boolean isAnyChildContained() {
            return false;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @Override
        public long getChildCount() {
            return 0;
        }

        @Override
        public ResourceCollection getChildren() {
            return ResourceCollection.EMPTY;
        }

        @Override
        public ResourceCollection getChildren(long from, long to) {
            return ResourceCollection.EMPTY;
        }

        @Override
        public Object getNodeData() {
            return null;
        }
    }
}
