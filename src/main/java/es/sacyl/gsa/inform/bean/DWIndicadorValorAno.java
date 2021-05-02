package es.sacyl.gsa.inform.bean;

/**
 *
 * @author 06551256M
 */
public class DWIndicadorValorAno {

    private DWIndicador dwindicador;
    private Integer ano;
    int[] mesesTotales = new int[13];

    public DWIndicador getDwindicador() {
        return dwindicador;
    }

    public void setDwindicador(DWIndicador dwindicador) {
        this.dwindicador = dwindicador;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public int[] getMesesTotales() {
        return mesesTotales;
    }

    public void setMesesTotales(int[] mesesTotales) {
        this.mesesTotales = mesesTotales;
    }

    public Integer[] getMesesNoTotal() {
        Integer[] meses = new Integer[12];
        for (int i = 0; i < 12; i++) {
            meses[i] = mesesTotales[i];
        }
        return meses;
    }

}
