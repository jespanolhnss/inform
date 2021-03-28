package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.component.notification.Notification;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.JimenaCampos_iBean;
import es.sacyl.gsa.inform.bean.JimenaInformeBean;
import es.sacyl.gsa.inform.bean.PacienteBean;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JimenaDao {

    private static final Logger logger = LogManager.getLogger(JimenaDao.class);

    private static final String sqlInformeBean = "i.*"
            + ",p.ape1,p.ape2,p.nombre,p.id as idpaciente,p.fnac,p.sexo,p.tarjeta,p.nss,p.dni,p.telefono,p.movil,p.nbdp,p.cias"
            + ",h.nhc"
            + ", c.id as idcentro,c.codigo as codigocentro, c.descripcion as descentro,c.nemonico "
            + " , s.id as idservicio, s.codigo as codigoservicio, s.descripcion as descservcicio  "
            + ",u.userid as usuuserid, u.apellido1 as usuapellido1,u.apellido2 as usuapellido2,u.nombre as usunombre, u.categoria as usucategoria,u.estado as usuestado"
            + " FROM informes i "
            + " JOIN centros c ON c.id=i.centro "
            + " JOIN servicios s ON s.id=i.servicio "
            + " JOIN pacientes p ON p.id=i.paciente  "
            + " JOIN historias h ON h.paciente=p.id "
            + " LEFT JOIN usuarios U ON u.userid=i.userid "
            + " WHERE 1=1 ";

    private static final String sqlInformeInterconsultaBean = " SELECT  plantilla_editor"
            + ",i.* "
            + " ,p.ape1,p.ape2,p.nombre,p.id as idpaciente,p.fnac,p.sexo,p.tarjeta,p.nss,p.dni,p.telefono,p.movil,p.nbdp,p.cias,h.nhc, c.id as idcentro,c.codigo as codigocentro, c.descripcion as descentro,c.nemonico  "
            + ", s.id as idservicio, s.codigo as codigoservicio, s.descripcion as descservcicio "
            + ",u.userid as usuuserid, u.apellido1 as usuapellido1,u.apellido2 as usuapellido2,u.nombre as usunombre, u.categoria as usucategoria,u.estado as usuestado"
            + ", sd.id as idserviciodestino, sd.codigo as codigoserviciodestino, sd.descripcion as descservciciodestino "
            + ",t.CENTRODESTINO "
            + "FROM informes i  "
            + " JOIN centros c ON c.id=i.centro  "
            + "JOIN servicios s ON s.id=i.servicio "
            + "JOIN pacientes p ON p.id=i.paciente "
            + "JOIN historias h ON h.paciente=p.id "
            + "LEFT JOIN usuarios U ON u.userid=i.userid"
            + " JOIN interconsultas t ON t.informe=i.id  "
            + " JOIN servicios sd ON sd.id=t.SERVICIODESTINO "
            + "WHERE 1=1 ";

    public JimenaDao() {
    }

    public Connection conecta() {
        Connection conn = null;
        String dbURL2;
        String username;
        String password;

        ParametroBean parametroBean = new ParametroDao().getPorCodigo(ParametroBean.URL_CONEXION_JIMENA);
        if (parametroBean == null) {
            Notification.show("Sin definir parámetro " + ParametroBean.URL_CONEXION_JIMENA, 10000, Notification.Position.MIDDLE);
        } else {
            String cadena = new ParametroDao().getPorCodigo(ParametroBean.URL_CONEXION_JIMENA).getValor();
            String[] conex = cadena.split("\\|");
            dbURL2 = "jdbc:oracle:thin:@" + conex[0];
            username = conex[1];
            password = conex[2];
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                conn = DriverManager.getConnection(dbURL2, username, password);
                logger.debug("Conexion con bbdd" + dbURL2 + " realizada.");
            } catch (ClassNotFoundException ex) {
                logger.error("Error de conexión con bbdd ", ex);
            } catch (SQLException ex) {
                logger.error("Error de conexión sql con bbdd ", ex);
            }
        }
        return conn;
    }

    public UsuarioBean getUsuarioBean(String dni) {
        String sql;
        Connection conn = this.conecta();
        UsuarioBean usuario = null;
        if (conn != null) {
            sql = " SELECT  * FROM usuarios WHERE userid='" + dni.trim().toUpperCase() + "'";
            try {
                Statement statement = conn.createStatement();
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    usuario = getUsuarioBeanResulset(resulSet);
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
        return usuario;
    }

    public UsuarioBean getUsuarioBeanResulset(ResultSet resulSet) {
        UsuarioBean usuario = null;
        try {
            usuario = new UsuarioBean();
            usuario.setDni(resulSet.getString("userid"));
            usuario.setApellido1(resulSet.getString("apellido1"));
            usuario.setApellido2(resulSet.getString("apellido2"));
            usuario.setNombre(resulSet.getString("nombre"));
            usuario.setMail(resulSet.getString("email"));
            usuario.setEstado(resulSet.getInt("estado"));
            usuario.setTelefono(resulSet.getString("telefono"));
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return usuario;
    }

    public CentroBean getCentroBeanResulset(ResultSet resulSet) {
        CentroBean centro = null;
        try {
            centro = new CentroBean();
            centro.setCodigo(resulSet.getString("codigo"));
            centro.setNomcen(resulSet.getString("descripcion"));
            //	centro.setArea(resulSet.getString("area"));
            //    centro.setLocalidad(resulSet.getString("localidad"));
            // 	centro.setTipo(resulSet.getInt("tipo"));
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return centro;
    }

    public PacienteBean getPaciente(String nhc) {
        String sql;
        Connection conn = this.conecta();
        PacienteBean paciente = null;
        if (conn != null) {
            sql = " SELECT  p.*,h.nhc FROM pacientes  p " + "JOIN historias h ON h.paciente = p.id " + " WHERE  nhc='"
                    + nhc + "' ";
            try {
                Statement statement = conn.createStatement();
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    paciente = getPacienteResulset(resulSet);
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
        return paciente;
    }

    public PacienteBean getPacienteResulset(ResultSet resulSet) {
        PacienteBean paciente = new PacienteBean();
        try {

            /*
			 * ID APELLIDOSNOMBRE NUMEROHC ESTADO FECHACAMBIO USUCAMBIO IDJIMENA
             */
            String apellidosnombre = resulSet.getString("ape1").trim() + " " + resulSet.getString("ape2").trim() + ","
                    + resulSet.getString("nombre").trim();
            paciente.setIdJimena(resulSet.getLong("id"));
            paciente.setApellidosnombre(apellidosnombre);
            paciente.setNumerohc(resulSet.getString("nhc"));
            paciente.setEstado(ConexionDao.BBDD_ACTIVOSI);
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return paciente;
    }

    /*
    El paciente que pasamos como parámetros es un paciente LOPD
    por lo que buscamos por NHC en lugar de por el id
     */
    public ArrayList<JimenaInformeBean> getListaInformesPaciente(PacienteBean paciente, GfhBean servicio, LocalDate desde, Integer canal, int orden, Integer estado) {
        String sql = "";
        Connection connection = this.conecta();
        ArrayList<JimenaInformeBean> listaInformeBeans = new ArrayList<JimenaInformeBean>();

        try {
            switch (orden) {
                case JimenaInformeBean.ORDENFECHA:
                    sql = " SELECT   row_number() over (ORDER BY i.fecha,i.hora  ) as numeroorden , ";
                    break;
                case JimenaInformeBean.ORDENFECHADESC:
                    sql = " SELECT   row_number() over (ORDER BY i.fecha desc,i.hora desc ) as numeroorden ,";
                    break;
            }
            sql = sql.concat(sqlInformeBean);
            sql = sql.concat(" AND i.estado=" + estado);
            if (paciente != null) {
                sql = sql.concat(" AND i.paciente='" + paciente.getIdJimena() + "'");
            }
            if (servicio != null && !servicio.getIdjimena().equals(new Long(0))) {
                sql = sql.concat(" AND i.servicio=" + servicio.getIdjimena());
            }
            if (desde != null) {
                sql = sql.concat(" AND i.fecha>=" + Utilidades.getFechaLong(desde));
            }
            if (canal != null) {
                sql = sql.concat("   AND i.canal=" + canal);
            }
            switch (orden) {
                case JimenaInformeBean.ORDENFECHA:
                    sql = sql.concat("  ORDER BY i.fecha,hora ");
                    break;
                case JimenaInformeBean.ORDENFECHADESC:
                    sql = sql.concat("  ORDER BY i.fecha DESC, i.hora DESC");
                    break;
            }
            logger.debug(sql);
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            int contador = 0;
            while (resulSet.next()) {
                /*
                    CentroBean centroBd = new CentroBean(resulSet.getLong("idcentro"), resulSet.getString("codigocentro"),
                            resulSet.getString("descentro"), resulSet.getString("nemonico"));
                 */
                GfhBean servicioBd = new GfhBean();
                servicioBd.setAsistencial(GfhBean.ASISTENCIASI);
                servicioBd.setCodigo(resulSet.getString("codigoservicio"));
                servicioBd.setDescripcion(resulSet.getString("descservcicio"));
                servicioBd.setIdjimena(resulSet.getLong("idservicio"));
                UsuarioBean usuarioBd = new UsuarioDao().getUsuairoResulSetJimena(resulSet, null);
                JimenaInformeBean informe = getInformeResulsetJimena(resulSet, false, paciente, null, servicioBd, usuarioBd, estado);
                informe.setNumeroOrden(resulSet.getInt("numeroorden"));
                listaInformeBeans.add(informe);

            }
            statement.close();
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        }

        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }

        return listaInformeBeans;
    }

    public ArrayList<JimenaInformeBean> getListaInterconsultasServicio(GfhBean servicio, LocalDate fechamenorque, Integer canal, int orden, Integer estado) {
        String sql = "";
        Connection connection = this.conecta();
        ArrayList<JimenaInformeBean> listaInformeBeans = new ArrayList<JimenaInformeBean>();

        try {

            sql = sqlInformeInterconsultaBean;
            sql = sql.concat("and i.plantilla_editor in (select id from formularios   where ambito= 14 and activo=3) ");
            sql = sql.concat(" AND i.estado=" + estado);

            if (servicio != null && !servicio.getIdjimena().equals(new Long(0))) {
                sql = sql.concat(" AND t.serviciodestino=" + servicio.getIdjimena());
            }
            if (fechamenorque != null) {
                sql = sql.concat(" AND i.fecha<=" + Utilidades.getFechaLong(fechamenorque));
            }
            if (canal != null) {
                sql = sql.concat("   AND i.canal=" + canal);
            }
            switch (orden) {
                case JimenaInformeBean.ORDENFECHA:
                    sql = sql.concat("  ORDER BY i.fecha,hora ");
                    break;
                case JimenaInformeBean.ORDENFECHADESC:
                    sql = sql.concat("  ORDER BY i.fecha DESC, i.hora DESC");
                    break;
            }

            logger.debug(sql);
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            int contador = 0;
            while (resulSet.next()) {
                /*
                    CentroBean centroBd = new CentroBean(resulSet.getLong("idcentro"), resulSet.getString("codigocentro"),
                            resulSet.getString("descentro"), resulSet.getString("nemonico"));
                 */
                GfhBean servicioBd = new GfhBean();
                servicioBd.setAsistencial(GfhBean.ASISTENCIASI);
                servicioBd.setCodigo(resulSet.getString("codigoservicio"));
                servicioBd.setDescripcion(resulSet.getString("descservcicio"));
                servicioBd.setIdjimena(resulSet.getLong("idservicio"));
                UsuarioBean usuarioBd = new UsuarioDao().getUsuairoResulSetJimena(resulSet, null);
                JimenaInformeBean informe = getInformeResulsetJimena(resulSet, false, null, null, servicioBd, usuarioBd, estado);
                GfhBean servicioDestino = new GfhBean();
                servicioBd.setAsistencial(GfhBean.ASISTENCIASI);
                servicioBd.setCodigo(resulSet.getString("descservciciodestino"));
                servicioBd.setDescripcion(resulSet.getString("codigoserviciodestino"));
                servicioBd.setIdjimena(resulSet.getLong("idserviciodestino"));
                informe.setInterconsultaServicioDestino(servicioDestino);
                listaInformeBeans.add(informe);
            }
            statement.close();
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        }

        try {
            connection.close();
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return listaInformeBeans;
    }

    /**
     *
     * @param id
     * @return
     */
    public Blob getBlobPdfInformeBean(Long id, Connection connectionParam) {
        String sql = "";
        Connection connection;
        if (connectionParam != null) {
            connection = connectionParam;
        } else {
            connection = this.conecta();
        }
        Statement statement;
        java.sql.Blob pdfBlob = null;
        try {
            sql = "SELECT docubin  FROM informes  WHERE id=" + id;
            logger.debug(sql);
            statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            if (resulSet.next()) {
                pdfBlob = resulSet.getBlob("docubin");
            }
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + id);

        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        try {
            if (connectionParam == null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error(ConexionDao.ERROR_CLOSE_BBDD_SQL, e);
        }
        return pdfBlob;
    }

    public File getFicheroPdfInformeBean(JimenaInformeBean informe) {
        File file = null;
        FileOutputStream outpu = null;
        InputStream inStream = null;
        Connection connection = this.conecta();
        try {
            String pathname = informe.getPathAbsolutePdf();
            file = new File(pathname);
            outpu = new FileOutputStream(file);
            Blob archivo = this.getBlobPdfInformeBean(informe.getId(), connection);
            inStream = archivo.getBinaryStream();
            int size = (int) archivo.length();
            byte[] buffer = new byte[size];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outpu.write(buffer, 0, length);
            }

        } catch (Exception ioe) {
            logger.error(Utilidades.getStackTrace(ioe));
        } finally {
            try {
                outpu.close();
                inStream.close();
                connection.close();
            } catch (Exception ioe) {
                logger.error("Cerrendo objetos de ficheros y conexiones ", ioe);
            }
        }
        informe.setFicheroInformeFile(file);
        return file;
    }

    public ArrayList<JimenaCampos_iBean> getListaCamposInforme(Long id, Integer estado) {
        String sql = "";
        Connection connection = this.conecta();
        Statement statement;
        ArrayList<JimenaCampos_iBean> listaCampos = new ArrayList<>();
        try {
            sql = "SELECT *  FROM campos_i  WHERE informe = " + id;
            if (estado != null) {
                sql = sql.concat(" AND estado=" + estado);
            }
            sql = sql.concat(" ORDER BY  id desc ");
            logger.debug(sql);

            statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                JimenaCampos_iBean campo_i = JimenaCampos_iBean.getCamposResulSet(resulSet);
                listaCampos.add(campo_i);
            }
            statement.close();
        } catch (SQLException e) {
            logger.error(sql, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(ConexionDao.ERROR_CLOSE_BBDD_SQL, e);
            }
        }
        return listaCampos;
    }

    /**
     * Este método retorna un objeto RespuestaSql para insertar los resultados
     * en la tabla de acciones asociado a la incidencia
     */
    public String doUpdateInformeEstado(Long id, int estadoNuevo) {
        String sql = null;
        Connection connection = this.conecta();
        ResultSet resulSet;
        try {
            sql = "UPDATE  informes SET estado= " + estadoNuevo + "  WHERE id = " + id;
            logger.debug(sql);
            Statement statement = connection.createStatement();
            Boolean updated = statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
            sql = null;
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
            sql = null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(ConexionDao.ERROR_CLOSE_BBDD_SQL, e);
            }
        }
        return sql;
    }

    /**
     *
     * @param id
     * @return Un string con las sentencia sql que ha ejecutado
     */
    public String doUpdateCampos_iEstado(Long id, int estadonuevo) {
        String sql = null;
        Connection connection = this.conecta();
        try {
            sql = "UPDATE  campos_i SET estado= " + estadonuevo + "  WHERE informe = " + id;
            logger.debug(sql);
            Statement statement = connection.createStatement();
            Boolean updated = statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            logger.error(sql, e);
            sql = null;
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
            sql = null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(ConexionDao.ERROR_CLOSE_BBDD_SQL, e);
            }
        }
        return sql;
    }

    /**
     *
     * @param id
     * @return
     */
    public String doUpdateInformeDesconsolida(Long id) {
        String sql = null;
        Connection connection = this.conecta();
        ResultSet resulSet;
        try {
            sql = "UPDATE  informes SET   estado = 1, tipobin = 0, tipoxml = 0   WHERE id = " + id;
            logger.debug(sql);
            Statement statement = connection.createStatement();
            Boolean updated = statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
            sql = null;
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
            sql = null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(ConexionDao.ERROR_CLOSE_BBDD_SQL, e);
            }
        }
        return sql;
    }

    /**
     *
     * @param rs
     * @param conCampos_I
     * @param paciente
     * @param centro
     * @param servicio
     * @param usuario
     * @param estado
     * @return
     */
    public JimenaInformeBean getInformeResulsetJimena(ResultSet rs, boolean conCampos_I, PacienteBean paciente, CentroBean centro,
            GfhBean servicio, UsuarioBean usuario, Integer estado) {
        JimenaInformeBean informe = new JimenaInformeBean();

        try {
            //   informe.setNumeroOrden(rs.getInt("numeroorden"));
            informe.setId(rs.getLong("id"));
            informe.setDescripcion(rs.getString("descripcion"));
            if (paciente == null) {
                // informe.setPacienteBean(new PacienteBeanDAO().getPacienteBeanPorId(rs.getLong("paciente"), false));
            } else {
                informe.setPacienteBean(paciente);
            }
            //  informe.setProblema(new Proceso(rs.getLong("problema")));
            informe.setEpisodio(rs.getLong("episodio"));
            if (centro == null) {
                //  informe.setCentro(new CentroDAO().getRegistroId(rs.getLong("centro")));
            } else {
                informe.setCentro(centro);
            }
            if (servicio == null) {
                //   informe.setServicioBean(new ServicioBeansDAO().getRegistroId(rs.getLong("servicio")));
            } else {
                informe.setServicioBean(servicio);
            }
            if (usuario != null) {
                informe.setUserid(usuario);
            }
            informe.setReferecia(rs.getString("referencia"));
            informe.setFecha(Utilidades.getFechaLocalDate(rs.getLong("fecha")));
            informe.setHora(rs.getLong("hora"));
            informe.setEstado(rs.getInt("estado"));
            // informe.setDocuxml(docuxml);
            informe.setTipoxml(rs.getInt("tipoxml"));
            // informe.setDocubin(docubin);
            informe.setTipobin(rs.getInt("tipobin"));
            informe.setPeticion(rs.getLong("peticion"));
            //    informe.setUserid(new UsuarioBeanDAO().getUsuarioBeanUserid(rs.getString("userid"), false));
            informe.setCanal(rs.getLong("canal"));
            informe.setTipoinforme(rs.getInt("tipoinforme"));
            informe.setUseridauth(new UsuarioBean(rs.getString("useridauth")));
            //     informe.setSrvauth(new GfhBean(rs.getLong("srvauth")));
            informe.setUseridredactor(new UsuarioBean(rs.getString("useridredactor")));

            informe.setPlantalla_editor(rs.getLong("plantilla_editor"));

            informe.setFlag(rs.getInt("flag"));
            informe.setPertenece(rs.getLong("pertenece"));
            informe.setVersion(rs.getInt("version"));
            informe.setNive_visibilidad(rs.getInt("nivel_visibilidad"));
            informe.setSubservicio(rs.getLong("subservicio"));
            informe.setUseridpeticionario(new UsuarioBean(rs.getString("useridpeticionario")));
            informe.setVisto(rs.getInt("visto"));
            informe.setUltimoguardado(rs.getLong("ultimoguardado"));
            informe.setBloqueado(rs.getInt("bloqueado"));
            informe.setAlmacenamiento(rs.getLong("almacenamiento"));
            informe.setTipo_documento(rs.getLong("tipo_documento"));
            informe.setAmbito(rs.getLong("ambito"));
            //     informe.setServicioBean_realizador(new GfhBean(rs.getLong("servicio_realizador")));
            informe.setFecha_proceso(Utilidades.getFechaLocalDateTime(rs.getLong("fecha_proceso")));
            informe.setReferencia_almacenamiento(rs.getString("referencia_almacenamiento"));
            informe.setNum_accesos(rs.getInt("num_accesos"));
            //     informe.setProblema(new Proceso(rs.getLong("problema")));
            informe.setUser_visto(new UsuarioBean(rs.getString("user_visto")));
            informe.setFecha_visto(rs.getLong("fecha_visto"));
            informe.setComentario_Visto(rs.getString("comentario_visto"));
            if (conCampos_I == true) {
                informe.setListaCampos(new JimenaDao().getListaCamposInforme(informe.getId(), estado));
            }
        } catch (SQLException e) {
            logger.error("Error resulet ", e);
        }
        return informe;
    }

    public ArrayList<CentroBean> getListaCentrosPrimaria() {
        String sql = "";
        Connection connection = this.conecta();
        Statement statement;
        ArrayList<CentroBean> listaCentros = new ArrayList<>();
        try {
            sql = "SELECT *  FROM centros  WHERE area = 17 and tipo = 2 ORDER BY  id";
            logger.debug(sql);

            statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                CentroBean centros = getCentroBeanResulset(resulSet);
                listaCentros.add(centros);
            }
            statement.close();
        } catch (SQLException e) {
            logger.error(sql, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(ConexionDao.ERROR_CLOSE_BBDD_SQL, e);
            }
        }
        return listaCentros;
    }

    public ArrayList<GfhBean> getListaServiciosPaciente(String nhc) {
        String sql = "";
        Connection connection = this.conecta();
        Statement statement;
        ArrayList<GfhBean> listaServicios = new ArrayList<>();
        try {
            sql = "SELECT UNIQUE i.servicio,s.codigo,s.descripcion "
                    + " FROM informes i"
                    + " JOIN historias h ON h.paciente=i.paciente "
                    + " JOIN servicios s On s.id=i.servicio "
                    + " WHERE nhc='" + nhc + "'"
                    + "  ORDER BY 1";
            logger.debug(sql);

            statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                GfhBean servicioBean = new GfhBean();
                servicioBean.setCodigo(resulSet.getString("codigo"));
                servicioBean.setIdjimena(resulSet.getLong("servicio"));
                servicioBean.setDescripcion(resulSet.getString("descripcion"));
                listaServicios.add(servicioBean);
            }
            statement.close();
        } catch (SQLException e) {
            logger.error(sql, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(ConexionDao.ERROR_CLOSE_BBDD_SQL, e);
            }
        }
        return listaServicios;
    }

    public ArrayList<GfhBean> getListaServicios() {
        String sql = "";
        Connection connection = this.conecta();
        Statement statement;
        ArrayList<GfhBean> listaServicios = new ArrayList<>();
        try {
            sql = " SELECT  unique s.codigo as serviciocodigo"
                    + " ,s.id as servicioid "
                    + ",s.descripcion  as serviciodescripcion FROM servicios_centro c "
                    + "JOIN servicios s  On c.servicio=s.id"
                    + "  ORDER BY s.codigo  ";
            logger.debug(sql);

            statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                GfhBean servicioBean = new GfhBean();
                servicioBean.setIdjimena(resulSet.getLong("servicioid"));
                servicioBean.setDescripcion(resulSet.getString("serviciodescripcion"));
                servicioBean.setCodigo(resulSet.getString("serviciocodigo"));
                listaServicios.add(servicioBean);
            }
            statement.close();
        } catch (SQLException e) {
            logger.error(sql, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(ConexionDao.ERROR_CLOSE_BBDD_SQL, e);
            }
        }
        return listaServicios;
    }

}
