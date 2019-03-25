package com.comunda.codingchallenge.mariia;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class NextSteps {
    private List<String> nextStepsList = new ArrayList<>();
    private Element found;

    public List<String> getNextStepsList() {
        return nextStepsList;
    }

    public void clear() {
        nextStepsList.clear();
    }

    public NextSteps(Element found) {
        this.found = found;
    }


    public void setFound(Element found) {
        this.found = found;
    }

    public void nextSteps() {

        if (this.found.hasChildNodes() && !this.found.getNodeName().equals("sequenceFlow")) {
            NodeList children = found.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeName().equals("outgoing")) {
                    nextStepsList.add(children.item(i).getTextContent());
                }
            }
        } else if (this.found.getNodeName().equals("sequenceFlow") && getTargetRef(this.found).length() > 0) {
            nextStepsList.add(getTargetRef(this.found));
        }
    }

    private String getTargetRef(Element withTarget) {
        return withTarget.getAttribute("targetRef");
    }

}
