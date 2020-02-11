package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.Calendar;

@Node(jcrType = "publicationsystem:changenotice")
public class ChangeNotice extends HippoCompound {

    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    public HippoHtml getContent() {
        return getHippoHtml("publicationsystem:content");
    }

    public Calendar getDate() {
        return getProperty("publicationsystem:date");
    }
}
