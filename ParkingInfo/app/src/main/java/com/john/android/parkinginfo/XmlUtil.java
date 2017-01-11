package com.john.android.parkinginfo;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by jeonghyonkim on 2016. 10. 11..
 */
public class XmlUtil {
    public final static String TAG="Parking";

    //메쏘드 [String 받을시 사용]
    public static Document loadXmlString(String xmlData) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource xmlSrc = new InputSource(new ByteArrayInputStream(xmlData.getBytes("utf-8")));
            Document doc = builder.parse(xmlSrc);
            doc.getDocumentElement().normalize();
            return doc;
        } catch (Exception ex) {
            Log.e(TAG,"loadXmlString str ex=["+ex+"]");
        }
        return null;
    }

    //xml 파일 읽어들어서 램 상주.
    public static Document loadXmlFile(String FileUrl) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(FileUrl));
            return doc;
        } catch (Exception ex) {
            Log.e(TAG,"loadXmlString file ex=["+ex+"]");
        }
        return null;
    }

    /**
     * root Element 아래의 Tag text value 값을 반환한다.
     * @param root root Element object
     * @param tagName tag name
     * @return tag의 text value
     */
    public static String getTagValue(Document root, String tagName ) {
        NodeList nList = root.getElementsByTagName(tagName);
        for (int i = 0; i < nList.getLength(); i++) {
            Node node = nList.item(i);
            if (node != null) {
                Node child = node.getFirstChild();
                if ((child != null) && (child.getNodeValue() != null)) {
                    //Log.d(TAG,"NodeValue:[" + child.getNodeValue() + "]");
                    return child.getNodeValue();
                }
            }
        }
        return "";
    }

    /**
     * root Element 아래의 Tag text value 값을 반환한다.
     * @param root root Element object
     * @param tagName tag name
     * @return tag의 text value
     */
    public static String getTagValue(Document root, String tagName,int ipos ) {
        NodeList nList = root.getElementsByTagName(tagName);
        Node node = nList.item(ipos);
        if (node != null) {
            Node child = node.getFirstChild();
            if ((child != null) && (child.getNodeValue() != null)) {
                //Log.d(TAG,"NodeValue:[" + child.getNodeValue() + "]");
                return child.getNodeValue();
            }
        }
        return "";
    }

    /**
     * Node의 attribute value를 반환한다.
     * @param node Node object
     * @param attrName attribute name
     * @return attribute의 value
     */
    public static String getAttribute(Node node, String attrName) {
        if ((node != null) && (node instanceof Element)) {
            return ((Element) node).getAttribute(attrName);
        }
        return "";
    }

    /**
     * Node의 attribute value를 반환한다.
     * @param node Node object
     * @param attrName attribute name
     * @return attribute의 value
     */
    public static String getAttribute(NodeList nl, String attrName) {
        String sAttribute = "";
        for(int k = 0; k<nl.getLength(); k++){
            Node n = nl.item(k);
            if(n != null){
                sAttribute = getAttribute(n,attrName);
            }
        }
        return sAttribute;
    }
}
