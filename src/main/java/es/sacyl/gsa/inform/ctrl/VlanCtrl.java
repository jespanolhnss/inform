/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ctrl;

import es.sacyl.gsa.inform.util.NumeroBinario;
import es.sacyl.gsa.inform.util.Utilidades;

/**
 *
 * @author 06551256M
 */
public class VlanCtrl {

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
        //     String[] valores = direcion.split("/");
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
     * Subred (and)11011100.01100100.01100100.00000000
     *
     * En decimal 220.100.100.0
     */
    public static String getCalcualPuertaEnlace(String direccion, String mascara) {
        String[] ips = direccion.split("\\.");
        String[] mask = mascara.split("\\.");
        String dirMascara1, dirMascara2, dirMascara3, dirMascara4;

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

        return Utilidades.binarioToDecimalString(dirMascara1) + "." + Utilidades.binarioToDecimalString(dirMascara2) + "."
                + Utilidades.binarioToDecimalString(dirMascara3) + "." + Utilidades.binarioToDecimalString(dirMascara4) + 1;
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
        if (!IpCtrl.siValid(valores[0])) {
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
}
