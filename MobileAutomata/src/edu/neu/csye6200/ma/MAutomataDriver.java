package edu.neu.csye6200.ma;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;



/**
 * @author RaviKumar ClassName : MAutomataDriver Description : Main class for
 *         displaying valid 2D automata in Swing UI. Valuable Output : Generates
 *         a UI and UI action elements. Uses MARegionSet extensively to generate
 *         consecutive regions.
 */

public class MAutomataDriver extends MAApp {

	protected static JButton startButton, pauseButton, rewindButton, stopButton, createButton;
	protected static JPanel mainPanel, northPanel, nConfig, nPlay, statusPanel;
	private MARegionSet maRegionSet;
	protected static JComboBox<Integer> comboRows = null;
	protected static JComboBox<Integer> comboCol = null;
	protected static JComboBox<RuleNames> comboRules = null;
	protected static JComboBox<Integer> comboSleepTime = null;
	protected static JComboBox<Integer> comboGenLimit = null;
	protected JLabel lblRows = null;
	protected JLabel lblCols = null;
	protected JLabel lblRules = null;
	protected JLabel lblSleepTime = null;
	protected JLabel lblGenLimit = null;
	protected JLabel lblGenCount = null;
	protected static JLabel genCount = null;
	protected static JLabel lblStatus = null;

	// Display region
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 600;
	private static final int BUTTONS_HEIGHT = 80;

	// For Logging application process to the console.
	private static Logger log = Logger.getLogger(MAutomataDriver.class.getName());

	public static void main(String[] args) {

		new MAutomataDriver();
		log.info("Mobile Automation started...");
	}

	// Constructor
	public MAutomataDriver() {
		
		// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setTitle("Mobile Automata");
		frame.setResizable(true);
		menuMgr.createDefaultActions(); // Set up default menu items

		showUI(); // Cause the Swing Dispatch thread to display the JFrame

	}

	public void windowClosing(WindowEvent e) {
		log.warning("Window closed. Simulation is stopped...");
		System.exit(0);
	}

	/**
	 * Create a main panel that will hold the bulk of our application display
	 */
	@Override
	public JPanel getMainPanel() {

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(BorderLayout.NORTH, getnorthPanel());
		mainPanel.setVisible(true);
		return mainPanel;
	}

	// North Panel
	public JPanel getnorthPanel() {

		northPanel = new JPanel();

		northPanel.setLayout(new FlowLayout());
		northPanel.setBackground(Color.WHITE);
		northPanel.setPreferredSize(new Dimension(100, 100));

		nConfig = new JPanel();

		nConfig.setLayout(new FlowLayout());
		nConfig.setBackground(Color.WHITE);

		// Combo Boxes

		lblRows = new JLabel("Rows");
		nConfig.add(lblRows);
		final Integer rows[] = { 40, 60, 80, 100, 120 };
		comboRows = new JComboBox<Integer>(rows);
		comboRows.setMaximumRowCount(5);
		comboRows.setEditable(false);
		comboRows.addActionListener(this);
		nConfig.add(comboRows);

		lblCols = new JLabel("Columns");
		nConfig.add(lblCols);
		final Integer cols[] = { 40, 60, 80, 100, 120 };
		comboCol = new JComboBox<Integer>(cols);
		comboCol.setMaximumRowCount(5);
		comboCol.setEditable(false);
		comboCol.addActionListener(this);
		nConfig.add(comboCol);

		lblRules = new JLabel("Rules");
		nConfig.add(lblRules);
		final RuleNames rulesNames[] = { RuleNames.DEADALIVE, RuleNames.BRIANSBRAIN, RuleNames.TOPDOWNTREE,
				RuleNames.GOLDWINNER, RuleNames.MAZERUNNER };
		comboRules = new JComboBox<RuleNames>(rulesNames);
		comboRules.setMaximumRowCount(5);
		comboRules.setEditable(false);
		comboRules.addActionListener(this);
		nConfig.add(comboRules);

		// Text Boxes
		lblSleepTime = new JLabel("Sleep Time");
		nConfig.add(lblSleepTime);
		final Integer slTimes[] = { 100, 200, 300, 400, 600 };
		comboSleepTime = new JComboBox<Integer>(slTimes);
		comboSleepTime.setMaximumRowCount(5);
		comboSleepTime.setEditable(false);
		comboSleepTime.addActionListener(this);
		nConfig.add(comboSleepTime);

		lblGenLimit = new JLabel("Generation Limit");
		nConfig.add(lblGenLimit);
		final Integer genLimits[] = { 50, 100, 150, 200, 400 };
		comboGenLimit = new JComboBox<Integer>(genLimits);
		comboGenLimit.setMaximumRowCount(5);
		comboGenLimit.setEditable(false);
		comboGenLimit.addActionListener(this);
		nConfig.add(comboGenLimit);

		// Create Button
		createButton = new JButton("Create");
		createButton.addActionListener(this);
		createButton.setFocusPainted(false);
		nConfig.add(createButton);

		nPlay = new JPanel();
		nPlay.setLayout(new FlowLayout());
		nPlay.setBackground(Color.WHITE);

		// Buttons
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		startButton.setFocusPainted(false);
		nPlay.add(startButton);

		pauseButton = new JButton("Pause"); // Allow the app to hear about button pushes
		pauseButton.addActionListener(this);
		nPlay.add(pauseButton);

		rewindButton = new JButton("Rewind"); // Allow the app to hear about button
		rewindButton.addActionListener(this);
		nPlay.add(rewindButton);

		stopButton = new JButton("Stop"); // Allow the app to hear about button
		stopButton.addActionListener(this);
		nPlay.add(stopButton);

		// Labels
		lblGenCount = new JLabel("Generations : ", 4); // Allow the app to hear Input button pushes
		nPlay.add(lblGenCount);

		genCount = new JLabel("0", 4);
		nPlay.add(genCount);

		// Status Panel
		statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout());
		statusPanel.setBackground(Color.LIGHT_GRAY);
		mainPanel.add(BorderLayout.SOUTH, statusPanel);

		// Labels
		lblStatus = new JLabel("", 10);
		statusPanel.add(lblStatus);

		// Initially disabling till user creates a simulation
		for (Component component : nPlay.getComponents()) {
			component.setEnabled(false);
		}

		northPanel.add(nConfig);
		northPanel.add(nPlay);

		return northPanel;
	}

	@Override
	// The related action for the buttons
	public void actionPerformed(ActionEvent ae) {

		if ("Create".equals(ae.getActionCommand())) {

			// Initialization

			if (Arrays.asList(mainPanel.getComponents()).contains(maRegionSet))
				mainPanel.remove(maRegionSet);

			MACell.cellCount = 0;

			int rows = (int) comboRows.getSelectedItem();
			int cols = (int) comboCol.getSelectedItem();
			RuleNames rule = (RuleNames) comboRules.getSelectedItem();
			int maximumGen = (int) comboGenLimit.getSelectedItem();
			int sleepTime = (int) comboSleepTime.getSelectedItem();

			int middleCell = Math.round((rows * cols) / 2);
			int initAliveCell = middleCell - Math.round(cols / 2);

			if (rule.compareTo(RuleNames.TOPDOWNTREE) == 0) {
				initAliveCell = Math.round(cols / 2);
			} else if (rule.compareTo(RuleNames.GOLDWINNER) == 0) {
				initAliveCell = cols * rows - Math.round(cols / 2);
			} else if (rule.compareTo(RuleNames.MAZERUNNER) == 0) {
				initAliveCell = 0;
			}

			MARegion maRegion = new MARegion(rule, rows, cols, initAliveCell);
			maRegionSet = new MARegionSet(maRegion, maximumGen, sleepTime);

			// maRegionSet.setLayout(new BoxLayout(maRegionSet, BoxLayout.Y_AXIS));
			maRegionSet.setLayout(new BorderLayout());
			// Setting preferred Sizes
			maRegionSet.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
			maRegionSet.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));

			// Disabling nConfig till user is done with the simulation
			for (Component component : nConfig.getComponents()) {
				component.setEnabled(false);
			}

			// Enabling nPlay till user is done with the simulation
			for (Component component : nPlay.getComponents()) {
				component.setEnabled(true);
			}

			// Initially there is no need to rewind or pause as the simulation is not yet
			// started
			pauseButton.setEnabled(false);
			rewindButton.setEnabled(false);

			maRegionSet.setBackground(Color.WHITE);
			mainPanel.add(BorderLayout.CENTER, maRegionSet);
			frame.setVisible(true);
			lblStatus.setText("Simulation Region Created successfully...");

		} else if ("Start".equals(ae.getActionCommand())) {

			lblStatus.setText("Mobile Automata Started ... !!! ");
			log.info("Starting the mobile simulation ... !!! ");

			// In start mode, you can pause/stop the simulation
			startButton.setEnabled(false);
			rewindButton.setEnabled(false);

			pauseButton.setEnabled(true);
			stopButton.setEnabled(true);

			// Getting the next Region
			maRegionSet.nextRegion();

		} else if ("Pause".equals(ae.getActionCommand())) {

			// In pause mode you can rewind,start,stop the simulation
			pauseButton.setEnabled(false);

			rewindButton.setEnabled(true);
			startButton.setEnabled(true);
			stopButton.setEnabled(true);

			// Inform the thread to pause the simulation
			maRegionSet.pauseThread();

		} else if ("Rewind".equals(ae.getActionCommand())) {

			lblStatus.setText("Mobile Automata is rewinding ...");
			log.info("Simulation will now rewind...");

			rewindButton.setEnabled(false);
			startButton.setEnabled(false);
			pauseButton.setEnabled(true);
			stopButton.setEnabled(true);

			// Inform the thread to rewind the simulation
			maRegionSet.rewindRegion();

		} else if ("Stop".equals(ae.getActionCommand())) {
			lblStatus.setText("Simulation stopped . Thank you for using Mobile Automata ... !!");
			log.info("Simulation stopped . Thank you for using Mobile Automata ... !!");

			genCount.setText("0");
			maRegionSet.stopThread();
			maRegionSet.setVisible(false);

			// Enabling nConfig as the user is done/stopped the simulation
			for (Component component : nConfig.getComponents()) {
				component.setEnabled(true);
			}

			// Disabling nPlay as user is done with the simulation
			for (Component component : nPlay.getComponents()) {
				component.setEnabled(false);
			}

		}

	}

//	// Constructor to initialize the entire UI.
//	public MAutomataDriver(MARegionSet regionSet) {
//
//		try {
//
//			try {
//				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // for getting system's look and
//																						// feel.
//			} catch (ClassNotFoundException e) {
//				log.severe("OOPS!! You have run into trouble creating the UIManager. Error Details : " + e.toString());
//
//			}
//
//			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//			// Setting preferred Sizes
//			regionSet.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
//			regionSet.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
//			add(regionSet);
//
//			add(Box.createRigidArea(new Dimension(FRAME_WIDTH, 5)));
//
//			// Constructing the JPanel for user interaction
//			JPanel userPanel = new JPanel();
//			userPanel.setSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
//			userPanel.setPreferredSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
//			userPanel.setMaximumSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
//			userPanel.setBackground(Color.WHITE);
//			add(BorderLayout.NORTH, userPanel);
//
//			// Creating required buttons to help User start the Automata.
//			startButton = new JButton("Start");
//			// place button at center, left position
//			startButton.setVerticalTextPosition(AbstractButton.CENTER);
//			startButton.setHorizontalTextPosition(AbstractButton.LEFT);
//			startButton.setMnemonic(KeyEvent.VK_D);
//			startButton.setActionCommand("start");
//
//			pauseButton = new JButton("Pause");
//			// Using default button position (Center, Right)
//			pauseButton.setMnemonic(KeyEvent.VK_E);
//			pauseButton.setActionCommand("pause");
//			pauseButton.setEnabled(false);
//
//			rewindButton = new JButton("Rewind");
//			// Using default button position (Center, Right)
//			rewindButton.setMnemonic(KeyEvent.VK_E);
//			rewindButton.setActionCommand("rewind");
//			rewindButton.setEnabled(false);
//
//			// Adding action Listeners to enable event handling for Start
//			startButton.addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//
//					startButton.setEnabled(false);
//					pauseButton.setEnabled(true);
//					rewindButton.setEnabled(false);
//
//					// Start the automata thread
//					regionSet.nextRegion();
//				}
//			});
//
//			// Adding action Listeners to enable event handling for Pause
//			pauseButton.addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					pauseButton.setEnabled(false);
//					rewindButton.setEnabled(true);
//					startButton.setEnabled(true);
//					// Inform the thread to pause at next opportunity
//					regionSet.pauseThread();
//
//				}
//			});
//
//			// Adding action Listeners to enable event handling for Rewind
//			rewindButton.addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					log.info("Simulation will now rewind...");
//					rewindButton.setEnabled(false);
//					startButton.setEnabled(false);
//					pauseButton.setEnabled(true);
//					regionSet.rewindRegion();
//
//				}
//			});
//
//			// Adding the buttons to the panel and the panel is added to the layout
//			userPanel.add(startButton);
//			userPanel.add(pauseButton);
//			userPanel.add(rewindButton);
//			add(userPanel);
//
//			log.info("MAutomataDriver holding the UI created successfully... Get ready to witness the simulation !!!");
//		} catch (Exception e) {
//			log.severe("MAutomataDriver holding the UI creation failed. Error Details : " + e.toString());
//		}
//	}

	// Main routine for getting user inputs and displaying in Swing UI
//	public static void main(String[] args) {
//
//		MAutomataDriver MAApp = new MAutomataDriver();
//		log.info("Mobile Automation started...");

//		// Getting these values to start constructing the grid
//		int rows, cols, ruleNumber, generationLimit, middleCell, initAliveCell, sleepTime;
//
//		MARegion maRegion; // MARegions for setting up the automata
//		MARegionSet maRegionSet; // MARegionSet which holds a Map of MARegions

	/*
	 * For testing displaying only forward way of automata, the reverse automata and
	 * other features will be included in 5b. Other features can be viewed using
	 * MADriver.java as the main Java Application
	 */
//		try (Scanner scnObj = new Scanner(System.in)) {
//			System.out.println("Welcome to Mobile Automata");
//			System.out.println("********************************************************");
//			System.out.println(
//					"Automation is viewed best till 200*200 dimension. Please choose rows and columns wisely..."); // This
//																													// restriction
//																													// can
//																													// be
//																													// placed
//																													// through
//																													// UI
//																													// in
//																													// 5b
//			System.out.println("Please enter rows you would like to see in the region : ");
//			rows = scnObj.nextInt();
//			System.out.println("Please enter columns you would like to see in the region : ");
//			cols = scnObj.nextInt();
//			System.out.println("Please enter your rule choice : \nEnter 1 for " + RuleNames.DEADALIVE + "\nEnter 2 for "
//					+ RuleNames.BRIANSBRAIN + "\nEnter 3 for " + RuleNames.TOPDOWNTREE + "\nEnter 4 for "
//					+ RuleNames.GOLDWINNER + "\nEnter 5 for " + RuleNames.MAZERUNNER);
//			ruleNumber = scnObj.nextInt();
//			System.out.println("Please enter Generation Limit Count : ");
//			generationLimit = scnObj.nextInt();
//			System.out.println("Please enter the rate at which you want to see the generations (milli seconds) : ");
//			sleepTime = scnObj.nextInt();
//
//			middleCell = Math.round((rows * cols) / 2); // for testing
//			initAliveCell = middleCell - Math.round(cols / 2); // for testing
//
//			/*
//			 * For Testing but we can take inputs from user using drop down box/combo box in
//			 * Swing UI. InitAliveCell can be taken as input through selecting a cell in the
//			 * UI or passing it as a value.
//			 */
//			if (ruleNumber == 1) {
//				maRegion = new MARegion(RuleNames.DEADALIVE, rows, cols, initAliveCell);
//			} else if (ruleNumber == 2) {
//				maRegion = new MARegion(RuleNames.BRIANSBRAIN, rows, cols, initAliveCell);
//			} else if (ruleNumber == 3) {
//				maRegion = new MARegion(RuleNames.TOPDOWNTREE, rows, cols, Math.round(cols / 2));
//			} else if (ruleNumber == 4) {
//				maRegion = new MARegion(RuleNames.GOLDWINNER, rows, cols, cols * rows - Math.round(cols / 2));
//			} else {
//				maRegion = new MARegion(RuleNames.MAZERUNNER, rows, cols, 0);
//			}
//
//			// Creating the Initial RegionSet with Initial Region.
//			maRegionSet = MARegionSet.setRSInstance(maRegion, generationLimit, sleepTime);
//			JFrame UIframe = new JFrame("Mobile Automata");
//
//			// Sample UI just to show a valid 2D Automata
//			UIframe.setSize(FRAME_WIDTH, FRAME_HEIGHT);
//			UIframe.addWindowListener(new WindowAdapter() {
//				public void windowClosing(WindowEvent e) {
//					log.warning("Window closed. Simulation is stopped...");
//					System.exit(0);
//				}
//			});
//
//			// Adding the RegionSet to the view.
//			MAutomataDriver ma = new MAutomataDriver(maRegionSet);
//			UIframe.getContentPane().add(ma, BorderLayout.CENTER);
//			UIframe.setVisible(true);

//		} catch (Exception e) {
//			log.severe("An exception occurred while creating UI. Details : " + e.toString());
//			System.exit(0);
//		}

//	}

	@Override
	public void windowOpened(WindowEvent e) {
		log.info("Window Opened. Welcome to Mobile Automata Simulation World ....");

	}

	@Override
	public void windowClosed(WindowEvent e) {
		log.info("Window Closed. Sorry to see you go...");

	}

	@Override
	public void windowIconified(WindowEvent e) {
		log.info("Window iconified...");

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		log.info("Window Deiconified...");

	}

	@Override
	public void windowActivated(WindowEvent e) {
		log.info("Window Activated...");

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		log.info("Window DeActivated...");

	}

}
