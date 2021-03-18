package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author juannietopajares
 */
public class AutonomiaDao extends ConexionDao implements Serializable, ConexionInterface<AutonomiaBean> {

    private static final Logger LOGGER = LogManager.getLogger(AutonomiaDao.class);
    private static final long serialVersionUID = 1L;

    public AutonomiaDao() {
        super();
        sql = " SELECT a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado  "
                + " FROM CAUTONOM a WHERE  1=1 ";
    }

    /**
     *
     * @param rs
     * @return A partir del ResulSet que a generado la ejecución de un sql
     * hacemos la conversion del modelo relacional de base de datos al modelo de
     * ojetos Bean de la aplicaicón.
     *
     */
    @Override
    public AutonomiaBean getRegistroResulset(ResultSet rs) {
        AutonomiaBean autonomiaBean = new AutonomiaBean();
        try {
            autonomiaBean.setCodigo(rs.getString("autonomiacodigo"));
            autonomiaBean.setNombre(rs.getString("autonomianombre"));
            autonomiaBean.setEstado(rs.getInt("autonomiaestado"));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return autonomiaBean;
    }

    /**
     *
     * @param codigo
     * @return Para el código que se pasa por parámetro devuelve el objeto Bean
     * recuperando de la base de datos el registro correspondiente a ese código
     * Si el código no existe en la base de datos o se le pasa null por
     * parámetro retorna un null
     *
     */
    @Override
    public AutonomiaBean getPorCodigo(String codigo) {
        Connection connection = null;
        AutonomiaBean autonomiaBean = null;
        if (codigo != null) {
            try {
                connection = super.getConexionBBDD();

                sql = sql.concat(" AND codigo='" + codigo + "'");

                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        autonomiaBean = getRegistroResulset(resulSet);
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
        return autonomiaBean;
    }

    /**
     *
     * @param autonomiaBean
     * @return Método común para hacer inser y update en la base de datos Como
     * parámetro se pasa el ojbeto bean que queremos hacer persisntencia en la
     * base de datos Verifica si existe en la base de datos: - si existe
     * actualiza - Sin no existe inserta un registro nuevo en la tabla
     */
    @Override
    public boolean doGrabaDatos(AutonomiaBean autonomiaBean) {
        boolean grabado = false;
        if (this.getPorCodigo(autonomiaBean.getCodigo()) == null) {
            grabado = this.doInsertaDatos(autonomiaBean);
        } else {
            grabado = this.doActualizaDatos(autonomiaBean);
        }
        return grabado;
    }

    /**
     *
     * @param autonomiaBean
     * @return Para el objeto bean del parámetro inserta en la base de datos una
     * fila asociada al objeto bean Pasa el modelo bean al model relacional
     * asociando a cada parámetro sql de inser el atributo correspondiente del
     * bean
     *
     */
    @Override
    public boolean doInsertaDatos(AutonomiaBean autonomiaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " INSERT INTO  CAUTONOM  (codigo,nombre,estado) "
                + " VALUES (?,?,?)  ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, autonomiaBean.getCodigo());
            statement.setString(2, autonomiaBean.getNombre());
            statement.setInt(3, autonomiaBean.getEstado());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(AutonomiaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param autonomiaBean
     * @return Acutaliza el registro en la tabla para el código del bean pasado
     * como parámetro
     */
    @Override
    public boolean doActualizaDatos(AutonomiaBean autonomiaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        connection = super.getConexionBBDD();
        sql = " UPDATE   CAUTONOM  SET nombre=?,estado=?  "
                + "WHERE codigo=? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, autonomiaBean.getNombre());
            statement.setInt(2, autonomiaBean.getEstado());
            statement.setString(3, autonomiaBean.getCodigo());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
        } catch (SQLException ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param autonomiaBean
     * @return Para el parámetro hace un borrado lógico poniendo a 0 el campo
     * estado
     */
    @Override
    public boolean doBorraDatos(AutonomiaBean autonomiaBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE  CAUTONOM SET estado=" + ConexionDao.BBDD_ACTIVONO + "WHERE codigo='" + autonomiaBean.getCodigo() + "'";
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
     * @return Recupera la lista de beans de la base de datos que cumplen el
     * critero del texto
     */
    @Override
    public ArrayList<AutonomiaBean> getLista(String texto) {
        return getLista(texto, null);
    }

    public ArrayList<AutonomiaBean> getLista(String texto, Integer estado) {
        Connection connection = null;
        ArrayList<AutonomiaBean> listautonomias = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            if (estado != null) {
                sql = sql.concat(" AND estado=" + estado);
            }
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(nombre) like'%" + texto.toUpperCase() + "%'  OR   UPPER(codigo) like'%" + texto.toUpperCase() + "%' )");
            }
            sql = sql.concat(" ORDER BY nombre  ");
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

    /**
     *
     * @param id
     * @return Como la tabla no tiene campo id lo pasa a string y llama al
     * método getPorcodigo
     */
    @Override
    public AutonomiaBean getPorId(Long id) {
        String codigo = Long.toString(id);
        return getPorCodigo(codigo);
    }

}
