package com.comunda.codingchallenge.mariia;

import org.w3c.dom.*;

public class FindElementById {
    private Element returnElement;

    public Element getReturnElement() {
        return returnElement;
    }

    public void setReturnElement(Element returnElement) {
        this.returnElement = returnElement;
    }

    public void setReturnElementNull() {
        this.returnElement = null;
    }


    public void findElementById(String id, NodeList list){
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;

                if (eElement.getAttribute("id").equals(id)) {
                    setReturnElement(eElement);
                    return;
                }
            }

            if (list.item(i).hasChildNodes()) {
                findElementById(id, list.item(i).getChildNodes());
            }
        }
    }
}
