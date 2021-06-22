package uk.nhs.digital;

import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import javax.jcr.RepositoryException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailMonitoringComponent implements RepositoryJob {
    private static final Logger log = LoggerFactory.getLogger(EmailMonitoringComponent.class);

    @Override
    public void execute(final RepositoryJobExecutionContext context) throws RepositoryException {
        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        Session session = (Session) envCtx.lookup("mail/NHSMail");

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(request.getParameter("from")));

            // Set To: header field of the header.
            final String to = "emailmonitoring@nhs.net";
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("EMAIL MONITOR ONLY");

            // Now set the actual message
            message.setText("AUTOMATED EMAIL - eForms Monitoring. Please do not delete.");

            // Send message
            Transport.send(message);
            log.info("EMAIL MONITOR: SUCCESS");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            log.warn("EMAIL MONITOR: FAILED");
            log.warn(mex.getMessage());
        }
    }
}
