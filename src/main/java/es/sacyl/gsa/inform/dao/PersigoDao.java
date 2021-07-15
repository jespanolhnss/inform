package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.component.notification.Notification;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 *
 * Campos de la tabla usuariospersigo
 *
 * NIF APE1 APE2 NOMBRE BDT_DATE_DAT SEXO_COD TIPOVIA DIRECCION NUMERO PLANTA
 * PUERTA EXT_DIR PROVINCIA MUNICIPIO LOCALIDAD FECHAINICIOPUESTO FECHAFINPUESTO
 * MAIL TEL1 TEL2 TEL3 TEL4 PUESTO DESCPUESTO CLASEPERSONA RELSERVICIO CODFUNC
 * DESCFUNC FANTIG EMPLEADO REFERENCIA GFH DESCGFH
 *
 * No usa objeto bean, inserta dede el objeto ResultSet
 */
public class PersigoDao extends ConexionDao {

    private static final Logger LOGGER = LogManager.getLogger(PersigoDao.class);

    String update = "UPDATE USUARIOSPERSIGO SET  "
            + "APE1=?,APE2=?,NOMBRE=?,BDT_DATE_DAT=?,SEXO_COD=?,TIPOVIA=?,DIRECCION=?,NUMERO=?,PLANTA=?"
            + ",PUERTA=?,EXT_DIR=?,PROVINCIA=?,MUNICIPIO=?,LOCALIDAD=?,FECHAINICIOPUESTO=?,FECHAFINPUESTO=?"
            + ",MAIL=?,TEL1=?,TEL2=?,TEL3=?"
            + ",TEL4=?,PUESTO=?,DESCPUESTO=?,CLASEPERSONA=?,RELSERVICIO=?,CODFUNC=?,DESCFUNC=?,FANTIG=?,EMPLEADO=?"
            + ",REFERENCIA=?,GFH=?,DESCGFH=? "
            + "  WHERE nif=? ";

    String insert = "INSERT into  USUARIOSPERSIGO"
            + "(APE1,APE2,NOMBRE,BDT_DATE_DAT,SEXO_COD,TIPOVIA,DIRECCION,NUMERO,PLANTA"
            + ",PUERTA,EXT_DIR,PROVINCIA,MUNICIPIO,LOCALIDAD,FECHAINICIOPUESTO,FECHAFINPUESTO,MAIL,TEL1,TEL2,TEL3"
            + ",TEL4,PUESTO,DESCPUESTO,CLASEPERSONA,RELSERVICIO,CODFUNC,DESCFUNC,FANTIG,EMPLEADO,REFERENCIA,GFH,DESCGFH "
            + ",NIF  ) "
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    /**
     *
     * @return
     */
    public Connection conecta() {
        Connection conn = null;
        String dbURL2;
        String username;
        String password;

        ParametroBean parametroBean = new ParametroDao().getPorCodigo(ParametroBean.URL_CONEXION_PERSIGO);
        if (parametroBean == null) {
            Notification.show("Sin definir parámetro " + ParametroBean.URL_CONEXION_PERSIGO, 10000, Notification.Position.MIDDLE);
        } else {
            String cadena = new ParametroDao().getPorCodigo(ParametroBean.URL_CONEXION_PERSIGO).getValor();
            String[] conex = cadena.split("\\|");
            dbURL2 = "jdbc:oracle:thin:@" + conex[0];
            username = conex[1];
            password = conex[2];
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                conn = DriverManager.getConnection(dbURL2, username, password);
                LOGGER.debug("Conexion con bbdd" + dbURL2 + " realizada.");
            } catch (ClassNotFoundException ex) {
                LOGGER.error("Error de conexión con bbdd ", ex);
            } catch (SQLException ex) {
                LOGGER.error("Error de conexión sql con bbdd ", ex);
            }
        }
        return conn;
    }

    /**
     * Replica la vista de persigo en la tabla local hnss.usuariospersigo
     * Inserta o actualiza (todos) los campos en la tabla según el nif
     *
     * @return Log resumen de los registros procesados
     *
     */
    public String doAcualizaTablaLocal() {
        String log = "Proceso de actualiacón de tabla usuariospersigo. \n";
        log = log.concat("Fecha inicio :" + LocalDateTime.now().toString() + "\n");
        Connection conn = this.conecta();
        UsuarioBean usuario = null;
        int insertados = 0, actualizados = 0, altas = 0, bajas = 0;
        if (conn != null) {
            sql = " SELECT  * FROM S15_PERS ";
            try {
                Statement statement = conn.createStatement();
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    if (existe(resulSet.getString("nif")) == true) {
                        actualizados++;
                        doEjecutaSql(resulSet, update);
                    } else {
                        doEjecutaSql(resulSet, insert);
                        insertados++;
                    }
                    if (resulSet.getDate("FECHAFINPUESTO") != null) {
                        Date baja = resulSet.getDate("FECHAFINPUESTO");
                        if (baja.equals(Date.valueOf(LocalDate.now()))) {
                            bajas++;
                        }
                    }
                    if (resulSet.getDate("FECHAINICIOPUESTO") != null) {
                        Date baja = resulSet.getDate("FECHAINICIOPUESTO");
                        if (baja.equals(Date.valueOf(LocalDate.now()))) {
                            altas++;
                        }
                    }
                }
                statement.close();
                LOGGER.debug(sql);
            } catch (SQLException e1) {
                LOGGER.error(sql, e1);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error(Utilidades.getStackTrace(e));
                }
            }
        }
        int total = insertados + actualizados;
        log = log.concat("Procesados " + total + " empleados.  \n" + insertados + " nuevos , " + actualizados + "antiguos. \n "
                + altas + " altas , " + bajas + " bajas. \n");
        log = log.concat("Fecha fin :" + LocalDateTime.now().toString() + "\n");
        return log;
    }

    /**
     * Actualiza la tupla de la tabla por nif
     *
     * @param rs
     */
    public void doEjecutaSql(ResultSet rs, String sqlParam) {
        Connection conn = this.getConexionBBDD();
        Boolean insertadoBoolean;
        try (PreparedStatement statement = conn.prepareStatement(sqlParam)) {
            if (rs.getString("ape1") != null) {
                statement.setString(1, rs.getString("ape1"));
            } else {
                statement.setNull(1, Types.CHAR);
            }
            if (rs.getString("ape2") != null) {
                statement.setString(2, rs.getString("ape2"));
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (rs.getString("nombre") != null) {
                statement.setString(3, rs.getString("nombre"));
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (rs.getDate("BDT_DATE_DAT") != null) {
                statement.setDate(4, rs.getDate("BDT_DATE_DAT"));
            } else {
                statement.setNull(4, Types.DATE);
            }
            if (rs.getString("SEXO_COD") != null) {
                statement.setString(5, rs.getString("SEXO_COD"));
            } else {
                statement.setNull(5, Types.CHAR);
            }
            if (rs.getString("TIPOVIA") != null) {
                statement.setString(6, rs.getString("TIPOVIA"));
            } else {
                statement.setNull(6, Types.CHAR);
            }
            if (rs.getString("DIRECCION") != null) {
                statement.setString(7, rs.getString("DIRECCION"));
            } else {
                statement.setNull(7, Types.CHAR);
            }
            if (rs.getString("NUMERO") != null) {
                statement.setString(8, rs.getString("NUMERO"));
            } else {
                statement.setNull(8, Types.CHAR);
            }
            if (rs.getString("PLANTA") != null) {
                statement.setString(9, rs.getString("PLANTA"));
            } else {
                statement.setNull(9, Types.CHAR);
            }
            //   + ",PUERTA,EXT_DIR,PROVINCIA,MUNICIPIO,LOCALIDAD,
            if (rs.getString("PUERTA") != null) {
                statement.setString(10, rs.getString("PUERTA"));
            } else {
                statement.setNull(10, Types.CHAR);
            }
            if (rs.getString("EXT_DIR") != null) {
                statement.setString(11, rs.getString("EXT_DIR"));
            } else {
                statement.setNull(11, Types.CHAR);
            }
            if (rs.getString("PROVINCIA") != null) {
                statement.setString(12, rs.getString("PROVINCIA"));
            } else {
                statement.setNull(12, Types.CHAR);
            }
            if (rs.getString("MUNICIPIO") != null) {
                statement.setString(13, rs.getString("MUNICIPIO"));
            } else {
                statement.setNull(13, Types.CHAR);
            }
            if (rs.getString("LOCALIDAD") != null) {
                statement.setString(14, rs.getString("LOCALIDAD"));
            } else {
                statement.setNull(14, Types.CHAR);
            }
            //   FECHAINICIOPUESTO,FECHAFINPUESTO,MAIL,TEL1,TEL2,TEL3"
            if (rs.getDate("FECHAINICIOPUESTO") != null) {
                statement.setDate(15, rs.getDate("FECHAINICIOPUESTO"));
            } else {
                statement.setNull(15, Types.DATE);
            }
            if (rs.getDate("FECHAFINPUESTO") != null) {
                statement.setDate(16, rs.getDate("FECHAFINPUESTO"));
            } else {
                statement.setNull(16, Types.DATE);
            }
            if (rs.getString("MAIL") != null) {
                statement.setString(17, rs.getString("MAIL"));
            } else {
                statement.setNull(17, Types.CHAR);
            }
            if (rs.getString("TEL1") != null) {
                statement.setString(18, rs.getString("TEL1"));
            } else {
                statement.setNull(18, Types.CHAR);
            }
            if (rs.getString("TEL2") != null) {
                statement.setString(19, rs.getString("TEL2"));
            } else {
                statement.setNull(19, Types.CHAR);
            }
            if (rs.getString("TEL3") != null) {
                statement.setString(20, rs.getString("TEL3"));
            } else {
                statement.setNull(20, Types.CHAR);
            }
            //   + ",TEL4,PUESTO,DESCPUESTO,CLASEPERSONA,RELSERVICIO,CODFUNC,DESCFUNC,FANTIG,EMPLEADO,REFERENCIA,GFH,DESCGFH
            if (rs.getString("TEL4") != null) {
                statement.setString(21, rs.getString("TEL4"));
            } else {
                statement.setNull(21, Types.CHAR);
            }

            statement.setLong(22, rs.getLong("PUESTO"));

            if (rs.getString("DESCPUESTO") != null) {
                statement.setString(23, rs.getString("DESCPUESTO"));
            } else {
                statement.setNull(23, Types.CHAR);
            }
            if (rs.getString("CLASEPERSONA") != null) {
                statement.setString(24, rs.getString("CLASEPERSONA"));
            } else {
                statement.setNull(24, Types.CHAR);
            }
            //  RELSERVICIO,CODFUNC,DESCFUNC,FANTIG,EMPLEADO,REFERENCIA,GFH,DESCGFH
            if (rs.getString("RELSERVICIO") != null) {
                statement.setString(25, rs.getString("RELSERVICIO"));
            } else {
                statement.setNull(25, Types.CHAR);
            }
            if (rs.getString("CODFUNC") != null) {
                statement.setString(26, rs.getString("CODFUNC"));
            } else {
                statement.setNull(26, Types.CHAR);
            }
            if (rs.getString("DESCFUNC") != null) {
                statement.setString(27, rs.getString("DESCFUNC"));
            } else {
                statement.setNull(27, Types.CHAR);
            }
            if (rs.getDate("FANTIG") != null) {
                statement.setDate(28, rs.getDate("FANTIG"));
            } else {
                statement.setNull(28, Types.CHAR);
            }
            //EMPLEADO,REFERENCIA,GFH,DESCGFH
            if (rs.getString("EMPLEADO") != null) {
                statement.setString(29, rs.getString("EMPLEADO"));
            } else {
                statement.setNull(29, Types.CHAR);
            }
            if (rs.getLong("REFERENCIA") != 0) {
                statement.setLong(30, rs.getLong("REFERENCIA"));
            } else {
                statement.setNull(30, Types.CHAR);
            }
            if (rs.getString("GFH") != null) {
                statement.setString(31, rs.getString("GFH"));
            } else {
                statement.setNull(31, Types.CHAR);
            }
            if (rs.getString("DESCGFH") != null) {
                statement.setString(32, rs.getString("DESCGFH"));
            } else {
                statement.setNull(32, Types.CHAR);
            }
            if (rs.getString("NIF") != null) {
                statement.setString(33, rs.getString("NIF"));
            } else {
                statement.setNull(33, Types.CHAR);
            }

            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException ex) {
            LOGGER.error(sql + Utilidades.getStackTrace(ex));
        } finally {
            this.doCierraConexion(conn);
        }

    }

    /**
     * Inserta el registro
     *
     * @param rs
     */
    /*
    public void doInserta(ResultSet rs) {
        Connection conn = this.getConexionBBDD();
        Boolean insertadoBoolean;

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            if (rs.getString("ape1") != null) {
                statement.setString(1, rs.getString("ape1"));
            } else {
                statement.setNull(1, Types.CHAR);
            }
            if (rs.getString("ape2") != null) {
                statement.setString(2, rs.getString("ape2"));
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (rs.getString("nombre") != null) {
                statement.setString(3, rs.getString("nombre"));
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (rs.getDate("BDT_DATE_DAT") != null) {
                statement.setDate(4, rs.getDate("BDT_DATE_DAT"));
            } else {
                statement.setNull(4, Types.DATE);
            }
            if (rs.getString("SEXO_COD") != null) {
                statement.setString(5, rs.getString("SEXO_COD"));
            } else {
                statement.setNull(5, Types.CHAR);
            }
            if (rs.getString("TIPOVIA") != null) {
                statement.setString(6, rs.getString("TIPOVIA"));
            } else {
                statement.setNull(6, Types.CHAR);
            }
            if (rs.getString("DIRECCION") != null) {
                statement.setString(7, rs.getString("DIRECCION"));
            } else {
                statement.setNull(7, Types.CHAR);
            }
            if (rs.getString("NUMERO") != null) {
                statement.setString(8, rs.getString("NUMERO"));
            } else {
                statement.setNull(8, Types.CHAR);
            }
            if (rs.getString("PLANTA") != null) {
                statement.setString(9, rs.getString("PLANTA"));
            } else {
                statement.setNull(9, Types.CHAR);
            }
            //   + ",PUERTA,EXT_DIR,PROVINCIA,MUNICIPIO,LOCALIDAD,
            if (rs.getString("PUERTA") != null) {
                statement.setString(10, rs.getString("PUERTA"));
            } else {
                statement.setNull(10, Types.CHAR);
            }
            if (rs.getString("EXT_DIR") != null) {
                statement.setString(11, rs.getString("EXT_DIR"));
            } else {
                statement.setNull(11, Types.CHAR);
            }
            if (rs.getString("PROVINCIA") != null) {
                statement.setString(12, rs.getString("PROVINCIA"));
            } else {
                statement.setNull(12, Types.CHAR);
            }
            if (rs.getString("MUNICIPIO") != null) {
                statement.setString(13, rs.getString("MUNICIPIO"));
            } else {
                statement.setNull(13, Types.CHAR);
            }
            if (rs.getString("LOCALIDAD") != null) {
                statement.setString(14, rs.getString("LOCALIDAD"));
            } else {
                statement.setNull(14, Types.CHAR);
            }
            //   FECHAINICIOPUESTO,FECHAFINPUESTO,MAIL,TEL1,TEL2,TEL3"
            if (rs.getDate("FECHAINICIOPUESTO") != null) {
                statement.setDate(15, rs.getDate("FECHAINICIOPUESTO"));
            } else {
                statement.setNull(15, Types.DATE);
            }
            if (rs.getDate("FECHAFINPUESTO") != null) {
                statement.setDate(16, rs.getDate("FECHAFINPUESTO"));
            } else {
                statement.setNull(16, Types.DATE);
            }
            if (rs.getString("MAIL") != null) {
                statement.setString(17, rs.getString("MAIL"));
            } else {
                statement.setNull(17, Types.CHAR);
            }
            if (rs.getString("TEL1") != null) {
                statement.setString(18, rs.getString("TEL1"));
            } else {
                statement.setNull(18, Types.CHAR);
            }
            if (rs.getString("TEL2") != null) {
                statement.setString(19, rs.getString("TEL2"));
            } else {
                statement.setNull(19, Types.CHAR);
            }
            if (rs.getString("TEL3") != null) {
                statement.setString(20, rs.getString("TEL3"));
            } else {
                statement.setNull(20, Types.CHAR);
            }
            //   + ",TEL4,PUESTO,DESCPUESTO,CLASEPERSONA,RELSERVICIO,CODFUNC,DESCFUNC,FANTIG,EMPLEADO,REFERENCIA,GFH,DESCGFH
            if (rs.getString("TEL4") != null) {
                statement.setString(21, rs.getString("TEL4"));
            } else {
                statement.setNull(21, Types.CHAR);
            }
            if (rs.getLong("PUESTO") != 0) {
                statement.setLong(22, rs.getLong("PUESTO"));
            } else {
                statement.setNull(22, Types.INTEGER);
            }
            if (rs.getString("DESCPUESTO") != null) {
                statement.setString(23, rs.getString("DESCPUESTO"));
            } else {
                statement.setNull(23, Types.CHAR);
            }
            if (rs.getString("CLASEPERSONA") != null) {
                statement.setString(24, rs.getString("CLASEPERSONA"));
            } else {
                statement.setNull(24, Types.CHAR);
            }
            //  RELSERVICIO,CODFUNC,DESCFUNC,FANTIG,EMPLEADO,REFERENCIA,GFH,DESCGFH
            if (rs.getString("RELSERVICIO") != null) {
                statement.setString(25, rs.getString("RELSERVICIO"));
            } else {
                statement.setNull(25, Types.CHAR);
            }
            if (rs.getString("CODFUNC") != null) {
                statement.setString(26, rs.getString("CODFUNC"));
            } else {
                statement.setNull(26, Types.CHAR);
            }
            if (rs.getString("DESCFUNC") != null) {
                statement.setString(27, rs.getString("DESCFUNC"));
            } else {
                statement.setNull(27, Types.CHAR);
            }
            if (rs.getDate("FANTIG") != null) {
                statement.setDate(28, rs.getDate("FANTIG"));
            } else {
                statement.setNull(28, Types.CHAR);
            }
//EMPLEADO,REFERENCIA,GFH,DESCGFH
            if (rs.getString("EMPLEADO") != null) {
                statement.setString(29, rs.getString("EMPLEADO"));
            } else {
                statement.setNull(29, Types.CHAR);
            }
            if (rs.getLong("REFERENCIA") != 0) {
                statement.setLong(30, rs.getLong("REFERENCIA"));
            } else {
                statement.setNull(30, Types.CHAR);
            }
            if (rs.getString("GFH") != null) {
                statement.setString(31, rs.getString("GFH"));
            } else {
                statement.setNull(31, Types.CHAR);
            }
            if (rs.getString("DESCGFH") != null) {
                statement.setString(32, rs.getString("DESCGFH"));
            } else {
                statement.setNull(32, Types.CHAR);
            }
            if (rs.getString("NIF") != null) {
                statement.setString(33, rs.getString("NIF"));
            } else {
                statement.setNull(33, Types.CHAR);
            }

            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException ex) {
            LOGGER.error(sql + Utilidades.getStackTrace(ex));
        } finally {
            this.doCierraConexion(conn);
        }
    }

     */
    /**
     * Verifica si existe un empleado con nif en la tabla usuariospersigo
     *
     * @param nif
     * @return
     */
    public Boolean existe(String nif) {
        Boolean existeBoolean = false;
        String sql;
        Connection conn = this.getConexionBBDD();
        UsuarioBean usuario = null;
        if (conn != null) {
            sql = " SELECT  * FROM USUARIOSPERSIGO WHERE NIF='" + nif.trim() + "' ";
            try {
                Statement statement = conn.createStatement();
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    existeBoolean = true;
                }
                statement.close();
                LOGGER.debug(sql);
            } catch (SQLException e1) {
                LOGGER.error(sql, e1);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.error(Utilidades.getStackTrace(e));
                }
            }
        }

        return existeBoolean;
    }

}
