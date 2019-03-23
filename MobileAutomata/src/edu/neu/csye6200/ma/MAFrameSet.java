/**
 * 
 */
package edu.neu.csye6200.ma;

import java.util.ArrayList;
import java.util.Map;
//import java.util.TreeMap;

/**
 * @author RaviKumar
 *
 */
public class MAFrameSet implements Runnable {

	private Map<Integer, MAFrame> maFrameRecord;
	private int generationCount = 0;
	private int genLimit = 3; // Maximum limit on generations at present
	private boolean completeFlag = false;
	private boolean pauseFlag = false;
	private MAFrame previousFrame = null;
	private ArrayList<MAFrame> lstMaFrames;

	public MAFrameSet(MAFrame maframe) {
		lstMaFrames = new ArrayList<MAFrame>();
		lstMaFrames.add(maframe);
		previousFrame = maframe;
	}

	@Override
	public void run() {
		System.out.println("Entered Run...");

		while (generationCount != genLimit) { // Loop until we are done with simulation generation

			if (previousFrame == null) {
				pauseFlag = true; // If we have not initialized a generation then pause and wait till we create
			}

			if (!pauseFlag) {
				MAFrame nextFrame;
				try {
					nextFrame = previousFrame.createNextFrame();
					lstMaFrames.add(nextFrame);
					previousFrame = nextFrame;
					generationCount++;

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

	public void addFrameToMap(int currentGen, MAFrame currentFrame) {
		maFrameRecord.put(generationCount, currentFrame);
	}

	public void removeFrameFromMap(int generation) {
		maFrameRecord.remove(generation);
	}

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

	/**
	 * @return the lstMaFrames
	 */
	public ArrayList<MAFrame> getLstMaFrames() {
		return lstMaFrames;
	}

	/**
	 * @param lstMaFrames the lstMaFrames to set
	 */
	public void setLstMaFrames(ArrayList<MAFrame> lstMaFrames) {
		this.lstMaFrames = lstMaFrames;
	}

}
