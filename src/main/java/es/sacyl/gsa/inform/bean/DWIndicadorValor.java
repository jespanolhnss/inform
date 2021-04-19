package es.sacyl.gsa.inform.bean;

/**
 *
 * @author 06551256M
 */
public class DWIndicadorValor {

    public Long id;
    public Integer ano;
    public Integer mes;
    public String servicio;
    public String areahosp;
    public String centro;
    public DWIndicador indicador;
    public Integer codivarHis;
    public String dimension1;
    public String dimension2;
    public String dimension3;
    public String dimension4;
    public Double valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAreahosp() {
        return areahosp;
    }

    public void setAreahosp(String areahosp) {
        this.areahosp = areahosp;
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

    public void setIndicador(DWIndicador indicador) {
        this.indicador = indicador;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
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

}
