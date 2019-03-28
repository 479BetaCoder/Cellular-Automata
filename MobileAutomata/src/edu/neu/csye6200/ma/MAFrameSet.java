package edu.neu.csye6200.ma;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

/**
 * @author RaviKumar ClassName : MAFrameSet Description : Stores MAFrames in a
 *         Map which are generated using rule provided by the user. Also helps
 *         in setting up frames in the UI. Valuable Output : maFrameRecord which
 *         contains all the frames generated to produce a valid 2D Mobile
 *         Automata.
 */

public class MAFrameSet extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private Map<Integer, MAFrame> maFrameRecord; // To store all the frames. Will be helpful in retrieving data back
	private int generationCount; // To keep track of frame generations.
	private int genLimit; // User defined limit until which the frames are generated and stored in the
	// MAP.
	private int sleepTime; // User defined sleep Time between generations.
	private boolean completeFlag; // Indicator to alert if the automata is complete. Using which we can show some
	// custom message to the user.
	private MAFrame currentFrame; // Current frame in the frameSet
	private boolean paused; // Helps in determining if the generation is paused
	private Thread cellTh; // Thread which executes once the user Starts the Simulation

	public MAFrameSet(MAFrame maframe, int genLimit, int sleepTime) {

		// Initializing the properties of a frameSet
		initializeFrameSet();
		this.genLimit = genLimit;
		this.sleepTime = sleepTime;

		// Adding the Initial Frame to the Map.
		addFrameToMap(generationCount, maframe);

		// Initializing previousFrame with the current frame to create a next frame
		// based on the rules.
		currentFrame = maframe;
	}

	/*
	 * Routine to initialize all the properties of a Frame Set. Must be called
	 * before using the properties.
	 */
	private void initializeFrameSet() {

		/*
		 * Using Tree Map as we can allow user to retrieve any frame based on generation
		 * and not just the previous generation. Also the search using Tree Map is
		 * faster.
		 */

		this.maFrameRecord = new TreeMap<Integer, MAFrame>();
		this.generationCount = 1;
		this.completeFlag = false;
		this.currentFrame = null;
		setOpaque(false); // This is necessary to make painting work
	}

	/*
	 * Function which gets triggered when the thread is executed to process the
	 * Simulation.
	 */

	@Override
	public void run() {

		System.out.println("Simulation Started...");

		while (!paused && generationCount != genLimit) {

			MAFrame nextFrame;
			nextFrame = currentFrame.createNextFrame();
			addFrameToMap(generationCount++, nextFrame); // Once done, the frame is added to the MAP
			currentFrame = nextFrame;

			// Checking if the Maze is solved
			if (currentFrame.getRuleName().compareTo(RuleNames.MAZERUNNER) == 0) {
				if (currentFrame.getCellAt(currentFrame.getFrameRows() - 1, currentFrame.getFrameColumns() - 1)
						.getCellState().compareTo(MACellState.ALIVE) == 0)
					generationCount = genLimit;
			}

			repaint(); // Paints the new state of the frame using paintComponent.

			// Checking if the UNZIPPING is finished
			if (currentFrame.getRuleName().compareTo(RuleNames.GOLDWINNER) == 0) {
				for (int col = 0; col < currentFrame.getFrameColumns(); col++) {
					if (currentFrame.getCellAt(0, col).getCellState().compareTo(MACellState.DEAD) == 0) {
						currentFrame.setZipDir(false);
						break;
					} else {
						currentFrame.setZipDir(true);
					}
				}

				// Checking for GOLDWINNER Simulation is done
				int testGen = 0; // To stop once we get the gold
				for (int row = 0; row < currentFrame.getFrameRows(); row++) {

					if (currentFrame.getCellAt(row, 1).getCellState().compareTo(MACellState.DEAD) == 0) {
						testGen++;

					}
					if (testGen == currentFrame.getFrameRows() - 1) {
						generationCount = genLimit;
						break;
					}

				}
				// Checking if the TOPDOWNTREE simulation is done even before the Generation
				// Count
			} else if (currentFrame.getRuleName().compareTo(RuleNames.TOPDOWNTREE) == 0) {
				for (int col = 0; col < currentFrame.getFrameColumns(); col++) {
					if (currentFrame.getCellAt(currentFrame.getFrameRows() - 1, col).getCellState()
							.compareTo(MACellState.ALIVE) == 0) {
						generationCount = genLimit;
						break;
					}
				}
				// Checking if the DEADALIVE simulation is done even before the Generation Count
			} else if (currentFrame.getRuleName().compareTo(RuleNames.DEADALIVE) == 0) {
				if (currentFrame.getCellAt(currentFrame.getFrameRows() - 1, currentFrame.getFrameColumns() - 1)
						.getCellState().compareTo(MACellState.ALIVE) == 0) {

					generationCount = genLimit;
					break;
				}

			} // Checking if BriansBrain is solved
			else {
				int bSim = 0;
				for (int row = 0; row < currentFrame.getFrameRows(); row++) {
					for (int col = 0; col < currentFrame.getFrameColumns(); col++) {
						if (currentFrame.getCellAt(row, col).getCellState() == MACellState.DEAD) {
							bSim++;
						}
					}
				}
				if (bSim == currentFrame.getFrameRows() * currentFrame.getFrameColumns()) {
					generationCount = genLimit;
					break;
				}
			}

			try {
				Thread.sleep(this.sleepTime);
			} catch (InterruptedException e) {
				System.out.println("The thread execution was interrupted. Detail : " + e.toString()); // Console
				// messages for
				// notifying
				// user. Can be
				// shifted to UI
				// later.
				break;
			}
		}
		if (generationCount < genLimit) {
			System.out.println("Simulation Paused..."); // Console messages for notifying user. Can be shifted to UI
			// later.
		} else
			System.out.println("Simulation completed Successfully..."); // Console messages for notifying user. Can be
		// shifted to UI later.

	}

	// Start point for generation next Frames. Called by MAutomataDriver.
	public void nextFrame() {
		cellTh = new Thread(this, "automataThread"); // Starts a new Thread
		this.paused = false;
		cellTh.start(); // Calls run method of the thread
	}

	// Routine to update the colors or paint the state of the cell.
	public void paintComponent(Graphics g) {
		int squarewidth = getSquareWidth();
		int squareheight = getSquareHeight();

		int hoffset = getHorizontalOffset();
		int voffset = getVerticalOffset();

		if (generationCount == genLimit && currentFrame.getRuleName().compareTo(RuleNames.MAZERUNNER) == 0) {
			for (int row = 0; row < currentFrame.getFrameRows(); row++) {
				for (int col = 0; col < currentFrame.getFrameColumns(); col++) {
					if (currentFrame.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
						g.setColor(Color.BLACK);
					} else if (currentFrame.getCellAt(row, col).getCellState() == MACellState.DEAD) {
						g.setColor(Color.WHITE);
					} else {
						g.setColor(Color.BLUE);
					}
					g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
							squareheight - 1);
				}
			}
		} else if (currentFrame.getRuleName().compareTo(RuleNames.MAZERUNNER) == 0) {
			for (int row = 0; row < currentFrame.getFrameRows(); row++) {
				for (int col = 0; col < currentFrame.getFrameColumns(); col++) {
					if (currentFrame.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
						g.setColor(Color.BLACK);
					} else if (currentFrame.getCellAt(row, col).getCellState() == MACellState.DEAD) {
						g.setColor(Color.WHITE);
					} else {
						g.setColor(Color.BLUE);
					}

					g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
							squareheight - 1);

				}

			}

		} else if (generationCount == genLimit && currentFrame.getRuleName().compareTo(RuleNames.TOPDOWNTREE) == 0) {
			for (int row = 0; row < currentFrame.getFrameRows(); row++) {
				for (int col = 0; col < currentFrame.getFrameColumns(); col++) {
					if (currentFrame.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
						g.setColor(Color.BLACK);
					} else if (currentFrame.getCellAt(row, col).getCellState() == MACellState.DEAD) {
						Color newColor = new Color(255, 215, 0);
						g.setColor(newColor);
					} else {
						g.setColor(Color.BLUE);
					}

					g.fillOval(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
							squareheight - 1);
				}

			}
		} else if (currentFrame.getRuleName().compareTo(RuleNames.GOLDWINNER) == 0 && currentFrame.isZipDir()) {
			for (int row = 0; row < currentFrame.getFrameRows(); row++) {
				for (int col = 0; col < currentFrame.getFrameColumns(); col++) {
					if (currentFrame.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
						g.setColor(Color.BLACK);
					} else if (currentFrame.getCellAt(row, col).getCellState() == MACellState.DEAD) {
						g.setColor(Color.WHITE);
					} else {
						g.setColor(Color.BLUE);
					}

					g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
							squareheight - 1);
				}

			}
		} else if (currentFrame.getRuleName().compareTo(RuleNames.GOLDWINNER) == 0 && !currentFrame.isZipDir()) {
			for (int row = 0; row < currentFrame.getFrameRows(); row++) {
				for (int col = 0; col < currentFrame.getFrameColumns(); col++) {
					if (currentFrame.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
						g.setColor(Color.BLACK);
					} else if (currentFrame.getCellAt(row, col).getCellState() == MACellState.DEAD) {
						Color newColor = new Color(255, 215, 0);
						g.setColor(newColor);
					} else {
						g.setColor(Color.BLUE);
					}

					g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
							squareheight - 1);
				}

			}
		} else {
			for (int row = 0; row < currentFrame.getFrameRows(); row++) {
				for (int col = 0; col < currentFrame.getFrameColumns(); col++) {
					if (currentFrame.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
						g.setColor(Color.GREEN);
					} else if (currentFrame.getCellAt(row, col).getCellState() == MACellState.DEAD) {
						g.setColor(Color.WHITE);
					} else {
						g.setColor(Color.BLUE);
					}

					g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
							squareheight - 1);
				}

			}
		}

	}

	// Pausing the thread when User insists. Called by MAutomataDriver
	public void pauseThread() {
		paused = true;
	}

	// Helper methods for calculating the dimensions and helping in filling the
	// rectangle
	private int getSquareWidth() {
		return getSize().width / currentFrame.getFrameColumns();
	}

	private int getSquareHeight() {
		return getSize().height / currentFrame.getFrameRows();
	}

	private int getHorizontalOffset() {
		return (getSize().width - (getSquareWidth() * currentFrame.getFrameColumns())) / 2;
	}

	private int getVerticalOffset() {
		return (getSize().height - (getSquareHeight() * currentFrame.getFrameRows())) / 2;
	}

	// Helper Method to add Frames to the Map
	public void addFrameToMap(int currentGen, MAFrame currentFrame) {
		maFrameRecord.put(generationCount, currentFrame);
	}

	// Getters and Setters Section

	/**
	 * @return the maFrameRecord
	 */
	public Map<Integer, MAFrame> getMaFrameRecord() {
		return maFrameRecord;
	}

	/**
	 * @param maFrameRecord the maFrameRecord to set
	 */
	public void setMaFrameRecord(Map<Integer, MAFrame> maFrameRecord) {
		this.maFrameRecord = maFrameRecord;
	}

	/**
	 * @return the generationCount
	 */
	public int getGenerationCount() {
		return generationCount;
	}

	/**
	 * @param generationCount the generationCount to set
	 */
	public void setGenerationCount(int generationCount) {
		this.generationCount = generationCount;
	}

	/**
	 * @return the genLimit
	 */
	public int getGenLimit() {
		return genLimit;
	}

	/**
	 * @param genLimit the genLimit to set
	 */
	public void setGenLimit(int genLimit) {
		this.genLimit = genLimit;
	}

	/**
	 * @return the completeFlag
	 */
	public boolean isCompleteFlag() {
		return completeFlag;
	}

	/**
	 * @param completeFlag the completeFlag to set
	 */
	public void setCompleteFlag(boolean completeFlag) {
		this.completeFlag = completeFlag;
	}

	/**
	 * @return the previousFrame
	 */
	public MAFrame getCurrentFrame() {
		return currentFrame;
	}

	/**
	 * @param previousFrame the previousFrame to set
	 */
	public void setCurrentFrame(MAFrame currentFrame) {
		this.currentFrame = currentFrame;
	}

}
