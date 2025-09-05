/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import com.origami.config.SisVars;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
 * @author Origami
 */
public class Email implements Serializable {

    protected List<File> adjuntos = new ArrayList<>();
    protected String usuarioCorreo = SisVars.correo;
    protected String password = SisVars.pass;
    protected String rutaArchivo1;
    protected String nombreArchivo1;
    protected String rutaArchivo2;
    protected String nombreArchivo2;
    protected String destinatario;
    protected String copiaOcultaBCC;
    protected String copiaCC;
    protected String asunto;
    protected String mensaje;

    public Email(String destinatario, String copiaOcultaBCC, String copiaCC, String asunto, String mensaje, List<File> files) {
        this.destinatario = destinatario;
        this.copiaOcultaBCC = copiaOcultaBCC;
        this.copiaCC = copiaCC;
        this.asunto = asunto;
        this.mensaje = mensaje;
        if (files != null) {
            this.adjuntos = files;
        }
    }

    public Email(String destinatario, String asunto, String mensaje, List<File> files) {
        this(destinatario, null, null, asunto, mensaje, files);
    }

    public boolean sendMail() {
        try {
            //INGRESO DE LAS PROPIEDADES DE LA CONEXION
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", SisVars.smtp_Host);
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", SisVars.smtp_Port);
            props.setProperty("mail.smtp.user", usuarioCorreo);
            props.setProperty("mail.smtp.auth", "true");
            //INSTANCIA DE LA SESSION
            Session session = Session.getInstance(props, null);
            //CUERPO DEL MENSAJE
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(usuarioCorreo, Constantes.fromEmail));
            mimeMessage.setSubject(asunto);
            mimeMessage.setSentDate(new Date());
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            if (copiaOcultaBCC != null) {
                mimeMessage.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(copiaOcultaBCC));
            }
            if (copiaCC != null) {
                mimeMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(copiaCC));
            }
            //TEXTO DEL MENSAJE
            MimeBodyPart texto = new MimeBodyPart();
            //texto.setText(mensaje);
            texto.setContent(mensaje, "text/html; charset=utf-8");
            //CONTENEDOR DE LAS PARTES
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            //ADJUNTAR LOS ARCHIVO EN PARTES
            MimeBodyPart file;
            for (File f : adjuntos) {
                file = new MimeBodyPart();
                file.attachFile(f);
                multipart.addBodyPart(file);
            }
            //AGREGAR MULTIPART EN CUERPO DEL MENSAJE
            mimeMessage.setContent(multipart);
            // ENVIAR MENSAJE
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect(usuarioCorreo, password);
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            }
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

}
