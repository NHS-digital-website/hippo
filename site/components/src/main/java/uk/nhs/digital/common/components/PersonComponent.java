package uk.nhs.digital.common.components;

import static org.hippoecm.hst.content.beans.query.builder.ConstraintBuilder.constraint;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.forge.selection.hst.contentbean.ValueList;
import org.onehippo.forge.selection.hst.util.SelectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.PersonComponentInfo;
import uk.nhs.digital.website.beans.BusinessUnit;
import uk.nhs.digital.website.beans.CommonFieldsBean;
import uk.nhs.digital.website.beans.JobRole;
import uk.nhs.digital.website.beans.JobRolePicker;
import uk.nhs.digital.website.beans.Person;
import uk.nhs.digital.website.beans.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ParametersInfo(type = PersonComponentInfo.class)
public class PersonComponent extends ContentRewriterComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonComponent.class);
    private static final int DEFAULT_BUSINESS_UNIT_LIMIT = 10;

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final ValueList suppressdataValueList =
            SelectionUtil.getValueListByIdentifier("suppressdata", RequestContextProvider.get());
        if (suppressdataValueList != null) {
            request.setAttribute("suppressdata", SelectionUtil.valueListAsMap(suppressdataValueList));
        }

        final ValueList imagedistributiontaggingValueList =
            SelectionUtil.getValueListByIdentifier("imagedistributiontagging", RequestContextProvider.get());
        if (imagedistributiontaggingValueList != null) {
            request.setAttribute("imagedistributiontagging", SelectionUtil.valueListAsMap(imagedistributiontaggingValueList));
        }

        final ValueList imagesourcepermissionValueList =
            SelectionUtil.getValueListByIdentifier("imagesourcepermission", RequestContextProvider.get());
        if (imagesourcepermissionValueList != null) {
            request.setAttribute("imagesourcepermission", SelectionUtil.valueListAsMap(imagedistributiontaggingValueList));
        }

        populateBusinessUnits(request);
    }

    void populateBusinessUnits(final HstRequest request) {
        final HippoBean contentBean = request.getRequestContext().getContentBean();
        if (!(contentBean instanceof Person)) {
            request.setAttribute("businessUnits", Collections.emptyList());
            return;
        }

        final PersonComponentInfo componentInfo = getComponentParametersInfo(request);
        final int limit = componentInfo != null && componentInfo.getBusinessUnitsLimit() > 0
            ? componentInfo.getBusinessUnitsLimit()
            : DEFAULT_BUSINESS_UNIT_LIMIT;

        final List<BusinessUnit> businessUnits = loadBusinessUnits((Person) contentBean, limit, request);
        request.setAttribute("businessUnits", businessUnits);
    }

    private List<BusinessUnit> loadBusinessUnits(final Person person, final int limit, final HstRequest request) {
        final Role role = person.getRoles();
        if (role == null) {
            return Collections.emptyList();
        }

        try {
            final HstQuery query = HstQueryBuilder.create(request.getRequestContext().getSiteContentBaseBean())
                .where(constraint("website:responsiblerole").notEqualTo(null))
                .ofTypes(BusinessUnit.class)
                .orderByAscending("website:order")
                .limit(limit)
                .build();

            final HippoBeanIterator iterator = query.execute().getHippoBeans();
            final List<BusinessUnit> businessUnits = new ArrayList<>();

            while (iterator.hasNext()) {
                final HippoBean hippoBean = iterator.nextHippoBean();
                if (hippoBean instanceof BusinessUnit) {
                    businessUnits.add((BusinessUnit) hippoBean);
                }
            }
            if (businessUnits.isEmpty()) {
                return Collections.emptyList();
            }

            return filterBusinessUnits(role, businessUnits);
        } catch (QueryException queryException) {
            LOGGER.error("Failed to load Business Units for Person {}", person.getIdentifier(), queryException);
            return Collections.emptyList();
        }
    }

    private List<BusinessUnit> filterBusinessUnits(final Role role, final List<BusinessUnit> businessUnits) {
        final List<BusinessUnit> filteredUnits = new ArrayList<>();
        final List<HippoBean> rolePickers = role.getRolepicker();

        if (rolePickers == null || rolePickers.isEmpty()) {
            return filteredUnits;
        }

        for (BusinessUnit unit : businessUnits) {
            final JobRole unitRole = (JobRole) unit.getResponsiblerole();
            if (unitRole == null || unitRole.getSingleProperty("jcr:uuid") == null) {
                continue;
            }

            for (HippoBean picker : rolePickers) {
                if (!(picker instanceof JobRolePicker)) {
                    continue;
                }

                final JobRolePicker jobRolePicker = (JobRolePicker) picker;
                final CommonFieldsBean pickedRole = (CommonFieldsBean) jobRolePicker.getPrimaryrolepicker();
                if (pickedRole == null || pickedRole.getSingleProperty("jcr:uuid") == null) {
                    continue;
                }

                if (unitRole.getSingleProperty("jcr:uuid").toString().equals(pickedRole.getSingleProperty("jcr:uuid").toString())) {
                    filteredUnits.add(unit);
                    break;
                }
            }
        }

        return filteredUnits;
    }
}
