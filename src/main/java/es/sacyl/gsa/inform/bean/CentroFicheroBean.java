package es.sacyl.gsa.inform.bean;

import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author juannietopajares
 */
public class CentroFicheroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private CentroBean centro;
    private String descripcion;
    private Blob ficheroBlobs;
    private String nombreFichero;
    private String nombreFicheroMiniatura;
    private Integer estado;
    private UsuarioBean usuarioCambio;
    private LocalDate fechaCambio;
    private byte[] imageBytes;

    private InputStream streamInputStream;

    public static ArrayList<String> TIPOFICHEROIMG = new ArrayList<String>() {
        {
            add("jpg");
            add("jpeg");
            add("png");
        }
    };

    public static ArrayList<String> TIPOFICHEROPDF = new ArrayList<String>() {
        {
            add("pdf");

        }
    };

    public CentroFicheroBean() {
        this.id = new Long(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CentroBean getCentro() {
        return centro;
    }

    public void setCentro(CentroBean centro) {
        this.centro = centro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDescripcionCorta() {
        if (descripcion == null) {
            return "";
        }

        if (descripcion != null && descripcion.length() < 30) {
            return descripcion;
        } else {
            return descripcion.substring(0, 29) + "....";
        }
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Blob getFicheroBlobs() {
        return ficheroBlobs;
    }

    public void setFicheroBlobs(Blob ficheroBlobs) {
        this.ficheroBlobs = ficheroBlobs;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public UsuarioBean getUsuarioCambio() {
        return usuarioCambio;
    }

    public void setUsuarioCambio(UsuarioBean usuarioCambio) {
        this.usuarioCambio = usuarioCambio;
    }

    public LocalDate getFechaCambio() {
        return fechaCambio;
    }

    public String getFechaCambioFormato() {
        return Utilidades.getFechadd_mm_yyyy(fechaCambio);
    }

    public void setFechaCambio(LocalDate fechaCambio) {
        this.fechaCambio = fechaCambio;
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
        return "http://localhost:8080" + Constantes.PDFURL + nombreFichero;
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

    public String getNombreFicheroNoExtension() {
        if (getNombreFichero() != null && getNombreFichero().length() > 4) {
            return nombreFichero.substring(0, nombreFichero.length() - 4);
        } else {
            return "";
        }
    }

    public String getExtensionFichero() {
        String extension = "";
        /*
        if (getNombre() != null && getNombre().length() > 5) {
            extension = getNombre().trim().substring(getNombre().trim().length() - 3, getNombre().trim().length());
        }
         */
        //   int posPunto = getNombre().indexOf(".");
        int posPunto = getNombreFichero().lastIndexOf(".");
        if (posPunto != -1) {
            extension = getNombreFichero().substring(posPunto, getNombreFichero().length());
        }
        return extension;
    }

}
