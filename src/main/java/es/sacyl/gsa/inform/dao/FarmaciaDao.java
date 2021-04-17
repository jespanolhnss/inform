package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.ParametroBean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FarmaciaDao {

    private static final Logger logger = LogManager.getLogger(FarmaciaDao.class);

    Connection conn = null;

    public FarmaciaDao() {
    }

    public Connection conecta() {
        Connection conn = null;

        String dbURL2, username, password;

        String cadena = new ParametroDao().getPorCodigo(ParametroBean.URL_CONEXION_CLINICA).getValor();

        String[] conex = cadena.split("\\|");
        dbURL2 = "jdbc:oracle:thin:@" + conex[0];
        username = conex[1];
        password = conex[2];

        String sql;

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection(dbURL2, username, password);
        } catch (ClassNotFoundException ex) {
            logger.error("Error conexion FARMACIA, clase no encontrada.", ex);
        } catch (SQLException ex) {
            logger.error("Error conexion FARMACIA, sql ", ex);
        }
        return conn;
    }
}
