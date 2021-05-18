package es.sacyl.gsa.inform.bean;

import es.sacyl.gsa.inform.dao.UbicacionDao;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author juannietopajares
 */
public class UbicacionBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private CentroBean centro;
    private String descripcion;
    private String descripcionFull;
    private UbicacionBean padre;
    private Integer nivel;

    public UbicacionBean() {
        super();
        this.nivel = 0;
    }

    public UbicacionBean(Long id) {
        this.id = id;
    }

    public CentroBean getCentro() {
        return centro;
    }

    public void setCentro(CentroBean centro) {
        this.centro = centro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDescripcionFull() {
        if (descripcionFull != null) {
            return descripcionFull;
        } else {
            return "";
        }
    }

    public void setDescripcionFull(String descripcionFull) {
        this.descripcionFull = descripcionFull;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public UbicacionBean getPadre() {
        return padre;
    }

    public String getPadreString() {
        if (padre != null && padre.getDescripcion() != null) {
            return padre.getDescripcion();
        } else {
            return this.getDescripcion();
        }
    }

    public void setPadre(UbicacionBean padre) {
        this.padre = padre;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public boolean hasLevel() {
        if (nivel == 0) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<UbicacionBean> getHijos() {
        return new UbicacionDao().getListaHijos(this, this.getCentro());
    }

    public ArrayList<UbicacionBean> getRootUbicaciones() {
        return new UbicacionDao().getListaPadresCentro(this.getCentro());
    }

    @Override
    public String toString() {
        return "UbicacionBean{" + "id=" + id + ", centro=" + centro + ", descripcion=" + descripcion + ", descripcionFull=" + descripcionFull + ", padre=" + padre + ", nivel=" + nivel + '}';
    }

}
