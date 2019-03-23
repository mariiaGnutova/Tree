package com.comunda.codingchallenge.mariia;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

		String start = "approveInvoice";
		String stop = "invoiceProcessed";
		ReadModel.readModel(start, stop);
	}





}
