package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.ContentNodeBinder;
import org.hippoecm.hst.content.beans.ContentNodeBindingException;
import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.website.beans.BaseDocument;

import javax.jcr.RepositoryException;

@HippoEssentialsGenerated(internalName = "publicationsystem:feedbackforum")
@Node(jcrType = "publicationsystem:feedbackforum")
public class FeedbackForum extends BaseDocument implements ContentNodeBinder {

    private static final Logger log = LoggerFactory.getLogger(FeedbackForum.class);

    private String subject;
    private String comments;
    private String url;

    public String getSubject() {
        return (subject == null) ? getProperty("publicationsystem:subject") : subject;
    }

    public String getComments() {
        return (comments == null) ? getProperty("publicationsystem:comments") : comments;
    }

    public String getUrl() {
        return (url == null) ? getProperty("publicationsystem:url") : url;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean bind(Object content, javax.jcr.Node node) throws ContentNodeBindingException {
        if (content instanceof FeedbackForum) {
            FeedbackForum feedbackForum = (FeedbackForum) content;
            try {
                node.setProperty("publicationsystem:subject", feedbackForum.getSubject());
                node.setProperty("publicationsystem:comments", feedbackForum.getComments());
                node.setProperty("publicationsystem:url", feedbackForum.getUrl());
            } catch (RepositoryException e) {
                log.error(
                    "Unable to bind the content to the JCR Node"
                        + e.getMessage(), e);
                throw new ContentNodeBindingException(e);
            }
            return true;
        }
        return false;
    }
}
