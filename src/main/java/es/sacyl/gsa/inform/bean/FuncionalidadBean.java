package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

public class FuncionalidadBean extends MasterBean implements Serializable, Comparable<FuncionalidadBean> {

    private static final long serialVersionUID = 1L;

    private String descripcion;
    private String textomenu;

    public static FuncionalidadBean PEDIRUSUARIO = new FuncionalidadBean(new Long(2));

    public FuncionalidadBean() {
        super();
    }

    public FuncionalidadBean(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTextomenu() {
        return textomenu;
    }

    public void setTextomenu(String textomenu) {
        this.textomenu = textomenu;
    }

    @Override
    public int compareTo(FuncionalidadBean o) {

        if (this.getId() == o.getId()) {
            return 0;
        } else {
            return 1;
        }
    }

}
