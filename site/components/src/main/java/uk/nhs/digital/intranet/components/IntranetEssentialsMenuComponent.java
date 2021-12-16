package uk.nhs.digital.intranet.components;

import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.sitemenu.HstSiteMenu;
import org.hippoecm.hst.core.sitemenu.HstSiteMenuItem;
import org.onehippo.cms7.essentials.components.EssentialsMenuComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsMenuComponentInfo;
import uk.nhs.digital.website.beans.SocialMediaLink;

import java.util.HashMap;
import java.util.Map;

@ParametersInfo(
    type = EssentialsMenuComponentInfo.class
)
public class IntranetEssentialsMenuComponent extends EssentialsMenuComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        Map<String, SocialMediaLink> socialMediaLinks = new HashMap<>();
        HstSiteMenu menu = (HstSiteMenu) request.getAttribute("menu");
        if (menu != null) {
            for (HstSiteMenuItem item : menu.getSiteMenuItems()) {
                if (item.getHstLink() != null) {
                    String path = item.getHstLink().getPath();
                    HippoDocument hippoBeanForPath = getHippoBeanForPath(path, HippoDocument.class);
                    if (hippoBeanForPath instanceof SocialMediaLink) {
                        socialMediaLinks.put(path, (SocialMediaLink) hippoBeanForPath);
                    }
                }
            }
        }
        request.setAttribute("socialMediaLinks", socialMediaLinks);
    }
}
