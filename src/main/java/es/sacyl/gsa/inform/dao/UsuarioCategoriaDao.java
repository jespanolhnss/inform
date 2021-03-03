/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.UsuarioCategoriaBean;
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
public class UsuarioCategoriaDao extends ConexionDao implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(UsuarioCategoriaDao.class);
    private static final long serialVersionUID = 1L;

    public UsuarioCategoriaDao() {
        super();
        sql = " SELECT uc.id as usuarioscategoriaid, uc.CODIGOPERSIGO as usuarioscategoriacodigo"
                + ", uc.nombre as usuarioscategoriaanombre,uc.estado as usuarioscategoriaestado  "
                + " FROM usuarioscategorias uc WHERE  1=1 ";
    }

    public static UsuarioCategoriaBean getRegistroResulset(ResultSet rs) {
        UsuarioCategoriaBean usuarioCategoriaBean = new UsuarioCategoriaBean();
        try {
            usuarioCategoriaBean.setId(rs.getLong("usuarioscategoriaid"));
            usuarioCategoriaBean.setCodigo(rs.getString("usuarioscategoriacodigo"));
            usuarioCategoriaBean.setNombre(rs.getString("usuarioscategoriaanombre").trim());
            usuarioCategoriaBean.setEstado(rs.getInt("usuarioscategoriaestado"));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return usuarioCategoriaBean;
    }

    public UsuarioCategoriaBean getPorCodigo(String codigo) {
        Connection connection = null;
        UsuarioCategoriaBean usuarioCategoriaBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND uc.CODIGOPERSIGO='" + codigo + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    usuarioCategoriaBean = getRegistroResulset(resulSet);
                }
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

    public UsuarioCategoriaBean getPorId(Long id) {
        Connection connection = null;
        UsuarioCategoriaBean usuarioCategoriaBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND uc.id='" + id + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    usuarioCategoriaBean = getRegistroResulset(resulSet);
                }
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

    public boolean doGrabaDatos(UsuarioCategoriaBean usuarioCategoriaBean) {
        boolean actualizado = false;

        if (this.getPorCodigo(usuarioCategoriaBean.getCodigo()) == null) {
            usuarioCategoriaBean.setId(getSiguienteId("usuarioscategorias"));
            actualizado = this.doInsertaDatos(usuarioCategoriaBean);
        } else {
            actualizado = this.doActualizaDatos(usuarioCategoriaBean);
        }
        return actualizado;
    }

    public boolean doInsertaDatos(UsuarioCategoriaBean usuarioCategoriaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  usuarioscategorias  (id,CODIGOPERSIGO,nombre, estado) "
                    + " VALUES "
                    + "(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, usuarioCategoriaBean.getId());
            if (usuarioCategoriaBean.getCodigo() != null) {
                statement.setString(2, usuarioCategoriaBean.getCodigo());
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

    public boolean doActualizaDatos(UsuarioCategoriaBean usuarioCategoriaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   usuarioscategorias  SET CODIGOPERSIGO=?,nombre=?, estado=?  WHERE id =?  ";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (usuarioCategoriaBean.getCodigo() != null) {
                statement.setString(1, usuarioCategoriaBean.getCodigo());
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

    public boolean doBorraDatos(UsuarioCategoriaBean usuarioCategoriaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   usuarioscategorias  SET estado='" + ConexionDao.BBDD_ACTIVONO + "' WHERE id=" + usuarioCategoriaBean.getId();
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

    public ArrayList<UsuarioCategoriaBean> getLista(String texto) {
        Connection connection = null;
        ArrayList<UsuarioCategoriaBean> lista = new ArrayList<>();
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
