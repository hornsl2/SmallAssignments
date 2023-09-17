package hw2;

import java.net.*;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.logging.*;

/*
   Scott Campbell
   CSE383

   Simple Http Server

 * This server will:
 * - take the port on the command line CHECK
 * - Handle GET and POST requests
 * - log messages written for major events to a file
 * - handle incorrect protocols
 * - use java packages
 * 
 * I certify this is my own work.
 */

public class HTTPServer2 {
	private static Logger LOGGER = Logger.getLogger("info");
	FileHandler fh = null;
	Socket client = null;
	InputStream is = null;
	OutputStream os = null;
	Map<String, String> systemProperties;
	Set<String> keys;

	// main entry point
	public static void main(String[] args) {
		int port = -1;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception err) {
			System.err.println("Error - First arg must be port (int)");
			System.exit(-1);
		}
		HTTPServer2 server = new HTTPServer2();
		server.Main(port);

	}

	// Constructor
	public HTTPServer2() {
		try {
			fh = new FileHandler("server.log");
			LOGGER.addHandler(fh);
			LOGGER.setUseParentHandlers(false);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			

		} catch (IOException err) {
			System.err.println("Error - can't open log file");
		}
	}

	// main loop
	public void Main(int port) {
		ServerSocket svr = null;
		LOGGER.info("binding to " + port);
		try {
			svr = new ServerSocket(port);
			// put the catch here for the serversocket so if the port fails the
			// error is caught here
		} catch (IOException err) {
			LOGGER.info("Error binding");
			System.err.println("Error-> can't bind");
			System.exit(-1);
		}

		// main loop
		while (true) {
			try {
				LOGGER.info("Waiting");
				client = svr.accept();
				client.setSoTimeout(2000);
				LOGGER.info("Connection made from " + client.getRemoteSocketAddress());
				is = client.getInputStream();
				os = client.getOutputStream();

				// REQLINE
				String reqLine = readLine();
				LOGGER.info("REQLINE IS: " + reqLine);
				String parts[] = reqLine.split(" ");

				// Headers
				StringBuffer headers = new StringBuffer();
				String oneHeaderLine = readLine();
				while (oneHeaderLine.length() != 0) {
					headers.append(oneHeaderLine + "\r\n");
					oneHeaderLine = readLine();
				}

				LOGGER.info(headers.toString());

				// we got request, now parse it
				try {
					// check if 3rd part looks like http
					if (!parts[2].toLowerCase().startsWith("http"))
						throw new IOException("Invalid Protocol");

					// now see if its GET or POST. Note forceing to lowercase
					// for check
					if (parts[0].toLowerCase().equals("get")) {
						
						if (parts[1].toLowerCase().equals("/info")) {//get and info
							String path= parts[1];
							LOGGER.info("info");
							System.out.print("/path detected");
							os.write(("*Path: "+path).getBytes());
							
							StringBuffer resp = new StringBuffer();
							
								RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
								systemProperties = runtimeBean.getSystemProperties();
								keys = systemProperties.keySet();
								for (String key : keys) {
					            String value = systemProperties.get(key);
					            //System.out.printf("[%s] = %s.\n", key, value);
					            resp.append( "["+key+"]" + " = " + value+"\n\r");
					            
					            sendResponse(200, "OK", resp.toString());
					        }//end if
							
						} else if(parts[1].toLowerCase().equals("/status"))//get and status
						{
							LOGGER.info("Status");
							StringBuffer resp = new StringBuffer();
							Date date= new Date();
							resp.append(date.toString()+"\n\r");
							sendResponse(200, "OK", resp.toString());
						}
						else//only get
								doGet(parts, headers.toString());
					} else if (parts[0].toLowerCase().equals("post")) {
						LOGGER.info("POST");
						sendResponse(200, "OK", "post");
					} else { // neither get or post so its invalid
						LOGGER.info("Invalid method");
						throw new IOException("Invalid Protocol");
					}
				} catch (Exception err) { // this is kinda cheating but any
											// problem decoding will cause an
											// error (eg split did not produce 3
											// parts)
					throw new IOException("Invalid Protocol");
				}

				// Close Connection
				System.out.println("Closing connection.");
				LOGGER.info("Closing");
				os.flush();
				client.close();

			} catch (IOException e) {
				// ok, we have an error, try and send back message
				LOGGER.log(Level.SEVERE, "Error", e);
				try {
					sendResponse(500, "Server Error", "Error processing request ");
				} catch (IOException err2) {
				}
			} finally { // and try to close
				try {
					client.close();
				} catch (IOException err1) {
				}
			} // end while true
		}
	}

	// process our get request
	public void doGet(String parts[], String headers) throws IOException {
		LOGGER.info("GET");
		// create response body
		StringBuffer resp = new StringBuffer();
		resp.append("Sam Horn\r\n");
		resp.append("hornsl2\r\n");
		resp.append(parts[1] + "\r\n");
		resp.append(headers);

		// send back response
		sendResponse(200, "OK", resp.toString());
	}

	// read in byte at a time until we get a full line and return line
	public String readLine() throws IOException {

		StringBuffer line = new StringBuffer();
		int x = is.read();
		while (x != -1 && x != '\n') {
			if (x != '\r')
				line.append((char) x);
			x = is.read();
		}
		// return line;
		return line.toString();
	}

	// method to create valid http response
	// note each header and the reqline are terminated with crlf
	// note blank crlf to indicate end of headers

	public void sendResponse(int code, String status, String body) throws IOException {
		StringBuffer resp = new StringBuffer();
		resp.append("Http/1.1 " + code + " " + status + "\r\n");
		resp.append("x-header: campbest server\r\n");
		if (body.length() != 0) {
			resp.append("Content-Type: text/text\r\n");
			resp.append("Content-Length: " + body.length() + "\r\n");
		}
		resp.append("\r\n"); // close header
		resp.append(body); // if it is zero length then nothing will be appended
		os.write(resp.toString().getBytes());
		os.flush();
	}

}
