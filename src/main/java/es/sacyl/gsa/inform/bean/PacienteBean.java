package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDate;

public class PacienteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String apellidosnombre;
    private Long idJimena;
    private String numerohc;
    private int estado;
    private UsuarioBean usucambio;
    private LocalDate fechacambio;

    public PacienteBean() {
    }

    public PacienteBean(String numerohc) {
        this.numerohc = numerohc;
    }

    public PacienteBean(String numerohc, String apellidosnombre) {
        this.numerohc = numerohc;
        this.apellidosnombre = apellidosnombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApellidosnombre() {
        if (this.apellidosnombre == null) {
            return "";
        } else if (this.apellidosnombre.isEmpty()) {
            return "";
        } else {
            return apellidosnombre;
        }
    }

    public void setApellidosnombre(String apellidosnombre) {

        this.apellidosnombre = apellidosnombre;
    }

    public Long getIdJimena() {
        return idJimena;
    }

    public void setIdJimena(Long idJimena) {
        this.idJimena = idJimena;
    }

    public String getNumerohc() {
        return numerohc;
    }

    public void setNumerohc(String numerohc) {
        this.numerohc = numerohc;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public UsuarioBean getUsucambio() {
        return usucambio;
    }

    public void setUsucambio(UsuarioBean usucambio) {
        this.usucambio = usucambio;
    }

    public LocalDate getFechacambio() {
        return fechacambio;
    }

    public void setFechacambio(LocalDate fechacambio) {
        this.fechacambio = fechacambio;
    }

}
