/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.CategoriaBean;
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
 * @author 06551256M
 */
public class CategoriaDao extends ConexionDao implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(CategoriaDao.class);
    private static final long serialVersionUID = 1L;

    public CategoriaDao() {
        super();
        sql = " SELECT uc.id as usuarioscategoriaid, uc.CODIGOPERSIGO as usuarioscategoriacodigo"
                + ", uc.nombre as usuarioscategoriaanombre,uc.estado as usuarioscategoriaestado  "
                + " FROM categorias uc WHERE  1=1 ";
    }

    public static CategoriaBean getRegistroResulset(ResultSet rs) {
        CategoriaBean usuarioCategoriaBean = new CategoriaBean();
        try {
            usuarioCategoriaBean.setId(rs.getLong("usuarioscategoriaid"));
            usuarioCategoriaBean.setCodigopersigo(rs.getString("usuarioscategoriacodigo"));
            usuarioCategoriaBean.setNombre(rs.getString("usuarioscategoriaanombre"));
            usuarioCategoriaBean.setEstado(rs.getInt("usuarioscategoriaestado"));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return usuarioCategoriaBean;
    }

    public CategoriaBean getPorCodigo(String codigo) {
        Connection connection = null;
        CategoriaBean usuarioCategoriaBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND uc.CODIGOPERSIGO='" + codigo + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    usuarioCategoriaBean = getRegistroResulset(resulSet);
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
        return usuarioCategoriaBean;
    }

    public CategoriaBean getPorId(Long id) {
        Connection connection = null;
        CategoriaBean usuarioCategoriaBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND uc.id='" + id + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    usuarioCategoriaBean = getRegistroResulset(resulSet);
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
        return usuarioCategoriaBean;
    }

    public boolean doGrabaDatos(CategoriaBean usuarioCategoriaBean) {
        boolean actualizado = false;

        if (this.getPorCodigo(usuarioCategoriaBean.getCodigopersigo()) == null) {
            usuarioCategoriaBean.setId(getSiguienteId("categorias"));
            actualizado = this.doInsertaDatos(usuarioCategoriaBean);
        } else {
            actualizado = this.doActualizaDatos(usuarioCategoriaBean);
        }
        return actualizado;
    }

    public boolean doInsertaDatos(CategoriaBean usuarioCategoriaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  categorias  (id,CODIGOPERSIGO,nombre, estado) "
                    + " VALUES "
                    + "(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, usuarioCategoriaBean.getId());
            if (usuarioCategoriaBean.getCodigopersigo() != null) {
                statement.setString(2, usuarioCategoriaBean.getCodigopersigo());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (usuarioCategoriaBean.getNombre() != null) {
                statement.setString(3, usuarioCategoriaBean.getNombre());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (usuarioCategoriaBean.getEstado() != null) {
                statement.setInt(4, usuarioCategoriaBean.getEstado());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
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

    public boolean doActualizaDatos(CategoriaBean usuarioCategoriaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   categorias  SET CODIGOPERSIGO=?,nombre=?, estado=?  WHERE id =?  ";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (usuarioCategoriaBean.getCodigopersigo() != null) {
                statement.setString(1, usuarioCategoriaBean.getCodigopersigo());
            } else {
                statement.setNull(1, Types.CHAR);
            }
            if (usuarioCategoriaBean.getNombre() != null) {
                statement.setString(2, usuarioCategoriaBean.getNombre());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (usuarioCategoriaBean.getEstado() != null) {
                statement.setInt(3, usuarioCategoriaBean.getEstado());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            statement.setLong(4, usuarioCategoriaBean.getId()
            );
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
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

    public boolean doBorraDatos(CategoriaBean usuarioCategoriaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   categorias  SET estado='" + ConexionDao.BBDD_ACTIVONO + "' WHERE id=" + usuarioCategoriaBean.getId();
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            insertadoBoolean = true;
            statement.close();
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

    public ArrayList<CategoriaBean> getLista(String texto) {
        Connection connection = null;
        ArrayList<CategoriaBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(uc.nombre) like'%" + texto.toUpperCase() + "%'  ");
            }
            sql = sql.concat(" ORDER BY uc.nombre  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet));
            }
            statement.close();
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
