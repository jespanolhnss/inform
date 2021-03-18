/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdNotaBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class LopdNotaDao extends ConexionDao {

    private static final Logger logger = LogManager.getLogger(LopdNotaDao.class);

    public Long doActualiza(LopdIncidenciaBean incidencia, LopdNotaBean nota) {
        Connection connection = null;
        Boolean insertado = false;
        try {
            connection = super.getConexionBBDD();

            UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            sql = " UPDATE lopd_notas SET texto=?,fechacambio=?,usucambio=?,estado=?  WHERE  id=?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, nota.getDescripcion());
            statement.setLong(2, Utilidades.getFechaLong(nota.getFecha()));
            statement.setLong(3, usuario.getId());
            statement.setInt(4, ConexionDao.BBDD_ACTIVOSI);
            statement.setLong(5, nota.getId());
            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        if (insertado == true) {
            return nota.getId();
        } else {
            return new Long(0);
        }
    }

    /**
     *
     * @param incidencia
     * @param nota
     * @return
     */
    public Long grabaDatos(LopdIncidenciaBean incidencia, LopdNotaBean nota) {
        boolean actualizado = false;

        if (nota.getId().equals(new Long(0))) {
            nota.setId(getSiguienteId("lopd_notas"));
            return this.doInserta(incidencia, nota);
        } else {
            return this.doActualiza(incidencia, nota);
        }
    }

    public Long doInserta(LopdIncidenciaBean incidencia, LopdNotaBean nota) {
        Connection connection = null;
        Boolean insertado = false;
        UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
        try {
            connection = super.getConexionBBDD();
            nota.setId(this.getSiguienteId("lopd_notas"));

            sql = " INSERT INTO lopd_notas (id,idincidencia,texto,fechacambio,usucambio,estado) "
                    + " VALUES (?,?,?,?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, nota.getId());
            statement.setLong(2, incidencia.getId());
            statement.setString(3, nota.getDescripcion());
            statement.setLong(4, Utilidades.getFechaLong(nota.getFecha()));
            statement.setLong(5, usuario.getId());
            statement.setInt(6, ConexionDao.BBDD_ACTIVOSI);
            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(sql + " valores " + nota.getId() + "," + incidencia.getId() + "," + nota.getDescripcion() + ","
                    + Utilidades.getFechaActualLong() + "," + usuario.getId() + ","
                    + ConexionDao.BBDD_ACTIVOSI);
        } catch (SQLException e) {
            logger.error(sql + " valores " + nota.getId() + "," + incidencia.getId() + "," + nota.getDescripcion() + ","
                    + Utilidades.getFechaActualLong() + "," + usuario.getId() + ","
                    + ConexionDao.BBDD_ACTIVOSI);
            logger.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        if (insertado == true) {
            return nota.getId();
        } else {
            return new Long(0);
        }
    }

    public ArrayList<LopdNotaBean> getNostasIncidencia(LopdIncidenciaBean incidencia) {
        Connection connection = null;
        ArrayList<LopdNotaBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM lopd_notas WHERE estado= " + ConexionDao.BBDD_ACTIVOSI + " AND idincidencia= "
                    + incidencia.getId() + " order by fechacambio";
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    LopdNotaBean nota = new LopdNotaBean();
                    nota.setId(resulSet.getLong("id"));
                    nota.setIdIncidenciaLong(resulSet.getLong("idincidencIa"));
                    nota.setDescripcion(resulSet.getString("texto"));
                    nota.setEstado(resulSet.getInt("estado"));
                    nota.setFecha(Utilidades.getFechaLocalDate(resulSet.getLong("fechacambio")));
                    nota.setUsucambio(new UsuarioDao().getPorId(resulSet.getLong("usucambio")));
                    lista.add(nota);
                }
                statement.close();
            }
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);

        }
        return lista;
    }

}
