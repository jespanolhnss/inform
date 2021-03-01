package es.sacyl.gsa.inform.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class LopdSujetoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String descripcion;

    public static int PACIENTE = 1;
    public static int TRABAJADOR = 2;
    public static int OTROS = 3;
    public static int USUARIO = 4;

    public static LopdSujetoBean SUJETO_PACIENTE = new LopdSujetoBean(PACIENTE, "Paciente");
    public static LopdSujetoBean SUJETO_TRABAJADOR = new LopdSujetoBean(TRABAJADOR, "Trabajador");
    public static LopdSujetoBean SUJETO_OTROS = new LopdSujetoBean(OTROS, "Otros");
    public static LopdSujetoBean SUJETO_USUARIO = new LopdSujetoBean(USUARIO, "Usuario");

    @SuppressWarnings("serial")
    public static ArrayList<LopdSujetoBean> LISTASUJETOS_COMPLETA = new ArrayList<LopdSujetoBean>() {
        {
            add(SUJETO_PACIENTE);
            add(SUJETO_TRABAJADOR);
            add(SUJETO_OTROS);
            add(SUJETO_USUARIO);
        }
    };
    @SuppressWarnings("serial")
    public static ArrayList<LopdSujetoBean> LISTASUJETOS_SINUSUARIOS = new ArrayList<LopdSujetoBean>() {
        {
            add(SUJETO_PACIENTE);
            add(SUJETO_TRABAJADOR);
            add(SUJETO_OTROS);
        }
    };

    public LopdSujetoBean(int i, String descripcion) {
        this.id = i;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public static String getDescripcionFromTipo(int tipo) {
        if (tipo == PACIENTE) {
            return "Pacinte";
        } else if (tipo == TRABAJADOR) {
            return "Trabajador";
        } else if (tipo == OTROS) {
            return "Otros";
        } else if (tipo == USUARIO) {
            return "Usuario";
        } else {
            return "";
        }
    }

    public static LopdSujetoBean getTipoSujeto(LopdTipoBean tipo) {
        switch (tipo.getSujeto().getId()) {
            case 1:
                return LopdSujetoBean.SUJETO_PACIENTE;
            case 2:
                return LopdSujetoBean.SUJETO_TRABAJADOR;
            case 3:
                return LopdSujetoBean.SUJETO_OTROS;
            case 4:
                return LopdSujetoBean.SUJETO_USUARIO;
        }
        return null;
    }

    @Override
    public String toString() {
        return "LopdSujeto [id=" + id + ", descripcion=" + descripcion + "]";
    }

}
