package es.sacyl.gsa.inform.bean;

/**
 *
 * @author 06551256M
 */
public class DWIndicadorValor extends MasterBean {

    public Integer ano;
    public Integer mes;
    public String servicio;

    public String centro;
    public DWIndicador indicador;
    public Integer codivarHis;
    public String dimension1;
    public String dimension2;
    public String dimension3;
    public String dimension4;
    public Integer valor;

    public DWIndicadorValor() {
        super();
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public DWIndicador getIndicador() {
        return indicador;
    }

    public String getIndicadorNombre() {
        if (indicador != null && indicador.getNombre() != null) {
            return indicador.getNombre();
        } else {
            return "";
        }
    }

    public void setIndicador(DWIndicador indicador) {
        this.indicador = indicador;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getCodivarHis() {
        return codivarHis;
    }

    public void setCodivarHis(Integer cocodivarHis) {
        this.codivarHis = cocodivarHis;
    }

    public String getDimension1() {
        return dimension1;
    }

    public void setDimension1(String dimension1) {
        this.dimension1 = dimension1;
    }

    public String getDimension2() {
        return dimension2;
    }

    public void setDimension2(String dimension2) {
        this.dimension2 = dimension2;
    }

    public String getDimension3() {
        return dimension3;
    }

    public void setDimension3(String dimension3) {
        this.dimension3 = dimension3;
    }

    public String getDimension4() {
        return dimension4;
    }

    public void setDimension4(String dimension4) {
        this.dimension4 = dimension4;
    }

    @Override
    public String toString() {
        return "DWIndicadorValor{" + "ano=" + ano + ", mes=" + mes + ", servicio=" + servicio + ", centro=" + centro + ", indicador=" + indicador + ", codivarHis=" + codivarHis + ", dimension1=" + dimension1 + ", dimension2=" + dimension2 + ", dimension3=" + dimension3 + ", dimension4=" + dimension4 + ", valor=" + valor + '}';
    }

}
