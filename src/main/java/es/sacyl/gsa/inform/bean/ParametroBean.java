/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author 06551256M
 */
public class ParametroBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String codigo;
    private String descripcion;
    private String valor;

//    public static String MAIL_DIRECCION_ORIGEN = "mail.direccion.origen";	// Dirección origen mail
    public static String MAIL_USER = "mail.smtp.user";	// Usuario mail del servidor
    public static String MAIL_SENDER = "mail.smtp.mail.sender"; //Usuario remitente
    public static String MAIL_PORT = "mail.smtp.port"; //	Puerto del correo
    public static String MAIL_PASSWORD = "mail.password";//	Password del usuario conexión  servidor
    public static String MAIL_HOST = "mail.smtp.host"; //	Host del correo
    public static String MAIL_AUTH = "mail.smtp.auth"; //	Auth

    public static String MAIL_LDAP_DESTINOLOPD = "mail.ldap.destinolopd"; //	Destinatarios correo lopd para gestion de incidencias

    public static String URL_CONEXION_HIS = "url.conexion.his";
    public static String URL_CONEXION_FARMCIA = "url.conexion.farmacia";
    public static String URL_CONEXION_TURNOS = "url.conexion.turnos";
    public static String URL_CONEXION_GACELA = "url.conexion.gacela";
    public static String URL_CONEXION_JIMENA = "url.conexion.jimena";

    public static String USR_INFORMATICOS = "usr.informaticos";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
