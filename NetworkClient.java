package puzzle;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 * 
 * H�l�zati interf�sz kliens m�dban.
 * <p>
 * A {@link Network} absztrakt oszt�ly kiterjeszt�se.
 * 
 * @author Lacz� Gyula
 * @author Varga Bal�zs
 * @author Winkelman Viktor
 *
 */
public class NetworkClient extends Network {

	/**
	 * Socket objektum.
	 */
	private Socket socket = null;
	
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
	 * @param j JumbleImage objektum, amellyel �ssze lesz kapcsolva az oszt�ly adott p�ld�nya
	 */
	NetworkClient(JumbleImage j) {
		super(j);
	}

	/**
	 * V�telt megval�s�t� sz�l, v�gtelen ciklusban v�r a be�rkez� adatokra. 
	 */
	private class ReceiverThread implements Runnable {

		public void run() {
			System.out.println("Connected to Server.");
			try {
				while (true) {
					String[] received = (String[]) in.readObject();
					jumb.receive(received);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				System.err.println("Server disconnected!");
				JOptionPane.showMessageDialog(null,"Server disconnected!","Error",JOptionPane.ERROR_MESSAGE);
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
			socket = new Socket(ip,port);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.flush();

			Thread rec = new Thread(new ReceiverThread());
			rec.start();
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection. ");
			JOptionPane.showMessageDialog(null, "Cannot connect to server!","Error",JOptionPane.ERROR_MESSAGE);
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
			if (socket != null)
				socket.close();
		} catch (IOException ex) {
			System.err.println("Error while closing conn.");
		}
	}
}
