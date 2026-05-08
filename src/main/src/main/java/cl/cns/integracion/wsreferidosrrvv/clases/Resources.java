/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

/**
 *
 * @author cow
 */
public class Resources {

    private static String valRcia = " rcia.* ";
    public static String SERVIDOR_SMTP_CONSORCIO = "correosmtp.cnsv.cl";
    public static int CANT_CORREOS_MAX_POR_EJECUTIVA = 2;
    private static String valFrom = " from ";
    private static String valFromRfd = "from " + " rfd_Referencias rcia ";
    private static String valInner = " inner join rfd_acciones a on (rcia.accion_id=a.id) ";
    private static String valInner2 = " inner join rfd_referidos rido on (rcia.referido_id=rido.id) "
            + " INNER JOIN RFD_EJECUTIVOS e ON (e.codigo = rcia.OWNERE) ";
    private static String valAnd = "AND e.CODIGO=? ";
    private static String valSelect = "SELECT * FROM rfd_referencias r ";
    private static String valWhere = "WHERE r.accion_id=3 ";
    private static String valSelect2 = " ( SELECT 1 id, ";
    private static String valLeft = "LEFT JOIN ";
    private static String valCount = " COUNT(*) total, ";
    private static String valFrom2 = "  FROM rfd_referencias r ";
    private static String valLeftJoin = " LEFT JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.id) LEFT JOIN rfd_ejecutivos e ON (e.codigo=r.ownere) LEFT JOIN rfd_sucursales s ON (s.id=e.sucursal_id) ";
    private static String valGroup = " GROUP BY color, ";
    private static String valSelect3 = " SELECT ";
    private static String valOrder = "order by rido.score, a.id";

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

    public static String sqlFechaFicha() {
        String varname = ""
                + valSelect3
                + " * "
                + valFrom
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

    public static String sqlObtenerMailEjecutiva() {
        String varname = "select correo from usuarios u " + "where usuario = ?";

        return varname;
    }

    public static String sqlCargarDataTable() {
        String varname = ""
                + "SELECT t6.* FROM (SELECT t5.* FROM (SELECT t3.*, rownum n "
                + "FROM (SELECT t.*, rownum rn FROM (SELECT r.*, r2.nombre, r2.rut, r2.correo, "
                + "r2.comuna, r2.region, s.nombre sucursal, a.nombre AS nomestado FROM RFD_REFERENCIAS r "
                + "INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID) "
                + "INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID) left join rfd_ejecutivos e on (e.codigo=r.ownere) left join rfd_sucursales s on (s.id=e.sucursal_id)) t) t3 "
                + "INNER JOIN ( (SELECT t2.rn, t2.id FROM (SELECT t.*, rownum rn "
                + "FROM (SELECT r.id, OWNERE, FECHA, e.sucursal_id, concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(r2.NOMBRE,' '),r2.APELLIDO),' '),r2.RUT),' '),to_char(r.FECHA,'Dy, dd mon YYYY')),' '),r2.TELEFONOS), ' '),r2.CORREO),' '), r.CANALNAME),' '),r2.COMUNA), ' '), r2.REGION),' '), r.OWNERENAME), ' '),a.NOMBRE), ' '), s.nombre) campos FROM RFD_REFERENCIAS r INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID) left join rfd_ejecutivos e on (e.codigo=r.ownere) left join rfd_sucursales s on (s.id=e.sucursal_id)"
                + "INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID)) t) t2 "
                + "WHERE lower(campos) LIKE lower('%?1%') ?6 ?7 ?8 ?9 )) t4 ON "
                + "(t3.id = t4.id) "
                + "ORDER BY ?4 ?5) t5 WHERE rownum <=?2 ) t6 WHERE n > ?3";

        return varname;
    }

    public static String sqlFindAllNotCerrado(String tipo) {
        String varname = null;
        String sel = "select ";

        if ("1".equals(tipo)) {
            varname = ""
                    + sel
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
                    + sel
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
                    + sel
                    + valRcia
                    + valFromRfd
                    + valInner
                    + valInner2
                    + "where a.id in (6) "
                    + valAnd
                    + valOrder;
        }

        if ("4".equals(tipo)) {
            varname = ""
                    + sel
                    + valRcia
                    + valFromRfd
                    + valInner
                    + valInner2
                    + "where a.id in (1,2,4,5,6,7,8) "
                    + valAnd
                    + "order by rcia.fecha desc";
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
                    + " order by r.fecha_accion desc  ";
        } else {
            varname = valSelect
                    + valWhere
                    + " and r.ownere=? "
                    + "order by r.fecha_accion  desc ";
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
                + valFrom
                + valSelect2
                + "             COUNT(*) total "
                + "     FROM rfd_referencias r  "
                + "     LEFT JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.id) LEFT JOIN rfd_ejecutivos e ON (e.codigo=r.ownere) LEFT JOIN rfd_sucursales s ON (s.id=e.sucursal_id) ?1 ?3 ?5 ) totaltotal "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorinactividad, "
                + "             a.colorb colorbinactividad "
                + valFrom2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 1 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) inactividad ON (totaltotal.id = inactividad.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorinteresados, "
                + "             a.colorb colorbinteresados "
                + valFrom2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 2 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) interesados ON (totaltotal.id = interesados.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorreagendados, "
                + "             a.colorb colorbreagendados "
                + valFrom2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 3 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) reagendados ON (totaltotal.id = reagendados.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorderivados, "
                + "             a.colorb colorbderivados "
                + valFrom2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 5 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) derivados ON (totaltotal.id = derivados.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorsinexito, "
                + "             a.colorb colorbsinexito "
                + valFrom2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 6 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) sinexito ON (totaltotal.id = sinexito.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorcerrados, "
                + "             a.colorb colorbcerrados "
                + valFrom2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 7 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) cerrados ON (totaltotal.id = cerrados.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color coloratendidos, "
                + "             a.colorb colorbatendidos "
                + valFrom2
                + valLeftJoin
                + "     WHERE r.ACCION_ID = 8 ?2 ?4 ?6 "
                + valGroup
                + "              colorb) atendidos ON (totaltotal.id = atendidos.id) "
                + valLeft
                + valSelect2
                + valCount
                + "             a.color colorgenesis, "
                + "             a.colorb colorbgenesis "
                + valFrom2
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

        return varname;
    }
}
