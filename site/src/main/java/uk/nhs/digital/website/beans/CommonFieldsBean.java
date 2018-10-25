package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

/**
 * This is a generic bean containing most of the used fields accross all the
 * document types. And it is mainly making the cdp.xml happy
 */
public class CommonFieldsBean extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:seosummary")
    public String getSeosummary() {
        return getProperty("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:summary")
    public HippoHtml getSummary() {
        return getHippoHtml("website:summary");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }


}
