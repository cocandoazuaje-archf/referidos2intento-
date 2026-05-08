/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.util;

import cl.cnsv.referidosrrvv.clases.Resources;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import org.apache.log4j.Logger;

/**
 *
 * @author cow
 */
public class construirBodyEnviarMail {

    private static final Logger LOGGER = Logger.getLogger(
            construirBodyEnviarMail.class);

    public static void construir(
            String correoDestinatario,
            String rutReferido,
            String nombreReferido,
            String regionReferido,
            String comunaReferido,
            String canalReferido,
            int envios) throws UnsupportedEncodingException, MessagingException {
        if ((correoDestinatario != null) && (!correoDestinatario.isEmpty())) {
            String smtpHostServer = Resources.SERVIDOR_SMTP_CONSORCIO;
            String emailID = correoDestinatario;
            Properties props = System.getProperties();
            props.put("mail.smtp.host", smtpHostServer);
            Session session = Session.getInstance(props, null);

            String body;

            body = Resources.bodySendMailDerivados(
                    nombreReferido,
                    rutReferido,
                    canalReferido,
                    regionReferido,
                    comunaReferido,
                    envios);

            EmailUtil.sendEmail(session, emailID, "Referido Asignado.", body);
        } else {
            LOGGER.info("No se obtuvo un correo de ejecutiva para enviar ...!! ");
        }
    }

    public static void construir2(String correoDestinatario, String body)
            throws UnsupportedEncodingException, MessagingException {
        if ((correoDestinatario != null) && (!correoDestinatario.isEmpty())) {
            String smtpHostServer = Resources.SERVIDOR_SMTP_CONSORCIO;
            String emailID = correoDestinatario;
            Properties props = System.getProperties();
            props.put("mail.smtp.host", smtpHostServer);
            Session session = Session.getInstance(props, null);

            EmailUtil.sendEmail(
                    session,
                    emailID,
                    "Actualizacion de Prospeccion de Referidos.",
                    body);
        } else {
            LOGGER.info("No se obtuvo un correo de ejecutiva para enviar ...!! ");
        }
    }
}
