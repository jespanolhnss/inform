package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.LopdSujetoBean;
import es.sacyl.gsa.inform.bean.LopdTipoBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LopdTipoDao extends ConexionDao {

    private static final Logger logger = LogManager.getLogger(LopdTipoDao.class);

    public LopdTipoDao() {
        super();
    }

    public LopdTipoBean getRegistroResulset(ResultSet rs) {
        LopdTipoBean incidenciaTipos = new LopdTipoBean();
        try {
            incidenciaTipos.setId(rs.getLong("id"));
            incidenciaTipos.setDescripcion(rs.getString("descripcion"));
            switch (rs.getInt("sujeto")) {
                case 1:
                    incidenciaTipos.setSujeto(LopdSujetoBean.SUJETO_PACIENTE);
                    break;
                case 2:
                    incidenciaTipos.setSujeto(LopdSujetoBean.SUJETO_TRABAJADOR);
                    break;
                case 3:
                    incidenciaTipos.setSujeto(LopdSujetoBean.SUJETO_OTROS);
                    break;
                case 4:
                    incidenciaTipos.setSujeto(LopdSujetoBean.SUJETO_USUARIO);
                    break;
                default:
                    break;
            }
            incidenciaTipos.setMailReponsable(rs.getBoolean("mailresponsable"));
            incidenciaTipos.setEstado(rs.getBoolean("estado"));
            incidenciaTipos.setUsucambio(new UsuarioDao().getPorId(rs.getLong("usucambio")));
            LocalDate fecha = Utilidades.getFechaLocalDate(rs.getLong("fechacambio"));
            if (fecha != null) {
                incidenciaTipos.setFechaCambio(fecha);
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(LopdTipoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return incidenciaTipos;
    }

    public LopdTipoBean getPorId(Long id) {
        Connection connection = null;
        LopdTipoBean incidenciaTipos = null;

        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM lopd_tipos WHERE  estado=" + ConexionDao.BBDD_ACTIVOSI + " AND id=" + id;
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            if (resulSet.next()) {
                incidenciaTipos = getRegistroResulset(resulSet);
            }
            statement.close();
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return incidenciaTipos;
    }

    /**
     *
     * @param sujeto
     * @param usuario
     * @param cadena
     * @param estado
     * @return
     */
    public ArrayList<LopdTipoBean> getListaIncidenciaTipos(LopdSujetoBean sujeto, UsuarioBean usuario, String cadena, Boolean estado) {
        Connection connection = null;
        ArrayList<LopdTipoBean> lista = new ArrayList<>();

        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM  lopd_tipos WHERE 1=1";
            if (sujeto != null) {
                sql = sql.concat(" AND sujeto=" + sujeto.getId());
            }
            if (usuario != null) {
                sql = sql.concat(" AND usucambio=" + usuario.getId());
            }
            if (cadena != null) {
                sql = sql.concat(" AND  UPPER(descripcion) like '%" + cadena.toUpperCase() + "%'");
            }
            if (estado != null && estado == Boolean.TRUE) {
                sql = sql.concat(" AND estado=" + ConexionDao.BBDD_ACTIVOSI);
            }
            if (estado != null && estado == Boolean.FALSE) {
                sql = sql.concat(" AND estado=" + ConexionDao.BBDD_ACTIVONO);
            }
            sql = sql.concat(" ORDER BY descripcion");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                LopdTipoBean incidenciaTipos = getRegistroResulset(resulSet);
                lista.add(incidenciaTipos);
            }
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    public boolean grabaDatos(LopdTipoBean incidenciaTipos) {
        boolean actualizado = false;

        if (incidenciaTipos.getId().equals(new Long(0))) {
            actualizado = this.insertaDatos(incidenciaTipos);
        } else {
            actualizado = this.actualizaDatos(incidenciaTipos);
        }
        return actualizado;
    }

    public boolean insertaDatos(LopdTipoBean incidenciaTipos) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();
            Long id = this.getSiguienteId("lopd_tipos");
            incidenciaTipos.setId(id);
            int valor;
            if (incidenciaTipos.getMailReponsable() == true) {
                valor = 1;
            } else {
                valor = 0;
            }
            sql = " INSERT INTO  lopd_tipos (id,descripcion,sujeto,mailresponsable,estado,usucambio,fechacambio) "
                    + " VALUES (" + incidenciaTipos.getId() + ",'" + incidenciaTipos.getDescripcion() + "' " + ","
                    + incidenciaTipos.getSujeto().getId() + "," + valor + "," + ConexionDao.BBDD_ACTIVOSI + ","
                    + usuario.getId() + "," + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ")";
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
            }
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    public boolean actualizaDatos(LopdTipoBean incidenciaTipos) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();
            int valor = incidenciaTipos.getMailReponsable() ? 1 : 0;
            sql = " UPDATE   lopd_tipos SET descripcion='" + incidenciaTipos.getDescripcion() + "',sujeto='"
                    + incidenciaTipos.getSujeto().getId() + "',mailresponsable='" + valor + "', usucambio="
                    + Long.toString(usuario.getId()) + ",  fechacambio=" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + ", estado=" + ConexionDao.BBDD_ACTIVOSI + " WHERE id=" + incidenciaTipos.getId();
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
            }
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    public boolean borraDatos(LopdTipoBean incidenciaTipos) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();
            /*
            sql = " UPDATE   lopd_tipos SET usucambio=" + Long.toString(usuario.getId()) + ",  fechacambio="
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ", estado=" + ConexionDao.BBDD_ACTIVONO
                    + " WHERE id=" + incidenciaTipos.getId();
             */
            sql = " UPDATE   lopd_tipos SET usucambio=" + Long.toString(usuario.getId()) + ",  fechacambio="
                    + Utilidades.getFechaLong(incidenciaTipos.getFechaCambio()) + ", estado=" + ConexionDao.BBDD_ACTIVONO
                    + " WHERE id=" + incidenciaTipos.getId();
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
            }
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

}
