package es.sacyl.gsa.inform.bean;

import java.io.Serializable;

/**
 *
 * @author 06551256M
 */
public class DatoGenericoBean extends MasterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idDatoEqipo;
    private String tipoDato;
    private String valor;

    public DatoGenericoBean() {
        super();
    }

    public Long getIdDatoEqipo() {
        return idDatoEqipo;
    }

    public void setIdDatoEqipo(Long idDatoEqipo) {
        this.idDatoEqipo = idDatoEqipo;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
