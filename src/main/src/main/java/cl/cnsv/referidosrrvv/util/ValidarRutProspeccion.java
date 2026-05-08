/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.util;

/**
 *
 * @author cox
 */
public class ValidarRutProspeccion {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(
            ValidarRutProspeccion.class);

    public ValidarRutProspeccion() {
        // constructor vacio
    }

    public static boolean isValidRutToSend(String rut) {
        boolean ret = false;
        if (rut != null && rut.trim().length() > 0) {
            try {
                // rut = rut.replaceAll("[.]", "").replaceAll("-", "").trim().toUpperCase();
                rut = rut.replace(".", "").replace("-", "").trim().toUpperCase();

                char dv = rut.charAt(rut.length() - 1);
                String mantisa = rut.substring(0, rut.length() - 1);
                if (isInteger(mantisa)) {
                    int mantisaInt = Integer.parseInt(mantisa);
                    ret = validarRut(mantisaInt, dv);
                }
            } catch (Throwable e) {
                LOGGER.error(e);
            }
        }
        return !ret;
    }

    private static boolean validarRut(int rut, char dv) {
        int m = 0, s = 1;
        for (; rut != 0; rut /= 10) {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return Character.toUpperCase(dv) == (char) (s != 0 ? s + 47 : 75);
    }

    private static boolean isInteger(String cad) {
        for (int i = 0; i < cad.length(); i++) {
            if (!Character.isDigit(cad.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
