/**
 * 
 */
package edu.neu.csye6200.ma;

/**
 * @author RaviKumar
 *
 */

enum MACellState {
	DEAD, ALIVE, DYING;
}

public class MACell {

	private MACellState cellState;
	protected MAFrame frame;
	private int cellXPos;
	private int cellYPos;
	private static int check = 0;
	

	// Initially the cell is in default state. (White square // for testing)
	public MACell(int xPos, int yPos, MAFrame frame) {
		this.cellXPos = xPos;
		this.cellYPos = yPos;
		this.frame = frame;
		if(check == 0|| check == 1 || check == 2)
		cellState = MACellState.ALIVE;
		else
		cellState = MACellState.DEAD;
		check++;
	//	System.out.println(check);
	}
	
	public MACell() {
		if(check == 0|| check == 1)
			cellState = MACellState.ALIVE;
			else
			cellState = MACellState.DEAD;
			check++;
	}

	/*
	 *  Implementation is provided by extending classes (Rules)
	 */
	protected MACellState getNextCellState() {
		return getCellState();
	}

	/*
	 * Sets the cell's current state and returns true if the new state is different
	 * from the current state.
	 */
	protected void setState(MACellState state) {
	if (!cellState.equals(state)) cellState = state;
	}

	// Returns a count of the current cell's alive neighbors.
//	public int getAliveNeighborsCount() {
//		int neighbors = 0;
//		for (int row = Math.max(0, cellXPos - 1); row <= Math.min(frame.numRows() - 1, cellXPos + 1); row++) {
//			for (int col = Math.max(0, cellYPos - 1); col <= Math.min(frame.numColumns() - 1, cellYPos + 1); col++) {
//				if (row != cellXPos || col != cellYPos) { // Excluding the current cell
//					if (frame.getCellAt(row, col).getState().getCellStatus() == 2) {
//						neighbors++;
//					}
//				}
//			}
//		}
//
//		return neighbors;
//	}

	protected int getNeighborsCount(MACellState state) {
		
		int desiredNeighbors = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (this.getCellXPos() + i >= 0 && this.getCellXPos() + i < 3 && this.getCellYPos() + j >= 0 && this.getCellYPos() + j < 3) {
					if (frame.getCellAt(this.getCellXPos() + i, this.getCellYPos() + j).getCellState().compareTo(state)==0) {
						if(this.getCellXPos() != i || this.getCellYPos() !=j)
						desiredNeighbors++;
					}
				}
			}
		}
		return desiredNeighbors;
	}

	protected int getDeadNeighborsCount(int x, int y, MAFrame previousFrame) {
		int deadNeighbors = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (x + i >= 0 && x + i < frame.getFrameWidth() && y + j >= 0 && y + j < frame.getFrameHeight()) {
					if (previousFrame.getCellAt(x + i, y + j).getCellState().equals(MACellState.DEAD)
							&& !(i == 0 && j == 0)) {
						deadNeighbors++;
					}
				}
			}
		}
		return deadNeighbors;
	}

	protected int getDyingNeighborsCount(int x, int y, MAFrame previousFrame) {
		int dyingNeighbors = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (x + i >= 0 && x + i < frame.getFrameWidth() && y + j >= 0 && y + j < frame.getFrameHeight()) {
					if (previousFrame.getCellAt(x + i, y + j).getCellState().equals(MACellState.DYING)
							&& !(i == 0 && j == 0)) {
						dyingNeighbors++;
					}
				}
			}
		}
		return dyingNeighbors;
	}

	/**
	 * @return the cellState
	 */
	public MACellState getCellState() {
		return cellState;
	}

	/**
	 * @param cellState the cellState to set
	 */
	public void setCellState(MACellState cellState) {
		this.cellState = cellState;
	}
	/**
	 * @return the frame
	 */
	public MAFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(MAFrame frame) {
		this.frame = frame;
	}

	/**
	 * @return the cellXPos
	 */
	public int getCellXPos() {
		return cellXPos;
	}

	/**
	 * @param cellXPos the cellXPos to set
	 */
	public void setCellXPos(int cellXPos) {
		this.cellXPos = cellXPos;
	}

	/**
	 * @return the cellYPos
	 */
	public int getCellYPos() {
		return cellYPos;
	}

	/**
	 * @param cellYPos the cellYPos to set
	 */
	public void setCellYPos(int cellYPos) {
		this.cellYPos = cellYPos;
	}

}