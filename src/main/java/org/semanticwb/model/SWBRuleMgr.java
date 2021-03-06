/*
 * SemanticWebBuilder es una plataforma para el desarrollo de portales y aplicaciones de integración,
 * colaboración y conocimiento, que gracias al uso de tecnología semántica puede generar contextos de
 * información alrededor de algún tema de interés o bien integrar información y aplicaciones de diferentes
 * fuentes, donde a la información se le asigna un significado, de forma que pueda ser interpretada y
 * procesada por personas y/o sistemas, es una creación original del Fondo de Información y Documentación
 * para la Industria INFOTEC, cuyo registro se encuentra actualmente en trámite.
 *
 * INFOTEC pone a su disposición la herramienta SemanticWebBuilder a través de su licenciamiento abierto al público (‘open source’),
 * en virtud del cual, usted podrá usarlo en las mismas condiciones con que INFOTEC lo ha diseñado y puesto a su disposición;
 * aprender de él; distribuirlo a terceros; acceder a su código fuente y modificarlo, y combinarlo o enlazarlo con otro software,
 * todo ello de conformidad con los términos y condiciones de la LICENCIA ABIERTA AL PÚBLICO que otorga INFOTEC para la utilización
 * del SemanticWebBuilder 4.0.
 *
 * INFOTEC no otorga garantía sobre SemanticWebBuilder, de ninguna especie y naturaleza, ni implícita ni explícita,
 * siendo usted completamente responsable de la utilización que le dé y asumiendo la totalidad de los riesgos que puedan derivar
 * de la misma.
 *
 * Si usted tiene cualquier duda o comentario sobre SemanticWebBuilder, INFOTEC pone a su disposición la siguiente
 * dirección electrónica:
 *  http://www.semanticwebbuilder.org.mx
 */
package org.semanticwb.model;

import java.util.*;

import org.semanticwb.Logger;
import org.semanticwb.SWBPlatform;
import org.semanticwb.SWBUtils;
import org.semanticwb.platform.SemanticObject;
import org.semanticwb.platform.SemanticProperty;
import org.w3c.dom.*;


// TODO: Auto-generated Javadoc
/** Objeto: Manejador de las Reglas en memoria.
 *
 * Object: Manager of the rules in memory.
 *
 * @author Javier Solis Gonzalez
 * @version 1.1
 */
public class SWBRuleMgr
{
    
    /** The log. */
    private static Logger log = SWBUtils.getLogger(SWBRuleMgr.class);

    /** The Constant TAG_INT_RULE. */
    public static final String TAG_INT_RULE ="SWBRule";
    
    /** The Constant TAG_INT_ROLE. */
    public static final String TAG_INT_ROLE ="SWBRole";
    
    /** The Constant TAG_INT_USERGROUP. */
    public static final String TAG_INT_USERGROUP ="SWBUserGroup";
    
    /** The Constant TAG_INT_ISREGISTERED. */
    public static final String TAG_INT_ISREGISTERED ="SWBIsRegistered";
    
    /** The Constant TAG_INT_ISSIGNED. */
    public static final String TAG_INT_ISSIGNED ="SWBIsSigned";
    
    /** The Constant TAG_INT_DEVICE. */
    public static final String TAG_INT_DEVICE ="SWBDevice";

    /** The Constant TAG_SESSION_ATT. */
    public static final String TAG_INT_USERIP="SWBUserIP";
    
    /** The Constant TAG_REQUEST_PARAM. */
    public static final String TAG_REQUEST_PARAM="SWBReqParam";
    
    /** The Constant TAG_SESSION_ATT. */
    public static final String TAG_SESSION_ATT="SWBSessAtt";

    /** The Constant TAG_SESSION_ATT. */
    public static final String TAG_WEBPAGEVISITED_ATT="SWBWebPageVisited";

    /** The Constant TAG_WebPageHistory_ATT. */
    public static final String TAG_WEBPAGEHISTORY_ATT="SWBWebPageHistory";
    
    /** The doms. */
    private HashMap<String,Document> doms;
    
//    //eval Inner Rules in occurrences
//    private HashMap occDoms;
//    private HashMap occUpds;

    /**
 * Creates new DBUser.
 */
    public SWBRuleMgr()
    {
        log.event("SWBRuleMgr Initialized...");
        doms = new HashMap();
        
////        //eval Inner Rules in occurrences
//        occDoms=new HashMap();
//        occUpds=new HashMap();
    }

    
    /**
     * Inits the.
     */
    public void init()
    {
//        Iterator<Rule> it = Rule.ClassMgr.listRules();
//        while (it.hasNext())
//        {
//            Rule rule = it.next();
//            try
//            {
//                String xml=rule.getXml();
//                if (xml != null)
//                {
//                    Document dom = SWBUtils.XML.xmlToDom(xml);
//                    if (dom != null)
//                    {
//                        doms.put(rule.getURI(), dom);
//                    }
//                }
//            } catch (Exception e)
//            {
//                log.error("Rule:"+rule.getURI(), e);
//            }
//        }
    }
 

    /**
	 * Eval.
	 * 
	 * @param user the user
	 * @param rule_uri the rule_uri
	 * @return true, if successful
	 * @return
	 */
    public boolean eval(User user, String rule_uri)
    {
        boolean ret=false;
        Document rul = doms.get(rule_uri);
        if(rul==null)
        {
            synchronized(this)
            {
                rul = doms.get(rule_uri);
                if(rul==null)
                {
                    SemanticObject obj=SemanticObject.createSemanticObject(rule_uri);
                    if(obj!=null)
                    {
                        reloadRule((Rule)obj.createGenericInstance());
                        rul = doms.get(rule_uri);
                    }
                    
                }

            }
            
        }
        
        if (user != null && rul != null)
        {
            Node node = rul.getChildNodes().item(0);
            if (node != null && node.getNodeName().equals("rule"))
            {
                ret=and(node, user);
            }
        }

        return ret;
    }

    /**
     * And.
     * 
     * @param node the node
     * @param user the user
     * @return true, if successful
     * @return
     */
    public boolean and(Node node, User user)
    {
        boolean ret=true;
        //System.out.println("and:"+node.getNodeName());
        NodeList nl = node.getChildNodes();
        for (int x = 0; x < nl.getLength(); x++)
        {
            if(nl.item(x)!=null)
            {
                if ("and".equals(nl.item(x).getNodeName()))
                {
                    ret = and(nl.item(x), user);
                }
                else if ("or".equals(nl.item(x).getNodeName()))
                {
                    ret = or(nl.item(x), user);
                }else
                {
                    ret = exp(nl.item(x), user);
                }
                if (!ret) 
                {
                    ret=false;
                    break;
                }
            }
        }

        return ret;
    }

    /**
     * Or.
     * 
     * @param node the node
     * @param user the user
     * @return true, if successful
     * @return
     */
    public boolean or(Node node, User user)
    {
        boolean ret=false;

        NodeList nl = node.getChildNodes();
        for (int x = 0; x < nl.getLength(); x++)
        {
            if(nl.item(x)!=null)
            {
                if ("and".equals(nl.item(x).getNodeName()))
                {
                    ret = and(nl.item(x), user);
                }
                else if ("or".equals(nl.item(x).getNodeName()))
                {
                    ret = or(nl.item(x), user);
                }else
                {
                    ret = exp(nl.item(x), user);
                }
                if (ret)
                {
                    ret=true;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * Exp.
     * 
     * @param node the node
     * @param user the user
     * @return true, if successful
     * @return
     */
    public boolean exp(Node node, User user)
    {
        boolean ret = false;

        try
        {
            Node aux = node.getChildNodes().item(0);
            if (aux == null)
            {
                return false;
            }
            String name = node.getNodeName();
            String cond = "=";
            Node att = node.getAttributes().getNamedItem("cond");
            if (att != null)
            {
                cond = att.getNodeValue();
            }
            String value = aux.getNodeValue();
            
            //validacion de Reglas
            if(name.equals(TAG_INT_RULE))
            {
                if(cond.equals("="))
                {
                    return eval(user, value);
                }else 
                {
                    return !eval(user, value);
                }
            }else if(name.equals(TAG_INT_ROLE)) //validacion de roles
            {
                Role role=(Role)SWBPlatform.getSemanticMgr().getOntology().getGenericObject(value);
                if(cond.equals("="))
                {
                    return user.hasRole(role);
                }else 
                {
                    return !user.hasRole(role);
                }
            }else if(name.equals(TAG_INT_USERGROUP)) //validacion de roles
            {
                UserGroup group=(UserGroup)SWBPlatform.getSemanticMgr().getOntology().getGenericObject(value);
                if(cond.equals("="))
                {
                    return group.hasUser(user);
                }else
                {
                    return !group.hasUser(user);
                }
            }else if(name.equals(TAG_INT_ISREGISTERED)) //validacion de roles
            {
                if(cond.equals("="))
                {
                    return user.isRegistered()==Boolean.parseBoolean(value);
                }else
                {
                    return user.isRegistered()!=Boolean.parseBoolean(value);
                }
            }else if(name.equals(TAG_INT_ISSIGNED)) //validacion de roles
            {
                if(cond.equals("="))
                {
                    return user.isSigned()==Boolean.parseBoolean(value);
                }else
                {
                    return user.isSigned()!=Boolean.parseBoolean(value);
                }
            }else if(name.equals(TAG_INT_DEVICE)) //validacion de roles
            {
                Device dev=(Device)SWBPlatform.getSemanticMgr().getOntology().getGenericObject(value);
                if(cond.equals("="))
                {
                    return user.hasDevice(dev);
                }else
                {
                    return !user.hasDevice(dev);
                }
            }else if(name.equals(TAG_INT_USERIP)) //validacion de roles
            {
                if(user!=null && user.getIp()!=null)
                {
                    if(cond.equals("="))
                    {
                        return user.getIp().startsWith(value);
                    }else
                    {
                        return !user.getIp().startsWith(value);
                    }
                }else
                {
                    //Revisar y eliminar estas lineas si no se cumple la condicion anterior
                    log.warn("SWBRuleMgr.exp:"+user+" -> "+SWBUtils.XML.domToXml(node.getOwnerDocument()));
                    return false;
                }
            }else if(name.equals(TAG_WEBPAGEHISTORY_ATT)) //validacion de roles
            {
                if(cond.equals("="))
                {
                    return user.getHistory().contains(value);
                }else if(cond.equals("!="))
                {
                    return !user.getHistory().contains(value);
                }else if(cond.startsWith("-"))
                {
                    try
                    {
                        int p=Integer.parseInt(cond)*-1;
                        if(user.getHistory().size()>p)
                        {
                            return user.getHistory().get(p).equals(value);
                        }
                    }catch(Exception e){log.error(e);}
                    return false;
                }
            }else if(name.equals(TAG_WEBPAGEVISITED_ATT)) //validacion de roles
            {
                if(cond.equals("="))
                {
                    return user.getVisited().contains(value);
                }else
                {
                    return !user.getVisited().contains(value);
                }
            }else //se busca en el xml del usuario
            {
                SemanticProperty prop=user.getSemanticObject().getSemanticClass().getProperty(name);
                if(prop!=null && prop.isDataTypeProperty())
                {
                    String usrval = user.getSemanticObject().getProperty(prop);

                    if(usrval==null && value==null)
                    {
                        ret=true;
                    }else if(usrval==null)
                    {
                        ret=false;
                    }else if (cond.equals("="))
                    {
                        if (usrval.equals(value))
                        {
                            ret = true;
                        }
                    } else if (cond.equals("!="))
                    {
                        if (!usrval.equals(value))
                        {
                            ret = true;
                        }
                    } else if (cond.equals(">"))
                    {
                        try
                        {
                            float i = Float.parseFloat(usrval);
                            float j = Float.parseFloat(value);
                            if (i > j)
                            {
                                ret = true;
                            }
                        } catch (NumberFormatException e)
                        {
                            if (usrval.compareTo(value) > 0)
                            {
                                ret = true;
                            }
                        }
                    } else if (cond.equals("<"))
                    {
                        try
                        {
                            float i = Float.parseFloat(usrval);
                            float j = Float.parseFloat(value);
                            if (i < j)
                            {
                                ret = true;
                            }
                        } catch (NumberFormatException e)
                        {
                            if (usrval.compareTo(value) < 0)
                            {
                                ret = true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            log.error("SWBRuleMgr.exp:"+user+" -> "+SWBUtils.XML.domToXml(node.getOwnerDocument()),e);
        }

        return ret;
    }

    /**
     * Destroy.
     */
    public void destroy()
    {
        log.event("SWBRuleMgr Destroyed...");
    }

    /**
     * Reload rule.
     * 
     * @param rule the rule
     */
    public void reloadRule(Rule rule)
    {
        try
        {
            String xml=rule.getXml();
            if (xml != null)
            {
                Document dom = SWBUtils.XML.xmlToDom(xml);
                if (dom != null)
                {
                    doms.put(rule.getURI(), dom);
                }
            }
        } catch (Exception e)
        {
            log.error("Rule:"+rule.getURI(), e);
        }
    }
}
