package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.GerenciaBean;
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
 * @author juannietopajares
 */
public class GerenciaDao extends ConexionDao implements Serializable, ConexionInterface<GerenciaBean> {

    private static final Logger LOGGER = LogManager.getLogger(GerenciaDao.class);
    private static final long serialVersionUID = 1L;

    /*
           codauto ,codigo, nombre , tipovia, callesec ,numcalsec, otrdomger, cpger , localger
     */
    public GerenciaDao() {
        super();
        sql = " SELECT g.codauto as gerenciacodauto ,g.codigo as  gerenciacodigo , g.nombre as  gerencianombre"
                + " , g.tipovia  as   gerenciatipovia, g.callesec as   gerenciacallesec,g.numcalsec  as gerencianumcalsec"
                + ", g.otrdomger as  gerenciaotrdomger, g.cpger  as gerenciacpger, g.localger as  gerencialocalger, g.estado gerenciaestado "
                + " ,a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado "
                + " , l.codigo as localidadcodigo,l.nombre as localidadnombre,l.codprov as localidadcodpro "
                + ",p. codigo as provinciacodigo, p.nombre as provincianombre,p.codauto as provinciacodauto  "
                + " FROM GERENCIA  g "
                + "  JOIN CAUTONOM a ON a.codigo = g.codauto  "
                + "  JOIN localidad l ON l.codigo=g.localger "
                + " JOIN provincia p ON p.codigo=l.codprov "
                + " WHERE  1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public GerenciaBean getRegistroResulset(ResultSet rs) {
        GerenciaBean gerenciaBean = new GerenciaBean();
        try {
            // gerenciaBean.setCodauto(rs.getString("gerenciacodauto"));
            gerenciaBean.setAutonomia(new AutonomiaDao().getRegistroResulset(rs));
            gerenciaBean.setProvincia(ProvinciaDao.getRegistroResulset(rs, gerenciaBean.getAutonomia()));
            gerenciaBean.setLocalidad(LocalidadDao.getRegistroResulset(rs, gerenciaBean.getProvincia()));
            gerenciaBean.setCodigo(rs.getString("gerenciacodigo"));
            gerenciaBean.setNombre(rs.getString("gerencianombre"));
            gerenciaBean.setTipovia(rs.getString("gerenciatipovia"));
            gerenciaBean.setCallesec(rs.getString("gerenciacallesec"));
            gerenciaBean.setNumcalsec(rs.getInt("gerencianumcalsec"));
            gerenciaBean.setOtrdomger(rs.getString("gerenciaotrdomger"));
            gerenciaBean.setCpger(rs.getString("gerenciacpger"));
            gerenciaBean.setEstado(rs.getInt("gerenciaestado"));

            // gerenciaBean.setNombre(rs.getString("gerencialocalger").trim());
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return gerenciaBean;
    }

    public GerenciaBean getGerenciaDefecto() {
        return getPorCodigo("17", "01");
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public GerenciaBean getPorId(Long id) {
        return null;
    }

    /**
     *
     * @param codigo
     * @return
     */
    @Override
    public GerenciaBean getPorCodigo(String codigo) {
        return getPorCodigo(null, codigo);
    }

    /**
     *
     * @param codauto
     * @param codigo
     * @return
     */
    public GerenciaBean getPorCodigo(String codauto, String codigo) {
        Connection connection = null;
        GerenciaBean gerenciaBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND   g.codauto='" + codauto + "' AND  g.codigo='" + codigo + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    gerenciaBean = getRegistroResulset(resulSet);
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
        return gerenciaBean;
    }

    @Override
    public boolean doGrabaDatos(GerenciaBean gerenciaBean) {
        boolean actualizado = false;
        if (this.getPorCodigo(gerenciaBean.getAutonomia().getCodigo(), gerenciaBean.getCodigo()) == null) {
            actualizado = this.doInsertaDatos(gerenciaBean);
        } else {
            actualizado = this.doActualizaDatos(gerenciaBean);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(GerenciaBean gerenciaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  GERENCIA  ( codauto ,codigo, nombre , tipovia, callesec ,numcalsec, otrdomger"
                    + ", cpger , localger, estado) " + " VALUES "
                    + "(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (gerenciaBean.getAutonomia() != null && gerenciaBean.getAutonomia().getCodigo() != null) {
                statement.setString(1, gerenciaBean.getAutonomia().getCodigo());
            } else {
                statement.setNull(1, Types.CHAR);
            }

            statement.setString(2, gerenciaBean.getCodigo());

            if (gerenciaBean.getNombre() != null) {
                statement.setString(3, gerenciaBean.getNombre());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (gerenciaBean.getTipovia() != null) {
                statement.setString(4, gerenciaBean.getTipovia());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (gerenciaBean.getCallesec() != null) {
                statement.setString(5, gerenciaBean.getCallesec());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            //   numcalsec, otrdomger, cpger , localge
            if (gerenciaBean.getNumcalsec() != null) {
                statement.setLong(6, gerenciaBean.getNumcalsec());
            } else {
                statement.setNull(6, Types.INTEGER);
            }
            if (gerenciaBean.getOtrdomger() != null) {
                statement.setString(7, gerenciaBean.getOtrdomger());
            } else {
                statement.setNull(7, Types.CHAR);
            }
            if (gerenciaBean.getCpger() != null) {
                statement.setString(8, gerenciaBean.getCpger());
            } else {
                statement.setNull(8, Types.CHAR);
            }
            if (gerenciaBean.getLocalidad() != null && gerenciaBean.getLocalidad().getCodigo() != null) {
                statement.setString(9, gerenciaBean.getLocalidad().getCodigo());
            } else {
                statement.setNull(9, Types.CHAR);
            }
            statement.setInt(10, gerenciaBean.getEstado());

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
     * @param gerenciaBean
     * @return
     */
    @Override
    public boolean doActualizaDatos(GerenciaBean gerenciaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   GERENCIA  SET  "
                    + "  nombre=? , tipovia=?, callesec=? "
                    + " ,numcalsec=?, otrdomger=?, cpger=? , localger=? , estado=?"
                    + " WHERE codauto=? AND codigo=?  ";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (gerenciaBean.getNombre() != null) {
                statement.setString(1, gerenciaBean.getNombre());
            } else {
                statement.setNull(1, Types.CHAR);
            }
            if (gerenciaBean.getTipovia() != null) {
                statement.setString(2, gerenciaBean.getTipovia());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (gerenciaBean.getCallesec() != null) {
                statement.setString(3, gerenciaBean.getCallesec());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            //   numcalsec, otrdomger, cpger , localge
            if (gerenciaBean.getNumcalsec() != null) {
                statement.setLong(4, gerenciaBean.getNumcalsec());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            if (gerenciaBean.getOtrdomger() != null) {
                statement.setString(5, gerenciaBean.getOtrdomger());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            if (gerenciaBean.getCpger() != null) {
                statement.setString(6, gerenciaBean.getCpger());
            } else {
                statement.setNull(6, Types.CHAR);
            }
            if (gerenciaBean.getLocalidad() != null && gerenciaBean.getLocalidad().getCodigo() != null) {
                statement.setString(7, gerenciaBean.getLocalidad().getCodigo());
            } else {
                statement.setNull(7, Types.CHAR);
            }

            statement.setInt(8, gerenciaBean.getEstado());

            if (gerenciaBean.getAutonomia() != null && gerenciaBean.getAutonomia().getCodigo() != null) {
                statement.setString(9, gerenciaBean.getAutonomia().getCodigo());
            } else {
                statement.setNull(9, Types.CHAR);
            }

            statement.setString(10, gerenciaBean.getCodigo());

            insertadoBoolean = statement.executeUpdate() > 0;
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
     * @param gerenciaBean
     * @return
     */
    @Override
    public boolean doBorraDatos(GerenciaBean gerenciaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   GERENCIA  SET estado=" + ConexionDao.BBDD_ACTIVONO + " WHERE  codauto='" + gerenciaBean.getAutonomia().getCodigo() + "'  AND "
                    + "codigo='" + gerenciaBean.getCodigo() + "'";
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
    public ArrayList<GerenciaBean> getLista(String texto) {
        return getLista(texto, null, null, null);
    }

    /**
     *
     * @param texto
     * @param autonomiaBean
     * @return
     */
    public ArrayList<GerenciaBean> getLista(String texto, AutonomiaBean autonomiaBean, ProvinciaBean provinciaBean, Integer estado) {
        Connection connection = null;
        ArrayList<GerenciaBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (estado != null) {
                sql = sql.concat(" AND g.estado=" + estado);
            }
            if (autonomiaBean != null) {
                sql = sql.concat(" AND g.codauto='" + autonomiaBean.getCodigo() + "'");
            }
            // El campo provincia no es de la tabla gerencia por eso filtra con localidad.codprov
            if (provinciaBean != null) {
                sql = sql.concat(" AND l.codprov='" + provinciaBean.getCodigo() + "'");
            }
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(g.nombre) like'%" + texto.toUpperCase() + "%'  OR   UPPER(g.codigo) like'%" + texto.toUpperCase() + "%' )");
            }

            sql = sql.concat(" ORDER BY g.nombre  ");
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
