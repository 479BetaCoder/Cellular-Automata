package edu.neu.csye6200.ma;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * @author RaviKumar ClassName : MAutomataDriver Description : Main class for
 *         displaying valid 2D automata in Swing UI.  
 *         Valuable Output : Generates a UI and UI action elements. Uses MARegionSet
 *         extensively to generate consecutive regions.
 */

public class MAutomataDriver extends JPanel {

	private static final long serialVersionUID = 1L;
	
	protected static JButton startButton, pauseButton, rewindButton;

	// Display region
	private static final int FRAME_WIDTH = 520;
	private static final int FRAME_HEIGHT = 590;
	private static final int BUTTONS_HEIGHT = 80;

	// For Logging application process to the console.
	private static Logger log = Logger.getLogger(MAutomataDriver.class.getName());

	// Constructor to initialize the entire UI. 
	public MAutomataDriver(MARegionSet regionSet) {

		try {

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // for getting system's look and feel.
			} catch (ClassNotFoundException e) {
				log.severe("OOPS!! You have run into trouble creating the UIManager. Error Details : " + e.toString());

			}

			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			// Setting preferred Sizes
			regionSet.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
			regionSet.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
			add(regionSet);

			add(Box.createRigidArea(new Dimension(FRAME_WIDTH, 5)));

			// Constructing the JPanel for user interaction
			JPanel userPanel = new JPanel();
			userPanel.setSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
			userPanel.setPreferredSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
			userPanel.setMaximumSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
			userPanel.setBackground(Color.WHITE);
			add(BorderLayout.NORTH,userPanel);
			

			// Creating required buttons to help User start the Automata.
			startButton = new JButton("Start");
			// place button at center, left position
			startButton.setVerticalTextPosition(AbstractButton.CENTER);
			startButton.setHorizontalTextPosition(AbstractButton.LEFT);
			startButton.setMnemonic(KeyEvent.VK_D);
			startButton.setActionCommand("start");

			pauseButton = new JButton("Pause");
			// Using default button position (Center, Right)
			pauseButton.setMnemonic(KeyEvent.VK_E);
			pauseButton.setActionCommand("pause");
			pauseButton.setEnabled(false);

			rewindButton = new JButton("Rewind");
			// Using default button position (Center, Right)
			rewindButton.setMnemonic(KeyEvent.VK_E);
			rewindButton.setActionCommand("rewind");
			rewindButton.setEnabled(false);

			// Adding action Listeners to enable event handling for Start
			startButton.addActionListener(new ActionListener() {

				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					startButton.setEnabled(false);
					pauseButton.setEnabled(true);
					rewindButton.setEnabled(false);

					// Start the automata thread
					regionSet.nextRegion();
				}
			});

			// Adding action Listeners to enable event handling for Pause
			pauseButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					pauseButton.setEnabled(false);
					rewindButton.setEnabled(true);
					startButton.setEnabled(true);
					// Inform the thread to pause at next opportunity
					regionSet.pauseThread();

				}
			});

			// Adding action Listeners to enable event handling for Rewind
			rewindButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					log.info("Simulation will now rewind...");
					rewindButton.setEnabled(false);
					startButton.setEnabled(false);
					pauseButton.setEnabled(true);
					regionSet.rewindRegion();

				}
			});

			// Adding the buttons to the panel and the panel is added to the layout
			userPanel.add(startButton);
			userPanel.add(pauseButton);
			userPanel.add(rewindButton);
			add(userPanel);

			log.info("MAutomataDriver holding the UI created successfully... Get ready to witness the simulation !!!");
		} catch (Exception e) {
			log.severe("MAutomataDriver holding the UI creation failed. Error Details : " + e.toString());
		}
	}
	
	



	// Main routine for getting user inputs and displaying in Swing UI
	public static void main(String[] args) {

		// Getting these values to start constructing the grid 
		int rows, cols, ruleNumber, generationLimit, middleCell, initAliveCell, sleepTime;

		MARegion maRegion; // MARegions for setting up the automata
		MARegionSet maRegionSet; // MARegionSet which holds a Map of MARegions

		/*
		 * For testing displaying only forward way of automata, the reverse automata and
		 * other features will be included in 5b. Other features can be viewed using
		 * MADriver.java as the main Java Application
		 */
		try (Scanner scnObj = new Scanner(System.in)) {
			System.out.println("Welcome to Mobile Automata");
			System.out.println("********************************************************");
			System.out.println(
					"Automation is viewed best till 200*200 dimension. Please choose rows and columns wisely..."); // This
																													// restriction
																													// can
																													// be
																													// placed
																													// through
																													// UI
																													// in
																													// 5b
			System.out.println("Please enter rows you would like to see in the region : ");
			rows = scnObj.nextInt();
			System.out.println("Please enter columns you would like to see in the region : ");
			cols = scnObj.nextInt();
			System.out.println("Please enter your rule choice : \nEnter 1 for " + RuleNames.DEADALIVE + "\nEnter 2 for "
					+ RuleNames.BRIANSBRAIN + "\nEnter 3 for " + RuleNames.TOPDOWNTREE + "\nEnter 4 for "
					+ RuleNames.GOLDWINNER + "\nEnter 5 for " + RuleNames.MAZERUNNER);
			ruleNumber = scnObj.nextInt();
			System.out.println("Please enter Generation Limit Count : ");
			generationLimit = scnObj.nextInt();
			System.out.println("Please enter the rate at which you want to see the generations (milli seconds) : ");
			sleepTime = scnObj.nextInt();

			middleCell = Math.round((rows * cols) / 2); // for testing
			initAliveCell = middleCell - Math.round(cols / 2); // for testing

			/*
			 * For Testing but we can take inputs from user using drop down box/combo box in
			 * Swing UI. InitAliveCell can be taken as input through selecting a cell in the
			 * UI or passing it as a value.
			 */
			if (ruleNumber == 1) {
				maRegion = new MARegion(RuleNames.DEADALIVE, rows, cols, initAliveCell);
			} else if (ruleNumber == 2) {
				maRegion = new MARegion(RuleNames.BRIANSBRAIN, rows, cols, initAliveCell);
			} else if (ruleNumber == 3) {
				maRegion = new MARegion(RuleNames.TOPDOWNTREE, rows, cols, Math.round(cols / 2));
			} else if (ruleNumber == 4) {
				maRegion = new MARegion(RuleNames.GOLDWINNER, rows, cols, cols * rows - Math.round(cols / 2));
			} else {
				maRegion = new MARegion(RuleNames.MAZERUNNER, rows, cols, 0);
			}

			// Creating the Initial RegionSet with Initial Region.
			maRegionSet = MARegionSet.setRSInstance(maRegion, generationLimit, sleepTime);
			JFrame UIframe = new JFrame("Mobile Automata");

			// Sample UI just to show a valid 2D Automata
			UIframe.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			UIframe.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					log.warning("Window closed. Simulation is stopped...");
					System.exit(0);
				}
			});

			// Adding the RegionSet to the view.
			MAutomataDriver ma = new MAutomataDriver(maRegionSet);
			UIframe.getContentPane().add(ma, BorderLayout.CENTER);
			UIframe.setVisible(true);

		} catch (Exception e) {
			log.severe("An exception occurred while creating UI. Details : " + e.toString());
			System.exit(0);
		}
	}

}
