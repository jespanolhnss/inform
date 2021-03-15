package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.GerenciaBean;
import es.sacyl.gsa.inform.bean.LocalidadBean;
import es.sacyl.gsa.inform.bean.NivelesAtencionBean;
import es.sacyl.gsa.inform.bean.NivelesAtentionTipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.ZonaBean;
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
 * @author juannietopajares
 */
public class CentroDao extends ConexionDao implements Serializable, ConexionInterface<CentroBean> {

    private static final Logger LOGGER = LogManager.getLogger(CentroDao.class);
    private static final long serialVersionUID = 1L;

    public CentroDao() {
        super();
        sql = " SELECT  c.ID as centroid,c.CODAUTO as centrocodauto,c.CODGEREN   as centrocodgeren"
                + " ,c.CODZONA as centrocodozona "
                + " ,c.CODIGO as centrocodigo, c.NOMCEN as centronumcen ,c.TIPOVIA as centrotipovia "
                + " ,c.CALLECEN as centrocallecen,c.NUMCALCEN as centronumcalcen,c.OTRDIRCEN as centrootrodircen"
                + " ,c.CODLOCAL as centrocodlocal "
                + " ,c.CPCENTRO as centrocpcentro,c.TELEPREV as centroteleprev,c.TIPOCENTRO as centrotipocentro"
                + " ,c.NIVATENCION as centronivatencion "
                + " ,c.estado as centroestado, c.mapgoogle as centromapggole, c.nomcorto as centronomcorto  "
                + " ,n.id as nivelid,n.codigo as nivelcodigo,n.descripcion as niveldescripcion,n.tipo as niveltipo"
                + " ,n.estado as nivelestado  "
                + " ,l.codigo as localidadcodigo,l.nombre localidadnombre,l.nombre localidadprovincia "
                + " ,p.codigo as provinciacodigo,p.nombre as provincianombre,p.nombre provinciacodauto "
                + " ,a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado "
                + " ,g.codauto gerenciacodauto ,g.codigo   gerenciacodigo ,g.nombre  gerencianombre, g.tipovia   gerenciatipovia"
                + " , g.callesec   gerenciacallesec,g.numcalsec  gerencianumcalsec "
                + "  ,g.otrdomger  gerenciaotrdomger, g.cpger  gerenciacpger, g.localger   gerencialocalger, g.estado gerenciaestado  "
                + " ,z.codauto  zonacodauto  ,z.codgeren  zonacodgeren,z.codigo  zonacodigo,z.nombre  zonanombre,z.codprov zonacodprov "
                + " ,ct.id as centrotipoid, ct.descripcion as centrotipodescripcion,ct.estado as centrotipoestado  "
                + " ,na.id as niveltipotipoid, na.descripcion as niveltipodescripcion,na.estado  as niveltipoestado  "
                + " FROM CENTROS c  "
                + " LEFT JOIN nivelesatencion n ON n.id = c.NIVATENCION  "
                + " JOIN LOCALIDAD l ON l.codigo = c.CODLOCAL "
                + " JOIN PROVINCIA p ON p.codigo=l.codprov "
                + " JOIN CAUTONOM a On a.codigo=p.CODAUTO  "
                + " JOIN GERENCIA g ON g.codauto=a.codigo AND g.codigo=c.codgeren  "
                + " JOIN ZONAS z ON z.codauto=c.codauto AND z.codgeren=g.codigo AND z.codigo=c.codzona "
                + " JOIN centrostipo ct ON ct.id=c.tipocentro "
                + " JOIN  NIVELESATENCIONTIPO na  ON na.id =n.tipo"
                + " WHERE 1=1 ";

    }

    @Override
    public CentroBean getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null, null);
    }

    public static CentroBean getRegistroResulset(ResultSet rs, AutonomiaBean autonomia, LocalidadBean localidad) {
        CentroBean centroBean = new CentroBean();
        try {
            centroBean.setId(rs.getLong("centroid"));
            if (autonomia == null) {
                AutonomiaBean autonomiaBean = new AutonomiaDao().getRegistroResulset(rs);
                centroBean.setAutonomia(autonomiaBean);
            } else {
                centroBean.setAutonomia(autonomia);
            }
            //  centroBean.setCodGeren(rs.getString("centrocodgeren"));
            GerenciaBean gerenciaBean = new GerenciaDao().getRegistroResulset(rs);
            centroBean.setGerencia(gerenciaBean);
            ZonaBean zonaBean = ZonaDao.getRegistroResulset(rs, centroBean.getAutonomia(), centroBean.getGerencia(), null);
            //centroBean.setCodzona(rs.getString("centrocodozona"));
            centroBean.setZona(zonaBean);
            centroBean.setCodigo(rs.getString("centrocodigo"));
            centroBean.setNomcen(rs.getString("centronumcen"));
            centroBean.setTipovia(rs.getString("centrotipovia"));
            centroBean.setCallecen(rs.getString("centrocallecen"));
            centroBean.setNumcalcen(rs.getInt("centronumcalcen"));
            centroBean.setOtrdirecen(rs.getString("centrootrodircen"));
            if (localidad == null) {
                ProvinciaBean provinciaBean = ProvinciaDao.getRegistroResulset(rs, centroBean.getAutonomia());
                LocalidadBean localidadBean = LocalidadDao.getRegistroResulset(rs, provinciaBean);
                centroBean.setLocalidad(localidadBean);
            } else {
                centroBean.setLocalidad(localidad);
            }
            centroBean.setCpcentro(rs.getString("centrocpcentro"));
            centroBean.setTeleprev(rs.getString("centroteleprev"));

            CentroTipoBean centroTiBean = new CentroTipoDao().getRegistroResulset(rs);
            centroBean.setTipocentro(centroTiBean);

            NivelesAtencionBean nivelesAtencionBean = new NivelesAtencionDao().getRegistroResulset(rs);
            centroBean.setNivatencion(nivelesAtencionBean);
            centroBean.setEstado(rs.getInt("centroestado"));
            centroBean.setMapgoogle(rs.getString("centromapggole"));
            centroBean.setNomcorto(rs.getString("centronomcorto"));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return centroBean;
    }

    @Override
    public CentroBean getPorCodigo(String codigo) {
        if (Utilidades.isNumeric(codigo)) {
            Long id = Long.parseLong(codigo);
            return getPorId(id);
        }
        return null;
    }

    public CentroBean getPorId(Long id, AutonomiaBean autonomiaBean, LocalidadBean localidadBean) {
        if (id == null) {
            return null;
        }
        Connection connection = null;
        CentroBean centroBean = null;
        try {
            connection = super.getConexionBBDD();

            sql = sql.concat(" AND c.id='" + id + "'");

            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    centroBean = getRegistroResulset(resulSet, autonomiaBean, localidadBean);
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
        return centroBean;
    }

    @Override
    public CentroBean getPorId(Long id) {
        return getPorId(id, null, null);

    }

    public CentroBean getPorNombre(String cadena, AutonomiaBean autonomiaBean) {
        Connection connection = null;
        CentroBean centroBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND    NOMCEN like '%" + cadena + "%'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    centroBean = getRegistroResulset(resulSet, autonomiaBean, null);
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
        return centroBean;
    }

    @Override
    public boolean doGrabaDatos(CentroBean centroBean) {
        return doGrabaDatos(centroBean, null);
    }

    public boolean doGrabaDatos(CentroBean centroBean, AutonomiaBean autonomiaBean) {
        boolean actualizado = false;

        if (this.getPorId(centroBean.getId(), autonomiaBean, null) == null) {
            centroBean.setId(getSiguienteId("centros"));
            actualizado = this.doInsertaDatos(centroBean);
        } else {
            actualizado = this.doActualizaDatos(centroBean);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(CentroBean centroBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  CENTROS (ID,CODAUTO,CODGEREN,CODZONA,CODIGO,NOMCEN,TIPOVIA   "
                    + ",CALLECEN,NUMCALCEN,OTRDIRCEN,CODLOCAL "
                    + " ,CPCENTRO,TELEPREV,TIPOCENTRO,NIVATENCION,ESTADO,MAPGOOGLE,NOMCORTO ) "
                    + " VALUES "
                    + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, centroBean.getId());
            if (centroBean.getAutonomia() != null && centroBean.getAutonomia().getCodigo() != null) {
                statement.setString(2, centroBean.getAutonomia().getCodigo());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (centroBean.getGerencia() != null && centroBean.getGerencia().getCodigo() != null) {
                statement.setString(3, centroBean.getGerencia().getCodigo());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (centroBean.getZona() != null && centroBean.getZona().getCodigo() != null) {
                statement.setString(4, centroBean.getZona().getCodigo());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (centroBean.getCodigo() != null) {
                statement.setString(5, centroBean.getCodigo());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            if (centroBean.getNomcen() != null) {
                statement.setString(6, centroBean.getNomcen());
            } else {
                statement.setNull(6, Types.CHAR);
            }
            // ,TIPOVIA   ,CALLECEN  ,   NUMCALCEN  ,OTRDIRCEN  ,   CODLOCAL   ,   CPCENTRO
            if (centroBean.getTipovia() != null) {
                statement.setString(7, centroBean.getTipovia());
            } else {
                statement.setNull(7, Types.CHAR);
            }
            if (centroBean.getCallecen() != null) {
                statement.setString(8, centroBean.getCallecen());
            } else {
                statement.setNull(8, Types.CHAR);
            }
            if (centroBean.getNumcalcen() != null) {
                statement.setLong(9, centroBean.getNumcalcen());
            } else {
                statement.setNull(9, Types.INTEGER);
            }
            if (centroBean.getOtrdirecen() != null) {
                statement.setString(10, centroBean.getOtrdirecen());
            } else {
                statement.setNull(10, Types.CHAR);
            }
            if (centroBean.getLocalidad() != null && centroBean.getLocalidad().getCodigo() != null) {
                statement.setString(11, centroBean.getLocalidad().getCodigo());
            } else {
                statement.setNull(11, Types.CHAR);
            }
            if (centroBean.getCpcentro() != null) {
                statement.setString(12, centroBean.getCpcentro());
            } else {
                statement.setNull(12, Types.CHAR);
            }
            //          ,   TELEPREV   ,    TIPOCENTRO , NIVATENCION  ) "
            if (centroBean.getTeleprev() != null) {
                statement.setString(13, centroBean.getTeleprev());
            } else {
                statement.setNull(13, Types.CHAR);
            }
            if (centroBean.getTipocentro() != null && centroBean.getTipocentro().getId() != null) {
                statement.setLong(14, centroBean.getTipocentro().getId());
            } else {
                statement.setNull(14, Types.INTEGER);
            }
            if (centroBean.getNivatencion() != null && centroBean.getNivatencion().getId() != null) {
                statement.setLong(15, centroBean.getNivatencion().getId());
            } else {
                statement.setNull(15, Types.INTEGER);
            }
            if (centroBean.getEstado() != null) {
                statement.setInt(16, centroBean.getEstado());
            } else {
                statement.setNull(16, Types.INTEGER);
            }
            if (centroBean.getMapgoogle() != null) {
                statement.setString(17, centroBean.getMapgoogle());
            } else {
                statement.setNull(17, Types.CHAR);
            }
            if (centroBean.getNomcorto() != null) {
                statement.setString(18, centroBean.getNomcorto());
            } else {
                statement.setNull(18, Types.CHAR);
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
    public boolean doActualizaDatos(CentroBean centroBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   CENTROS "
                    + " SET CODAUTO=?,CODGEREN=?, CODZONA=?,CODIGO=?,NOMCEN=?"
                    + " ,TIPOVIA=?   "
                    + " ,CALLECEN=?  ,   NUMCALCEN=?  ,OTRDIRCEN=?  ,   CODLOCAL=? "
                    + " ,   CPCENTRO =?  ,   TELEPREV=?   ,    TIPOCENTRO=? , NIVATENCION =? "
                    + ",ESTADO=?,MAPGOOGLE=?,NOMCORTO=?"
                    + " WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (centroBean.getAutonomia() != null && centroBean.getAutonomia().getCodigo() != null) {
                statement.setString(1, centroBean.getAutonomia().getCodigo());
            } else {
                statement.setNull(1, Types.CHAR);
            }
            if (centroBean.getGerencia() != null && centroBean.getGerencia().getCodigo() != null) {
                statement.setString(2, centroBean.getGerencia().getCodigo());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (centroBean.getZona() != null && centroBean.getZona().getCodigo() != null) {
                statement.setString(3, centroBean.getZona().getCodigo());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (centroBean.getCodigo() != null) {
                statement.setString(4, centroBean.getCodigo());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (centroBean.getNomcen() != null) {
                statement.setString(5, centroBean.getNomcen());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            // ,TIPOVIA   ,CALLECEN  ,   NUMCALCEN  ,OTRDIRCEN  ,   CODLOCAL   ,   CPCENTRO
            if (centroBean.getTipovia() != null) {
                statement.setString(6, centroBean.getTipovia());
            } else {
                statement.setNull(6, Types.CHAR);
            }
            if (centroBean.getCallecen() != null) {
                statement.setString(7, centroBean.getCallecen());
            } else {
                statement.setNull(7, Types.CHAR);
            }
            if (centroBean.getNumcalcen() != null) {
                statement.setLong(8, centroBean.getNumcalcen());
            } else {
                statement.setNull(8, Types.INTEGER);
            }
            if (centroBean.getOtrdirecen() != null) {
                statement.setString(9, centroBean.getOtrdirecen());
            } else {
                statement.setNull(9, Types.CHAR);
            }
            if (centroBean.getLocalidad() != null && centroBean.getLocalidad().getCodigo() != null) {
                statement.setString(10, centroBean.getLocalidad().getCodigo());
            } else {
                statement.setNull(10, Types.CHAR);
            }
            if (centroBean.getCpcentro() != null) {
                statement.setString(11, centroBean.getCpcentro());
            } else {
                statement.setNull(11, Types.CHAR);
            }
            //          ,   TELEPREV   ,    TIPOCENTRO , NIVATENCION  ) "
            if (centroBean.getTeleprev() != null) {
                statement.setString(12, centroBean.getTeleprev());
            } else {
                statement.setNull(12, Types.CHAR);
            }

            if (centroBean.getTipocentro() != null && centroBean.getTipocentro().getId() != null) {
                statement.setLong(13, centroBean.getTipocentro().getId());
            } else {
                statement.setNull(13, Types.INTEGER);
            }
            if (centroBean.getNivatencion() != null && centroBean.getNivatencion().getId() != null) {
                statement.setLong(14, centroBean.getNivatencion().getId());
            } else {
                statement.setNull(14, Types.INTEGER);
            }
            if (centroBean.getEstado() != null) {
                statement.setInt(15, centroBean.getEstado());
            } else {
                statement.setNull(15, Types.INTEGER);
            }
            if (centroBean.getMapgoogle() != null) {
                statement.setString(16, centroBean.getMapgoogle());
            } else {
                statement.setNull(16, Types.CHAR);
            }
            if (centroBean.getNomcorto() != null) {
                statement.setString(17, centroBean.getNomcorto());
            } else {
                statement.setNull(17, Types.CHAR);
            }

            statement.setLong(18, centroBean.getId());

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
    public boolean doBorraDatos(CentroBean centroBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  CENTROS WHERE ID='" + centroBean.getId() + "'";
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

    @Override
    public ArrayList<CentroBean> getLista(String cadena) {
        return getLista(cadena, null, null, null, null, null, null, null);
    }

    /**
     *
     * @param texto
     * @param autonomiaBean
     * @param provinciaBean
     * @param localidadBean
     * @param nivelesAtencionBean
     * @return
     */
    public ArrayList<CentroBean> getLista(String texto, AutonomiaBean autonomiaBean, ProvinciaBean provinciaBean, LocalidadBean localidadBean,
            NivelesAtencionBean nivelesAtencionBean, CentroTipoBean centroTipoBean, NivelesAtentionTipoBean nivelesAtentionTipoBean, Integer estado) {
        Connection connection = null;
        ArrayList<CentroBean> listaCentroTBeans = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();

            if (autonomiaBean != null && autonomiaBean.getCodigo() != null && !autonomiaBean.getCodigo().isEmpty()) {
                sql = sql.concat(" AND a.codigo='" + autonomiaBean.getCodigo() + "'");
            }
            if (provinciaBean != null && provinciaBean.getCodigo() != null && !provinciaBean.getCodigo().isEmpty()) {
                sql = sql.concat(" AND p.codigo='" + provinciaBean.getCodigo() + "'");
            }
            if (localidadBean != null && localidadBean.getCodigo() != null && !localidadBean.getCodigo().isEmpty()) {
                sql = sql.concat(" AND c.codlocal='" + localidadBean.getCodigo() + "'");
            }
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(c.NOMCEN) like'%" + texto.toUpperCase() + "%'  OR   UPPER(codigo) like'%" + texto.toUpperCase() + "%' )");
            }
            if (centroTipoBean != null) {
                sql = sql.concat(" AND tipocentro=" + centroTipoBean.getId());
            }
            if (nivelesAtencionBean != null) {
                sql = sql.concat(" AND  n.id=" + nivelesAtencionBean.getId());
            }
            if (nivelesAtentionTipoBean != null) {
                sql = sql.concat(" AND  n.tipo=" + nivelesAtentionTipoBean.getId());
            }
            if (estado != null) {
                sql = sql.concat(" AND c.estado=" + estado);
            }
            sql = sql.concat(" ORDER BY c.nomcen  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                listaCentroTBeans.add(getRegistroResulset(resulSet, autonomiaBean, localidadBean));
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
        return listaCentroTBeans;
    }

}
