package cl.cns.integracion.wsreferidosrrvv.vo;

public class DatosContactoPropertiesMainVO {

    private String log4jPath;
    private String log4jFileName;
    private String APIEMTarget;
    private String APIEMPath;
    private String APIEMPlantillaP1;
    private String APIEMPlantillaP2;
    private String APIEMPreguntasEnvio;
    private boolean APIEMRespuestaEnvio;
    private Integer APIEMSegundosDelay;
    private String SMSTarget;
    private String SMSPath;
    private String SMSPlantillaP1;
    private String SMSPlantillaP2;
    private String SMSLineaNegocio;
    private Integer SMSMaxNombre;

    private int codigoError;
    private String codigoErromensaje;
    private int nNegocio;

    public int getnNegocio() {
        return nNegocio;
    }

    public void setnNegocio(int nNegocio) {
        this.nNegocio = nNegocio;
    }

    public String getLog4jPath() {
        return log4jPath;
    }

    public void setLog4jPath(String log4jPath) {
        this.log4jPath = log4jPath;
    }

    public String getLog4jFileName() {
        return log4jFileName;
    }

    public void setLog4jFileName(String log4jFileName) {
        this.log4jFileName = log4jFileName;
    }

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

    public String getAPIEMTarget() {
        return APIEMTarget;
    }

    public void setAPIEMTarget(String APIEMTarget) {
        this.APIEMTarget = APIEMTarget;
    }

    public String getAPIEMPath() {
        return APIEMPath;
    }

    public void setAPIEMPath(String APIEMPath) {
        this.APIEMPath = APIEMPath;
    }

    public String getAPIEMPlantillaP1() {
        return APIEMPlantillaP1;
    }

    public void setAPIEMPlantillaP1(String APIEMPlantillaP1) {
        this.APIEMPlantillaP1 = APIEMPlantillaP1;
    }

    public String getAPIEMPlantillaP2() {
        return APIEMPlantillaP2;
    }

    public void setAPIEMPlantillaP2(String APIEMPlantillaP2) {
        this.APIEMPlantillaP2 = APIEMPlantillaP2;
    }

    public String getSMSTarget() {
        return SMSTarget;
    }

    public void setSMSTarget(String SMSTarget) {
        this.SMSTarget = SMSTarget;
    }

    public String getSMSPath() {
        return SMSPath;
    }

    public void setSMSPath(String SMSPath) {
        this.SMSPath = SMSPath;
    }

    public String getSMSPlantillaP1() {
        return SMSPlantillaP1;
    }

    public void setSMSPlantillaP1(String SMSPlantillaP1) {
        this.SMSPlantillaP1 = SMSPlantillaP1;
    }

    public String getSMSPlantillaP2() {
        return SMSPlantillaP2;
    }

    public void setSMSPlantillaP2(String SMSPlantillaP2) {
        this.SMSPlantillaP2 = SMSPlantillaP2;
    }

    public String getSMSLineaNegocio() {
        return SMSLineaNegocio;
    }

    public void setSMSLineaNegocio(String SMSLineaNegocio) {
        this.SMSLineaNegocio = SMSLineaNegocio;
    }

    public String getAPIEMPreguntasEnvio() {
        return APIEMPreguntasEnvio;
    }

    public void setAPIEMPreguntasEnvio(String APIEMPreguntasEnvio) {
        this.APIEMPreguntasEnvio = APIEMPreguntasEnvio;
    }

    public boolean isAPIEMRespuestaEnvio() {
        return APIEMRespuestaEnvio;
    }

    public void setAPIEMRespuestaEnvio(boolean APIEMRespuestaEnvio) {
        this.APIEMRespuestaEnvio = APIEMRespuestaEnvio;
    }

    public Integer getSMSMaxNombre() {
        return SMSMaxNombre;
    }

    public void setSMSMaxNombre(Integer SMSMaxNombre) {
        this.SMSMaxNombre = SMSMaxNombre;
    }

    public Integer getAPIEMSegundosDelay() {
        return APIEMSegundosDelay;
    }

    public void setAPIEMSegundosDelay(Integer APIEMSegundosDelay) {
        this.APIEMSegundosDelay = APIEMSegundosDelay;
    }
}
