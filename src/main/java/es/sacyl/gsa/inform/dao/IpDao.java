package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.IpBean;
import es.sacyl.gsa.inform.bean.VlanBean;
import es.sacyl.gsa.inform.ctrl.IpCtrl;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
                + ", e.mac as equipomac"
                + " ,e.estado as equipoestado,e.fechacambio as equipofechacambio, e.usucambio as equipousucambio "
                + " ,gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasisencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado,gfh.gfhpersigo"
                + " ,u.id as ubicacionesid , u.centro as ubicacionescentro, u.descripcion as ubicacionesdescripcion,"
                + " u.idpadre ubicacionesidpadre, u.nivel  as ubicacionesnivel  "
                + " , usu.id as usuarioid,usu.dni as usuariodni,usu.apellido1 as usuarioapellido1"
                + ",usu.apellido2 as usuarioapellido2,usu.nombre as usuarionombre"
                + ",usu.estado as usuarioestado,usu.usucambio as usuariousucambio"
                + ",usu.fechacambio as usuariofechacambio,usu.mail as usuariomail"
                + ",usu.telefono as usuariotelefon,usu.idgfh as usuarioidgfh"
                + ",usu.idcategoria as usuarioidcategoria,usu.movil as usuariomovil"
                + ",usu.mailprivado as usuariomailprivado,usu.telegram as usuariotegegram"
                + ",usu.solicita as usuariosolicita"
                + ",uc.id as usuarioscategoriaid, uc.CODIGOPERSIGO as usuarioscategoriacodigo"
                + ",uc.nombre as usuarioscategoriaanombre,uc.estado as usuarioscategoriaestado  "
                + ",gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasistencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado,gfh.gfhpersigo, gfh.division  as gfhdivision"
                + " FROM ips   ip "
                + " JOIN vlan ON vlan.id=ip.vlan "
                + " LEFT JOIN equipos e ON e.id=ip.equipo "
                + " LEFT JOIN  gfh  ON gfh.id=e.servicio "
                + " LEFT JOIN ubicaciones u ON u.id=e.ubicacion"
                + " LEFT JOIN  usuarios usu ON usu.id=ip.usucambio "
                + " LEFT JOIN categorias uc ON uc.id=usu.idcategoria "
                + " LEFT JOIN gfh gfh ON usu.idgfh = gfh.id"
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
            ipBean.setNumeroIp(IpCtrl.getValorNumerico(ipBean.getIp()));
            ipBean.setEstado(rs.getInt("ipestado"));
            if (equipoBean == null) {
                if (rs.getLong("ipequipo") != 0) {
                    ipBean.setEquipo(new EquipoDao().getPorId(rs.getLong("ipequipo")));
                }
                //ipBean.setEquipo(new EquipoDao().getRegistroResulset(rs, false, false));
            } else {
                ipBean.setEquipo(equipoBean);
            }
            if (vlanBean == null) {
                ipBean.setVlan(new VlanDao().getRegistroResulset(rs));
            } else {
                ipBean.setVlan(vlanBean);
            }
            ipBean.setFechacambio(Utilidades.getFechaLocalDate(rs.getLong("ipfechacambio")));
            // ipBean.setUsucambio(new UsuarioDao().getPorId(rs.getLong("ipusucambio")));
            ipBean.setUsucambio(new UsuarioDao().getRegistroResulset(rs));

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
        }
        return ipBean;
    }

    @Override
    public IpBean getPorCodigo(String ip) {
        Connection connection = null;
        IpBean ipBean = null;

        try {
            connection = super.getConexionBBDD();

            sql = sql.concat(" AND ip.ip='" + ip + "'");

            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    ipBean = getRegistroResulset(resulSet);
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

        return ipBean;
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

            if (ipBean.getUsucambio() != null && ipBean.getUsucambio().getId() != null) {
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

    /**
     *
     * @param equipoBean
     * @return Para el equipo dado libera todas sus ips
     */
    public boolean doLiberaIpsEquipo(EquipoBean equipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE  ips SET equipo=NULL  WHERE equipo ='" + equipoBean.getId() + "'";
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
    public boolean doBorraDatos(IpBean ipBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE  ips SET estado=" + ConexionDao.BBDD_ACTIVONO + "WHERE id='" + ipBean.getId() + "'";
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

    /**
     *
     * @param texto
     * @param vlanBean
     * @param equipoBean
     * @param estado
     * @param libres
     * @return
     */
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
            // ordenar por numero
            Collections.sort(lista, new Comparator<IpBean>() {
                @Override
                public int compare(IpBean p2, IpBean p1) {
                    // Aqui esta el truco, ahora comparamos p2 con p1 y no al reves como antes
                    return new Long(p2.getNumeroIp()).compareTo(new Long(p1.getNumeroIp()));
                }
            });
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
     * @param vlanBean
     * @param equipoBean
     * @param estado
     * @param libres
     * @return
     */
    public HashMap<Long, IpBean> getListaMap(String texto, VlanBean vlanBean, EquipoBean equipoBean, Integer estado, String libres) {
        Connection connection = null;
        HashMap<Long, IpBean> lista = new HashMap<>();
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
                IpBean ip = getRegistroResulset(resulSet, vlanBean, equipoBean);
                Long key = IpCtrl.getValorNumerico(ip.getIp());
                lista.put(key, ip);
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
