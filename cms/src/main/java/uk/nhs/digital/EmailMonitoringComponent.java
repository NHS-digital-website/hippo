package uk.nhs.digital;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EmailMonitoringComponent implements RepositoryJob {
    private static final Logger log = LoggerFactory.getLogger(EmailMonitoringComponent.class);

    @Override
    public void execute(final RepositoryJobExecutionContext context) throws RepositoryException {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            Session session = (Session) envCtx.lookup("mail/Session");

            try {
                Message message = new MimeMessage(session);
                InternetAddress[] to = new InternetAddress[1];
                to[0] = new InternetAddress("emailmonitoring@nhs.net");
                message.setRecipients(Message.RecipientType.TO, to);
                message.setSubject("EMAIL MONITOR ONLY");
                message.setContent("AUTOMATED", "text/plain");
                InternetAddress fromAddress = new InternetAddress("hippo.nhsdigital@nhs.net");
                message.setFrom(fromAddress);
                Transport.send(message);
                log.info("EMAIL MONITOR: SUCCESS");
            } catch (MessagingException mex) {
                mex.printStackTrace();
                log.warn("EMAIL MONITOR: FAILED");
                log.warn(mex.getMessage());
            }
        } catch (NamingException e) {
            log.warn(e.getMessage());
        }
    }
}
