package es.sacyl.gsa.inform.ctrl;

import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.IpBean;
import es.sacyl.gsa.inform.dao.IpDao;
import es.sacyl.gsa.inform.util.PatronesExpRegulares;
import es.sacyl.gsa.inform.util.Utilidades;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author 06551256M
 */
public class IpCtrl {

    /**
     *
     * @param ip
     * @return
     */
    public static Boolean isValid(String ip) {

        if (ip == null || ip.isEmpty()) {
            return true;
        }
        ip = ip.trim();
        if ((ip.length() < 6) & (ip.length() > 15)) {
            return false;
        }

        try {
            Pattern pattern = Pattern.compile(PatronesExpRegulares.IP);
            Matcher matcher = pattern.matcher(ip);
            return matcher.matches();
        } catch (PatternSyntaxException ex) {
            return false;
        }
    }

    /**
     *
     * @param ip
     * @return
     */
    public static Boolean isLibre(String ip) {
        Boolean libre = false;
        if (new IpDao().getPorCodigo(ip).getEquipo() == null) {
            libre = true;
        }
        return libre;
    }

    public static Boolean isLibre(String ip, EquipoBean equipoBean) {
        Boolean libre = false;
        IpBean ipBean = new IpDao().getPorCodigo(ip);
        if (equipoBean == null) {
            if (ipBean != null && ipBean.getEquipo() == null) {
                libre = true;
            }
        } else {
            if (ipBean != null && ipBean.getEquipo() == null) {
                libre = true;

            } else if (ipBean != null && ipBean.getEquipo().getId().equals(equipoBean.getId())) {
                libre = true;
            }
        }
        return libre;
    }

    /**
     *
     * @param ip
     * @return Convierte la ip en formato numero para ordenar
     *
     * 10.36.65.10
     *
     * 10 * 1000 000 000 = 10 000 000 000
     *
     * 36 * 1000 000 = 36 000 000
     *
     * 65 * 1000 = 65 000
     *
     * 10 = 10
     *
     * -------------------------------------
     *
     * 10 036 065 010
     */
    public static Long getValorNumerico(String ip) {
        String[] dirs = ip.split("\\.");
        Long valor = new Long(0);
        if (isValid(ip) && Utilidades.isNumeric(dirs[0]) && Utilidades.isNumeric(dirs[1]) && Utilidades.isNumeric(dirs[2]) && Utilidades.isNumeric(dirs[3])) {
            valor = Long.parseLong(dirs[0]) * 1000000000
                    + Long.parseLong(dirs[1]) * 1000000
                    + Long.parseLong(dirs[2]) * 1000
                    + Long.parseLong(dirs[3]);
        }
        return valor;
    }

    /**
     *
     * @param lista
     * @return valita que la lista de ips separadas por , sea valida
     */
    public static Boolean listaIpsValidas(String lista) {
        Boolean valida = true;
        String[] listaIps = lista.split(",");
        for (String unaIp : listaIps) {
            if (!isValid(unaIp)) {
                valida = false;
            }
        }
        return valida;
    }

    /**
     *
     * @param lista
     * @return
     */
    public static Boolean listaIpsLibres(String lista) {
        Boolean valida = true;
        String[] listaIps = lista.split(",");
        for (String unaIp : listaIps) {
            if (!isLibre(unaIp)) {
                valida = false;
            }
        }
        return valida;
    }

    public static Boolean listaIpsLibres(String lista, EquipoBean equipoBean) {
        Boolean valida = true;
        String[] listaIps = lista.split(",");
        for (String unaIp : listaIps) {
            if (!isLibre(unaIp, equipoBean)) {
                valida = false;
            }
        }
        return valida;
    }
}
