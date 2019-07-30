package com.etp.resumeg.resumeg;

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

public class XmlService {

    public static final String xmlFilePath = "xml/xmlfile.xml";


    public static void createXmlTest() {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

        String xmlResult = null;

        try {
            DocumentBuilder docBuilder = null;

            docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            // Create Person root element
            Element personRootElement = doc.createElement("Person");
            doc.appendChild(personRootElement);

            // Create First Name Element
            Element firstNameElement = doc.createElement("FirstName");
            firstNameElement.appendChild(doc.createTextNode("Sergey"));
            personRootElement.appendChild(firstNameElement);

            // Transform Document to XML String
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            if (new File("xml").mkdir()) {
                StreamResult streamResult = new StreamResult(new File(xmlFilePath));
                StringWriter writer = new StringWriter();
                transformer.transform(domSource, new StreamResult(writer));
                transformer.transform(domSource, streamResult);

                // Get the String value of final xml document
                xmlResult = writer.getBuffer().toString();
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        System.out.println(xmlResult);
    }
}
