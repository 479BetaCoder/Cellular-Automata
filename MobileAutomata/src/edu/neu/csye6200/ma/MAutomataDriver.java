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
		lblStatus.setText("Welcome to Mobile Automata Simulation World ... !!! ");
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
		final RuleNames rulesNames[] = { RuleNames.LOCKME, RuleNames.DEADALIVE, RuleNames.BRIANSBRAIN, RuleNames.TOPDOWNTREE,
				RuleNames.GOLDWINNER};
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
		final Integer genLimits[] = { 50, 100, 150, 200, 400, 1000 };
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
