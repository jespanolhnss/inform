
package es.sacyl.gsa.inform.bean;

/**
 *
 * @author juannietopajares
 */
public class AutonomiaBean {

    private String codigo;
    private String nombre;
    private Integer estado;

    public static AutonomiaBean AUTONOMIADEFECTO = new AutonomiaBean("17", "CASTILLA Y LEON");

    public AutonomiaBean() {

    }

    public AutonomiaBean(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
 public String getEstadoString() {
     if (estado==null) return "";
     else {
        if  ( estado==0) 
            return "N";
        else
            return "S";
     }
    }

 public Boolean  getEstadoBoolena() {
        if  (estado==0) 
            return false;
        else
            return true;
    }
    public void setEstado(String estado) {
        if ( estado.equals("S"))
             this.   estado=1;
        else 
            this.estado=0;
    }
    public void setEstado(Boolean estado) {
        if ( estado==true)
        this.   estado=1;
        else 
            this.estado=0;
    }
}
