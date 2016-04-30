package proba;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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



public class GuiOfTheGame extends JFrame {

	private static final long serialVersionUID = 1L;
	private CoTheActions InstCoTheActions;
	JumbleImage split = null;

	GuiOfTheGame(CoTheActions Co) {
		
		super("proba");
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
		
		JLabel timeFromGameStart = new JLabel("Time from start of the game:");
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
				//InstCoTheActions.startSingleGame();
				//amíg nincs megcsinálva a dialógus, addig így működik:
				String imagename = "Ford-gt-1366x768.jpg";
				origPicField.removeAll();
				origPicField.add(new JumbleImage(imagename, 180));
				JumbleImage temp = new JumbleImage(imagename, 375);	// a Panel címe miatt nem fér ki a 400
				split = temp;
				split.jumble();
				exerciseField.removeAll();
				exerciseField.add(split);
				revalidate();
				repaint();
				numberOfMoves.setText("Moves: 0");
				timeFromGameStart.setText("Time from start of the game: 0:00");
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
				InstCoTheActions.startLoad();
			}
		});
		Options.add(Load);
		
		JMenuItem Save = new JMenuItem("Save");
		Save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				InstCoTheActions.saveGame();
			}
		});
		Options.add(Save);
		
		JMenuItem Exit = new JMenuItem("Exit");
		Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				System.exit(0);
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
					  timeFromGameStart.setText("Time from start of the game: " + split.elapsed_time());
				  }
			  }
			}, (long) 1000, (long) 1000);
	    
		setVisible(true);
	}	
}
