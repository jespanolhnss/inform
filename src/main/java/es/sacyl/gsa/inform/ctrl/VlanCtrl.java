/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ctrl;

import es.sacyl.gsa.inform.bean.IpBean;
import es.sacyl.gsa.inform.bean.VlanBean;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.IpDao;
import es.sacyl.gsa.inform.util.NumeroBinario;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class VlanCtrl implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(VlanCtrl.class);
    private static final long serialVersionUID = 1L;

    public static String ERROVALIDACION1 = "Error composición máscara. El formatro es direccion /valor ";
    public static String ERROVALIDACION2 = "Error direccin IP";
    public static String ERROVALIDACION3 = "Nº de unos no válido";
    public static String ERROVALIDACION4 = "Error el según valor debe ser un número";

    public static Integer MINIMO_NUMERO_UNOS = 8;
    public static Integer MAXIMO_NUMERO_UNOS = 32;

    /**
     *
     * @param dir en formato 10.10.2.1/24
     * @return Una máscara de red en formato mas1.mas2.cas3.mas4
     *
     * 24 son los unos de la mascara
     *
     * los ceros de la deecha son 32-24
     *
     * Numerobinario tiene un constructor que le pasas el numero de digitos y el
     * tipo y crea un binario con tantos ceros o unos como el número de digitos
     *
     * Numerobinario, tiene el método de rellenar con el caracter por la
     * izquierta con los unos indicados
     *
     * A partir de la cadena de 32 bit trocecamos el nº de 32 bits en grupos de
     * 8 los pasamos cada grupo a a decimal
     *
     */
    public static String getCalculaMascara(String dirIp, String unos) {
        String dirMascara1, dirMascara2, dirMascara3, dirMascara4;
        String[] ip = dirIp.split("\\.");
        if (Utilidades.isNumeric(unos)) {
            int numerodeUnos = Integer.parseInt(unos);
            int numerodeCeros = 32 - numerodeUnos;
            String bianarioBase = new NumeroBinario(numerodeCeros, NumeroBinario.ZERO_CHAR).rellenaIzquierda(32, NumeroBinario.ONE_CHAR);
            dirMascara1 = bianarioBase.substring(0, 8);
            dirMascara2 = bianarioBase.substring(8, 16);
            dirMascara3 = bianarioBase.substring(16, 24);
            dirMascara4 = bianarioBase.substring(24, 32);
            return Utilidades.binarioToDecimalString(dirMascara1) + "." + Utilidades.binarioToDecimalString(dirMascara2) + "."
                    + Utilidades.binarioToDecimalString(dirMascara3) + "." + Utilidades.binarioToDecimalString(dirMascara4);
        } else {
            return "";
        }
    }

    /**
     *
     * @param direccion
     * @return
     */
    public static String getCalculaMascara(String direccion) {
        String[] valores = direccion.split("/");
        return getCalculaMascara(valores[0], valores[1]);
    }

    /**
     *
     * @param direccion direccion ip base
     * @param mascara máscara de red
     * @return
     *
     * Se convierte la direccion y la máscara en binario, se aplican AND y al
     * resultado se pasa a decimal
     *
     * Ejemplo 220.100.100.10/27
     *
     * IP 220.100.100.10 Máscara de red: 255.255.255.255.192
     *
     * ip binario: 11011100.01100100.01100100.00001010
     *
     * mas binario 11111111.11111111.11111111.11100000
     *
     * -----------------------------------------------
     *
     * Subred (and)11011100.01100100.01100100.00000000 mas 1 En decimal
     * 220.100.100.0
     *
     * La puerta de enlace es la direccin base + 1
     */
    public static String getCalculaPuertaEnlace(String direccion, String mascara) {
        String[] ips = direccion.split("\\.");
        String[] mask = mascara.split("\\.");
        /*
        ips[3] = Integer.toString(Integer.parseInt(ips[3]) + 1);
        return ips[0] + "." + ips[1] + "." + ips[2] + "." + ips[3];
         */
        String dirMascara1, dirMascara2, dirMascara3, dirMascara4;
        try {
            NumeroBinario ipBinario = new NumeroBinario(
                    new NumeroBinario(Integer.parseInt(ips[0])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR)
                            .concat(new NumeroBinario(Integer.parseInt(ips[1])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                            .concat(new NumeroBinario(Integer.parseInt(ips[2])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                            .concat(new NumeroBinario(Integer.parseInt(ips[3])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
            );

            NumeroBinario maskBinario = new NumeroBinario(
                    new NumeroBinario(Integer.parseInt(mask[0])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR)
                            .concat(new NumeroBinario(Integer.parseInt(mask[1])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                            .concat(new NumeroBinario(Integer.parseInt(mask[2])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                            .concat(new NumeroBinario(Integer.parseInt(mask[3])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
            );

            String direccionBase = ipBinario.andLogica(maskBinario).getNumero();

            dirMascara1 = direccionBase.substring(0, 8);
            dirMascara2 = direccionBase.substring(8, 16);
            dirMascara3 = direccionBase.substring(16, 24);
            dirMascara4 = direccionBase.substring(24, 32);

            int dir4 = Utilidades.binarioToDecimal(dirMascara4);
            dir4++;

            dirMascara4 = new NumeroBinario(dir4).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR);
            return Utilidades.binarioToDecimalString(dirMascara1) + "." + Utilidades.binarioToDecimalString(dirMascara2) + "."
                    + Utilidades.binarioToDecimalString(dirMascara3) + "." + Utilidades.binarioToDecimalString(dirMascara4);

        } catch (Exception ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        }

        return "";

    }

    /**
     *
     * @param direccion en formato 10.36.64.1/22
     * @return
     */
    public static String getCalculaPuertaEnlace(String direccion) {
        String[] valores = direccion.split("/");
        return getCalculaPuertaEnlace(valores[0], valores[1]);
    }

    /**
     *
     * @param direccion
     * @param mascara
     * @return
     *
     * La dirección broadcast se obtiene poniendo a uno todos los bits de host.
     * Ejemplo 10.36.64.1/22
     *
     * 10.36.67.255	> >	00001010.00100100.01000011.11111111
     */
    public static String getCalculaBroadcast(String dirIp, String unos) {
        String dirBroadcast1, dirBroadcast2, dirBroadcast3, dirBroadcast4;
        String[] ips = dirIp.split("\\.");
        try {
            NumeroBinario ipBinario = new NumeroBinario(
                    new NumeroBinario(Integer.parseInt(ips[0])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR)
                            .concat(new NumeroBinario(Integer.parseInt(ips[1])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                            .concat(new NumeroBinario(Integer.parseInt(ips[2])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                            .concat(new NumeroBinario(Integer.parseInt(ips[3])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
            );

            if (Utilidades.isNumeric(unos)) {
                int numerodeUnos = Integer.parseInt(unos);
                int numerodeCeros = 32 - numerodeUnos;
                NumeroBinario dirHost = new NumeroBinario(numerodeCeros, NumeroBinario.ONE_CHAR);

                String cadenaBinaio = ipBinario.getNumero().substring(0, numerodeUnos) + dirHost.getNumero();

                dirBroadcast1 = cadenaBinaio.substring(0, 8);
                dirBroadcast2 = cadenaBinaio.substring(8, 16);
                dirBroadcast3 = cadenaBinaio.substring(16, 24);
                dirBroadcast4 = cadenaBinaio.substring(24, 32);

                return Utilidades.binarioToDecimalString(dirBroadcast1) + "." + Utilidades.binarioToDecimalString(dirBroadcast2) + "."
                        + Utilidades.binarioToDecimalString(dirBroadcast3) + "." + Utilidades.binarioToDecimalString(dirBroadcast4);
            }
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return "";

    }

    /**
     *
     * @param dirreccion
     * @return
     */
    public static String getCalculaBroadcast(String direccion) {
        String[] valores = direccion.split("/");
        return getCalculaBroadcast(valores[0], valores[1]);
    }

    /**
     *
     * @param dirIp
     * @param unos
     * @return
     *
     * A partir de la direccion de BoradCast resta uno al la ultima ip
     */
    public static String getCalculaUltimaIp(String dirIp, String unos) {
        String ip = getCalculaBroadcast(dirIp, unos);

        String[] ips = ip.split("\\.");
        ips[3] = Integer.toString(Integer.parseInt(ips[3]) - 1);

        return ips[0] + "." + ips[1] + "." + ips[2] + "." + ips[3];

    }

    /**
     *
     * @param direccion
     * @return
     */
    public static String getCalculaUltimaIp(String direccion) {
        String[] valores = direccion.split("/");
        return getCalculaUltimaIp(valores[0], valores[1]);
    }

    /**
     *
     * @param dirIp
     * @param unos
     * @return
     *
     * A partir de la direccion de BoradCast resta uno al la ultima ip
     */
    public static Integer getCalculaNumeroDirecciones(String unos) {
        Integer numero = 0;
        if (Utilidades.isNumeric(unos)) {
            int numerodeUnos = Integer.parseInt(unos);
            int numerodeCeros = 32 - numerodeUnos;
            NumeroBinario nb = new NumeroBinario(numerodeCeros, NumeroBinario.ONE_CHAR);
            numero = Utilidades.binarioToDecimal(nb.getNumero());
        }
        return numero;
    }

    /**
     *
     * @param direcion
     * @return Valida el formato de la dirección dir.dir.dir.dir/unos
     *
     * -1 Error en formato de la cadena. No tiene la
     *
     * -2 Dirección IP no válida
     *
     * -3 Número de unos no válido
     *
     * -4 En número de unos no es un valor numérico
     *
     * 1 Formato válido
     */
    public static Integer isDireccionValida(String direcion) {
        String[] valores = direcion.split("/");
        if (valores.length != 2) {
            return -1;
        }
        if (!IpCtrl.isValid(valores[0])) {
            return -2;
        }
        if (Utilidades.isNumero(valores[1])) {
            Integer valor = Integer.parseInt(valores[1]);
            if (valor < MINIMO_NUMERO_UNOS || valor > MAXIMO_NUMERO_UNOS) {
                return -3;
            }
        } else {
            return -4;
        }
        return 1;
    }

    public void doGeneraIpsDelRango(VlanBean vlanBean) {
        String dir1, dir2, dir3, dir4;
        String[] ips = vlanBean.getPuertaenlace().split("\\.");
        //    NumeroBinario unoBinario = new NumeroBinario(
        //           new NumeroBinario(31, NumeroBinario.ZERO_CHAR).getNumero() + NumeroBinario.ONE_CHAR);

        NumeroBinario unoBinario = new NumeroBinario("1");

        // pasa cada direccion a binario y rellena con ceros por la izqueirda para montar la direccion con los 32 caracteres
        NumeroBinario ipBinario = new NumeroBinario(
                new NumeroBinario(Integer.parseInt(ips[0])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR)
                        .concat(new NumeroBinario(Integer.parseInt(ips[1])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                        .concat(new NumeroBinario(Integer.parseInt(ips[2])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR))
                        .concat(new NumeroBinario(Integer.parseInt(ips[3])).rellenaIzquierda(8, NumeroBinario.ZERO_CHAR)));
        int contador = 0;
        do {
            dir1 = ipBinario.getNumero().substring(0, 8);
            dir2 = ipBinario.getNumero().substring(8, 16);
            dir3 = ipBinario.getNumero().substring(16, 24);
            dir4 = ipBinario.getNumero().substring(24, 32);
            IpBean ip = new IpBean();
            ip.setIp(Utilidades.binarioToDecimalString(dir1) + "." + Utilidades.binarioToDecimalString(dir2) + "."
                    + Utilidades.binarioToDecimalString(dir3) + "." + Utilidades.binarioToDecimalString(dir4));

            ip.setVlan(vlanBean);
            ip.setValoresAut();
            IpDao ipDao = new IpDao();
            // si la ip no existe la inserta si no no hace nada
            if (ipDao.getPorCodigo(ip.getIp()) == null) {
                ip.setId(new ConexionDao().getSiguienteId("ips"));
                ipDao.doInsertaDatos(ip);
            }
            contador++;
            // suma uno para calcular la siguiente direccion y lo guarda en el mismo objeto
            ipBinario = ipBinario.sumar(unoBinario);
        } while (contador < vlanBean.getNumeroDirecciones());

    }
}
