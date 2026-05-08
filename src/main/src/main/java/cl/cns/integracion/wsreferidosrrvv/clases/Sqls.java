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
public class Sqls {

        private static String valRcia = " rcia.* ";
        private static String valFromRfd = "from " + " rfd_Referencias rcia ";
        private static String valInner = " inner join rfd_acciones a on (rcia.accion_id=a.id) ";
        private static String valInner2 = " inner join rfd_referidos rido on (rcia.referido_id=rido.id) "
                        + " INNER JOIN RFD_EJECUTIVOS e ON (e.codigo = rcia.OWNERE) ";
        private static String valAnd = "AND e.CODIGO=? ";
        private static String valSelect = "SELECT * FROM rfd_referencias r ";
        private static String valWhere = "WHERE r.accion_id=3 ";
        private static String valFront2 = " FROM rfd_referencias r ";
        private static String valGroup = " GROUP BY color, ";
        private static String valSelect3 = " select ";
        private static String valOrder = "order by rido.score, a.id";
        private static String valSelect4 = " (SELECT 1 id, ";
        private static String valCount = " count(*) total, ";
        private static String valLeftJoin = "  LEFT JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.id) ";
        private static String valJoin = " LEFT JOIN ";
        private static String valJoin2 = "  LEFT JOIN ";

        public static String sqlDerivarMasivoDetalExportarDataTable() {
                String varname = ""
                                + "SELECT t5.* FROM (SELECT t3.*, rownum n "
                                + "FROM (SELECT t.*, rownum rn FROM (SELECT r.*, r2.nombre, r2.rut, r2.correo, r2.comuna, "
                                + "r2.region, a.nombre AS nomestado FROM RFD_REFERENCIAS r INNER JOIN RFD_REFERIDOS r2 "
                                + "ON (r.REFERIDO_ID = r2.ID) INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID)) t) t3 "
                                + "INNER JOIN ( (SELECT t2.rn FROM (SELECT t.*, rownum rn "
                                + "FROM (SELECT concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(r2.NOMBRE,' '),r2.APELLIDO),' '),r2.RUT),' '),to_char(r.FECHA,'Dy, dd mon YYYY')),' '),r2.TELEFONOS), ' '),r2.CORREO),' '), r.CANALNAME),' '),r2.COMUNA), ' '), r2.REGION),' '), r.OWNERENAME), ' '),a.NOMBRE) campos FROM RFD_REFERENCIAS r INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID) INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID)) t) t2 "
                                + "WHERE lower(campos) LIKE lower('%?1%'))) t4 ON (t3.rn=t4.rn) ) t5";

                return varname;
        }

        public static String sqlCargarDataTable() {
                String varname = ""
                                + "SELECT t6.* FROM (SELECT t5.* FROM (SELECT t3.*, rownum n "
                                + "FROM (SELECT t.*, rownum rn FROM (SELECT r.*, r2.nombre, r2.rut, r2.correo, "
                                + "r2.comuna, r2.region, a.nombre AS nomestado FROM RFD_REFERENCIAS r "
                                + "INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID) "
                                + "INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID)) t) t3 "
                                + "INNER JOIN ( (SELECT t2.rn FROM (SELECT t.*, rownum rn "
                                + "FROM (SELECT concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(concat(r2.NOMBRE,' '),r2.APELLIDO),' '),r2.RUT),' '),to_char(r.FECHA,'Dy, dd mon YYYY')),' '),r2.TELEFONOS), ' '),r2.CORREO),' '), r.CANALNAME),' '),r2.COMUNA), ' '), r2.REGION),' '), r.OWNERENAME), ' '),a.NOMBRE) campos FROM RFD_REFERENCIAS r INNER JOIN RFD_REFERIDOS r2 ON (r.REFERIDO_ID = r2.ID) "
                                + "INNER JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.ID)) t) t2 "
                                + "WHERE lower(campos) LIKE lower('%?1%'))) t4 ON (t3.rn=t4.rn) "
                                + "ORDER BY ?4 ?5) t5 WHERE rownum <=?2 ) t6 WHERE n > ?3";

                return varname;
        }

        public static String sqlFindAllNotCerrado(String tipo) {
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
                                        + valOrder;
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

                return varname;
        }

        public static String sqlFindByReagendada() {
                String varname = valSelect
                                + valWhere
                                + "and r.fecha_accion <= (TO_DATE(CONCAT(TO_CHAR(sysdate, 'YYYY-MM-DD' ), "
                                + "' 23:00:00'), 'YYYY/MM/DD HH24:MI:SS'))  and r.ownere=? "
                                + "order by r.fecha_accion ";

                return varname;
        }

        public static String sqlFindByReferencia() {
                String varname = "SELECT * FROM rfd_Bitacoras b "
                                + "WHERE b.referencia_id = ? order by b.fecha desc ";

                return varname;
        }

        public static String sqlPanelEjecutivo() {
                String varname = ""
                                + "SELECT SYS_GUID() id , "
                                + "       totaltotal.total total "
                                + "       , NVL2(inactividad.total, inactividad.total, 0) tinactividad "
                                + "       , ROUND(NVL2(((inactividad.total / totaltotal.total)* 100),((inactividad.total / totaltotal.total)* 100),0), 2) pinactividad, inactividad.colorinactividad, inactividad.colorbinactividad "
                                + "       , NVL2(interesados.total,interesados.total,0) tinteresados "
                                + "  , ROUND(NVL2((( interesados.total / totaltotal.total )* 100 ),(( interesados.total / totaltotal.total )* 100 ),0), 2 ) pinteresados, interesados.colorinteresados, interesados.colorbinteresados "
                                + "       , NVL2(reagendados.total,reagendados.total,0) treagendados "
                                + "  , ROUND(NVL2((( reagendados.total / totaltotal.total )* 100 ),(( reagendados.total / totaltotal.total )* 100 ),0), 2 ) preagendados, reagendados.colorreagendados, reagendados.colorbreagendados "
                                + "       , NVL2(derivados.total,derivados.total,0) tderivados "
                                + "       , ROUND(NVL2((( derivados.total / totaltotal.total )* 100 ),(( derivados.total / totaltotal.total )* 100 ),0), 2 ) pderivados, derivados.colorderivados, derivados.colorbderivados "
                                + "       , NVL2(sinexito.total,sinexito.total,0) tsinexito "
                                + "       , ROUND(NVL2((( sinexito.total / totaltotal.total )* 100 ),(( sinexito.total / totaltotal.total )* 100 ),0), 2 ) psinexito, sinexito.colorsinexito, sinexito.colorbsinexito "
                                + "       , NVL2(cerrados.total,cerrados.total,0) tcerrados "
                                + "       , ROUND(NVL2((( cerrados.total / totaltotal.total )* 100 ),(( cerrados.total / totaltotal.total )* 100 ),0), 2 ) pcerrados, cerrados.colorcerrados, cerrados.colorbcerrados "
                                + "       , NVL2(atendidos.total,atendidos.total,0) tatendidos "
                                + "       , ROUND(NVL2((( atendidos.total / totaltotal.total )* 100 ),(( atendidos.total / totaltotal.total )* 100 ),0), 2 ) patendidos, atendidos.coloratendidos, atendidos.colorbatendidos "
                                + "       , NVL2(genesis.total,genesis.total,0) tgenesis "
                                + "       , ROUND(NVL2((( genesis.total / totaltotal.total )* 100 ),(( genesis.total / totaltotal.total )* 100 ),0), 2 ) pgenesis, genesis.colorgenesis, genesis.colorbgenesis "
                                + " "
                                + "FROM "
                                + valSelect4
                                + "            count(*) total "
                                + valFront2
                                + "     LEFT JOIN RFD_ACCIONES a ON (r.ACCION_ID = a.id)) totaltotal "
                                + valJoin2
                                + valSelect4
                                + valCount
                                + "            a.color colorinactividad, "
                                + "            a.colorb colorbinactividad "
                                + valFront2
                                + valLeftJoin
                                + "     WHERE r.ACCION_ID = 1 "
                                + valGroup
                                + "              colorb) inactividad ON (totaltotal.id=inactividad.id) "
                                + valJoin
                                + valSelect4
                                + valCount
                                + "            a.color colorinteresados, "
                                + "            a.colorb colorbinteresados "
                                + valFront2
                                + valLeftJoin
                                + "     WHERE r.ACCION_ID = 2 "
                                + valGroup
                                + "              colorb) interesados ON (totaltotal.id=interesados.id) "
                                + valJoin
                                + valSelect4
                                + valCount
                                + "            a.color colorreagendados, "
                                + "            a.colorb colorbreagendados "
                                + valFront2
                                + valLeftJoin
                                + "     WHERE r.ACCION_ID = 3 "
                                + valGroup
                                + "              colorb) reagendados ON (totaltotal.id=reagendados.id) "
                                + valJoin
                                + valSelect4
                                + valCount
                                + "            a.color colorderivados, "
                                + "            a.colorb colorbderivados "
                                + valFront2
                                + valLeftJoin
                                + "     WHERE r.ACCION_ID = 5 "
                                + valGroup
                                + "              colorb) derivados ON (totaltotal.id=derivados.id) "
                                + valJoin
                                + valSelect4
                                + valCount
                                + "            a.color colorsinexito, "
                                + "            a.colorb colorbsinexito "
                                + valFront2
                                + valLeftJoin
                                + "     WHERE r.ACCION_ID = 6 "
                                + valGroup
                                + "              colorb) sinexito ON (totaltotal.id=sinexito.id) "
                                + valJoin2
                                + valSelect4
                                + valCount
                                + "            a.color colorcerrados, "
                                + "            a.colorb colorbcerrados "
                                + valFront2
                                + valLeftJoin
                                + "     WHERE r.ACCION_ID = 7 "
                                + valGroup
                                + "              colorb) cerrados ON (totaltotal.id=cerrados.id) "
                                + valJoin2
                                + valSelect4
                                + valCount
                                + "            a.color coloratendidos, "
                                + "            a.colorb colorbatendidos "
                                + valFront2
                                + valLeftJoin
                                + "     WHERE r.ACCION_ID = 8 "
                                + valGroup
                                + "              colorb) atendidos ON (totaltotal.id=atendidos.id) "
                                + valJoin2
                                + valSelect4
                                + valCount
                                + "            a.color colorgenesis, "
                                + "            a.colorb colorbgenesis "
                                + valFront2
                                + valLeftJoin
                                + "     WHERE r.ACCION_ID = 9 "
                                + valGroup
                                + "              colorb) genesis ON (totaltotal.id=genesis.id)";

                return varname;
        }

        public static String sqlFindAllNotCerradoFull() {
                String varname = ""
                                + "select "
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
                String varname = valSelect
                                + valWhere
                                + "and r.fecha_accion <= (TO_DATE(CONCAT(TO_CHAR(sysdate, 'YYYY-MM-DD' ), "
                                + "' 23:00:00'), 'YYYY/MM/DD HH24:MI:SS'))  "
                                + "order by r.fecha_accion ";

                return varname;
        }
}
