package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:casestudy")
@Node(jcrType = "website:casestudy")
public class CaseStudy extends CaseStudyAndBlogBase {

    // used to differentiate between different types of content blocks
    public String getArticleType() {
        return "case-study";
    }


}
