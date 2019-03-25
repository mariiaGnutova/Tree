package com.comunda.codingchallenge.mariia;

import org.camunda.bpm.engine.impl.util.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;

import static com.comunda.codingchallenge.mariia.Util.trustManager;

public class DefinitionsXml {
    private BufferedReader bufferedReader;
    private URL url;
    private String urlString;

    public void closeBufferedReader() throws IOException {
        bufferedReader.close();
    }

    public DefinitionsXml(String urlString) {
        this.urlString = urlString;
    }

    public NodeList getDefinitionsXml() throws IOException, ParserConfigurationException, SAXException {
        trustManager();
        url = new URL(this.urlString);
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
