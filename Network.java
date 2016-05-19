package puzzle;

abstract class Network {

	protected JumbleImage jumb;

	Network(JumbleImage j) {
		jumb = j;
	}

	abstract void connect(String ip, int port);

	abstract void disconnect();

	abstract void send(String[] s);
}
