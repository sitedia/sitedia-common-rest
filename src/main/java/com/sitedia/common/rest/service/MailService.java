package com.sitedia.common.rest.service;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Mail send service
 * @author cedric
 *
 */
@Service
@Lazy
public class MailService {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final Logger mailLogger = Logger.getLogger("error.mail");

    @Value("${spring.mail.from}")
    private String from;

    @Value("${app.mail.enabled}")
    private Boolean enabled;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MessageSource messageSource;

    /**
     * Send the mail according to the Spring boot configuration
     * @param recipient
     * @param subject
     * @param body
     */
    public void sendMail(String recipient, String subject, String body) {
        sendMail(recipient, subject, body, null, null);
    }

    @Async
    public void sendMail(String recipient, String subject, String body, String attachmentName, byte[] attachmentContent) {

        if (!enabled) {
            return;
        }

        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            // use the true flag to indicate you need a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(from));
            helper.setTo(recipient);

            // use the true flag to indicate the text included is HTML
            helper.setSubject(subject);
            helper.setText(body, true);

            // Set the attachment
            if (attachmentName != null) {
                helper.addAttachment(attachmentName, new ByteArrayResource(attachmentContent));
            }

            // Send the mail
            logger.log(Level.INFO, "Sending mail to {0}: {1}", new String[] { recipient, subject });
            if (!recipient.endsWith("@localhost")) {
                javaMailSender.send(message);
            } else {
                logger.warning(String.format("Mail not send due to test mail: %s", recipient));
            }

        } catch (MailSendException e) {
            logger.log(Level.FINE, "Unable to send mail", e);
        } catch (Exception e) {
            mailLogger.severe(e.getMessage());
            logger.log(Level.SEVERE, "Unable to send mail", e);
        }
    }

    public String createBody(String title, String text, String link) {
        String result = messageSource.getMessage("app.mail.format", new String[0], Locale.FRENCH);
        result = result.replaceAll("##title##", title);
        result = result.replaceAll("##text##", text);
        result = result.replaceAll("##link##", link);
        return result;
    }

}
