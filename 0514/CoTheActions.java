package proba;

import javax.swing.JOptionPane;
import java.awt.Component;
import javax.swing.JFileChooser;
import java.io.File;







public class CoTheActions {

	CoTheActions() {   														//konstruktor
	}
	
	void startMultiGame() {
		
		System.out.println("multi game starting");
	
	}

	int startExit() {
		
		
		Component frame3=null;
	
		int b = JOptionPane.showConfirmDialog(
            frame3,
            "Would you like to save the game?");
	
		if (b == JOptionPane.OK_OPTION) {
	    
	     
	    }
		else {
			
			System.out.println("Cancel");
		}

	
	return b;   
	
	}
	
	
	String getImagename(){
		
		Component fr_1=null;
		String defaultLocation_image = "";
		String file_n = File.separator+defaultLocation_image;
		JFileChooser filech = new JFileChooser(new File(file_n));
		filech.setDialogTitle("Select a picture");
		filech.showOpenDialog(fr_1);
		System.out.println(filech.getSelectedFile().getAbsolutePath());
		return filech.getSelectedFile().getAbsolutePath();
			
		}
	
	String getPlayername(){
		
		String playerName;
		Component fr_2=null;
		
		playerName = (String)JOptionPane.showInputDialog(
		                    fr_2,
		                    "Player name:",
		                    "Start the game",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    "player_01");

		if ((playerName != null)) {
			
			return playerName;
		}
		
		return "error";
		
		}
	
	int getTableParam(){
		
		Component fr_3=null;
		Object[] possibilities = {"3", "4", "5", "6"};
		String s = (String)JOptionPane.showInputDialog(
		                    fr_3,
		                    "Row and column size:",
		                    "Setting of table parameters",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    possibilities,
		                    "3");
		
		return Integer.parseInt(s);
	
	}
	
	void startDisconnect(){
		
	System.out.println("disconnect starting");
			
	}
	
	String startLoad(){
		
		Component fr_4=null;
		String defaultLocation_saved_games = "";
		String file_n = File.separator+defaultLocation_saved_games;
		JFileChooser file_ch_2 = new JFileChooser(new File(file_n));
		file_ch_2.setDialogTitle("Choose from the saved games");
		file_ch_2.showOpenDialog(fr_4);
		return file_ch_2.getSelectedFile().getName();
	
				
	}
	
}
	

