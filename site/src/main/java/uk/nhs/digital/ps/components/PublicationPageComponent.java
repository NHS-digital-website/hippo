package uk.nhs.digital.ps.components;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import uk.nhs.digital.ps.beans.PublicationPage;
import uk.nhs.digital.ps.beans.TextSection;

import java.util.List;
import java.util.stream.Collectors;

public class PublicationPageComponent extends EssentialsContentComponent {

    private final PageSectionGrouper pageSectionGrouper = new PageSectionGrouper();

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {
        super.doBeforeRender(request, response);
        PublicationPage page = (PublicationPage) request.getRequestContext().getContentBean();

        request.setAttribute("page", page);
        request.setAttribute("index", getIndex(page));
        request.setAttribute("pageSections", pageSectionGrouper.groupSections(page.getBodySections()));
    }

    private List<String> getIndex(PublicationPage page) {
        return page.getBodySections().stream()
            .filter(section -> section instanceof TextSection)
            .map(section -> ((TextSection) section).getHeading())
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toList());
    }
}
