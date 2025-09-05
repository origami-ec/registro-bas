/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.test;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Administrator
 */
public class MailTest {

    protected String protocol;
    protected String auth;
    protected String tls;

    protected String host;
    protected String port;
    protected String user;
    protected String pass;

    protected String to;
    protected String subject;
    protected String content;

    public MailTest(String protocol, String auth, String tls, String host,
            String port, String user, String pass, String to, String subject,
            String content) {
        this.protocol = protocol;
        this.auth = auth;
        this.tls = tls;
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public boolean sendMail() {
        try {

            //INGRESO DE LAS PROPIEDADES DE LA CONEXION
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", protocol);
            props.setProperty("mail.smtp.starttls.enable", tls);
            props.setProperty("mail.smtp.host", host);
            props.setProperty("mail.smtp.port", port);
            props.setProperty("mail.smtp.user", user);
            props.setProperty("mail.smtp.auth", auth);

            //INSTANCIA DE LA SESSION
            Session session = Session.getInstance(props, null);

            //CUERPO DEL MENSAJE
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(user));
            mimeMessage.setSubject(subject);
            mimeMessage.setSentDate(new Date());
            mimeMessage.addRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            //TEXTO DEL MENSAJE
            MimeBodyPart texto = new MimeBodyPart();
            texto.setContent(content, "text/html; charset=utf-8");

            //CONTENEDOR DE LAS PARTES
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);

            //AGREGAR MULTIPART EN CUERPO DEL MENSAJE
            mimeMessage.setContent(multipart);

            // ENVIAR MENSAJE
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect(user, pass);
                transport.sendMessage(mimeMessage,
                        mimeMessage.getAllRecipients());
            }

            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(MailTest.class.getName()).log(Level.SEVERE,
                    null, ex);
            return false;
        }

    }

}
