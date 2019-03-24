package com.comunda.codingchallenge.mariia;

import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadModel {
    public static void printPathToSystemOut(String startId, String stopId) throws IOException, ParserConfigurationException, SAXException {
        start = startId;
        stop = stopId;

        definitionsXml = getDefinitionsXml();

        findElementById(definitionsXml, start);

        try {
            buildPathBetweenNodes();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Start Element wasn't found");
        }

        bufferedReader.close();
    }

    private static String start = new String();
    private static String stop = new String();
    private static BufferedReader bufferedReader;
    private static ArrayList<String> stepsId = new ArrayList<>();
    private static ArrayList<Element> returnElements = new ArrayList<>();

    private static NodeList definitionsXml = new NodeList() {
        @Override
        public Node item(int index) {
            return null;
        }

        @Override
        public int getLength() {
            return 0;
        }
    };

    private static void buildPathBetweenNodes() {
        Element element = returnElements.get(0);

        List<String> idTofind = nextSteps(element);

        if (!element.getNodeName().equals("sequenceFlow")) {
            stepsId.add(element.getAttribute("id"));
        }

        if (idTofind.size() == 0 && !element.getAttribute("id").equals(stop)) {
            do {
                stepsId.remove(stepsId.size() - 1);
                returnElements.clear();
                findElementById(definitionsXml, stepsId.get(stepsId.size() - 1));
            }
            while (nextSteps(returnElements.get(0)).size() < 2);

        }

        for (int i = idTofind.size() - 1; i >= 0; i--) {
            returnElements.clear();

            if (idTofind.get(i).equals(stop)) {
                stepsId.add(idTofind.get(i));
                System.out.printf("The path from %s to %s is: %s\n", start, stop, stepsId);
                return;
            }

            if (stepsId.indexOf(idTofind.get(i)) == -1) {
                findElementById(definitionsXml, idTofind.get(i));

                try {
                    buildPathBetweenNodes();
                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("Element %s wasn't found\n", idTofind.get(i));
                }
            }
        }
    }

    private static List<String> nextSteps(Element found) {
        List<String> nextStepsList = new ArrayList<>();

        if (found.hasChildNodes() && !found.getNodeName().equals("sequenceFlow")) {
            NodeList children = found.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("outgoing")) {
                    nextStepsList.add(children.item(i).getTextContent());
                }
            }
        } else if (found.getNodeName().equals("sequenceFlow") && getTargetRef(found).length() > 0) {
            nextStepsList.add(getTargetRef(found));
        }

        return nextStepsList;
    }

    private static String getTargetRef(Element withTarget) {
        return withTarget.getAttribute("targetRef");
    }

    private static void findElementById(NodeList list, String id) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;

                if (eElement.getAttribute("id").equals(id)) {
                    returnElements.add(eElement);

                    return;
                }
            }

            if (list.item(i).hasChildNodes()) {
                findElementById(list.item(i).getChildNodes(), id);
            }
        }
    }

    private static NodeList getDefinitionsXml() throws IOException, ParserConfigurationException, SAXException {
        CreateTrustManager.trustManager();

        URL url = new URL("https://elxkoom6p4.execute-api.eu-central-1.amazonaws.com/" +
                "prod/engine-rest/process-definition/key/invoice/xml");
        url.openStream();

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        bufferedReader = new BufferedReader(inputStreamReader);
        String inputLine;
        String jsonObg = new String();

        while ((inputLine = bufferedReader.readLine()) != null) {
            JSONObject obj = new JSONObject(inputLine);
            jsonObg = obj.getString("bpmn20Xml");
        }

        // Create a DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Create a Document from stream
        ByteArrayInputStream input = new ByteArrayInputStream(jsonObg.getBytes());
        Document doc = builder.parse(input);
        doc.getDocumentElement().normalize();

        Node definitions = doc.getElementsByTagName("definitions").item(0);

        return definitions.getChildNodes();
    }
}
