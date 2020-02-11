package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.Calendar;

@Node(jcrType = "publicationsystem:interactivetool")
public class InteractiveTool extends HippoCompound {

    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    public String getLink() {
        return getProperty("publicationsystem:link");
    }

    public HippoHtml getContent() {
        return getHippoHtml("publicationsystem:content");
    }

    public Calendar getDate() {
        return getProperty("publicationsystem:date");
    }

    public Boolean getAccessible() {
        return getProperty("publicationsystem:accessible");
    }

    public AccessibleLink getAccessiblelocation() {
        return getBean("publicationsystem:accessiblelocation", AccessibleLink.class);
    }
}
