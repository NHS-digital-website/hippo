package uk.nhs.digital.common.components;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.component.support.forms.FormField;
import org.hippoecm.hst.component.support.forms.FormMap;
import org.hippoecm.hst.component.support.forms.FormUtils;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.annotations.Persistable;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.ObjectBeanPersistenceException;
import org.hippoecm.hst.content.beans.manager.workflow.WorkflowPersistenceManager;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.FeedbackForum;

import java.util.Calendar;
import javax.jcr.RepositoryException;
import javax.jcr.Session;


public class FeedbackForumComponent extends BaseHstComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackForumComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) throws HstComponentException {
        boolean isPreview = RequestContextProvider.get().isPreview();
        request.setAttribute("isPreview", isPreview);

        final FormMap formMap = new FormMap();
        FormUtils.populate(request, formMap);
        request.setAttribute("errors", formMap.getMessage());
        request.setAttribute("subject", getFormFieldValue(formMap, "subject"));
        request.setAttribute("url", request.getRequestURL());
        request.setAttribute("comments", getFormFieldValue(formMap, "comment"));
        request.setAttribute("success", request.getParameter("success"));
    }

    @Override
    @Persistable
    public void doAction(HstRequest request, HstResponse response) throws HstComponentException {
        LOGGER.info("DO ACTION CALLED");

        final FormMap formMap = new FormMap(request, new String[]{"subject", "comments", "url"});

        boolean valid = true;
        for (String fieldName : formMap.getFieldNames()) {
            final FormField field = formMap.getField(fieldName);
            if (StringUtils.isEmpty(field.getValue()) && !("url".equals(fieldName))) {
                field.addMessage("Field is required");
                valid = false;
            }
        }

        //create simple doctype with feedback -> Save in specific folder.
        //improvements -> Add url (window.location) automatically on hidden field on click.
        if (!valid) {
            LOGGER.debug("Form is not valid");
            FormUtils.persistFormMap(request, response, formMap, null);
            return;
        }

        final String siteContentBasePath = request.getRequestContext().getSiteContentBasePath();
        final Calendar currentDate = Calendar.getInstance();

        final String feedbackFolderPath = "/" + siteContentBasePath + "/feedback/" + currentDate.get(Calendar.YEAR)
            + "/" + (currentDate.get(Calendar.MONTH) + 1) + "/" + currentDate.get(Calendar.DAY_OF_MONTH);

        final String feedbackDocumentName = "feedback-for-" + request.getRequestURL() + "-" + currentDate.getTimeInMillis();

        try {
            final Session session = request.getRequestContext().getSession();
            WorkflowPersistenceManager wpm = getWorkflowPersistenceManager(session);

            final String feedbackDocumentPath = wpm.createAndReturn(feedbackFolderPath, "publicationsystem:feedbackforum", feedbackDocumentName, true);
            final FeedbackForum feedbackForum = (FeedbackForum) wpm.getObject(feedbackDocumentPath);

            if (feedbackForum == null) {
                throw new HstComponentException("Failed to add Feedback");
            }

            populateFeedbackBean(formMap, feedbackForum);
            wpm.update(feedbackForum);
            wpm.save();

            response.setRenderParameter("success", "success");

            final FeedbackForum ChangedFeedbackForum = (FeedbackForum) wpm.getObject(feedbackDocumentPath);
            LOGGER.info(ChangedFeedbackForum.toString());

        } catch (RepositoryException | ObjectBeanPersistenceException e) {
            LOGGER.error("Fails to persist feedback {}", e.getMessage());
        } catch (ObjectBeanManagerException e) {
            LOGGER.error("Failed to retrieve object from document path: {}", feedbackDocumentName);
        }
    }

    private void populateFeedbackBean(FormMap formMap, FeedbackForum feedbackForum) {
        feedbackForum.setComments(formMap.getField("comments").getValue());
        feedbackForum.setSubject(formMap.getField("subject").getValue());
        feedbackForum.setUrl(formMap.getField("url").getValue());
    }

    private String getFormFieldValue(final FormMap formMap, final String fieldName) {
        if (!StringUtils.isEmpty(fieldName) && formMap.getFormMap().containsKey(fieldName)) {
            return StringEscapeUtils.escapeHtml(formMap.getField(fieldName).getValue().trim());
        }
        return StringUtils.EMPTY;
    }
}
