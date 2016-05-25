package puzzle;

/**
 * 
 * Absztrakt oszt�ly a h�l�zati interf�szhez.
 * <p>
 * Kiterjeszti a {@link NetworkClient} �s a {@link NetworkServer} oszt�ly.
 * 
 * @author Lacz� Gyula
 * @author Varga Bal�zs
 * @author Winkelman Viktor
 *
 */
abstract class Network {

	/**
	 * Az a JumbleImage objektum, amelyb�l p�ld�nyos�tj�k az oszt�lyt.
	 */
	protected JumbleImage jumb;

	/**
	 * Konstruktor.
	 * 
	 * @param j JumbleImage objektum, amellyel �ssze lesz kapcsolva az oszt�ly adott p�ld�nya
	 */
	Network(JumbleImage j) {
		jumb = j;
	}

	/**
	 * Kapcsol�d�s.
	 * 
	 * @param ip szerver IP c�me (csak kliens m�dban haszn�lt)
	 * @param port ezen a porton kapcsol�dunk
	 */
	abstract void connect(String ip, int port);

	/**
	 * Kapcsolat bont�sa.
	 */
	abstract void disconnect();

	/**
	 * Adat k�ld�se.
	 * 
	 * @param s az elk�ldend� sztring t�mb
	 */
	abstract void send(String[] s);
}
