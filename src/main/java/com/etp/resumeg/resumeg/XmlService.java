package com.etp.resumeg.resumeg;

import com.etp.resumeg.resumeg.xmlpojo.Template;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.*;
import java.util.*;

public class XmlService {

    public static final String xmlFilePath = "src/main/resources/xml/xmlfile.xml";

    public static void createXmlTest(HashMap<String, Structure> multipleColumnStructures) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

//        String xmlResult = null;

        try {
            DocumentBuilder docBuilder = null;

            docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Template template = new Template();
//            List<Template.TemplateItem> templateItems = template.getTemplateItem();

            // Create Person root element
//            Element templateRootElement = doc.createElement("Template");
//            doc.appendChild(templateRootElement);

            for (Map.Entry<String, Structure> structureEntry : multipleColumnStructures.entrySet()
            ) {
                Structure structure = structureEntry.getValue();
                System.out.println("Structure text: " + structure.getText() + ", Structure items:" + structure.getItems().size());

                for (MyItem subItem :
                        structure.getItems()) {
                    for (TagContent tagContentItem :
                            TagContent.getTagContentList()) {
                        for (String tagContent :
                                tagContentItem.getContent()) {
                            if (tagContent.toLowerCase().contains(subItem.getText().toLowerCase())) {
                                System.out.println("structItem(" + subItem.getText() + ").contains: " + tagContentItem.getTagName());
                                System.out.println();

                                // xml yapisi
                                // <Template><TemplateItem><tagName>title</tagName><Font></Font><Rectangle></Rectangle></TemplateItem></Template>
                                // TemplateItem icinde -> Font, Rectangle
                                // Font icinde -> color, fontSize, fontName
                                // Rectangle icinde -> dimensions
                                // Create TemplateItem Element

                                Template.TemplateItem templateItem = new Template.TemplateItem();
                                templateItem.setTagName(tagContentItem.getTagName());
                                templateItem.setTagContent(subItem.getText());

                                Template.TemplateItem.Font font = new Template.TemplateItem.Font();
                                font.setFontColor(PdfFontService.getFontColor(subItem.getTextRenderInfo()));
                                font.setFontName(subItem.getFont().getFontProgram().getFontNames().getFontName());
                                font.setFontSize(subItem.getFontSize());

                                templateItem.setFont(font);

                                Template.TemplateItem.Rectangle rectangle = new Template.TemplateItem.Rectangle();
                                rectangle.setX(subItem.getLL().getX());
                                rectangle.setY(subItem.getLL().getY());

                                templateItem.setRectangle(rectangle);

                                // add templateItem to templateItems array
                                template.getTemplateItem().add(templateItem);


//                                Element templateItemElement = doc.createElement("TemplateItem");
//
//                                Element tagNameElement = doc.createElement("TagName");
//                                tagNameElement.appendChild(doc.createTextNode(tagContentItem.getTagName()));
//
//                                Element tagContentElement = doc.createElement("TagContent");
//                                tagContentElement.appendChild(doc.createTextNode(subItem.getText()));
//
//                                Element fontElement = doc.createElement("Font");
//                                Element fontSizeElement = doc.createElement("FontSize");
//                                fontSizeElement.appendChild(doc.createTextNode(String.valueOf(subItem.getFontSize())));
//                                Element fontNameElement = doc.createElement("FontName");
//                                fontNameElement.appendChild(doc.createTextNode(subItem.getFont().getFontProgram().getFontNames().getFontName()));
//                                // font color
//                                String fontColor = PdfFontService.getFontColor(subItem.getTextRenderInfo());
//                                Element fontColorElement = doc.createElement("FontColor");
//                                fontColorElement.appendChild(doc.createTextNode(fontColor));
//
//                                Element rectangleElement = doc.createElement("Rectangle");
//                                Element xElement = doc.createElement("X");
//                                xElement.appendChild(doc.createTextNode(String.valueOf(subItem.getLL().getX())));
//                                Element yElement = doc.createElement("Y");
//                                yElement.appendChild(doc.createTextNode(String.valueOf(subItem.getLL().getY())));
//
//
//                                // append child elements
//                                rectangleElement.appendChild(xElement);
//                                rectangleElement.appendChild(yElement);
//                                fontElement.appendChild(fontSizeElement);
//                                fontElement.appendChild(fontNameElement);
//                                fontElement.appendChild(fontColorElement);
//                                templateItemElement.appendChild(tagNameElement);
//                                templateItemElement.appendChild(tagContentElement);
//                                templateItemElement.appendChild(fontElement);
//                                templateItemElement.appendChild(rectangleElement);
//                                templateRootElement.appendChild(templateItemElement);

                            }
                        }
                    }
                }
            }

//            template.setTemplateItem(templateItems);
//            Collections.copy(template.getTemplateItem(), templateItems);

            // Transform Document to XML String
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            File xmlDir = new File("src/main/resources/xml");


            if (!xmlDir.exists()) {
                xmlDir.mkdir();
            }

            // element being an instance of one of the classes generated previously
            try {
//                JAXBContext jc = JAXBContext.newInstance("com.etp.resumeg.resumeg.xmlpojo");
                JAXBContext jc = JAXBContext.newInstance("com.etp.resumeg.resumeg.xmlpojo");
                Marshaller m = jc.createMarshaller();
                OutputStream os = new FileOutputStream(xmlFilePath);
                m.marshal(template, os);
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

//            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
//            StringWriter writer = new StringWriter();
//            transformer.transform(domSource, new StreamResult(writer));
//            transformer.transform(domSource, streamResult);

            // Get the String value of final xml document
//            xmlResult = writer.getBuffer().toString();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

//        System.out.println("xmlResult: " + xmlResult);
    }
}
