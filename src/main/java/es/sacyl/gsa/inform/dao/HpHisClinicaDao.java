package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.DWIndicadorHis;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class HpHisClinicaDao {

    private String cadena = null;
    private static final Logger logger = LogManager.getLogger(HpHisClinicaDao.class);

    public HpHisClinicaDao() {

    }

    public Connection conecta() {
        Connection conn = null;
        if (cadena == null) {
            cadena = new ParametroDao().getPorCodigo(ParametroBean.URL_CONEXION_CLINICA).getValor();
        }
        String dbURL2 = "jdbc:informix-sqli://" + cadena;
        try {
            Class.forName("com.informix.jdbc.IfxDriver");
            conn = DriverManager.getConnection(dbURL2);
        } catch (ClassNotFoundException ex) {
            logger.error("Error conexion his, clase no contrada.", ex);
        } catch (SQLException ex) {
            logger.error(Utilidades.getStackTrace(ex));
        }
        return conn;
    }

    /**
     *
     * @param desde
     * @param hasta
     * @return si hay una sola fila norden de calculo devuele ese valor si hay
     * mas de una fila devuelve -1 si no hya 0
     */
    public Integer existeCalculoPeriodo(LocalDate desde, LocalDate hasta) {
        Connection conn = this.conecta();
        int contador = 0;
        int norden = 0;
        String sql = " select unique norden from est_servi where  fecha_desde =?  and fecha_hasta=? ";
        if (conn != null) {
            try {
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setDate(1, java.sql.Date.valueOf(desde));
                    statement.setDate(2, java.sql.Date.valueOf(hasta));
                    ResultSet resulSet = statement.executeQuery();
                    while (resulSet.next()) {
                        contador++;
                        norden = resulSet.getInt("norden");
                    }
                    if (contador > 1) {
                        norden = -1;
                    }
                    statement.close();
                    logger.debug(" select unique norden from est_servi where  fecha_desde ='" + java.sql.Date.valueOf(desde) + "'  "
                            + "and fecha_hasta='" + java.sql.Date.valueOf(hasta) + "' ");
                }
            } catch (SQLException e) {
                logger.error(sql + Utilidades.getStackTrace(e));
            } catch (Exception e) {
                logger.error(Utilidades.getStackTrace(e));
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(Utilidades.getStackTrace(e));
                }
            }
        }
        return norden;
    }

    /**
     *
     * @param norden
     * @return
     *
     * lista de indicadoresValores de la tabla de his est_servi
     */
    public ArrayList<DWIndicadorHis> getListaEst_Servi(Integer norden) {
        Connection conn = this.conecta();
        ArrayList<DWIndicadorHis> lista = new ArrayList<>();
        String sql = " SELECT * FROM est_servi WHERE norden=? ";
        if (conn != null) {
            try {
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, norden);
                    ResultSet resulSet = statement.executeQuery();
                    logger.debug(" SELECT * FROM est_servi WHERE norden= " + norden.toString());
                    while (resulSet.next()) {
                        lista.add(getResultSetDWIndicadorValor(resulSet));
                    }
                    statement.close();

                }
            } catch (SQLException e) {
                logger.error(sql + Utilidades.getStackTrace(e));
            } catch (Exception e) {
                logger.error(Utilidades.getStackTrace(e));
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(Utilidades.getStackTrace(e));
                }
            }
        }
        return lista;
    }

    public DWIndicadorHis getResultSetDWIndicadorValor(ResultSet rs) {
// servicio     mes     anyo     codivar     valor     valorrp     norden     fecha_desde     fecha_hasta
        DWIndicadorHis indicadorValor = new DWIndicadorHis();
        try {
            indicadorValor.setAnyo(rs.getInt("anyo"));
            indicadorValor.setMes(rs.getInt("mes"));
            indicadorValor.setServicio(rs.getString("servicio"));
            if (indicadorValor.getServicio() != null) {
                indicadorValor.setServicio(indicadorValor.getServicio().trim());
            }
            indicadorValor.setValor(rs.getDouble("valor"));
            indicadorValor.setCodivar(rs.getString("codivar"));
            if (indicadorValor.getCodivar() != null) {
                indicadorValor.setCodivar(indicadorValor.getCodivar().trim());
            }
        } catch (SQLException es) {
            logger.error(Utilidades.getStackTrace(es));
        }

        return indicadorValor;
    }

}
