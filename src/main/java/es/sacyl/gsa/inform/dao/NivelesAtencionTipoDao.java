/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.NivelesAtentionTipoBean;
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
public class NivelesAtencionTipoDao extends ConexionDao implements Serializable, ConexionInterface<NivelesAtentionTipoBean> {

    private static final Logger LOGGER = LogManager.getLogger(NivelesAtencionTipoDao.class);
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public NivelesAtencionTipoDao() {
        super();
        sql = " SELECT na.id as niveltipotipoid, na.DESCRIPCION as niveltipodescripcion,na.estado  as niveltipoestado "
                + " FROM NIVELESATENCIONTIPO na WHERE  1=1 ";
    }

    /**
     * 
     * @param rs
     * @return 
     */
    @Override
    public NivelesAtentionTipoBean getRegistroResulset(ResultSet rs) {
        NivelesAtentionTipoBean nivelesAtentionTipoBean = new NivelesAtentionTipoBean();
        try {
            nivelesAtentionTipoBean.setId(rs.getLong("niveltipotipoid"));
            nivelesAtentionTipoBean.setDescripcion(rs.getString("niveltipodescripcion"));
              nivelesAtentionTipoBean.setEstado(rs.getInt("niveltipoestado"));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return nivelesAtentionTipoBean;
    }

    /**
     * 
     * @param codigo
     * @return 
     */
    @Override
    public NivelesAtentionTipoBean getPorCodigo(String codigo) {
        if (Utilidades.isNumeric(codigo)) {
            Long id = Long.parseLong(codigo);
            return getPorId(id);
        }
        return null;
    }
    
    
/**
 * 
 * @param id
 * @return 
 */
    @Override
    public NivelesAtentionTipoBean getPorId(Long id) {
        Connection connection = null;
        NivelesAtentionTipoBean nivelesAtentionTipoBean = null;
        try {
            connection = super.getConexionBBDD();
            if (id != null) {
                sql = sql.concat(" AND na.id='" + id + "'");
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        nivelesAtentionTipoBean = getRegistroResulset(resulSet);
                    }
                    statement.close();
                }
                LOGGER.debug(sql);
            }
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return nivelesAtentionTipoBean;
    }

    /**
     * 
     * @param nivelesAtentionTipoBean
     * @return 
     */
    @Override
    public boolean doGrabaDatos(NivelesAtentionTipoBean nivelesAtentionTipoBean) {
        boolean actualizado = false;

        if (this.getPorId(nivelesAtentionTipoBean.getId()) == null) {
            nivelesAtentionTipoBean.setId(getSiguienteId("NIVELESATENCIONTIPO"));
            actualizado = this.doInsertaDatos(nivelesAtentionTipoBean);
        } else {
            actualizado = this.doActualizaDatos(nivelesAtentionTipoBean);
        }
        return actualizado;
    }
    
/**
 * 
 * @param nivelesAtentionTipoBean
 * @return 
 */
    @Override
    public boolean doInsertaDatos(NivelesAtentionTipoBean nivelesAtentionTipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  NIVELESATENCIONTIPO  (id,descripcion,estado) " + " VALUES "
                    + "('" + nivelesAtentionTipoBean.getId() + "','" + nivelesAtentionTipoBean.getDescripcion() + "'"
                    + ","+nivelesAtentionTipoBean.getEstado()+ " )";
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

    /**
     * 
     * @param nivelesAtentionTipoBean
     * @return 
     */
    @Override
    public boolean doActualizaDatos(NivelesAtentionTipoBean nivelesAtentionTipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   NIVELESATENCIONTIPO  SET descripcion='" + nivelesAtentionTipoBean.getDescripcion()+"',"
                    + "estado=" + nivelesAtentionTipoBean.getEstado()
                    + " WHERE id='" + nivelesAtentionTipoBean.getId() + "'";
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                statement.close();
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
    public ArrayList<NivelesAtentionTipoBean> getLista(String texto) {
        return getLista(texto, null);
    }

    /**
     * 
     * @param texto
     * @param estado
     * @return 
     */
    public ArrayList<NivelesAtentionTipoBean> getLista(String texto, Integer estado) {
        Connection connection = null;
        ArrayList<NivelesAtentionTipoBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (estado != null) {
                sql = sql.concat(" AND estado=" + estado);
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

    /**
     * 
     * @param texto
     * @return 
     */
    public HashMap<Long, NivelesAtentionTipoBean> getMap(String texto) {
        Connection connection = null;
        HashMap<Long, NivelesAtentionTipoBean> listaCentroTipo = new HashMap<>();
        try {
            connection = super.getConexionBBDD();
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(descripcoin) like'%" + texto.toUpperCase() + "%' )");
            }
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    NivelesAtentionTipoBean nivelesAtentionTipoBean = getRegistroResulset(resulSet);
                    listaCentroTipo.put(nivelesAtentionTipoBean.getId(), nivelesAtentionTipoBean);
                }
                statement.close();
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

    /**
     * 
     * @param nivelesAtentionTipoBean
     * @return 
     */
    @Override
    public boolean doBorraDatos(NivelesAtentionTipoBean nivelesAtentionTipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   NIVELESATENCIONTIPO  SET estado=" + ConexionDao.BBDD_ACTIVONO + "WHERE id='" + nivelesAtentionTipoBean.getId() + "'";
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
