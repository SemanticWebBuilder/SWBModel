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
 *  http://www.semanticwebbuilder.org
 */
package org.semanticwb.repository.base;

// TODO: Auto-generated Javadoc
/**
 * The Interface ClassDefinitionBase.
 */
public interface ClassDefinitionBase extends org.semanticwb.model.GenericObject
{
    
    /** The Constant jcr_primaryItemName. */
    public static final org.semanticwb.platform.SemanticProperty jcr_primaryItemName=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/1.0#primaryItemName");
    
    /** The Constant jcr_orderableChildNodes. */
    public static final org.semanticwb.platform.SemanticProperty jcr_orderableChildNodes=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/1.0#orderableChildNodes");
    
    /** The Constant mix_mixin. */
    public static final org.semanticwb.platform.SemanticProperty mix_mixin=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/mix/1.0#mixin");
    
    /** The Constant nt_ChildNodeDefinition. */
    public static final org.semanticwb.platform.SemanticClass nt_ChildNodeDefinition=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.jcp.org/jcr/nt/1.0#childNodeDefinition");
    
    /** The Constant jcr_childNodeDefinition. */
    public static final org.semanticwb.platform.SemanticProperty jcr_childNodeDefinition=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/1.0#childNodeDefinition");
    
    /** The Constant nt_PropertyDefinition. */
    public static final org.semanticwb.platform.SemanticClass nt_PropertyDefinition=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.jcp.org/jcr/nt/1.0#propertyDefinition");
    
    /** The Constant jcr_propertyDefinition. */
    public static final org.semanticwb.platform.SemanticProperty jcr_propertyDefinition=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/1.0#propertyDefinition");
    
    /** The Constant jcr_supertypes. */
    public static final org.semanticwb.platform.SemanticProperty jcr_supertypes=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticProperty("http://www.jcp.org/jcr/1.0#supertypes");
    
    /** The Constant swbrep_ClassDefinition. */
    public static final org.semanticwb.platform.SemanticClass swbrep_ClassDefinition=org.semanticwb.SWBPlatform.getSemanticMgr().getVocabulary().getSemanticClass("http://www.semanticwebbuilder.org/swb4/repository#ClassDefinition");

    /**
     * Checks if is orderable child nodes.
     * 
     * @return true, if is orderable child nodes
     */
    public boolean isOrderableChildNodes();

    /**
     * Sets the orderable child nodes.
     * 
     * @param value the new orderable child nodes
     */
    public void setOrderableChildNodes(boolean value);
}