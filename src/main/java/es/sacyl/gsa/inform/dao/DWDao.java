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
        sql = " SELECT * FROM dw_indicadores WHERE 1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    public DWIndicador getRegistroResulset(ResultSet rs) {
        DWIndicador indicador = new DWIndicador();
        try {
            indicador.setCodigo(rs.getString("codigo"));
            indicador.setNombre(rs.getString("nombre"));
            indicador.setArea(rs.getString("area"));
            indicador.setOrden(rs.getInt("orden"));
            indicador.setVisible(rs.getString("visible"));
            indicador.setCalculado(rs.getString("calculado"));
            indicador.setItem(rs.getLong("item"));
            indicador.setCodivarhis(rs.getString("codivarhis"));
            indicador.setSql(rs.getString("sql"));
            indicador.setDescricion(rs.getString("descripcion"));
            //  System.out.println(indicador.getCodigo() + ":" + indicador.getCodivarhis());

        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }

        return indicador;
    }

    /**
     *
     * @param area
     * @return
     *
     */
    public ArrayList<DWIndicador> getLista(String area) {
        ArrayList<DWIndicador> lista = new ArrayList<>();
        Connection connection = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND area='" + area + "'");
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

    /**
     *
     * @param DdWIndicadorValor
     * @return
     */
    public Boolean doGrabaDatos(DWIndicadorValor dWIndicadorValor, String tabla) {
        Long id = existeIndicadorTabla(dWIndicadorValor, tabla);
        if (id == null) {
            doInserta(dWIndicadorValor, tabla);
        } else {
            dWIndicadorValor.setId(id);
            doActualiza(dWIndicadorValor, tabla);
        }
        return true;
    }

    public Long existeIndicadorTabla(DWIndicadorValor dWIndicadorValor, String tabla) {
        Long id = null;
        Connection connection = null;
        try {
            connection = super.getConexionBBDD();
            switch (tabla) {
                case "DW_HOS_INDICADORES":
                    sql = " SELECT id FROM " + tabla + " WHERE anyo=" + dWIndicadorValor.ano + " "
                            + " AND  mes=" + dWIndicadorValor.getMes() + " "
                            + " AND  indicador='" + dWIndicadorValor.indicador.getCodigo() + "'"
                            + " ";
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
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return id;
    }

    public Boolean doInserta(DWIndicadorValor dWIndicadorValor, String tabla) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        switch (tabla) {
            case "DW_HOS_INDICADORES":
                dWIndicadorValor.setId(this.getSiguienteId(tabla));
                sql = " INSERT INTO  DW_HOS_INDICADORES  (id,anyo,mes,centro,servicio,area,indicador,valor) "
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
                    statement.setString(6, dWIndicadorValor.getAreahosp());
                    statement.setString(7, dWIndicadorValor.getIndicador().codigo);
                    statement.setDouble(8, dWIndicadorValor.getValor());
                    insertadoBoolean = statement.executeUpdate() > 0;
                    statement.close();
                    LOGGER.debug(sql);
                } catch (SQLException ex) {
                    LOGGER.error(Utilidades.getStackTrace(ex));
                }
                break;
        }

        return insertadoBoolean;
    }

    public Boolean doActualiza(DWIndicadorValor dWIndicadorValor, String tabla) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        switch (tabla) {
            case "DW_HOS_INDICADORES":
                dWIndicadorValor.setId(this.getSiguienteId(tabla));
                sql = " UPDATE  DW_HOS_INDICADORES  set valor =? "
                        + " WHERE id=?  ";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setDouble(1, dWIndicadorValor.getValor());
                    statement.setLong(2, dWIndicadorValor.getId());
                    insertadoBoolean = statement.executeUpdate() > 0;
                    statement.close();
                    LOGGER.debug(sql);
                } catch (SQLException ex) {
                    LOGGER.error(Utilidades.getStackTrace(ex));
                }
                break;
        }

        return insertadoBoolean;
    }
}
