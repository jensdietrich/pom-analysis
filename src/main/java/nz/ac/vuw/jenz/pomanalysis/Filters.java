package nz.ac.vuw.jenz.pomanalysis;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.function.Predicate;

public class Filters {

    static Predicate<File> USES_SHADE_PLUGIN = new FilterXMLDocumentWithXPath("//plugin/artifactId[text() = 'maven-shade-plugin']");
    static Predicate<File> USES_SHADE_PLUGIN_WITH_RELOCATIONS = new FilterXMLDocumentWithXPath("//plugin/artifactId[text() = 'maven-shade-plugin']/parent::node()//relocations");
    static Predicate<File> USES_STAGING_PLUGIN = new FilterXMLDocumentWithXPath("//plugin/artifactId[text() = 'nexus-staging-maven-plugin']");
    static Predicate<File> HAS_MODULES = new FilterXMLDocumentWithXPath("/project/modules");
    static Predicate<File> HAS_PARENT = new FilterXMLDocumentWithXPath("/project/parent");


    // filter based on whether an element described by xpath exists
    static class FilterXMLDocumentWithXPath implements Predicate<File> {
        private XPathExpression xPathExpression = null;

        public FilterXMLDocumentWithXPath(String xpath) {
            try {
                this.xPathExpression = XPathFactory.newInstance().newXPath().compile(xpath);
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean test(File file) {
            try {
                Document dom = parse(file);
                NodeList nodeList = (NodeList) xPathExpression.evaluate(dom, XPathConstants.NODESET);
                return nodeList.getLength()>0;
            } catch (Exception x) {
                System.err.println("\tinvalid pom (parser exception): " + file.toString());
                // x.printStackTrace();
                return false;
            }
        }
    }

    static Document parse(File f) throws Exception {
        FileInputStream fileIS = new FileInputStream(f);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.parse(fileIS);
    }

}
