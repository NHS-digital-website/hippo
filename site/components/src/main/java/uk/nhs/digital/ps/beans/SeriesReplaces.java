package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "publicationsystem:seriesReplaces")
@Node(jcrType = "publicationsystem:seriesReplaces")
public class SeriesReplaces extends HippoDocument {


    @HippoEssentialsGenerated(internalName = "publicationsystem:date")
    public Calendar getDate() {
        return getProperty("publicationsystem:date");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:mirror")
    public Series getMirror() {
        return getLinkedBean("publicationsystem:mirror", Series.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:whyReplaced")
    public HippoHtml getWhyReplaced() {
        return getHippoHtml("publicationsystem:whyReplaced");
    }
}
