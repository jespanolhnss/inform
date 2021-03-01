package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LopdNotaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long idIncidenciaLong;
    private LocalDate fecha;
    private Integer hora;
    private String descripcion;
    private UsuarioBean usucambio;
    private int estado;

    public LopdNotaBean() {
        this.id = new Long(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdString() {
        return Long.toString(this.id);
    }

    public Long getIdIncidenciaLong() {
        return idIncidenciaLong;
    }

    public void setIdIncidenciaLong(Long idIncidenciaLong) {
        this.idIncidenciaLong = idIncidenciaLong;
    }

    public LocalDate getFecha() {
        return fecha;
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

    public String getFechaHoraFormato() {
        DateTimeFormatter fechaformato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (this.fecha != null) {
            return fechaformato.format(this.fecha);
        } else {
            return "";
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public UsuarioBean getUsucambio() {
        return usucambio;
    }

    public void setUsucambio(UsuarioBean usucambio) {
        this.usucambio = usucambio;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

}
