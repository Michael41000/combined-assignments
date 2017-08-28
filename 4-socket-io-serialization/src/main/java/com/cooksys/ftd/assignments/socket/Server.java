package com.cooksys.ftd.assignments.socket;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.LocalConfig;
import com.cooksys.ftd.assignments.socket.model.Student;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Server extends Utils {

    /**
     * Reads a {@link Student} object from the given file path
     *
     * @param studentFilePath the file path from which to read the student config file
     * @param jaxb the JAXB context to use during unmarshalling
     * @return a {@link Student} object unmarshalled from the given file path
     */
    public static Student loadStudent(String studentFilePath, JAXBContext jaxb) throws JAXBException {
    	Unmarshaller unmarshaller;
		unmarshaller = jaxb.createUnmarshaller();
		
		Student student = (Student) unmarshaller.unmarshal(new File(studentFilePath));
		
		return student;
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
    	// Create a jaxb context
    	JAXBContext context = Utils.createJAXBContext();
    	
    	// Load a config object from the <project-root>/config/config.xml path
    	Config config = null;
		try {
			config = Utils.loadConfig("config/config.xml", context);
		} catch (JAXBException e) {
			System.out.println("Config XML file not properly formatted");
			return;
		}
        
        LocalConfig localConfig = config.getLocal();
        
        Socket client = null;
        OutputStreamWriter writer = null;
        // Will keep server open indefinitely for at most an hour
        long stopTime = System.currentTimeMillis() + 1000 * 60 * 60;
        while(System.currentTimeMillis() < stopTime)
        {
	        try (ServerSocket server = new ServerSocket(localConfig.getPort())){
	        	// Wait for client to connect for only 1 minute
	        	server.setSoTimeout(1000 * 60);
	        	
	        	// Connect to the client
	        	System.out.println("Waiting for Client");
	        	client = server.accept();
	        	
	        	Student student = loadStudent(config.getStudentFilePath(), context);
	        	
	        	Marshaller marshaller = context.createMarshaller();
	        	
	        	// Open a stream to send information to the client
	        	writer = new OutputStreamWriter(client.getOutputStream());
	        	
	        	// Send the student to the client
	        	marshaller.marshal(student, writer);
	        	
	        	System.out.println("Successfully wrote to client");
	        	
			} catch (SocketTimeoutException e)
	        {
				System.out.println("Client did not connect in time.");
			} catch (BindException e)
	        {
				System.out.println("Address already in use");
				return;
	        } catch (JAXBException e) {
				System.out.println("Student XML file not properly formatted");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally
	        {
				try {
					client.close();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e)
				{
					// Do nothing
				}
	        }
    	}
        
        System.out.println("Server Stopped");

    }
}
