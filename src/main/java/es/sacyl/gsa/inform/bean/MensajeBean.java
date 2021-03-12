package es.sacyl.gsa.inform.bean;

/**
 *
 * @author 06551256M
 */
public class MensajeBean extends MasterBean {

    private String tipo;
    private String destinatarios;
    private String asuntos;
    private String contenido;
    private String ficherosAdjuntos;

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
