package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.DWIndicador;
import es.sacyl.gsa.inform.bean.DWIndicadorValor;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class DWDao extends ConexionDao {

    private static final Logger LOGGER = LogManager.getLogger(DWDao.class);
    private static final long serialVersionUID = 1L;

    public DWDao() {
    }

    /**
     *
     * @param DdWIndicadorValor
     * @return
     */
    public Boolean doGrabaDatos(DWIndicadorValor dWIndicadorValor, String tabla) {
        Long id = existeIndicadorTabla(dWIndicadorValor, tabla);
        Boolean grabados = false;
        if (id == null) {
            grabados = doInserta(dWIndicadorValor, tabla);
        } else {
            dWIndicadorValor.setId(id);
            grabados = doActualiza(dWIndicadorValor, tabla);
        }
        return grabados;
    }

    /**
     *
     * @param dWIndicadorValor
     * @param tabla
     * @return
     */
    public Long existeIndicadorTabla(DWIndicadorValor dWIndicadorValor, String tabla) {
        Long id = null;
        Connection connection = null;
        try {
            connection = super.getConexionBBDD();
            switch (tabla) {
                case "DW_HOS_INDICADORES":
                    // dimension1  tiene las áreas de actividad segun SIAE
                    sql = " SELECT id FROM " + tabla + " WHERE ano=" + dWIndicadorValor.ano + " "
                            + " AND  mes=" + dWIndicadorValor.getMes() + " "
                            + " AND  indicador='" + dWIndicadorValor.indicador.getCodigo() + "'"
                            + " AND  servicio='" + dWIndicadorValor.getServicio() + "'"
                            + " AND dimension1='" + dWIndicadorValor.dimension1 + "'";
                    break;
                case "DW_RECU_INDICADORES":
                    sql = " SELECT id FROM " + tabla + " WHERE ano=" + dWIndicadorValor.ano + " "
                            + " AND  mes=" + dWIndicadorValor.getMes() + " "
                            + " AND  indicador='" + dWIndicadorValor.indicador.getCodigo() + "'";
                    if (dWIndicadorValor.getDimension1() != null) {
                        sql = sql.concat(" AND dimension1='" + dWIndicadorValor.getDimension1() + "'");
                    }
                    if (dWIndicadorValor.getDimension2() != null) {
                        sql = sql.concat(" AND dimension2='" + dWIndicadorValor.getDimension2() + "'");
                    }
                    break;

            }
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    id = resulSet.getLong("id");
                }
                statement.close();
            }
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + ":" + dWIndicadorValor.toString() + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return id;
    }

    /**
     *
     * @param dWIndicadorValor
     * @param tabla
     * @return
     */
    public Boolean doInserta(DWIndicadorValor dWIndicadorValor, String tabla) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            switch (tabla) {
                case "DW_HOS_INDICADORES":
                    dWIndicadorValor.setId(this.getSiguienteId(tabla));
                    sql = " INSERT INTO  DW_HOS_INDICADORES  (id,ano,mes,centro,servicio,dimension1,indicador,valor) "
                            + " VALUES (?,?,?,?,?,?,?,?)  ";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setLong(1, dWIndicadorValor.getId());
                        statement.setInt(2, dWIndicadorValor.getAno());
                        statement.setInt(3, dWIndicadorValor.getMes());
                        if (dWIndicadorValor.getCentro() != null && !dWIndicadorValor.getCentro().isEmpty()) {
                            statement.setString(4, dWIndicadorValor.getCentro());
                        } else {
                            statement.setNull(4, Types.NCHAR);
                        }
                        statement.setString(5, dWIndicadorValor.getServicio());
                        statement.setString(6, dWIndicadorValor.getDimension1());
                        statement.setString(7, dWIndicadorValor.getIndicador().getCodigo());
                        statement.setDouble(8, dWIndicadorValor.getValor());
                        insertadoBoolean = statement.executeUpdate() > 0;
                        statement.close();
                        LOGGER.debug(sql);
                    } catch (SQLException ex) {
                        LOGGER.error(sql + ":" + dWIndicadorValor.toString() + "  " + Utilidades.getStackTrace(ex));
                    } finally {
                        this.doCierraConexion(connection);
                    }
                    break;
                case "DW_RECU_INDICADORES":
                    dWIndicadorValor.setId(this.getSiguienteId(tabla));
                    sql = " INSERT INTO  DW_RECU_INDICADORES  (ano,mes,indicador,valor,dimension1,dimension2,id) "
                            + " VALUES (?,?,?,?,?,?,?)  ";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, dWIndicadorValor.getAno());
                        statement.setInt(2, dWIndicadorValor.getMes());
                        statement.setString(3, dWIndicadorValor.getIndicador().getCodigo());
                        statement.setDouble(4, dWIndicadorValor.getValor());
                        if (dWIndicadorValor.getDimension1() != null && !dWIndicadorValor.getDimension1().isEmpty()) {
                            statement.setString(5, dWIndicadorValor.getDimension1());
                        } else {
                            statement.setNull(5, Types.VARCHAR);
                        }
                        if (dWIndicadorValor.getDimension2() != null && !dWIndicadorValor.getDimension2().isEmpty()) {
                            statement.setString(6, dWIndicadorValor.getDimension2());
                        } else {
                            statement.setNull(6, Types.VARCHAR);
                        }
                        statement.setLong(7, dWIndicadorValor.getId());
                        insertadoBoolean = statement.executeUpdate() > 0;
                        statement.close();
                        LOGGER.debug(sql);
                    } catch (SQLException ex) {
                        LOGGER.error(sql + ":" + dWIndicadorValor.toString() + "  " + Utilidades.getStackTrace(ex));
                    } finally {
                        this.doCierraConexion(connection);
                    }
                    break;
            }
        } catch (Exception es) {

        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param dWIndicadorValor
     * @param tabla
     * @return
     *
     * Actualiza una fila en la tabla de indicadores dada
     */
    public Boolean doActualiza(DWIndicadorValor dWIndicadorValor, String tabla) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();

        sql = " UPDATE  " + tabla + " set valor =? "
                + " WHERE id=?  ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, dWIndicadorValor.getValor());
            statement.setLong(2, dWIndicadorValor.getId());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException ex) {
            LOGGER.error(sql + ":" + dWIndicadorValor.toString() + "  " + Utilidades.getStackTrace(ex));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }

        return insertadoBoolean;
    }

    public ArrayList<DWIndicadorValor> getLista(Integer ano, Integer mes, DWIndicador dWIndicador, String tabla) {
        ArrayList<DWIndicadorValor> lista = new ArrayList<>();
        Connection connection = null;
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM " + tabla;
            if (ano != null) {
                sql = sql.concat(" AND ano='" + ano + "'");
            }
            if (mes != null) {
                sql = sql.concat(" AND mes='" + mes + "'");
            }
            if (dWIndicador != null) {
                sql = sql.concat(" AND indicador ='" + dWIndicador.getCodigo() + "'");
            }
            LOGGER.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    lista.add(getRegistroResulset(resulSet));
                }
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    public DWIndicadorValor getRegistroResulset(ResultSet rs) {
        DWIndicadorValor indicador = new DWIndicadorValor();
        try {
            indicador.setId(rs.getLong("id"));
            indicador.setAno(rs.getInt("ano"));
            indicador.setMes(rs.getInt("mes"));
            indicador.setIndicador(new DWIndicadorDao().getPorCodigo(rs.getString("indicador")));
            indicador.setValor(rs.getInt("valor"));

            try {
                int orden = rs.findColumn("dimension1");
                indicador.setDimension1(rs.getString("dimension1"));
            } catch (SQLException e) {

            }

            try {
                int orden = rs.findColumn("dimension2");
                indicador.setDimension1(rs.getString("dimension2"));
            } catch (SQLException e) {

            }
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }

        return indicador;
    }

    public boolean doBorraDatos(DWIndicadorValor ob, String tabla) {
        Connection connection = null;
        Boolean borradoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM '" + tabla + "' WHERE id=" + ob.getId() + "'";
            try (Statement statement = connection.createStatement()) {
                borradoBoolean = statement.execute(sql);
                borradoBoolean = true;
                statement.close();
            }
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return borradoBoolean;
    }
}
