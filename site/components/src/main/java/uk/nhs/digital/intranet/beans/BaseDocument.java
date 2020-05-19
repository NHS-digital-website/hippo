package uk.nhs.digital.intranet.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

import java.util.Calendar;
import java.util.Date;

@Node(jcrType = "intranet:basedocument")
public abstract class BaseDocument extends HippoDocument {

    public String getTitle() {
        return getSingleProperty("intranet:title");
    }

    public String getShortsummary() {
        return getSingleProperty("intranet:shortsummary");
    }

    public Date getLastModified() {
        return ((Calendar) getSingleProperty("hippostdpubwf:lastModificationDate")).getTime();
    }

    public Date getCreationDate() {
        return ((Calendar) getSingleProperty("hippostdpubwf:creationDate")).getTime();
    }

    public String getCreatedBy() {
        return getSingleProperty("hippostdpubwf:createdBy");
    }

    public String[] getKeys() {
        return getMultipleProperty("hippotaxonomy:keys");
    }

    public String[] getTopics() {
        return getMultipleProperty("common:SearchableTags");
    }

    public abstract String getDocType();
}
