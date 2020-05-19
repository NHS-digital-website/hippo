package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

import java.util.Calendar;

@Node(jcrType = "publicationsystem:survey")
public class Survey extends HippoCompound {

    public String getLink() {
        return getSingleProperty("publicationsystem:link");
    }

    public Calendar getDate() {
        return getSingleProperty("publicationsystem:date");
    }
}
