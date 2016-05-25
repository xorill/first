package puzzle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * 
 * Hálózati interfész szerver módban.
 * <p>
 * A {@link Network} absztrakt osztály kiterjesztése.
 * 
 * @author Laczó Gyula
 * @author Varga Balázs
 * @author Winkelman Viktor
 *
 */
public class NetworkServer extends Network {

	/**
	 * Szerver socket objektum.
	 */
	private ServerSocket serverSocket = null;
	
	/**
	 * Kliens socket objektum.
	 */
	private Socket clientSocket = null;
	
	/**
	 * Kimeneti adatfolyam.
	 */
	private ObjectOutputStream out = null;
	
	/**
	 * Bemeneti adatfolyam.
	 */
	private ObjectInputStream in = null;

	
	
	/**
	 * Konstruktor.
	 * 
	 * @param j JumbleImage objektum, amellyel össze lesz kapcsolva az osztály adott példánya
	 */
	NetworkServer(JumbleImage j) {
		super(j);
	}

	/**
	 * Vételt megvalósító szál, végtelen ciklusban vár a beérkezõ adatokra. 
	 */
	private class ReceiverThread implements Runnable {

		public void run() {
			try {
				System.out.println("Waiting for Client");
				clientSocket = serverSocket.accept();
				System.out.println("Client connected.");
			} catch (IOException e) {
				System.err.println("Accept failed.");
				disconnect();
				return;
			}

			try {
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());
				out.flush();
			} catch (IOException e) {
				System.err.println("Error while getting streams.");
				disconnect();
				return;
			}

			try {
				Thread.sleep(100);
				jumb.sendState();
				jumb.setConnected(true);
				while (true) {
					String[] received = (String[]) in.readObject();
					jumb.receive(received);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Client disconnected!");
				JOptionPane.showMessageDialog(null,"Client disconnected!","Error",JOptionPane.ERROR_MESSAGE);
			} finally {
				disconnect();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void connect(String ip, int port) {
		disconnect();
		try {
			serverSocket = new ServerSocket(port);

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
		} catch (IOException e) {
			System.err.println("Could not listen on port "+port+".");
			JOptionPane.showMessageDialog(null,"Could not listen on port "+port+".","Error",JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void send(String[] s) {
		if (out == null)
			return;
		try {
			out.writeObject(s);
			out.flush();
		} catch (IOException ex) {
			System.err.println("Send error.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	void disconnect() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (clientSocket != null)
				clientSocket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(NetworkServer.class.getName()).log(Level.SEVERE,null,ex);
		}
	}
}
