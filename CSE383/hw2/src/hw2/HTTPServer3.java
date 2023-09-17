/* NAME: Chris Succop
 * Date: 9/12
 * Class: CSE 383
 * DESCRIPTION: This is the 2nd lab in which we are creating a HTTP server.
 */
 
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
	InputStream iS;
	OutputStream oS;
	String uniqueID;
	ArrayList<String> arr;
	String name;

	private static Logger LOGGER = Logger.getLogger("info");
	FileHandler fh = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			port = Integer.parseInt(args[0]);
			HttpServer svr = new HttpServer();
			svr.Main();

		} catch (Exception e) {
			System.out.print("ERR- cannot create object");
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
			System.err.println("in use");
			System.exit(-1);
		}

		while (true) {
			try {
				LOGGER.info("Connecting");
				socket = sSock.accept();
				iS = socket.getInputStream();
				oS = socket.getOutputStream();
				socket.setSoTimeout(10000);
				handle();
				socket.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.log(Level.SEVERE, "Constructor Error", e);
			}
		}
	}

	public void handle() {
		arr = new ArrayList<String>();
		LOGGER.info("Start Client handeling");
		try {
			char b;
			int[] check = new int[2];
			int count = 0;
			String s = "";
			do {
				do {

					b = (char) iS.read();
					check[0] = check[1];
					check[1] = b;
					s += b;
				} while (check[0] != 13 && check[1] != 10);
				arr.add(s);
				count++;
			} while (arr.get(count).getBytes()[0] != 13 && arr.get(count).getBytes()[1] != 10);
			if (arr.get(0).substring(0, arr.get(0).indexOf(" ")).equalsIgnoreCase("GET")) {
				retGet();
			} else if (arr.get(0).substring(0, arr.get(0).indexOf(" ")).equalsIgnoreCase("POST")) {
				LOGGER.info("Returning POST Request info");
				oS.write("POST\n\r".getBytes());
			} else {
				LOGGER.info("500 ERROR");
				oS.write("ERROR: 500\n\r".getBytes());
			}

		} catch (Exception err) {
			LOGGER.log(Level.SEVERE, "error handeling client", err);
		}
	}

	public void retGet() {
		try {
			LOGGER.info("Returning Get Request info");
			int spc = arr.get(0).indexOf("GET") + 3;
			int spc2 = arr.get(0).indexOf("/HTTP/1.1");
			String path = "~PATH~ " + arr.get(0).substring(spc, spc2) + "\n\r";

			uniqueID = "succopca\n\r";
			name = "Chris Succop\n\r";
			oS.write(uniqueID.getBytes());
			oS.write(name.getBytes());
			oS.write(path.getBytes());
			for (int i = 1; i < arr.size(); i++) {
				oS.write(arr.get(i).getBytes());
			}
		} catch (Exception err) {
			LOGGER.log(Level.SEVERE, "error returning GET info", err);
		}
	}
}

