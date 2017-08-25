package com.cooksys.ftd.assignments.socket.model;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.cooksys.ftd.assignments.socket.Utils;

public class ConfigXMLCreater {
	public static void main(String[] args) throws JAXBException {
		// Create the student
		Student student = new Student();
		student.setFirstName("Michael");
		student.setLastName("Rollberg");
		student.setFavoriteIDE("Eclipse");
		student.setFavoriteLanguage("Java");
		student.setFavoriteParadigm("Paradigm");
		
		// Create the config
		Config config = new Config();
		LocalConfig localConfig = new LocalConfig();
		RemoteConfig remoteConfig = new RemoteConfig();
		
		// Get a random port number
		int port = ThreadLocalRandom.current().nextInt(49152, 65536);
		
		localConfig.setPort(port);
		remoteConfig.setHost("localhost");
		remoteConfig.setPort(port);
		
		config.setLocal(localConfig);
		config.setRemote(remoteConfig);
		config.setStudentFilePath("config/student.xml");
		
		// Create the file references
		File configXML = new File("config/config.xml");
		File studentXML = new File("config/student.xml");
		
		if (configXML.exists())
		{
			configXML.delete();
		}
		if (studentXML.exists())
		{
			studentXML.delete();
		}
		
		// Create a jaxb context
    	JAXBContext context = Utils.createJAXBContext();
    	
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    	
    	// Write the config and student xml files
    	marshaller.marshal(config, configXML);
    	marshaller.marshal(student, studentXML);
		
	}

}
