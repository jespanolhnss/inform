package es.sacyl.gsa.inform.bean;

import es.sacyl.gsa.inform.util.Constantes;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LopdDocumentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private LopdIncidenciaBean idIncidenica;
    private File ficheroAdjunto;
    private LocalDate fecha;
    private Integer hora;
    private UsuarioBean usuCambio;
    private LocalDate fechaCambio;
    private int estado;
    private long ifinformejimena ;

    private final String PREFIJONOMBREPARAPDF = "DocumentoIncidencia";
    public LopdDocumentoBean() {
        this.id = new Long(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LopdIncidenciaBean getIdIncidenica() {
        return idIncidenica;
    }

    public void setIdIncidenica(LopdIncidenciaBean idIncidenica) {
        this.idIncidenica = idIncidenica;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getFechaHoraFormato() {
        DateTimeFormatter fechaformato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (this.fecha != null) {
            return fechaformato.format(this.fecha);
        } else {
            return "";
        }
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public UsuarioBean getUsuCambio() {
        return usuCambio;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setUsuCambio(UsuarioBean usuCambio) {
        this.usuCambio = usuCambio;
    }

    public LocalDate getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDate fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

 

    public File getFicheroAdjunto() {
        return ficheroAdjunto;
    }

    public void setFicheroAdjunto(File ficheroAdjunto) {
        this.ficheroAdjunto = ficheroAdjunto;
    }

    public long getIfinformejimena() {
        return ifinformejimena;
    }

    public void setIfinformejimena(long ifinformejimena) {
        this.ifinformejimena = ifinformejimena;
    }
    
    
        public String getNombreFicheroPdf() {
        return  PREFIJONOMBREPARAPDF+ this.id + ".pdf";
    }

      public String getPathAbsolutePdf() {
        return Constantes.PDFPATHABSOLUTO + getNombreFicheroPdf()  ;
    }

    public String getUrlFile() {
        return "http://localhost:8080" + "/"  + Constantes.PDFDIRECTORIO+ "/"+ getNombreFicheroPdf() ;
    }

}
