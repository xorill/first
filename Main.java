package puzzle;

/**
 * 
 * Main osztály, elindítja a játékot.
 * 
 * @author Laczó Gyula
 * @author Varga Balázs
 * @author Winkelman Viktor
 *
 */
public class Main {

	/**
	 * Belépési pont, elindítja a játékot.
	 * 
	 * @param args argumentumok
	 */
	public static void main(String[] args) {
		
		System.out.println("Program starts");
		GuiOfTheGame gui = new GuiOfTheGame(new CoTheActions());
		

	}

}
