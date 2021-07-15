package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 *
 * http://artemisa.unicauca.edu.co/~dabravo/jndi_esp_2/index.htm
 *
 * Gestiona las operaciones con datos del Active directory
 *
 *
 */
public class UsuarioDADao {

    private static final Logger LOGGER = LogManager.getLogger(UsuarioDADao.class);
    private static final long serialVersionUID = 1L;
    private DirContext context;
    private String domainName = "grs.root";
    private String serverName = "hnssntdc0002";
    private String domainDC = "dc=grs,dc=root";
    private Hashtable<String, String> envInicial = new Hashtable<>();
    private SearchControls controls;

    public UsuarioDADao() {
        String username = "06551256M";
        String password = "Primavera2121-";

        String fileSeparator = System.getProperty("file.separator");
        String javaHome = System.getProperty("java.home");
        String keystore = null;
        if (javaHome.indexOf("jre") == -1) {
            keystore = javaHome + fileSeparator + "jre" + fileSeparator + "lib" + fileSeparator + "security"
                    + fileSeparator + "ldap";
        } else {
            keystore = javaHome + fileSeparator + "lib" + fileSeparator + "security" + fileSeparator + "ldap";
        }

        System.setProperty("javax.net.ssl.trustStore", keystore);
        System.setProperty("javax.net.ssl.trustStorePassword", "ldap123");
        envInicial.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        envInicial.put(Context.PROVIDER_URL, "ldaps://hnssntdc0002.grs.root");
        envInicial.put(Context.SECURITY_AUTHENTICATION, "simple");
        envInicial.put(Context.SECURITY_PRINCIPAL, username + "@GRS.ROOT");
        envInicial.put(Context.SECURITY_CREDENTIALS, password);
        envInicial.put(Context.REFERRAL, "follow");
        envInicial.put("com.sun.jndi.ldap.read.timeout", "3000");

        controls = new SearchControls();
        controls.setReturningAttributes(new String[]{"sn", "givenName", "uid", "gcos", "mail", "memberOf", "sacylApplication", "telefhoneNumber",
            "sacylPersonFunctionalGpoup", "sacylPersonCPF", "sacylPersonCollegiateNumbre"});
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

    }

    /**
     * Hace una consulta de datos al Active Directory por el dni del
     * usuario<br/>
     * Si no ecuentra usuairo devuelve null.<br/>
     * Recupera los objetos siguientes:<br/>
     * <ul>
     * <li>Nombre</li>
     * <li>Apellidos</li>
     * <li>mail</li>
     * <li>Grupos de los que es miembro</li>
     * <li>Aplicaciones Sacyl asociadas</li>
     * </ul>
     *
     * @param dni
     * @return
     */
    public UsuarioBean getPorDni(String dni) {
        UsuarioBean usuario = null;
        try {
            context = new InitialDirContext(envInicial);
            String filter = "(&(objectClass=user)(samaccountname=" + dni + "))";
            NamingEnumeration values = context.search(domainDC, filter, controls);
            if (values != null) {
                usuario = getDatos(values);
            }
            LOGGER.debug("(&(objectClass=user)(samaccountname=" + dni + "))");
            context.close();
        } catch (AuthenticationException a) {
            LOGGER.error("Authentication failed: " + Utilidades.getStackTrace(a));
        } catch (NamingException e) {
            LOGGER.error("Failed to bind to LDAP / get account information: " + Utilidades.getStackTrace(e));
        }
        return usuario;
    }

    public UsuarioBean getPorMail(String email) {
        UsuarioBean usuario = null;
        try {
            context = new InitialDirContext(envInicial);
            NamingEnumeration<SearchResult> values = context.search(domainDC, "(& (mail=" + email + ")(objectClass=user))", controls);
            if (values != null) {
                usuario = getDatos(values);
            }
        } catch (AuthenticationException a) {
            LOGGER.error("Authentication failed: " + Utilidades.getStackTrace(a));
        } catch (NamingException e) {
            LOGGER.error("Failed to bind to LDAP / get account information: " + Utilidades.getStackTrace(e));
        }
        return usuario;
    }

    public UsuarioBean getDatos(NamingEnumeration<SearchResult> values) throws NamingException {
        UsuarioBean usuario = null;
        ArrayList<String> groups = new ArrayList<String>();
        ArrayList<String> groupsApp = new ArrayList<String>();
        while (values.hasMoreElements()) {
            SearchResult result = (SearchResult) values.next();
            Attributes attribs = result.getAttributes();
            if (null != attribs) {
                usuario = new UsuarioBean();
                for (NamingEnumeration ae = attribs.getAll(); ae.hasMoreElements();) {
                    Attribute atr = (Attribute) ae.next();
                    String attributeID = atr.getID();
                    for (Enumeration vals = atr.getAll();
                            vals.hasMoreElements();) {
                        String valor = vals.nextElement().toString();
                        switch (attributeID) {
                            case "uid":
                                usuario.setDni(valor);
                                break;
                            case "givenName":
                                usuario.setNombre(valor);
                                break;
                            case "sn":
                                String[] apes = valor.split(" ");
                                if (apes.length == 2) {
                                    usuario.setApellido1(apes[0]);
                                    usuario.setApellido2(apes[1]);
                                }
                                usuario.setApellido1(valor);
                                break;
                            case "mail":
                                usuario.setMail(valor);
                                break;
                            case "telefhoneNumber":
                                usuario.setMail(valor);
                                break;
                            case "sacylPersonFunctionalGpoup":
                                usuario.setGfh(new GfhDao().getPorCodigo(valor));
                                break;
                            case "sacylPersonCPF":
                                usuario.setCpf(valor);
                                break;
                            case "sacylPersonCollegiateNumbre":
                                usuario.setColegiadonumero(valor);
                                break;
                            case "memberOf":
                                Attributes atts = context.getAttributes(valor.toString(), new String[]{"CN"});
                                Attribute att = atts.get("CN");
                                groups.add(att.get().toString());
                                break;
                            case "sacylApplication":
                                groupsApp.add(valor);
                                break;
                        }
                        //  System.out.println(attributeID + ": " + valor);
                    };
                }
                usuario.setGruposActiveDirectory(groups);
                usuario.setAppsActiveDirectory(groupsApp);
            }
        }
        return usuario;
    }

    static String toDC(String domainName) {
        StringBuilder buf = new StringBuilder();
        for (String token : domainName.split("\\.")) {
            if (token.length() == 0) {
                continue;   // defensive check
            }
            if (buf.length() > 0) {
                buf.append(",");
            }
            buf.append("DC=").append(token);
        }
        return buf.toString();
    }
}
