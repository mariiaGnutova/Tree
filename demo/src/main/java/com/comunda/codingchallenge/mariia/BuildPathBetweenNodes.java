package com.comunda.codingchallenge.mariia;

import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.List;

public class BuildPathBetweenNodes {
    private ArrayList<String> stepsId = new ArrayList<>();
    private NodeList definitionsXml;
    private String start;
    private String stop;

    public BuildPathBetweenNodes(String start, String stop, NodeList definitionsXml) {
        this.definitionsXml = definitionsXml;
        this.start = start;
        this.stop = stop;
    }

    public void buildPathBetweenNodes(Element element) {
        NextSteps listIds = new NextSteps(element);
        listIds.clear();
        listIds.nextSteps();
        List<String> idsToFind = listIds.getNextStepsList();

        if (!element.getNodeName().equals("sequenceFlow")) {
            stepsId.add(element.getAttribute("id"));
        }

        if (idsToFind.size() == 0 && !element.getAttribute("id").equals(this.stop)) {

            NextSteps listIds2 = new NextSteps(element);
            do {
                listIds2.clear();
                FindElementById nextElement = new FindElementById();
                stepsId.remove(stepsId.size() - 1);
                nextElement.setReturnElementNull();
                nextElement.findElementById(stepsId.get(stepsId.size() - 1), this.definitionsXml);
                listIds2.setFound(nextElement.getReturnElement());
                listIds2.nextSteps();
            }
            while (listIds2.getNextStepsList().size() < 2);

        }

       for (int i = idsToFind.size() - 1; i >= 0; i--) {
        if (idsToFind.get(i).equals(this.stop)) {
                stepsId.add(idsToFind.get(i));
                System.out.printf("The path from %s to %s is: %s\n", this.start, this.stop, stepsId);

                return;
            }

            if (stepsId.indexOf(idsToFind.get(i)) == -1) {
                FindElementById lastElement = new FindElementById();
                lastElement.setReturnElementNull();
               lastElement.findElementById(idsToFind.get(i), this.definitionsXml);
                try {
                    buildPathBetweenNodes(lastElement.getReturnElement());

                } catch (IndexOutOfBoundsException e) {
                    System.out.printf("Element %s wasn't found\n", idsToFind.get(i));
                }

            }
       }

    }

}
