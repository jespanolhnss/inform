package es.sacyl.gsa.inform.util;

import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.dao.ParametroDao;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class MandaMail.
 */
public class MandaMail {

    private static final Logger logger = LogManager.getLogger(MandaMail.class);

    public static final String MAIL_ERRORENVIO_STRING = "Error enviando mail";

    private final Properties properties = new Properties();

    private Session session;

    /**
     * Inits the.
     */
    private void init() {

    }

    /**
     * Send email.
     *
     * @param destinatario the destinatario separados por comas
     * @param asunto the asunto
     * @param contenido the contenido
     *
     */
    public void sendEmail(String destinatario, String asunto, String contenido) {
        try {

            /*
            properties.put("mail.smtp.host", "smtp.saludcastillayleon.es");
            properties.put("mail.smtp.port", 25);
            properties.put("mail.smtp.mail.sender", "avisos.hnss@saludcastillayleon.es");
            properties.put("mail.smtp.user", "grs.root/avisos.hnss");
            properties.put("mail.smtp.auth", "true");
             */
            properties.put("mail.smtp.host", new ParametroDao().getPorCodigo(ParametroBean.MAIL_HOST).getValor());
            properties.put("mail.smtp.port", new ParametroDao().getPorCodigo(ParametroBean.MAIL_PORT).getValor());
            properties.put("mail.smtp.mail.sender", new ParametroDao().getPorCodigo(ParametroBean.MAIL_SENDER).getValor());
            properties.put("mail.smtp.user", new ParametroDao().getPorCodigo(ParametroBean.MAIL_USER).getValor());
            properties.put("mail.smtp.auth", new ParametroDao().getPorCodigo(ParametroBean.MAIL_AUTH).getValor());

            session = Session.getDefaultInstance(properties);
            String[] destinatarios = new String[1];
            if (destinatario.indexOf(",") != -1) {
                destinatarios = destinatario.split(",");
            } else {
                destinatarios[0] = destinatario;
            }
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            for (String destino : destinatarios) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(destino));
            }
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("informatica.hnss@saludcastillayleon.es"));
            message.setSubject(asunto);
            message.setText(contenido);

            Transport t = session.getTransport("smtp");
            // t.connect((String) properties.get("mail.smtp.host ") + (String)
            // properties.get("mail.smtp.user"),
            // (String) MyUI.objParametros.get(Parametros.KEY_MAILPASSW));
            t.connect("grs.root/avisos.hnss", "12345678");
            // t.connect((String) properties.get("mail.smtp.user"),
            // (String) MyUI.objParametros.get(Parametros.KEY_MAILPASSW));
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            logger.debug("mensaje enviado" + destinatarios.toString() + "\n Asunto: " + asunto + "\n Contenido:"
                    + contenido);
        } catch (AddressException e) {
            logger.error(Utilidades.getStackTrace(e));
        } catch (MessagingException e) {
            logger.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        }
    }

    public void mandaMailAdjunto(String destinatario, String asunto, String contenido, String pathFile, String nameFile) {

        try {
            properties.put("mail.smtp.host", new ParametroDao().getPorCodigo(ParametroBean.MAIL_HOST).getValor());
            properties.put("mail.smtp.port", new ParametroDao().getPorCodigo(ParametroBean.MAIL_PORT).getValor());
            properties.put("mail.smtp.mail.sender", new ParametroDao().getPorCodigo(ParametroBean.MAIL_SENDER).getValor());
            properties.put("mail.smtp.user", new ParametroDao().getPorCodigo(ParametroBean.MAIL_USER).getValor());
            properties.put("mail.smtp.auth", new ParametroDao().getPorCodigo(ParametroBean.MAIL_AUTH).getValor());

            session = Session.getDefaultInstance(properties);

            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
            texto.setText(contenido);

            // Se compone el adjunto con la imagen
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(
                    new DataHandler(new FileDataSource(pathFile)));
            adjunto.setFileName(nameFile);

            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjunto);

            String[] destinatarios = destinatario.split(",");
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            for (String destino : destinatarios) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(destino));
            }
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("informatica.hnss@saludcastillayleon.es"));
            message.setSubject(asunto);
            message.setText(contenido);
            message.setContent(multiParte);

            Transport t = session.getTransport("smtp");
            // t.connect((String) properties.get("mail.smtp.host ") + (String)
            // properties.get("mail.smtp.user"),
            // (String) MyUI.objParametros.get(Parametros.KEY_MAILPASSW));
            t.connect("grs.root/avisos.hnss", "12345678");
            // t.connect((String) properties.get("mail.smtp.user"),
            // (String) MyUI.objParametros.get(Parametros.KEY_MAILPASSW));
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            logger.debug("mensaje enviado" + destinatarios.toString() + "\n Asunto: " + asunto + "\n Contenido:"
                    + contenido);
        } catch (AddressException e) {
            logger.error(Utilidades.getStackTrace(e));
        } catch (MessagingException e) {
            logger.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        }

    }
}
