package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 * The Class Municipio. *
 *
 * @author Juan Nieto
 * @version 23.5.2018
 */
public class LocalidadBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigo;

    private ProvinciaBean provincia;

    private String nombre;

    public static final String TITULO = "Localidades ";

    public LocalidadBean() {

    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public ProvinciaBean getProvincia() {
        return provincia;
    }

    public String getProvinciaNombre() {
        return provincia == null ? "" : provincia.getNombre();
    }

    public void setProvincia(ProvinciaBean provincia) {
        this.provincia = provincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
