package com.ca.mfd.prc.core.message.channel;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.exception.InkelinkExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

@Component
public class MailChannel {

    private static final Logger logger = LoggerFactory.getLogger(MailChannel.class);
    public void sendMail(String to, String subject, String content, String fromMail, String fromMailHost, String fromMailPasswd, String fromHostPort) throws MessagingException {

        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp"); // 使用的协议（JavaMail规范要求）
        properties.setProperty("mail.smtp.host", fromMailHost); // 发件人的邮箱的 SMTP服务器地址
        properties.setProperty("mail.smtp.port", fromHostPort);
        properties.setProperty("mail.smtp.auth", "true"); // 请求认证，参数名称与具体实现有关
        //  properties.setProperty("mail.smtp.socketFactory.port", fromHostPort);
        //  properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        //  properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);

        Session session = Session.getDefaultInstance(properties);
        MimeMessage mimeMessage = new MimeMessage(session);
        Transport transport = null;
        try {
            mimeMessage.setFrom(new InternetAddress(fromMail));

            String[] splitToEmails = to.split("[,;]");
            if (splitToEmails.length <= 0) {
                throw new InkelinkException("邮件发送没有接收地址！");
            }
            InternetAddress[] addresses = new InternetAddress[splitToEmails.length];
            for (int i = 0; i < splitToEmails.length; i++) {
                addresses[i] = InternetAddress.parse(splitToEmails[i])[0];
            }
            mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, addresses);
            mimeMessage.setSentDate(new Date());
            mimeMessage.setSubject(subject);
            mimeMessage.setText(content);
            mimeMessage.saveChanges();

            transport = session.getTransport("smtp");
            transport.connect(fromMail, fromMailPasswd);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            // transport.close();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }


}
