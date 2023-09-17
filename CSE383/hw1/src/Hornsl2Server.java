import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;

public class Hornsl2Server {
	ServerSocket welcomeSocket;
	Socket connectionSocket;
	BufferedReader inFromClient;
	//InputStreamReader dis;
	DataInputStream dis;
	DataOutputStream dos;
	ArrayList<String> inputs;
	String uniqueID;
	int values;

	private static Logger LOGGER = Logger.getLogger("info");
	FileHandler fh = null;

	public static void main(String argv[]) {
		try {

			new Hornsl2Server();
		} catch (Exception e) {
		} // end catch

	}// end main

	public Hornsl2Server() throws IOException {
		while (true)// create connection
		{

			try {
				welcomeSocket = new ServerSocket(6789);
				connectionSocket = welcomeSocket.accept();
				inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				dis = new DataInputStream(connectionSocket.getInputStream());
				dos = new DataOutputStream(connectionSocket.getOutputStream());
				connectionSocket.setSoTimeout(10000); // 10 second timeout
				
				if (connectionSocket.isConnected()) {

					System.out.println("Connection Accepted");
					writeProtocol();
					welcomeSocket= new ServerSocket(6789);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} // end catch
		} // end while
			// writeProtocol();
	}// end constructor

	private void sendGreeting() throws IOException {// should send a uniqeID
		// TODO Auto-generated method stub
		System.out.println("Started sendGreeting");
		uniqueID="hornsl2";

		dos.writeUTF(uniqueID);
	}

	private void readValues() { // should read 4 values
		// TODO Auto-generated method stub
		
		try {
			System.out.println("Started readValues");
			for(int i =0; i<4;i++)
			{
				//if(inFromClient.ready())
				System.out.println(dis.readInt());
			}
			//System.out.println();
			//System.out.println(values);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.print("Err- ReadValues");
			//System.out.print("Err- Value is not a string");
		}

	}

	private void sendSum() {// should send a uniqeID
		// TODO Auto-generated method stub
try { 
	System.out.println("Started sendSums");
	dos.writeUTF(String.valueOf(values));
} catch (IOException e) {
	// TODO Auto-generated catch block
	System.out.print("Err- SendSum");
}
	}

	public void writeProtocol() {
		try {
			sendGreeting();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		readValues();
		sendSum();
		// endConnection()?

	}// end writeProtocol

	public void log() {
	}// end method log

}// end class
