/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ctrl;

import es.sacyl.gsa.inform.util.PatronesExpRegulares;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author 06551256M
 */
public class IpCtrl {

    public static Boolean siValid(String ip) {

        if (ip == null || ip.isEmpty()) {
            return false;
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
        String[] dirs = ip.split(".");
        Long valor = new Long(0);
        valor = Long.parseLong(dirs[3]) * 1000000000
                + Long.parseLong(dirs[2]) * 1000000
                + Long.parseLong(dirs[1]) * 1000
                + Long.parseLong(dirs[0]);
        return valor;
    }
}
