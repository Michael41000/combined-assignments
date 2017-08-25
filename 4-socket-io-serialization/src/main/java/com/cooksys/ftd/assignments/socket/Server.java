package com.cooksys.ftd.assignments.socket;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.LocalConfig;
import com.cooksys.ftd.assignments.socket.model.RemoteConfig;
import com.cooksys.ftd.assignments.socket.model.Student;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Server extends Utils {

    /**
     * Reads a {@link Student} object from the given file path
     *
     * @param studentFilePath the file path from which to read the student config file
     * @param jaxb the JAXB context to use during unmarshalling
     * @return a {@link Student} object unmarshalled from the given file path
     */
    public static Student loadStudent(String studentFilePath, JAXBContext jaxb) {
    	Unmarshaller unmarshaller;
		try {
			unmarshaller = jaxb.createUnmarshaller();
			
			Student student = (Student) unmarshaller.unmarshal(new File(studentFilePath));
			
			return student;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
    }

    /**
     * The server should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" property of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.LocalConfig} object to create a server socket that
     * listens for connections on the configured port.
     *
     * Upon receiving a connection, the server should unmarshal a {@link Student} object from a file location
     * specified by the config's "studentFilePath" property. It should then re-marshal the object to xml over the
     * socket's output stream, sending the object to the client.
     *
     * Following this transaction, the server may shut down or listen for more connections.
     */
    public static void main(String[] args) {
    	JAXBContext context = Utils.createJAXBContext();
    	
    	Config config = Utils.loadConfig("config/config.xml", context);
        
        LocalConfig localConfig = config.getLocal();
        RemoteConfig remoteConfig = config.getRemote();
        Student student = loadStudent(config.getStudentFilePath(), context);
        
        System.out.println("Local Port: " + localConfig.getPort());
        System.out.println("Remote Port: " + remoteConfig.getPort());
        System.out.println("Remote Host: " + remoteConfig.getHost());
        System.out.println("Student First Name: " + student.getFirstName());
        System.out.println("Student Last Name: " + student.getLastName());
        System.out.println("Student Favorite IDE: " + student.getFavoriteIDE());
        System.out.println("Student Favorite Language: " + student.getFavoriteLanguage());
        System.out.println("Student Favorite Paradigm: " + student.getFavoriteParadigm());


    }
}
