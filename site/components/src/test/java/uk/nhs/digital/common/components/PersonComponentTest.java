package uk.nhs.digital.common.components;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.common.components.info.PersonComponentInfo;
import uk.nhs.digital.website.beans.BusinessUnit;
import uk.nhs.digital.website.beans.CommonFieldsBean;
import uk.nhs.digital.website.beans.JobRole;
import uk.nhs.digital.website.beans.JobRolePicker;
import uk.nhs.digital.website.beans.Person;
import uk.nhs.digital.website.beans.Role;

import java.util.List;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
@PrepareForTest({HstQueryBuilder.class, CommonComponent.class})
public class PersonComponentTest {

    @Mock private HstRequest request;
    @Mock private HstRequestContext requestContext;
    @Mock private HippoBean siteContentBaseBean;
    @Mock private Person personDocument;
    @Mock private Role role;
    @Mock private JobRolePicker jobRolePicker;
    @Mock private JobRole selectedJobRole;
    @Mock private PersonComponentInfo componentInfo;
    @Mock private HstQueryBuilder queryBuilder;
    @Mock private HstQuery query;
    @Mock private HstQueryResult queryResult;
    @Mock private HippoBeanIterator hippoBeanIterator;
    @Mock private BusinessUnit matchingBusinessUnit;
    @Mock private JobRole matchingUnitRole;
    @Mock private BusinessUnit otherBusinessUnit;
    @Mock private JobRole otherUnitRole;

    @Before
    public void setUp() throws Exception {
        openMocks(this);

        when(request.getRequestContext()).thenReturn(requestContext);
        when(requestContext.getContentBean()).thenReturn(personDocument);
        when(requestContext.getSiteContentBaseBean()).thenReturn(siteContentBaseBean);

        when(personDocument.getRoles()).thenReturn(role);
        when(role.getRolepicker()).thenReturn(singletonList(jobRolePicker));
        when(jobRolePicker.getPrimaryrolepicker()).thenReturn(selectedJobRole);
        when(selectedJobRole.getSingleProperty("jcr:uuid")).thenReturn("matching-uuid");

        when(matchingBusinessUnit.getResponsiblerole()).thenReturn(matchingUnitRole);
        when(matchingUnitRole.getSingleProperty("jcr:uuid")).thenReturn("matching-uuid");
        when(otherBusinessUnit.getResponsiblerole()).thenReturn(otherUnitRole);
        when(otherUnitRole.getSingleProperty("jcr:uuid")).thenReturn("different-uuid");

        PowerMockito.mockStatic(HstQueryBuilder.class);
        when(HstQueryBuilder.create(siteContentBaseBean)).thenReturn(queryBuilder);
        when(queryBuilder.where(any())).thenReturn(queryBuilder);
        when(queryBuilder.ofTypes(BusinessUnit.class)).thenReturn(queryBuilder);
        when(queryBuilder.orderByAscending("website:order")).thenReturn(queryBuilder);
        when(queryBuilder.limit(anyInt())).thenReturn(queryBuilder);
        when(queryBuilder.build()).thenReturn(query);
        when(query.execute()).thenReturn(queryResult);
        when(queryResult.getHippoBeans()).thenReturn(hippoBeanIterator);

        PowerMockito.suppress(PowerMockito.constructor(CommonComponent.class));
    }

    @Test
    public void populateBusinessUnits_filtersResultsAndHonoursConfiguredLimit() throws Exception {
        final TestablePersonComponent component = PowerMockito.mock(TestablePersonComponent.class, Answers.CALLS_REAL_METHODS);
        component.setComponentInfo(componentInfo);
        when(componentInfo.getBusinessUnitsLimit()).thenReturn(2);
        when(hippoBeanIterator.hasNext()).thenReturn(true, true, false);
        when(hippoBeanIterator.nextHippoBean()).thenReturn(matchingBusinessUnit, otherBusinessUnit);

        component.populateBusinessUnits(request);

        verify(queryBuilder).limit(2);

        final ArgumentCaptor<List<BusinessUnit>> businessUnitsCaptor = ArgumentCaptor.forClass(List.class);
        verify(request).setAttribute(eq("businessUnits"), businessUnitsCaptor.capture());

        assertThat(businessUnitsCaptor.getValue(), contains(matchingBusinessUnit));
    }

    @Test
    public void populateBusinessUnits_appliesDefaultLimitWhenNoneConfigured() throws Exception {
        final TestablePersonComponent component = PowerMockito.mock(TestablePersonComponent.class, Answers.CALLS_REAL_METHODS);
        component.setComponentInfo(null);
        when(hippoBeanIterator.hasNext()).thenReturn(true, false);
        when(hippoBeanIterator.nextHippoBean()).thenReturn(matchingBusinessUnit);

        component.populateBusinessUnits(request);

        verify(queryBuilder).limit(10);

        final ArgumentCaptor<List<BusinessUnit>> businessUnitsCaptor = ArgumentCaptor.forClass(List.class);
        verify(request).setAttribute(eq("businessUnits"), businessUnitsCaptor.capture());

        assertThat(businessUnitsCaptor.getValue(), contains(matchingBusinessUnit));
    }

    @Test
    public void populateBusinessUnits_setsEmptyListWhenContentBeanIsNotPerson() {
        final TestablePersonComponent component = PowerMockito.mock(TestablePersonComponent.class, Answers.CALLS_REAL_METHODS);
        component.setComponentInfo(componentInfo);
        when(requestContext.getContentBean()).thenReturn(null);

        component.populateBusinessUnits(request);

        final ArgumentCaptor<List<BusinessUnit>> businessUnitsCaptor = ArgumentCaptor.forClass(List.class);
        verify(request).setAttribute(eq("businessUnits"), businessUnitsCaptor.capture());
        assertThat(businessUnitsCaptor.getValue(), empty());
    }

    private static class TestablePersonComponent extends PersonComponent {

        private PersonComponentInfo componentInfo;

        private void setComponentInfo(final PersonComponentInfo componentInfo) {
            this.componentInfo = componentInfo;
        }

        @Override
        protected PersonComponentInfo getComponentParametersInfo(final HstRequest request) {
            return componentInfo;
        }
    }
}
