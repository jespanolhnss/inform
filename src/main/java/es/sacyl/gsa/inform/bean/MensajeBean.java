package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author 06551256M
 */
public class MensajeBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tipo;
    private String destinatarios;
    private String asuntos;
    private String contenido;
    private String ficherosAdjuntos;

    public final static String TIPOMAIL = "Mail";
    public final static String TIPOTELEGRAM = "Telegram";
    public final static String TIPOPUSH = "Push";

    public MensajeBean() {
        super();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(String destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getAsuntos() {
        return asuntos;
    }

    public void setAsuntos(String asuntos) {
        this.asuntos = asuntos;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFicherosAdjuntos() {
        return ficherosAdjuntos;
    }

    public void setFicherosAdjuntos(String ficherosAdjuntos) {
        this.ficherosAdjuntos = ficherosAdjuntos;
    }

}
