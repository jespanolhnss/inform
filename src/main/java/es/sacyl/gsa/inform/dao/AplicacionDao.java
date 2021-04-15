package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.ProveedorBean;
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
public class AplicacionDao extends ConexionDao implements Serializable, ConexionInterface<AplicacionBean> {

    private static final Logger LOGGER = LogManager.getLogger(AplicacionDao.class);
    private static final long serialVersionUID = 1L;

    public AplicacionDao() {
        super();
        sql = " SELECT ap.id as aplicacionid,ap.nombre as aplicacionnombre,ap.proveedor as aplicacionproveedor "
                + " ,ap.ambito as aplicacionambito, ap.gestionUsuarios as aplicaciongestionusuarios"
                + " ,ap. descripcion as aplicaciondescripcion,ap.gfh as aplicaciongfh"
                + " ,ap.fechaInstalacion aplicacionfechaInstalacion,   ap.estado as aplicacionestado"
                + " ,ap. usucambio as aplicacionusucambio,ap. fechacambio as aplicacionfechacambio "
                + " ,gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + " ,gfh.asistencial as gfhasisencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado"
                + " ,prvee.id as proveedorid, prvee.nombre as proveedornombre"
                + " ,prvee.direccion as proveedordirecion,prvee.codpostal as proveedorcodpostal"
                + " ,prvee.telefonos as proveedortelfoonos,prvee.mail as proveedormail"
                + " ,prvee.localidad as proveedorlocalidad,prvee.provincia  as proveedorprovincia"
                + " ,l.codigo as localidadcodigo,l.nombre as localidadnombre,l.codprov as localidadcodpro"
                + " ,p. codigo as provinciacodigo, p.nombre as provincianombre,p.codauto as provinciacodauto  "
                + " ,a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado   "
                + " ,usu.id as usuarioid,usu.dni as usuariodni,usu.apellido1 as usuarioapellido1"
                + ",usu.apellido2 as usuarioapellido2,usu.nombre as usuarionombre"
                + ",usu.estado as usuarioestado,usu.usucambio as usuariousucambio"
                + ",usu.fechacambio as usuariofechacambio,usu.mail as usuariomail"
                + ",usu.telefono as usuariotelefon,usu.idgfh as usuarioidgfh"
                + ",usu.idcategoria as usuarioidcategoria,usu.movil as usuariomovil"
                + ",usu.mailprivado as usuariomailprivado,usu.telegram as usuariotegegram"
                + ",usu.solicita as usuariosolicita"
                + ",uc.id as usuarioscategoriaid, uc.CODIGOPERSIGO as usuarioscategoriacodigo"
                + ",uc.nombre as usuarioscategoriaanombre,uc.estado as usuarioscategoriaestado  "
                + " FROM aplicaciones ap "
                + " LEFT  JOIN gfh  ON gfh.id=ap.gfh"
                + " LEFT  JOIN proveedores prvee ON  prvee.id=ap.proveedor"
                + " LEFT  JOIN localidad l ON l.codigo=prvee.localidad"
                + " LEFT  JOIN provincia p ON p.codigo=l.codprov "
                + " LEFT  JOIN CAUTONOM a ON a.codigo=p.CODAUTO  "
                + " LEFT JOIN usuarios usu ON usu.id=ap.usucambio"
                + " LEFT JOIN categorias uc ON uc.id=usu.idcategoria "
                + " WHERE  1=1 ";
    }

    @Override
    public AplicacionBean getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null, null);
    }

    public static AplicacionBean getRegistroResulset(ResultSet rs, GfhBean gfhBean, ProveedorBean proveedorBean) {
        AplicacionBean aplicacionesBean = new AplicacionBean();
        try {
            aplicacionesBean.setId(rs.getLong("aplicacionid"));
            aplicacionesBean.setNombre(rs.getString("aplicacionnombre"));
            //proveedor falta
            aplicacionesBean.setAmbito(rs.getString("aplicacionambito"));
            aplicacionesBean.setGestionUsuarios(rs.getString("aplicaciongestionusuarios"));
            aplicacionesBean.setDescripcion(rs.getString("aplicaciondescripcion"));
            if (gfhBean != null) {
                aplicacionesBean.setGfh(gfhBean);
            } else {
                aplicacionesBean.setGfh(new GfhDao().getRegistroResulset(rs));
            }
            aplicacionesBean.setFechaInstalacion(Utilidades.getFechaLocalDate(rs.getLong("aplicacionfechaInstalacion")));
            if (proveedorBean != null) {
                aplicacionesBean.setProveedor(proveedorBean);
            } else {
                aplicacionesBean.setProveedor(new ProveedorDao().getRegistroResulset(rs));
            }
            aplicacionesBean.setEstado(rs.getInt("aplicacionestado"));
            aplicacionesBean.setFechacambio(Utilidades.getFechaLocalDate(rs.getLong("aplicacionfechacambio")));
            // aplicacionesBean.setUsucambio(new UsuarioDao().getPorId(rs.getLong("aplicacionusucambio")));
            aplicacionesBean.setUsucambio(new UsuarioDao().getRegistroResulset(rs));
            // lista de perfiles
            aplicacionesBean.setListaPerfiles(new AplicacionPerfilDao().getLista(null, aplicacionesBean));
            // lista equipos
            //  aplicacionesBean.setListaEquipoBeans(new EquipoAplicacionDao().getLista(null, null, aplicacionesBean));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return aplicacionesBean;
    }

    @Override
    public AplicacionBean getPorCodigo(String codigo) {
        if (Utilidades.isNumeric(codigo)) {
            Long id = Long.parseLong(codigo);
            return getPorId(id);
        }
        return null;
    }

    @Override
    public AplicacionBean getPorId(Long id) {
        Connection connection = null;
        AplicacionBean aplicacionesBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND ap.id='" + id + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    aplicacionesBean = getRegistroResulset(resulSet, null, null);
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
        return aplicacionesBean;
    }

    @Override
    public boolean doGrabaDatos(AplicacionBean aplicacionesBean) {
        boolean actualizado = false;
        if (this.getPorId(aplicacionesBean.getId()) == null) {
            aplicacionesBean.setId(getSiguienteId("aplicaciones"));
            actualizado = this.doInsertaDatos(aplicacionesBean);
        } else {
            actualizado = this.doActualizaDatos(aplicacionesBean);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(AplicacionBean aplicacionesBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  aplicaciones  "
                    + "( id,nombre,proveedor,ambito,gestionusuarios,descripcion,gfh,fechainstalacion,estado,usucambio,fechacambio ) "
                    + " VALUES "
                    + "(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, aplicacionesBean.getId());
            if (aplicacionesBean.getNombre() != null) {
                statement.setString(2, aplicacionesBean.getNombre());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (aplicacionesBean.getProveedor() != null && aplicacionesBean.getProveedor().getId() != null) {
                statement.setLong(3, aplicacionesBean.getProveedor().getId());
            } else {
                statement.setNull(3, Types.DECIMAL);
            }
            if (aplicacionesBean.getAmbito() != null) {
                statement.setString(4, aplicacionesBean.getAmbito());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (aplicacionesBean.getGestionUsuarios() != null) {
                statement.setString(5, aplicacionesBean.getGestionUsuarios());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            if (aplicacionesBean.getDescripcion() != null) {
                statement.setString(6, aplicacionesBean.getDescripcion());
            } else {
                statement.setNull(6, Types.CHAR);
            }
            //,servicio,fechainstalacion,estado,usucambio,fechacambio ) "
            if (aplicacionesBean.getGfh() != null && aplicacionesBean.getGfh().getId() != null) {
                statement.setLong(7, aplicacionesBean.getGfh().getId());
            } else {
                statement.setNull(7, Types.DECIMAL);
            }
            if (aplicacionesBean.getFechaInstalacion() != null) {
                statement.setLong(8, Utilidades.getFechaLong(aplicacionesBean.getFechaInstalacion()));
            } else {
                statement.setNull(8, Types.DECIMAL);
            }
            if (aplicacionesBean.getEstado() != null) {
                statement.setInt(9, aplicacionesBean.getEstado());
            } else {
                statement.setNull(9, Types.CHAR);
            }
            statement.setLong(10, aplicacionesBean.getUsucambio().getId());
            statement.setLong(11, Utilidades.getFechaLong(aplicacionesBean.getFechacambio()));
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
    public boolean doActualizaDatos(AplicacionBean aplicacionesBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE  aplicaciones  SET "
                    + " nombre=?,proveedor=?,ambito=?,gestionusuarios=?,descripcion=?"
                    + ",gfh=?,fechainstalacion=?,estado=?,usucambio=?,fechacambio=?  "
                    + " WHERE id=? ";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (aplicacionesBean.getNombre() != null) {
                statement.setString(1, aplicacionesBean.getNombre());
            } else {
                statement.setNull(1, Types.CHAR);
            }
            if (aplicacionesBean.getProveedor() != null && aplicacionesBean.getProveedor().getId() != null) {
                statement.setLong(2, aplicacionesBean.getProveedor().getId());
            } else {
                statement.setNull(2, Types.DECIMAL);
            }
            if (aplicacionesBean.getAmbito() != null) {
                statement.setString(3, aplicacionesBean.getAmbito());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (aplicacionesBean.getGestionUsuarios() != null) {
                statement.setString(4, aplicacionesBean.getGestionUsuarios());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (aplicacionesBean.getDescripcion() != null) {
                statement.setString(5, aplicacionesBean.getDescripcion());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            //,gfh,fechainstalacion,estado,usucambio,fechacambio ) "
            if (aplicacionesBean.getGfh() != null && aplicacionesBean.getGfh().getId() != null) {
                statement.setLong(6, aplicacionesBean.getGfh().getId());
            } else {
                statement.setNull(6, Types.DECIMAL);
            }
            if (aplicacionesBean.getFechaInstalacion() != null) {
                statement.setLong(7, Utilidades.getFechaLong(aplicacionesBean.getFechaInstalacion()));
            } else {
                statement.setNull(7, Types.DECIMAL);
            }
            if (aplicacionesBean.getEstado() != null) {
                statement.setInt(8, aplicacionesBean.getEstado());
            } else {
                statement.setNull(8, Types.CHAR);
            }
            statement.setLong(9, aplicacionesBean.getUsucambio().getId());
            statement.setLong(10, Utilidades.getFechaLong(aplicacionesBean.getFechacambio()));
            statement.setLong(11, aplicacionesBean.getId());
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
    public boolean doBorraDatos(AplicacionBean aplicacionesBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   aplicaciones  SET estado=?,usucambio=? ,fechacambio=? "
                    + " WHERE id=?  ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, aplicacionesBean.getEstado());
            if (aplicacionesBean.getUsucambio() != null && aplicacionesBean.getUsucambio().getId() != null) {
                statement.setLong(2, aplicacionesBean.getUsucambio().getId());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            statement.setLong(3, Utilidades.getFechaLong(aplicacionesBean.getFechacambio()));
            statement.setLong(4, aplicacionesBean.getId());
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
     * @param texto
     * @param gfhBean
     * @param proveedorBean
     * @param estado
     * @return
     */
    public ArrayList<AplicacionBean> getLista(String texto, GfhBean gfhBean, ProveedorBean proveedorBean, Integer estado) {
        Connection connection = null;
        ArrayList<AplicacionBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();

            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND   UPPER(ap,descripcion) like'%" + texto.toUpperCase() + "%'  ");
            }
            if (gfhBean != null) {
                sql = sql.concat(" AND ap.gfh=" + gfhBean.getId());
            }
            if (proveedorBean != null) {
                sql = sql.concat(" AND ap.proveedor=" + proveedorBean.getId());
            }
            if (estado != null) {
                sql = sql.concat(" AND ap.estado=" + estado);
            }

            sql = sql.concat(" ORDER BY ap.descripcion  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, gfhBean, proveedorBean));
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

    @Override
    public ArrayList<AplicacionBean> getLista(String texto) {
        return getLista(texto, null, null, null);
    }

}
