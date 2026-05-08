package cl.cns.integracion.wsreferidosrrvv.exceptions;

public class NegocioException extends Exception {

    private String mensaje;

    public NegocioException(String msg) {
        this.mensaje = msg;
    }

    @Override
    public String getMessage() {
        return mensaje;
    }
}
