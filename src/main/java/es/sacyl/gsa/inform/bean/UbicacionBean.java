package es.sacyl.gsa.inform.bean;

import es.sacyl.gsa.inform.dao.UbicacionDao;
import java.util.ArrayList;

/**
 *
 * @author juannietopajares
 */
public class UbicacionBean {

    private Long id;
    private CentroBean centro;
    private String descripcion;
    private String descripcionFull;
    private UbicacionBean padre;
    private Integer nivel;

    public UbicacionBean() {
        this.id = new Long(0);
        this.nivel = 0;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return descripcionFull;
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
        return new UbicacionDao().getListaHijos(this);
    }

    public ArrayList<UbicacionBean> getRootUbicaciones() {
        return new UbicacionDao().getListaPadresCentro(this.getCentro());
    }

    @Override
    public String toString() {
        return "UbicacionBean{" + "id=" + id + ", centro=" + centro + ", descripcion=" + descripcion + ", descripcionFull=" + descripcionFull + ", padre=" + padre + ", nivel=" + nivel + '}';
    }

}
