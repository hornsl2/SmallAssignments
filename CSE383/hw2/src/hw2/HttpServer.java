//package hornsl2;
/* NAME: Sam Horn
 * Date: 9/12/2017
 * Class: CSE383
 * DESCRIPTION: Lab 2:HTTP Server that accepts GET and POST Requests
 *  */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class HttpServer {

	static int port;
	ServerSocket sSock;
	Socket socket;
	InputStream input;
	OutputStream output;
	String uniqueID;
	ArrayList<String> values;
	String name;

	private static Logger LOGGER = Logger.getLogger("info");
	FileHandler fh = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// use first arg for port #
		try {
			port = Integer.parseInt(args[0]);
			HttpServer http = new HttpServer();
			http.Main();

		} catch (Exception err) {
			System.out.print("ERR- cannot create object");
			LOGGER.log(Level.SEVERE, "Unable to create server object, shutting down", err);
		}

	}

	public HttpServer() throws IOException {
		fh = new FileHandler("server.log");
		LOGGER.addHandler(fh);
		LOGGER.setUseParentHandlers(false);
		SimpleFormatter formatter = new SimpleFormatter();
		fh.setFormatter(formatter);
	}

	public void Main() {
		// init
		try {
			sSock = new ServerSocket(port);
		} catch (IOException err) {
			System.err.println("Port in use");
			LOGGER.log(Level.SEVERE, "Port in use, shutting down", err);
			System.exit(-1);
		}

		while (true) {
			try {
				LOGGER.info("Connecting");
				socket = sSock.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();
				socket.setSoTimeout(15000);
				handleRequest();
				socket.close();
			} catch (IOException err) {
				LOGGER.log(Level.SEVERE, "Constructor Error", err);
			}
		}
	}

	public void handleRequest() {
		values = new ArrayList<String>();
		LOGGER.info("Start Client Communication");
		try {
			char byteIn;
			int count = 0;
			int[] lastVals = new int[2]; // used for the last two bytes read in
			String currentString = "";
			do {
				do {
					byteIn = (char) input.read();
					lastVals[0] = lastVals[1];
					lastVals[1] = byteIn;

					// append every input byte to the string to eventually be
					// added to an arrayList
					currentString += byteIn;

					// check for \n\r to to stop reading
					
					}while(lastVals[0] != 13 && lastVals[1] != 10);
				
				values.add(currentString);
				count++;
			} while (values.get(count).getBytes()[0] != 13 && values.get(count).getBytes()[1] != 10);// check
																										// last
																										// read
																										// bytes
																										// are
																										// indeed
																										// a
																										// stop
																										// char

			// check if the first field is a POST
			if (values.get(0).substring(0, values.get(0).indexOf(" ")).equalsIgnoreCase("POST")) {
				LOGGER.info("Returning POST Request");
				output.write("POST\n\r".getBytes());

				// check if first value is GET
			} else if (values.get(0).substring(0, values.get(0).indexOf(" ")).equalsIgnoreCase("GET")) {} else {
				LOGGER.info("ERROR: 500, unable to detect GET/POST , n/r/ , or stream stopped reading");
				output.write("ERROR: 500\n\r".getBytes());
			}

		} catch (Exception err) {
			LOGGER.log(Level.SEVERE, "error handeling client", err);
			
		}
	}

	public void getResponse() {

		try {
			LOGGER.info("Returning GET REQUEST");
			name = "Sam Horn\n\r";
			uniqueID = "Hornsl2\n\r";

			int toReturn1 = values.get(0).indexOf("GET") + 3;
			int toReturn2 = values.get(0).indexOf("/HTTP/1.1");
			String path = "**PATH- " + values.get(0).substring(toReturn1, toReturn2) + "\n\r";

			output.write(name.getBytes());
			output.write(uniqueID.getBytes());
			output.write(path.getBytes());
			for (int i = 1; i < values.size(); i++) {
				output.write(values.get(i).getBytes());
			}
			LOGGER.info("Returning GET REQUEST completed");
		} catch (Exception err) {
			LOGGER.log(Level.SEVERE, "error returning GET info", err);
		}
	}
}
