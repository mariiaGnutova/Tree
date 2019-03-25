package com.comunda.codingchallenge.mariia;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class ReadModel {

    private String start;
    private String stop;
    private NodeList definitionsXml;


    public ReadModel(String start, String stop) {
        this.start = start;
        this.stop = stop;
    }

    public void printPathToSystemOut() throws IOException, ParserConfigurationException, SAXException {
        DefinitionsXml defineXML = new DefinitionsXml("https://elxkoom6p4.execute-api.eu-central-1.amazonaws." +
                "com/prod/engine-rest/process-definition/key/invoice/xml");
        definitionsXml = defineXML.getDefinitionsXml();

        FindElementById firstElement = new FindElementById();
        firstElement.findElementById(this.start, definitionsXml);

        BuildPathBetweenNodes pathBetweenNodes = new BuildPathBetweenNodes(this.start, this.stop, definitionsXml);

        try {
            pathBetweenNodes.buildPathBetweenNodes(firstElement.getReturnElement());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Path wasn't found");
        }

        defineXML.closeBufferedReader();
    }
}
