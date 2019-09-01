
package com.etp.resumeg.resumeg.xmlpojo;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.etp.resumeg.resumeg.xmlpojo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.etp.resumeg.resumeg.xmlpojo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Template }
     * 
     */
    public Template createTemplate() {
        return new Template();
    }

    /**
     * Create an instance of {@link Template.TemplateItem }
     * 
     */
    public Template.TemplateItem createTemplateTemplateItem() {
        return new Template.TemplateItem();
    }

    /**
     * Create an instance of {@link Template.TemplateItem.Font }
     * 
     */
    public Template.TemplateItem.Font createTemplateTemplateItemFont() {
        return new Template.TemplateItem.Font();
    }

    /**
     * Create an instance of {@link Template.TemplateItem.Rectangle }
     * 
     */
    public Template.TemplateItem.Rectangle createTemplateTemplateItemRectangle() {
        return new Template.TemplateItem.Rectangle();
    }

}
