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
public class PeticionesPendientesBean extends MasterBean implements Serializable {
    
    UsuarioBean usuario;
    LocalDate fechaSolicitud;
    AplicacionBean aplicacion;
    Integer idPerfil;
    String tipo;

    public UsuarioBean getUsuario() {
        return usuario;
    }
    
    public Long getIdUsuario() {
        return usuario.id;
    }
    
    public String getNombreUsuario() {
        return usuario.getNombre() + " " + usuario.getApellido1() + " " + usuario.getApellido2();
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }
    
    public void setIdUsuario(Long usuario) {
        this.usuario.id = usuario;
    }
    
    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public AplicacionBean getAplicacion() {
        return aplicacion;
    }
    
    public Long getIdAplicacion() {
        return aplicacion.id;
    }
    
    public String getNombreAplicacion() {
        return aplicacion.getNombre();
    }

    public void setAplicacion(AplicacionBean aplicacion) {
        this.aplicacion = aplicacion;
    }
    
    public void setIdAplicacion(Long idAplicacion) {
        this.aplicacion.id = idAplicacion;
    }
    
    public Integer getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(Integer idPerfil) {
        this.idPerfil = idPerfil;
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

    public void setUsucambio(UsuarioBean usucambio) {
        this.usucambio = usucambio;
    }   
    
}