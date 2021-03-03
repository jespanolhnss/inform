package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.GfhBean;
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
public class EquipoDao extends ConexionDao implements Serializable, ConexionInterface<EquipoBean> {

    private static final Logger LOGGER = LogManager.getLogger(EquipoDao.class);
    private static final long serialVersionUID = 1L;

    public EquipoDao() {
        super();
        sql = " SELECT  e.id as equipoid,e.tipo as equipotipo, e.inventario as  equipoinventario,e.marca as   equipomarca"
                + " ,e.modelo as  equipomodelo"
                + " ,e.numeroserie as   equiponumeroserie, e.centro   equipocentro,  e.ubicacion  equipoubicacion  "
                + " ,e.servicio as  equiposervicio, e.ip as  equipoip, e.comentario as  equipocomentario "
                + " ,gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasisencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado"
                + " ,u.id as ubicacionesid , u.centro as ubicacionescentro, u.descripcion as ubicacionesdescripcion,"
                + " u.idpadre ubicacionesidpadre, u.nivel  as ubicacionesnivel  "
                + " c.ID as centroid,c.CODAUTO as centrocodauto,c.CODGEREN   as centrocodgeren"
                + " ,c.CODZONA as centrocodozona "
                + " ,c.CODIGO as centrocodigo, c.NOMCEN as centronumcen ,c.TIPOVIA as centrotipovia "
                + " ,c.CALLECEN as centrocallecen,c.NUMCALCEN as centronumcalcen,c.OTRDIRCEN as centrootrodircen"
                + " ,c.CODLOCAL as centrocodlocal "
                + " ,c.CPCENTRO as centrocpcentro,c.TELEPREV as centroteleprev,c.TIPOCENTRO as centrotipocentro"
                + " ,c.NIVATENCION as centronivatencion "
                + " ,c.estado as centroestado, c.mapgoogle as centromapggole  "
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
                + " FROM equipos e  "
                + " LEFT JOIN  gfh  ON gfh.id=e.servicio"
                + " LEFT JOIN ubicaciones u ON u.id=e.ubicacion"
                + " LEFT JOIN centros c ON c.id = e.centro"
                + " LEFT JOIN nivelesatencion n ON n.id = c.NIVATENCION  "
                + " JOIN LOCALIDAD l ON l.codigo = c.CODLOCAL "
                + " JOIN PROVINCIA p ON p.codigo=l.codprov "
                + " JOIN CAUTONOM a On a.codigo=p.CODAUTO  "
                + " JOIN GERENCIA g ON g.codauto=a.codigo AND g.codigo=c.codgeren  "
                + " JOIN ZONAS z ON z.codauto=c.codauto AND z.codgeren=g.codigo AND z.codigo=c.codzona "
                + " JOIN centrostipo ct ON ct.id=c.tipocentro "
                + " JOIN  NIVELESATENCIONTIPO na  ON na.id =n.tipo"
                + " WHERE  1=1 ";

        sql = " SELECT  e.id as equipoid,e.tipo as equipotipo, e.inventario as  equipoinventario,e.marca as   equipomarca"
                + " ,e.modelo as  equipomodelo"
                + " ,e.numeroserie as   equiponumeroserie, e.centro   equipocentro,  e.ubicacion  equipoubicacion  "
                + " ,e.servicio as  equiposervicio, e.ip as  equipoip, e.comentario as  equipocomentario "
                + " ,e.estado as equipoestado,e.fechacambio as equipofechacambio, e.usucambio as equipousucambio "
                + " ,gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasisencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado"
                + " ,u.id as ubicacionesid , u.centro as ubicacionescentro, u.descripcion as ubicacionesdescripcion,"
                + " u.idpadre ubicacionesidpadre, u.nivel  as ubicacionesnivel  "
                + " FROM equipos e  "
                + " LEFT JOIN  gfh  ON gfh.id=e.servicio"
                + " LEFT JOIN ubicaciones u ON u.id=e.ubicacion"
                + " WHERE  1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public EquipoBean getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null, null);
    }

    /**
     *
     * @param rs
     * @param centro
     * @param servicio
     * @return
     */
    public static EquipoBean getRegistroResulset(ResultSet rs, CentroBean centro, GfhBean servicio) {
        EquipoBean equipoBean = new EquipoBean();
        try {
            equipoBean.setId(rs.getLong("equipoid"));
            equipoBean.setTipo(rs.getString("equipotipo"));
            equipoBean.setInventario(rs.getString("equipoinventario"));
            equipoBean.setMarca(rs.getString("equipomarca"));
            equipoBean.setModelo(rs.getString("equipomodelo"));
            equipoBean.setNumeroSerie(rs.getString("equiponumeroserie"));
            if (centro == null) {
                equipoBean.setCentro(new CentroDao().getPorId(rs.getLong("equipocentro")));
            } else {
                equipoBean.setCentro(centro);
            }

            //          equipoBean.setUbicacion(new UbicacionDao().getPorId(rs.getLong("equipoubicacion")));
            equipoBean.setUbicacion(new UbicacionDao().getRegistroResulset(rs, equipoBean.getCentro()));
            if (servicio == null) {
                //     equipoBean.setServicio(new GfhDao().getPorId(rs.getLong("equiposervicio")));
                equipoBean.setServicio(new GfhDao().getRegistroResulset(rs));
            } else {
                equipoBean.setServicio(servicio);
            }

            //   equipoBean.setIp(rs.getString("equipoip"));
            equipoBean.setComentario(rs.getString("equipocomentario"));

            equipoBean.setEstado(rs.getInt("equipoestado"));
            equipoBean.setFechacambio(Utilidades.getFechaLocalDate(rs.getLong("equipofechacambio")));
            equipoBean.setUsucambio(new UsuarioDao().getPorId(rs.getLong("equipousucambio")));
// actualiza ip
            equipoBean.setListaIps(new IpDao().getLista(null, null, equipoBean, null, null));
            // actualiza app instaladas
            equipoBean.setAplicacinesArrayList(new EquipoAplicacionDao().getLista(null, equipoBean, null));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return equipoBean;
    }

    /**
     *
     * @param codigo
     * @return
     */
    @Override
    public EquipoBean getPorCodigo(String codigo) {
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
    public EquipoBean getPorId(Long id) {
        Connection connection = null;
        EquipoBean equipoBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND e.id='" + id + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    equipoBean = getRegistroResulset(resulSet, null, null);
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
        return equipoBean;
    }

    /**
     *
     * @param equipoBean
     * @return
     */
    @Override
    public boolean doGrabaDatos(EquipoBean equipoBean) {
        boolean actualizado = false;
        if (this.getPorId(equipoBean.getId()) == null) {
            equipoBean.setId(this.getSiguienteId("equipos"));
            actualizado = this.doInsertaDatos(equipoBean);
        } else {
            actualizado = this.doActualizaDatos(equipoBean);
        }
        return actualizado;
    }

    @Override
    public boolean doInsertaDatos(EquipoBean equipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = sql = "INSERT INTO     equipos  "
                    + "( id,tipo,  inventario,  marca,  modelo,  numeroserie,  centro,  ubicacion, servicio,    comentario, estado,fechacambio,usucambio )"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, equipoBean.getId());
            if (equipoBean.getTipo() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, equipoBean.getTipo());
            }
            if (equipoBean.getInventario() == null) {
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(3, equipoBean.getInventario());
            }
            if (equipoBean.getMarca() == null) {
                statement.setNull(4, Types.VARCHAR);
            } else {
                statement.setString(4, equipoBean.getMarca());
            }
            if (equipoBean.getModelo() == null) {
                statement.setNull(5, Types.VARCHAR);
            } else {
                statement.setString(5, equipoBean.getModelo());
            }
            if (equipoBean.getNumeroSerie() == null) {
                statement.setNull(6, Types.VARCHAR);
            } else {
                statement.setString(6, equipoBean.getNumeroSerie());
            }
            //centro,  ubicacion, servicio,  ip,  comentario
            if (equipoBean.getCentro() == null) {
                statement.setNull(7, Types.DECIMAL);
            } else {
                statement.setLong(7, equipoBean.getCentro().getId());
            }
            if (equipoBean.getUbicacion() == null) {
                statement.setNull(8, Types.DECIMAL);
            } else {
                statement.setLong(8, equipoBean.getUbicacion().getId());
            }
            if (equipoBean.getServicio() == null) {
                statement.setNull(9, Types.DECIMAL);
            } else {
                statement.setLong(9, equipoBean.getServicio().getId());
            }

            if (equipoBean.getComentario() == null) {
                statement.setNull(10, Types.VARCHAR);
            } else {
                statement.setString(10, equipoBean.getComentario());
            }
            statement.setInt(11, ConexionDao.BBDD_ACTIVOSI);
            statement.setLong(12, Utilidades.getFechaLong(equipoBean.getFechacambio()));
            statement.setLong(13, equipoBean.getUsucambio().getId());

            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException e) {
            LOGGER.error(sql, e);

        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public boolean doActualizaDatos(EquipoBean equipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();

            sql = sql = "UPDATE     equipos  SET "
                    + "tipo=?, inventario=?,  marca=?,  modelo=?,  numeroserie=?,  centro=?,  ubicacion=?, servicio=?,   comentario=? "
                    + " , fechacambio=?, usucambio=?  WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (equipoBean.getTipo() == null) {
                statement.setNull(1, Types.VARCHAR);
            } else {
                statement.setString(1, equipoBean.getTipo());
            }
            if (equipoBean.getInventario() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, equipoBean.getInventario());
            }
            if (equipoBean.getMarca() == null) {
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(3, equipoBean.getMarca());
            }
            if (equipoBean.getModelo() == null) {
                statement.setNull(4, Types.VARCHAR);
            } else {
                statement.setString(4, equipoBean.getModelo());
            }
            if (equipoBean.getNumeroSerie() == null) {
                statement.setNull(5, Types.VARCHAR);
            } else {
                statement.setString(5, equipoBean.getNumeroSerie());
            }
            //centro,  ubicacion, servicio,  ip,  comentario
            if (equipoBean.getCentro() == null) {
                statement.setNull(6, Types.DECIMAL);
            } else {
                statement.setLong(6, equipoBean.getCentro().getId());
            }
            if (equipoBean.getUbicacion() == null) {
                statement.setNull(7, Types.DECIMAL);
            } else {
                statement.setLong(7, equipoBean.getUbicacion().getId());
            }
            if (equipoBean.getServicio() == null) {
                statement.setNull(8, Types.DECIMAL);
            } else {
                statement.setLong(8, equipoBean.getServicio().getId());
            }

            if (equipoBean.getComentario() == null) {
                statement.setNull(9, Types.VARCHAR);
            } else {
                statement.setString(9, equipoBean.getComentario());
            }
            statement.setLong(10, Utilidades.getFechaLong(equipoBean.getFechacambio()));
            statement.setLong(11, equipoBean.getUsucambio().getId());
            statement.setLong(12, equipoBean.getId());

            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException e) {
            LOGGER.error(sql, e);

        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public boolean doBorraDatos(EquipoBean equipoBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   equipos  SET estado=?,usucambio=? ,fechacambio=? "
                    + " WHERE id=?  ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, equipoBean.getEstado());
            if (equipoBean.getUsucambio() != null && equipoBean.getUsucambio().getId() != null) {
                statement.setLong(2, equipoBean.getUsucambio().getId());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            statement.setLong(3, Utilidades.getFechaLong(equipoBean.getFechacambio()));
            statement.setLong(4, equipoBean.getId());
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
     * @return
     */
    @Override
    public ArrayList<EquipoBean> getLista(String texto) {
        return getLista(texto, null, null, null, null);
    }

    /**
     *
     * @param texto
     * @param tipo
     * @param centro
     * @param servicio
     * @return
     */
    public ArrayList<EquipoBean> getLista(String texto, String tipo, CentroBean centro, GfhBean servicio, Integer estado) {
        Connection connection = null;
        ArrayList<EquipoBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();

            if (tipo != null) {
                sql = sql.concat(" AND e.tipo='" + tipo + "'");
            }
            if (centro != null) {
                sql = sql.concat(" AND e.centro='" + centro.getId() + "'");
            }
            if (servicio != null) {
                sql = sql.concat(" AND e.servicio='" + servicio.getId() + "'");
            }

            if (texto != null && !texto.isEmpty()) {
                //   sql = sql.concat(" AND  ( UPPER(nombre) like'%" + texto.toUpperCase() + "%'  OR   UPPER(codigo) like'%" + texto.toUpperCase() + "%' )");
            }
            if (estado != null) {
                sql = sql.concat(" AND e.estado=" + estado);

            }
            sql = sql.concat(" ORDER BY e.tipo,e.marca  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, centro, servicio));
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
