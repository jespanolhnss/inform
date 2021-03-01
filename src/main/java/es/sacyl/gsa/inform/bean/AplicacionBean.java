package es.sacyl.gsa.inform.bean;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public class AplicacionBean extends MasterBean {

    //id,nombre,proveedor,ambito,gestionusuarios,descripcion,servicio,fechainstalacion,estado,usucambio,fechacambio
    private String nombre;
    private ProveedorBean proveedor;
    private String ambito;
    private String gestionUsuarios;
    private String descripcion;
    private GfhBean gfh;
    private LocalDate fechaInstalacion;

    private ArrayList<AplicacionPerfilBean> listaPerfiles = new ArrayList<>();

    public AplicacionBean() {
        super();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ProveedorBean getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorBean proveedor) {
        this.proveedor = proveedor;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getGestionUsuarios() {
        return gestionUsuarios;
    }

    public void setGestionUsuarios(String gestionUsuarios) {
        this.gestionUsuarios = gestionUsuarios;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public GfhBean getGfh() {
        return gfh;
    }

    public void setGfh(GfhBean gfh) {
        this.gfh = gfh;
    }

    public LocalDate getFechaInstalacion() {
        return fechaInstalacion;
    }

    public void setFechaInstalacion(LocalDate fechaInstalacion) {
        this.fechaInstalacion = fechaInstalacion;
    }

    public ArrayList<AplicacionPerfilBean> getListaPerfiles() {
        return listaPerfiles;
    }

    public void setListaPerfiles(ArrayList<AplicacionPerfilBean> listaPerfiles) {
        this.listaPerfiles = listaPerfiles;
    }

}
