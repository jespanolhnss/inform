package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroUsuarioBean;
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
public class CentroUsuarioDao extends ConexionDao implements Serializable, ConexionInterface<CentroUsuarioBean> {

    private static final Logger LOGGER = LogManager.getLogger(CentroUsuarioDao.class);
    private static final long serialVersionUID = 1L;

    public CentroUsuarioDao() {
        super();
        sql = " SELECT cnus.id as centrousuarioid, cnus.IDUSUAIRO as centrousuarioidusuario , cnus.cargo "
                + " as centrousuariocargo, cnus.comentario as  centrousuariocomentario "
                + " FROM centrousuarios cnus  WHERE 1=1 ";
    }

    @Override
    public CentroUsuarioBean getRegistroResulset(ResultSet rs) {

        return getRegistroResulset(rs, null);
    }

    public CentroUsuarioBean getRegistroResulset(ResultSet rs, CentroBean centroBean) {

        CentroUsuarioBean centroUsuarioBean = new CentroUsuarioBean();
        try {
            centroUsuarioBean.setId(rs.getLong("centrousuarioid"));
            centroUsuarioBean.setCargo(rs.getString("centrousuariocargo"));
            centroUsuarioBean.setComentario(rs.getString("centrousuariocomentario"));
            centroUsuarioBean.setCentro(centroBean);

            centroUsuarioBean.setUsuario(new UsuarioDao().getPorId(rs.getLong("centrousuarioidusuario")));

        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return centroUsuarioBean;
    }

    @Override
    public CentroUsuarioBean getPorCodigo(String codigo) {
        return null;
    }

    @Override
    public CentroUsuarioBean getPorId(Long id) {
        Connection connection = null;
        CentroUsuarioBean centroUsuarioBean = null;
        try {
            connection = super.getConexionBBDD();

            sql = sql.concat(" AND id='" + id + "'");

            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    centroUsuarioBean = getRegistroResulset(resulSet, null);
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
        return centroUsuarioBean;
    }

    @Override
    public boolean doGrabaDatos(CentroUsuarioBean centroUsuarioBean) {
        boolean grabado = false;
        if (this.getPorId(centroUsuarioBean.getId()) == null) {
            centroUsuarioBean.setId(getSiguienteId("centrousuarios"));
            grabado = this.doInsertaDatos(centroUsuarioBean);
        } else {
            grabado = this.doActualizaDatos(centroUsuarioBean);
        }
        return grabado;
    }

    @Override
    public boolean doInsertaDatos(CentroUsuarioBean centroUsuarioBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " INSERT INTO  centrousuarios  (id,IDUSUAIRO,idcentro,cargo,comentario) "
                + " VALUES (?,?,?,?,?)  ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, centroUsuarioBean.getId());
            statement.setLong(2, centroUsuarioBean.getUsuario().getId());
            statement.setLong(3, centroUsuarioBean.getCentro().getId());
            statement.setString(4, centroUsuarioBean.getCargo());
            if (centroUsuarioBean.getComentario() != null && !centroUsuarioBean.getComentario().isEmpty()) {
                statement.setString(5, centroUsuarioBean.getComentario());
            } else {
                statement.setNull(5, Types.CHAR);
            }

            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        }
        return insertadoBoolean;
    }

    @Override
    public boolean doActualizaDatos(CentroUsuarioBean centroUsuarioBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " UPDATE   centrousuarios  SET cargo=?,comentario=? "
                + " WHERE id=? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, centroUsuarioBean.getCargo());
            if (centroUsuarioBean.getComentario() != null && !centroUsuarioBean.getComentario().isEmpty()) {
                statement.setString(2, centroUsuarioBean.getComentario());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            statement.setLong(3, centroUsuarioBean.getId());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param centroUsuarioBean
     * @return
     */
    @Override
    public boolean doBorraDatos(CentroUsuarioBean centroUsuarioBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  centrousuarios WHERE id=" + centroUsuarioBean.getId();
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

    /**
     *
     * @param texto
     * @return
     */
    @Override
    public ArrayList<CentroUsuarioBean> getLista(String texto) {
        return getLista(texto, null);
    }

    /**
     *
     * @param texto
     * @param centroBean
     * @return
     */
    public ArrayList<CentroUsuarioBean> getLista(String texto, CentroBean centroBean) {
        Connection connection = null;
        ArrayList<CentroUsuarioBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND idcentro=" + centroBean.getId());
            }
            //   sql = sql.concat(" ORDER BY id  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, centroBean));
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

    public ArrayList<CentroUsuarioBean> getLista(CentroBean centroBean) {
        return getLista(null, centroBean);
    }

}
