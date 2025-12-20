package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsDocumentComponent;
import uk.nhs.digital.common.components.info.TargetedBannerComponentInfo;
import uk.nhs.digital.website.beans.TargetedBanner;


@ParametersInfo(type = TargetedBannerComponentInfo.class)
public class TargetedBannerComponent extends EssentialsDocumentComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        final TargetedBanner targetedBanner = request.getModel("document");

        if (targetedBanner != null) {

            // Get the current page path
            String currentPath = request.getRequestContext().getResolvedSiteMapItem().getPathInfo();
        
            // Find the first banner targeting this path
            if (targetedBanner.getTargetPaths().stream().anyMatch(path -> {
                String normalizedPath = path.length() <= 2 ? "(.*)?" : path.replace(".", "\\.").replaceAll("^/|/$", "").replaceAll("(/?\\*)$","(/.*)?");
                String regex = "^" + normalizedPath + "$";
                return currentPath.matches(regex);
            })) {

                request.setAttribute("banner", targetedBanner); 
            }
        }
    }
}
