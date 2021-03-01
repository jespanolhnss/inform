package es.sacyl.gsa.inform.util;

/**
 *
 * @author 06551256M
 */
/**
 *
 * @author andreas
 *
 * Copia de internet
 *
 * Almacena en un string un numero binario
 *
 */
public class NumeroBinario {

    /**
     * Constranstes que definen el = y el 1
     */
    public final static char ZERO_CHAR = '0';
    public final static char ONE_CHAR = '1';

    /**
     * El String que contiene la secuencias de 0 y 1 : numero
     */
    private String numero;

    /**
     *
     * @param numero
     *
     * Crea el numero binario a partir del String que sólo debe tener 0 y 1
     */
    public NumeroBinario(String numero) {
        // Hay que validar que sea un n&uacute;mero binario!!!
        if (!this.validaNumeroBinario(numero)) {
            throw new IllegalArgumentException("El número  no es binario");
        }
        this.numero = numero;
    }

    /**
     *
     * @param numeroElementos
     * @param ceroUno Crea el número binario con tantos cero o unos como se
     * indican en numeroElementos y con el bit generador ceroUno
     */
    public NumeroBinario(int numeroElementos, char ceroUno) {
        if (ceroUno == ZERO_CHAR || ceroUno == ONE_CHAR) {
            this.numero = this.rellenaIzquierda("", numeroElementos, ceroUno);
        } else {
            throw new IllegalArgumentException("El caracter generador  no es  0 o 1 ");
        }
    }

    /**
     *
     * @param numerodecimal El numero entero en sistema decimal lo pasa a
     * binario mediante divisiones sucesibas
     */
    public NumeroBinario(int numerodecimal) {
        this.numero = this.integerToBiario(numerodecimal);
    }

    /**
     *
     * @param numero
     * @return una cadena de 0 y 1 a paritir del entero del parámetros
     *
     */
    public static String integerToBiario(Integer numero) {
        /**
         * variable donde se van almacenando la cadena de bit
         */
        String binario = "";
        while (numero > 0) {
            //Utilización de if simplificado en el cual se verifica si el residuo de la división es 0 en caso de cumplirse se agrega 0 a la var aux.
            binario = numero % 2 == 0 ? "0" + binario : "1" + binario;
            numero = numero / 2; //Esta condición es la permite salir del bucle.
        }
        return binario;
    }

    /**
     *
     * @param numero
     * @return Valida que el número sea binario, sólo tiene 0 y 1
     */
    private boolean validaNumeroBinario(String numero) {
        final String REGEX = "[01]+";
        return numero.matches(REGEX);
    }

    /**
     *
     * @param numero Set del número
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     *
     * @return Get del número
     */
    public String getNumero() {
        return numero;
    }

    /**
     *
     * @param numero. Cadena que se va a rellenar con bit por la izquierda
     * @param longitud. Número de bit a rellenar
     * @param charrelleno. bit con el que se quiere rellenar
     * @return Rellena con bit por la izquierda el string numero hasta completar
     * la longitud con el bit de relleno indicado
     *
     */
    public String rellenaIzquierda(String numero, int longitud, char charrelleno) {
        if (numero == null) {
            numero = getNumero();
        }
        if (longitud == numero.length()) {
            return numero;
        }
        int missing = longitud - numero.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < missing; i++) {
            sb.append(charrelleno);
        }
        sb.append(numero);
        return sb.toString();
    }

    public String rellenaIzquierda(int longitud, char charrelleno) {
        return rellenaIzquierda(this.numero, longitud, charrelleno);
    }

    // M&eacute;todo que rellena con ceros por la derecha
    public String fillWithTrailingZeros(String numero, int longitud) {

        if (longitud == numero.length()) {

            return numero;

        }

        StringBuilder sb = new StringBuilder();
        sb.append(numero);

        for (int i = 0; i < longitud; i++) {

            sb.append(ZERO_CHAR);

        }

        return sb.toString();

    }

    /**
     *
     * @param otroNumero
     * @return Suma en número actual con el pasado por parámetro
     */
    public NumeroBinario sumar(NumeroBinario otroNumero) {

        int maxLength = Math.max(this.numero.length(), otroNumero.numero.length());

        String zfNumero = rellenaIzquierda(this.numero, maxLength, ZERO_CHAR);
        String zfOtroNumero = rellenaIzquierda(otroNumero.numero, maxLength, ZERO_CHAR);

        char carry = ZERO_CHAR;
        char[] result = new char[maxLength + 1];

        for (int i = maxLength - 1; i >= 0; i--) {

            char zfNumeroCharAtI = zfNumero.charAt(i);
            char zfOtroNumeroCharAtI = zfOtroNumero.charAt(i);

            if (zfNumeroCharAtI == zfOtroNumeroCharAtI) {

                if (ZERO_CHAR == zfNumeroCharAtI) {

                    if (ONE_CHAR == carry) {
                        result[i + 1] = ONE_CHAR;
                        carry = ZERO_CHAR;
                    } else {
                        result[i + 1] = ZERO_CHAR;
                        carry = ZERO_CHAR;
                    }

                } else {

                    if (ONE_CHAR == carry) {
                        result[i + 1] = ONE_CHAR;
                        carry = ONE_CHAR;
                    } else {
                        result[i + 1] = ZERO_CHAR;
                        carry = ONE_CHAR;
                    }

                }

            } else {

                if (ONE_CHAR == carry) {
                    result[i + 1] = ZERO_CHAR;
                    carry = ONE_CHAR;
                } else {
                    result[i + 1] = ONE_CHAR;
                    carry = ZERO_CHAR;
                }

            }

        }

        result[0] = carry;

        return new NumeroBinario(new String(result).trim());
    }

    /**
     *
     * @param otroNumero
     * @return
     *
     * Resta el n&uacute;mero binario actual con el pasado como argumento
     */
    public NumeroBinario restar(NumeroBinario otroNumero) {

        int maxLength = Math.max(this.numero.length(), otroNumero.numero.length());

        String zfOtroNumero = rellenaIzquierda(otroNumero.numero, maxLength, ZERO_CHAR);

        char[] complement = new char[maxLength];

        for (int i = 0; i < maxLength; i++) {

            complement[i] = ZERO_CHAR == zfOtroNumero.charAt(i) ? ONE_CHAR : ZERO_CHAR;

        }

        NumeroBinario one = new NumeroBinario(Character.toString(ONE_CHAR));
        NumeroBinario tmp = new NumeroBinario(new String(complement));

        NumeroBinario result = tmp.sumar(one);

        result = this.sumar(result);

        result.numero = result.getNumero().substring(2);

        return result;

    }

    /**
     *
     * @param otroNumero
     * @return
     *
     * Método que multiplica el n&uacute;mero binario actual con el pasado como
     * argumento
     */
    public NumeroBinario multiplicar(NumeroBinario otroNumero) {
        int maxLength = otroNumero.numero.length();
        NumeroBinario sum = new NumeroBinario(Character.toString(ZERO_CHAR));
        int limit = maxLength - 1;

        for (int i = limit; i >= 0; i--) {
            if (otroNumero.numero.charAt(i) == ONE_CHAR) {
                String trail = this.fillWithTrailingZeros(this.numero, limit - i);
                NumeroBinario tmp = new NumeroBinario(trail);
                sum = sum.sumar(tmp);
                sum.setNumero(sum.numero.substring(1));
            }

        }

        return sum;
    }

    /**
     *
     * @param otroNumero
     * @return
     *
     * operación and bit a bit
     *
     *  * Los números tienen que tener la misma longitud
     */
    public NumeroBinario andLogica(NumeroBinario otroNumero) {
        StringBuilder sb = new StringBuilder();
        if (this.numero.length() == otroNumero.numero.length()) {

            for (int i = 0; i < this.numero.length(); i++) {
                if (this.numero.charAt(i) == ZERO_CHAR || otroNumero.numero.charAt(i) == ZERO_CHAR) {
                    sb.append(ZERO_CHAR);
                } else {
                    sb.append(ONE_CHAR);
                }
            }
            return new NumeroBinario(sb.toString());
        }
        return null;
    }

    /**
     *
     * @param otroNumero
     * @return operación or bit a bit Los números tienen que tener la misma
     * longitud
     */
    public NumeroBinario orLogica(NumeroBinario otroNumero) {
        StringBuilder sb = new StringBuilder();
        if (this.numero.length() == otroNumero.numero.length()) {

            for (int i = 0; i < this.numero.length(); i++) {
                if (this.numero.charAt(i) == ONE_CHAR || otroNumero.numero.charAt(i) == ONE_CHAR) {
                    sb.append(ONE_CHAR);
                } else {
                    sb.append(ZERO_CHAR);
                }
            }
            return new NumeroBinario(sb.toString());
        }
        return null;
    }
}
