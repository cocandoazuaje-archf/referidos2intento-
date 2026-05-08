/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.models.Referidos;
import cl.cnsv.referidosrrvv.models.Sucursales;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * @author cow
 */
public class Resources {

    private static String valRcia = " rcia.* ";
    public static String SERVIDOR_SMTP_CONSORCIO = "correosmtp.cnsv.cl";
    public static int CANT_CORREOS_MAX_POR_EJECUTIVA = 2;
    public static ProspeccionGetConfig prospeccionGetConfig = new ProspeccionGetConfig();
    public static String URL_PROSPECTION = prospeccionGetConfig.getUrl();
    public static String KEY_PROSPECTION = prospeccionGetConfig.getX_api_key();
    public static List<Referidos> listaParaProspeccionConHilo = new ArrayList<>();
    public static List<Sucursales> listaParaProspeccionConHiloSucursales = new ArrayList<>();
    public static String URL_REFERIDOSRRVV_POOL = "jdbc/referidosrrvv";
    public static Client client = ClientBuilder.newClient();
    private static String valFromRfd = "from " + " rfd_Referencias rcia ";
    private static String valInner = " inner join rfd_acciones a on (rcia.accion_id=a.id) ";
    private static String valInner2 = " inner join rfd_referidos rido on (rcia.referido_id=rido.id) "
            + " INNER JOIN RFD_EJECUTIVOS e ON (e.codigo = rcia.OWNERE) ";
    private static String valAnd = "AND e.CODIGO=? ";
    private static String valSelect = "SELECT * FROM rfd_referencias r ";
    private static String valWhere = "WHERE r.accion_id=3 ";
    private static String valSelect2 = " ( SELECT 1 id, ";
    private static String valLeft = "LEFT JOIN ";
    private static String valCount = "  COUNT(*) total, ";
    private static String valFront2 = "  FROM rfd_referencias r ";
    private static String valLeftJoin = " LEFT JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.id) LEFT JOIN rfd_ejecutivos e ON (e.codigo=r.ownere) LEFT JOIN rfd_sucursales s ON (s.id=e.sucursal_id) ";
    private static String valGroup = " GROUP BY color, ";
    private static String valSelect3 = "SELECT ";
    private static String valOrder = "order by rido.score, a.id";

    public static Connection getConexion() throws NamingException, SQLException {
        InitialContext context;
        DataSource dataSource;
        Connection con = null;
        context = new InitialContext();
        dataSource = (DataSource) context.lookup(URL_REFERIDOSRRVV_POOL);
        con = dataSource.getConnection();

        return con;
    }

    public static String bodySendMailDerivados(
            String nombreReferido,
            String rutReferido,
            String canalReferido,
            String regionReferido,
            String comunaReferido,
            int envios) {
        String varname;

        if (envios > CANT_CORREOS_MAX_POR_EJECUTIVA) {
            varname = "<h3>Se le ha asignado nuevos prospectos a través de la plataforma de Referidos RRVV, &nbsp;favor atender a la brevedad posible:</h3><hr /><p>&nbsp;</p><p>&nbsp;</p><p><br /><br /><br /><br /><br /></p><h4>Para mayor detalle de su referido dirigirse a la siguiente Url :</h4><p><a href=\"https://aplicaciones.cnsv.cl/referidosrrvv\">https://aplicaciones.cnsv.cl/referidosrrvv</a></p>";
        } else {
            varname = "<h3>Se le ha asignado un nuevo prospecto a través de la plataforma de Referidos RRVV, "
                    + "&nbsp;favor atender a la brevedad posible:</h3><hr /><p>&nbsp;</p>"
                    + "<table style=\"height: 99px;\" width=\"386\"><tbody><tr><td "
                    + "style=\"width: 185px;\">Referido :&nbsp;</td><td style=\"width: 185px;\">"
                    + nombreReferido
                    + "</td></tr><tr><td style=\"width: 185px;\">Rut</td><td "
                    + "style=\"width: 185px;\">"
                    + rutReferido
                    + "</td></tr><tr>"
                    + "<td style=\"width: 185px;\">"
                    + "Canal:</td><td style=\"width: 185px;\">"
                    + canalReferido
                    + "</td></tr><tr><td "
                    + "style=\"width: 185px;\">Region:&nbsp;</td><td style=\"width: 185px;\">"
                    + regionReferido
                    + "</td></tr><tr><td "
                    + "style=\"width: 185px;\">Comuna:&nbsp;</td>"
                    + "<td style=\"width: 185px;\">"
                    + comunaReferido
                    + "</td></tr></tbody></table>"
                    + "<p>&nbsp;</p><p><br /><br /><br /><br /><br /></p><h4>Para mayor detalle de su referido "
                    + "dirigirse a la siguiente Url :</h4><p><a "
                    + "href=\"https://aplicaciones.cnsv.cl/referidosrrvv\">"
                    + "https://aplicaciones.cnsv.cl/referidosrrvv</a></p>";
        }

        return varname;
    }

    public static String sqlUpdateProspeccion() {
        String varname = "SELECT r.id, r.rut, r.nombre FROM RFD_REFERIDOS r\n"
                + "WHERE r.RUT NOT LIKE '%SR%' ";

        return varname;
    }

    public static String sqlFechaFicha() {
        String varname = ""
                + valSelect3
                + " * "
                + "FROM "
                + " rfd_bitacoras "
                + "WHERE "
                + " fecha IN ( "
                + "     SELECT MAX(fecha) "
                + " FROM "
                + "     rfd_bitacoras "
                + " WHERE "
                + "     (comentarios LIKE '%COMPLETAR FICHA ->%' "
                + "     OR comentarios LIKE '%REFERIDO CREADO POR FICHA POR EJECUTIVO(A) ->%') "
                + "     AND referencia_id = ?)";

        return varname;
    }

    public static String sql2UltimosComentarios() {
        String varname = ""
                + "SELECT t.* "
                + "     FROM "
                + "         ( SELECT comentarios original, "
                + "                  SUBSTR(comentarios, INSTR(b.comentarios, '->', 1, 1) + 2, 100) COMENTARIOS, "
                + "                  B.id, "
                + "                  B.fecha "
                + "          FROM rfd_bitacoras b "
                + "          WHERE referencia_id = ? "
                + "              AND (comentarios LIKE '%AGENDADO%' "
                + "                   OR comentarios LIKE '%ATENDIDO POR OTRO EJECUTIVO/ASESOR%' "
                + "                   OR comentarios LIKE '%CERRADO EN AFP%' "
                + "                   OR comentarios LIKE '%CERRADO EN OTRA CIA%' "
                + "                   OR comentarios LIKE '%CIERRE TRAMITE CNL%' "
                + "                   OR comentarios LIKE '%CIERRE TRAMITE CNS%' "
                + "                   OR comentarios LIKE '%CLIENTE NO UBICABLE%' "
                + "                   OR comentarios LIKE '%CLIENTE REFERIDO REPETIDO%' "
                + "                   OR comentarios LIKE '%ESTUDIO PENSIÓN%' "
                + "                   OR comentarios LIKE '%ESTUDIO PENSIÓN ANTICIPADA%' "
                + "                   OR comentarios LIKE '%INICIA TRÁMITE%' "
                + "                   OR comentarios LIKE '%NO APLICA%' "
                + "                   OR comentarios LIKE '%NO CUMPLE REQUISITO EDAD%' "
                + "                   OR comentarios LIKE '%NO CUMPLE REQUISITO PRIMA%' "
                + "                   OR comentarios LIKE '%NO ESTA INTERESADO%' "
                + "                   OR comentarios LIKE '%OFERTA EXTERNA%' "
                + "                   OR comentarios LIKE '%POSTERGADO%' "
                + "                   OR comentarios LIKE '%REQUERIMIENTO POST VENTA%' "
                + "                   OR comentarios LIKE '%REQUIERE INFORMACIÓN%' "
                + "                   OR comentarios LIKE '%SOLICITA INFORMACIÓN POR CORREO%' "
                + "                   OR comentarios LIKE '%SOLICITA NUEVA LLAMADA%' "
                + "                   OR comentarios LIKE '%SOLICITUD OTROS PRODUCTOS%' "
                + "                   OR comentarios LIKE '%TELÉFONO ERRÓNEO%' "
                + "                   OR comentarios LIKE '%TRÁMITE INVALIDEZ%' "
                + "                   OR comentarios LIKE '%PENSIONADO%' "
                + "                   OR comentarios LIKE '%CLIENTE FUTURO%') ) t";

        return varname;
    }

    public static String sql2UltimosComentarios2() {
        String varname = ""
                + "SELECT t2.* "
                + "     FROM "
                + "         ( SELECT comentarios original, "
                + "                  CONCAT('OTROS // ', SUBSTR(comentarios, INSTR(b.comentarios, '->', 1, 1) + 2, 100)) COMENTARIOS, "
                + "                  B.id, "
                + "                  B.fecha "
                + "          FROM rfd_bitacoras b "
                + "          WHERE referencia_id = ? "
                + "              AND NOT (comentarios LIKE '%AGENDADO%' "
                + "                       OR comentarios LIKE '%ATENDIDO POR OTRO EJECUTIVO/ASESOR%' "
                + "                       OR comentarios LIKE '%CERRADO EN AFP%' "
                + "                       OR comentarios LIKE '%CERRADO EN OTRA CIA%' "
                + "                       OR comentarios LIKE '%CIERRE TRAMITE CNL%' "
                + "                       OR comentarios LIKE '%CIERRE TRAMITE CNS%' "
                + "                       OR comentarios LIKE '%CLIENTE NO UBICABLE%' "
                + "                       OR comentarios LIKE '%CLIENTE REFERIDO REPETIDO%' "
                + "                       OR comentarios LIKE '%ESTUDIO PENSIÓN%' "
                + "                       OR comentarios LIKE '%ESTUDIO PENSIÓN ANTICIPADA%' "
                + "                       OR comentarios LIKE '%INICIA TRÁMITE%' "
                + "                       OR comentarios LIKE '%NO APLICA%' "
                + "                       OR comentarios LIKE '%NO CUMPLE REQUISITO EDAD%' "
                + "                       OR comentarios LIKE '%NO CUMPLE REQUISITO PRIMA%' "
                + "                       OR comentarios LIKE '%NO ESTA INTERESADO%' "
                + "                       OR comentarios LIKE '%OFERTA EXTERNA%' "
                + "                       OR comentarios LIKE '%POSTERGADO%' "
                + "                       OR comentarios LIKE '%REQUERIMIENTO POST VENTA%' "
                + "                       OR comentarios LIKE '%REQUIERE INFORMACIÓN%' "
                + "                       OR comentarios LIKE '%SOLICITA INFORMACIÓN POR CORREO%' "
                + "                       OR comentarios LIKE '%SOLICITA NUEVA LLAMADA%' "
                + "                       OR comentarios LIKE '%SOLICITUD OTROS PRODUCTOS%' "
                + "                       OR comentarios LIKE '%TELÉFONO ERRÓNEO%' "
                + "                       OR comentarios LIKE '%TRÁMITE INVALIDEZ%' "
                + "                       OR comentarios LIKE '%PENSIONADO%' "
                + "                       OR comentarios LIKE '%CLIENTE FUTURO%') "
                + "              AND (comentarios LIKE '%VENTA EXITOSA%' "
                + "                    OR comentarios LIKE '%SIN EXITO%' "
                + "                    OR comentarios LIKE '%DEJAR COMENTARIO%' "
                + "                    OR COMENTARIOS LIKE '%REFERENCIA CARGADA DEL GENESIS CON ESTATUS%') ) t2";

        return varname;
    }

    public static String sqlRolesUsuariosNombre() {
        String varname = ""
                + "SELECT SYS_GUID() id, 'usuario_entrante' estado, r.nombre, r.rol "
                + "FROM RFD_ROLESUSUARIOS r WHERE Upper(nombre) = ? "
                + "UNION SELECT SYS_GUID() id, 'es_asistente_de' estado, r.nombre, r.rol "
                + "FROM RFD_ROLESUSUARIOS r WHERE Upper(ast)= ? "
                + "AND 'ast' IN (SELECT rol FROM RFD_ROLESUSUARIOS WHERE Upper(NOMBRE)=?) "
                + "UNION SELECT SYS_GUID() id, 'es_supervisor_de' estado, r.nombre, r.rol "
                + "FROM RFD_ROLESUSUARIOS r WHERE Upper(sup)= ? AND 'sup' IN (SELECT rol "
                + "FROM RFD_ROLESUSUARIOS WHERE Upper(NOMBRE)=?)";

        return varname;
    }

    public static String sqlCanales() {
        String varname = ""
                + "SELECT * "
                + "FROM   (SELECT Upper(canalname) canalname "
                + "        FROM   rfd_referencias "
                + "        ORDER  BY canalname) t "
                + "GROUP BY  canalname";

        return varname;
    }

    public static String sqlEnviarAlGenesis() {
        String varname = "SELECT * FROM RFD_REFERENCIAS "
                + "WHERE ACCION_ID=1 AND CANALNAME NOT IN ('SUCURSAL','REFERIDO PROPIO')";

        return varname;
    }

    public static String sqlDerivarMasivoDetalExportarDataTable() {
        String varname = ""
                + "SELECT t5.* FROM (SELECT t3.*, rownum n "
                + "FROM (SELECT t.*, rownum rn FROM (SELECT r.*, r2.nombre, r2.rut, r2.correo, r2.comuna, "
                + "r2.region, a.nombre AS nomestado FROM RFD_REFERENCIAS r INNER JOIN RFD_REFERIDOS r2 "
                + "ON (r.REFERIDO_ID = r2.ID) INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID)) t) t3 "
                + "INNER JOIN ( (SELECT t2.rn, t2.id FROM (SELECT t.*, rownum rn "
                + "FROM (SELECT r.id, ownere, fecha, e.sucursal_id, concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(r2.NOMBRE,' '),r2.APELLIDO),' '),r2.RUT),' '),to_char(r.FECHA,'Dy, dd mon YYYY')),' '),r2.TELEFONOS), ' '),r2.CORREO),' '), r.CANALNAME),' '),r2.COMUNA), ' '), r2.REGION),' '), r.OWNERENAME), ' '),a.NOMBRE), ' '),s.NOMBRE) campos FROM RFD_REFERENCIAS r INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID) INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID) left join rfd_ejecutivos e on (e.codigo=r.ownere) left join rfd_sucursales s on (s.id=e.sucursal_id)) t) t2 "
                + "WHERE lower(campos) LIKE lower('%?1%') ?6 ?7 ?8 )) t4 ON (t3.id = t4.id)  ) t5";

        return varname;
    }

    public static String sqlExportarDataTable() {
        String varname1 = "SELECT nvl(rb.canalname,' ') AS canal, nvl(rb.comuna,' ') AS comuna, nvl(rb.correo,' ') AS correo, nvl(rb.afp, ' ') AS afp,"
                         + " nvl(rb.prima, ' ') AS prima,  nvl(rb.ownerename,' ') AS ejecutivo, nvl(rb.nomestado,' ') AS estado, nvl(rb.fechanac,NULL) AS fechanac,"
                         + " nvl(rb.fecha,NULL) AS fecha, nvl(rb.fecha_accion,NULL) AS fechaaccion, nvl(rb.fechaficha,NULL) AS fechaficha, nvl(rb.id,NULL) AS id,"
                         + " nvl(rb.nombre,' ') AS nombre, nvl(rb.region,' ') AS region, nvl(rb.telefonos,' ') AS telefonos, nvl(rb.telefonos2,' ') AS telefonos2,"
                         + " nvl(rb.telefonos3,' ') AS telefonos3, nvl(rb.dpensionarse,'Sin Información') AS pensionarse, nvl(rb.dclientesolicito,'Sin Información') AS clientesolicito, nvl(rb.daccionrealizo,'Sin Información') AS accionrealizo, nvl(rb.dtipopension,'Sin Información') AS tipopension, nvl((CASE rb.sexo WHEN '1' THEN 'Masculino' ELSE 'Femenino' END),' ') AS sexo, nvl(rb.rut,' ') AS rut, nvl(rb.comentarios,' ') AS ultimos2coments,"
                         + " (SELECT ( SELECT p.pregunta FROM rfd_resp_encuesta r INNER JOIN rfd_pregunta p ON ( r.pregunta_id = p.id ) WHERE referido_id = rb.id AND p.id = 1 and rownum<2 ) from dual) encdigitalpreg1,"
                         + " (SELECT ( SELECT r.respuesta_bool FROM rfd_resp_encuesta r INNER JOIN rfd_pregunta p ON ( r.pregunta_id = p.id ) WHERE referido_id = rb.id AND p.id = 1 and rownum<2 ) from dual) encdigitalresp1,"
                         + " (SELECT ( SELECT p.pregunta FROM rfd_resp_encuesta r INNER JOIN rfd_pregunta p ON ( r.pregunta_id = p.id ) WHERE referido_id = rb.id AND p.id = 2 and rownum<2 ) from dual) encdigitalpreg2,"
                         + " (SELECT ( SELECT r.respuesta_bool FROM rfd_resp_encuesta r INNER JOIN rfd_pregunta p ON ( r.pregunta_id = p.id ) WHERE referido_id = rb.id AND p.id = 2 and rownum<2 ) from dual) encdigitalresp2,"
                         + " (SELECT ( SELECT p.pregunta FROM rfd_resp_encuesta r INNER JOIN rfd_pregunta p ON ( r.pregunta_id = p.id ) WHERE referido_id = rb.id AND p.id = 3 and rownum<2 ) from dual) encdigitalpreg3,"
                         + " (SELECT ( SELECT r.respuesta_bool FROM rfd_resp_encuesta r INNER JOIN rfd_pregunta p ON ( r.pregunta_id = p.id ) WHERE referido_id = rb.id AND p.id = 3 and rownum<2 ) from dual) encdigitalresp3,"
                         + " (SELECT ( SELECT p.pregunta FROM rfd_resp_encuesta r INNER JOIN rfd_pregunta p ON ( r.pregunta_id = p.id ) WHERE referido_id = rb.id AND p.id = 4 and rownum<2 ) from dual) encdigitalpreg4,"
                         + " (SELECT ( SELECT r.fecha FROM rfd_resp_encuesta r INNER JOIN rfd_pregunta p ON ( r.pregunta_id = p.id ) WHERE referido_id = rb.id AND p.id = 4 and rownum<2 ) from dual) encdigitalresp4 FROM ( SELECT r.*, b.*, ff.*, e11.dpensionarse, e12.dclientesolicito, e13.daccionrealizo, e14.dtipopension FROM ( SELECT t5.* FROM ( SELECT t3.*, ROWNUM n FROM ( SELECT t.*, ROWNUM rn FROM ( SELECT r.*, r2.nombre, r2.fechanac, r2.telefonos, r2.telefonos2, r2.telefonos3, r2.pensionarse, r2.cliente_solicito clientesolicito, r2.accion_realizo accionrealizo, r2.tipo_pension tipopension, r2.rut, r2.correo, r2.afp, r2.prima, r2.comuna, r2.region, r2.sexo, a.nombre AS nomestado FROM rfd_referencias r INNER JOIN rfd_referidos r2 ON (r.referido_id = r2.id) INNER JOIN rfd_acciones a ON (r.accion_id = a.id) ) t ) t3 INNER JOIN ( ( SELECT t2.rn, t2.id FROM ( SELECT t.*, ROWNUM rn FROM ( SELECT r.id, ownere, fecha, e.sucursal_id, concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat (concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat (r2.nombre,' '),r2.apellido),' '),r2.rut),' '),TO_CHAR(r.fecha,'Dy, dd mon YYYY' )),' '),r2.telefonos),' '),r2.correo),' '),r.canalname),' '),r2.comuna),' ' ),r2.region),' '),r.ownerename),' '),a.nombre),' '),s.nombre) campos FROM rfd_referencias r INNER JOIN rfd_referidos r2 ON (r.referido_id = r2.id) INNER JOIN rfd_acciones a ON (r.accion_id = a.id) LEFT JOIN rfd_ejecutivos e ON (e.codigo = r.ownere) LEFT JOIN rfd_sucursales s ON (s.id = e.sucursal_id) ) t ) t2 WHERE lower(campos) LIKE lower('%?1%') ?6 ?7 ?8 )) t4 ON (t3.id = t4.id) ) t5 ) r LEFT JOIN"
                         + " (SELECT referencia_id, LISTAGG(comentarios,' // ') WITHIN GROUP( ORDER BY fecha) AS comentarios FROM (SELECT n12.* FROM (SELECT n.*, ROW_NUMBER() OVER(PARTITION BY n.referencia_id ORDER BY n.fecha DESC) seq_group FROM (SELECT bit.* FROM (SELECT referencia_id, fecha, substr(comentarios,instr(b.comentarios,'->',1,1) + 2,100) comentarios FROM rfd_bitacoras b WHERE (comentarios LIKE '%AGENDADO%' OR comentarios LIKE '%ATENDIDO POR OTRO EJECUTIVO/ASESOR%' OR comentarios LIKE '%CERRADO EN AFP%' OR comentarios LIKE '%CERRADO EN OTRA CIA%' OR comentarios LIKE '%CIERRE TRAMITE CNL%' OR comentarios LIKE '%CIERRE TRAMITE CNS%' OR comentarios LIKE '%CLIENTE NO UBICABLE%' OR comentarios LIKE '%CLIENTE REFERIDO REPETIDO%' OR comentarios LIKE '%ESTUDIO PENSIÓN%' OR comentarios LIKE '%ESTUDIO PENSIÓN ANTICIPADA%' OR comentarios LIKE '%INICIA TRÁMITE%' OR comentarios LIKE '%NO APLICA%' OR comentarios LIKE '%NO CUMPLE REQUISITO EDAD%' OR comentarios LIKE '%NO CUMPLE REQUISITO PRIMA%' OR comentarios LIKE '%NO ESTA INTERESADO%' OR comentarios LIKE '%OFERTA EXTERNA%' OR comentarios LIKE '%POSTERGADO%' OR comentarios LIKE '%REQUERIMIENTO POST VENTA%' OR comentarios LIKE '%REQUIERE INFORMACIÓN%' OR comentarios LIKE '%SOLICITA INFORMACIÓN POR CORREO%' OR comentarios LIKE '%SOLICITA NUEVA LLAMADA%' OR comentarios LIKE '%SOLICITUD OTROS PRODUCTOS%' OR comentarios LIKE '%TELÉFONO ERRÓNEO%' OR comentarios LIKE '%TRÁMITE INVALIDEZ%' OR comentarios LIKE '%PENSIONADO%' OR comentarios LIKE '%CLIENTE FUTURO%') UNION SELECT referencia_id, fecha, concat('OTROS // ',substr(comentarios,instr(b.comentarios,'->',1,1) + 2,100)) comentarios FROM rfd_bitacoras b WHERE NOT (comentarios LIKE '%AGENDADO%' OR comentarios LIKE '%ATENDIDO POR OTRO EJECUTIVO/ASESOR%' OR comentarios LIKE '%CERRADO EN AFP%' OR comentarios LIKE '%CERRADO EN OTRA CIA%' OR comentarios LIKE '%CIERRE TRAMITE CNL%' OR comentarios LIKE '%CIERRE TRAMITE CNS%' OR comentarios LIKE '%CLIENTE NO UBICABLE%' OR comentarios LIKE '%CLIENTE REFERIDO REPETIDO%' OR comentarios LIKE '%ESTUDIO PENSIÓN%' OR comentarios LIKE '%ESTUDIO PENSIÓN ANTICIPADA%' OR comentarios LIKE '%INICIA TRÁMITE%' OR comentarios LIKE '%NO APLICA%' OR comentarios LIKE '%NO CUMPLE REQUISITO EDAD%' OR comentarios LIKE '%NO CUMPLE REQUISITO PRIMA%' OR comentarios LIKE '%NO ESTA INTERESADO%' OR comentarios LIKE '%OFERTA EXTERNA%' OR comentarios LIKE '%POSTERGADO%' OR comentarios LIKE '%REQUERIMIENTO POST VENTA%' OR comentarios LIKE '%REQUIERE INFORMACIÓN%' OR comentarios LIKE '%SOLICITA INFORMACIÓN POR CORREO%' OR comentarios LIKE '%SOLICITA NUEVA LLAMADA%' OR comentarios LIKE '%SOLICITUD OTROS PRODUCTOS%' OR comentarios LIKE '%TELÉFONO ERRÓNEO%' OR comentarios LIKE '%TRÁMITE INVALIDEZ%' OR comentarios LIKE '%PENSIONADO%' OR comentarios LIKE '%CLIENTE FUTURO%') AND (comentarios LIKE '%VENTA EXITOSA%' OR comentarios LIKE '%SIN EXITO%' OR comentarios LIKE '%DEJAR COMENTARIO%' OR comentarios LIKE '%REFERENCIA CARGADA DEL GENESIS CON ESTATUS%') ) bit ORDER BY fecha DESC) n) n12 WHERE seq_group = 1 OR seq_group = 2 ) u2c GROUP BY referencia_id ) b ON (r.id = b.referencia_id) LEFT JOIN ( SELECT referencia_id, MAX(fecha) fechaficha FROM rfd_bitacoras WHERE (comentarios LIKE '%COMPLETAR FICHA ->%' OR comentarios LIKE '%REFERIDO CREADO POR FICHA POR EJECUTIVO(A) ->%') GROUP BY referencia_id ) ff ON (ff.referencia_id = b.referencia_id) LEFT JOIN ( SELECT 1 valor, 'Es cliente Consorcio' dpensionarse FROM dual UNION SELECT 2 valor, 'Recomendación amigo, conocido, familiar' dpensionarse FROM dual UNION SELECT 3 valor, 'Recomendación AFP' dpensionarse FROM dual UNION SELECT 4 valor, 'Cercanía sucursal' dpensionarse FROM dual UNION SELECT 5 valor, 'Buscó información por Internet' dpensionarse FROM dual UNION SELECT 6 valor, 'Facebook' dpensionarse FROM dual UNION SELECT 7 valor, 'Radio' dpensionarse FROM dual UNION SELECT 8 valor, 'TV' dpensionarse FROM dual UNION SELECT 9 valor, 'Contactado por CNS' dpensionarse FROM dual UNION SELECT 10 valor, 'Otro' dpensionarse FROM dual ) e11 ON (e11.valor = r.pensionarse) LEFT JOIN ( SELECT 1 valor, 'Para él' dclientesolicito FROM dual UNION SELECT 2 valor, 'Para un tercero' dclientesolicito FROM dual ) e12 ON (e12.valor = r.clientesolicito) LEFT JOIN ( SELECT 1 valor, 'Asesoría' daccionrealizo FROM dual UNION SELECT 2 valor, 'Ingreso solicitud de oferta a SCOMP' daccionrealizo FROM dual UNION SELECT 3 valor, 'Estudio Pensión' daccionrealizo FROM dual UNION SELECT 4 valor, 'Oferta Externa' daccionrealizo FROM dual UNION SELECT 5 valor, 'Cierre' daccionrealizo FROM dual ) e13 ON (e13.valor = r.accionrealizo) LEFT JOIN ( SELECT 1 valor, 'Vejez' dtipopension FROM dual UNION SELECT 2 valor, 'Vejez Anticipada' dtipopension FROM dual UNION SELECT 3 valor, 'Invalidez' dtipopension FROM dual UNION SELECT 4 valor, 'Sobrevivencia' dtipopension FROM dual ) e14 ON (e14.valor = r.tipopension) ) rb";
        return varname1;
    }

    public static String sqlObtenerMailEjecutiva() {
        String varname = "select correo from usuarios u " + "where usuario = ?";

        return varname;
    }

    public static String sqlCargarDataTable() {
        String varname = ""
                + "SELECT t6.* FROM (SELECT t5.* FROM (SELECT t3.*, rownum n "
                + "FROM (SELECT t.*, rownum rn FROM (SELECT r.*, r2.nombre, r2.rut, r2.correo, "
                + "r2.comuna, r2.region, s.nombre sucursal,  a.nombre AS nomestado FROM RFD_REFERENCIAS r "
                + "INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID) "
                + "INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID) left join rfd_ejecutivos e on (e.codigo=r.ownere) left join rfd_sucursales s on (s.id=e.sucursal_id)) t) t3 "
                + "INNER JOIN ( (SELECT t2.rn, t2.id FROM (SELECT t.*, rownum rn "
                + "FROM (SELECT r.id, OWNERE, FECHA, e.sucursal_id, CONCAT(CASE r.prioritario WHEN 1 THEN 'Prioritario' ELSE 'No Priorizado'  end, concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(r2.NOMBRE, ' '), r2.APELLIDO), ' '), r2.RUT), ' '), to_char(r.FECHA, 'Dy, dd mon YYYY')), ' '), r2.TELEFONOS), ' '), r2.CORREO), ' '), r.CANALNAME), ' '), r2.COMUNA), ' '), r2.REGION), ' '), r.OWNERENAME), ' '), a.NOMBRE), ' '), s.nombre))  campos FROM RFD_REFERENCIAS r INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID) left join rfd_ejecutivos e on (e.codigo=r.ownere) left join rfd_sucursales s on (s.id=e.sucursal_id)"
                + "INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID)) t) t2 "
                + "WHERE lower(campos) LIKE lower('%?1%') ?6 ?7 ?8 ?9 )) t4 ON "
                + "(t3.id = t4.id) "
                + "ORDER BY ?4 ?5) t5 WHERE n <=?2 ) t6 WHERE n > ?3";

        return varname;
    }

    public static String sqlFindAllNotCerrado(String tipo) {
        System.out.println("****** pasando ... ");

        String varname = null;

        if ("1".equals(tipo)) {
            varname = ""
                    + valSelect3
                    + valRcia
                    + valFromRfd
                    + valInner
                    + valInner2
                    + "where a.id in (1,2,4,5,8) "
                    + valAnd
                    + "order by rcia.fecha desc";
        }
        if ("2".equals(tipo)) {
            varname = ""
                    + valSelect3
                    + valRcia
                    + valFromRfd
                    + valInner
                    + valInner2
                    + "where a.id in (7) "
                    + valAnd
                    + valOrder;
        }
        if ("3".equals(tipo)) {
            varname = ""
                    + valSelect3
                    + valRcia
                    + valFromRfd
                    + valInner
                    + valInner2
                    + "where a.id in (6) "
                    + valAnd
                    + valOrder;
        }

        if ("4".equals(tipo)) {
            // System.out.println("******* Estoy pasando por aqui ...");

            /*
             * varname = "" + "select " + valRcia + "from " + "	rfd_Referencias rcia "
             * + "	inner join rfd_acciones a on (rcia.accion_id=a.id) "
             * + "	inner join rfd_referidos rido on (rcia.referido_id=rido.id) "
             * + "	INNER JOIN RFD_EJECUTIVOS e ON (e.codigo = rcia.OWNERE) " +
             * "where a.id in (1,2,4,5,6,7,8) "
             * +valAnd + "order by rcia.fecha desc";
             */
            varname = ""
                    + valSelect3
                    + "rcia.ID , "
                    + "rcia.VERSION, "
                    + "rcia.ACCION_ID , "
                    + "rcia.BLOCKEDBYCALL , "
                    + "rcia.CANALNAME , "
                    + "rcia.EJECUTIVO_ID , "
                    + "rcia.FECHA , "
                    + "rcia.FECHA_ACCION , "
                    + "rcia.FECHABLOCKEDBYCALL , "
                    + "rcia.FECHACLICKC , "
                    + "rcia.FECHACLICKE , "
                    + "rcia.FECHATERMINOC , "
                    + "rcia.FECHATERMINOE , "
                    + "rcia.OWNERC , "
                    + "rcia.OWNERE , "
                    + "rcia.OWNERENAME , "
                    + "rcia.REFERIDO_ID , "
                    + "rcia.REFERIDOR , "
                    + "rcia.USUARIO , "
                    + " b.comentarios MAIL_ACCION "
                    + "FROM rfd_Referencias rcia "
                    + "INNER JOIN rfd_acciones a ON (rcia.accion_id=a.id) "
                    + "INNER JOIN rfd_referidos rido ON (rcia.referido_id=rido.id) "
                    + "INNER JOIN RFD_EJECUTIVOS e ON (e.codigo = rcia.OWNERE) "
                    + "LEFT JOIN (SELECT id, REFERENCIA_ID ,COMENTARIOS  FROM RFD_BITACORAS rb "
                    + "WHERE rb.ID IN (SELECT  MAX(id)  AS max_id "
                    + "FROM RFD_BITACORAS rb "
                    + "GROUP BY REFERENCIA_ID )) b ON b.referencia_id = rcia.ID "
                    + "WHERE a.id IN (1, "
                    + "               2, "
                    + "               4, "
                    + "               5, "
                    + "               6, "
                    + "               7, "
                    + "               8) "
					+ " AND e.codigo = ? " //202404 se agrega filtro ejecutivo
                    + "ORDER BY rcia.fecha DESC --BANDERA test";
        }

        return varname;
    }

    public static String sqlFindByReagendada(String tipo) {
        String varname;

        if ("1".equals(tipo)) {
            varname = valSelect
                    + valWhere
                    + "and r.fecha_accion <= (TO_DATE(CONCAT(TO_CHAR(sysdate, 'YYYY-MM-DD' ), "
                    + "' 23:00:00'), 'YYYY/MM/DD HH24:MI:SS'))  "
                    + "  and r.ownere=? "
                    + " order by r.fecha_accion asc  ";
        } else {
            varname = valSelect
                    + valWhere
                    + " and r.ownere=? "
                    + "order by r.fecha_accion  asc ";
        }

        return varname;
    }

    public static String sqlFindByReferencia() {
        String varname = "SELECT * FROM rfd_Bitacoras b "
                + "WHERE b.referencia_id = ? order by b.fecha desc ";

        return varname;
    }

    public static String sqlPanelEjecutivo() {
        String varname = ""
                + "SELECT SYS_GUID() id, "
                + "       totaltotal.total total, "
                + "       NVL2(inactividad.total, inactividad.total, 0) tinactividad, "
                + "       ROUND(NVL2(((inactividad.total / totaltotal.total)* 100),((inactividad.total / totaltotal.total)* 100), 0), 2) pinactividad, "
                + "       inactividad.colorinactividad, "
                + "       inactividad.colorbinactividad, "
                + "       NVL2(interesados.total, interesados.total, 0) tinteresados, "
                + "       ROUND(NVL2(((interesados.total / totaltotal.total)* 100),((interesados.total / totaltotal.total)* 100), 0), 2) pinteresados, "
                + "       interesados.colorinteresados, "
                + "       interesados.colorbinteresados, "
                + "       NVL2(reagendados.total, reagendados.total, 0) treagendados, "
                + "       ROUND(NVL2(((reagendados.total / totaltotal.total)* 100),((reagendados.total / totaltotal.total)* 100), 0), 2) preagendados, "
                + "       reagendados.colorreagendados, "
                + "       reagendados.colorbreagendados, "
                + "       NVL2(derivados.total, derivados.total, 0) tderivados, "
                + "       ROUND(NVL2(((derivados.total / totaltotal.total)* 100),((derivados.total / totaltotal.total)* 100), 0), 2) pderivados, "
                + "       derivados.colorderivados, "
                + "       derivados.colorbderivados, "
                + "       NVL2(sinexito.total, sinexito.total, 0) tsinexito, "
                + "       ROUND(NVL2(((sinexito.total / totaltotal.total)* 100),((sinexito.total / totaltotal.total)* 100), 0), 2) psinexito, "
                + "       sinexito.colorsinexito, "
                + "       sinexito.colorbsinexito, "
                + "       NVL2(cerrados.total, cerrados.total, 0) tcerrados, "
                + "       ROUND(NVL2(((cerrados.total / totaltotal.total)* 100),((cerrados.total / totaltotal.total)* 100), 0), 2) pcerrados, "
                + "       cerrados.colorcerrados, "
                + "       cerrados.colorbcerrados, "
                + "       NVL2(atendidos.total, atendidos.total, 0) tatendidos, "
                + "       ROUND(NVL2(((atendidos.total / totaltotal.total)* 100),((atendidos.total / totaltotal.total)* 100), 0), 2) patendidos, "
                + "       atendidos.coloratendidos, "
                + "       atendidos.colorbatendidos, "
                + "       NVL2(genesis.total, genesis.total, 0) tgenesis, "
                + "       ROUND(NVL2(((genesis.total / totaltotal.total)* 100),((genesis.total / totaltotal.total)* 100), 0), 2) pgenesis, "
                + "       genesis.colorgenesis, "
                + "       genesis.colorbgenesis "
                + "FROM "
                + valSelect2
                + "             COUNT(*) total "
                + "     FROM rfd_referencias r  "
                + "     LEFT JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.id) LEFT JOIN rfd_ejecutivos e ON (e.codigo=r.ownere) LEFT JOIN rfd_sucursales s ON (s.id=e.sucursal_id) ?1 ?3 ?5 ) totaltotal "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorinactividad, "
                + "             a.colorb colorbinactividad "
                + valFront2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 1 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) inactividad ON (totaltotal.id = inactividad.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorinteresados, "
                + "             a.colorb colorbinteresados "
                + valFront2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 2 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) interesados ON (totaltotal.id = interesados.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorreagendados, "
                + "             a.colorb colorbreagendados "
                + valFront2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 3 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) reagendados ON (totaltotal.id = reagendados.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorderivados, "
                + "             a.colorb colorbderivados "
                + valFront2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 5 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) derivados ON (totaltotal.id = derivados.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorsinexito, "
                + "             a.colorb colorbsinexito "
                + valFront2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 6 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) sinexito ON (totaltotal.id = sinexito.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorcerrados, "
                + "             a.colorb colorbcerrados "
                + valFront2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 7 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) cerrados ON (totaltotal.id = cerrados.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color coloratendidos, "
                + "             a.colorb colorbatendidos "
                + valFront2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 8 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) atendidos ON (totaltotal.id = atendidos.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorgenesis, "
                + "             a.colorb colorbgenesis "
                + valFront2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 9 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) genesis ON (totaltotal.id = genesis.id)";

        return varname;
    }

    public static String sqlFindAllNotCerradoFull() {
        String varname = ""
                + valSelect3
                + valRcia
                + "from "
                + " rfd_Referencias rcia "
                + valInner
                + " inner join rfd_referidos rido on (rcia.referido_id=rido.id) "
                + " order by "
                + " rido.score, "
                + " a.id";

        return varname;
    }

    public static String sqlFindByReferido() {
        String varname = ""
                + "SELECT * FROM RFD_REFERENCIAS r "
                + "WHERE r.REFERIDO_ID=? "
                + "AND r.ACCION_ID<>6";

        return varname;
    }

    public static String sqlFindByReagendadaFull() {
        String varname = valSelect + valWhere + "order by r.fecha_accion ";
        System.out.println("**************************");
        System.out.println(varname);
        System.out.println("**************************");

        return varname;
    }
}
