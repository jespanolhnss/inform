package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GfhDao extends ConexionDao implements Serializable, ConexionInterface<GfhBean> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(GfhDao.class);

    public GfhDao() {
        super();
        sql = " SELECT gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasistencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado,gfh.gfhpersigo "
                + " FROM gfh gfh WHERE 1=1 ";
    }

    /**
     *
     * @return
     */
    @Override
    public GfhBean getRegistroResulset(ResultSet rs) {
        GfhBean gfh = null;
        try {
            gfh = new GfhBean();
            gfh.setId(rs.getLong("gfhid"));
            gfh.setIdjimena(rs.getLong("gfhidjimena"));
            gfh.setCodigo(rs.getString("gfhcodigo"));
            gfh.setDescripcion(rs.getString("gfhdescripcion"));
            gfh.setAsistencial(rs.getInt("gfhasistencial"));
            gfh.setEstado(rs.getInt("gfhestado"));
            gfh.setGfhpersigo(rs.getString("gfhpersigo"));
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return gfh;
    }

    /**
     *
     * @param gfhBean
     * @return
     */
    @Override
    public boolean doGrabaDatos(GfhBean gfhBean) {
        boolean actualizado = false;
        if (this.getPorId(gfhBean.getId()) == null) {
            gfhBean.setId(getSiguienteId("gfh"));
            actualizado = doInsertaDatos(gfhBean);
        } else {
            actualizado = this.doActualizaDatos(gfhBean);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(GfhBean gfhBean) {

        Connection connection = null;
        boolean insertado = false;
        try {
            connection = super.getConexionBBDD();

            sql = " INSERT INTO gfh (id,codigo,idjimena,descripcion,asistencial,estado,fechacambio,usucambio,gfhpersigo) "
                    + " VALUES (?,?,?,?,?,?,?,?,?)  ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, gfhBean.getId());
                statement.setString(2, gfhBean.getCodigo());
                if (gfhBean.getIdjimena() != null) {
                    statement.setLong(3, gfhBean.getIdjimena());
                } else {
                    statement.setNull(3, Types.INTEGER);
                }
                statement.setString(4, gfhBean.getDescripcion());
                statement.setInt(5, gfhBean.getAsistencial());
                statement.setInt(6, gfhBean.getEstado());
                statement.setLong(7, Long.parseLong(gfhBean.getFechacambio().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
                if (gfhBean.getUsucambio() != null && gfhBean.getUsucambio().getId() != null) {
                    statement.setLong(8, gfhBean.getUsucambio().getId());
                } else {
                    statement.setLong(8, UsuarioBean.USUARIO_SISTEMA.getId());
                }
                if (gfhBean.getGfhpersigo() != null) {
                    statement.setString(9, gfhBean.getGfhpersigo());
                } else {
                    statement.setNull(9, Types.VARCHAR);
                }
                insertado = statement.executeUpdate() > 0;
                statement.close();
            }

            logger.debug(sql);
        } catch (SQLException e) {
            logger.debug(Utilidades.getStackTrace(e));

        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    /**
     *
     * @param gfhBean
     * @return
     */
    @Override
    public boolean doActualizaDatos(GfhBean gfhBean) {
        Connection connection = null;
        boolean insertado = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   gfh SET "
                    + " codigo=?,idjimena=?,descripcion=?,asistencial=?,estado=?,fechacambio=?,usucambio=?,gfhpersigo=?  "
                    + " WHERE id= ? ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, gfhBean.getCodigo());
                if (gfhBean.getIdjimena() != null) {
                    statement.setLong(2, gfhBean.getIdjimena());
                } else {
                    statement.setNull(2, Types.INTEGER);
                }

                statement.setString(3, gfhBean.getDescripcion());
                statement.setInt(4, gfhBean.getAsistencial());
                statement.setInt(5, gfhBean.getEstado());
                statement.setLong(6, Long.parseLong(gfhBean.getFechacambio().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
                if (gfhBean.getUsucambio() != null && gfhBean.getUsucambio().getId() != null) {
                    statement.setLong(7, gfhBean.getUsucambio().getId());
                } else {
                    statement.setNull(7, Types.INTEGER);
                }
                if (gfhBean.getGfhpersigo() != null) {
                    statement.setString(8, gfhBean.getGfhpersigo());
                } else {
                    statement.setNull(8, Types.VARCHAR);
                }
                statement.setLong(9, gfhBean.getId());
                insertado = statement.executeUpdate() > 0;
                statement.close();
            }

            logger.debug(sql);
        } catch (SQLException e) {
            logger.debug(Utilidades.getStackTrace(e));

        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    /**
     *
     * @param gfhBean
     * @return
     */
    @Override
    public boolean doBorraDatos(GfhBean gfhBean) {

        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuarioa = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();

            sql = " UPDATE gfh SET   usucambio='" + usuarioa.getDni() + "',  fechacambio="
                    + Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))) + ",estado=" + ConexionDao.BBDD_ACTIVONO
                    + " WHERE id=" + gfhBean.getId();
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
                statement.close();
            }
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
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
    public ArrayList<GfhBean> getLista(String texto) {
        return getLista(texto, null);
    }

    public ArrayList<GfhBean> getLista(String texto, Integer estado) {
        Connection connection = null;
        ArrayList<GfhBean> lista = new ArrayList<GfhBean>();
        try {
            connection = super.getConexionBBDD();
            if (estado != null) {
                sql = sql.concat(" AND estado=" + estado);
            }
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND descripcion LIKE '%" + texto + "%'");
            }
            sql = sql.concat(" ORDER BY descripcion	");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                GfhBean gfh = getRegistroResulset(resulSet);
                lista.add(gfh);
            }
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    @Override
    public GfhBean getPorId(Long id) {

        Connection connection = null;
        GfhBean gfh = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND id=" + id);
            logger.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    gfh = getRegistroResulset(resulSet);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return gfh;
    }

    public GfhBean getServicioIdJimena(Long id) {
        Connection connection = null;
        GfhBean gfh = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND idjimena=" + id);
            logger.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    gfh = getRegistroResulset(resulSet);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return gfh;
    }

    /**
     *
     * @param codigo
     * @param estado
     * @return
     */
    public GfhBean getPorCodigo(String codigo, Integer estado) {
        Connection connection = null;
        GfhBean gfh = null;
        try {
            connection = super.getConexionBBDD();
            //      sql = "SELECT  *  FROM gfh WHERE 1=1 ";
            if (estado != null) {
                sql = sql.concat(" AND  estado =" + estado);
            }
            if (codigo != null && !codigo.isEmpty()) {
                sql = sql.concat(" AND codigo='" + codigo + "'");
            }

            logger.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    gfh = getRegistroResulset(resulSet);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return gfh;
    }

    public GfhBean getPorCodigoPersigo(String codigo) {
        Connection connection = null;
        GfhBean gfh = null;
        try {
            connection = super.getConexionBBDD();
            //  sql = "SELECT  *  FROM gfh WHERE 1=1 ";
            if (codigo != null && !codigo.isEmpty()) {
                sql = sql.concat(" AND gfhpersigo='" + codigo + "'");
            }
            logger.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    gfh = getRegistroResulset(resulSet);
                } else {
                    logger.error("No existe gfh en hnss para el código persigo " + codigo);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return gfh;
    }

    public GfhBean getServicioCodigoExiste(String codigo, GfhBean gfhParam) {
        Connection connection = null;
        GfhBean gfh = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND  estado=" + ConexionDao.BBDD_ACTIVOSI + " AND codigo='" + codigo
                    + "' AND id!=" + gfhParam.getId());
            logger.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    gfh = getRegistroResulset(resulSet);
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return gfh;
    }

    @Override
    public GfhBean getPorCodigo(String codigo) {
        return getPorCodigo(codigo, null);
    }

}
