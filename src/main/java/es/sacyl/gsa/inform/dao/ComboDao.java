package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
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
 * @author JuanNieto
 */
public class ComboDao extends ConexionDao implements Serializable, ConexionInterface<ComboBean> {

    private static final Logger LOGGER = LogManager.getLogger(ComboDao.class);
    private static final long serialVersionUID = 1L;

    @Override
    public ComboBean getRegistroResulset(ResultSet rs) {
        ComboBean combo = new ComboBean();
        try {
            combo.setId(rs.getLong("id"));
            combo.setGrupo(rs.getString("grupo").trim());
            combo.setDescripcion(rs.getString("descripcion").trim());
            combo.setOrden(rs.getInt("orden"));
            combo.setRama(rs.getString("rama").trim());
            combo.setValor(rs.getString("valor").trim());
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return combo;
    }

    @Override
    public ComboBean getPorCodigo(String codigo) {
        if (Utilidades.isNumeric(codigo)) {
            Long id = Long.parseLong(codigo);
            return getPorId(id);
        }
        return null;
    }

    @Override
    public ComboBean getPorId(Long id) {
        Connection connection = null;
        ComboBean combo = null;
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM combos_informatica WHERE 1=1 ";
            if (id != null) {
                sql = sql.concat(" AND id=" + id);
            } else {
                return null;
            }
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            if (resulSet.next()) {
                combo = getRegistroResulset(resulSet);
            }
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql, Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return combo;
    }

    @Override
    public boolean doGrabaDatos(ComboBean combo) {
        boolean actualizado = false;
        if (this.getPorId(combo.getId()) == null) {
            combo.setId(getSiguienteId("combos_informatica"));
            actualizado = this.doInsertaDatos(combo);
        } else {
            actualizado = this.doActualizaDatos(combo);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(ComboBean combo) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = "INSERT INTO     combos_informatica "
                    + "( id, grupo,descripcion,orden,pertenece,rama,valor )"
                    + " VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            combo.setId(this.getSiguienteId("combos_informatica"));
            statement.setLong(1, combo.getId());
            if (combo.getGrupo() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, combo.getGrupo());
            }
            if (combo.getDescripcion() == null) {
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(3, combo.getDescripcion());
            }
            if (combo.getOrden() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, combo.getOrden());
            }
            if (combo.getPertenece() == null) {
                statement.setNull(5, Types.INTEGER);
            } else {
                statement.setLong(5, combo.getPertenece());
            }
            if (combo.getRama() == null) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setString(6, combo.getRama());
            }
            if (combo.getValor() == null) {
                statement.setNull(7, Types.VARCHAR);
            } else {
                statement.setString(7, combo.getValor());
            }
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException e) {
            LOGGER.error(sql, e);

        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public boolean doActualizaDatos(ComboBean combo) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {

            connection = super.getConexionBBDD();
            sql = sql = "UPDATE      combos_informatica  SET"
                    + "  grupo=?,descripcion=?,orden=?,pertenece=?,rama=?,valor=? "
                    + " WHERE id=?";

            PreparedStatement statement = connection.prepareStatement(sql);

            if (combo.getGrupo() == null) {
                statement.setNull(1, Types.VARCHAR);
            } else {
                statement.setString(1, combo.getGrupo());
            }
            if (combo.getDescripcion() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, combo.getDescripcion());
            }
            if (combo.getOrden() == null) {
                statement.setNull(3, Types.INTEGER);
            } else {
                statement.setInt(3, combo.getOrden());
            }
            if (combo.getPertenece() == null) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setLong(4, combo.getPertenece());
            }
            if (combo.getRama() == null) {
                statement.setNull(5, Types.VARCHAR);
            } else {
                statement.setString(5, combo.getRama());
            }
            if (combo.getValor() == null) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setString(6, combo.getValor());
            }
            statement.setLong(7, combo.getId());
            LOGGER.debug(sql);
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException e) {
            LOGGER.error(sql, Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public ArrayList<ComboBean> getLista(String cadena) {
        return getLista(cadena, null, null);
    }

    /**
     *
     * @param texto
     * @param grupo
     * @return
     */
    public ArrayList<ComboBean> getLista(String texto, String grupo, String rama) {
        Connection connection = null;
        ArrayList<ComboBean> listaCombos = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM combos_informatica WHERE  1=1 ";
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  UPPER(descripcion) like'%" + texto.toUpperCase() + "%'");
            }
            if (grupo != null) {
                sql = sql.concat(" AND  UPPER(grupo) like'%" + grupo.toUpperCase() + "%'");
            }
            if (rama != null) {
                sql = sql.concat(" AND  UPPER(rama) like'%" + rama.toUpperCase() + "%'");
            }
            sql = sql.concat("ORDER BY descripcion  ");
            LOGGER.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    listaCombos.add(getRegistroResulset(resulSet));
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
        return listaCombos;
    }

    public ArrayList<String> getListaGruposCombos() {
        Connection connection = null;
        ArrayList<String> listaGrupos = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT  DISTINCT grupo FROM 	 combos_informatica GROUP by grupo ";
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    listaGrupos.add(resulSet.getString("grupo"));
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
        return listaGrupos;
    }

    @Override
    public boolean doBorraDatos(ComboBean combo) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  combos_informatica WHERE id='" + combo.getId() + "'";
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
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
        return insertadoBoolean;
    }

    public ArrayList<String> getListaGrupos(String grupo, Integer anchoRama) {
        Connection connection = null;

        ArrayList<String> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT UNIQUE rama FROM combos_informatica WHERE grupo='" + grupo + "'"
                    + " ORDER BY  rama  ";
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    String cadena = resulSet.getString("rama");
                    if (cadena.length() > anchoRama) {
                        cadena = cadena.substring(0, anchoRama);
                    }
                    lista.add(cadena);
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
        return lista;
    }

    public ArrayList<String> getListaGruposRama(String grupo, Integer anchoCadena) {
        Connection connection = null;

        ArrayList<String> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT UNIQUE rama FROM combos_informatica WHERE grupo='" + grupo + "' ORDER BY rama";
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    String cadena = resulSet.getString("rama");
                    if (cadena.length() > anchoCadena) {
                        cadena = cadena.substring(0, anchoCadena);
                    }
                    lista.add(cadena);
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
        return lista;
    }

    public ArrayList<String> getListaGruposRamaValor(String grupo, String rama, Integer anchoCadena) {
        Connection connection = null;

        ArrayList<String> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT UNIQUE valor FROM combos_informatica WHERE grupo='" + grupo + "'"
                    + " AND rama='" + rama + "'  ";
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    String cadena = resulSet.getString("valor");
                    if (cadena.length() > anchoCadena) {
                        cadena = cadena.substring(0, anchoCadena);
                    }
                    lista.add(cadena);
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
        return lista;
    }
}
