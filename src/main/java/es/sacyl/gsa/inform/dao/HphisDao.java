package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class HphisDao {

    private String cadena = null;
    private static final Logger logger = LogManager.getLogger(HphisDao.class);

    public HphisDao() {
    }

    public HphisDao(String cadena) {
        this.cadena = cadena;
    }

    public Connection conecta() {
        Connection conn = null;
        if (cadena == null) {
            cadena = new ParametroDao().getPorCodigo(ParametroBean.URL_CONEXION_HIS).getValor();
        }
        String dbURL2 = "jdbc:informix-sqli://" + cadena;
        try {
            Class.forName("com.informix.jdbc.IfxDriver");
            conn = DriverManager.getConnection(dbURL2);
        } catch (ClassNotFoundException ex) {
            logger.error("Error conexion his, clase no contrada.", ex);
        } catch (SQLException ex) {
            logger.error(Utilidades.getStackTrace(ex));
        }
        return conn;
    }
}
