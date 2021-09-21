package app.service;

import app.model.Order;
import app.util.OrderToPDF;
import app.util.PswdCrypt;
import java.io.File;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

/**
 * Класс для отправки писем на электронную почту клиента
 *
 */
@Stateless
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class);

    @Resource(lookup = "confirmURL")
    private String confirmUrl;

    @Resource(name = "mail/notification")
    private Session session;

    @Inject
    PswdCrypt pswdCrypt;

    @Inject
    OrderToPDF orderToPDF;

    /**
     * Метод отправляет письмо содержащее ссылку для активации 
     * учетной записи клиента
     *
     * @param emailAddress
     * @param userId
     */
    @Asynchronous
    public void sendEmailForConfirm(String emailAddress, int userId) {
        if (userId > 0) {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("example@gmail.com"));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(emailAddress)
                );
                message.setSubject("Confirm email");
                message.setText("To confirm your account, follow the link:"
                        + "\n\n" + confirmUrl + "?id=" + userId + "&confirm="
                        + pswdCrypt.pswdCrypt(emailAddress));

                Transport.send(message);
            } catch (MessagingException ex) {
                logger.error("Message sending error = {}" + ex);
            }
        } else {
            logger.error("Failed to send mail -> " + emailAddress);
        }
    }

    /**
     * Метод отправляет заказ-наряд в формате .pdf на почту клиента
     * @param order заказ-наряд
     */
    @Asynchronous
    public void sendEmailWithAttachment(Order order) {
        orderToPDF.manipulatePdf(order);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("example@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(order.getClient().getEmail())
            );
            message.setSubject("Confirm email");

            File file = new File("/home/deniskharlamov/orderToPDF/" + order.getOrderNumber() + ".pdf");
            Multipart multipart = new MimeMultipart();
            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(file.getName());
            multipart.addBodyPart(attachmentBodyPart);
            BodyPart htmlBodyPart = new MimeBodyPart();
            htmlBodyPart.setContent("Work order in attachment.", "text/html");
            multipart.addBodyPart(htmlBodyPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException ex) {
            logger.error("Failed to send mail -> ", ex);
        }
    }
}