package proba;


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
import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.util.Timer;
import java.util.TimerTask;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;




public class GuiOfTheGame extends JFrame {

	private static final long serialVersionUID = 1L;
	private CoTheActions InstCoTheActions;
	JumbleImage split = null;
	String imagename = "";
	String playerName="";
	String loadedGame="";
	int tableSize;

	GuiOfTheGame(CoTheActions Co) {
		
		super("Tili toli");
		InstCoTheActions = Co;
		setSize(720, 720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		Timer timer = new Timer();
		
		JPanel exerciseField = new JPanel(new BorderLayout());
		exerciseField.setBounds(30, 30, 400, 400);
		exerciseField.setBorder(BorderFactory.createTitledBorder("Exercise"));
		add(exerciseField);
		
		JPanel origPicField = new JPanel(new BorderLayout());
		origPicField.setBounds(470,430 , 200, 200);
		origPicField.setBorder(BorderFactory.createTitledBorder("Original Picture"));
		add(origPicField);
		
		JPanel statusInf = new JPanel();
		statusInf.setLayout(new BoxLayout(statusInf, BoxLayout.PAGE_AXIS));
		statusInf.setBounds(470,30 , 200, 400);
		statusInf.setBorder(BorderFactory.createTitledBorder("Status of the game"));
		add(statusInf);
		
		JLabel timeFromGameStart = new JLabel("Time:");
		statusInf.add(timeFromGameStart);
	    
	    JLabel numberOfMoves = new JLabel("Moves:");
		statusInf.add(numberOfMoves);
		
		JLabel numberOfGood = new JLabel("Pieces in the right place: ");
		statusInf.add(numberOfGood);
		
		
		JMenuBar mbarOfTheGame= new JMenuBar();
		JMenu NGame = new JMenu("New game");

		JMenuItem SingleUser = new JMenuItem("Single");
		SingleUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				
				playerName=InstCoTheActions.getPlayername();
				imagename=InstCoTheActions.getImagename();
				tableSize = InstCoTheActions.getTableParam();
				
				origPicField.removeAll();
				origPicField.add(new JumbleImage(imagename, 180, tableSize));
				JumbleImage temp = new JumbleImage(imagename, 375, tableSize);	// a Panel címe miatt nem fér ki a 400
				split = temp;
				split.jumble();
				exerciseField.removeAll();
				exerciseField.add(split);
				revalidate();
				repaint();
				numberOfMoves.setText("Moves: 0");
				timeFromGameStart.setText("Time: 0:00");
				numberOfGood.setText("Pieces in the right place: " + split.check());
			}
		});
		NGame.add(SingleUser);
		
		JMenuItem MultUser = new JMenuItem("Multi User");
		MultUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				InstCoTheActions.startMultiGame();
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
				JumbleImage temp = new JumbleImage(imagename, 375, Integer.parseInt(content.substring(0,content.indexOf(','))));
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
			}
		});
		Options.add(Load);
		
		JMenuItem Save = new JMenuItem("Save");
		Save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
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
				split.move(evObject.getX()-5, evObject.getY()-18);	// pontosan az egér hegyére igazítás
				exerciseField.add(split);
				exerciseField.repaint();
				numberOfMoves.setText("Moves: " + split.getmoves());
				numberOfGood.setText("Pieces in the right place: " + split.check());
			}
		});
		
		timer.scheduleAtFixedRate(new TimerTask() {
			  public void run() {
				  if(split != null){
					  timeFromGameStart.setText("Time: " + split.elapsedTime());
				  }
			  }
			}, (long) 1000, (long) 1000);
	    
		setVisible(true);
	}	
}