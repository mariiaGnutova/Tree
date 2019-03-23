package com.comunda.codingchallenge.mariia;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReadModel {
    public static StringBuilder output = new StringBuilder();
    public static String start = new String();
    public static String stop = new String();
    public static NodeList list = new NodeList() {
        @Override
        public Node item(int index) {
            return null;
        }

        @Override
        public int getLength() {
            return 0;
        }
    };

    public static void readModel(String startId, String stopId) throws IOException, ParserConfigurationException, SAXException {
        start = startId;
        stop = stopId;
        CreateTrustManager.trustManager();

// And as before now you can use URL and URLConnection
        URL url = new URL("https://elxkoom6p4.execute-api.eu-central-1.amazonaws.com/prod/engine-rest/" +
                "process-definition/key/invoice/xml");
        InputStream stream = url.openStream();
        System.out.println(stream.toString());
// .. then download the file
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String inputLine;
        String jsonObg = new String();

        while ((inputLine = br.readLine()) != null) {
            JSONObject obj = new JSONObject(inputLine);
            jsonObg = obj.getString("bpmn20Xml");
            System.out.println("json " + jsonObg);
        }
        // Create a DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Create a Document from stream
        ByteArrayInputStream input = new ByteArrayInputStream(jsonObg.getBytes());
        Document doc = builder.parse(input);
        //Extract the root element
        Element root = doc.getDocumentElement();
        doc.getDocumentElement().normalize();


        Node definitions = doc.getElementsByTagName("definitions").item(0);
        list = definitions.getChildNodes();
        findById(list, start);

        try {
            recursion(returnElements.get(0));
        } catch (NullPointerException e) {
            System.out.println("Start Element wasn't found");
        }

        br.close();
    }

    public static ArrayList<String> stepsId = new ArrayList<>();
    public static int copy;

    public static void recursion(Element found) {
        List<String> idTofind = nextStep(found);

        if (!found.getNodeName().equals("sequenceFlow")) {
            stepsId.add(found.getAttribute("id"));
        }

        if (idTofind.size() == 0) {
            //System.out.println("StepID : " + stepsId + "; Copy ID: " + copy);
            //for (int k = stepsId.size() - 1; k > copy; k--){
            //   stepsId.remove(k)  ;
            //}
            //System.out.println("StepID 2: " + stepsId);
        }

        for (int i = 0; i < idTofind.size(); i++) {
            returnElements.clear();
            if (idTofind.get(i).equals(stop)) {
                stepsId.add(idTofind.get(i));
                System.out.println(" steps: " + stepsId);
                return;
            }
            if (stepsId.indexOf(idTofind.get(i)) == -1) {
                findById(list, idTofind.get(i));
                try {
                    recursion(returnElements.get(0));
                } catch (NullPointerException e) {
                    System.out.println("Element " + idTofind.get(i) + "wasn't found");
                }
            } else {
                stepsId.add(idTofind.get(i));
                copy = stepsId.indexOf(idTofind.get(i));
                System.out.println("Copy " + copy);

            }
        }
    }


    public static List<String> nextStep(Element found) {
        List<String> nextStep = new ArrayList<>();
        if (found.hasChildNodes() && !found.getNodeName().equals("sequenceFlow")) {
            NodeList children = found.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("outgoing")) {
                    //                  System.out.println(" Node Name: " + found.getNodeName() + "; id: " + found.getAttribute("id")
                    //                         + "; outgoing: " + children.item(i).getTextContent());
                    nextStep.add(children.item(i).getTextContent());
                }
            }
        } else if (found.getNodeName().equals("sequenceFlow") && getTargetRef(found).length() > 0) {
            nextStep.add(getTargetRef(found));
            //     System.out.println(" Node Name: " + found.getNodeName() + "; id: " + found.getAttribute("id")
            //              + "; TargetRef: " + getTargetRef(found));
        }
        return nextStep;
    }


    public static String getTargetRef(Element withTarget) {
        return withTarget.getAttribute("targetRef");
    }

    public static ArrayList<Element> returnElements = new ArrayList<>();

    public static void findById(NodeList list, String id) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                if (eElement.getAttribute("id").equals(id)) {
                    returnElements.add(eElement);
                }
            }
            if (list.item(i).hasChildNodes()) {
                findById(list.item(i).getChildNodes(), id);
            }
        }
    }
}
