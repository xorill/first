package puzzle;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

public class NetworkClient extends Network {

	private Socket socket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	NetworkClient(JumbleImage j) {
		super(j);
	}

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
			} finally {
				disconnect();
			}
		}
	}

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
