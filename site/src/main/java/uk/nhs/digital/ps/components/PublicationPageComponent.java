package uk.nhs.digital.ps.components;

import static java.util.stream.Collectors.toList;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.EssentialsContentComponent;
import uk.nhs.digital.ps.beans.ChartSection;
import uk.nhs.digital.ps.beans.MapSection;
import uk.nhs.digital.ps.beans.PublicationPage;
import uk.nhs.digital.ps.beans.TextSection;
import uk.nhs.digital.website.beans.*;

import java.util.List;

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
            .map(PublicationPageComponent::getTitle)
            .filter(StringUtils::isNotBlank)
            .collect(toList());
    }

    private static String getTitle(HippoBean section) {
        if (section instanceof Section) {
            return ((Section) section).getTitle();
        }
        if (section instanceof TextSection) {
            return ((TextSection) section).getHeading();
        }
        if (section instanceof ChartSection) {
            return ((ChartSection) section).getTitle();
        }
        if (section instanceof MapSection) {
            return ((MapSection) section).getTitle();
        }
        if (section instanceof IconList) {
            return ((IconList) section).getTitle();
        }
        if (section instanceof GallerySection) {
            return ((GallerySection) section).getTitle();
        }
        if (section instanceof Download) {
            return ((Download) section).getTitle();
        }
        if (section instanceof Expander) {
            return ((Expander) section).getHeading();
        }
        if (section instanceof Infographic) {
            return ((Infographic) section).getHeadline();
        }
        return "";
    }
}
