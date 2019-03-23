/**
 * 
 */
package edu.neu.csye6200.ma;

/**
 * @author RaviKumar
 *
 */
public class MAFrame {

	private MARule ma_rule;
	protected MACell[][] arrCells;
	private int frameWidth;
	private int frameHeight;

	public MAFrame(MARule ma_rule, int frameWidth, int frameHeight) {
		
		this.ma_rule = ma_rule;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.arrCells = new MACell[frameWidth][frameHeight];

		for (int x = 0; x < frameWidth; x++)
			for (int y = 0; y < frameHeight; y++)
				this.arrCells[x][y] = new MACell(x, y, this);

		

	}

	public MAFrame() {
		
	}

	/**
	 * existing MAFrame copy to be used by MARule to generate next generation
	 *
	 * @param MAFrameCopy
	 */
	public MAFrame(MAFrame previousFrame) {
		ma_rule = previousFrame.ma_rule; // Copying the same rule used by the instance
		frameWidth = previousFrame.frameWidth;
		frameHeight = previousFrame.frameHeight;
		arrCells = new MACell[frameWidth][frameHeight];

		for (int x = 0; x < frameWidth; x++)
			for (int y = 0; y < frameHeight; y++)
				arrCells[x][y] = new MACell(x, y, this);

	}

	/*
	 * Returns Cell at the specified row and column. If frame location is not valid,
	 * a dead cell will be returned.
	 */
	public MACell getCellAt(int row, int col) {
		if ((row < 0) || (row > getFrameWidth())) {
			throw new RuntimeException("Not a valid row to getCellAt: " + row);
		}
		if ((col < 0) || (col > getFrameHeight())) {
			throw new RuntimeException("Not a valid column to getCellAt: " + col);
		}
		return arrCells[row][col];
	}

	/*
	 * Next Cell States are calculated looping through each cell in the frame, then
	 * getting the live/dead/dying neighbors and based on the rule, the state is
	 * decided.
	 */

	public MACellState[][] nextCellStates() {
		MACellState[][] nextStates = new MACellState[getFrameWidth()][getFrameHeight()];

		for (int i = 0; i < getFrameWidth(); i++) {
			for (int j = 0; j < getFrameHeight(); j++) {
				nextStates[i][j] = getCellAt(i, j).getNextCellState();

			}
		}

		return nextStates;
	}

	/**
	 * Creating new generation using from old generation through rules
	 * 
	 * @throws CloneNotSupportedException
	 */
	public MAFrame createNextFrame() {
		MAFrame newFrame = new MAFrame(this);
		MACellState[][] newCellStates = nextCellStates();
		for (int i = 0; i < getFrameWidth(); i++) {
			for (int j = 0; j < getFrameHeight(); j++) {
				newFrame.getCellAt(i, j).setState(newCellStates[i][j]);
			}
		}

		return newFrame;
	}

	/**
	 * @return the ma_rule
	 */
	public MARule getMa_rule() {
		return ma_rule;
	}

	/**
	 * @param ma_rule the ma_rule to set
	 */
	public void setMa_rule(MARule ma_rule) {
		this.ma_rule = ma_rule;
	}



	/**
	 * @return the frameWidth
	 */
	public int getFrameWidth() {
		return frameWidth;
	}

	/**
	 * @param frameWidth the frameWidth to set
	 */
	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	/**
	 * @return the frameHeight
	 */
	public int getFrameHeight() {
		return frameHeight;
	}

	/**
	 * @param frameHeight the frameHeight to set
	 */
	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

}
