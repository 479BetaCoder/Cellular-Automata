package edu.neu.csye6200.ma;


import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.swing.JPanel;

/**
 * @author RaviKumar ClassName : MARegionSet Description : Stores MARegions in a
 *         Map which are generated using rule provided by the user. Also helps
 *         in setting up Regions in the UI. Valuable Output : maRegionRecord
 *         which contains all the Regions generated to produce a valid 2D Mobile
 *         Automata.
 */

public class MARegionSet extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private Map<Integer, MARegion> maRegionRecord; // To store all the Regions. Will be helpful in retrieving data back
	private int generationCount; // To keep track of region generations.
	private int genLimit; // User defined limit until which the Regions are generated and stored in the
	// MAP.
	private int sleepTime; // User defined sleep Time between generations.
	private boolean completeFlag; // Indicator to alert if the automata is complete. Using which we can show some
	// custom message to the user.
	private MARegion currentRegion; // Current region in the RegionSet
	private boolean paused; // Helps in determining if the generation is paused
	private boolean stopped;
	private boolean rewind; // Helps in determining if rewind is called
	private Thread cellTh; // Thread which executes once the user Starts the Simulation
	

	// For Logging application process to the console.
	private static Logger log = Logger.getLogger(MAutomataDriver.class.getName());

	
	public MARegionSet(MARegion maRegion, int genLimit, int sleepTime) {

		// Initializing the properties of a RegionSet
		initializeRegionSet();
		this.currentRegion = maRegion;
		this.genLimit = genLimit;
		this.sleepTime = sleepTime;
		
		// Adding the Initial Region to the Map.
		addRegionToMap(generationCount, currentRegion);
		log.info("MARegion Set created successfully...");

	}



	/*
	 * Routine to initialize all the properties of a Region Set. Must be called
	 * before using the properties.
	 */
	private void initializeRegionSet() {

		/*
		 * Using Tree Map as we can allow user to retrieve any region based on
		 * generation and not just the previous generation. Also the search using Tree
		 * Map is faster.
		 */

		this.maRegionRecord = new TreeMap<Integer, MARegion>();
		this.generationCount = 0;
		this.completeFlag = false;
		setOpaque(false); // This is necessary to make painting work
	}

	/*
	 * Function which gets triggered when the thread is executed to process the
	 * Simulation.
	 */

	@Override
	public void run() {
		
		try {
			
			while (!paused && generationCount != genLimit && !completeFlag && !stopped) {

				MARegion nextRegion;
				
				if (rewind && generationCount > 0) {
					currentRegion = getMaRegionRecord().get(generationCount - 1);
					removeRegionFromMap(generationCount);
					generationCount--;
					repaint(); // Paints the new state of the region using paintComponent.

				} else if (rewind && generationCount == 0) {
					paused = true;

				} else {
					nextRegion = currentRegion.createNextRegion();
					generationCount++;
					addRegionToMap(generationCount, nextRegion); // Once done, the region is added to the MAP
					currentRegion = nextRegion;
					repaint(); // Paints the new state of the region using paintComponent.
				}
				MAutomataDriver.genCount.setText(generationCount + "");
				simulationCheck();
				try {
					Thread.sleep(this.sleepTime);
				} catch (InterruptedException e) {
					log.severe("The thread execution was interrupted. Details : " + e.toString());
					break;
				}
			}if(stopped) {
				stopped = false;
				}else if (generationCount < genLimit && paused) {
				if (rewind && generationCount == 0) {
					rewind = false;
					 MAutomataDriver.lblStatus.setText("Simulation paused as user went back to the initial state...");
					log.info("Simulation paused as user went back to the initial state...");
					MAutomataDriver.pauseButton.setEnabled(false);
					MAutomataDriver.startButton.setEnabled(true);

				} else if (rewind) {
					 MAutomataDriver.lblStatus.setText("Simulation paused while user was rewinding...");
					log.info("Simulation paused while user was rewinding...");
					MAutomataDriver.startButton.setEnabled(true);
					MAutomataDriver.rewindButton.setEnabled(true);
				} else {
					 MAutomataDriver.lblStatus.setText("Simulation Paused...");
					log.info("Simulation Paused...");
				}

			} else if (completeFlag) {
				
				 MAutomataDriver.lblStatus.setText("Simulation completed Successfully...");
				log.info("Simulation completed Successfully...");
				
				MAutomataDriver.pauseButton.setEnabled(false);
				MAutomataDriver.startButton.setEnabled(false);
				
			}else if(generationCount == genLimit) {
				 	MAutomataDriver.lblStatus.setText("Simulation reached maximum generation Limit...");
					log.info("Simulation reached maximum generation Limit...");
					
					MAutomataDriver.pauseButton.setEnabled(false);
					MAutomataDriver.startButton.setEnabled(false);
			}
		} catch (Exception e) {
			log.severe("OOPS!! Some issue occured while simulation was in progress. Details : " + e.toString());
		}

	}

	// check if the simulation is completed even before generation Limit
	private void simulationCheck() {

		// Checking if the Maze is solved
		if (currentRegion.getRuleName().compareTo(RuleNames.MAZERUNNER) == 0) {
			if (currentRegion.getCellAt(currentRegion.getRegionRows() - 1, currentRegion.getRegionColumns() - 1)
					.getCellState().compareTo(MACellState.ALIVE) == 0)
				generationCount = genLimit;
			// Checking if the UNZIPPING is finished
		} else if (currentRegion.getRuleName().compareTo(RuleNames.GOLDWINNER) == 0) {
			for (int col = 0; col < currentRegion.getRegionColumns(); col++) {
				if (currentRegion.getCellAt(0, col).getCellState().compareTo(MACellState.DEAD) == 0) {
					currentRegion.setZipDir(false);
					break;
				} else {
					currentRegion.setZipDir(true);
				}
			}

			// Checking for GOLDWINNER Simulation is done
			int testGen = 0; // To stop once we get the gold
			for (int row = 0; row < currentRegion.getRegionRows(); row++) {

				if (currentRegion.getCellAt(row, 1).getCellState().compareTo(MACellState.DEAD) == 0) {
					testGen++;

				}
				if (testGen == currentRegion.getRegionRows() - 1) {
					completeFlag = true;
					break;
				}

			}
			// Checking if the TOPDOWNTREE simulation is done even before the Generation
			// Count
		} else if (currentRegion.getRuleName().compareTo(RuleNames.TOPDOWNTREE) == 0) {
			for (int col = 0; col < currentRegion.getRegionColumns(); col++) {
				if (currentRegion.getCellAt(currentRegion.getRegionRows() - 1, col).getCellState()
						.compareTo(MACellState.ALIVE) == 0) {
					completeFlag = true;
					break;
				}
			}
			// Checking if the DEADALIVE simulation is done even before the Generation Count
		} else if (currentRegion.getRuleName().compareTo(RuleNames.DEADALIVE) == 0) {
			if (currentRegion.getCellAt(currentRegion.getRegionRows() - 1, currentRegion.getRegionColumns() - 1)
					.getCellState().compareTo(MACellState.ALIVE) == 0) {

				completeFlag = true;

			}

		} // Checking if BriansBrain is solved
		else {
			int bSim = 0;
			for (int row = 0; row < currentRegion.getRegionRows(); row++) {
				for (int col = 0; col < currentRegion.getRegionColumns(); col++) {
					if (currentRegion.getCellAt(row, col).getCellState() == MACellState.DEAD) {
						bSim++;
					}
				}
			}
			if (bSim == currentRegion.getRegionRows() * currentRegion.getRegionColumns()) {
				completeFlag = true;

			}
		}

	}

	// Start point for generation next Regions. Called by MAutomataDriver.
	public void nextRegion() {
		cellTh = new Thread(this, "automataThread"); // Starts a new Thread
		this.paused = false;
		this.rewind = false;
		cellTh.start(); // Calls run method of the thread
	}

	// Start point for generation next Regions. Called by MAutomataDriver.
	public void rewindRegion() {
		cellTh = new Thread(this, "automataThread"); // Starts a new Thread
		this.paused = false;
		this.rewind = true;
		cellTh.start(); // Calls run method of the thread
	}

	// Routine to update the colors or paint the state of the cell.
	public void paintComponent(Graphics g) {

		try {
			int squarewidth = getSquareWidth();
			int squareheight = getSquareHeight();

			int hoffset = getHorizontalOffset();
			int voffset = getVerticalOffset();

			// MAZE RUNNER GRAPHICS
			if (currentRegion.getRuleName().compareTo(RuleNames.MAZERUNNER) == 0) {
				for (int row = 0; row < currentRegion.getRegionRows(); row++) {
					for (int col = 0; col < currentRegion.getRegionColumns(); col++) {
						if (currentRegion.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
							g.setColor(Color.BLACK);
						} else if (currentRegion.getCellAt(row, col).getCellState() == MACellState.DEAD) {
							g.setColor(Color.WHITE);
						} else {
							g.setColor(Color.BLUE);
						}

						g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
								squareheight - 1);

					}

				}
				// TOPDOWNTREE GRAPHICS (After completion ALIVE Cells are BLACK, DEAD are GOLD
				// and DYING are BLUE
			} else if (completeFlag && currentRegion.getRuleName().compareTo(RuleNames.TOPDOWNTREE) == 0) {
				for (int row = 0; row < currentRegion.getRegionRows(); row++) {
					for (int col = 0; col < currentRegion.getRegionColumns(); col++) {
						if (currentRegion.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
							g.setColor(Color.BLACK);
						} else if (currentRegion.getCellAt(row, col).getCellState() == MACellState.DEAD) {
							Color newColor = new Color(255, 215, 0); // For Gold Color
							g.setColor(newColor);
						} else {
							g.setColor(Color.BLUE);
						}
						// Shape changes from rectangle to oval
						g.fillOval(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
								squareheight - 1);
					}

				} // GOLDWINNER GRAPHICS Initial
			} else if (currentRegion.getRuleName().compareTo(RuleNames.GOLDWINNER) == 0 && currentRegion.isZipDir()) {
				for (int row = 0; row < currentRegion.getRegionRows(); row++) {
					for (int col = 0; col < currentRegion.getRegionColumns(); col++) {
						if (currentRegion.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
							g.setColor(Color.BLACK);
						} else if (currentRegion.getCellAt(row, col).getCellState() == MACellState.DEAD) {
							g.setColor(Color.WHITE);
						} else {
							g.setColor(Color.BLUE);
						}

						g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
								squareheight - 1);
					}

				} // GOLDWINNER GRAPHICS when un-zipped opens ( DEAD changes its color from WHITE
					// to GOLD
			} else if (currentRegion.getRuleName().compareTo(RuleNames.GOLDWINNER) == 0 && !currentRegion.isZipDir()) {
				for (int row = 0; row < currentRegion.getRegionRows(); row++) {
					for (int col = 0; col < currentRegion.getRegionColumns(); col++) {
						if (currentRegion.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
							g.setColor(Color.BLACK);
						} else if (currentRegion.getCellAt(row, col).getCellState() == MACellState.DEAD) {
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
				for (int row = 0; row < currentRegion.getRegionRows(); row++) {
					for (int col = 0; col < currentRegion.getRegionColumns(); col++) {
						if (currentRegion.getCellAt(row, col).getCellState() == MACellState.ALIVE) {
							g.setColor(Color.GREEN);
						} else if (currentRegion.getCellAt(row, col).getCellState() == MACellState.DEAD) {
							g.setColor(Color.WHITE);
						} else {
							g.setColor(Color.BLUE);
						}

						g.fillRect(hoffset + col * squarewidth, voffset + row * squareheight, squarewidth - 1,
								squareheight - 1);
						
					}

				}
			}
		} catch (Exception e) {
			log.severe("Whoa!! Some exception occurred while setting up graphics. Details : " + e.toString());
		}

	}

	// Pausing the thread when User insists. Called by MAutomataDriver
	public void pauseThread() {
		paused = true;
	}

	// Going to the previous generation. Called by MAutomataDriver
	public void rewindThread() {
		rewind = true;
	}
	
	// Terminating the simulation. Called by MAutomataDriver
		public void stopThread() {
			stopped = true;
		}

	// Helper methods for calculating the dimensions and helping in filling the
	// rectangle
	private int getSquareWidth() {
		return getSize().width / currentRegion.getRegionColumns();
	}

	private int getSquareHeight() {
		return getSize().height / currentRegion.getRegionRows();
	}

	private int getHorizontalOffset() {
		return (getSize().width - (getSquareWidth() * currentRegion.getRegionColumns())) / 2;
	}

	private int getVerticalOffset() {
		return (getSize().height - (getSquareHeight() * currentRegion.getRegionRows())) / 2;
	}

	// Helper Method to add Regions to the Map
	public void addRegionToMap(int currentGen, MARegion currentRegion) {
		maRegionRecord.put(currentGen, currentRegion);
	}

	// Helper Method to add Regions to the Map
	public void removeRegionFromMap(int currentGen) {
		maRegionRecord.remove(currentGen);
	}

	
	// Getters and Setters Section

	/**
	 * @return the maRegionRecord
	 */
	public Map<Integer, MARegion> getMaRegionRecord() {
		return maRegionRecord;
	}

	/**
	 * @param maRegionRecord the maRegionRecord to set
	 */
	public void setMaRegionRecord(Map<Integer, MARegion> maRegionRecord) {
		this.maRegionRecord = maRegionRecord;
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
	 * @return the comboGenLimit
	 */
	public int getGenLimit() {
		return genLimit;
	}

	/**
	 * @param comboGenLimit the comboGenLimit to set
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
	 * @return the current Region
	 */
	public MARegion getCurrentRegion() {
		return currentRegion;
	}

	/**
	 * @param currentRegion the currentRegion to set
	 */
	public void setCurrentRegion(MARegion currentRegion) {
		this.currentRegion = currentRegion;
	}

	/**
	 * @return the paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param paused the paused to set
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * @return the stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * @param stopped the stopped to set
	 */
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	/**
	 * @return the rewind
	 */
	public boolean isRewind() {
		return rewind;
	}

	/**
	 * @param rewind the rewind to set
	 */
	public void setRewind(boolean rewind) {
		this.rewind = rewind;
	}

}
