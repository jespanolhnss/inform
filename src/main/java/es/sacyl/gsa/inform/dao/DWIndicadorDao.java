package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.DWIndicador;
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
public class DWIndicadorDao extends ConexionDao implements ConexionInterface<DWIndicador> {

    private static final Logger LOGGER = LogManager.getLogger(DWIndicadorDao.class);
    private static final long serialVersionUID = 1L;

    public DWIndicadorDao() {
        sql = " SELECT * FROM dw_indicadores WHERE 1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public DWIndicador getRegistroResulset(ResultSet rs) {
        DWIndicador indicador = new DWIndicador();
        try {
            indicador.setCodigo(rs.getString("codigo"));
            indicador.setNombre(rs.getString("nombre"));
            indicador.setArea(rs.getString("area"));
            indicador.setTipo(rs.getString("tipo"));
            indicador.setOrden(rs.getInt("orden"));
            indicador.setVisible(rs.getString("visible"));
            indicador.setCalculado(rs.getString("calculado"));
            indicador.setItem(rs.getLong("item"));
            indicador.setCodivarhis(rs.getInt("codivarhis"));
            indicador.setSql(rs.getString("sql"));
            indicador.setDescripcion(rs.getString("descripcion"));
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
    @Override
    public ArrayList<DWIndicador> getLista(String area) {
        return getLista(area, null, null);
    }

    /**
     *
     * @param area
     * @param cadena
     * @return
     */
    public ArrayList<DWIndicador> getLista(String area, String cadena, String tipo) {
        ArrayList<DWIndicador> lista = new ArrayList<>();
        Connection connection = null;
        try {
            connection = super.getConexionBBDD();
            if (area != null && !area.isEmpty()) {
                sql = sql.concat(" AND area='" + area + "'");
            }
            if (cadena != null && !cadena.isEmpty() && !cadena.trim().equals("")) {
                sql = sql.concat(" AND  (UPPER(nombre) LIKE '%" + cadena.toUpperCase() + "%' "
                        + "  or UPPER(codigo) LIKE '%" + cadena.toUpperCase() + "%')");
            }

            if (tipo != null && !tipo.isEmpty()) {
                sql = sql.concat(" AND tipo LIKE '%" + tipo + "%'");
            }
            sql = sql.concat(" ORDER BY nombre ");
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

    @Override
    public DWIndicador getPorCodigo(String codigo) {
        DWIndicador dwindicador = null;
        Connection connection = null;
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM DW_indicadores WHERE codigo= '" + codigo + "'";
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    dwindicador = getRegistroResulset(resulSet);
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
        return dwindicador;
    }

    @Override
    public DWIndicador getPorId(Long id) {
        return null;
    }

    @Override
    public boolean doGrabaDatos(DWIndicador dWIndicador) {
        boolean grabadoo = false;
        if (this.getPorCodigo(dWIndicador.getCodigo()) == null) {
            grabadoo = doInsertaDatos(dWIndicador);
        } else {
            grabadoo = doActualizaDatos(dWIndicador);
        }
        return grabadoo;
    }

    @Override
    public boolean doInsertaDatos(DWIndicador dWIndicador) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " INSERT  INTO  DW_INDICADORES ( nombre,area,orden,visible,calculado"
                + ",formula,item,codivarhis,tablahis,sql,descripcion,tipo, codigo)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,? ) ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dWIndicador.getNombre());
            statement.setString(2, dWIndicador.getArea());
            if (dWIndicador.getOrden() != null) {
                statement.setInt(3, dWIndicador.getOrden());
            } else {
                statement.setInt(3, 0);
            }
            if (dWIndicador.getVisible() != null) {
                statement.setString(4, dWIndicador.getVisible());
            } else {
                statement.setString(4, "S");
            }
            if (dWIndicador.getCalculado() != null) {
                statement.setString(5, dWIndicador.getCalculado());
            } else {
                statement.setString(5, "N");
            }
            if (dWIndicador.getFormula() != null) {
                statement.setString(6, dWIndicador.getFormula());
            } else {
                statement.setNull(6, Types.NCHAR);
            }
            if (dWIndicador.getItem() != null) {
                statement.setLong(7, dWIndicador.getItem());
            } else {
                statement.setNull(7, Types.BIGINT);
            }
            if (dWIndicador.getCodivarhis() != null) {
                statement.setInt(8, dWIndicador.getCodivarhis());
            } else {
                statement.setNull(8, Types.NCHAR);
            }
            if (dWIndicador.getTablahis() != null) {
                statement.setString(9, dWIndicador.getTablahis());
            } else {
                statement.setNull(9, Types.NCHAR);
            }
            if (dWIndicador.getSql() != null) {
                statement.setString(10, dWIndicador.getSql());
            } else {
                statement.setNull(10, Types.NVARCHAR);
            }
            if (dWIndicador.getDescripcion() != null) {
                statement.setString(11, dWIndicador.getDescripcion());
            } else {
                statement.setNull(11, Types.NCHAR);
            }
            if (dWIndicador.getTipo() != null) {
                statement.setString(12, dWIndicador.getTipo());
            } else {
                statement.setNull(12, Types.NCHAR);
            }
            statement.setString(13, dWIndicador.getCodigo());

            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        }

        return insertadoBoolean;
    }

    @Override
    public boolean doActualizaDatos(DWIndicador dWIndicador) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " UPDATE  DW_INDICADORES  SET nombre=?,area=?,orden=?,visible=?,calculado=?"
                + ",formula=?,item=?,codivarhis=?,tablahis=?,sql=?,descripcion=?,tipo=? "
                + " WHERE codigo=?  ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dWIndicador.getNombre());
            statement.setString(2, dWIndicador.getArea());
            if (dWIndicador.getOrden() != null) {
                statement.setInt(3, dWIndicador.getOrden());
            } else {
                statement.setInt(3, 0);
            }
            if (dWIndicador.getVisible() != null) {
                statement.setString(4, dWIndicador.getVisible());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (dWIndicador.getCalculado() != null) {
                statement.setString(5, dWIndicador.getCalculado());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            //    + ",formula=?,item=?,codivarhis=?,tablahis=?,sql=?,descripcion=? "
            if (dWIndicador.getFormula() != null) {
                statement.setString(6, dWIndicador.getFormula());
            } else {
                statement.setNull(6, Types.NCHAR);
            }
            if (dWIndicador.getItem() != null) {
                statement.setLong(7, dWIndicador.getItem());
            } else {
                statement.setNull(7, Types.BIGINT);
            }
            if (dWIndicador.getCodivarhis() != null) {
                statement.setInt(8, dWIndicador.getCodivarhis());
            } else {
                statement.setNull(8, Types.NCHAR);
            }
            if (dWIndicador.getTablahis() != null) {
                statement.setString(9, dWIndicador.getTablahis());
            } else {
                statement.setNull(9, Types.NCHAR);
            }
            if (dWIndicador.getSql() != null) {
                statement.setString(10, dWIndicador.getSql());
            } else {
                statement.setNull(10, Types.NVARCHAR);
            }
            if (dWIndicador.getDescripcion() != null) {
                statement.setString(11, dWIndicador.getDescripcion());
            } else {
                statement.setNull(11, Types.NCHAR);
            }
            if (dWIndicador.getTipo() != null) {
                statement.setString(12, dWIndicador.getTipo());
            } else {
                statement.setNull(12, Types.NCHAR);
            }
            statement.setString(13, dWIndicador.getCodigo());

            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        }

        return insertadoBoolean;
    }

    @Override
    public boolean doBorraDatos(DWIndicador ob) {
        Connection connection = null;
        Boolean borradoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM dw_indicador WHERE codigo=" + ob.getCodigo() + "'";
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
