package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:editorsnotes")
@Node(jcrType = "website:editorsnotes")
public class EditorsNotes extends BaseDocument {

    @HippoEssentialsGenerated(internalName = "website:editorsnote")
    public HippoHtml getEditorsnote() {
        return getHippoHtml("website:editorsnote");
    }

}
