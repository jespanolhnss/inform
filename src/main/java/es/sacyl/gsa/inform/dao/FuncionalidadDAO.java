package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.FuncionalidadBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FuncionalidadDAO extends ConexionDao implements ConexionInterface<FuncionalidadBean> {

    private static final Logger logger = LogManager.getLogger(FuncionalidadDAO.class);

    public FuncionalidadDAO() {
        super();
        sql = " SELECT  func.id as funcid,func.descripcion as funcdescripcion,func.textomenu as functextomenu"
                + " ,func.estado as funcestado, func.fechacambio as funcfechacambio,func.usucambio as funcfechacambio "
                + " ,usu.id as usuarioid,usu.dni as usuariodni,usu.apellido1 as usuarioapellido1"
                + " ,usu.apellido2 as usuarioapellido2,usu.nombre as usuarionombre"
                + " ,usu.estado as usuarioestado,usu.usucambio as usuariousucambio"
                + " ,usu.fechacambio as usuariofechacambio,usu.mail as usuariomail"
                + " ,usu.telefono as usuariotelefon,usu.idgfh as usuarioidgfh"
                + " ,usu.idcategoria as usuarioidcategoria,usu.movil as usuariomovil"
                + " ,usu.mailprivado as usuariomailprivado,usu.telegram as usuariotegegram"
                + " ,usu.solicita as usuariosolicita"
                + " ,uc.id as usuarioscategoriaid, uc.CODIGOPERSIGO as usuarioscategoriacodigo"
                + " ,uc.nombre as usuarioscategoriaanombre,uc.estado as usuarioscategoriaestado  "
                + ",gfh.id as gfhId,gfh.codigo as gfhcodigo,gfh.descripcion as gfhdescripcion"
                + ",gfh.asistencial as gfhasistencial,gfh.idjimena  as gfhidjimena, gfh.estado as gfhestado,gfh.gfhpersigo "
                + " FROM  funcionalidad  func "
                + "    LEFT JOIN usuarios usu ON usu.id=func.usucambio "
                + "    LEFT JOIN categorias uc ON uc.id=usu.idcategoria "
                + " LEFT JOIN gfh gfh ON usu.idgfh = gfh.id"
                + " WHERE 1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    public FuncionalidadBean getRegistroResulset(ResultSet rs) {
        FuncionalidadBean funcionalidad = new FuncionalidadBean();
        try {

            funcionalidad.setId(rs.getLong("funcid"));
            funcionalidad.setDescripcion(rs.getString("funcdescripcion"));
            funcionalidad.setTextomenu(rs.getString("functextomenu"));
            funcionalidad.setEstado(rs.getBoolean("funcestado"));
            // funcionalidad.setUsucambio(new UsuarioDao().getPorId(rs.getLong("funcfechacambio")));
            funcionalidad.setUsucambio(new UsuarioDao().getRegistroResulset(rs, Boolean.FALSE));
            try {
                LocalDate date = Utilidades.getFechaLocalDate(rs.getLong("funcfechacambio"));
                funcionalidad.setFechacambio(date);
            } catch (SQLException e) {
                logger.error(Utilidades.getStackTrace(e));
            }
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return funcionalidad;
    }

    @Override
    public FuncionalidadBean getPorCodigo(String codigo) {
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
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
     * @param texto
     * @return
     */
    @Override
    public ArrayList<FuncionalidadBean> getLista(String texto) {
        Connection connection = null;
        ArrayList<FuncionalidadBean> lista = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND func.estado=" + ConexionDao.BBDD_ACTIVOSI);

            sql = sql.concat(" order by func.descripcion ");

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

    public Map<String, FuncionalidadBean> getListaMap(String texto) {
        Connection connection = null;
        Map<String, FuncionalidadBean> lista = new HashMap<>();
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND func.estado=" + ConexionDao.BBDD_ACTIVOSI);

            sql = sql.concat(" order by func.descripcion ");

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                FuncionalidadBean funcionalidad = getRegistroResulset(resulSet);
                lista.put(funcionalidad.getTextomenu(), funcionalidad);
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
            sql = sql.concat(" AND  func.estado= " + ConexionDao.BBDD_ACTIVOSI);
            sql = sql.concat(" order by func.descripcion ");

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
    @Override
    public boolean doGrabaDatos(FuncionalidadBean funcionalidad) {
        boolean actualizado = false;
        if (funcionalidad.getId().equals(new Long(0))) {
            actualizado = this.doInsertaDatos(funcionalidad);
        } else {
            actualizado = this.doActualizaDatos(funcionalidad);
        }
        return actualizado;
    }

    /**
     *
     * @param funcionalidad
     * @return
     */
    @Override
    public boolean doInsertaDatos(FuncionalidadBean funcionalidad) {
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
    @Override
    public boolean doActualizaDatos(FuncionalidadBean funcionalidad) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();
            sql = " UPDATE   funcionalidad SET descripcion='" + funcionalidad.getDescripcion() + "',textomenu='"
                    + funcionalidad.getTextomenu() + "',usucambio='" + usuario.getDni() + "',  fechacambio="
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + " WHERE id=" + funcionalidad.getId();

            sql = " UPDATE  funcionalidad  set descripcion =? , textomenu=?"
                    + ", usucambio=?, fechacambio=?, estado=? "
                    + " WHERE id=?  ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if (funcionalidad.getDescripcion() != null) {
                    statement.setString(1, funcionalidad.getDescripcion());
                } else {
                    statement.setNull(1, Types.VARCHAR);
                }
                if (funcionalidad.getTextomenu() != null) {
                    statement.setString(2, funcionalidad.getTextomenu());
                } else {
                    statement.setNull(2, Types.VARCHAR);
                }
                if (funcionalidad.getUsucambio() != null && funcionalidad.getUsucambio().getId() != null) {
                    statement.setLong(3, funcionalidad.getUsucambio().getId());
                } else {
                    statement.setNull(3, Types.INTEGER);
                }
                if (funcionalidad.getFechacambio() != null) {
                    statement.setLong(4, Utilidades.getFechaLong(funcionalidad.getFechacambio()));
                } else {
                    statement.setNull(4, Types.INTEGER);
                }
                if (funcionalidad.getEstado() != null) {
                    statement.setInt(5, funcionalidad.getEstado());
                } else {
                    statement.setNull(5, Types.INTEGER);
                }
                statement.setLong(6, funcionalidad.getId());
                insertadoBoolean = statement.executeUpdate() > 0;
                statement.close();
                logger.debug(sql);
            }

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
    @Override
    public boolean doBorraDatos(FuncionalidadBean funcionalidad) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();
            sql = " DELETE  FROM    funcionalidad WHERE id="
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
            if (usuario.getEstado() != UsuarioBean.USUARIO_ADMINISTRADOR) {
                sql = sql.concat(" AND func.id IN ( SELECT  idfuncionalidad FROM us_funcionalidad "
                        + "WHERE  idusuario=" + usuario.getId() + ") ");
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

    public Map<String, FuncionalidadBean> getListaStrigMap(UsuarioBean usuario) {
        Connection connection = null;
        Map<String, FuncionalidadBean> lista = new HashMap<>();
        try {
            connection = super.getConexionBBDD();
            if (usuario.getEstado() != UsuarioBean.USUARIO_ADMINISTRADOR) {
                sql = sql.concat(" AND func.id IN ( SELECT  idfuncionalidad FROM us_funcionalidad "
                        + "WHERE  idusuario=" + usuario.getId() + ") ");
            }
            sql = sql.concat(" ORDER BY textomenu ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                FuncionalidadBean funcionalidad = getRegistroResulset(resulSet);
                lista.put(funcionalidad.getTextomenu(), funcionalidad);
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
