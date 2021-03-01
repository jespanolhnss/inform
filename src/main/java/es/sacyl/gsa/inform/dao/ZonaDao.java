package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.GerenciaBean;
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
public class ZonaDao extends ConexionDao implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(ZonaDao.class);
    private static final long serialVersionUID = 1L;

    public ZonaDao() {
        super();
        sql = " SELECT z.codauto  zonacodauto  ,z.codgeren  zonacodgeren,z.codigo  zonacodigo"
                + ",z.nombre  zonanombre,z.codprov zonacodprov"
                + ",a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado  "
                + ",p. codigo as provinciacodigo, p.nombre as provincianombre, p.codauto as provinciacodauto "
                + " , g.codauto gerenciacodauto ,g.codigo   gerenciacodigo , g.nombre  gerencianombre "
                + " , g.tipovia   gerenciatipovia, g.callesec   gerenciacallesec,g.numcalsec  gerencianumcalsec"
                + ", g.estado as gerenciaestado"
                + ", g.otrdomger as  gerenciaotrdomger, g.cpger  as  gerenciacpger, g.localger  as  gerencialocalger "
                +" , l.codigo as localidadcodigo,l.nombre as localidadnombre,l.codprov as localidadcodpro "
                + " FROM ZONAS z "
                + " JOIN CAUTONOM a  ON z.codauto= a.codigo "
                + " JOIN PROVINCIA  p ON  p.codigo=z.codprov "
                + " JOIN  GERENCIA  g On g.codigo=z.codgeren "
                  + "  JOIN localidad l  ON l.codigo=g.LOCALGER "
                + " WHERE  1=1  ";
    }

    public static ZonaBean getRegistroResulset(ResultSet rs, AutonomiaBean autonomiaBean, GerenciaBean gerenciaBean, ProvinciaBean provinciaBean) {
        ZonaBean zonaBean = new ZonaBean();
        try {
            // codauto,codgeren,codigo,nombre,codprov
            if (autonomiaBean == null) {
                zonaBean.setAutonomia(new AutonomiaDao().getRegistroResulset(rs));
            } else {
                zonaBean.setAutonomia(autonomiaBean);
            }
            if (gerenciaBean == null) {
                zonaBean.setGerencia(new GerenciaDao().getRegistroResulset(rs));
            } else {
                zonaBean.setGerencia(gerenciaBean);
            }
            zonaBean.setCodigo(rs.getString("zonacodigo"));
            zonaBean.setNombre(rs.getString("zonanombre"));
            if (provinciaBean == null) {
                zonaBean.setProvincia(ProvinciaDao.getRegistroResulset(rs, zonaBean.getAutonomia()));
            } else {
                zonaBean.setProvincia(provinciaBean);
            }
            //codauto,codgeren,codigo,nombre,codprov

        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return zonaBean;
    }

    public ZonaBean getPorCodigo(String codauto, String codgeren, String codigo) {
        Connection connection = null;
        ZonaBean zonaBean = null;
        // codauto,codgeren,codigo,nombre,codprov
        try {
            connection = super.getConexionBBDD();
            if (codauto != null) {
                sql = sql.concat(" AND z.codauto='" + codauto + "'");
            }
            if (codgeren != null) {
                sql = sql.concat(" AND z.codgeren='" + codauto + "'");
            }
            if (codigo != null) {
                sql = sql.concat(" AND z.codigo='" + codigo + "'");
            }

            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    zonaBean = getRegistroResulset(resulSet, null, null, null);
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
        return zonaBean;
    }

    public boolean doGrabaDatos(ZonaBean zonaBean) {
        boolean actualizado = false;

        if (this.getPorCodigo(zonaBean.getAutonomia().getCodigo(), zonaBean.getGerencia().getCodigo(), zonaBean.getCodigo()) == null) {
            actualizado = this.doInsertaDatos(zonaBean);
        } else {
            actualizado = this.doActualizaDatos(zonaBean);
        }
        return actualizado;
    }

    public boolean doInsertaDatos(ZonaBean zonaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  ZONAS  ( "
                    + "codauto,codgeren,codigo,nombre,codprov) " + " VALUES "
                    + "(?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, zonaBean.getAutonomia().getCodigo());
            statement.setString(2, zonaBean.getGerencia().getCodigo());
            statement.setString(3, zonaBean.getCodigo());

            if (zonaBean.getNombre() != null) {
                statement.setString(4, zonaBean.getNombre());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (zonaBean.getProvincia() != null && zonaBean.getProvincia().getCodigo() != null) {
                statement.setString(4, zonaBean.getProvincia().getCodigo());
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

    public boolean doActualizaDatos(ZonaBean zonaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   ZONAS "
                    + "nombre=?,codprov=?  "
                    + " WHERE codauto=?,codgeren=?,codigo=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (zonaBean.getNombre() != null) {
                statement.setString(1, zonaBean.getNombre());
            } else {
                statement.setNull(1, Types.CHAR);
            }
            if (zonaBean.getProvincia() != null && zonaBean.getProvincia().getCodigo() != null) {
                statement.setString(2, zonaBean.getProvincia().getCodigo());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            statement.setString(3, zonaBean.getAutonomia().getCodigo());
            statement.setString(4, zonaBean.getGerencia().getCodigo());
            statement.setString(5, zonaBean.getCodigo());
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

    public boolean doBorraDatos(ZonaBean zonaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  ZONAS WHERE "
                    + " codauto='" + zonaBean.getAutonomia().getCodigo() + "'"
                    + " AND codgeren='" + zonaBean.getGerencia().getCodigo() + "'"
                    + " AND codigo='" + zonaBean.getCodigo() + "'";
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
     * @param AutonomiaBean
     * @param provinciaBean
     * @param GerenciaBean
     * @param texto
     * @return
     */
    public ArrayList<ZonaBean> getLista(AutonomiaBean autonomiaBean, ProvinciaBean provinciaBean, GerenciaBean gerenciaBean, String texto) {
        Connection connection = null;
        ArrayList<ZonaBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();

            if (autonomiaBean != null) {
                sql = sql.concat(" AND z.codauto=" + autonomiaBean.getCodigo());
            }
            if (provinciaBean != null) {
                sql = sql.concat(" AND z.codprov=" + provinciaBean.getCodigo());
            }
            if (gerenciaBean != null) {
                sql = sql.concat(" AND z.codgeren=" + gerenciaBean.getCodigo());
            }

            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(z.nombre) like'%" + texto.toUpperCase() + "%'  OR   UPPER(z.codigo) like'%" + texto.toUpperCase() + "%' )");
            }

            sql = sql.concat(" ORDER BY z.nombre  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, null, null, null));
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
