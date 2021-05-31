package es.sacyl.gsa.inform.ctrl;

import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.dao.ParametroDao;
import java.util.HashMap;

/**
 *
 * @author 06551256M
 */
public class ParametrosCargados {

    private HashMap<String, ParametroBean> parametrosValores = new HashMap<>();

    private static ParametrosCargados misParametros = null;

    public static ParametrosCargados getParametrosCargados() {

        if (misParametros == null) {

            misParametros = new ParametrosCargados();
            misParametros.setValores();
        }
        return misParametros;
    }

    private ParametrosCargados() {

    }

    private void setValores() {
        for (String unParametro : ParametroBean.TODOSLISPARAMETROS) {
            parametrosValores.put(unParametro, new ParametroDao().getPorCodigo(unParametro));
        }
    }

    public HashMap<String, ParametroBean> getParametrosValores() {
        return parametrosValores;
    }

    public void setParametrosValores(HashMap<String, ParametroBean> parametrosValores) {
        this.parametrosValores = parametrosValores;
    }

    public static ParametrosCargados getMisParametros() {
        return misParametros;
    }

    public static void setMisParametros(ParametrosCargados misParametros) {
        ParametrosCargados.misParametros = misParametros;
    }

}
