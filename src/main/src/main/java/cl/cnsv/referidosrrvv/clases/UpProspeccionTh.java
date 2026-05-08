/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.models.Referidos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author cox
 */
public class UpProspeccionTh implements Runnable {

    private List<Referidos> referidos = null;

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(
            UpProspeccionTh.class.getName());
    private final String mensaje;
    private boolean pasaronDatos;

    public UpProspeccionTh(
            List<Referidos> r,
            String mensaje,
            boolean pasaronDatos) {
        this.referidos = r;
        this.mensaje = mensaje;
        this.pasaronDatos = pasaronDatos;
    }

    @Override
    public void run() {
        Connection conn = null;
        String vRutSDV, vRut;
        Integer tam = referidos.size();
        Integer x = 0;
        for (Referidos o : referidos) {
            x++;
            Referidos r = (Referidos) o;
            vRut = r.getRut();
            vRutSDV = vRutSDV = ValidaRutProspeccion.obtener(vRut);

            if ((vRutSDV.length() > 0) && (!vRut.contains("SR"))) {
                PreparedStatement ps = null;
                try {
                    continuar2(vRutSDV, r, conn, ps, x, tam);
                } catch (Exception ex) {
                    LOGGER.error(ex);
                } finally {
                    continuar(ps, conn);
                }
            }
        }
    }

    private void continuar(PreparedStatement ps, Connection conn) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ex) {
                Logger
                        .getLogger(UpProspeccionTh.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger
                        .getLogger(UpProspeccionTh.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    private void continuar2(
            String vRutSDV, Referidos r, Connection conn, PreparedStatement ps, Integer x, Integer tam)
            throws NamingException, SQLException {
        String p = Prospection2.consultar(vRutSDV, 5000);

		String sql = "UPDATE RFD_REFERIDOS SET score = ? WHERE id = ?";

        try {
			conn = Resources.getConexion();
			ps = conn.prepareStatement(sql);
			ps.setString(1, p);
			ps.setBigDecimal(2, r.getId()); 
			ps.executeUpdate();
        } catch (SQLException e) {
                Logger
                        .getLogger(UpProspeccionTh.class.getName())
                        .log(Level.SEVERE, null, e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                Logger
                        .getLogger(UpProspeccionTh.class.getName())
                        .log(Level.SEVERE, null, e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Manejo de excepciones
                }
            }
        }

        LOGGER.info("**  -> "
                + mensaje
                + "  -> "
                + "["
                + x.toString()
                + "/"
                + tam.toString()
                + "] -> "
                + r.getNombre()
                + " -> "
                + r.getRut()
                + " -> "
                + vRutSDV
                + " -> "
                + p);
    }
}
