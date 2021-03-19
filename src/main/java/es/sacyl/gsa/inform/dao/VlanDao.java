package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.VlanBean;
import es.sacyl.gsa.inform.ctrl.VlanCtrl;
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
public class VlanDao extends ConexionDao implements Serializable, ConexionInterface<VlanBean> {

    private static final Logger LOGGER = LogManager.getLogger(VlanDao.class);
    private static final long serialVersionUID = 1L;

    // id,direccion,nombre,puertaenlace,estado,usucambio ,fechacambio
    public VlanDao() {
        super();
        sql = " SELECT vlan.id as vlanid,vlan.direccion as vlandireccion,vlan.nombre as vlannombre,vlan.puertaenlace as vlanpuertaenlace"
                + ",vlan.estado  as vlanestado,vlan.usucambio as vlanusucambio ,vlan.fechacambio  as vlanfechacmabio"
                + ", vlan.comentario as vlancomentario "
                + " , usu.id as usuarioid,usu.dni as usuariodni,usu.apellido1 as usuarioapellido1"
                + ",usu.apellido2 as usuarioapellido2,usu.nombre as usuarionombre"
                + ",usu.estado as usuarioestado,usu.usucambio as usuariousucambio"
                + ",usu.fechacambio as usuariofechacambio,usu.mail as usuariomail"
                + ",usu.telefono as usuariotelefon,usu.idgfh as usuarioidgfh"
                + ",usu.idcategoria as usuarioidcategoria"
                + " FROM vlan  "
                + " LEFT JOIN  usuarios usu ON usu.id=vlan.usucambio "
                + " WHERE  1=1 ";
    }

    @Override
    public VlanBean getRegistroResulset(ResultSet rs) {
        VlanBean vlanBean = new VlanBean();
        try {
            vlanBean.setId(rs.getLong("vlanid"));
            vlanBean.setDireccion(rs.getString("vlandireccion"));
            vlanBean.setNombre(rs.getString("vlannombre"));
            vlanBean.setPuertaenlace(rs.getString("vlanpuertaenlace"));
            vlanBean.setEstado(rs.getInt("vlanestado"));
            // vlanBean.setUsucambio(new UsuarioDao().getPorId(rs.getLong("vlanusucambio")));
            vlanBean.setUsucambio(new UsuarioDao().getRegistroResulset(rs));
            vlanBean.setFechacambio(Utilidades.getFechaLocalDate(rs.getLong("vlanfechacmabio")));
            vlanBean.setComentario(rs.getString("vlancomentario"));
            vlanBean.setMascara(VlanCtrl.getCalculaMascara(vlanBean.getDireccion()));
            String[] valores = vlanBean.getDireccion().split("/");
            vlanBean.setPuertaenlace(VlanCtrl.getCalculaPuertaEnlace(valores[0], vlanBean.getMascara()));
            vlanBean.setBroadcast(VlanCtrl.getCalculaBroadcast(vlanBean.getDireccion()));
            vlanBean.setUltimaIp(VlanCtrl.getCalculaUltimaIp(vlanBean.getDireccion()));

            vlanBean.setNumeroDirecciones(VlanCtrl.getCalculaNumeroDirecciones(valores[1]));

        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return vlanBean;
    }

    @Override
    public VlanBean getPorCodigo(String codigo) {
        return null;
    }

    @Override
    public VlanBean getPorId(Long id) {
        Connection connection = null;
        VlanBean vlanBean = null;
        if (id != null) {
            try {
                connection = super.getConexionBBDD();
                sql = sql.concat(" AND id='" + id + "'");
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        vlanBean = getRegistroResulset(resulSet);
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
        return vlanBean;
    }

    @Override
    public boolean doGrabaDatos(VlanBean vlanBean) {
        boolean grabado = false;
        if (this.getPorId(vlanBean.getId()) == null) {
            vlanBean.setId(getSiguienteId("vlan"));
            grabado = this.doInsertaDatos(vlanBean);
        } else {
            grabado = this.doActualizaDatos(vlanBean);
        }
        return grabado;
    }

    @Override
    public boolean doInsertaDatos(VlanBean vlanBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " INSERT INTO  vlan   (id,direccion,nombre,puertaenlace,estado,usucambio ,fechacambio,comentario) "
                + " VALUES (?,?,?,?,?,?,?,?)  ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, vlanBean.getId());
            statement.setString(2, vlanBean.getDireccion());
            statement.setString(3, vlanBean.getNombre());
            statement.setString(4, vlanBean.getPuertaenlace());
            statement.setInt(5, vlanBean.getEstado());
            if (vlanBean.getUsucambio() != null && vlanBean.getUsucambio().getId() != null) {
                statement.setLong(6, vlanBean.getUsucambio().getId());
            } else {
                statement.setNull(6, Types.CHAR);
            }
            statement.setLong(7, Utilidades.getFechaLong(vlanBean.getFechacambio()));

            if (vlanBean.getComentario() != null) {
                statement.setString(8, vlanBean.getComentario());
            } else {
                statement.setNull(8, Types.CHAR);
            }

            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException ex) {
            LOGGER.error(sql + Utilidades.getStackTrace(ex));
        }
        return insertadoBoolean;
    }

    public boolean doActualizaDatos(VlanBean vlanBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " UPDATE   vlan  SET direccion=?,nombre=?,puertaenlace=?,estado=?,usucambio=? ,fechacambio=? "
                + " , comentario=? "
                + " WHERE id=?  ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, vlanBean.getDireccion());
            statement.setString(2, vlanBean.getNombre());
            statement.setString(3, vlanBean.getPuertaenlace());
            statement.setInt(4, vlanBean.getEstado());
            if (vlanBean.getUsucambio() != null && vlanBean.getUsucambio().getId() != null) {
                statement.setLong(5, vlanBean.getUsucambio().getId());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            statement.setLong(6, Utilidades.getFechaLong(vlanBean.getFechacambio()));
            if (vlanBean.getComentario() != null) {
                statement.setString(7, vlanBean.getComentario());
            } else {
                statement.setNull(7, Types.CHAR);
            }
            statement.setLong(8, vlanBean.getId());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException ex) {
            LOGGER.error(sql + Utilidades.getStackTrace(ex));
        }
        return insertadoBoolean;
    }

    @Override
    public boolean doBorraDatos(VlanBean vlanBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   vlan  SET estado=?,usucambio=? ,fechacambio=? "
                    + " WHERE id=?  ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, vlanBean.getEstado());
            if (vlanBean.getUsucambio() != null && vlanBean.getUsucambio().getId() != null) {
                statement.setLong(2, vlanBean.getUsucambio().getId());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            statement.setLong(3, Utilidades.getFechaLong(vlanBean.getFechacambio()));
            statement.setLong(4, vlanBean.getId());
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
    public ArrayList<VlanBean> getLista(String texto) {
        return getLista(texto, null);
    }

    public ArrayList<VlanBean> getLista(String texto, Integer estado) {
        Connection connection = null;
        ArrayList<VlanBean> listautonomias = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (estado != null) {
                sql = sql.concat(" AND vlan.estado=" + estado);
            }
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND   UPPER(vlan.nombre) like'%" + texto.toUpperCase() + "%' ");
            }
            sql = sql.concat(" ORDER BY vlan.nombre  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                listautonomias.add(getRegistroResulset(resulSet));
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
        return listautonomias;
    }
}
