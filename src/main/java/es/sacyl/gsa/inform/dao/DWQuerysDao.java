package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.DWIndicador;
import es.sacyl.gsa.inform.bean.DWIndicadorValorAno;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
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
public class DWQuerysDao extends ConexionDao {

    private static final Logger LOGGER = LogManager.getLogger(DWDao.class);
    private static final long serialVersionUID = 1L;

    public DWQuerysDao() {
    }

    public ArrayList<DWIndicadorValorAno> getListaHospitalizacionAnoServicio(Integer ano, String servicio) {
        ArrayList<DWIndicadorValorAno> lista = new ArrayList<>();

        sql = "SELECT UNIQUE indicador,dimension1,orden,defin.nombre"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=1  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as enero "
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=2  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as febrero"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=3  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as marzo"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=4  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as abril "
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=5  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as mayo"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=6  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as junio"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=7  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as julio "
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=8  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as agosto"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=9  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as septiembre"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=10 AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as octubre "
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=11 AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as noviembre"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=12 AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as diciembre"
                + " FROM dw_hos_indicadores dw"
                + " JOIN dw_indicadores defin ON defin.codigo = dw.indicador"
                + " WHERE ano=" + ano + "   AND servicio='" + servicio + "' AND orden<>0"
                + " ORDER  by orden";

        Connection connection = null;
        try {
            connection = super.getConexionBBDD();

            LOGGER.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    // el tipo de indicador definimos código y nombre
                    DWIndicador indi = new DWIndicadorDao().getPorCodigo(resulSet.getString("indicador"));
                    DWIndicadorValorAno indicador = new DWIndicadorValorAno();
                    indicador.setDwindicador(indi);
                    indicador.setAno(ano);
                    int[] valores = new int[13];
                    valores[0] = resulSet.getInt("enero");
                    valores[1] = resulSet.getInt("febrero");
                    valores[2] = resulSet.getInt("marzo");
                    valores[3] = resulSet.getInt("abril");
                    valores[4] = resulSet.getInt("mayo");
                    valores[5] = resulSet.getInt("junio");
                    valores[6] = resulSet.getInt("julio");
                    valores[7] = resulSet.getInt("agosto");
                    valores[8] = resulSet.getInt("septiembre");
                    valores[9] = resulSet.getInt("octubre");
                    valores[10] = resulSet.getInt("noviembre");
                    valores[11] = resulSet.getInt("diciembre");
                    int total = 0;
                    for (int i = 0; i < 12; i++) {
                        total += valores[i];
                    }
                    valores[12] = total;
                    indicador.setMesesTotales(valores);
                    lista.add(indicador);
                }
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    public DWIndicadorValorAno geIndicadorHospitalizacionAnoServicio(Integer ano, String servicio, String indicador) {
        DWIndicadorValorAno indicador1 = null;

        sql = "SELECT UNIQUE indicador,dimension1,orden,defin.nombre"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=1  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as enero "
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=2  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as febrero"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=3  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as marzo"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=4  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as abril "
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=5  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as mayo"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=6  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as junio"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=7  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as julio "
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=8  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as agosto"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=9  AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as septiembre"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=10 AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as octubre "
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=11 AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as noviembre"
                + ",(SELECT SUM(valor) FROM dw_hos_indicadores WHERE ano=" + ano + " AND mes=12 AND servicio='" + servicio + "' AND dimension1=dw.dimension1 AND indicador=dw.indicador ) as diciembre"
                + " FROM dw_hos_indicadores dw"
                + " JOIN dw_indicadores defin ON defin.codigo = dw.indicador"
                + " WHERE ano=" + ano + "   AND servicio='" + servicio + "' "
                + " AND indicador ='" + indicador + "'"
                + " ORDER  by orden";

        Connection connection = null;
        try {
            connection = super.getConexionBBDD();

            LOGGER.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    indicador1 = getResulset(resulSet, ano);
                }
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return indicador1;
    }

    public DWIndicadorValorAno getResulset(ResultSet resulSet, Integer ano) {
        DWIndicador indi = new DWIndicador();
        DWIndicadorValorAno indicador = new DWIndicadorValorAno();
        try {
            indi.setCodigo(resulSet.getString("indicador"));

            indi.setNombre(resulSet.getString("nombre"));
            indicador.setDwindicador(indi);
            indicador.setAno(ano);
            int[] valores = new int[13];
            valores[0] = resulSet.getInt("enero");
            valores[1] = resulSet.getInt("febrero");
            valores[2] = resulSet.getInt("marzo");
            valores[3] = resulSet.getInt("abril");
            valores[4] = resulSet.getInt("mayo");
            valores[5] = resulSet.getInt("junio");
            valores[6] = resulSet.getInt("julio");
            valores[7] = resulSet.getInt("agosto");
            valores[8] = resulSet.getInt("septiembre");
            valores[9] = resulSet.getInt("octubre");
            valores[10] = resulSet.getInt("noviembre");
            valores[11] = resulSet.getInt("diciembre");
            int total = 0;
            for (int i = 0; i < 12; i++) {
                total += valores[i];
            }
            valores[12] = total;
            indicador.setMesesTotales(valores);
        } catch (SQLException ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        }
        return indicador;

    }

    /**
     *
     * @param tipo
     * @return
     */
    public ArrayList<DatoGenericoBean> getUgenciasUltimos30dias(String tipo) {
        Connection connection = null;
        ArrayList<DatoGenericoBean> lista = new ArrayList<>();
        switch (tipo) {
            case "COVID":
                sql = "  SELECT * FROM ( "
                        + " SELECT mes||':'||dia as fecha, total_covid as total "
                        + " FROM dw_urg_covid ORDER BY ano desc, mes desc, dia desc"
                        + ") WHERE ROWNUM<=30";
                break;
            case "NOCOVID":
                sql = "  SELECT * FROM ( "
                        + " SELECT mes||':'||dia as fecha, generales as total "
                        + " FROM dw_urg_covid ORDER BY ano desc, mes desc, dia desc"
                        + ") WHERE ROWNUM<=30";
                break;
        }
        try {
            connection = super.getConexionBBDD();
            LOGGER.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    DatoGenericoBean dato = new DatoGenericoBean();
                    dato.setTipoDato(resulSet.getString("fecha"));
                    dato.setValorInt(resulSet.getInt("total"));
                    lista.add(dato);
                }
                statement.close();
            }
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
