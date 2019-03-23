package com.comunda.codingchallenge.mariia;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		if(args.length == 2){
			String start = args[0];
			String stop = args[1];
		ReadModel.readModel(start, stop);
		}
		else{
			System.out.println("Wrong arguments");
		}
	}





}
