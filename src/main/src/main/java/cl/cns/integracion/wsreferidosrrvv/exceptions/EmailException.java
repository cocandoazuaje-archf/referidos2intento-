package cl.cns.integracion.wsreferidosrrvv.exceptions;

public class EmailException extends Exception {

    private String mensaje;

    public EmailException(String msg) {
        this.mensaje = msg;
    }

    @Override
    public String getMessage() {
        return mensaje;
    }
}
