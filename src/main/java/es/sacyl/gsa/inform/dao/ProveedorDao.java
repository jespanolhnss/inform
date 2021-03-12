package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.ProveedorBean;
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
 * @author 06551256M
 */
public class ProveedorDao extends ConexionDao implements Serializable, ConexionInterface<ProveedorBean> {

    private static final Logger LOGGER = LogManager.getLogger(ProveedorDao.class);
    private static final long serialVersionUID = 1L;

    //   nombre,direccion,codpostal,telefonos,mail,localidad,provincia;
    public ProveedorDao() {
        super();
        sql = " SELECT  prvee.id as proveedorid, prvee.nombre as proveedornombre"
                + " ,prvee.direccion as proveedordirecion,prvee.codpostal as proveedorcodpostal"
                + " ,prvee.telefonos as proveedortelfoonos,prvee.mail as proveedormail"
                + " ,prvee.localidad as proveedorlocalidad,prvee.provincia  as proveedorprovincia"
                + " ,l.codigo as localidadcodigo,l.nombre as localidadnombre,l.codprov as localidadcodpro"
                + " ,p. codigo as provinciacodigo, p.nombre as provincianombre,p.codauto as provinciacodauto  "
                + " ,a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado   "
                + " FROM proveedores prvee "
                + " JOIN localidad l ON l.codigo=prvee.localidad"
                + " JOIN provincia p ON p.codigo=l.codprov "
                + " JOIN CAUTONOM a ON a.codigo=p.CODAUTO  "
                + "WHERE  1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public ProveedorBean getRegistroResulset(ResultSet rs) {

        ProveedorBean proveedorBean = new ProveedorBean();
        try {
            proveedorBean.setId(rs.getLong("proveedorid"));
            proveedorBean.setNombre(rs.getString("proveedornombre"));
            proveedorBean.setDireccion(rs.getString("proveedordirecion"));
            proveedorBean.setCodpostal(rs.getString("proveedorcodpostal"));
            proveedorBean.setAutonomia(new AutonomiaDao().getRegistroResulset(rs));
            proveedorBean.setTelefonos(rs.getString("proveedortelfoonos"));
            proveedorBean.setMail(rs.getString("proveedormail"));

            proveedorBean.setProvincia(ProvinciaDao.getRegistroResulset(rs, null));
            proveedorBean.setLocalidad(LocalidadDao.getRegistroResulset(rs, proveedorBean.getProvincia()));

        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return proveedorBean;
    }

    /**
     *
     * @param codigo
     * @return
     */
    @Override
    public ProveedorBean getPorCodigo(String codigo) {
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
    public ProveedorBean getPorId(Long id) {
        Connection connection = null;
        ProveedorBean proveedorBean = null;
        if (id != null) {
            try {
                connection = super.getConexionBBDD();
                sql = sql.concat(" AND prvee.id='" + id + "'");
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        proveedorBean = getRegistroResulset(resulSet);
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
        return proveedorBean;
    }

    /**
     *
     * @param proveedorBean
     * @return
     */
    @Override
    public boolean doGrabaDatos(ProveedorBean proveedorBean) {
        boolean actualizado = false;
        if (this.getPorId(proveedorBean.getId()) == null) {
            proveedorBean.setId(getSiguienteId("proveedores"));
            actualizado = this.doInsertaDatos(proveedorBean);
        } else {
            actualizado = this.doActualizaDatos(proveedorBean);
        }
        return actualizado;
    }

    /**
     *
     * @param proveedorBean
     * @return
     */
    @Override
    public boolean doInsertaDatos(ProveedorBean proveedorBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  proveedores  "
                    + "( id,nombre,direccion,codpostal,telefonos,mail,provincia,localidad,estado,usucambio,fechacambio ) "
                    + " VALUES "
                    + "(?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, proveedorBean.getId());
            if (proveedorBean.getNombre() != null) {
                statement.setString(2, proveedorBean.getNombre());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (proveedorBean.getDireccion() != null) {
                statement.setString(3, proveedorBean.getDireccion());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (proveedorBean.getCodpostal() != null) {
                statement.setString(4, proveedorBean.getCodpostal());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (proveedorBean.getTelefonos() != null) {
                statement.setString(5, proveedorBean.getTelefonos());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            if (proveedorBean.getMail() != null) {
                statement.setString(6, proveedorBean.getMail());
            } else {
                statement.setNull(6, Types.CHAR);
            }
            if (proveedorBean.getProvincia() != null) {
                statement.setString(7, proveedorBean.getProvincia().getCodigo());
            } else {
                statement.setNull(7, Types.CHAR);
            }

            if (proveedorBean.getLocalidad() != null) {
                statement.setString(8, proveedorBean.getLocalidad().getCodigo());
            } else {
                statement.setNull(8, Types.CHAR);
            }

            if (proveedorBean.getEstado() != null) {
                statement.setInt(9, proveedorBean.getEstado());
            } else {
                statement.setNull(9, Types.CHAR);
            }
            statement.setLong(10, proveedorBean.getUsucambio().getId());
            statement.setLong(11, Utilidades.getFechaLong(proveedorBean.getFechacambio()));

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
     * @param proveedorBean
     * @return
     */
    @Override
    public boolean doActualizaDatos(ProveedorBean proveedorBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = "UPDATE   proveedores   SET"
                    + " nombre=?,direccion=?,codpostal=?,telefonos=?,mail=?,provincia=?,localidad=?,estado=?,usucambio=?"
                    + ",fechacambio=?  "
                    + " WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            if (proveedorBean.getNombre() != null) {
                statement.setString(1, proveedorBean.getNombre());
            } else {
                statement.setNull(1, Types.CHAR);
            }
            if (proveedorBean.getDireccion() != null) {
                statement.setString(2, proveedorBean.getDireccion());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (proveedorBean.getCodpostal() != null) {
                statement.setString(3, proveedorBean.getCodpostal());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (proveedorBean.getTelefonos() != null) {
                statement.setString(4, proveedorBean.getTelefonos());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (proveedorBean.getMail() != null) {
                statement.setString(5, proveedorBean.getMail());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            if (proveedorBean.getProvincia() != null) {
                statement.setString(6, proveedorBean.getProvincia().getCodigo());
            } else {
                statement.setNull(6, Types.CHAR);
            }

            if (proveedorBean.getLocalidad() != null) {
                statement.setString(7, proveedorBean.getLocalidad().getCodigo());
            } else {
                statement.setNull(7, Types.CHAR);
            }

            if (proveedorBean.getEstado() != null) {
                statement.setInt(8, proveedorBean.getEstado());
            } else {
                statement.setNull(8, Types.CHAR);
            }
            statement.setLong(9, proveedorBean.getUsucambio().getId());
            statement.setLong(10, Utilidades.getFechaLong(proveedorBean.getFechacambio()));

            statement.setLong(11, proveedorBean.getId());
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
    public boolean doBorraDatos(ProveedorBean aplicacionesBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   proveedores SET estado=" + CentroDao.BBDD_ACTIVONO + " WHERE id='" + aplicacionesBean.getId() + "'";
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
     * @param texto
     * @return
     */
    @Override
    public ArrayList<ProveedorBean> getLista(String texto) {
        return getLista(texto, null, ConexionDao.BBDD_ACTIVOSI);
    }

    public ArrayList<ProveedorBean> getLista(String texto, ProvinciaBean provinciaBean, Integer estado) {
        Connection connection = null;
        ArrayList<ProveedorBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();

            if (provinciaBean != null) {
                sql = sql.concat(" AND provincia='" + provinciaBean.getCodigo() + "'");
            }
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(prvee.nombre) like'%" + texto.toUpperCase() + "%'  ");
            }
            if (estado != null) {
                sql = sql.concat(" AND prvee.estado = " + estado);
            }
            sql = sql.concat(" ORDER BY prvee.nombre  ");
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
