package es.sacyl.gsa.inform.bean;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServletRequest;
import es.sacyl.gsa.inform.dao.ParametroDao;
import es.sacyl.gsa.inform.util.Constantes;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;

/**
 *
 * @author 06551256M
 */
public class DatoGenericoBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idDatoEqipo;
    private Long idDatoAplicacion;
    private String tipoDato;
    private String valor;
    private Integer valorInt;
// para cuando el dato generico incluya un fichero
    private Blob ficheroBlobs;
    private String nombreFichero;
    private String nombreFicheroMiniatura;
    private byte[] imageBytes;
    private InputStream streamInputStream;
//

    public DatoGenericoBean() {
        super();
    }

    public DatoGenericoBean(String tipo, String valor) {
        this.tipoDato = tipo;
        this.valor = valor;
    }

    public Long getIdDatoEqipo() {
        return idDatoEqipo;
    }

    public void setIdDatoEqipo(Long idDatoEqipo) {
        this.idDatoEqipo = idDatoEqipo;
    }

    public String getTipoDato() {
        if (tipoDato != null) {
            return tipoDato;
        } else {
            return "";
        }
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getValor() {
        return valor;
    }

    public String getValorAncho(int ancho) {
        if (valor != null) {
            if (valor.length() < ancho) {
                return valor;
            } else {
                return (valor.substring(0, ancho - 1));

            }
        } else {
            return "";
        }
    }

    public String getValorAncho25() {
        return getValorAncho(25);
    }

    public String getValorAncho50() {
        return getValorAncho(50);
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Long getIdDatoAplicacion() {
        return idDatoAplicacion;
    }

    public void setIdDatoAplicacion(Long idDatoAplicacion) {
        this.idDatoAplicacion = idDatoAplicacion;
    }

    public Integer getValorInt() {
        return valorInt;
    }

    public void setValorInt(Integer valorInt) {
        this.valorInt = valorInt;
    }

    public Blob getFicheroBlobs() {
        return ficheroBlobs;
    }

    public void setFicheroBlobs(Blob ficheroBlobs) {
        this.ficheroBlobs = ficheroBlobs;
    }

    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    public String getNombreFicheroMiniatura() {
        return nombreFicheroMiniatura;
    }

    /*
    public String getExtensionFichero() {
        String extension = "";

        int posPunto = getNombreFichero().lastIndexOf(".");
        if (posPunto != -1) {
            extension = getNombreFichero().substring(posPunto, getNombreFichero().length());
        }
        return extension;
    }
     */
    public String getExtensionFichero() {
        return new FicheroNombre(getNombreFichero()).getExtension();
    }

    public String getNombreFicheroNoExtension() {
        return new FicheroNombre(getNombreFichero()).getNombreSinExtension();
    }

    public void setNombreFicheroMiniatura(String nombreFicheroMiniatura) {
        this.nombreFicheroMiniatura = nombreFicheroMiniatura;
    }

    public String getPathAbsoluto() {
        return Constantes.PDFPATHABSOLUTO + nombreFichero;
    }

    public String getPathRelativo() {
        return Constantes.PDFPATHRELATIVO + nombreFichero;
    }

    public String getPathRelativoMiniatura() {
        return Constantes.PDFPATHRELATIVO + nombreFicheroMiniatura;
    }

    public String getUrlFichero() {
        String urlDelPdf = "";
        String cadena = Constantes.PDFURL + nombreFichero;
        String adr, port;
        VaadinRequest currentRequest = VaadinRequest.getCurrent();
        VaadinServletRequest vaadinServletRequest = null;
        if (currentRequest instanceof VaadinServletRequest) {
            vaadinServletRequest = (VaadinServletRequest) currentRequest;
            adr = vaadinServletRequest.getLocalAddr();
            if (adr.charAt(0) == "0".charAt(0)) {
                adr = "localhost";
            }
            port = Integer.toString(vaadinServletRequest.getLocalPort());
            urlDelPdf = "http://" + adr + System.getProperty("file.separator") + cadena;
        } else {
            adr = new ParametroDao().getPorCodigo(ParametroBean.URL_INSTANCIASERVIDOR).getValor();
            urlDelPdf = "http://" + adr + System.getProperty("file.separator") + cadena;
        }
        return urlDelPdf;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public InputStream getStreamInputStream() {
        return streamInputStream;
    }

    public void setStreamInputStream(InputStream streamInputStream) {
        this.streamInputStream = streamInputStream;
    }

}
