package uk.nhs.digital.intranet.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

import java.util.Calendar;
import java.util.Date;

@Node(jcrType = "intranet:basedocument")
public abstract class BaseDocument extends HippoDocument {

    public String getTitle() {
        return getProperty("intranet:title");
    }

    public String getShortsummary() {
        return getProperty("intranet:shortsummary");
    }

    public Date getLastModified() {
        return ((Calendar) getProperty("hippostdpubwf:lastModificationDate")).getTime();
    }

    public Date getCreationDate() {
        return ((Calendar) getProperty("hippostdpubwf:creationDate")).getTime();
    }

    public String getCreatedBy() {
        return getProperty("hippostdpubwf:createdBy");
    }

    public String[] getKeys() {
        return getProperty("hippotaxonomy:keys");
    }

    public String[] getTopics() {
        return getProperty("common:SearchableTags");
    }

    public abstract String getDocType();
}
