package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
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
        /*
        sql = " SELECT  e.id as equipoid,e.tipo as equipotipo, e.inventario as  equipoinventario,e.marca as   equipomarca"
                + " ,e.modelo as  equipomodelo"
                + " ,e.numeroserie as   equiponumeroserie, e.centro   equipocentro,  e.ubicacion  equipoubicacion  "
                + " ,e.servicio as  equiposervicio, e.ip as  equipoip, e.comentario as  equipocomentario,e.mac as equipomac "
                + " ,e.usuario as equipousuario, e.nombredominio sa equiponombredominio"
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
                + " ,e.servicio as  equiposervicio, e.ip as  equipoip, e.comentario as  equipocomentario,e.mac as equipomac "
                + " ,e.estado as equipoestado,e.fechacambio as equipofechacambio, e.usucambio as equipousucambio "
                + " ,e.usuario as equipousuario, e.nombredominio as equiponombredominio"
                + " ,gfh.id as gfhid,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasistencial,gfh.idjimena as gfhidjimena, gfh.estado as gfhestado"
                + " ,u.id as ubicacionesid , u.centro as ubicacionescentro, u.descripcion as ubicacionesdescripcion"
                + " ,u.idpadre ubicacionesidpadre, u.nivel  as ubicacionesnivel  "
                + " ,usu.id as usuarioid,usu.dni as usuariodni,usu.apellido1 as usuarioapellido1"
                + ",usu.apellido2 as usuarioapellido2,usu.nombre as usuarionombre"
                + ",usu.estado as usuarioestado,usu.usucambio as usuariousucambio"
                + ",usu.fechacambio as usuariofechacambio,usu.mail as usuariomail"
                + ",usu.telefono as usuariotelefon,usu.idgfh as usuarioidgfh"
                + ",usu.idcategoria as usuarioidcategoria"
                + ",usu.idcategoria as usuarioidcategoria,usu.movil as usuariomovil"
                + ",usu.mailprivado as usuariomailprivado,usu.telegram as usuariotegegram"
                + ",usu.solicita as usuariosolicita"
                + ",uc.id as usuarioscategoriaid, uc.CODIGOPERSIGO as usuarioscategoriacodigo"
                + ",uc.nombre as usuarioscategoriaanombre,uc.estado as usuarioscategoriaestado  "
                + ",gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasistencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado"
                + ",usue.id as usueid,usue.dni as usuedni,usue.apellido1 as usueapellido1"
                + ",usue.apellido2 as usueapellido2,usue.nombre as usuenombre"
                + ",usue.estado as usueestado,usue.usucambio as usueusucambio"
                + ",usue.fechacambio as usuefechacambio,usue.mail as usuemail"
                + ",usue.telefono as usuetelefon,usue.idgfh as usueidgfh"
                + ",usue.idcategoria as usueidcategoria"
                + " FROM equipos e  "
                + " LEFT JOIN  gfh  ON gfh.id=e.servicio"
                + " LEFT JOIN ubicaciones u ON u.id=e.ubicacion"
                + " LEFT JOIN usuarios usu ON usu.id=e.usucambio"
                + " LEFT JOIN categorias uc ON uc.id=usu.idcategoria "
                + " LEFT JOIN usuarios usue ON usue.id=e.usuario"
                + " LEFT JOIN gfh gfh ON usu.idgfh = gfh.id"
                + " WHERE  1=1 ";
         */
        sql = " SELECT  e.id as equipoid,e.tipo as equipotipo, e.inventario as  equipoinventario,e.marca as   equipomarca"
                + " ,e.modelo as  equipomodelo"
                + " ,e.numeroserie as   equiponumeroserie, e.centro   equipocentro,  e.ubicacion  equipoubicacion  "
                + " ,e.servicio as  equiposervicio, e.ip as  equipoip, e.comentario as  equipocomentario,e.mac as equipomac "
                + " ,e.estado as equipoestado,e.fechacambio as equipofechacambio, e.usucambio as equipousucambio "
                + " ,e.usuario as equipousuario, e.nombredominio as equiponombredominio"
                + " ,gfh.id as gfhid,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasistencial,gfh.idjimena as gfhidjimena, gfh.estado as gfhestado"
                + " ,u.id as ubicacionesid , u.centro as ubicacionescentro, u.descripcion as ubicacionesdescripcion"
                + " ,u.idpadre ubicacionesidpadre, u.nivel  as ubicacionesnivel  "
                + " ,usu.id as usuarioid,usu.dni as usuariodni,usu.apellido1 as usuarioapellido1"
                + ",usu.apellido2 as usuarioapellido2,usu.nombre as usuarionombre"
                + ",usu.estado as usuarioestado,usu.usucambio as usuariousucambio"
                + ",usu.fechacambio as usuariofechacambio,usu.mail as usuariomail"
                + ",usu.telefono as usuariotelefon,usu.idgfh as usuarioidgfh"
                + ",usu.idcategoria as usuarioidcategoria"
                + ",usu.idcategoria as usuarioidcategoria,usu.movil as usuariomovil"
                + ",usu.mailprivado as usuariomailprivado,usu.telegram as usuariotegegram"
                + ",usu.solicita as usuariosolicita"
                + ",uc.id as usuarioscategoriaid, uc.CODIGOPERSIGO as usuarioscategoriacodigo"
                + ",uc.nombre as usuarioscategoriaanombre,uc.estado as usuarioscategoriaestado  "
                + ",gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasistencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado,gfh.gfhpersigo, gfh.division  as gfhdivision "
                + ",usue.id as usueid,usue.dni as usuedni,usue.apellido1 as usueapellido1"
                + ",usue.apellido2 as usueapellido2,usue.nombre as usuenombre"
                + ",usue.estado as usueestado,usue.usucambio as usueusucambio"
                + ",usue.fechacambio as usuefechacambio,usue.mail as usuemail"
                + ",usue.telefono as usuetelefon,usue.idgfh as usueidgfh"
                + ",usue.idcategoria as usueidcategoria"
                + " ,c.ID as centroid,c.CODAUTO as centrocodauto,c.CODGEREN   as centrocodgeren"
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
                + " FROM equipos e  "
                + " LEFT JOIN  gfh  ON gfh.id=e.servicio"
                + " LEFT JOIN ubicaciones u ON u.id=e.ubicacion"
                + " LEFT JOIN usuarios usu ON usu.id=e.usucambio"
                + " LEFT JOIN categorias uc ON uc.id=usu.idcategoria "
                + " LEFT JOIN gfh gfh ON usu.idgfh = gfh.id"
                + " LEFT JOIN usuarios usue ON usue.id=e.usuario"
                + " LEFT JOIN centros c ON c.id=e.centro "
                + " LEFT JOIN nivelesatencion n ON n.id = c.NIVATENCION  "
                + " JOIN LOCALIDAD l ON l.codigo = c.CODLOCAL "
                + " JOIN PROVINCIA p ON p.codigo=l.codprov "
                + " JOIN CAUTONOM a On a.codigo=p.CODAUTO  "
                + " JOIN GERENCIA g ON g.codauto=a.codigo AND g.codigo=c.codgeren  "
                + " JOIN ZONAS z ON z.codauto=c.codauto AND z.codgeren=g.codigo AND z.codigo=c.codzona "
                + " JOIN centrostipo ct ON ct.id=c.tipocentro "
                + " JOIN  NIVELESATENCIONTIPO na  ON na.id =n.tipo"
                + " WHERE  1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public EquipoBean getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null, null, null, null, null, null);
    }

    public EquipoBean getRegistroResulset(ResultSet rs, Boolean conAplicaciones, Boolean conIps, AplicacionBean aplicacionBean) {
        return getRegistroResulset(rs, null, null, conAplicaciones, conIps, aplicacionBean, null);
    }

    /**
     *
     * @param rs
     * @param centro
     * @param servicio
     * @return
     */
    public static EquipoBean getRegistroResulset(ResultSet rs, CentroBean centro, GfhBean servicio,
            Boolean conAplicaciones, Boolean conIps, AplicacionBean aplicacionBean, Boolean conUbicacion) {
        EquipoBean equipoBean = new EquipoBean();
        try {
            equipoBean.setId(rs.getLong("equipoid"));
            equipoBean.setTipo(rs.getString("equipotipo"));

            equipoBean.setInventario(rs.getLong("equipoinventario"));
            if (equipoBean.getInventario().equals(new Long(0))) {
                equipoBean.setInventario(null);
            }
            equipoBean.setMarca(rs.getString("equipomarca"));
            equipoBean.setModelo(rs.getString("equipomodelo"));
            equipoBean.setNumeroSerie(rs.getString("equiponumeroserie"));
            if (centro == null) {
                //equipoBean.setCentro(new CentroDao().getPorId(rs.getLong("equipocentro")));
                equipoBean.setCentro(new CentroDao().getRegistroResulset(rs));
            } else {
                equipoBean.setCentro(centro);
            }

            //  equipoBean.setUbicacion(new UbicacionDao().getRegistroResulset(rs, equipoBean.getCentro()));
            if (servicio == null) {
                //     equipoBean.setServicio(new GfhDao().getPorId(rs.getLong("equiposervicio")));
                equipoBean.setServicio(new GfhDao().getRegistroResulset(rs));
            } else {
                equipoBean.setServicio(servicio);
            }

            //   equipoBean.setIp(rs.getString("equipoip"));
            equipoBean.setComentario(rs.getString("equipocomentario"));
            equipoBean.setMacadress(rs.getString("equipomac"));
            equipoBean.setNombredominio(rs.getString("equiponombredominio"));

            equipoBean.setUsuario(UsuarioDao.getRegistroResulsetUsuario(rs));

            equipoBean.setEstado(rs.getInt("equipoestado"));
            equipoBean.setFechacambio(Utilidades.getFechaLocalDate(rs.getLong("equipofechacambio")));
            //   equipoBean.setUsucambio(new UsuarioDao().getPorId(rs.getLong("equipousucambio")));
            equipoBean.setUsucambio(new UsuarioDao().getRegistroResulset(rs));

            // para no sobrecargar los sql
            if (conUbicacion != null && conUbicacion == true) {
                equipoBean.setUbicacion(new UbicacionDao().getPorId(rs.getLong("equipoubicacion")));
            } else {
                // guarda solo el id de la ubicacion
                equipoBean.setUbicacion(new UbicacionBean(rs.getLong("equipoubicacion")));
            }
// actualiza ip

            if (conIps == true) {
                equipoBean.setListaIps(new IpDao().getLista(null, null, equipoBean, null, null));
            }

            // actualiza app instaladas
            if (conAplicaciones == true) {
                equipoBean.setAplicacinesArrayList(new EquipoAplicacionDao().getLista(null, equipoBean, aplicacionBean));
            }
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return equipoBean;
    }

    public EquipoBean getPorInventario(String inventario) {
        Connection connection = null;
        EquipoBean equipoBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND e.inventario='" + inventario + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    equipoBean = getRegistroResulset(resulSet, null, null, true, true, null, true);
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
        return getPorId(id, null, null, null);
    }

    public EquipoBean getPorId(Long id, CentroBean centro, GfhBean gfh, AplicacionBean aplicacion) {
        Connection connection = null;
        EquipoBean equipoBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND e.id='" + id + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    equipoBean = getRegistroResulset(resulSet, centro, gfh, true, true, aplicacion, true);
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

    public EquipoBean getPorIP(String ip) {
        Connection connection = null;
        EquipoBean equipoBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND e.id IN (SELECT equipo FROm ips WHERE ip='" + ip + "')");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    equipoBean = getRegistroResulset(resulSet, null, null, true, true, null, true);
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
                    + "( id,tipo,  inventario,  marca,  modelo,  numeroserie,  centro,  ubicacion, servicio"
                    + ",    comentario, estado,fechacambio,usucambio,mac, usuario )"
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                statement.setLong(3, equipoBean.getInventario());
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
            if (equipoBean.getUsuario() != null) {
                statement.setLong(13, equipoBean.getUsucambio().getId());
            } else {
                statement.setNull(13, Types.INTEGER);
            }
            if (equipoBean.getMacadress() == null) {
                statement.setNull(14, Types.VARCHAR);
            } else {
                statement.setString(14, equipoBean.getMacadress());
            }

            if (equipoBean.getUsuario() != null && equipoBean.getUsuario().getId() != null) {
                statement.setLong(15, equipoBean.getUsuario().getId());
            } else {
                statement.setNull(15, Types.INTEGER);
            }
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
                    + "tipo=?, inventario=?,  marca=?,  modelo=?,  numeroserie=?,  centro=?,  ubicacion=?, servicio=?"
                    + ",   comentario=? "
                    + " , fechacambio=?, usucambio=? ,mac=? , usuario=?, estado=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            if (equipoBean.getTipo() == null) {
                statement.setNull(1, Types.VARCHAR);
            } else {
                statement.setString(1, equipoBean.getTipo());
            }
            if (equipoBean.getInventario() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setLong(2, equipoBean.getInventario());
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

            if (equipoBean.getUsucambio() != null) {
                statement.setLong(11, equipoBean.getUsucambio().getId());
            } else {
                statement.setNull(11, Types.INTEGER);
            }

            if (equipoBean.getMacadress() == null) {
                statement.setNull(12, Types.VARCHAR);
            } else {
                statement.setString(12, equipoBean.getMacadress());
            }

            if (equipoBean.getUsuario() != null && equipoBean.getUsuario().getId() != null) {
                statement.setLong(13, equipoBean.getUsuario().getId());
            } else {
                statement.setNull(13, Types.INTEGER);
            }

            statement.setInt(14, equipoBean.getEstado());
            statement.setLong(15, equipoBean.getId());

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
        return getLista(texto, null, null, null, null, null, null);
    }

    /**
     *
     * @param texto
     * @param tipo
     * @param centro
     * @param servicio
     * @return
     */
    public ArrayList<EquipoBean> getLista(String texto, String tipo, String marca, CentroBean centro, GfhBean servicio, Integer estado,
            AplicacionBean aplicacionBean
    ) {
        Connection connection = null;
        ArrayList<EquipoBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();

            if (tipo != null && !tipo.isEmpty()) {
                sql = sql.concat(" AND e.tipo='" + tipo + "'");
            }
            if (marca != null && marca.isEmpty()) {
                sql = sql.concat(" AND e.marca='" + marca + "'");
            }
            if (centro != null) {
                sql = sql.concat(" AND e.centro='" + centro.getId() + "'");
            }
            if (servicio != null) {
                sql = sql.concat(" AND e.servicio='" + servicio.getId() + "'");
            }
            if (texto != null && !texto.isEmpty()) {

                if (IpCtrl.isValid(texto)) {
                    sql = sql.concat("  AND e.id IN (select  equipo from ips where not equipo  is null AND  ip = '" + texto + "')");
                } else if (texto.contains("10.")) {
                    sql = sql.concat(" AND  e.id IN (select  equipo from ips where not equipo  is null AND  ip LIKE  '" + texto + "%')");
                } else if (texto.charAt(2) == ":".charAt(0)) {
                    sql = sql.concat(" AND  e.mac  LIKE  '" + texto + "%'");
                } else {
                    sql = sql.concat(" AND e.nombredominio like '%" + texto + "%'");
                }
            }
            if (estado != null) {
                sql = sql.concat(" AND e.estado=" + estado);

            }
            sql = sql.concat(" ORDER BY e.tipo,e.marca  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, centro, servicio, true, true, aplicacionBean, false));
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

    public String getSiguienteInventario() {
        Connection connection = null;
        String valor = null;
        String sqlinv = null;
        try {
            connection = super.getConexionBBDD();
            sqlinv = " SELECT inventario FROM equipos WHERE NOT inventario IS NULL ORDER BY id desc ";

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sqlinv);
            if (resulSet.next()) {
                valor = resulSet.getString("inventario");
                if (Utilidades.isNumeric(valor)) {
                    valor = Long.toString(Long.parseLong(valor) + 1);
                }
            }
            statement.close();
            LOGGER.debug(sqlinv);
        } catch (SQLException e) {
            LOGGER.error(sqlinv + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return valor;
    }

    /*
    Métodos de datos genéricos
     */
    public ArrayList<DatoGenericoBean> getListaDatosGenericos(EquipoBean equipoBean) {
        ArrayList<DatoGenericoBean> lista = new ArrayList<>();
        ArrayList<String> listaTipoDatos = new ComboDao().getListaGruposRamaValor(ComboBean.TIPOEQUIPODATOS, equipoBean.getTipo(), 50);
        for (String tipo : listaTipoDatos) {
            DatoGenericoBean dato = new DatoGenericoBean();
            dato.setIdDatoEqipo(equipoBean.getId());
            dato.setTipoDato(tipo);
            dato.setValor(this.getValorDatoGenerico(dato));
            lista.add(dato);
        }
        return lista;
    }

    /**
     *
     * @param dato
     * @return
     */
    public String getValorDatoGenerico(DatoGenericoBean dato) {
        String valor = "";
        Connection connection = null;
        String sqlValor = "";
        try {
            connection = super.getConexionBBDD();
            sqlValor = " SELECT * FROM equiposdatos WHERE idequipo=" + dato.getIdDatoEqipo() + " AND "
                    + " tipodato='" + dato.getTipoDato() + "'";
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sqlValor);
            if (resulSet.next()) {
                valor = resulSet.getString("valor");
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
        return valor;
    }

    public String getValorDatoGenericoDemModelo(String tipodato, EquipoBean equipoBean) {
        String valor = "";
        Connection connection = null;
        String sqlValor = "";
        try {
            connection = super.getConexionBBDD();
            sqlValor = " SELECT  e.marca,e.modelo,d.* FROM equipos e "
                    + " JOIN equiposdatos d ON e.marca='" + equipoBean.getMarca() + "' AND  e.modelo='" + equipoBean.getModelo() + "' "
                    + " WHERE d.tipodato='" + tipodato + "' ORDER BY d.id desc";
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sqlValor);
            while (resulSet.next()) {
                valor = resulSet.getString("valor");
                if (valor != null && !valor.isEmpty()) {
                    break;
                }
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
        return valor;
    }

    public Boolean grabatValorDatoGenerico(DatoGenericoBean dato) {
        String valor = "";
        String sqlValor = "";
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sqlValor = " SELECT * FROM equiposdatos WHERE idequipo=" + dato.getIdDatoEqipo() + " AND "
                    + " tipodato='" + dato.getTipoDato() + "'";
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sqlValor);
            if (resulSet.next()) {
                sqlValor = " UPDATE equiposdatos SET valor=?,estado=?,fechacambio=?,usucambio=? WHERE idequipo=? AND tipodato=? ";
                PreparedStatement statementInsert = connection.prepareStatement(sqlValor);
                statementInsert.setString(1, dato.getValor());

                statementInsert.setInt(2, dato.getEstado());
                statementInsert.setLong(3, Utilidades.getFechaLong(dato.getFechacambio()));
                if (dato.getUsucambio() != null && dato.getUsucambio().getId() != null) {
                    statementInsert.setLong(4, dato.getUsucambio().getId());
                } else {
                    statementInsert.setNull(4, Types.INTEGER);
                }
                statementInsert.setLong(5, dato.getIdDatoEqipo());
                statementInsert.setString(6, dato.getTipoDato());
                insertadoBoolean = statementInsert.executeUpdate() > 0;
                statementInsert.close();
                LOGGER.debug(sqlValor);
            } else {
                dato.setId(getSiguienteId("equiposdatos"));
                sqlValor = " INSERT INTO  equiposdatos (id, valor, idequipo, tipodato,estado,fechacambio,usucambio) "
                        + "VALUES (?,?,?,?,?,?,?) ";
                PreparedStatement statementUpdate = connection.prepareStatement(sqlValor);
                statementUpdate.setLong(1, dato.getId());
                statementUpdate.setString(2, dato.getValor());
                statementUpdate.setLong(3, dato.getIdDatoEqipo());
                statementUpdate.setString(4, dato.getTipoDato());
                statementUpdate.setInt(5, dato.getEstado());
                statementUpdate.setLong(6, Utilidades.getFechaLong(dato.getFechacambio()));
                if (dato.getUsucambio() != null && dato.getUsucambio().getId() != null) {
                    statementUpdate.setLong(7, dato.getUsucambio().getId());
                } else {
                    statementUpdate.setNull(7, Types.INTEGER);
                }
                insertadoBoolean = statementUpdate.executeUpdate() > 0;
                statementUpdate.close();
                LOGGER.debug(sqlValor);
            }
            statement.close();
            LOGGER.debug(sqlValor);
        } catch (SQLException e) {
            LOGGER.error(sqlValor + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    public Boolean borraValorDatoGenerico(DatoGenericoBean dato) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        String sqlValor = "";
        try {
            connection = super.getConexionBBDD();
            sqlValor = " DELETE FROM  equiposdatos WHERE idequipo=? AND  tipo=? ";
            PreparedStatement statement = connection.prepareStatement(sqlValor);
            statement.setLong(1, dato.getEstado());
            statement.setString(2, dato.getValor());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sqlValor);
        } catch (SQLException e) {
            LOGGER.error(sqlValor + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

}
