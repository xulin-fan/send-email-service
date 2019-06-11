package com.yuki.sere.services;

import com.yuki.sere.pojo.MailPo;
import com.yuki.sere.utils.StringUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by fan on 2019/6/11.
 */
public class MailSender {

    private Session session;

    private MailPo mailPo;

    public MailSender(MailPo mailPo) {
        this.mailPo = mailPo;
        initSession();
    }

    public MailPo getMailPo() {
        return mailPo;
    }


    public void sendHtml() throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setSubject(mailPo.getSubject());
        message.setFrom(new InternetAddress(mailPo.getFromuser(),"QATeam-Fan"));
        message.setRecipients(Message.RecipientType.TO, getAddresses(mailPo.getTouser()));
        message.setRecipients(Message.RecipientType.CC, getAddresses(mailPo.getCcuser()));
        message.setRecipients(Message.RecipientType.BCC, getAddresses(mailPo.getBccuser()));
        message.setContent(getMultipart());
        Transport.send(message);
    }

    private Session initSession() {
        Properties p = System.getProperties();
        p.setProperty("mail.smtp.host", mailPo.getHost());
        p.setProperty("mail.smtp.auth", "true");
        p.setProperty("mail.smtp.user", mailPo.getFromuser());
        p.setProperty("mail.smtp.pass", mailPo.getAuthPass());

        return session = Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailPo.getFromuser(), mailPo.getAuthPass());
            }
        });
    }

    private Multipart getMultipart() throws MessagingException {
        MimeMultipart multipart = new MimeMultipart();
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(mailPo.getContent(), "text/html;charset=utf-8");
        multipart.addBodyPart(bodyPart);
        return multipart;
    }

    private Address[] getAddresses(String mails) {
        return !StringUtils.isNull(mails) ? Stream.of(mails.split(","))
                .map(mail -> {
                    try {
                        return new InternetAddress(mail);
                    } catch (AddressException e) {
                        System.out.println(mail + " 格式非法");
                        return null;
                    }
                }).filter(Objects::nonNull).toArray(InternetAddress[]::new) : null;
    }
}
