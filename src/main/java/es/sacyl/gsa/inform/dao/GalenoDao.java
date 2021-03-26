package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.bean.UsuarioGalenoBean;
import es.sacyl.gsa.inform.ctrl.IpCtrl;
import es.sacyl.gsa.inform.util.Utilidades;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
    private static String sqlUsuarios = "select usuario,nombre,nomb,apellido1,apellido2 " + 
            ",avisos,dirftp,grupousu,centrohis,unidadenf,hisingresado,hisconsultas,hislistadeespera " + 
            ",siglobloque,siglom1,siglom2,siglom3,sigloc,cs,fecha,activo,caducidad,dondetrabaja " + 
            ",telefono_contacto,gfhgeshis,tur_colectivos,email,email_oficial,maes_servhis,hdia_areas " + 
            ",botiquinesfar,ambito,ambitohos,cargo,divisioncontratos,pwddefecto,comentario " + 
            ",fechagraba,usugraba,nuevomodificado from galeno_usuarios where 1 = 1 ";

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
    
    /**
     * Devuelve un UsuarioGalenoBean con los datos almacenados en el resultSet
     * @param resulSet
     * @return 
     */
    public UsuarioGalenoBean getResulset(ResultSet resulSet) {
        UsuarioGalenoBean usuario = null;
        try {
            usuario = new UsuarioGalenoBean();
            usuario.setDni(resulSet.getString("usuario"));
            usuario.setNombreCompleto(resulSet.getString("nombre"));
            usuario.setNombre(resulSet.getString("nomb"));
            usuario.setApellido1(resulSet.getString("apellido1"));
            usuario.setApellido2(resulSet.getString("apellido2"));
            usuario.setAvisos(resulSet.getString("avisos"));           
            usuario.setDirftp(resulSet.getString("dirftp"));
            usuario.setGrupousu(resulSet.getString("grupousu"));
            usuario.setCentrohis(resulSet.getString("centrohis"));
            usuario.setUnidadenf(resulSet.getString("unidadenf"));
            usuario.setHisingresado(resulSet.getString("hisingresado"));            
            usuario.setHisconsultas(resulSet.getString("hisconsultas"));
            usuario.setHislistadeespera(resulSet.getString("hislistadeespera"));           
            usuario.setSiglobloque(resulSet.getString("siglobloque"));            
            usuario.setSiglom1(resulSet.getString("siglom1"));
            usuario.setSiglom2(resulSet.getString("siglom2"));
            usuario.setSiglom3(resulSet.getString("siglom3"));            
            usuario.setSigloc(resulSet.getString("sigloc"));
            usuario.setCs(resulSet.getString("cs"));
            usuario.setFecha(resulSet.getDate("fecha").toLocalDate());
            usuario.setActivo(resulSet.getString("activo"));
            usuario.setCaducidad(resulSet.getDate("caducidad").toLocalDate());
            usuario.setDondetrabaja(resulSet.getString("dondetrabaja"));
            usuario.setTelefono(resulSet.getString("telefono_contacto"));
            usuario.setGfhgeshis(resulSet.getString("gfhgeshis"));
            usuario.setTur_colectivos(resulSet.getString("tur_colectivos"));
            usuario.setMail(resulSet.getString("email"));
            usuario.setEmail_oficial(resulSet.getString("email_oficial"));
            usuario.setMaes_servhis(resulSet.getString("maes_servhis"));
            usuario.setHdia_areas(resulSet.getString("hdia_areas"));
            usuario.setBotiquinesfar(resulSet.getString("botiquinesfar"));
            usuario.setAmbito(resulSet.getString("ambito"));
            usuario.setAmbitohos(resulSet.getString("ambitohos"));
            usuario.setCargo(resulSet.getString("cargo"));
            usuario.setDivisioncontratos(resulSet.getString("divisioncontratos"));
            usuario.setPwddefecto(resulSet.getString("pwddefecto"));
            usuario.setComentario(resulSet.getString("comentario"));
            usuario.setFechacambio(resulSet.getDate("fechagraba").toLocalDate());            
            usuario.setUsucambio(new UsuarioDao().getUsuarioDni(resulSet.getString("usuacambio"), Boolean.FALSE));
            usuario.setNuevomodificado(resulSet.getString("nuevomodificado"));            
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return usuario;
    }
    
    /**
     * Comprueba si existe el usuario
     * @param usuario
     * @return 
     */
    public boolean existeUsuario(String usuario) {
        
        Connection conn = this.conecta();
        Boolean existe = false;
        
        if (conn != null) {
            try {
                sqlUsuarios = sqlUsuarios + " and usuario = " + usuario;                
                Statement statement = conn.createStatement();
                existe = statement.execute(sqlUsuarios);
                existe = true;
                statement.close();
            } catch (SQLException e) {
                logger.error(sqlUsuarios + Utilidades.getStackTrace(e));
            } catch (Exception e) {
                logger.error(Utilidades.getStackTrace(e));
            }  finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(Utilidades.getStackTrace(e));                    
                }                
            }            
        }       
        return existe;
    }
    
    /**
     * Llama al método de insertar o al de actualizar según corresponda
     * @param usuario
     * @return 
     */
    public boolean doGrabaDatos(UsuarioGalenoBean usuario) {
        boolean actualizado = false;
        if (existeUsuario(usuario.getDni()) == true) {
            doActualizaUsuario(usuario);
            actualizado = true;
        } else {
            doInsertaUsuario(usuario);
            actualizado = true;
        }
        return actualizado;
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
    
    /**
     * Devuelve el usuario que tiene el dni que pasamos como parámetro
     * @param dni
     * @return 
     */
    public UsuarioGalenoBean getPorDni(String dni) {
        
        Connection conn = this.conecta();
        UsuarioGalenoBean usuario = null;
        
        if (conn != null) {
            try {
                sqlUsuarios = sqlUsuarios + " and usuario = " + dni;                
                Statement statement = conn.createStatement();
                ResultSet resulSet = statement.executeQuery(sqlUsuarios);
                while (resulSet.next()) {
                    usuario = getResulset(resulSet);
                }
                statement.close();
                logger.debug(sqlUsuarios);
            } catch (SQLException e) {
                logger.error(sqlUsuarios + Utilidades.getStackTrace(e));
            } catch (Exception e) {
                logger.error(Utilidades.getStackTrace(e));
            }  finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(Utilidades.getStackTrace(e));                    
                }                
            }            
        } 
        return usuario;
    }
    
    /**
     * Inserta un usuario nuevo
     * @param usuario
     * @return 
     */
    public boolean doInsertaUsuario(UsuarioGalenoBean usuario) {
        
        Connection conn = this.conecta();
        Boolean insertado = false;
        String nombreCompleto;
        String nombreCompletoNombre;
        String nombreCompletoApellido1;
        String nombreCompletoApellido2;
        String sql = "insert into galeno_usuarios(usuario,nombre,nomb,apellido1,apellido2 " + 
                ",avisos,dirftp,grupousu,centrohis,unidadenf,hisingresado,hisconsultas,hislistadeespera " + 
                ",siglobloque,siglom1,siglom2,siglom3,sigloc,cs,fecha,activo,caducidad,dondetrabaja " + 
                ",telefono_contacto,gfhgeshis,tur_colectivos,email,email_oficial,maes_servhis,hdia_areas " + 
                ",botiquinesfar,ambito,ambitohos,cargo,divisioncontratos,pwddefecto,comentario " + 
                ",fechagraba,usugraba,nuevomodificado) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" + 
                ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";  ;
        
        if (conn != null) {
            try {              
                PreparedStatement statement = conn.prepareStatement(sql);
                if (usuario.getDni() != null) {
                    statement.setString(1, usuario.getDni());
                } else {
                    statement.setNull(1, Types.CHAR);
                }
                if (usuario.getApellidosNombre() != null) {
                    statement.setString(2, usuario.getApellidosNombre());
                } else {
                    statement.setNull(2, Types.CHAR);
                }    
                if (usuario.getNombre() != null) {
                    statement.setString(3, usuario.getNombre());
                } else {
                    statement.setNull(3, Types.CHAR);
                }    
                if (usuario.getApellido1() != null) {
                    statement.setString(4, usuario.getApellido1());
                } else {
                    statement.setNull(4, Types.CHAR);
                }
                if (usuario.getApellido2() != null) {
                    statement.setString(5, usuario.getApellido1());
                } else {
                    statement.setNull(5, Types.CHAR);
                }               
                if (usuario.getAvisos()!= null) {
                    statement.setString(6, usuario.getAvisos());
                } else {
                    statement.setNull(6, Types.CHAR);
                }
                if (usuario.getDirftp() != null) {
                    statement.setString(7, usuario.getDirftp());
                } else {
                    statement.setNull(7, Types.CHAR);
                }
                if (usuario.getGrupousu()!= null) {
                    statement.setString(8, usuario.getGrupousu());
                } else {
                    statement.setNull(8, Types.CHAR);
                }
                if (usuario.getCentrohis()!= null) {
                    statement.setString(9, usuario.getCentrohis());
                } else {
                    statement.setNull(9, Types.CHAR);
                }
                if (usuario.getUnidadenf()!= null) {
                    statement.setString(10, usuario.getUnidadenf());
                } else {
                    statement.setNull(10, Types.CHAR);
                }
                if (usuario.getHisingresado()!= null) {
                    statement.setString(11, usuario.getHisingresado());
                } else {
                    statement.setNull(11, Types.CHAR);
                }
                if (usuario.getHisconsultas()!= null) {
                    statement.setString(12, usuario.getHisconsultas());
                } else {
                    statement.setNull(12, Types.CHAR);
                }
                if (usuario.getHislistadeespera()!= null) {
                    statement.setString(13, usuario.getHislistadeespera());
                } else {
                    statement.setNull(13, Types.CHAR);
                }
                if (usuario.getSiglobloque()!= null) {
                    statement.setString(14, usuario.getSiglobloque());
                } else {
                    statement.setNull(14, Types.CHAR);
                }
                if (usuario.getSiglom1()!= null) {
                    statement.setString(15, usuario.getSiglom1());
                } else {
                    statement.setNull(15, Types.CHAR);
                }
                if (usuario.getSiglom2()!= null) {
                    statement.setString(16, usuario.getSiglom2());
                } else {
                    statement.setNull(16, Types.CHAR);
                }
                if (usuario.getSiglom3()!= null) {
                    statement.setString(17, usuario.getSiglom3());
                } else {
                    statement.setNull(17, Types.CHAR);
                }
                if (usuario.getSigloc()!= null) {
                    statement.setString(18, usuario.getSigloc());
                } else {
                    statement.setNull(18, Types.CHAR);
                }
                if (usuario.getCs()!= null) {
                    statement.setString(19, usuario.getCs());
                } else {
                    statement.setNull(19, Types.CHAR);
                }
                if (usuario.getFecha()!= null) {
                    statement.setDate(20, Date.valueOf(usuario.getFecha()));
                } else {
                    statement.setNull(20, Types.DATE);
                }
                if (usuario.getActivo()!= null) {
                    statement.setString(21, usuario.getActivo());
                } else {
                    statement.setNull(21, Types.CHAR);
                }
                if (usuario.getCaducidad()!= null) {
                    statement.setDate(22, Date.valueOf(usuario.getCaducidad()));
                } else {
                    statement.setNull(22, Types.DATE);
                }
                if (usuario.getDondetrabaja()!= null) {
                    statement.setString(23, usuario.getDondetrabaja());
                } else {
                    statement.setNull(23, Types.CHAR);
                }
                if (usuario.getTelefono()!= null) {
                    statement.setString(24, usuario.getTelefono());
                } else {
                    statement.setNull(24, Types.CHAR);
                }
                if (usuario.getGfhgeshis()!= null) {
                    statement.setString(25, usuario.getGfhgeshis());
                } else {
                    statement.setNull(25, Types.CHAR);
                }
                if (usuario.getTur_colectivos()!= null) {
                    statement.setString(26, usuario.getTur_colectivos());
                } else {
                    statement.setNull(26, Types.CHAR);
                }
                if (usuario.getMail()!= null) {
                    statement.setString(27, usuario.getMail());
                } else {
                    statement.setNull(27, Types.CHAR);
                }
                if (usuario.getEmail_oficial()!= null) {
                    statement.setString(28, usuario.getEmail_oficial());
                } else {
                    statement.setNull(28, Types.CHAR);
                }
                if (usuario.getMaes_servhis()!= null) {
                    statement.setString(29, usuario.getMaes_servhis());
                } else {
                    statement.setNull(29, Types.CHAR);
                }
                if (usuario.getHdia_areas()!= null) {
                    statement.setString(30, usuario.getHdia_areas());
                } else {
                    statement.setNull(30, Types.CHAR);
                }
                if (usuario.getBotiquinesfar()!= null) {
                    statement.setString(31, usuario.getBotiquinesfar());
                } else {
                    statement.setNull(31, Types.CHAR);
                }
                if (usuario.getAmbito()!= null) {
                    statement.setString(32, usuario.getAmbito());
                } else {
                    statement.setNull(32, Types.CHAR);
                }
                if (usuario.getAmbitohos()!= null) {
                    statement.setString(33, usuario.getAmbitohos());
                } else {
                    statement.setNull(33, Types.CHAR);
                }
                if (usuario.getCargo()!= null) {
                    statement.setString(34, usuario.getCargo());
                } else {
                    statement.setNull(34, Types.CHAR);
                }
                if (usuario.getDivisioncontratos()!= null) {
                    statement.setString(35, usuario.getDivisioncontratos());
                } else {
                    statement.setNull(35, Types.CHAR);
                }
                if (usuario.getPwddefecto()!= null) {
                    statement.setString(36, usuario.getPwddefecto());
                } else {
                    statement.setNull(36, Types.CHAR);
                }
                if (usuario.getComentario()!= null) {
                    statement.setString(37, usuario.getComentario());
                } else {
                    statement.setNull(37, Types.CHAR);
                }
                if (usuario.getFechacambio()!= null) {
                    statement.setDate(38, Date.valueOf(usuario.getFechacambio()));
                } else {
                    statement.setNull(38, Types.DATE);
                }
                if (usuario.getUsucambio()!= null) {
                    statement.setString(39, usuario.getUsucambio().getDni());
                } else {
                    statement.setNull(39, Types.CHAR);
                }
                if (usuario.getNuevomodificado()!= null) {
                    statement.setString(40, usuario.getNuevomodificado());
                } else {
                    statement.setNull(40, Types.CHAR);
                }
                
                insertado = statement.execute(sql);
                statement.close();
                insertado = true;
            } catch (SQLException e) {
                logger.error(sql + Utilidades.getStackTrace(e));
            } catch (Exception e) {
                logger.error(Utilidades.getStackTrace(e));
            }  finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(Utilidades.getStackTrace(e));                    
                }                
            }            
        }       
        
        return insertado;        
    }   
    
    /**
     * Actualiza un usuario existente
     * @param usuario
     * @return 
     */
    public boolean doActualizaUsuario(UsuarioGalenoBean usuario) {
        
        Connection conn = this.conecta();
        Boolean actualizado = false;
        String nombreCompleto;
        String nombreCompletoNombre;
        String nombreCompletoApellido1;
        String nombreCompletoApellido2;
        String sql = "update galeno_usuarios set nombre = ?,nomb = ?,apellido1 = ? " + 
                ",apellido2 = ?,avisos = ?,dirftp = ?,grupousu = ?,centrohis = ?,unidadenf = ? " + 
                ",hisingresado = ?,hisconsultas = ?,hislistadeespera = ?,siglobloque = ?,siglom1 = ? " + 
                ",siglom2 = ?,siglom3 = ?,sigloc = ?,cs = ?,fecha = ?,activo = ?,caducidad = ? " + 
                ",dondetrabaja = ?,telefono_contacto = ?,gfhgeshis = ?,tur_colectivos = ? " + 
                ",email = ?,email_oficial = ?,maes_servhis = ?,hdia_areas = ?,botiquinesfar = ? " + 
                ",ambito = ?,ambitohos = ?,cargo = ?,divisioncontratos = ?,pwddefecto = ? " + 
                ",comentario = ?,fechagraba = ?,usugraba = ?,nuevomodificado = ? where usuario = ?";
        
        if (conn != null) {
            try {                        
                PreparedStatement statement = conn.prepareStatement(sql);
                
                if (usuario.getApellidosNombre()!= null) {
                    statement.setString(1, usuario.getApellidosNombre());
                } else {
                    statement.setNull(1, Types.CHAR);
                }
                if (usuario.getNombre()!= null) {
                    statement.setString(2, usuario.getNombre());
                } else {
                    statement.setNull(2, Types.CHAR);
                }
                if (usuario.getApellido1()!= null) {
                    statement.setString(3, usuario.getApellido1());
                } else {
                    statement.setNull(3, Types.CHAR);
                }
                if (usuario.getApellido2()!= null) {
                    statement.setString(4, usuario.getApellido2());
                } else {
                    statement.setNull(4, Types.CHAR);
                }
                if (usuario.getAvisos()!= null) {
                    statement.setString(5, usuario.getAvisos());
                } else {
                    statement.setNull(5, Types.CHAR);
                }
                if (usuario.getDirftp() != null) {
                    statement.setString(6, usuario.getDirftp());
                } else {
                    statement.setNull(6, Types.CHAR);
                }
                if (usuario.getGrupousu()!= null) {
                    statement.setString(7, usuario.getGrupousu());
                } else {
                    statement.setNull(7, Types.CHAR);
                }
                if (usuario.getCentrohis()!= null) {
                    statement.setString(8, usuario.getCentrohis());
                } else {
                    statement.setNull(8, Types.CHAR);
                }
                if (usuario.getUnidadenf()!= null) {
                    statement.setString(9, usuario.getUnidadenf());
                } else {
                    statement.setNull(9, Types.CHAR);
                }
                if (usuario.getHisingresado()!= null) {
                    statement.setString(10, usuario.getHisingresado());
                } else {
                    statement.setNull(10, Types.CHAR);
                }
                if (usuario.getHisconsultas()!= null) {
                    statement.setString(11, usuario.getHisconsultas());
                } else {
                    statement.setNull(11, Types.CHAR);
                }
                if (usuario.getHislistadeespera()!= null) {
                    statement.setString(12, usuario.getHislistadeespera());
                } else {
                    statement.setNull(12, Types.CHAR);
                }
                if (usuario.getSiglobloque()!= null) {
                    statement.setString(13, usuario.getSiglobloque());
                } else {
                    statement.setNull(13, Types.CHAR);
                }
                if (usuario.getSiglom1()!= null) {
                    statement.setString(14, usuario.getSiglom1());
                } else {
                    statement.setNull(14, Types.CHAR);
                }
                if (usuario.getSiglom2()!= null) {
                    statement.setString(15, usuario.getSiglom2());
                } else {
                    statement.setNull(15, Types.CHAR);
                }
                if (usuario.getSiglom3()!= null) {
                    statement.setString(16, usuario.getSiglom3());
                } else {
                    statement.setNull(16, Types.CHAR);
                }
                if (usuario.getSigloc()!= null) {
                    statement.setString(17, usuario.getSigloc());
                } else {
                    statement.setNull(17, Types.CHAR);
                }
                if (usuario.getCs()!= null) {
                    statement.setString(18, usuario.getCs());
                } else {
                    statement.setNull(18, Types.CHAR);
                }
                if (usuario.getFecha()!= null) {
                    statement.setDate(19, Date.valueOf(usuario.getFecha()));
                } else {
                    statement.setNull(19, Types.DATE);
                }
                if (usuario.getActivo()!= null) {
                    statement.setString(20, usuario.getActivo());
                } else {
                    statement.setNull(20, Types.CHAR);
                }
                if (usuario.getCaducidad()!= null) {
                    statement.setDate(21, Date.valueOf(usuario.getCaducidad()));
                } else {
                    statement.setNull(21, Types.DATE);
                }
                if (usuario.getDondetrabaja()!= null) {
                    statement.setString(22, usuario.getDondetrabaja());
                } else {
                    statement.setNull(22, Types.CHAR);
                }
                if (usuario.getTelefono()!= null) {
                    statement.setString(23, usuario.getTelefono());
                } else {
                    statement.setNull(23, Types.CHAR);
                }
                if (usuario.getGfhgeshis()!= null) {
                    statement.setString(24, usuario.getGfhgeshis());
                } else {
                    statement.setNull(24, Types.CHAR);
                }
                if (usuario.getTur_colectivos()!= null) {
                    statement.setString(25, usuario.getTur_colectivos());
                } else {
                    statement.setNull(25, Types.CHAR);
                }
                if (usuario.getMail()!= null) {
                    statement.setString(26, usuario.getMail());
                } else {
                    statement.setNull(26, Types.CHAR);
                }
                if (usuario.getEmail_oficial()!= null) {
                    statement.setString(27, usuario.getEmail_oficial());
                } else {
                    statement.setNull(27, Types.CHAR);
                }
                if (usuario.getMaes_servhis()!= null) {
                    statement.setString(28, usuario.getMaes_servhis());
                } else {
                    statement.setNull(28, Types.CHAR);
                }
                if (usuario.getHdia_areas()!= null) {
                    statement.setString(29, usuario.getHdia_areas());
                } else {
                    statement.setNull(29, Types.CHAR);
                }
                if (usuario.getBotiquinesfar()!= null) {
                    statement.setString(30, usuario.getBotiquinesfar());
                } else {
                    statement.setNull(30, Types.CHAR);
                }
                if (usuario.getAmbito()!= null) {
                    statement.setString(31, usuario.getAmbito());
                } else {
                    statement.setNull(31, Types.CHAR);
                }
                if (usuario.getAmbitohos()!= null) {
                    statement.setString(32, usuario.getAmbitohos());
                } else {
                    statement.setNull(32, Types.CHAR);
                }
                if (usuario.getCargo()!= null) {
                    statement.setString(33, usuario.getCargo());
                } else {
                    statement.setNull(33, Types.CHAR);
                }
                if (usuario.getDivisioncontratos()!= null) {
                    statement.setString(34, usuario.getDivisioncontratos());
                } else {
                    statement.setNull(34, Types.CHAR);
                }
                if (usuario.getPwddefecto()!= null) {
                    statement.setString(35, usuario.getPwddefecto());
                } else {
                    statement.setNull(35, Types.CHAR);
                }
                if (usuario.getComentario()!= null) {
                    statement.setString(36, usuario.getComentario());
                } else {
                    statement.setNull(36, Types.CHAR);
                }
                if (usuario.getFechacambio()!= null) {
                    statement.setDate(37, Date.valueOf(usuario.getFechacambio()));
                } else {
                    statement.setNull(37, Types.DATE);
                }
                if (usuario.getUsucambio()!= null) {
                    statement.setString(38, usuario.getUsucambio().getDni());
                } else {
                    statement.setNull(38, Types.CHAR);
                }
                if (usuario.getNuevomodificado()!= null) {
                    statement.setString(39, usuario.getNuevomodificado());
                } else {
                    statement.setNull(39, Types.CHAR);
                }
                statement.setString(40, usuario.getDni());
                
                actualizado = statement.execute(sql);
                statement.close();
                actualizado = true;
            } catch (SQLException e) {
                logger.error(sql + Utilidades.getStackTrace(e));
            } catch (Exception e) {
                logger.error(Utilidades.getStackTrace(e));
            }  finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(Utilidades.getStackTrace(e));                    
                }                
            }            
        }       
        
        return actualizado;        
    }    
}