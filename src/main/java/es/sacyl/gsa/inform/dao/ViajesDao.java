package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.ViajeBean;
import es.sacyl.gsa.inform.bean.ViajeCentroBean;
import es.sacyl.gsa.inform.bean.ViajeTecnicoBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class ViajesDao extends ConexionDao implements Serializable {

    private static final Logger LOGGER = LogManager.getLogger(ViajesDao.class);
    private static final long serialVersionUID = 1L;

    public ViajesDao() {
        super();
    }

    /**
     *
     * @param rs El objeto ResultSet que retorna la ejecuación de la sentencia
     * SELECT
     * @return Un objeto ViajeBean con los valores recuperados por ResulSet
     */
    private ViajeBean getRegistroResulset(ResultSet rs) {
        ViajeBean viajeBean = new ViajeBean();
        try {
            viajeBean.setId(rs.getLong("id"));
            viajeBean.setSalida(Utilidades.getFechaHoraLocalDateTime(rs.getLong("fecha"), rs.getInt("horasalida")));
            viajeBean.setLlegada(Utilidades.getFechaHoraLocalDateTime(rs.getLong("fecha"), rs.getInt("horaLlegada")));
            viajeBean.setMatricula(rs.getString("matricula"));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return viajeBean;
    }

    /**
     *
     * @param id Id del viaje que se desea recuperar
     * @return Objeto ViajeBean Si viajeCompleto == true con todos los datos del
     * viajes, viajescentros, viajestecnicos Si viajeCompleto == false sólo
     * datos de la tabla viajes
     */
    public ViajeBean getPorId(Long id, Boolean viajeCompleto) {
        Connection connection = null;
        ViajeBean viajeBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM viajes  WHERE  1=1 ";

            if (id != null) {
                sql = sql.concat(" AND id=" + id);
            }
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    viajeBean = getRegistroResulset(resulSet);
                    /**
                     * Si la llamada es del método GrabaDatos para verificar si
                     * el id existe o no no recupera tecnicos ni centros y se
                     * evitan sentencias select innecesarias
                     */
                    if (viajeCompleto == Boolean.FALSE) {
                        viajeBean.setListaCentros(getViajeCentros(viajeBean));
                        viajeBean.setListaTecnicos(getViajeTecnicos(viajeBean));
                    }
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
        return viajeBean;
    }

    /**
     *
     * @param viajeBean que se desea almacenar de forma persistente en la base
     * de datos
     * @return
     *
     * Comprueba si viajeBean.id existe ya en la tabla Si no existe lo inserta y
     * si exite lo actualiza
     */
    public boolean doGrabaDatos(ViajeBean viajeBean) {
        boolean actualizado = false;
        if (viajeBean != null && viajeBean.getId() != null && getPorId(viajeBean.getId(), Boolean.FALSE) == null) {
            actualizado = this.doInsertaDatos(viajeBean);
        } else {
            actualizado = this.doActualizaDatos(viajeBean);
        }
        return actualizado;
    }

    public boolean doInsertaDatos(ViajeBean viajeBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            viajeBean.setId(this.getSiguienteId("viajes"));
            sql = " INSERT INTO  viajes  (id,fecha,horasalida,horallegada,matricula,estado,usucambio,fechacambio) " + " VALUES "
                    + "(" + viajeBean.getId() + ",'" + Utilidades.getFechaLong(viajeBean.getSalida()) + "','"
                    + Utilidades.getHoraInt(viajeBean.getSalida())
                    + "','" + Utilidades.getHoraInt(viajeBean.getLlegada()) + "','" + viajeBean.getMatricula() + "'"
                    + ", '" + ConexionDao.BBDD_ACTIVOSI + "','"
                    + usuarioBean.getId() + "','" + Utilidades.getFechaActualLong() + "')";
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                //    doInsertaCentros(viajeBean);
                //   doInsertaTecnicos(viajeBean);
                insertadoBoolean = true;
            }
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

    public boolean doActualizaDatos(ViajeBean viajeBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   viajes  SET fecha='" + Utilidades.getFechaLong(viajeBean.getSalida()) + "'"
                    + ",horasalida='" + Utilidades.getHoraInt(viajeBean.getSalida()) + "', horallegada="
                    + Utilidades.getHoraInt(viajeBean.getLlegada())
                    + ",matricula='" + viajeBean.getMatricula() + "'"
                    + ",estado='" + ConexionDao.BBDD_ACTIVOSI + "'"
                    + ",usucambio='" + usuarioBean.getId() + "'"
                    + ",fechacambio=" + Utilidades.getFechaActualLong()
                    + " WHERE id='" + viajeBean.getId() + "'";

            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                //   doBorraCentros(viajeBean.getId());
                //  doInsertaCentros(viajeBean);
                //  doBorraTecnicos(viajeBean.getId());
                //  doInsertaCentros(viajeBean);
                insertadoBoolean = true;
            }
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));

        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    public boolean doBorraDatos(ViajeBean viajeBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE viajes SET  " + ",estado='" + ConexionDao.BBDD_ACTIVOSI + "'"
                    + ",usucambio='" + usuarioBean.getId() + "'"
                    + ",fechacambio=" + Utilidades.getFechaActualLong() + "'"
                    + " WHERE id='" + viajeBean.getId() + "'";
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
     * @param desde Fecha inicial desde la que se quieren recuperar los viajes
     * @param hasta Fecha final hasta la que se quieren recuperar los viajes
     * @param centroBean Centro del que se quieren recuerar los viajes
     * @param usuarioBean Ténico del que se quieren recuperar los viajes
     * @return
     */
    public ArrayList<ViajeBean> getListaViajes(LocalDate desde, LocalDate hasta, CentroBean centroBean, UsuarioBean usuarioBean, Integer activo) {
        Connection connection = null;
        ArrayList<ViajeBean> listaViajes = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = "   SELECT UNIQUE  v.* FROM viajes  v "
                    + "LEFT  JOIN viajescentros c On c.idviaje = v.id "
                    + "LEFT  JOIN viajestecnicos t On t.idviaje=v.id WHERE  estado= " + activo;
            if (desde != null) {
                sql = sql.concat(" AND v.fecha>=" + Utilidades.getFechaLong(desde));
            }
            if (hasta != null) {
                sql = sql.concat(" AND v.fecha<=" + Utilidades.getFechaLong(hasta));
            }
            if (centroBean != null) {
                sql = sql.concat(" AND c.idcentro =" + centroBean.getId());
            }
            if (usuarioBean != null) {
                sql = sql.concat(" AND t.idtecnico =" + usuarioBean.getId());
            }
            sql = sql.concat(" ORDER BY fecha,horasalida  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                ViajeBean viajeBean = getRegistroResulset(resulSet);
                viajeBean.setListaCentros(getViajeCentros(viajeBean));
                viajeBean.setListaTecnicos(getViajeTecnicos(viajeBean));
                listaViajes.add(viajeBean);
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
        return listaViajes;
    }

    /**
     *
     * @param id Id del viaje del cual se quieren recuperar los datos
     * @return Un rrayList<ViajeCentroBean> con los datos asociados a viaje.
     */
    public ArrayList<ViajeCentroBean> getViajeCentros(ViajeBean viajeBean) {
        ArrayList<ViajeCentroBean> listaCentros = new ArrayList<>();
        Connection connection = null;
        try {
            connection = super.getConexionBBDD();
            sql = "   SELECT  * FROM viajescentros  WHERE idviaje= " + viajeBean.getId();
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                ViajeCentroBean viajeCentroBean = new ViajeCentroBean();
                viajeCentroBean.setId(resulSet.getLong("id"));
                viajeCentroBean.setPreparacion(resulSet.getString("preparacion"));
                viajeCentroBean.setActuacion(resulSet.getString("actuacion"));
                viajeCentroBean.setCentroDestino(new CentroDao().getPorId(resulSet.getLong("idcentro")));
                viajeCentroBean.setIdViaje(resulSet.getLong("idViaje"));
                listaCentros.add(viajeCentroBean);
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
        return listaCentros;
    }

    /**
     *
     * @param id El id del viaje cuyos técnicos se quieren recuperar
     * @return Un ArrayList de Usuarios asociados al viaje
     */
    public ArrayList<ViajeTecnicoBean> getViajeTecnicos(ViajeBean viajeBean) {
        ArrayList<ViajeTecnicoBean> listaTecnicos = new ArrayList<>();
        Connection connection = null;
        try {
            connection = super.getConexionBBDD();
            sql = "   SELECT  * FROM viajestecnicos  WHERE idviaje= " + viajeBean.getId();
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                ViajeTecnicoBean viajeTecnico = new ViajeTecnicoBean();
                viajeTecnico.setId(resulSet.getLong("id"));
                viajeTecnico.setViaje(viajeBean);
                UsuarioBean tecnico = new UsuarioDao().getPorId(resulSet.getLong("idtecnico"));
                viajeTecnico.setTecnico(tecnico);

                listaTecnicos.add(viajeTecnico);
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
        return listaTecnicos;
    }

    /**
     *
     * @param id
     * @return Boora todos los centros asociados al viaje
     */
    public Boolean doBorraCentros(Long id) {
        Connection connection = null;
        Boolean booradoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  viajesCentros WHERE idviaje='" + id + "'";
            Statement statement = connection.createStatement();
            booradoBoolean = statement.execute(sql);
            booradoBoolean = true;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return booradoBoolean;
    }

    public Boolean doBorraUnCentro(Long id, ViajeCentroBean viajeCentroBean) {
        Connection connection = null;
        Boolean booradoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  viajesCentros WHERE idviaje='" + id + "'  AND idcenro='" + viajeCentroBean.getId() + "'";
            Statement statement = connection.createStatement();
            booradoBoolean = statement.execute(sql);
            booradoBoolean = true;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return booradoBoolean;
    }

    /**
     *
     * @param id
     * @return Boora todos los tecnicos asociados al viaje
     */
    public Boolean doBorraTecnicos(Long id) {
        Connection connection = null;
        Boolean booradoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  viajestecnicos WHERE idviaje='" + id + "'";
            Statement statement = connection.createStatement();
            booradoBoolean = statement.execute(sql);
            booradoBoolean = true;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return booradoBoolean;
    }

    /**
     *
     * @param id
     * @return Boora un tecnicos asociados al viaje id
     */
    public Boolean doBorraUnTecnico(ViajeTecnicoBean viajeTecnicoBean) {
        Connection connection = null;
        Boolean booradoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  viajestecnicos WHERE idviaje='" + viajeTecnicoBean.getViaje().getId() + "' AND idtecnico='" + viajeTecnicoBean.getTecnico().getId() + "'";
            Statement statement = connection.createStatement();
            booradoBoolean = statement.execute(sql);
            booradoBoolean = true;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return booradoBoolean;
    }

    /*
    public Boolean doInsertaCentros(ViajeBean viajeBean) {
        Boolean insertadoBoolean = false;
        try {
            for (ViajeCentroBean viajeCentroBean : viajeBean.getListaCentros()) {
                doInsertaUnCentros(viajeBean, viajeCentroBean);
            }
            insertadoBoolean = true;
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return insertadoBoolean;
    }
     */
    public Boolean doInsertaUnCentros(ViajeCentroBean viajeCentroBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            viajeCentroBean.setId(super.getSiguienteId("viajescentros"));
            sql = " INSERT into   viajescentros (id,idviaje,idcentro,preparacion,actuacion) values "
                    + " ('" + viajeCentroBean.getId() + "'"
                    + ",'" + viajeCentroBean.getIdViaje() + "' "
                    + ",'" + viajeCentroBean.getCentroDestino().getId() + "' "
                    + ",'" + (viajeCentroBean.getPreparacion() == null ? "  " : viajeCentroBean.getPreparacion()) + "' "
                    + ",'" + (viajeCentroBean.getActuacion() == null ? "  " : viajeCentroBean.getActuacion()) + "' "
                    + " ) ";
            LOGGER.debug(sql);
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            statement.close();
            insertadoBoolean = true;
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    public Boolean doActualizaUnCentro(ViajeCentroBean viajeCentroBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " update viajescentros set " + "idviaje='" + viajeCentroBean.getIdViaje() + "'"
                    + ",idcentro='" + viajeCentroBean.getCentroDestino().getId() + "'"
                    + ",preparacion='" + (viajeCentroBean.getPreparacion() == null ? " " : viajeCentroBean.getPreparacion()) + "'"
                    + ",actuacion='" + (viajeCentroBean.getActuacion() == null ? " " : viajeCentroBean.getActuacion()) + "'"
                    + " WHERE id='" + viajeCentroBean.getId() + "'";
            LOGGER.debug(sql);
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            statement.close();
            insertadoBoolean = true;
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
     * @param viajeBean
     * @param tecnico
     * @return Inserta un técnico en la tabla asociado al viaje
     */
    public Boolean doInsertaUnTecnico(ViajeBean viajeBean, UsuarioBean tecnico) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            Long id = super.getSiguienteId("viajestecnicos");
            sql = " INSERT into   viajestecnicos (id,idviaje,idtecnico) values "
                    + " ('" + id + "'"
                    + ",'" + viajeBean.getId() + "' "
                    + ",'" + tecnico.getId() + "' "
                    + " ) ";
            LOGGER.debug(sql);
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            statement.close();
            insertadoBoolean = true;
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }
}
