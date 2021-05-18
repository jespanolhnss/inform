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
public class UsuarioPeticionBean extends MasterBean implements Serializable {

    UsuarioBean usuario;
    UsuarioBean peticionario;
    LocalDate fechaSolicitud;
    String centros;
    String comentario;
    String tipo;

    public UsuarioBean getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }

    public UsuarioBean getPeticionario() {
        return peticionario;
    }

    public void setPeticionario(UsuarioBean peticionario) {
        this.peticionario = peticionario;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getCentros() {
        return centros;
    }

    public void setCentros(String centros) {
        this.centros = centros;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getUsuarioNombre() {
        if (usuario != null) {
            return usuario.getApellidosNombre();
        } else {
            return "";
        }
    }

    public void setUsucambio(UsuarioBean usucambio) {
        this.usucambio = usucambio;
    }

}
