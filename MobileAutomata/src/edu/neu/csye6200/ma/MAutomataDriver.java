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

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * @author RaviKumar ClassName : MAutomataDriver Description : Main class for
 *         displaying valid 2D automata in Swing UI. Note : Not all features are
 *         included. Will be including them in UI part. Valuable Output : Gives
 *         a window to start the Automata and also Pause it. Uses MAFrameSet
 *         extensively to generate consecutive frames.
 */
public class MAutomataDriver extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MAFrameSet frameSet;
	private JButton startButton, pauseButton;

	// Display frame
	private static final int FRAME_WIDTH = 520;
	private static final int FRAME_HEIGHT = 590;
	private static final int BUTTONS_HEIGHT = 80;

	// Constructor to initialize the entire UI. Will be enhanced in the final UI
	// design.
	public MAutomataDriver(MAFrameSet frameSet) {

		this.frameSet = frameSet;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Error setting UI: " + e);
		}

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Setting preferred Sizes
		frameSet.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
		frameSet.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
		add(frameSet);

		add(Box.createRigidArea(new Dimension(FRAME_WIDTH, 5)));

		// Constructing the JPanel for display
		JPanel panel1 = new JPanel();
		panel1.setSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
		panel1.setPreferredSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
		panel1.setMaximumSize(new Dimension(FRAME_WIDTH, BUTTONS_HEIGHT));
		panel1.setBackground(Color.WHITE);

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

		// Adding action Listeners to enable event handling
		startButton.addActionListener(this);
		pauseButton.addActionListener(this);

		// Adding the buttons to the panel and the panel is added to the layout
		panel1.add(startButton);
		panel1.add(pauseButton);
		add(panel1);
	}

	// Event handler which responds to User interaction with the UI Window
	@Override
	public void actionPerformed(ActionEvent e) {
		// Starting the Mobile Automata
		if (e.getActionCommand().equals("start")) {
			startButton.setEnabled(false);
			pauseButton.setEnabled(true);
			// Start the automata thread
			frameSet.nextFrame();
		} else if (e.getActionCommand().equals("pause")) {
			startButton.setEnabled(true);
			pauseButton.setEnabled(false);
			// Inform the game thread to stop at next opportunity
			frameSet.pauseThread();
		}

	}

	// Main routine for getting user inputs and displaying in Swing UI
	public static void main(String[] args) {

		// for testing getting the inputs through Console (can be generated using UI in
		// 5b)
		int rows, cols, ruleNumber, generationLimit, middleCell, initAliveCell, sleepTime;

		MAFrame maFrameGen; // MAFrames for setting up the automata
		MAFrameSet maFrameGenSet; // MAFrameSet which holds a Map of MAFrames

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
			System.out.println("Please enter rows you would like to see in the frame : ");
			rows = scnObj.nextInt();
			System.out.println("Please enter columns you would like to see in the frame : ");
			cols = scnObj.nextInt();
			System.out.println("Please enter your rule choice : \nEnter 1 for " + RuleNames.DEADALIVE
					+ "\nEnter 2 for " + RuleNames.BRIANSBRAIN + "\nEnter 3 for " + RuleNames.TOPDOWNTREE
					+ "\nEnter 4 for " + RuleNames.GOLDWINNER
					+ "\nEnter 5 for " + RuleNames.MAZERUNNER);
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
				maFrameGen = new MAFrame(RuleNames.DEADALIVE, rows, cols, initAliveCell);
			} else if (ruleNumber == 2) {
				maFrameGen = new MAFrame(RuleNames.BRIANSBRAIN, rows, cols, initAliveCell);
			} else if (ruleNumber == 3) {
				maFrameGen = new MAFrame(RuleNames.TOPDOWNTREE, rows, cols, Math.round(cols / 2));
			} else if (ruleNumber == 4) {
				maFrameGen = new MAFrame(RuleNames.GOLDWINNER, rows, cols, cols*rows-Math.round(cols/2));
			}else {
				maFrameGen = new MAFrame(RuleNames.MAZERUNNER, rows, cols, 0);
			}

			// Creating the Initial FrameSet with Initial Frame.
			maFrameGenSet = new MAFrameSet(maFrameGen, generationLimit, sleepTime);
			JFrame UIframe = new JFrame("Mobile Automata");

			// Sample UI just to show a valid 2D Automata
			UIframe.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			UIframe.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.out.println("Window closed. Simulation is stopped...");
					System.exit(0);
				}
			});

			// Adding the FrameSet to the view.
			MAutomataDriver ma = new MAutomataDriver(maFrameGenSet);
			UIframe.getContentPane().add(ma, BorderLayout.CENTER);
			UIframe.setVisible(true);

		} catch (Exception e) {
			System.out.println("An exception occurred while creating UI. Details : " + e.toString());
			System.exit(0);
		}
	}

}
