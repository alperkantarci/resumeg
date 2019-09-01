
package com.etp.resumeg.resumeg.xmlpojo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TemplateItem" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="TagName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="TagContent" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Font">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="FontSize" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                             &lt;element name="FontName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="FontColor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Rectangle">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="X" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                             &lt;element name="Y" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "templateItem"
})
@XmlRootElement(name = "Template")
public class Template {

    @XmlElement(name = "TemplateItem", required = true)
    protected List<Template.TemplateItem> templateItem;

    /**
     * Gets the value of the templateItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the templateItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplateItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Template.TemplateItem }
     * 
     * 
     */
    public List<Template.TemplateItem> getTemplateItem() {
        if (templateItem == null) {
            templateItem = new ArrayList<Template.TemplateItem>();
        }
        return this.templateItem;
    }

    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="TagName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="TagContent" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Font">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="FontSize" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                   &lt;element name="FontName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="FontColor" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Rectangle">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="X" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                   &lt;element name="Y" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tagName",
        "tagContent",
        "font",
        "rectangle"
    })
    public static class TemplateItem {

        @XmlElement(name = "TagName", required = true)
        protected String tagName;
        @XmlElement(name = "TagContent", required = true)
        protected String tagContent;
        @XmlElement(name = "Font", required = true)
        protected Template.TemplateItem.Font font;
        @XmlElement(name = "Rectangle", required = true)
        protected Template.TemplateItem.Rectangle rectangle;

        /**
         * Gets the value of the tagName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTagName() {
            return tagName;
        }

        /**
         * Sets the value of the tagName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTagName(String value) {
            this.tagName = value;
        }

        /**
         * Gets the value of the tagContent property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTagContent() {
            return tagContent;
        }

        /**
         * Sets the value of the tagContent property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTagContent(String value) {
            this.tagContent = value;
        }

        /**
         * Gets the value of the font property.
         * 
         * @return
         *     possible object is
         *     {@link Template.TemplateItem.Font }
         *     
         */
        public Template.TemplateItem.Font getFont() {
            return font;
        }

        /**
         * Sets the value of the font property.
         * 
         * @param value
         *     allowed object is
         *     {@link Template.TemplateItem.Font }
         *     
         */
        public void setFont(Template.TemplateItem.Font value) {
            this.font = value;
        }

        /**
         * Gets the value of the rectangle property.
         * 
         * @return
         *     possible object is
         *     {@link Template.TemplateItem.Rectangle }
         *     
         */
        public Template.TemplateItem.Rectangle getRectangle() {
            return rectangle;
        }

        /**
         * Sets the value of the rectangle property.
         * 
         * @param value
         *     allowed object is
         *     {@link Template.TemplateItem.Rectangle }
         *     
         */
        public void setRectangle(Template.TemplateItem.Rectangle value) {
            this.rectangle = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="FontSize" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *         &lt;element name="FontName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="FontColor" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "fontSize",
            "fontName",
            "fontColor"
        })
        public static class Font {

            @XmlElement(name = "FontSize")
            protected double fontSize;
            @XmlElement(name = "FontName", required = true)
            protected String fontName;
            @XmlElement(name = "FontColor", required = true)
            protected String fontColor;

            /**
             * Gets the value of the fontSize property.
             * 
             */
            public double getFontSize() {
                return fontSize;
            }

            /**
             * Sets the value of the fontSize property.
             * 
             */
            public void setFontSize(double value) {
                this.fontSize = value;
            }

            /**
             * Gets the value of the fontName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFontName() {
                return fontName;
            }

            /**
             * Sets the value of the fontName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFontName(String value) {
                this.fontName = value;
            }

            /**
             * Gets the value of the fontColor property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFontColor() {
                return fontColor;
            }

            /**
             * Sets the value of the fontColor property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFontColor(String value) {
                this.fontColor = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="X" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *         &lt;element name="Y" type="{http://www.w3.org/2001/XMLSchema}double"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "x",
            "y"
        })
        public static class Rectangle {

            @XmlElement(name = "X")
            protected double x;
            @XmlElement(name = "Y")
            protected double y;

            /**
             * Gets the value of the x property.
             * 
             */
            public double getX() {
                return x;
            }

            /**
             * Sets the value of the x property.
             * 
             */
            public void setX(double value) {
                this.x = value;
            }

            /**
             * Gets the value of the y property.
             * 
             */
            public double getY() {
                return y;
            }

            /**
             * Sets the value of the y property.
             * 
             */
            public void setY(double value) {
                this.y = value;
            }

        }

    }

}
