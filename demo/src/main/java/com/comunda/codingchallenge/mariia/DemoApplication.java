package com.comunda.codingchallenge.mariia;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

		//SpringApplication.run(DemoApplication.class, args);
		String start = "approveInvoice";
		String stop = "invoiceProcessed";
		//String start = "sequenceFlow_180";
		//String stop = "invoiceNotProcessed";
		ReadModel.readModel(start, stop);
	}





}
