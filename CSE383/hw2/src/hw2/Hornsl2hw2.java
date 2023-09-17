import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.*;
import java.text.SimpleDateFormat;


/*
NAME :Sam Horn
Date : 9/6/2016
Class:CSE383
FileName: Hornsl2Server.java

Description

Attributions
*/


public class Hornsl2Server {
	ServerSocket welcomeSocket;
	Socket connectionSocket;
	BufferedReader inFromClient;

	DataInputStream dis;
	DataOutputStream dos;
	ArrayList<String> inputs;
	String uniqueID;
	double values;
	boolean flag; 
	
	int port;
	InputStream in;
	OutputStream out;

	private static Logger LOGGER = Logger.getLogger("info");
	FileHandler fh = null;

	public static void main(String argv[]) {
		try {

			new Hornsl2Server();
		} catch (Exception err) {
		} // end catch

	}// end main

	//constructor runs the bulk of the operations similar to the "Main" of the provided client
	public Hornsl2Server() throws IOException {
		fh = new FileHandler("server.log");
		LOGGER.addHandler(fh);
		LOGGER.setUseParentHandlers(false);
		SimpleFormatter formatter = new SimpleFormatter();  
		fh.setFormatter(formatter);  
		
		flag=false;
		
		while (true){// create connection

			
			try {
				
				welcomeSocket = new ServerSocket(6789);
				connectionSocket = welcomeSocket.accept();
				connectionSocket.setSoTimeout(10000); // 10 second timeout
				
				dis = new DataInputStream(connectionSocket.getInputStream());
				dos = new DataOutputStream(connectionSocket.getOutputStream());
				
				if (connectionSocket.isConnected()) {

					System.out.println("\t*Connection Accepted");
					writeProtocol();
					//welcomeSocket= new ServerSocket(6789);
				
					//After the protocol has concluded. reset the any connections to allow for new connections
				welcomeSocket.close();
				connectionSocket.close();
				
				dis=null;
				dos=null;
				flag=false;
				System.out.println("\t*refresh-ready for new connection");
				}
				
		
				
				
			} catch (IOException err) {
				//System.out.println("**constructor error");
				LOGGER.log(Level.SEVERE,"error during connection", err);
				// TODO Auto-generated catch block
				
			} // end catch
		} // end while
			// writeProtocol();
	}// end constructor

	private void sendGreeting() throws IOException {// should send a uniqeID
		// TODO Auto-generated method stub
		// System.out.println("Started sendGreeting");
		
		uniqueID = "hornsl2";
		if(flag==false)
		{
		flag=true;
			dos.writeUTF(uniqueID);
		}
		
	}

	private void readValues() { // should read 4 values
		// TODO Auto-generated method stub

		values = 0;
		try {
			int input = dis.readInt();
			while (input != 0) {

				if (input == 1) {
					int z = dis.readInt();
					values += z;
				} else if (input == 2) {
					double y = dis.readDouble();
					values += y;
				}
				input = dis.readInt();
			}
			dos.writeUTF("ok");
			System.out.print("values--");
			System.out.println(values);

		} // end try
		catch (Exception err) {

			System.out.print("Err- readValues");
			LOGGER.log(Level.SEVERE, "error during communication-reading NUM", err);
		}
	}

	private void sendSum() {//
		// TODO Auto-generated method stub
		try {
			// System.out.println("Started sendSums");
			dos.writeDouble(values);
		} catch (IOException err) {
			// TODO Auto-generated catch block
			System.out.println("Err- SendSum");
			LOGGER.log(Level.SEVERE, "error during protocol-SENDSUM", err);
		}
	}

	public void writeProtocol() {
		try {
			sendGreeting();

		} catch (IOException err) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			LOGGER.log(Level.SEVERE, "error during protocol-greeting", err);
		}
		readValues();
		sendSum();
	

	}// end writeProtocol

}// end class
