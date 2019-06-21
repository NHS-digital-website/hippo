package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.ArrayList;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:notesforeditors")
@Node(jcrType = "website:notesforeditors")
public class NotesForEditors extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:customnotes")
    public List<HippoHtml> getCustomnotes() {
        return getChildBeansByName("website:customnotes", HippoHtml.class);
    }

    @HippoEssentialsGenerated(internalName = "website:preparednotes")
    public List<HippoBean> getPreparednotes() {
        return getLinkedBeans("website:preparednotes", HippoBean.class);
    }

    public List<HippoHtml> getHtmlnotes() {
        List<HippoHtml> notes = new ArrayList<>();
        
        for (HippoBean bean : getPreparednotes()) {
            EditorsNotes note = (EditorsNotes)bean;
            notes.add(note.getEditorsnote());
        }
        notes.addAll(this.getCustomnotes());

        return notes;
    }

}
