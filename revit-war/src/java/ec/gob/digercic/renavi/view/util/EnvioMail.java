/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.view.util;

import java.util.Properties;
//Para manejo de JavaMail
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author daniel.porras
 */
public class EnvioMail {

    /**
     * Método para resetera clave generada aleatoriamente y enviar un email para
     * la notificación al usuario.
     *
     * @param nombres
     * @param nomUsuario
     * @param email
     * @return 001 si el mensaje es enviado correctamente 002 si la clave no
     * pudo ser reseteada. 003 si no existe un correo electrónico al cual enviar
     * el mail 004 si existe algun oreo error.
     * @throws Exception
     */
    public static boolean enviarEmail(String nombres, String nomUsuario,
            String email, String password, String mailHost, String mailPort, String mailUser,
            String mailPassword, String mailFrom) {
        /*String mailHost;
         String mailPort;
         final String mailUser;
         final String mailPassword;
         String mailFrom;
         //String mensajeTitulo;
         //String mensajeInicial;*/
        String alertBody = "";
        String destinatarios = "";
        Boolean sendMail = true;
        final String mailUserF = mailUser;
        final String mailPasswordF = mailPassword;

        // INICIALIZO EL SERVIFOR SMTP
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", mailHost); //SMTP Host
            props.put("mail.smtp.socketFactory.port", mailPort); //SSL Port
            props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
            props.put("mail.smtp.port", mailPort); //SMTP Port

            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailUserF, mailPasswordF);
                }
            };

            if (email != null || !email.equals("")) {
                destinatarios = email;
            } else {
                sendMail = false;
            }
            // ENVIO DEL MENSAJE
            try {
                if (sendMail) {

                    //Session mailSession = Session.getDefaultInstance(props, auth);
                    Session mailSession = Session.getInstance(props, auth);

                    Transport transport = mailSession.getTransport("smtp");

                    MimeMessage message = new MimeMessage(mailSession);
                    //Asunto
                    message.setSubject("Reseteo de clave");
                    message.setFrom(new InternetAddress(mailFrom));

                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatarios));
                    MimeMultipart multipart = new MimeMultipart("related");

                    //Genero la clave aleatoriamente
                    BodyPart messageBodyPart = new MimeBodyPart();
                    alertBody = "<html><head><title></title></head><body bgcolor=\"#FFF\">"
                            + "<div align=\"justify\" style=\"margin:50px;\">"
                            + "<h2>Direcci&oacute;n General de Registro Civil, Identificaci&oacute;n y Cedulaci&oacute;n</h2>"
                            + "<br/>"
                            + "<h2>Estimado(a): " + nombres.toUpperCase() + ",</h2>"
                            + "<p>"
                            + "Usted solicit&oacute; RESTABLECER SU CLAVE en el Sistema Nacional de Registro "
                            + "de Datos Vitales <b>'REVIT'</b>, para lo cual se le ha proporcionado una clave "
                            + "en forma aleatoria."
                            + "<br/> <h3>NOTA:</h3><br/>"
                            + "Usuario: "
                            + (nomUsuario == null ? "" : nomUsuario)
                            + "<br/>Clave: " + password
                            + "<br/><br/>"
                            + "Le recomendamos que por motivos de seguridad cambie su clave una vez ingrese al sistema. </p>"
                            + "<br/><br/>"
                            + "<i><b>Atentamente:</b> " + mailFrom + "</i><br />"
                            + "</div></body></html>";
                    messageBodyPart.setContent(alertBody, "text/html");
                    multipart.addBodyPart(messageBodyPart);
                    message.setContent(multipart);
                    transport.connect();
                    transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
                    transport.close();
                    return true;
                } else {
                    return false;
                }

            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
