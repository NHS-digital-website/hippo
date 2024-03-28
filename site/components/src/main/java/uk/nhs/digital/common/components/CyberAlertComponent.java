package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.builder.HstQueryBuilder;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.common.components.info.CyberAlertComponentInfo;
import uk.nhs.digital.website.beans.CyberAlert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@ParametersInfo(type = CyberAlertComponentInfo.class)
public class CyberAlertComponent extends CommonComponent {

    private static Logger LOGGER = LoggerFactory.getLogger(CyberAlertComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        final CyberAlertComponentInfo componentParametersInfo = getComponentParametersInfo(request);
        final int configuredAlertSize = componentParametersInfo.getNumberOfAlertsToDisplay();
        request.setAttribute("title", componentParametersInfo.getTitle());
        try {

            final HippoBean baseContentBean = request.getRequestContext().getSiteContentBaseBean();
            final HippoBean cyberAlertScope = (HippoBean) request.getRequestContext().getObjectBeanManager().getObject(baseContentBean.getPath() + "/cyber-alerts");

            HstQueryBuilder builder = HstQueryBuilder.create(cyberAlertScope);
            HstQueryResult alertsQueryResult = builder.ofTypes(CyberAlert.class)
                .orderByDescending("publicationsystem:NominalDate")
                .limit(componentParametersInfo.getNumberOfAlertsToDisplay())
                .build()
                .execute();

            List<CyberAlert> alertsListToDisplay;
            if (alertsQueryResult != null && configuredAlertSize > 0) {
                alertsListToDisplay = createCyberAlertsList(configuredAlertSize, alertsQueryResult);
                request.setAttribute("cyberAlertList", alertsListToDisplay);
            }

        } catch (QueryException | ObjectBeanManagerException e) {
            LOGGER.error("Failed to execute Cyber Alerts Query ", e);
        }

    }

    private List<CyberAlert> createCyberAlertsList(final int configuredAlertSize, final HstQueryResult alertsQueryResult) {
        Calendar twoWeekAgo = Calendar.getInstance();
        twoWeekAgo.add(Calendar.WEEK_OF_YEAR, -2);

        HippoBeanIterator allAlerts = alertsQueryResult.getHippoBeans();
        final List<CyberAlert> severeWithinTwoWeeks = new ArrayList<>();
        final List<CyberAlert> allOtherAlerts = new ArrayList<>();
        while (allAlerts.hasNext()) {
            CyberAlert cyberAlert = (CyberAlert) allAlerts.next();
            if (cyberAlert.getSeverity() != null && cyberAlert.getPublishedDate() != null) {
                if (cyberAlert.getSeverity().equals("High") && cyberAlert.getPublishedDate().getTimeInMillis() > twoWeekAgo.getTimeInMillis()) {
                    severeWithinTwoWeeks.add(cyberAlert);
                } else {
                    allOtherAlerts.add(cyberAlert);
                }
            }
        }

        List<CyberAlert> cyberAlertsList;
        if (severeWithinTwoWeeks.size() > configuredAlertSize) {
            cyberAlertsList = severeWithinTwoWeeks.subList(0, configuredAlertSize);
        } else {
            cyberAlertsList = severeWithinTwoWeeks;
        }

        if (cyberAlertsList.size() < configuredAlertSize) {
            int numberOfAlertsToAdd = configuredAlertSize - cyberAlertsList.size();
            for (int i = 0; i < numberOfAlertsToAdd && i < allOtherAlerts.size(); ) {
                cyberAlertsList.add(allOtherAlerts.get(i++));
            }
        }
        return cyberAlertsList;
    }

}

