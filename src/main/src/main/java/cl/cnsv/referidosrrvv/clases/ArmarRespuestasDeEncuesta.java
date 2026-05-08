package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.models.Referidos;

public class ArmarRespuestasDeEncuesta {

    String resp = "Sin Información";

    public ArmarRespuestasDeEncuesta() {
        // constructor vacio
    }

    public String pensionarse(Referidos r) {
        if ("1".equals(r.getPensionarse())) {
            this.resp = "Es cliente Consorcio";
        }
        if ("2".equals(r.getPensionarse())) {
            this.resp = "Recomendación amigo, conocido, familiar";
        }
        if ("3".equals(r.getPensionarse())) {
            this.resp = "Recomendación AFP";
        }
        if ("4".equals(r.getPensionarse())) {
            this.resp = "Cercanía sucursal";
        }
        if ("5".equals(r.getPensionarse())) {
            this.resp = "Buscó información por Internet";
        }
        if ("6".equals(r.getPensionarse())) {
            this.resp = "Facebook";
        }
        if ("7".equals(r.getPensionarse())) {
            this.resp = "Radio";
        }
        if ("8".equals(r.getPensionarse())) {
            this.resp = "TV";
        }
        if ("9".equals(r.getPensionarse())) {
            this.resp = "Contactado por CNS";
        }
        if ("10".equals(r.getPensionarse())) {
            this.resp = "Otro";
        }

        return this.resp;
    }

    public String clienteSolicito(Referidos r) {
        if ("1".equals(r.getClienteSolicito())) {
            this.resp = "Para él";
        }
        if ("2".equals(r.getClienteSolicito())) {
            this.resp = "Para un tercero";
        }

        return this.resp;
    }

    public String accionRealizo(Referidos r) {
        if ("1".equals(r.getAccionRealizo())) {
            this.resp = "Asesoría";
        }
        if ("2".equals(r.getAccionRealizo())) {
            this.resp = "Ingreso solicitud de oferta a SCOMP";
        }
        if ("3".equals(r.getAccionRealizo())) {
            this.resp = "Estudio Pensión";
        }
        if ("4".equals(r.getAccionRealizo())) {
            this.resp = "Oferta Externa";
        }
        if ("5".equals(r.getAccionRealizo())) {
            this.resp = "Cierre";
        }

        return this.resp;
    }

    public String tipoPension(Referidos r) {
        if ("1".equals(r.getTipoPension())) {
            this.resp = "Vejez";
        }
        if ("2".equals(r.getTipoPension())) {
            this.resp = "Vejez Anticipada";
        }
        if ("3".equals(r.getTipoPension())) {
            this.resp = "Invalidez";
        }
        if ("4".equals(r.getTipoPension())) {
            this.resp = "Sobrevivencia";
        }

        return this.resp;
    }
}
