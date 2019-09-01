package com.etp.resumeg.resumeg;

import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class XmlService {

    public static final String xmlFilePath = "xml/xmlfile.xml";

    public static void createXmlTest(HashMap<String, Structure> multipleColumnStructures) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

//        String xmlResult = null;

        try {
            DocumentBuilder docBuilder = null;

            docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            // Create Person root element
            Element templateRootElement = doc.createElement("Template");
            doc.appendChild(templateRootElement);

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
                            if(tagContent.toLowerCase().contains(subItem.getText().toLowerCase())) {
                                System.out.println("structItem(" + subItem.getText() + ").contains: " + tagContentItem.getTagName());
                                System.out.println();

                                // Bu kisimda xml olustur
                                // xml yapisi
                                // <Template><TemplateItem><tagName>title</tagName><Font></Font><Rectangle></Rectangle></TemplateItem></Template>
                                // TemplateItem icinde -> Font, Rectangle
                                // Font icinde -> color, fontSize, fontName
                                // Rectangle icinde -> dimensions
                                // Create TemplateItem Element
                                Element templateItemElement = doc.createElement("TemplateItem");

                                Element tagNameElement = doc.createElement("TagName");
                                tagNameElement.appendChild(doc.createTextNode(tagContentItem.getTagName()));

                                Element tagContentElement = doc.createElement("TagContent");
                                tagContentElement.appendChild(doc.createTextNode(subItem.getText()));

                                Element fontElement = doc.createElement("Font");
                                Element fontSizeElement = doc.createElement("FontSize");
                                fontSizeElement.appendChild(doc.createTextNode(String.valueOf(subItem.getFontSize())));
                                Element fontNameElement = doc.createElement("FontName");
                                fontNameElement.appendChild(doc.createTextNode(subItem.getFont().getFontProgram().getFontNames().getFontName()));
                                // font color
                                String fontColor = PdfFontService.getFontColor(subItem.getTextRenderInfo());
                                Element fontColorElement = doc.createElement("FontColor");
                                fontColorElement.appendChild(doc.createTextNode(fontColor));

                                Element rectangleElement = doc.createElement("Rectangle");
                                Element xElement = doc.createElement("X");
                                xElement.appendChild(doc.createTextNode(String.valueOf(subItem.getLL().getX())));
                                Element yElement = doc.createElement("Y");
                                yElement.appendChild(doc.createTextNode(String.valueOf(subItem.getLL().getY())));


                                // append child elements
                                rectangleElement.appendChild(xElement);
                                rectangleElement.appendChild(yElement);
                                fontElement.appendChild(fontSizeElement);
                                fontElement.appendChild(fontNameElement);
                                fontElement.appendChild(fontColorElement);
                                templateItemElement.appendChild(tagNameElement);
                                templateItemElement.appendChild(tagContentElement);
                                templateItemElement.appendChild(fontElement);
                                templateItemElement.appendChild(rectangleElement);
                                templateRootElement.appendChild(templateItemElement);
                            }
                        }
                    }
                }
            }

            // Transform Document to XML String
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            File xmlDir = new File("xml");

            if(!xmlDir.exists()){
                xmlDir.mkdir();
            }

            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
//            StringWriter writer = new StringWriter();
//            transformer.transform(domSource, new StreamResult(writer));
            transformer.transform(domSource, streamResult);

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
