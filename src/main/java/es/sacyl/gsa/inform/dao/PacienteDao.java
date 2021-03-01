package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.PacienteBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacienteDao extends ConexionDao {

    private static final Logger logger = LogManager.getLogger(PacienteDao.class);

    public PacienteDao() {
        super();
    }

    public PacienteBean getPacienteNhc(String numerohc) {
        Connection connection = null;
        PacienteBean paciente = null;
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM pacientes WHERE numerohc='" + numerohc + "'";
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            if (resulSet.next()) {
                paciente = getRegistroResulset(resulSet);
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
        return paciente;
    }

    public PacienteBean getRegistroResulset(ResultSet rs) {
        PacienteBean paciente = new PacienteBean();
        try {
            paciente.setId(rs.getLong("id"));
            paciente.setIdJimena(rs.getLong("idjimena"));
            paciente.setNumerohc(rs.getString("numerohc"));
            paciente.setApellidosnombre(rs.getString("apellidosnombre"));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return paciente;
    }

    public Long insertaPaciente(PacienteBean paciente) {
        Connection connection = null;
        boolean insertado = false;
        Long id = null;
        try {
            connection = super.getConexionBBDD();
            id = new ConexionDao().getSiguienteId("pacientes");
            paciente.setId(id);
            paciente.setEstado(UsuarioBean.USUARIO_ACTIVO);
            // ID APELLIDOSNOMBRE NUMEROHC ESTADO FECHACAMBIO USUCAMBIO IDJIMENA
            sql = " INSERT INTO pacientes (id,apellidosnombre,numerohc,estado,fechacambio,usucambio,idjimena) "
                    + " VALUES (?,?,?,?,?,?,?)  ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, paciente.getId());
            statement.setString(2, paciente.getApellidosnombre());
            statement.setString(3, paciente.getNumerohc());
            statement.setInt(4, ConexionDao.BBDD_ACTIVOSI);
            statement.setLong(5, Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
            statement.setLong(6,
                    ((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME)).getId());
            statement.setLong(7, paciente.getIdJimena());
            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(" INSERT INTO pacientes (id,apellidosnombre,numerohc,estado,fechacambio,usucambio,idjimena) "
                    + " VALUES (" + paciente.getId() + ",'" + paciente.getApellidosnombre() + "','"
                    + paciente.getNumerohc() + "'," + ConexionDao.BBDD_ACTIVOSI + ","
                    + Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))) + ",'"
                    + ((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME)).getId() + ",'"
                    + paciente.getIdJimena() + ")");

        } catch (SQLException e) {
            logger.error(" INSERT INTO pacientes (id,apellidosnombre,numerohc,estado,fechacambio,usucambio,idjimena) "
                    + " VALUES (" + paciente.getId() + "," + paciente.getApellidosnombre() + "','"
                    + paciente.getNumerohc() + "'," + ConexionDao.BBDD_ACTIVOSI + ","
                    + Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))) + ",'"
                    + ((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME)).getId() + ",'"
                    + paciente.getIdJimena() + ")", e);

        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return id;
    }

}
