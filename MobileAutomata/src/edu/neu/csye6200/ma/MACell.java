/**
 * 
 */
package edu.neu.csye6200.ma;

/**
 * @author RaviKumar ClassName : MACell 
 * Description : Contains MACellState in each MACell and determines Neighbor Cell Count for the desired state.
 * Valuable Outcome: Gets the Neighbor Count based on the MACellState the Rule is looking for and forms as a base class for MARule.
 */

/*
 * ENUM MACellState. Standardized the cell state as other primitive types can
 * lead to poor maintainability as the code grows. Advantage : Can introduce new
 * state when required without much code modification.
 */

enum MACellState {
	DEAD, ALIVE, DYING;
}

public class MACell {

	private MACellState cellState; // Stores the MACellState for an MACell Object.
	protected MAFrame frame; // Determines the frame to which the cell belongs.
	private int cellXPos; // Stores the cell's Row position.
	private int cellYPos; // Stores the cell's Column position.
	private static int cellCount = 0;

	public MACell() {

	}

	// initializing cell states from MARule called by MAFrame
	public MACell(MAFrame frame, MACellState cellState) {

		this.frame = frame;
		
		if (cellCount == frame.getInitialAliveCell() || cellCount == frame.getInitialAliveCell()-1)
			this.cellState = MACellState.ALIVE;
		else
			this.cellState = cellState;

		cellCount++;

	}

	/*
	 * Implementation is provided by extending classes (Rules)
	 */
	protected MACellState getNextCellState() {
		return getCellState();
	}

	/*
	 * Sets the cell's current state and returns true if the new state is different
	 * from the current state.
	 */
	protected void setState(MACellState state) {
		if (!cellState.equals(state))
			cellState = state;
	}

	/*
	 * Function to calculate the number of neighbors with a particular state for the
	 * current cell. State to look after is provided as Input.
	 */

	protected int getNeighborsCount(MACellState state) {

		int desiredNeighbors = 0;
		try {

			for (int i = -1; i < 2; i++) {  
				for (int j = -1; j < 2; j++) {
					if (this.getCellXPos() + i >= 0 && this.getCellXPos() + i < getFrame().getFrameRows()
							&& this.getCellYPos() + j >= 0 && this.getCellYPos() + j < getFrame().getFrameColumns()) {
						if (getFrame().getCellAt(this.getCellXPos() + i, this.getCellYPos() + j).getCellState()
								.compareTo(state) == 0) {
							if (!(i == 0 && j == 0)) // should not consider the current cell as neighbor
								desiredNeighbors++;
						}
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Exception occured while getting Neighbor Count : " + e.toString());
			desiredNeighbors = 0;
		}
		return desiredNeighbors;
	}

	// Getters and Setters

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