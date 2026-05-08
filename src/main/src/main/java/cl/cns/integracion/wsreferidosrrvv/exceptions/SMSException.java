package cl.cns.integracion.wsreferidosrrvv.exceptions;

public class SMSException extends Exception {

    private String mensaje;

    public SMSException(String msg) {
        this.mensaje = msg;
    }

    @Override
    public String getMessage() {
        return mensaje;
    }
}
