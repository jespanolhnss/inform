package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 * The Class ProvinciaBean.
 *
 * @author Juan Nieto
 * @version 23.5.2018
 */
public class ProvinciaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigo;

    private String nombre;

    private AutonomiaBean autonomia;

    public final static ProvinciaBean PROVINCIA_DEFECTO = new ProvinciaBean("05", "ÁVILA");

    //   public final static ProvinciaBean AVILA = new ProvinciaBean("05", "ÁVILA");
    /**
     * Instantiates a new provincia.
     */
    public ProvinciaBean() {
    }

    /**
     * Instantiates a new provincia.
     *
     * @param codigo the codigo
     * @param descripcion the descripcion
     */
    public ProvinciaBean(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    /**
     * Gets the codigo.
     *
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Sets the codigo.
     *
     * @param codigo the new codigo
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public AutonomiaBean getAutonomia() {
        return autonomia;
    }

    public void setAutonomia(AutonomiaBean autonomia) {
        this.autonomia = autonomia;
    }

}
