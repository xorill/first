package puzzle;

/**
 * 
 * Absztrakt osztály a hálózati interfészhez.
 * <p>
 * Kiterjeszti a {@link NetworkClient} és a {@link NetworkServer} osztály.
 * 
 * @author Laczó Gyula
 * @author Varga Balázs
 * @author Winkelman Viktor
 *
 */
abstract class Network {

	/**
	 * Az a JumbleImage objektum, amelybõl példányosítják az osztályt.
	 */
	protected JumbleImage jumb;

	/**
	 * Konstruktor.
	 * 
	 * @param j JumbleImage objektum, amellyel össze lesz kapcsolva az osztály adott példánya
	 */
	Network(JumbleImage j) {
		jumb = j;
	}

	/**
	 * Kapcsolódás.
	 * 
	 * @param ip szerver IP címe (csak kliens módban használt)
	 * @param port ezen a porton kapcsolódunk
	 */
	abstract void connect(String ip, int port);

	/**
	 * Kapcsolat bontása.
	 */
	abstract void disconnect();

	/**
	 * Adat küldése.
	 * 
	 * @param s az elküldendõ sztring tömb
	 */
	abstract void send(String[] s);
}
