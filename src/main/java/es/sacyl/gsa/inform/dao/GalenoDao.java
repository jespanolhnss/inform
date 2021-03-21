package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.ctrl.IpCtrl;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.DriverManager;
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
public class GalenoDao {

    private String cadena = null;
    private static final Logger logger = LogManager.getLogger(GalenoDao.class);

    public GalenoDao() {
    }

    public Connection conecta() {
        Connection conn = null;

        cadena = new ParametroDao().getPorCodigo(ParametroBean.URL_CONEXION_GALENO).getValor();
//10.40.37.233:1527/clihis:informixserver=clinica;user=galeno;password=torozo

        String dbURL2 = "jdbc:informix-sqli://" + cadena;

        try {
            Class.forName("com.informix.jdbc.IfxDriver");
            conn = DriverManager.getConnection(dbURL2);
        } catch (ClassNotFoundException ex) {
            logger.error("Error conexion galeno, clase no contrada.", ex);
        } catch (SQLException ex) {
            logger.error(Utilidades.getStackTrace(ex));
        }
        return conn;
    }

    public ArrayList<DatoGenericoBean> getEquipo(String dato) {
        Connection conn = this.conecta();
        ArrayList<DatoGenericoBean> datos = new ArrayList<>();
        String sql = "  SELECT  dir1 ,dir2,dir3,dir4 ,nombre_equipo,gfh,usuario,observaciones"
                + " ,cod_maqui,invt_marca.descripcion,modelo,nserie ,nom_maqui,equipo,fec_baja,edificio,codplan,codala,sala"
                + " FROM hnss_ip_lineas"
                + " JOIN  hnss_ip on hnss_ip.id=hnss_ip_lineas.id_ip"
                + " LEFT JOIN  man_maqui on man_maqui.cod_maqui=hnss_ip_lineas. numero_registro"
                + " LEFT JOIN  invt_marca on invt_marca.codigo=man_maqui.marca WHERE 1=1 "
                + "  AND  hnss_ip_lineas.fecha_fin is null  ";
        if (IpCtrl.isValid(dato)) {
            String[] dirs = dato.split("\\.");
            sql = sql.concat(" AND dir1='" + dirs[0] + "'");
            sql = sql.concat(" AND dir2='" + dirs[1] + "'");
            sql = sql.concat(" AND dir3='" + dirs[2] + "'");
            sql = sql.concat(" AND dir4='" + dirs[3] + "'");
        } else {
            sql = sql.concat(" AND cod_maqui='" + dato + "'");
        }
        if (conn != null) {
            try {
                Statement statement = conn.createStatement();
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    DatoGenericoBean datog = new DatoGenericoBean();
                    datog.setTipoDato("inventario");
                    if (resulSet.getString("cod_maqui") != null) {
                        datog.setValor(resulSet.getString("cod_maqui").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("marca");
                    if (resulSet.getString("descripcion") != null) {
                        datog.setValor(resulSet.getString("descripcion").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("equipo");
                    if (resulSet.getString("descripcion") != null) {
                        datog.setValor(resulSet.getString("descripcion").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("ip");
                    if (resulSet.getString("dir1") != null && resulSet.getString("dir2") != null
                            && resulSet.getString("dir3") != null
                            && resulSet.getString("dir4") != null) {
                        datog.setValor(resulSet.getString("dir1").trim() + "." + resulSet.getString("dir2").trim() + "."
                                + resulSet.getString("dir3").trim() + "." + resulSet.getString("dir4").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("nombre_equipo");
                    if (resulSet.getString("nombre_equipo") != null) {
                        datog.setValor(resulSet.getString("nombre_equipo").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("gfh");
                    if (resulSet.getString("gfh") != null) {
                        datog.setValor(resulSet.getString("gfh").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("Observaciones");
                    if (resulSet.getString("observaciones") != null && !resulSet.getString("observaciones").isEmpty()) {
                        datog.setValor(resulSet.getString("observaciones").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("cod_maqui");
                    if (resulSet.getString("cod_maqui") != null) {
                        datog.setValor(resulSet.getString("cod_maqui").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("nom_maqui");
                    if (resulSet.getString("nom_maqui") != null) {
                        datog.setValor(resulSet.getString("nom_maqui").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("modelo");
                    if (resulSet.getString("modelo") != null) {
                        datog.setValor(resulSet.getString("modelo").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("nserie");
                    if (resulSet.getString("nserie") != null) {
                        datog.setValor(resulSet.getString("nserie").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("nombre_equipo");
                    if (resulSet.getString("nombre_equipo") != null) {
                        datog.setValor(resulSet.getString("nombre_equipo").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("edificio");
                    if (resulSet.getString("edificio") != null) {
                        datog.setValor(resulSet.getString("edificio").trim());
                    } else {
                        datog.setValor("");
                    }
                    datos.add(datog);

                    datog = new DatoGenericoBean();
                    datog.setTipoDato("ubicacion");
                    if (resulSet.getString("codplan") != null) {
                        datog.setValor(resulSet.getString("codplan"));
                    } else {
                        datog.setValor("");
                    }
                    if (resulSet.getString("codala") != null) {
                        datog.setValor(datog.getValor() + "-" + resulSet.getString("codala").trim());
                    }
                    if (resulSet.getString("sala") != null) {
                        datog.setValor(datog.getValor() + "-" + resulSet.getString("sala").trim());
                    }

                    datos.add(datog);

                }
                statement.close();
                logger.debug(sql);
            } catch (SQLException e1) {
                logger.error(sql, e1);
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(Utilidades.getStackTrace(e));
                }
            }
        }

        return datos;
    }
}
