package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.NivelesAtencionBean;
import es.sacyl.gsa.inform.bean.NivelesAtentionTipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
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
public class NivelesAtencionDao extends ConexionDao implements Serializable, ConexionInterface<NivelesAtencionBean> {

    private static final Logger LOGGER = LogManager.getLogger(NivelesAtencionDao.class);
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public NivelesAtencionDao() {
        super();
        sql = " SELECT   n.id as nivelid,n.codigo as nivelcodigo,n.descripcion as niveldescripcion,n.tipo as niveltipo,n.estado as nivelestado "
                + " , na.id as niveltipotipoid, na.descripcion as niveltipodescripcion,na.estado  as niveltipoestado  "
                + "   , p. codigo as provinciacodigo, p.nombre as provincianombre,p.codauto as provinciacodauto "
                + " ,a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado "
                + " FROM nivelesatencion n "
                + "  JOIN  NIVELESATENCIONTIPO na  ON na.id =n.tipo"
                + " JOIN provincia p ON p.codigo=n.codprov  "
                + "  JOIN  cautonom  a ON  p.codauto=a.codigo "
                + " WHERE  1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public NivelesAtencionBean getRegistroResulset(ResultSet rs) {
        NivelesAtencionBean nivel = new NivelesAtencionBean();
        try {
            nivel.setId(rs.getLong("nivelid"));
            nivel.setCodigo(rs.getString("nivelcodigo").trim());
            nivel.setDescripcion(rs.getString("niveldescripcion").trim());
            nivel.setTipo(new NivelesAtencionTipoDao().getRegistroResulset(rs));
            nivel.setEstado(rs.getInt("nivelestado"));
            nivel.setProvincia(ProvinciaDao.getRegistroResulset(rs, null));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return nivel;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public NivelesAtencionBean getPorId(Long id) {
        Connection connection = null;
        NivelesAtencionBean nivelesAtencionBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND n.id='" + id + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    nivelesAtencionBean = getRegistroResulset(resulSet);
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
        return nivelesAtencionBean;
    }

    /**
     *
     * @param codigo
     * @return
     */
    @Override
    public NivelesAtencionBean getPorCodigo(String codigo) {
        Connection connection = null;
        NivelesAtencionBean nivelesAtencionBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND codigo='" + codigo + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    nivelesAtencionBean = getRegistroResulset(resulSet);
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
        return nivelesAtencionBean;
    }

    /**
     *
     * @param nivelesAtencionBean
     * @return
     */
    @Override
    public boolean doGrabaDatos(NivelesAtencionBean nivelesAtencionBean) {
        boolean actualizado = false;

        if (this.getPorId(nivelesAtencionBean.getId()) == null) {
            nivelesAtencionBean.setId(this.getSiguienteId("nivelesatencion"));
            actualizado = this.doInsertaDatos(nivelesAtencionBean);
        } else {
            actualizado = this.doActualizaDatos(nivelesAtencionBean);
        }
        return actualizado;
    }

    /**
     *
     * @param nivelatencion
     * @return
     */
    @Override
    public boolean doInsertaDatos(NivelesAtencionBean nivelesAtencionBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO    nivelesatencion  (id,codigo,descripcion,tipo, estado,codprov)  "
                    + " VALUES "
                    + "(?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, nivelesAtencionBean.getId());
            if (nivelesAtencionBean.getCodigo() != null) {
                statement.setString(2, nivelesAtencionBean.getCodigo());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (nivelesAtencionBean.getDescripcion() != null) {
                statement.setString(3, nivelesAtencionBean.getDescripcion());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (nivelesAtencionBean.getTipo() != null && nivelesAtencionBean.getTipo().getId() != null) {
                statement.setLong(4, nivelesAtencionBean.getTipo().getId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            if (nivelesAtencionBean.getEstado() != null) {
                statement.setInt(5, nivelesAtencionBean.getEstado());
            } else {
                statement.setNull(5, Types.INTEGER);
            }
            if (nivelesAtencionBean.getProvincia() != null && nivelesAtencionBean.getProvincia().getCodigo() != null) {
                statement.setString(6, nivelesAtencionBean.getProvincia().getCodigo());
            } else {
                statement.setNull(6, Types.INTEGER);
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

    /**
     *
     * @param nivelatencion
     * @return
     */
    @Override
    public boolean doActualizaDatos(NivelesAtencionBean nivelesAtencionBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE    nivelesatencion  SET "
                    + "codigo=?,descripcion=?,tipo=?, estado=?,codprov=? "
                    + " WHERE id=?  ";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (nivelesAtencionBean.getCodigo() != null) {
                statement.setString(1, nivelesAtencionBean.getCodigo());
            } else {
                statement.setNull(1, Types.CHAR);
            }
            if (nivelesAtencionBean.getDescripcion() != null) {
                statement.setString(2, nivelesAtencionBean.getDescripcion());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (nivelesAtencionBean.getTipo() != null && nivelesAtencionBean.getTipo().getId() != null) {
                statement.setLong(3, nivelesAtencionBean.getTipo().getId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            if (nivelesAtencionBean.getEstado() != null) {
                statement.setInt(4, nivelesAtencionBean.getEstado());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            if (nivelesAtencionBean.getProvincia() != null && nivelesAtencionBean.getProvincia().getCodigo() != null) {
                statement.setString(5, nivelesAtencionBean.getProvincia().getCodigo());
            } else {
                statement.setNull(5, Types.INTEGER);
            }
            statement.setLong(6, nivelesAtencionBean.getId());
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

    /**
     *
     * @param nivelatencion
     * @return
     */
    @Override
    public boolean doBorraDatos(NivelesAtencionBean nivelatencion) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   nivelesatencion  SET estado=" + ConexionDao.BBDD_ACTIVONO + " WHERE id='" + nivelatencion.getId() + "'";
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
    public ArrayList<NivelesAtencionBean> getLista(String texto) {
        return getLista(texto, null, null, null);
    }

    /**
     *
     * @param texto
     * @param nivelTipo
     * @param provinciaBean
     * @return
     */
    public ArrayList<NivelesAtencionBean> getLista(String texto, NivelesAtentionTipoBean nivelTipo, ProvinciaBean provinciaBean, Integer estado) {
        Connection connection = null;
        ArrayList<NivelesAtencionBean> listaniveles = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (estado != null) {
                sql = sql.concat(" AND n.estado=" + estado);
            }
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(descripcion) like'%" + texto.toUpperCase() + "%'  OR   UPPER(codigo) like'%" + texto.toUpperCase() + "%' )");
            }
            if (nivelTipo != null) {
                sql = sql.concat(" AND  n.tipo=" + nivelTipo.getId());
            }
            if (provinciaBean != null) {
                sql = sql.concat(" AND  n.CODPROV=" + provinciaBean.getCodigo());
            }
            sql = sql.concat(" ORDER BY n.descripcion  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                listaniveles.add(getRegistroResulset(resulSet));
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
        return listaniveles;
    }
}
