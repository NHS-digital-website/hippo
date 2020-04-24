package uk.nhs.digital.intranet.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

import java.util.Calendar;
import java.util.Date;

@Node(jcrType = "intranet:basedocument")
public class BaseDocument extends HippoDocument {

    public String getTitle() {
        return getProperty("intranet:title");
    }

    public String getShortsummary() {
        return getProperty("intranet:shortsummary");
    }

    public Date getLastModified() {
        return ((Calendar) getProperty("hippostdpubwf:lastModificationDate")).getTime();
    }
}
