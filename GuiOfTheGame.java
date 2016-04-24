package proba;


import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;



public class GuiOfTheGame extends JFrame {

	private static final long serialVersionUID = 1L;
	private CoTheActions InstCoTheActions;
	

	GuiOfTheGame(CoTheActions Co) {
		
		super("proba");
		InstCoTheActions = Co;
		setSize(720, 720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);

		
		
		JMenuBar mbarOfTheGame= new JMenuBar();

		JMenu NGame = new JMenu("New game");

		JMenuItem SingleUser = new JMenuItem("Single");
		SingleUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evObject) {
				InstCoTheActions.startSingleGame();
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

		
		
		JPanel exerciseField = new JPanel(new BorderLayout());
		exerciseField.setBounds(30, 30, 400, 400);
		exerciseField.setBorder(BorderFactory.createTitledBorder("Exercise"));
		exerciseField.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent evObject) {
				InstCoTheActions.mouseEvent(new Point(evObject.getX(), evObject.getY()));
			}
		});
		add(exerciseField);

		
		
		JPanel origPicField = new JPanel(new BorderLayout());
		origPicField.setBounds(470,430 , 200, 200);
		origPicField.setBorder(BorderFactory.createTitledBorder("Original Picture"));
		add(origPicField);
		
		
		JPanel statusInf = new JPanel(new BorderLayout());
		statusInf.setBounds(470,30 , 200, 400);
		statusInf.setBorder(BorderFactory.createTitledBorder("Status of the game"));
		
		add(statusInf);
		
		
		
		JLabel timeFromGameStart = new JLabel("Time from start of the game:");
		timeFromGameStart.setVerticalTextPosition(JLabel.TOP);
		timeFromGameStart.setHorizontalTextPosition(JLabel.CENTER);
		
	    statusInf.add(timeFromGameStart);
	    
		setVisible(true);
	}

	

	
}
