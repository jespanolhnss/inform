package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.FuncionalidadBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FuncionalidadDAO extends ConexionDao {

    private static final Logger logger = LogManager.getLogger(FuncionalidadDAO.class);

    public FuncionalidadDAO() {
        super();
    }

    /**
     *
     * @param rs
     * @return
     */
    public FuncionalidadBean getRegistroResulset(ResultSet rs) {
        FuncionalidadBean funcionalidad = new FuncionalidadBean();
        try {

            funcionalidad.setId(rs.getLong("id"));
            funcionalidad.setDescripcion(rs.getString("descripcion"));
            funcionalidad.setTextomenu(rs.getString("textomenu"));
            funcionalidad.setEstado(rs.getBoolean("estado"));
            funcionalidad.setUsucambio(new UsuarioDao().getPorId(rs.getLong("usucambio")));
            try {
                LocalDate date = Utilidades.getFechaLocalDate(rs.getLong("fechacambio"));
                funcionalidad.setFechacambio(date);
            } catch (SQLException e) {
                logger.error(Utilidades.getStackTrace(e));
            }
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return funcionalidad;
    }

    /**
     *
     * @param id
     * @return
     */
    public FuncionalidadBean getPorId(Long id) {
        Connection connection = null;
        FuncionalidadBean funcionalidad = null;
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM funcionalidad WHERE   id=	" + id;
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            if (resulSet.next()) {
                funcionalidad = getRegistroResulset(resulSet);
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
        return funcionalidad;
    }

    /**
     *
     * @param descString
     * @return
     */
    public FuncionalidadBean getPorDescripcion(String descString) {
        Connection connection = null;
        FuncionalidadBean funcionalidad = null;
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM funcionalidad WHERE   descripcion='" + descString + "'";
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            if (resulSet.next()) {
                funcionalidad = getRegistroResulset(resulSet);
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
        return funcionalidad;
    }

    /**
     *
     * @return
     */
    public ArrayList<FuncionalidadBean> getListaFuncionalidad() {
        Connection connection = null;
        ArrayList<FuncionalidadBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM  funcionalidad WHERE estado= " + ConexionDao.BBDD_ACTIVOSI;
            sql = sql.concat(" order by descripcion ");

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                FuncionalidadBean funcionalidad = getRegistroResulset(resulSet);
                lista.add(funcionalidad);
            }
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    /**
     *
     * @return
     */
    public Set<FuncionalidadBean> getListaFuncionalidadSet() {
        Connection connection = null;
        Set<FuncionalidadBean> lista = new HashSet<FuncionalidadBean>();

        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM  funcionalidad WHERE estado= " + ConexionDao.BBDD_ACTIVOSI;
            sql = sql.concat(" order by descripcion ");

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                FuncionalidadBean funcionalidad = getRegistroResulset(resulSet);
                lista.add(funcionalidad);
            }
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getListaFuncionalidadString() {
        Connection connection = null;
        ArrayList<String> lista = new ArrayList<String>();

        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM  funcionalidad WHERE estado= " + ConexionDao.BBDD_ACTIVOSI;
            sql = sql.concat(" order by descripcion ");

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                FuncionalidadBean funcionalidad = getRegistroResulset(resulSet);
                lista.add(funcionalidad.getDescripcion());
            }
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    /**
     *
     * @param funcionalidad
     * @return
     */
    public boolean grabaDatos(FuncionalidadBean funcionalidad) {
        boolean actualizado = false;

        if (funcionalidad.getId().equals(new Long(0))) {
            actualizado = this.insertaDatos(funcionalidad);
        } else {
            actualizado = this.actualizaDatos(funcionalidad);
        }
        return actualizado;
    }

    /**
     *
     * @param funcionalidad
     * @return
     */
    public boolean insertaDatos(FuncionalidadBean funcionalidad) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();
            Long id = new ConexionDao().getSiguienteId("funcionalidad");
            funcionalidad.setId(id);
            sql = " INSERT INTO  funcionalidad (id,descripcion,textomenu,estado,usucambio,fechacambio) " + " VALUES ("
                    + funcionalidad.getId() + ",'" + funcionalidad.getDescripcion() + "','"
                    + funcionalidad.getTextomenu() + "'," + ConexionDao.BBDD_ACTIVOSI + "," + usuario.getId() + ","
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ")";
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            insertadoBoolean = true;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param funcionalidad
     * @return
     */
    public boolean actualizaDatos(FuncionalidadBean funcionalidad) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();
            sql = " UPDATE   funcionalidad SET descripcion='" + funcionalidad.getDescripcion() + "',textomenu='"
                    + funcionalidad.getTextomenu() + "',usucambio='" + usuario.getDni() + "',  fechacambio="
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + " WHERE id=" + funcionalidad.getId();

            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            insertadoBoolean = true;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param funcionalidad
     * @return
     */
    public boolean borraDatos(FuncionalidadBean funcionalidad) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();
            sql = " UPDATE   funcionalidad SET usucambio='" + usuario.getDni() + "',  fechacambio="
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ",estado=" + ConexionDao.BBDD_ACTIVONO + " WHERE id="
                    + funcionalidad.getId();
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            insertadoBoolean = true;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param usuario
     * @return
     */
    public Set<FuncionalidadBean> getListaFuncioUsuario(UsuarioBean usuario) {
        Connection connection = null;
        Set<FuncionalidadBean> lista = new HashSet<FuncionalidadBean>();
        try {
            connection = super.getConexionBBDD();

            sql = "SELECT f.*	" + " FROM funcionalidad f 	"
                    + "  JOIN us_funcionalidad  u ON  f.id=u.idfuncionalidad  and  u.idusuario=" + usuario.getId()
                    + " WHERE f.estado=" + ConexionDao.BBDD_ACTIVOSI;

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                FuncionalidadBean funcionalidad = getRegistroResulset(resulSet);
                // funcionalidad.setPermitida(resulSet.getBoolean("permitida"));
                lista.add(funcionalidad);
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
        return lista;
    }

    /**
     *
     * @param usuario
     * @return
     */
    public ArrayList<FuncionalidadBean> getListaFuncioUsuarioAl(UsuarioBean usuario) {
        Connection connection = null;
        ArrayList<FuncionalidadBean> lista = new ArrayList<FuncionalidadBean>();
        try {
            connection = super.getConexionBBDD();
            if (usuario.getEstado() == UsuarioBean.USUARIO_ADMINISTRADOR) {
                sql = "SELECT f.*	" + " FROM funcionalidad f 	" + "   WHERE f.estado=" + ConexionDao.BBDD_ACTIVOSI
                        + " ORDER BY textomenu ";
            } else {
                sql = "SELECT f.*	" + " FROM funcionalidad f 	"
                        + "  JOIN us_funcionalidad  u ON  f.id=u.idfuncionalidad AND u.permitida=1 and  u.idusuario="
                        + usuario.getId() + " WHERE f.estado=" + ConexionDao.BBDD_ACTIVOSI + " ORDER BY textomenu ";
            }
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                FuncionalidadBean funcionalidad = getRegistroResulset(resulSet);
                lista.add(funcionalidad);
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
        return lista;
    }

    /**
     *
     * @param usuario
     * @return
     */
    public ArrayList<String> getListaFuncioUsuarioString(UsuarioBean usuario) {
        Connection connection = null;
        ArrayList<String> lista = new ArrayList<String>();
        try {
            connection = super.getConexionBBDD();
            sql = "SELECT f.*	" + " FROM funcionalidad f 	"
                    + "  JOIN us_funcionalidad  u ON  f.id=u.idfuncionalidad  and u.permitida=1 AND u.estado="
                    + ConexionDao.BBDD_ACTIVOSI + " AND u.idusuario=" + usuario.getId() + " WHERE f.estado="
                    + ConexionDao.BBDD_ACTIVOSI;

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                FuncionalidadBean funcionalidad = getRegistroResulset(resulSet);
                lista.add(funcionalidad.getDescripcion());
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
        return lista;
    }
}
