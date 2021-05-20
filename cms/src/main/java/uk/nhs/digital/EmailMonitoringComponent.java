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
// see context.xml to connect to nhs mail
        final String to = "lesley.kelly2@nhs.net";
        final String from = "lesleykellynhsd@gmail.com";
        final String host = "send.nhs.net";
        final String port = "587";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);

        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Email Monitoring Auto Fired Email from BR");

            // Now set the actual message
            message.setText("Auto Email: forward to AWS for monitoring.");

            // Send message
            Transport.send(message);
            System.out.println("EMAIL MONITOR: SUCCESS");
            log.info("EMAIL MONITOR: SUCCESS");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            System.out.println("EMAIL MONITOR: FAILED");
            System.out.println(mex.getMessage());
            log.warn("EMAIL MONITOR: FAILED");
            log.warn(mex.getMessage());
        }
    }
}
