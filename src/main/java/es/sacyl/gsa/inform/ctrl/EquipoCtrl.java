package es.sacyl.gsa.inform.ctrl;

import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.dao.ParametroDao;
import es.sacyl.gsa.inform.util.Utilidades;

/**
 *
 * @author 06551256M
 */
public class EquipoCtrl {

    public Integer getSiguienteNumeroInventario() {
        int valor = 0;
        String cadena = new ParametroDao().getPorCodigo(ParametroBean.INVENTIARIO_SIGUIENTEVALOR).getValor();
        if (Utilidades.isNumeric(cadena)) {
            valor = Integer.parseInt(cadena);
            valor++;
            ParametroBean param = new ParametroBean();
            param.setCodigo(ParametroBean.INVENTIARIO_SIGUIENTEVALOR);
            param.setValor(Integer.toString(valor));
            new ParametroDao().doActualizaValor(param);
        }
        return valor;
    }

}
