/*
 * SemanticWebBuilder es una plataforma para el desarrollo de portales y aplicaciones de integración,
 * colaboración y conocimiento, que gracias al uso de tecnología semántica puede generar contextos de
 * información alrededor de algún tema de interés o bien integrar información y aplicaciones de diferentes
 * fuentes, donde a la información se le asigna un significado, de forma que pueda ser interpretada y
 * procesada por personas y/o sistemas, es una creación original del Fondo de Información y Documentación
 * para la Industria INFOTEC, cuyo registro se encuentra actualmente en trámite.
 *
 * INFOTEC pone a su disposición la herramienta SemanticWebBuilder a través de su licenciamiento abierto al público ('open source'),
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

import java.util.Date;
import java.util.StringTokenizer;

import org.semanticwb.Logger;
import org.semanticwb.SWBUtils;
import org.semanticwb.model.base.CalendarBase;
import org.semanticwb.platform.SemanticObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class Calendar.
 */
public class Calendar extends CalendarBase {

	/** The log. */
	private static Logger LOG = SWBUtils.getLogger(Calendar.class);

	/** The m_dom. */
	private Document mDom = null;

	/** The m_node. */
	private NodeList mNode = null;

	/**
	 * Instantiates a new calendar.
	 * 
	 * @param base
	 *            the base
	 */
	public Calendar(SemanticObject base) {
		super(base);
	}

	/**
	 * Gets the dom.
	 * 
	 * @return the dom
	 */
	public Document getDom() {
		return getSemanticObject().getDomProperty(swb_xml);
	}

	/**
	 * Gets the node list.
	 * 
	 * @return the node list
	 */
	private NodeList getNodeList() {
		Document aux = getDom();

		if (aux != mDom) {
			mDom = aux;

			NodeList nl = aux.getElementsByTagName("interval");
			int n = nl.getLength();

			if (n > 0) {
				mNode = nl;
			} else {
				mNode = null;
			}
		}

		return mNode;
	}

	/**
	 * Checks if is on schedule.
	 * 
	 * @return true, if is on schedule
	 */
	public boolean isOnSchedule() {
		boolean ret = false;
		Date today = new Date();
		NodeList nl = getNodeList();

		if (nl == null) {
			ret = true;
		} else {
			for (int x = 0; x < nl.getLength(); x++) {
				Node interval = nl.item(x);
//                                System.out.println("Intervalo:\n"+SWBUtils.XML.nodeToString(interval));
				try {
					ret = eval(today, interval);
					if (ret) {
						ret = true;
						break;
					}
				} catch (Exception e) {
					LOG.error(e);
				}
			}
		}

		return ret;
	}

	/**
	 * Eval.
	 * 
	 * @param today
	 *            the today
	 * @param interval
	 *            the interval
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 */
	private boolean eval(Date today, Node interval) throws Exception {
		boolean ret = true;
		Date inidate = null;
//                System.out.println("eval:\n\n"+SWBUtils.XML.nodeToString(interval)+" \ntoday:"+today.toString());
		if (interval != null) {
			NodeList nl = interval.getChildNodes();

			for (int x = 0; x < nl.getLength(); x++) {
				if (nl.item(x) != null) {
					String name = nl.item(x).getNodeName();

					if (name.equals("inidate")) {
						inidate = new Date(nl.item(x).getFirstChild().getNodeValue());                                              
//                                                System.out.println("IniDate: "+inidate.toString());
						if (inidate.after(today)) {
							ret = false;
							break;
						}
					} else if (name.equals("enddate")) {
						try {
							Date enddate = new Date(nl.item(x).getFirstChild().getNodeValue());

							enddate.setHours(23);
							enddate.setMinutes(59);
							enddate.setSeconds(59);

							if (enddate.before(today)) {
								ret = false;

								break;
							}
						} catch (Exception e) {
							LOG.error("Nodo Mal en Calendar/Eval:" + nl.item(x).getFirstChild().getNodeValue(), e);
						}
					} else if (name.equals("starthour")) {
						String time = nl.item(x).getFirstChild().getNodeValue();
						StringTokenizer st = new StringTokenizer(time, ":");
						int h = 0, m = 0, s = 0;

						try {
							h = Integer.parseInt(st.nextToken());

							if (st.hasMoreTokens()) {
								m = Integer.parseInt(st.nextToken());
							}

							if (st.hasMoreTokens()) {
								s = Integer.parseInt(st.nextToken());
							}
						} catch (Exception noe) {
							// No error
						}

						inidate = new Date(today.getTime());
						inidate.setHours(h);
						inidate.setMinutes(m);
						inidate.setSeconds(s);

						if (inidate.after(today)) {
							ret = false;
							break;
						}
					} else if (name.equals("endhour")) {
						String time = nl.item(x).getFirstChild().getNodeValue();
						StringTokenizer st = new StringTokenizer(time, ":");
						int h = 0, m = 0, s = 60;

						try {
							h = Integer.parseInt(st.nextToken());

							if (st.hasMoreTokens()) {
								m = Integer.parseInt(st.nextToken());
							}

							if (st.hasMoreTokens()) {
								s = Integer.parseInt(st.nextToken());
							}
						} catch (Exception noe) {
							// No error
						}

						Date enddate = new Date(today.getTime());

						enddate.setHours(h);
						enddate.setMinutes(m);
						enddate.setSeconds(s);

						if (enddate.before(today)) {
							ret = false;
							break;
						}
					} else if (name.equals("iterations") && !evalIteration(inidate, today, nl.item(x))) {
						ret = false;
						break;
					}
				}
			}
		}

		return ret;
	}

	/**
	 * Eval iteration.
	 * 
	 * @param inidate
	 *            the inidate
	 * @param today
	 *            the today
	 * @param iteration
	 *            the iteration
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 * @return
	 */
	private boolean evalIteration(Date inidate, Date today, Node iteration) throws Exception {
		boolean ret = true;

//                System.out.println("evalIteration:\n\n"+SWBUtils.XML.nodeToString(iteration));
		try {
			NodeList nl = iteration.getChildNodes();

			for (int x = 0; x < nl.getLength(); x++) {
				String name = nl.item(x).getNodeName();
				boolean eval = evalElement(inidate, today, nl.item(x));
				
				if (!eval && (name.equals("weekly") || name.equals("monthly") || name.equals("yearly"))) {
					ret = false;
					break;
				}

				/*if (name.equals("weekly") && !evalElement(inidate, today, nl.item(x))) {
					ret = false;
					break;
				} else if (name.equals("monthly") && !evalElement(inidate, today, nl.item(x))) {
					ret = false;
					break;
				} else if (name.equals("yearly") && !evalElement(inidate, today, nl.item(x))) {
					ret = false;
					break;
				}*/
			}
		} catch (Exception e) {
			throw new Exception("error Calendar Iteration 1", e);
		}

		return ret;
	}

	/**
	 * Eval element.
	 * 
	 * @param inidate
	 *            the inidate
	 * @param today
	 *            the today
	 * @param element
	 *            the element
	 * @return true, if successful
	 * @throws Exception
	 *             the exception
	 * @return
	 */
	private boolean evalElement(Date inidate, Date today, Node element) throws Exception {
		boolean ret = true;
//                System.out.println("evalElement:\n\n"+SWBUtils.XML.nodeToString(element)+" \ntoday:"+today.toString()+" \niniday:"+inidate.toString());
		try {
			NodeList nl = element.getChildNodes();

			for (int x = 0; x < nl.getLength(); x++) {
				String name = nl.item(x).getNodeName();
                                
				String value = nl.item(x).getFirstChild().getNodeValue();
				int val = Integer.parseInt(value);
//                                System.out.println(name+" ("+val+")");
				if (name.equals("wdays")) {
//                                    System.out.println((1 << today.getDay()));
					if (((1 << today.getDay()) & val) == 0) {
						ret = false;
						break;
					}
				} else if (name.equals("day")) {
//                                    System.out.println("hoy:"+today.getDate());
					if (today.getDate() < val) {
						ret = false;
						break;
					}
				} else if (name.equals("today")) {
//                                    System.out.println("hoy:"+today.getDate());
					if (today.getDate() > val) {
						ret = false;
						break;
					}
				}else if (name.equals("months")) {
					int inim = inidate.getMonth(); // mes inicial
					int actm = today.getMonth(); // mes actual
					int dify = today.getYear() - inidate.getYear(); // diferencia de años
					int difm = actm + dify * 12 - inim; // diferencia de meses realses contando años
					int modm = difm % val; // modulo de los mese con respecto al valor

					if (modm != 0) {
						ret = false;
						break;
					}
				} else if (name.equals("week")) {
                                    
                                        java.util.Calendar cal = java.util.Calendar.getInstance();
                                        cal.setTime(today);
                                        int week = cal.get(java.util.Calendar.DAY_OF_WEEK_IN_MONTH);
                                        
                                         java.util.Calendar last = java.util.Calendar.getInstance();
                                        last.setTime(today);
                                        last.add(java.util.Calendar.DATE, 7);
                                        boolean lastweek = cal.get(java.util.Calendar.MONTH)!=last.get(java.util.Calendar.MONTH);
					//int week = (int) ((today.getDate() + inidate.getDay() - 1) / 7) + 1;
//                                        System.out.println("\n\ntoday.getDate("+today.getDate()+")\ninidate.getDay("+inidate.getDay()+")\nweek:"+week);
					if ((val<5 && val != week) || ((val == 5) && !lastweek)) {
						ret = false;
						break;
					}
				} else if (name.equals("month")) {
					if ((today.getMonth() + 1) != val) {
						ret = false;
						break;
					}
				} else if (name.equals("years")) {
					int dify = today.getYear() - inidate.getYear(); // diferencia de años
					int mody = dify % val; // modulo de los años con respecto al valor

					if (mody != 0) {
						ret = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("Error Calendar Iteration 2", e);
		}

		return ret;
	}
}