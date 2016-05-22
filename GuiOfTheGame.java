package puzzle;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import java.util.Timer;
import java.util.TimerTask;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class GuiOfTheGame extends JFrame {

	private static final long serialVersionUID = 1L;
	private CoTheActions InstCoTheActions;
	private String imagename = "";
	private String playerName = "";
	private String loadedGame = "";
	private int tableSize;
	private JPanel exerciseField, origPicField;
	private JLabel numberOfGood;
	JumbleImage split = null;
	
	private class WaiterThread implements Runnable {
		public void run(){
			while(!split.isConnected()) ;
			imagename=split.imgadr;
			split.startTimer();
			origPicField.add(new JumbleImage(imagename, 180, 3));
			numberOfGood.setText("Pieces in the right place: " + split.check());
			exerciseField.removeAll();
			exerciseField.add(split);
			revalidate();
			repaint();
		}
	}
	
	GuiOfTheGame(CoTheActions Co) {
		
		super("Tili toli");
		InstCoTheActions = Co;
		setSize(920, 720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		Timer timer = new Timer();
		
		exerciseField = new JPanel(new BorderLayout());
		exerciseField.setBounds(30, 30, 600, 600);
		exerciseField.setBorder(BorderFactory.createTitledBorder("Exercise"));
		add(exerciseField);
		
		origPicField = new JPanel(new BorderLayout());
		origPicField.setBounds(670,430 , 200, 200);
		origPicField.setBorder(BorderFactory.createTitledBorder("Original Picture"));
		add(origPicField);
		
		JPanel statusInf = new JPanel();
		statusInf.setLayout(new BoxLayout(statusInf, BoxLayout.PAGE_AXIS));
		statusInf.setBounds(670,30 , 200, 400);
		statusInf.setBorder(BorderFactory.createTitledBorder("Status of the game"));
		add(statusInf);
		
		JLabel timeFromGameStart = new JLabel("Time:");
		statusInf.add(timeFromGameStart);
	    
	    JLabel numberOfMoves = new JLabel("Moves:");
		statusInf.add(numberOfMoves);
		
		numberOfGood = new JLabel("Pieces in the right place: ");
		statusInf.add(numberOfGood);
		
		JLabel modeOfGame = new JLabel("Mode: ");
		statusInf.add(modeOfGame);
		
		JLabel othersMoves = new JLabel("");
		statusInf.add(othersMoves);
		
		JLabel othersGood = new JLabel("");
		statusInf.add(othersGood);	
		
		JMenuBar mbarOfTheGame= new JMenuBar();
		JMenu NGame = new JMenu("New game");

		JMenuItem SingleUser = new JMenuItem("Single");
		SingleUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				playerName = InstCoTheActions.getPlayername();
				imagename = InstCoTheActions.getImagename();
				tableSize = InstCoTheActions.getTableParam();
				
				origPicField.removeAll();
				origPicField.add(new JumbleImage(imagename, 180, tableSize));
				JumbleImage temp = new JumbleImage(imagename, 575, tableSize);	// a Panel cĂ­me miatt nem fĂ©r ki a 400
				split = temp;
				split.jumble();
				exerciseField.removeAll();
				exerciseField.add(split);
				revalidate();
				repaint();
				numberOfMoves.setText("Moves: 0");
				timeFromGameStart.setText("Time: 0:00");
				numberOfGood.setText("Pieces in the right place: " + split.check());
				modeOfGame.setText("Mode: single player");
				othersMoves.setText("");
				othersGood.setText("");
				split.startTimer();
				split.setMulti(false);
			}
		});
		NGame.add(SingleUser);
		
		JMenuItem MultUser = new JMenuItem("Multi User");

		MultUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				if((split!=null)&&(split.isMulti()&&(split.isConnected())))
					split.disconnect();
				exerciseField.removeAll();
				origPicField.removeAll();
				revalidate();
				repaint();
				numberOfMoves.setText("Moves: 0");
				timeFromGameStart.setText("Time: 0:00");
				numberOfGood.setText("Pieces in the right place: ");
				modeOfGame.setText("Mode: multiplayer");
				othersMoves.setText("Opponent's moves:");
				othersGood.setText("Opponent's right pieces:");
				
				String[] s=InstCoTheActions.startMultiGame();
				if(s[0].equals("Server")){
					modeOfGame.setText("Mode: multiplayer (server)");
					imagename=s[3];
					tableSize=Integer.parseInt(s[4]);
					split=new JumbleImage(imagename, 575, tableSize);
					split.jumble();
					split.setMulti(true);
					split.client=false;
					split.startServer(Integer.parseInt(s[2]));
				}
				else{
					modeOfGame.setText("Mode: multiplayer (client)");
					split=new JumbleImage();
					split.setMulti(true);
					split.client=true;
					split.startClient(s[1],Integer.parseInt(s[2]));
				}
				Thread wt=new Thread(new WaiterThread()); //varakozunk a kapcsolatra
				wt.start();
			}
		});
		NGame.add(MultUser);

		mbarOfTheGame.add(NGame);

			
		JMenu Options = new JMenu("Options");
		Options.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				InstCoTheActions.startDisconnect();
			}
		});
		mbarOfTheGame.add(Options);
		
		JMenuItem Disconn = new JMenuItem("Disconnect");
		Disconn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				InstCoTheActions.startDisconnect();
				if((split!=null)&&(split.isMulti()))split.disconnect();
			}
		});
		Options.add(Disconn);
		
		JMenuItem Load = new JMenuItem("Load");
		Load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				loadedGame = InstCoTheActions.startLoad();
				String content = null;
				File file = new File(loadedGame);
			    FileReader reader = null;
			    try {
			        reader = new FileReader(loadedGame);
			        char[] chars = new char[(int) file.length()];
			        reader.read(chars);
			        content = new String(chars);
			        reader.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    playerName = content.substring(0,content.indexOf(','));
			    content = content.substring(content.indexOf(',')+1);
			    imagename = content.substring(0,content.indexOf(','));
			    content = content.substring(content.indexOf(',')+1);
			    origPicField.removeAll();
				origPicField.add(new JumbleImage(imagename, 180, Integer.parseInt(content.substring(0,content.indexOf(',')))));
				JumbleImage temp = new JumbleImage(imagename, 575, Integer.parseInt(content.substring(0,content.indexOf(','))));
				split = temp;
				content = content.substring(content.indexOf(',')+1);
				split.loadState(content);
				exerciseField.removeAll();
				exerciseField.add(split);
				revalidate();
				repaint();
				timeFromGameStart.setText("Time: " + split.elapsedTime());
				numberOfMoves.setText("Moves: " + split.getmoves());
				numberOfGood.setText("Pieces in the right place: " + split.check());
				modeOfGame.setText("Mode: single player");
				othersMoves.setText("");
				othersGood.setText("");
				split.startTimer();
				split.setMulti(false);
			}
		});
		Options.add(Load);
		
		JMenuItem Save = new JMenuItem("Save");
		Save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				if(split==null)
					return;
				if(split.isMulti()){
					JOptionPane.showMessageDialog(null,"Cannot save game in multiplayer mode!","Sorry",JOptionPane.ERROR_MESSAGE);
					return;
				}
				PrintWriter writer = null;
				try {
					DateFormat date_F = new SimpleDateFormat("yyyyMMddHHmmss");
					Calendar c = Calendar.getInstance();
					writer = new PrintWriter(playerName + "_" + date_F.format(c.getTime()) + ".txt", "UTF-8");
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				writer.printf(playerName + "," + imagename + "," + split.state());
				writer.close();
			}
		});
		Options.add(Save);
		
		JMenuItem Exit = new JMenuItem("Exit");
		Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				int sel = InstCoTheActions.startExit();
				if(sel == 0){
					Save.doClick(1);
					System.exit(0);
				}
				else if(sel == 1) System.exit(0);
			}
		});
		Options.add(Exit);

		setJMenuBar(mbarOfTheGame);
		
		exerciseField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evObject) {
				//InstCoTheActions.mouseEvent(new Point(evObject.getX(), evObject.getY()));
				Component fr_1 = null;
				split.move(evObject.getX()-5, evObject.getY()-18);	// pontosan az egĂ©r hegyĂ©re igazĂ­tĂˇs
				exerciseField.add(split);
				exerciseField.repaint();
				numberOfMoves.setText("Moves: " + split.getmoves());
				numberOfGood.setText("Pieces in the right place: " + split.check());
				if(split.isMulti()) {	
					split.send();
				}
				if(split.getStop()) JOptionPane.showMessageDialog(
		                fr_1,
		                "Your stats:\nMoves: " + split.getmoves() + "\nTime: " + split.elapsedTime(),
		                "You win!",
		                JOptionPane.PLAIN_MESSAGE,
		                null);	
			}
		});
		
		timer.scheduleAtFixedRate(new TimerTask() {
			  public void run() {
				  if(split != null){
					  timeFromGameStart.setText("Time: " + split.elapsedTime());
					  if((split.isMulti())&&(split.isConnected())&&(!split.getStop())){
						  othersMoves.setText("Opponent's moves: "+split.getOppMoves());
						  othersGood.setText("Opponent's right pieces: "+split.getOppRight());
						  if(Integer.parseInt(split.getOppRight())==split.getNumCells()){
							  JOptionPane.showMessageDialog(null,"You lose!");
							  split.setStop(true);
						  }
					  }
				  }
			  }
			}, (long) 1000, (long) 100); //ez szandekosan 100ms! igy azonnal frissul az ellenfel allasa
	    
		setVisible(true);
	}	
}
