package edu.neu.csye6200.ma;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author RaviKumar
 * ClassName : MAFrameSet
 * Description : Stores MAFrames in a Map which are generated using rule provided by the user. 
 * Valuable Output : maFrameRecord which contains all the frames generated to produce a valid 2D Mobile Automata.
 */

public class MAFrameSet implements Runnable {

	private Map<Integer, MAFrame> maFrameRecord;
	private int generationCount; //To keep track of frame generations.
	private int genLimit; // User defined limit until which the frames are generated and stored in the MAP.
	private boolean completeFlag; // Indicator to alert if the automata is complete. Using which we can show some custom message to the user.
	private boolean pauseFlag; // Indicator to allow user pause a automata in progress to examine its validity.
	private MAFrame previousFrame;

	public MAFrameSet(MAFrame maframe,int genLimit) {

		// Initializing the properties of a frameSet
		initializeFrameSet();
		this.genLimit = genLimit;
		// Adding the Initial Frame to the Map.
		addFrameToMap(generationCount, maframe);

		// Initializing previousFrame with the current frame to create a next frame
		// based on the rules.
		previousFrame = maframe;
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
		this.pauseFlag = false; 
		this.previousFrame = null; 
	}

	/*
	 * Function which gets triggered by user action and runs to create next frames
	 * to drive the Mobile Automata
	 */

	@Override
	public void run() {
		System.out.println("Entered Run...");

		while (generationCount != genLimit) { // Loop until we are done with simulation generation

			if (previousFrame == null) {
				pauseFlag = true; // If we have not initialized a generation then pause and wait till we create
			}

			if (!pauseFlag) { // We can use a pause Flag through JButton once swing UI is done.
				MAFrame nextFrame;
				try {
					nextFrame = previousFrame.createNextFrame(); // Using previousFrame object, a new frame is getting created. 
					addFrameToMap(generationCount++, nextFrame); // Once done, the frame is added to the MAP
					previousFrame = nextFrame; // Now the frame created becomes previous Frame for the next generation.
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

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
	 * @return the pauseFlag
	 */
	public boolean isPauseFlag() {
		return pauseFlag;
	}

	/**
	 * @param pauseFlag the pauseFlag to set
	 */
	public void setPauseFlag(boolean pauseFlag) {
		this.pauseFlag = pauseFlag;
	}

	/**
	 * @return the previousFrame
	 */
	public MAFrame getPreviousFrame() {
		return previousFrame;
	}

	/**
	 * @param previousFrame the previousFrame to set
	 */
	public void setPreviousFrame(MAFrame previousFrame) {
		this.previousFrame = previousFrame;
	}

}
