package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class ProvinciasDAO. *
 *
 * @author Juan Nieto
 * @version 23.5.2018
 *
 */
public class ProvinciaDao extends  ConexionDao implements Serializable,ConexionInterface<ProvinciaBean> {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(ProvinciaDao.class);

    /**
     * Instantiates a new provincias DAO.
     */
    public ProvinciaDao() {
        super();
        sql = "SELECT   p. codigo as provinciacodigo, p.nombre as provincianombre,p.codauto as provinciacodauto "
                + " ,a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado "
                + " FROM   PROVINCIA p,CAUTONOM a "
                + " WHERE  p.codauto=a.codigo ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
      public  ProvinciaBean getRegistroResulset(ResultSet rs) {
          return getRegistroResulset(rs, null);
      }
    public static ProvinciaBean getRegistroResulset(ResultSet rs, AutonomiaBean autonamia) {
        ProvinciaBean provinciaBean = new ProvinciaBean();
        try {
            provinciaBean.setCodigo(rs.getString("provinciacodigo"));
            provinciaBean.setNombre(rs.getString("provincianombre").trim());
            if (autonamia == null) {
                AutonomiaBean autonomiaBean = new AutonomiaDao().getRegistroResulset(rs);
                provinciaBean.setAutonomia(autonomiaBean);
            } else {
                provinciaBean.setAutonomia(autonamia);
            }
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return provinciaBean;
    }

    /**
     *
     * @param provinciaBean
     * @return
     */
    @Override
    public boolean doGrabaDatos(ProvinciaBean provinciaBean) {
        boolean actualizado = false;

        if (this.getPorCodigo(provinciaBean.getCodigo()) == null) {
            actualizado = this.doInsertaDatos(provinciaBean);
        } else {
            actualizado = this.doActualizaDatos(provinciaBean);
        }
        return actualizado;
    }

    /**
     *
     * @param provinciaBean
     * @return
     */
    @Override
    public boolean doInsertaDatos(ProvinciaBean provinciaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  PROVINCIA  (codigo,nombre,codauto) " + " VALUES "
                    + "(,'" + provinciaBean.getCodigo() + "','" + provinciaBean.getNombre() + "','" + provinciaBean.getAutonomia().getCodigo() + "')";
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
     * @param provinciaBean
     * @return
     */
    @Override
    public boolean doActualizaDatos(ProvinciaBean provinciaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   PROVINCIA  SET nombre='" + provinciaBean.getNombre()+"'"
                    + " WHERE codigo='" + provinciaBean.getCodigo() + "'";
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
                statement.close();
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
     * @param provinciaBean
     * @return
     */
    @Override
    public boolean doBorraDatos(ProvinciaBean provinciaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  PROVINCIA WHERE codigo='" + provinciaBean.getCodigo() + "'";
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
     * @param cadena
     * @return
     */
    @Override
     public ArrayList<ProvinciaBean> getLista(String cadena) {
     return getLista(cadena,null);
     
     }
    /**
     * Gets the lista provincias.
     *
     * @param cadena
     * @param autonomiaBean
     * @return the lista provincias
     */
    public ArrayList<ProvinciaBean> getLista(String cadena, AutonomiaBean autonomiaBean) {
        Connection connection = null;
        ArrayList<ProvinciaBean> listaProvincias = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (cadena != null) {
                sql = sql.concat(" AND UPPER(p.nombre) LIKE '%" + cadena.toUpperCase() + "%'");
            }
            if (autonomiaBean != null) {
                sql = sql.concat(" AND p.codauto='" + autonomiaBean.getCodigo() + "'");
            }
            sql = sql.concat(" ORDER BY p.nombre ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                ProvinciaBean provincia = getRegistroResulset(resulSet, autonomiaBean);
                listaProvincias.add(provincia);
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
        return listaProvincias;
    }

    /**
     * Gets the por codigo.
     *
     * @param codigo the codigo
     * @return the por codigo
     */
    @Override
    public ProvinciaBean getPorCodigo(String codigo) {
        Connection connection = null;
        ProvinciaBean provincia = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND p.codigo='" + codigo + "'");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            if (resulSet.next()) {
                provincia = getRegistroResulset(resulSet, null);
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
        return provincia;
    }

    @Override
    public ProvinciaBean getPorId(Long id) {
return null;
    }

}
