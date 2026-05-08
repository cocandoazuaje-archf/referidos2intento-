package cl.cns.integracion.wsreferidosrrvv.vo;

public class EMCredentialsVO {

    private String rutaLlaveEmail;
    private String rutaLlaveSMS;
    private String EMAIL_USR;
    private String EMAIL_PASS;
    private String SMS_USR;
    private String SMS_PASS;

    private int codigoError;
    private String codigoErromensaje;

    public int getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(int codigoError) {
        this.codigoError = codigoError;
    }

    public String getCodigoErromensaje() {
        return codigoErromensaje;
    }

    public void setCodigoErromensaje(String codigoErromensaje) {
        this.codigoErromensaje = codigoErromensaje;
    }

    public String getRutaLlaveEmail() {
        return rutaLlaveEmail;
    }

    public void setRutaLlaveEmail(String rutaLlaveEmail) {
        this.rutaLlaveEmail = rutaLlaveEmail;
    }

    public String getRutaLlaveSMS() {
        return rutaLlaveSMS;
    }

    public void setRutaLlaveSMS(String rutaLlaveSMS) {
        this.rutaLlaveSMS = rutaLlaveSMS;
    }

    public String getEMAIL_USR() {
        return EMAIL_USR;
    }

    public void setEMAIL_USR(String EMAIL_USR) {
        this.EMAIL_USR = EMAIL_USR;
    }

    public String getEMAIL_PASS() {
        return EMAIL_PASS;
    }

    public void setEMAIL_PASS(String EMAIL_PASS) {
        this.EMAIL_PASS = EMAIL_PASS;
    }

    public String getSMS_USR() {
        return SMS_USR;
    }

    public void setSMS_USR(String SMS_USR) {
        this.SMS_USR = SMS_USR;
    }

    public String getSMS_PASS() {
        return SMS_PASS;
    }

    public void setSMS_PASS(String SMS_PASS) {
        this.SMS_PASS = SMS_PASS;
    }
}
