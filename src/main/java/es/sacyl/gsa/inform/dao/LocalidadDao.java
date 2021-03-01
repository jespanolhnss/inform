package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.LocalidadBean;
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
 * The Class MunicipioDAO.
 *
 * @author Juan Nieto
 * @version 23.5.2018
 */
public class LocalidadDao extends ConexionDao  implements Serializable,ConexionInterface<LocalidadBean> {

    private static final Logger LOGGER = LogManager.getLogger(LocalidadDao.class);
    private static final long serialVersionUID = 1L;


    /**
     * Instantiates a new municipio DAO.
     */
    public LocalidadDao() {
        super();
        sql = " SELECT l.codigo as localidadcodigo,l.nombre as localidadnombre,l.codprov as localidadcodpro"
                + " ,p. codigo as provinciacodigo, p.nombre as provincianombre,p.codauto as provinciacodauto  "
                + " ,a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado   "
                + " FROM localidad l "
                + "  JOIN provincia p ON p.codigo=l.codprov "
                + "  JOIN CAUTONOM a ON a.codigo=p.CODAUTO  ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public  LocalidadBean getRegistroResulset(ResultSet rs) {
          return getRegistroResulset(rs, null);
      }
    /**
     * 
     * @param rs
     * @param provincia
     * @return 
     */
    public static LocalidadBean getRegistroResulset(ResultSet rs, ProvinciaBean provincia) {
        LocalidadBean localidad = new LocalidadBean();
        try {
            localidad.setCodigo(rs.getString("localidadcodigo"));
            localidad.setNombre(rs.getString("localidadnombre"));
            if (provincia == null) {
                ProvinciaBean provinciaBean = ProvinciaDao.getRegistroResulset(rs, null);
                localidad.setProvincia(provinciaBean);
            } else {
                localidad.setProvincia(provincia);
            }
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return localidad;
    }

    /**
     *
     * @param cadena
     * @return
     */
    @Override
    public ArrayList<LocalidadBean> getLista(String cadena) {
         return getLista(cadena, null,null);
     
     }
    /**
     * Gets the lista municipios.
     *
     * @param provincia the provincia
     * @return the lista municipios
     */
    public ArrayList<LocalidadBean> getLista(String cadena, AutonomiaBean  autonomiaBean, ProvinciaBean provinciaBean) {
        Connection connection = null;
        ArrayList<LocalidadBean> listaMunicipios = new ArrayList<>();
            try {
                connection = super.getConexionBBDD();
                if (cadena != null &&  ! cadena.isEmpty()) {
                    sql = sql.concat(" AND upper(l.nombre) LIKE '%" + cadena.toUpperCase().trim() + "%'");
                }
                 if (autonomiaBean != null) {
                     // autonomia no esta en la tabla localidad esta en provincia
                    sql = sql.concat(" AND p.CODAUTO='" + autonomiaBean.getCodigo() + "'");
                }
                if (provinciaBean != null) {
                    sql = sql.concat(" AND l.codprov='" + provinciaBean.getCodigo() + "'");
                }
                sql = sql.concat(" ORDER BY l.nombre ");
                Statement statement = connection.createStatement();
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    LocalidadBean municipio = getRegistroResulset(resulSet, provinciaBean);
                    listaMunicipios.add(municipio);
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
   
        return listaMunicipios;
    }

    /**
     * Gets the por id.
     *
     * @param codigo
     * @return the por id
     */
    public LocalidadBean getPorCodigo(String codigo) {
        Connection connection = null;
        LocalidadBean municipio = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND l.codigo='" + codigo + "'");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            if (resulSet.next()) {
                municipio = getRegistroResulset(resulSet, null);
            }
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return municipio;
    }

    /**
     *
     * @param localidadBean
     * @return
     */
    @Override
    public boolean doGrabaDatos(LocalidadBean localidadBean) {
        boolean actualizado = false;

        if (this.getPorCodigo(localidadBean.getCodigo()) == null) {
            actualizado = this.doInsertaDatos(localidadBean);
        } else {
            actualizado = this.doActualizaDatos(localidadBean);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(LocalidadBean localidadBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
      sql = " INSERT INTO  localidad  (codigo,descripcion,codprov ) " + " VALUES "
                    + "(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, localidadBean.getCodigo());

            if (localidadBean.getNombre()!= null) {
                statement.setString(2, localidadBean.getNombre());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (localidadBean.getProvincia()!= null  && localidadBean.getProvincia().getCodigo()!=null) {
                statement.setString(3, localidadBean.getProvincia().getCodigo());
            } else {
                statement.setNull(3, Types.CHAR);
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

    @Override
    public boolean doActualizaDatos(LocalidadBean localidadBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
      sql = " UPDATE  localidad set NOMBRE=?"
              + " WHERE  codigo=? AND codprov=? ";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (localidadBean.getNombre()!= null) {
                statement.setString(1, localidadBean.getNombre());
            } else {
                statement.setNull(1, Types.CHAR);
            }
             statement.setString(2, localidadBean.getCodigo());
             
            if (localidadBean.getProvincia()!= null  && localidadBean.getProvincia().getCodigo()!=null) {
                statement.setString(3, localidadBean.getProvincia().getCodigo());
            } else {
                statement.setNull(3, Types.CHAR);
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
     * @param municipio
     * @return
     */
    @Override
    public boolean doBorraDatos(LocalidadBean municipio) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  localidad WHERE codigo='" + municipio.getCodigo() + "'";
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
     * @param id
     * @return  null No aplica en esta tabla no tiene di
     */
    @Override
    public LocalidadBean getPorId(Long id) {
        return null;
    }

}
