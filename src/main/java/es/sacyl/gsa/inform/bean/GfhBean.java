package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

public class GfhBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigo;
    private Long idjimena;
    private String descripcion;
    private Integer asistencial;
    private String gfhpersigo;
    private UsuarioBean jefe;
    private String division;

    public static final Long SERVICIOIDTRAUMAJIMEAN = new Long(51);
    public static final Integer ASISTENCIASI = 1;

    public GfhBean() {
        super();
        this.descripcion = "";
        this.codigo = "";
        this.idjimena = new Long(0);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getIdjimena() {
        return idjimena;
    }

    public void setIdjimena(Long idjimena) {
        this.idjimena = idjimena;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getAsistencial() {
        return asistencial;
    }

    public void setAsistencial(Integer asistencial) {
        this.asistencial = asistencial;
    }

    public String getAsistencialString() {
        if (asistencial == null) {
            return "";
        } else {
            if (asistencial == 0) {
                return "N";
            } else {
                return "S";
            }
        }
    }

    public Boolean getAsistencialBoolean() {
        if (asistencial == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setAsistencia(String asistencial) {
        if (asistencial != null && asistencial.equals("S")) {
            this.asistencial = 1;
        } else {
            this.asistencial = 0;
        }
    }

    public String getGfhpersigo() {
        return gfhpersigo;
    }

    public DatoGenericoBean getGfhpersigoDato() {
        return new DatoGenericoBean(gfhpersigo, gfhpersigo);
    }

    public void setGfhpersigo(String gfhpersigo) {
        this.gfhpersigo = gfhpersigo;
    }

    public void setGfhpersigo(DatoGenericoBean gfhpersigo) {
        this.gfhpersigo = gfhpersigo.getTipoDato();
    }

    public UsuarioBean getJefe() {
        return jefe;
    }

    public void setJefe(UsuarioBean jefe) {
        this.jefe = jefe;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    @Override
    public String toString() {
        return "GfhBean{" + "codigo=" + codigo + ", idjimena=" + idjimena + ", descripcion=" + descripcion + ", asistencial=" + asistencial + '}';
    }

}
