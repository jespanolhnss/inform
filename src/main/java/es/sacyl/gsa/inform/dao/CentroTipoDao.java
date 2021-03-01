package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author juannietopajares
 */
public class CentroTipoDao extends ConexionDao implements Serializable, ConexionInterface<CentroTipoBean> {

    private static final Logger LOGGER = LogManager.getLogger(CentroTipoDao.class);
    private static final long serialVersionUID = 1L;

    public CentroTipoDao() {
        super();
        sql = " SELECT ct.id as centrotipoid, ct.descripcion as centrotipodescripcion,ct.estado as centrotipoestado  "
                + " FROM centrostipo ct WHERE  1=1 ";
    }

    @Override
    public CentroTipoBean getRegistroResulset(ResultSet rs) {
        CentroTipoBean centroTipoBean = new CentroTipoBean();
        try {
            centroTipoBean.setId(rs.getLong("centrotipoid"));
            centroTipoBean.setDescripcion(rs.getString("centrotipodescripcion"));
            centroTipoBean.setEstado(rs.getInt("centrotipoestado"));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return centroTipoBean;
    }

    @Override
    public CentroTipoBean getPorCodigo(String codigo) {
        if (Utilidades.isNumeric(codigo)) {
            Long id = Long.parseLong(codigo);
            return getPorId(id);
        }
        return null;
    }

    @Override
    public CentroTipoBean getPorId(Long id) {
        Connection connection = null;
        CentroTipoBean centroTipoBean = null;
        try {
            connection = super.getConexionBBDD();
            if (id != null) {
                sql = sql.concat(" AND ct.id='" + id + "'");
            }
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    centroTipoBean = getRegistroResulset(resulSet);
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
        return centroTipoBean;
    }

    @Override
    public boolean doGrabaDatos(CentroTipoBean centroTipoBean) {
        boolean actualizado = false;
        if (this.getPorId(centroTipoBean.getId()) == null) {
            actualizado = this.doInsertaDatos(centroTipoBean);
        } else {
            actualizado = this.doActualizaDatos(centroTipoBean);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(CentroTipoBean centroTipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  centrostipo  (id,descripcion) " + " VALUES "
                    + "(,'" + centroTipoBean.getId() + "','" + centroTipoBean.getDescripcion() + "')";
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
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

    @Override
    public boolean doActualizaDatos(CentroTipoBean centroTipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   centrostipo  SET descripcion='" + centroTipoBean.getDescripcion()+"'"
                    + ", estado=" + centroTipoBean.getEstado()
                    + " WHERE id='" + centroTipoBean.getId() + "'";
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
            }
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));

        } catch (Exception e) {
            LOGGER.error(e);
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
     public ArrayList<CentroTipoBean> getLista(String texto) {
         return getLista(texto, null);
     }
    
    public ArrayList<CentroTipoBean> getLista(String texto, Integer estado) {
        Connection connection = null;
        ArrayList<CentroTipoBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (estado!=null){
                sql=sql.concat(" AND estado="+ estado);
            }
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(descripcion) like'%" + texto.toUpperCase() + "%' )");
            }
            sql = sql.concat(" ORDER BY descripcion  ");
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

    public HashMap<Long, CentroTipoBean> getMap(String texto) {
        Connection connection = null;
        HashMap<Long, CentroTipoBean> listaCentroTipo = new HashMap<>();
        try {
            connection = super.getConexionBBDD();
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(descripcoin) like'%" + texto.toUpperCase() + "%' )");
            }
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    CentroTipoBean centrotipo = getRegistroResulset(resulSet);
                    listaCentroTipo.put(centrotipo.getId(), centrotipo);
                }
            }
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql, Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return listaCentroTipo;
    }

    @Override
    public boolean doBorraDatos(CentroTipoBean centroTipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  centrostipo WHERE id='" + centroTipoBean.getId() + "'";
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
}
