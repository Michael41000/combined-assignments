package com.cooksys.ftd.assignments.socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import com.cooksys.ftd.assignments.socket.model.Config;
import com.cooksys.ftd.assignments.socket.model.LocalConfig;
import com.cooksys.ftd.assignments.socket.model.RemoteConfig;
import com.cooksys.ftd.assignments.socket.model.Student;

public class Client {

    /**
     * The client should load a {@link com.cooksys.ftd.assignments.socket.model.Config} object from the
     * <project-root>/config/config.xml path, using the "port" and "host" properties of the embedded
     * {@link com.cooksys.ftd.assignments.socket.model.RemoteConfig} object to create a socket that connects to
     * a {@link Server} listening on the given host and port.
     *
     * The client should expect the server to send a {@link com.cooksys.ftd.assignments.socket.model.Student} object
     * over the socket as xml, and should unmarshal that object before printing its details to the console.
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
    	
    	RemoteConfig remoteConfig = config.getRemote();
    	
    	Socket socket = null;
    	try {
    		// Open a socket to talk to the server
			socket = new Socket(remoteConfig.getHost(), remoteConfig.getPort());
			
			// Open a stream to receive information from the server
			InputStreamReader reader = new InputStreamReader(socket.getInputStream());
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			 
			// Get the student from the server
			Student student = (Student) unmarshaller.unmarshal(reader);
			
			
			System.out.println("Student name: " + student.getFirstName() + " " + student.getLastName());
			System.out.println("Student Favorite IDE: " + student.getFavoriteIDE());
			System.out.println("Student Favorite Language: " + student.getFavoriteLanguage());
			System.out.println("Student Favorite Paradigm: " + student.getFavoriteParadigm());
			
			socket.close();
			 
			 
			 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnmarshalException e)
    	{
			System.out.println("Server sent bad or no data");
			return;
    	} catch (SocketException e)
    	{
			System.out.println("No connection to server");
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
        {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e)
			{
				// Do nothing
			}
        }
    }
}
