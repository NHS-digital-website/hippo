package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:CorporateImageSetSpecification")
@Node(jcrType = "website:CorporateImageSetSpecification")
public class CorporateImageSetSpecification extends HippoGalleryImageSet {
    @HippoEssentialsGenerated(internalName = "website:authorPhoto")
    public HippoGalleryImageBean getAuthorPhoto() {
        return getBean("website:authorPhoto", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:authorPhotoLarge")
    public HippoGalleryImageBean getAuthorPhotoLarge() {
        return getBean("website:authorPhotoLarge", HippoGalleryImageBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:authorPhotoLarger")
    public HippoGalleryImageBean getAuthorPhotoLarger() {
        return getBean("website:authorPhotoLarger", HippoGalleryImageBean.class);
    }
}
