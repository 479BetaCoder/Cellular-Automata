package edu.neu.csye6200.ma;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.logging.Logger;

/**
 * A Mobile Automata Abstract application class
 * 
 * @author RaviKumar
 *
 */
public abstract class MAApp implements ActionListener, WindowListener {
	protected JFrame frame = null;
	protected MenuManager menuMgr = null;

	// Used for logging MAApp events in console.
	private static Logger log = Logger.getLogger(MAApp.class.getName());

	/**
	 * Default Constructor called when MAAutomataDriver (main) object is
	 * instantiated.
	 */
	public MAApp() {
		initGUI(); // call to create JFrame for UI
	}

	/**
	 * Initialize the Graphical User Interface
	 */
	public void initGUI() {

		frame = new JFrame();
		frame.setTitle("Mobile Automata");

		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame.DISPOSE_ON_CLOSE)

		// Permit the app to hear about the window opening
		frame.addWindowListener(this);

		menuMgr = new MenuManager(this); // Call to MenuManager to instantiate the menu bar

		frame.setJMenuBar(menuMgr.getMenuBar()); // Add the menu bar to this application

		frame.setLayout(new BorderLayout());
		frame.add(getMainPanel(), BorderLayout.CENTER); // Adding the main Panel i.e., Simulation region to JFrame
	}

	/**
	 * Override this method to provide the main content panel.
	 * 
	 * @return a JPanel, which contains the main content of of your application
	 */
	public abstract JPanel getMainPanel();

	/**
	 * A convenience method that uses the Swing dispatch thread to show the UI. This
	 * prevents concurrency problems during component initialization.
	 */
	public void showUI() {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setVisible(true); // The UI is built, so display it;
			}
		});

	}

	/**
	 * Shut down the application
	 */
	public void exit() {
		log.info("Mobile Automata Simulation Closed... Bye !!");
		frame.dispose();
		System.exit(0);
	}

	/**
	 * Override this method to show a About Dialog
	 */
	public void showHelp() {
	}

}
