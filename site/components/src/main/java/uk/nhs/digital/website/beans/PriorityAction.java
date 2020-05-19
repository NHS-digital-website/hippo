package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;

@Node(jcrType = "website:priorityaction")
public class PriorityAction extends CommonFieldsBean {

    public String getAction() {
        return getSingleProperty("website:action");
    }

    public String getAdditionalInformation() {
        return getSingleProperty("website:additionalinformation");
    }

    public HippoBean getLink() {
        return getBean("website:itemlink");
    }
}
