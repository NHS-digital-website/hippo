package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

@Node(jcrType = "website:tableau")
public class Tableau extends HippoCompound {

    public String getUrl() {
        return getProperty("website:url");
    }

    public boolean getHidetabs() {
        return getProperty("website:hidetabs");
    }

    public String getDevice() {
        return getProperty("website:device");
    }

    public String getSectionType() {
        return "tableau";
    }
}
