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
}
