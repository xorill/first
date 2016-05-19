package puzzle;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.File;


public class CoTheActions {
	
	CoTheActions() {   														//konstruktor
	}
	
	String[] startMultiGame() {
		
		System.out.println("multi game starting");
		
	    JPanel panel = new JPanel(new BorderLayout(5, 5));

	    JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
	    label.add(new JLabel("Mode:", SwingConstants.RIGHT));
	    panel.add(label,BorderLayout.WEST);
	    label.add(new JLabel("Server IP:", SwingConstants.RIGHT));
	    panel.add(label,BorderLayout.WEST);
	    label.add(new JLabel("Port:", SwingConstants.RIGHT));
	    panel.add(label,BorderLayout.WEST);

	    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    String[] opts={"Server","Client"};
	    JComboBox<String> mode = new JComboBox<String>(opts);
	    controls.add(mode);
	    JTextField ip = new JTextField("127.0.0.1");
	    controls.add(ip);
	    panel.add(controls, BorderLayout.CENTER);
	    ip.setEnabled(false);
	    mode.addActionListener(new ActionListener(){
	    	@Override
	    	public void actionPerformed(ActionEvent evObject){
	    		if(mode.getSelectedIndex()==0)
	    			ip.setEnabled(false);
	    		else
	    			ip.setEnabled(true);
	    	}
	    });
	    JTextField port = new JTextField("10007");
	    controls.add(port);

	    JOptionPane.showMessageDialog(null, panel, "Start Multiplayer", JOptionPane.QUESTION_MESSAGE);
	    
	    String[] s=new String[3];
	    s[0]=(String)mode.getSelectedItem();
	    s[1]=(String)ip.getText();
	    s[2]=(String)port.getText();
	    
	    return s; //ha szerver, akkor ures stringet adunk vissza; ha kliens, akkor a szerver IP-jet
	}

	int startExit() {
		
		
		Component frame3 = null;
		int b = JOptionPane.showConfirmDialog(frame3, "Would you like to save the game?");
		if (b == JOptionPane.OK_OPTION) {
	    }
		else {
			System.out.println("Cancel");
		}
		return b;
	}
	
	
	String getImagename(){
		
		Component fr_1 = null;
		String defaultLocation_image = "";
		String file_n = File.separator + defaultLocation_image;
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
		
		Component fr_3 = null;
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
		
		Component fr_4 = null;
		String defaultLocation_saved_games = "";
		String file_n = File.separator + defaultLocation_saved_games;
		JFileChooser file_ch_2 = new JFileChooser(new File(file_n));
		file_ch_2.setDialogTitle("Choose from the saved games");
		file_ch_2.showOpenDialog(fr_4);
		return file_ch_2.getSelectedFile().getName();
	}
	
}
