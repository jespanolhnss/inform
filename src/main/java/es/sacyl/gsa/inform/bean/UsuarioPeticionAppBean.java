/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author 06532775Q
 */
public class UsuarioPeticionAppBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    UsuarioPeticionBean Peticion;
    AplicacionBean aplicacion;
    Long idPerfil;
    AplicacionPerfilBean perfil;

    public AplicacionPerfilBean getPerfil() {
        return perfil;
    }

    public void setPerfil(AplicacionPerfilBean perfil) {
        this.perfil = perfil;
    }
    String tipo;
    String comentario;

    public UsuarioPeticionBean getPeticion() {
        return Peticion;
    }

    public void setPeticion(UsuarioPeticionBean Peticion) {
        this.Peticion = Peticion;
    }

    public AplicacionBean getAplicacion() {
        return aplicacion;
    }

    public String getAplicacionString() {
        if (aplicacion != null) {
            return aplicacion.getNombre();
        } else {
            return "?";
        }
    }

    public void setAplicacion(AplicacionBean aplicacion) {
        this.aplicacion = aplicacion;
    }

    public Long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public LocalDate getFechacambio() {
        return fechacambio;
    }

    public void setFechacambio(LocalDate fechacambio) {
        this.fechacambio = fechacambio;
    }

    public UsuarioBean getUsucambio() {
        return usucambio;
    }

    public void setUsucambio(UsuarioBean usucambio) {
        this.usucambio = usucambio;
    }

}
