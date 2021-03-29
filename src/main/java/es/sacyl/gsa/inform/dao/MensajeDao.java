package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.MensajeBean;
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
public class MensajeDao extends ConexionDao implements Serializable, ConexionInterface<MensajeBean> {

    private static final Logger LOGGER = LogManager.getLogger(AutonomiaDao.class);
    private static final long serialVersionUID = 1L;

    public MensajeDao() {
        super();
        sql = " SELECT * from mensajes WHERE 1=1";
    }

    @Override
    public MensajeBean getRegistroResulset(ResultSet rs) {

        MensajeBean mensajeBean = new MensajeBean();
        try {
            mensajeBean.setId(rs.getLong("id"));
            mensajeBean.setTipo(rs.getString("tipo"));
            mensajeBean.setDestinatarios(rs.getString("destinatarios"));
            mensajeBean.setContenido(rs.getString("contenido"));
            mensajeBean.setFicherosAdjuntos(rs.getString("ficherosadnuntos"));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return mensajeBean;

    }

    @Override
    public MensajeBean getPorCodigo(String codigo) {
        if (Utilidades.isNumeric(codigo)) {
            return getPorId(Long.parseLong(codigo));
        } else {
            return null;
        }
    }

    @Override
    public MensajeBean getPorId(Long id) {
        Connection connection = null;
        MensajeBean mensajeBean = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND id='" + id + "'");
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    mensajeBean = getRegistroResulset(resulSet);
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
        return mensajeBean;
    }

    @Override
    public boolean doGrabaDatos(MensajeBean mensajeBean) {
        boolean grabado = false;
        if (this.getPorId(mensajeBean.getId()) == null) {
            mensajeBean.setId(getSiguienteId("mensajes"));
            grabado = this.doInsertaDatos(mensajeBean);
        } else {
            grabado = this.doActualizaDatos(mensajeBean);
        }
        return grabado;
    }

    @Override
    public boolean doInsertaDatos(MensajeBean mensajeBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " INSERT INTO  mensajes  (id,tipo,destinatarios,asuntos,contenido,ficherosadjuntos) "
                + " VALUES (?,?,?,?,?,?)  ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, mensajeBean.getId());
            statement.setString(2, mensajeBean.getTipo());
            statement.setString(3, mensajeBean.getDestinatarios());
            if (mensajeBean.getAsuntos() != null && !mensajeBean.getAsuntos().isEmpty()) {
                statement.setString(4, mensajeBean.getAsuntos());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (mensajeBean.getContenido() != null && !mensajeBean.getContenido().isEmpty()) {
                statement.setString(5, mensajeBean.getContenido());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            if (mensajeBean.getFicherosAdjuntos() != null && !mensajeBean.getFicherosAdjuntos().isEmpty()) {
                statement.setString(6, mensajeBean.getFicherosAdjuntos());
            } else {
                statement.setNull(6, Types.CHAR);
            }
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public boolean doActualizaDatos(MensajeBean mensajeBean
    ) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " UPDATE   mensajes  SET tipo=?,destinatarios=?,asuntos=?,contenido=?,ficherosadjuntos=?"
                + "WHERE id=? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mensajeBean.getTipo());
            statement.setString(2, mensajeBean.getDestinatarios());
            if (mensajeBean.getAsuntos() != null && !mensajeBean.getAsuntos().isEmpty()) {
                statement.setString(3, mensajeBean.getAsuntos());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (mensajeBean.getContenido() != null && !mensajeBean.getContenido().isEmpty()) {
                statement.setString(4, mensajeBean.getContenido());
            } else {
                statement.setNull(4, Types.CHAR);
            }
            if (mensajeBean.getFicherosAdjuntos() != null && !mensajeBean.getFicherosAdjuntos().isEmpty()) {
                statement.setString(5, mensajeBean.getFicherosAdjuntos());
            } else {
                statement.setNull(5, Types.CHAR);
            }
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public boolean doBorraDatos(MensajeBean mensajeBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " DELETE  * FROM mensajes WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, mensajeBean.getId());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    @Override
    public ArrayList<MensajeBean> getLista(String texto) {
        Connection connection = null;
        ArrayList<MensajeBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" ORDER BY id  ");
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
