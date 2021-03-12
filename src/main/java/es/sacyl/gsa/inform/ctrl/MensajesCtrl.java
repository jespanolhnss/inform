package es.sacyl.gsa.inform.ctrl;

import com.vaadin.flow.component.notification.Notification;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.MensajeBean;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.dao.MensajeDao;
import es.sacyl.gsa.inform.dao.ParametroDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class MensajesCtrl {

    private static final Logger logger = LogManager.getLogger(MensajesCtrl.class);

    /**
     *
     * @param lopdIncidenciaBean Inserta en la tabla mensajes uno nuevo por la
     * creación de una incidencia
     *
     */
    public void doNuevoMensajeIncidencia(LopdIncidenciaBean lopdIncidenciaBean) {
        String msg = "";

        try {
            MensajeBean mensajeBean = new MensajeBean();
            mensajeBean.setAsuntos(LopdIncidenciaBean.MAIL_ASUNTO_NUEVA.substring(0, 99));
            mensajeBean.setContenido(lopdIncidenciaBean.getHtmlContenidoSolicitud().substring(0, 99));
            String destinatario = new ParametroDao().getPorCodigo(ParametroBean.MAIL_LDAP_DESTINOLOPD).getValor().substring(0, 99);
            if (destinatario != null) {
                mensajeBean.setDestinatarios(destinatario.substring(0, 99));
                new MensajeDao().doGrabaDatos(mensajeBean);
            } else {
                msg = " Parámetro no encontrado " + ParametroBean.MAIL_LDAP_DESTINOLOPD;
                Notification.show(msg);
                throw new Exception(msg);
            }
        } catch (Exception e) {
            logger.error(msg);
        }
    }

}
