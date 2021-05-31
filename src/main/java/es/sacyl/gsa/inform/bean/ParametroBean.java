/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.util.ArrayList;

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

    public static String URL_CONEXION_FARMCIA = "url.conexion.farmacia";
    public static String URL_CONEXION_TURNOS = "url.conexion.turnos";
    public static String URL_CONEXION_GACELA = "url.conexion.gacela";
    public static String URL_CONEXION_JIMENA = "url.conexion.jimena";
    public static String URL_CONEXION_GALENO = "url.conexion.galeno";
    public static String URL_CONEXION_CLINICA = "url.conexion.calinica";

    // IP del servidor dodne está instalada la instancia de la app
    public static String URL_INSTANCIASERVIDOR = "url.instanciaservidor";

    public static String USR_INFORMATICOS = "usr.informaticos";

    public static String PRINT_ETIQUETAS = "print.etitquetas";

    public static String VALORDEFECTO_TIPOCENTRO = "valordefecto.tipocentro";

    public static String INVENTIARIO_SIGUIENTEVALOR = "inventario.siguientevalor";

    public static ArrayList<String> TODOSLISPARAMETROS = new ArrayList<String>() {
        {
            add(ParametroBean.MAIL_USER);
            add(ParametroBean.MAIL_SENDER);
            add(ParametroBean.MAIL_PORT);
            add(ParametroBean.MAIL_PASSWORD);
            add(ParametroBean.MAIL_HOST);
            add(ParametroBean.MAIL_AUTH);
            add(ParametroBean.MAIL_LDAP_DESTINOLOPD);
            add(ParametroBean.URL_CONEXION_FARMCIA);
            add(ParametroBean.URL_CONEXION_TURNOS);
            add(ParametroBean.URL_CONEXION_GACELA);
            add(ParametroBean.URL_CONEXION_JIMENA);
            add(ParametroBean.URL_CONEXION_GALENO);
            add(ParametroBean.URL_CONEXION_CLINICA);
            add(ParametroBean.URL_INSTANCIASERVIDOR);
            add(ParametroBean.USR_INFORMATICOS);
            add(ParametroBean.PRINT_ETIQUETAS);
            add(ParametroBean.VALORDEFECTO_TIPOCENTRO);
            add(ParametroBean.INVENTIARIO_SIGUIENTEVALOR);
        }
    };

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
