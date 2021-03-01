package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.IpBean;
import es.sacyl.gsa.inform.bean.VlanBean;
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
public class IpDao extends ConexionDao implements Serializable, ConexionInterface<IpBean> {

    private static final Logger LOGGER = LogManager.getLogger(IpDao.class);
    private static final long serialVersionUID = 1L;

    public IpDao() {
        super();
        sql = " SELECT ip.id as ipid, ip.ip as ipip, ip.equipo as ipequipo,ip.estado as ipestado"
                + " ,ip.usucambio as ipusucambio, ip.fechacambio as ipfechacambio,ip.vlan as ipvlan  "
                + " ,vlan.id as vlanid,vlan.direccion as vlandireccion,vlan.nombre as vlannombre,vlan.puertaenlace as vlanpuertaenlace"
                + " ,vlan.estado  as vlanestado,vlan.usucambio as vlanusucambio ,vlan.fechacambio  as vlanfechacmabio"
                + " ,vlan.comentario as vlancomentario " + " ,e.id as equipoid,e.tipo as equipotipo, e.inventario as  equipoinventario,e.marca as   equipomarca"
                + " ,e.modelo as  equipomodelo"
                + " ,e.numeroserie as   equiponumeroserie, e.centro   equipocentro,  e.ubicacion  equipoubicacion  "
                + " ,e.servicio as  equiposervicio, e.ip as  equipoip, e.comentario as  equipocomentario "
                + " ,gfh.id as servicioid,gfh.codigo as serviciocodigo,gfh.descripcion as serviciodescripcion "
                + " ,gfh.asistencial as servicioasisencial,gfh.idjimena  as servicioidjimena, gfh.estado as servicioestado "
                + " FROM ips   ip "
                + " JOIN vlan ON vlan.id=ip.vlan "
                + " LEFT JOIN equipos e ON e.id=ip.equipo "
                + " LEFT JOIN  gfh  ON gfh.id=e.servicio "
                + " WHERE  1=1 ";

    }

    @Override
    public IpBean getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null, null);
    }

    public IpBean getRegistroResulset(ResultSet rs, VlanBean vlanBean, EquipoBean equipoBean) {
        IpBean ipBean = new IpBean();
        try {
            ipBean.setId(rs.getLong("ipid"));
            ipBean.setIp(rs.getString("ipip").trim());
            ipBean.setEstado(rs.getInt("ipestado"));
            if (equipoBean == null) {
                ipBean.setEquipo(new EquipoDao().getPorId(rs.getLong("ipequipo")));
            } else {
                ipBean.setEquipo(equipoBean);
            }
            if (vlanBean == null) {
                ipBean.setVlan(new VlanDao().getRegistroResulset(rs));
            } else {
                ipBean.setVlan(vlanBean);
            }
            ipBean.setFechacambio(Utilidades.getFechaLocalDate(rs.getLong("ipfechacambio")));
            ipBean.setUsucambio(new UsuarioDao().getPorId(rs.getLong("ipusucambio")));

        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return ipBean;
    }

    @Override
    public IpBean getPorId(Long id) {
        Connection connection = null;
        IpBean ipBean = null;
        if (id != null) {
            try {
                connection = super.getConexionBBDD();

                sql = sql.concat(" AND ip.id='" + id + "'");

                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        ipBean = getRegistroResulset(resulSet);
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
        }
        return ipBean;
    }

    @Override
    public IpBean getPorCodigo(String codigo) {
        return null;
    }

    @Override
    public boolean doGrabaDatos(IpBean ipBean) {
        boolean grabado = false;
        if (this.getPorId(ipBean.getId()) == null) {
            ipBean.setId(getSiguienteId("ips"));
            grabado = this.doInsertaDatos(ipBean);
        } else {
            grabado = this.doActualizaDatos(ipBean);
        }
        return grabado;
    }

    @Override
    public boolean doInsertaDatos(IpBean ipBean) {
        Connection connection = null;
        boolean insertado = false;

        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO   ips  (id, ip, vlan,equipo,estado,usucambio, fechacambio)"
                    + " VALUES (?,?,?,?,?,?,?)  ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, ipBean.getId());
            statement.setString(2, ipBean.getIp());
            statement.setLong(3, ipBean.getVlan().getId());
            if (ipBean.getEquipo() != null && ipBean.getEquipo().getId() != null) {
                statement.setLong(4, ipBean.getEquipo().getId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }

            statement.setInt(5, ipBean.getEstado());
            if (ipBean.getUsucambio() == null) {
                statement.setLong(6, ipBean.getUsucambio().getId());
            } else {
                statement.setNull(6, Types.INTEGER);
            }
            statement.setLong(7, Utilidades.getFechaLong(ipBean.getFechacambio()));
            insertado = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    @Override
    public boolean doActualizaDatos(IpBean ipBean) {
        Connection connection = null;
        boolean insertado = false;

        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE  ips  SET  ip=?, equipo=?,vlan=?,estado=?"
                    + ",usucambio=? , fechacambio=?"
                    + " WHERE id=?  ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, ipBean.getIp());
            statement.setLong(2, ipBean.getVlan().getId());
            if (ipBean.getEquipo() != null && ipBean.getEquipo().getId() != null) {
                statement.setLong(3, ipBean.getEquipo().getId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            statement.setInt(4, ipBean.getEstado());

            if (ipBean.getUsucambio() != null && ipBean.getUsucambio().getId() != null) {
                statement.setLong(5, ipBean.getUsucambio().getId());
            } else {
                statement.setNull(5, Types.INTEGER);
            }
            statement.setLong(6, Utilidades.getFechaLong(ipBean.getFechacambio()));

            statement.setLong(7, ipBean.getId());
            insertado = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    /**
     *
     * @param ipBean
     * @return
     */
    public boolean doActualizaEquipo(IpBean ipBean) {
        Connection connection = null;
        boolean insertado = false;

        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE  ips  SET   equipo=?,estado=?"
                    + ",usucambio=? , fechacambio=?"
                    + " WHERE id=?  ";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (ipBean.getEquipo() != null && ipBean.getEquipo().getId() != null) {
                statement.setLong(1, ipBean.getEquipo().getId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            statement.setInt(2, ipBean.getEstado());

            if (ipBean.getUsucambio() == null) {
                statement.setLong(3, ipBean.getUsucambio().getId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            statement.setLong(4, Utilidades.getFechaLong(ipBean.getFechacambio()));

            statement.setLong(5, ipBean.getId());
            insertado = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    public boolean doLiberaIpsEquipo(EquipoBean equipoBean) {
        Connection connection = null;
        boolean insertado = false;
        for (IpBean ipBean : equipoBean.getListaIps()) {
            ipBean.setEquipo(null);
            ipBean.setValoresAut();
            this.doActualizaEquipo(ipBean);
        }
        return insertado;
    }

    @Override
    public boolean doBorraDatos(IpBean ipBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE  ip SET estado=" + ConexionDao.BBDD_ACTIVONO + "WHERE id='" + ipBean.getId() + "'";
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
    public ArrayList<IpBean> getLista(String texto) {
        return getLista(texto, null, null, null, null);
    }

    public ArrayList<IpBean> getLista(String texto, VlanBean vlanBean, EquipoBean equipoBean, Integer estado, String libres) {
        Connection connection = null;
        ArrayList<IpBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (texto != null) {
                sql = sql.concat(" AND ip.ip LIKE '" + texto + "%'");
            }
            if (estado != null) {
                sql = sql.concat(" AND ip.estado=" + estado);
            }
            if (vlanBean != null) {
                sql = sql.concat(" AND ip.vlan=" + vlanBean.getId());
            }
            if (equipoBean != null) {
                sql = sql.concat(" AND ip.equipo=" + equipoBean.getId());
            }
            if (libres != null) {
                if (libres.equals(IpBean.IPLIBRESI)) {
                    sql = sql.concat(" AND ip.equipo IS NULL ");
                }
                if (libres.equals(IpBean.IPLIBRENO)) {
                    sql = sql.concat(" AND NOT ip.equipo IS NULL ");
                }
            }

            sql = sql.concat(" ORDER BY ip.ip  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, vlanBean, equipoBean));
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
