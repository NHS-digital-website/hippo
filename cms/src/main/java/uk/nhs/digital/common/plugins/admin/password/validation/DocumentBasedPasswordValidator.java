package uk.nhs.digital.common.plugins.admin.password.validation;

import org.hippoecm.frontend.plugin.config.*;
import org.hippoecm.frontend.plugins.cms.admin.password.validation.*;
import org.hippoecm.frontend.plugins.cms.admin.users.*;
import org.hippoecm.frontend.session.*;
import org.hippoecm.repository.util.*;
import org.slf4j.*;

import java.util.*;
import javax.jcr.*;

public class DocumentBasedPasswordValidator extends AbstractPasswordValidator {

    private static final Logger log = LoggerFactory.getLogger(DocumentBasedPasswordValidator.class);

    private static final String PATH_PROPERTY = "path";
    private static final String DESCRIPTION_PROPERTY = "message";
    private static final String DEFAULT_PASSWORD_DOCUMENT_PATH = "/content/documents/administration/password-blacklist";
    private static final String DEFAULT_VALIDATION_MESSAGE = "Please choose a different password ";

    private String passwordDocumentPath;
    private String validationMessage;

    public DocumentBasedPasswordValidator(IPluginConfig config) {
        super(config);
        this.passwordDocumentPath = config.getString(PATH_PROPERTY, DEFAULT_PASSWORD_DOCUMENT_PATH);
        this.validationMessage = config.getString(DESCRIPTION_PROPERTY, DEFAULT_VALIDATION_MESSAGE);
    }

    public List<String> getPasswordsFromBlacklist() {
        List<String> blacklistedPasswords = null;
        Session session = null;
        try {
            //impersonate is needed for those users not having read permission for the administration folder
            //liveuser will be used since non authorized users (for some strange reasons) don't have access to the content folder
            session = UserSession.get().getJcrSession().impersonate(new SimpleCredentials("liveuser", new char[0]));
            Node passwordDocumentNode = JcrUtils.getNodeIfExists(passwordDocumentPath, session);

            if (passwordDocumentNode != null) {
                //this implementation relies on the published version of the blacklist doc
                Optional<Node> publishedVariant = WorkflowUtils.getDocumentVariantNode(passwordDocumentNode, WorkflowUtils.Variant.PUBLISHED);

                if (publishedVariant.isPresent()) {
                    blacklistedPasswords = Arrays.asList(
                        JcrUtils.getMultipleStringProperty(publishedVariant.get(), "common:entries", new String[0])
                    );
                }
            } else {
                log.error("User {} doesn't have read access to this password file {}", UserSession.get().getJcrSession().getUserID(), this.passwordDocumentPath);
            }
        } catch (RepositoryException repositoryException) {
            log.error("Something went wrong while validation password using black list {}", repositoryException);
        } finally {
            //logout after impersonate
            if (session != null) {
                session.logout();
            }
        }
        return blacklistedPasswords;
    }

    @Override
    public String getDescription() {
        return this.validationMessage;
    }

    @Override
    protected boolean isValid(final String candidatePassword, final User user) {
        List<String> invalidPasswords = getPasswordsFromBlacklist();
        if (invalidPasswords == null || invalidPasswords.isEmpty()) {
            return true;
        } else {
            return invalidPasswords.parallelStream().noneMatch(blacklistedPassword -> blacklistedPassword.equals(candidatePassword));
        }
    }
}
