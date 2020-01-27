package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.Arrays;
import java.util.Calendar;

@Node(jcrType = "website:update")
public class Update extends CommonFieldsBean {

    public String getTitle() {
        return getProperty("website:title");
    }

    public HippoHtml getContent() {
        return getHippoHtml("website:content");
    }

    public String getSeverity() {
        return getProperty("website:severity");
    }

    public Calendar getExpirydate() {
        return getProperty("website:expirydate");
    }

    public HippoBean getRelateddocument() {
        return getLinkedBean("website:relateddocument", HippoBean.class);
    }

    public enum Severity {
        CRITICAL("critical", 0),
        IMPORTANT("important", 1),
        INFORMATION("information", 2);

        private String text;
        private int sortOrder;

        public String getText() {
            return text;
        }

        public int getSortOrder() {
            return sortOrder;
        }

        Severity(String text, int sortOrder) {
            this.text = text;
            this.sortOrder = sortOrder;
        }

        public static int getSortOrder(String severityText) {
            return Arrays.stream(Severity.values())
                .filter(severity -> severity.getText().equals(severityText))
                .findFirst()
                .map(Severity::getSortOrder)
                .orElse(Integer.MAX_VALUE);
        }
    }
}
