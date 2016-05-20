package puzzle;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.File;


public class CoTheActions {
	
	CoTheActions() {   	//konstruktor
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
	    label.add(new JLabel("Image:", SwingConstants.RIGHT));
	    panel.add(label,BorderLayout.WEST);
	    label.add(new JLabel("Board size:", SwingConstants.RIGHT));
	    panel.add(label,BorderLayout.WEST);

	    JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
	    String[] opts={"Server","Client"};
	    JComboBox<String> mode = new JComboBox<String>(opts);
	    controls.add(mode);
	    JTextField ip = new JTextField("127.0.0.1");
	    controls.add(ip);
	    ip.setEnabled(false);
	    JTextField port = new JTextField("10007");
	    controls.add(port);
	    String[] imgopts={"Pug","Porsche","Woods"};
	    JComboBox<String> imgsel = new JComboBox<String>(imgopts);
	    controls.add(imgsel);
	    String[] brdopts={"3x3","4x4","5x5","6x6"};
	    JComboBox<String> brdset = new JComboBox<String>(brdopts);
	    controls.add(brdset);
	    panel.add(controls, BorderLayout.CENTER);
	    
	    mode.addActionListener(new ActionListener(){
	    	@Override
	    	public void actionPerformed(ActionEvent evObject){
	    		if(mode.getSelectedIndex()==0){ //ha szerver, akkor nem lehet IP-t megadni
	    			ip.setEnabled(false);
	    			imgsel.setEnabled(true);
	    			brdset.setEnabled(true);	    			
	    		}
	    		else{ //ha kliens, akkor nem lehet kepet es meretet valaszani
	    			ip.setEnabled(true);
	    			imgsel.setEnabled(false);
	    			brdset.setEnabled(false);
	    		}
	    	}
	    });

	    JOptionPane.showMessageDialog(null, panel, "Start Multiplayer", JOptionPane.QUESTION_MESSAGE);
	    
	    String[] s=new String[5];
	    s[0]=(String)mode.getSelectedItem(); //elso helyen: Server vagy Client
	    s[1]=(String)ip.getText(); //masodik helyen: IP cim
	    s[2]=(String)port.getText(); //harmadik helyen: port
	    s[3]=(String)imgsel.getSelectedItem();
	    s[3]=s[3].toLowerCase()+".jpg"; //negyedik helyen: kepfajl neve
	    s[4]=(String)brdset.getSelectedItem();
	    s[4]=s[4].substring(0,1); //otodik helyen: egy szam (3-6), a keveres merete
	    return s;
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
		Object[] source = {"Default","Own"};
		Object[] def = {"Pug","Porsche","Woods"};
		String imageSource = (String)JOptionPane.showInputDialog(
                fr_1,
                "Image source:",
                "Select image source",
                JOptionPane.PLAIN_MESSAGE,
                null,
                source,
                source[0]);	
		if(imageSource=="Default"){
			String image = (String)JOptionPane.showInputDialog(
	                fr_1,
	                "Image:",
	                "Select image",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                def,
	                def[0]);
			return image.toLowerCase()+".jpg";
		}
		else if(imageSource=="Own"){
			fr_1 = null;
			String defaultLocation_image = "";
			String file_n = File.separator + defaultLocation_image;
			JFileChooser filech = new JFileChooser(new File(file_n));
			filech.setDialogTitle("Select a picture");
			filech.showOpenDialog(fr_1);
			System.out.println(filech.getSelectedFile().getAbsolutePath());
			return filech.getSelectedFile().getAbsolutePath();
		}
		return "error";
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
