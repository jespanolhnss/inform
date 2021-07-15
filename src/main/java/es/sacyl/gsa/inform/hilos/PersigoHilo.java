/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.hilos;

import es.sacyl.gsa.inform.bean.MensajeBean;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.dao.MensajeDao;
import es.sacyl.gsa.inform.dao.ParametroDao;
import es.sacyl.gsa.inform.dao.PersigoDao;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class PersigoHilo {

    private static final Logger LOGGER = LogManager.getLogger(PersigoHilo.class);
    private static PersigoHilo miPersigoHilo;

    public static PersigoHilo getHiloPersigo() {
        if (miPersigoHilo == null) {
            miPersigoHilo = new PersigoHilo();
        }
        return miPersigoHilo;
    }

    private PersigoHilo() {
        Timer timerObj = new Timer();
        TimerTask timerTaskObj = new TimerTask() {
            @Override
            public void run() {
                String log = new PersigoDao().doAcualizaTablaLocal();
                LOGGER.debug("Hilo persigo :" + LocalDateTime.now());
                MensajeBean msg = new MensajeBean();
                msg.setAsuntos("Hilo actualización pérsigo");
                msg.setContenido(log);
                msg.setTipo(MensajeBean.TIPOMAIL);
                msg.setDestinatarios(new ParametroDao().getPorCodigo(ParametroBean.MAIL_SISTEMAS_HNSS).getValor());
                new MensajeDao().doGrabaDatos(msg);
            }
        };
        // 20 minutos
        timerObj.schedule(timerTaskObj, 0, 10000);

    }

}
