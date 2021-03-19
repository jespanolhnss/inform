package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author 06551256M
 *
 */
public class ProveedorBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String direccion;
    private String codpostal;
    private String telefonos;
    private String mail;
    private LocalidadBean localidad;
    private ProvinciaBean provincia;
    // esta atributo no es de bbdd
    private AutonomiaBean autonomia;

    //   nombre,direccion,codpostal,telefonos,mail,localidad,provincia;
    public ProveedorBean() {
        super();
    }

    public ProveedorBean(Long id, String nombre) {
        super();
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodpostal() {
        return codpostal;
    }

    public void setCodpostal(String codpostal) {
        this.codpostal = codpostal;
    }

    public String getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public LocalidadBean getLocalidad() {
        return localidad;
    }

    public void setLocalidad(LocalidadBean localidad) {
        this.localidad = localidad;
    }

    public ProvinciaBean getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaBean provincia) {
        this.provincia = provincia;
    }

    public AutonomiaBean getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(AutonomiaBean autonomia) {
        this.autonomia = autonomia;
    }

}
