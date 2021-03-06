package es.sacyl.gsa.inform.dao;

/**
 *
 * @author 06551256M
 */
import com.vaadin.flow.component.notification.Notification;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class ConexionDao.
 *
 * @author JuanNieto
 *
 * Inventario de tablas y alias para mantener
 *
 * autonomia a
 *
 * aplicacion ap
 *
 * aplicacionesPerfiles appp ap
 *
 * centros c
 *
 * centrosficheros cf
 *
 * centrostipo ct
 *
 * combos
 *
 * equipos e
 *
 * funcionalidad
 *
 * GERENCIA g
 *
 * gfh gfh
 *
 * localidad l
 *
 * lopd_documentos
 *
 * lopd_incidencias
 *
 * lopd_notas
 *
 * lopd_tipos
 *
 * nivelesatencion n
 *
 * NIVELESATENCIONTIPO na
 *
 * pacientes
 *
 * parametros
 *
 *
 * proveedores prvee
 *
 * PROVINCIA p
 *
 * ubicaciones u
 *
 * usuarioscategorias uc
 *
 * usuarios usu
 *
 * viajes
 *
 * ZONAS z
 *
 *
 *
 */
public class ConexionDao implements Serializable {

    private static final long serialVersionUID = 1L;

    private DataSource dataSource = null;

    private static final Logger LOGGER = LogManager.getLogger(ConexionDao.class);

    private InitialContext ctx;

    protected String persistencia = null;

    protected String sql;

    public final static String ERROR_BBDD_SIN_CONEXION = "No se ha podido establecer la conexión con la base de datos ";

    public final static String ERROR_BBDD_SQL = "Error en sentencia SQL. ";

    public final static String ERROR_CLOSE_BBDD_SQL = "Error cerrando conexión. ";

    public final static String ERROR_BBDD_CONTEXTO = "Error iniciando contexto.";

    public static int BBDD_ACTIVOSI = 1;
    public static int BBDD_ACTIVONO = 0;

    public DateTimeFormatter formatterdd_mm_yyyy = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public DateTimeFormatter formatterdd_mm_yyyy_hh_mm = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    //protected UsuarioBean usuarioBean;
    /**
     * asigna valor al atributo usuario con el valor almacenado en la variable
     * sesion
     */
    public ConexionDao() {

    }

    /**
     * Gets the conexion BBDD.
     *
     * @return the conexion BBDD
     */
    public Connection getConexionBBDD() {
        try {
            if (dataSource == null) {
                ctx = new InitialContext();
                if (ctx == null) {
                    throw new Exception(ConexionDao.ERROR_BBDD_CONTEXTO);
                } else {
                    dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/dataSourceHnss");
                    if (dataSource != null) {
                        return dataSource.getConnection();
                    } else {
                        LOGGER.error(ConexionDao.ERROR_BBDD_SIN_CONEXION);
                        (new Notification("Error sin conexión a la base de datos", 9000, Notification.Position.MIDDLE)).open();
                    }
                }
            } else {
                return dataSource.getConnection();
            }
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
            (new Notification("Error conexion a la base de datos " + e.getMessage(), 9000, Notification.Position.MIDDLE)).open();
        }
        return null;
    }

    /**
     *
     * @param tabla
     * @return
     *
     * Si existe una secuencia con el patron SEC_ID_nombre_tabla
     *
     * retorna la secuenta
     *
     * si no recupera el siguiente id de la tabla
     *
     * Si es el primer regisgtro por que la tabla esta vacía recupera un 1
     *
     */
    public Long getSiguienteId(String tabla) {
        Connection connection = null;
        Long id = new Long(1);
        try {
            connection = this.getConexionBBDD();
            if (existeSecuencia(tabla) == true) {
                sql = " select SEC_ID_" + tabla + ".nextval as id from dual ";
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        id = resulSet.getLong("id");
                    }
                    statement.close();
                }
            } else if (tabla.substring(0, 2).toLowerCase().equals("dw") || tabla.toLowerCase().equals("combos")) {
                sql = " select SECUENCIAIDS.nextval as id from dual ";
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        id = resulSet.getLong("id");
                    }
                    statement.close();
                }
            } else {
                LOGGER.debug("No hay secuencia para la tabla " + tabla + " Se recupera el siguiente id ");
                sql = " SELECT max(id) +1  as id FROM  " + tabla;
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        id = resulSet.getLong("id");
                        if (id.equals(new Long(0))) {
                            id = new Long(1);
                        }
                    }
                    statement.close();
                }
            }
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            doCierraConexion(connection);
        }
        return id;
    }

    /**
     *
     * @param connection
     */
    public void doCierraConexion(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
    }

    /**
     * Método para verificar que la coenxión con la BBDD está activa
     *
     * @return
     */
    public Boolean isTestConexion() {
        Connection connection = null;
        Boolean resultado = false;
        try {
            connection = getConexionBBDD();
            sql = " SELECT * FROM dual ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                resultado = statement.executeUpdate() > 0;
                statement.close();
            }
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql, Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return resultado;
    }

    public Boolean existeSecuencia(String tabla) {
        Connection connection = null;
        Boolean existe = false;
        String secuencia = "SEC_ID_" + tabla.toUpperCase();
        try {
            connection = this.getConexionBBDD();
            //  sql = " SELECT max(id) +1  as id FROM  " + tabla;
            sql = "  SELECT sequence_name FROM all_sequences  "
                    + " WHERE  sequence_name = '" + secuencia + "'";
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    existe = true;
                }
            }
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));

        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            doCierraConexion(connection);
        }
        return existe;
    }
}
