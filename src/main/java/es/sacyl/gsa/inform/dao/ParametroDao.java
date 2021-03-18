package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
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
public class ParametroDao extends ConexionDao implements Serializable, ConexionInterface<ParametroBean> {

    private static final Logger LOGGER = LogManager.getLogger(ParametroDao.class);
    private static final long serialVersionUID = 1L;

    public ParametroDao() {
        super();
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public ParametroBean getRegistroResulset(ResultSet rs) {
        ParametroBean parametroBean = new ParametroBean();
        try {
            parametroBean.setId(rs.getLong("id"));
            parametroBean.setCodigo(rs.getString("codigo").trim());
            parametroBean.setDescripcion(rs.getString("descripcion").trim());
            parametroBean.setValor(rs.getString("valor").trim());
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return parametroBean;
    }

    @Override
    public ParametroBean getPorId(Long id) {
        Connection connection = null;
        ParametroBean parametroBean = null;
        if (id == null || id.equals(new Long(0))) {
            parametroBean = null;
        } else {
            try {
                connection = super.getConexionBBDD();
                sql = " SELECT * FROM parametros WHERE  1=1 ";

                if (id != null && !id.equals(new Long(0))) {
                    sql = sql.concat(" AND id=" + id);
                }
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        parametroBean = getRegistroResulset(resulSet);
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
        return parametroBean;
    }

    /**
     *
     * @param codigo
     * @return
     */
    @Override
    public ParametroBean getPorCodigo(String codigo) {
        Connection connection = null;
        ParametroBean parametroBean = null;
        if (codigo == null || codigo.isEmpty()) {
            parametroBean = null;
        } else {
            try {
                connection = super.getConexionBBDD();
                sql = " SELECT * FROM parametros WHERE  1=1 ";

                if (codigo != null && !codigo.isEmpty()) {
                    sql = sql.concat(" AND codigo='" + codigo.trim() + "'");
                }
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sql);
                    if (resulSet.next()) {
                        parametroBean = getRegistroResulset(resulSet);
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
        return parametroBean;
    }

    /**
     *
     * @param parametroBean
     * @return
     */
    @Override
    public boolean doGrabaDatos(ParametroBean parametroBean) {
        boolean actualizado = false;

        if (this.getPorId(parametroBean.getId()) == null) {
            actualizado = this.doInsertaDatos(parametroBean);
        } else {
            actualizado = this.doActualizaDatos(parametroBean);
        }
        return actualizado;
    }

    /**
     *
     * @param parametroBean
     * @return
     */
    @Override
    public boolean doInsertaDatos(ParametroBean parametroBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            parametroBean.setId(this.getSiguienteId("parametros"));
            sql = " INSERT INTO  parametros  (id,codigo,descripcion,valor) " + " VALUES "
                    + "(" + parametroBean.getId() + ""
                    + ",'" + parametroBean.getCodigo() + "'"
                    + ",'" + parametroBean.getDescripcion() + "'"
                    + ",'" + parametroBean.getValor() + "')";
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
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
        return insertadoBoolean;
    }

    /**
     *
     * @param parametroBean
     * @return
     */
    @Override
    public boolean doActualizaDatos(ParametroBean parametroBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   parametros  SET codigo='" + parametroBean.getCodigo()
                    + "',descripcion='" + parametroBean.getDescripcion() + "', valor='" + parametroBean.getValor() + "'"
                    + " WHERE id='" + parametroBean.getId() + "'";
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
                statement.close();
            }
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));

        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param texto
     * @return
     */
    @Override
    public ArrayList<ParametroBean> getLista(String texto) {
        Connection connection = null;
        ArrayList<ParametroBean> listaParametros = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM parametros WHERE  1=1 ";
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND  ( UPPER(descripcion) like'%" + texto.toUpperCase() + "%'  OR   UPPER(codigo) like'%" + texto.toUpperCase() + "%' )");
            }
            sql = sql.concat(" ORDER BY descripcion  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                listaParametros.add(getRegistroResulset(resulSet));
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
        return listaParametros;
    }

    /**
     *
     * @param parametroBean
     * @return
     */
    @Override
    public boolean doBorraDatos(ParametroBean parametroBean) {
        Connection connection = null;
        Boolean booradoBoolean = false;
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  parametros WHERE id='" + parametroBean.getId() + "'";
            Statement statement = connection.createStatement();
            booradoBoolean = statement.execute(sql);
            booradoBoolean = true;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return booradoBoolean;
    }
}
