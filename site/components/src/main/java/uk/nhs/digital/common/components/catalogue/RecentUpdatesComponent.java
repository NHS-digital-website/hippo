package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import uk.nhs.digital.common.beans.RecentUpdates;

@ParametersInfo(
    type = RecentUpdatesComponentInfo.class
)
public class RecentUpdatesComponent extends EssentialsListComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        final HstRequestContext ctx = request.getRequestContext();
        RecentUpdates document = (RecentUpdates) ctx.getContentBean();
        request.setAttribute(
                "org.hippoecm.hst.utils.ParameterUtils.parametersInfo",
                new RecentUpdatesComponentInfoImpl(
                    getPageSizeFromDocument(document),
                    getPathFromDocument(document),
                    getIncludeChildrenFromDocument(document),
                    getTypeFromDocument(document)
                )
        );
        super.doBeforeRender(request, response);
        request.setAttribute("document", document);
    }

    private int getPageSizeFromDocument(RecentUpdates document) {
        try {
            return (int) document.getItemsPerPage();
        } catch (Exception e) {
            return 10;
        }
    }

    private String getPathFromDocument(RecentUpdates document) {
        try {
            return document.getPickerPath();
        } catch (Exception e) {
            return "/content/documents/corporate-website/services";
        }
    }

    private String getTypeFromDocument(RecentUpdates document) {
        try {
            return document.getPickerType();
        } catch (Exception e) {
            return "website:service";
        }
    }

    private boolean getIncludeChildrenFromDocument(RecentUpdates document) {
        try {
            return document.getIncludeChildren();
        } catch (Exception e) {
            return true;
        }
    }

}
