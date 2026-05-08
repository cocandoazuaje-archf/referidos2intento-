/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.services;

import cl.cns.integracion.wsreferidosrrvv.clases.CrearReferidoReferenciaBitacora;
import cl.cns.integracion.wsreferidosrrvv.util.Constantes;
import cl.cns.integracion.wsreferidosrrvv.vo.DatosReferidos;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.Stateless;

/**
 *
 * @author RyC1
 */
@Stateless
public class ReferidosValidaciones {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(
            CrearReferidoReferenciaBitacora.class.getName());
    private String valUsuario = "usuario \n";
    private String valSiguiente = "El siguiente campo es obligatorio : ";
    private String valSiguiente2 = "Los siguientes campos son obligatorios : ";
    private String valElFormato = "El formato del rut no es correcto \n";
    private String valNoSeCumple = "No se cumple con las siguientes validaciones: ";

    public String ValidacionCrearReferido(DatosReferidos entity) {
        int intValidaciones = 0;
        String strValidaciones = "";
        String salida = "";
        /* Validacion de obligatoriedad */
        entity.setNombre(
                entity.getNombre() != null ? entity.getNombre().trim() : "");
        entity.setRut(entity.getRut() != null ? entity.getRut().trim() : "");
        entity.setCanal(entity.getCanal() != null ? entity.getCanal().trim() : "");
        entity.setUsuario(
                entity.getUsuario() != null ? entity.getUsuario().trim() : "");
        entity.setCorreo(
                entity.getCorreo() != null ? entity.getCorreo().trim() : "");
        entity.setApellidos(
                entity.getApellidos() != null ? entity.getApellidos().trim() : "");
        entity.setSexo(entity.getSexo() != null ? entity.getSexo().trim() : "");
        entity.setTelefono(
                entity.getTelefono() != null ? entity.getTelefono().trim() : "");

        continuar4(entity, intValidaciones, strValidaciones);

        if (intValidaciones > 0) {
            if (intValidaciones == 1) {
                salida = valSiguiente + strValidaciones;
            } else {
                salida = valSiguiente2 + strValidaciones;
            }
        } else {
            continuar2(entity, intValidaciones, strValidaciones);
            continuar(entity, intValidaciones, strValidaciones, salida);
        }
        return salida;
    }

    public String ValidacionModificaReferido(DatosReferidos entity) {
        int intValidaciones = 0;
        String strValidaciones = "";
        String salida = "";

        entity.setRut(entity.getRut() != null ? entity.getRut().trim() : "");
        entity.setUsuario(
                entity.getUsuario() != null ? entity.getUsuario().trim() : "");
        entity.setCorreo(
                entity.getCorreo() != null ? entity.getCorreo().trim() : "");

        /* Validacion de obligatoriedad */
        if (entity.getUsuario().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = valUsuario;
        }
        if (entity.getRut().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "rut \n";
        }
        if (intValidaciones > 0) {
            if (intValidaciones == 1) {
                salida = valSiguiente + strValidaciones;
            } else {
                salida = valSiguiente2 + strValidaciones;
            }
        } else {
            continuar3(entity, intValidaciones, strValidaciones, salida);
        }
        return salida;
    }

    public String ValidacionModificaEjecutivo(DatosReferidos entity) {
        int intValidaciones = 0;
        String strValidaciones = "";
        String salida = "";
        /* Validacion obligatoriedad */
        entity.setRut(entity.getRut() != null ? entity.getRut().trim() : "");
        entity.setUsuario(
                entity.getUsuario() != null ? entity.getUsuario().trim() : "");

        continuar6(entity, intValidaciones, strValidaciones);

        if (intValidaciones > 0) {
            if (intValidaciones == 1) {
                salida = valSiguiente + strValidaciones;
            } else {
                salida = valSiguiente2 + strValidaciones;
            }
        } else {
            /* Validacion de formato del rut */
            if (!validarRut(entity.getRut()) && !entity.getRut().equals("")) {
                intValidaciones = ++intValidaciones;
                strValidaciones = valElFormato;
            }
            /* Validacion de tamanio */
            if (TamanioCampo(entity.getRut() + "", Constantes.RUT)
                    && !entity.getRut().equals("")) {
                intValidaciones = ++intValidaciones;
                strValidaciones = "El largo maximo del campo Rut es" + Constantes.LARGORUT + "\n";
            }
            continuar5(entity, intValidaciones, strValidaciones, salida);
        }
        return salida;
    }

    public static boolean validarRut(String rut) {
        // Acepta RUTs: con puntos y guion / solo con guion / y solo nuemeros donde se
        // considera el último digito como dígito verificador.
        if (!rut.matches("\\d{1,3}(\\.\\d{3})*\\-[\\dKk]|\\d+\\-[\\dKk]|\\d+[\\dKk]")) {
            return false;
        }
        boolean validacion = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }
        } catch (java.lang.NumberFormatException e) {
            LOGGER.error("Error : ", e);
        } catch (Exception e) {
            LOGGER.error("Error : ", e);
        }
        return validacion;
    }

    public boolean ValidarCorreoElectronico(String email) {
        boolean correcto = false;
        String emailPattern = "^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@"
                + "[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            correcto = true;
        }

        return correcto;
    }

    public boolean TamanioCampo(String StrNumero, int intCampoValidar) {
        int Largo = StrNumero.length();
        boolean valor = true;

        switch (intCampoValidar) {
            case Constantes.CORREO:
                valor = Largo > Constantes.LARGOCORREO ? true : false;
                break;
            case Constantes.RUT:
                valor = Largo > Constantes.LARGORUT ? true : false;
                break;
            case Constantes.USUARIO:
                valor = Largo > Constantes.LARGOUSUARIO ? true : false;
                break;
            case Constantes.SUCURSAL:
                valor = Largo > Constantes.LARGOSUCURSAL ? true : false;
                break;
            default:
                valor = continuarCase(intCampoValidar, valor, Largo);
                break;
        }

        return valor;
    }

    private boolean continuarCase(int intCampoValidar, boolean valor, int Largo) {

        switch (intCampoValidar) {
            case Constantes.NOMBRE:
                valor = Largo > Constantes.LARGONOMBRE ? true : false;
                break;
            case Constantes.APELLIDO:
                valor = Largo > Constantes.LARGOAPELLIDO ? true : false;
                break;
            case Constantes.SEXO:
                valor = Largo > Constantes.LARGOSEXO ? true : false;
                break;
            case Constantes.TELEFONO:
                valor = Largo > Constantes.LARGOTELEFONO ? true : false;
                break;
            default:
                valor = false;
                break;
        }

        return valor;

    }

    private void continuar(
            DatosReferidos entity,
            int intValidaciones,
            String strValidaciones,
            String salida) {
        if (TamanioCampo(entity.getSexo() + "", Constantes.SEXO)
                && !entity.getSexo().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "El largo maximo del campo Sexo es" + Constantes.LARGOSEXO + "\n";
        }
        if (TamanioCampo(entity.getTelefono() + "", Constantes.TELEFONO)
                && !entity.getTelefono().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "El largo maximo del campo Telefono es"
                    + Constantes.LARGOTELEFONO
                    + "\n";
        }
        if (TamanioCampo(entity.getRut() + "", Constantes.RUT)
                && !entity.getRut().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "El largo maximo del campo Rut es" + Constantes.LARGORUT + "\n";
        }
        if (intValidaciones > 0) {
            salida = valNoSeCumple + strValidaciones;
        }
    }

    private void continuar2(
            DatosReferidos entity,
            int intValidaciones,
            String strValidaciones) {
        /* Validacion de formato de los campos */
        if (!validarRut(entity.getRut()) && !entity.getRut().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = valElFormato;
        }
        if (!ValidarCorreoElectronico(entity.getCorreo())
                && !entity.getCorreo().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "El formato del correo no es correcto \n";
        }
        /* Validacion de tamanio */
        if (TamanioCampo(entity.getNombre() + "", Constantes.NOMBRE)
                && !entity.getNombre().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "El largo maximo del campo Nombre es" + Constantes.LARGONOMBRE + "\n";
        }
        if (TamanioCampo(entity.getApellidos() + "", Constantes.APELLIDO)
                && !entity.getApellidos().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "El largo maximo del campo Apellido es"
                    + Constantes.LARGOAPELLIDO
                    + "\n";
        }
    }

    private void continuar3(
            DatosReferidos entity,
            int intValidaciones,
            String strValidaciones,
            String salida) {
        /* Validacion de formato del rut y el correo */
        if (!validarRut(entity.getRut()) && !entity.getRut().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = valElFormato;
        }
        if (!ValidarCorreoElectronico(entity.getCorreo())
                && !entity.getCorreo().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "El formato del correo no es correcto \n";
        }
        if (intValidaciones > 0) {
            salida = valNoSeCumple + strValidaciones;
        }
    }

    private void continuar4(
            DatosReferidos entity,
            int intValidaciones,
            String strValidaciones) {
        if (entity.getNombre().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "nombre \n";
        }

        if (entity.getRut().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "rut \n";
        }

        if (entity.getCanal().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "canal \n";
        }

        if (entity.getUsuario().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = valUsuario;
        }
    }

    private void continuar5(
            DatosReferidos entity,
            int intValidaciones,
            String strValidaciones,
            String salida) {
        if (TamanioCampo(entity.getUsuario() + "", Constantes.USUARIO)
                && !entity.getUsuario().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "El largo maximo del campo Usuario es" + Constantes.LARGOUSUARIO + "\n";
        }

        if (TamanioCampo(entity.getIdsucursal() + "", Constantes.SUCURSAL)
                && !entity.getIdsucursal().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "El largo maximo del campo Sucursal es"
                    + Constantes.LARGOSUCURSAL
                    + "\n";
        }
        if (intValidaciones > 0) {
            salida = valNoSeCumple + strValidaciones;
        }
    }

    private void continuar6(
            DatosReferidos entity,
            int intValidaciones,
            String strValidaciones) {
        if (entity.getRut().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "Rut \n";
        }

        if (entity.getUsuario().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = valUsuario;
        }
        if (entity.getIdsucursal().equals("")) {
            intValidaciones = ++intValidaciones;
            strValidaciones = "sucursal \n";
        }
    }
}
