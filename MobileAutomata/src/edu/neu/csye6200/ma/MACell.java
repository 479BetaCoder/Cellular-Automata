/**
 * 
 */
package edu.neu.csye6200.ma;

import java.util.Random;
import java.util.logging.Logger;

/**
 * @author RaviKumar ClassName : MACell Description : Contains MACellState in
 *         each MACell and determines Neighbor Cell Count for the desired state.
 *         Valuable Outcome: Gets the Neighbor Count based on the MACellState
 *         the Rule is looking for and forms as a base class for MARule.
 */

/*
 * ENUM MACellState. Standardized the cell state as other primitive types can
 * lead to poor maintainability as the code grows. Advantage : Can introduce new
 * state when required without much code modification.
 */

enum MACellState {
	ALIVE, DEAD, DYING;
}

public abstract class MACell implements IMARule {

	private MACellState cellState; // Stores the MACellState for an MACell Object.
	protected MARegion region; // Determines the region to which the cell belongs.
	private int cellXPos; // Stores the cell's Row position.
	private int cellYPos; // Stores the cell's Column position.
	protected static int cellCount = 0; // useful in initializing cell states
	
	// For Logging application process to the console.
	private static Logger log = Logger.getLogger(MACell.class.getName());
	

	public MACell() {
		
	}

	// initializing cell states from MARule called by MARegion
	public MACell(MARegion region, MACellState cellState) {

		this.region = region;
		
			if (region.getRuleName().compareTo(RuleNames.TOPDOWNTREE) == 0) {
			if (cellCount == region.getInitialAliveCell())
				this.cellState = MACellState.ALIVE;
			else
				this.cellState = cellState;
		}else if (region.getRuleName().compareTo(RuleNames.GOLDWINNER) == 0) {
			if (cellCount == region.getInitialAliveCell())
				this.cellState = MACellState.DYING;
			else
				this.cellState = MACellState.ALIVE;
		} else if (region.getRuleName().compareTo(RuleNames.LOCKME) == 0) {
			if (cellCount != 0) {
				this.cellState = MACellState.values()[new Random().nextInt(3)];
				if (this.cellState.compareTo(MACellState.ALIVE) == 0) {
					this.cellState = MACellState.DEAD;
				}
			} else
				this.cellState = MACellState.ALIVE;
		}else {
			if (cellCount == region.getInitialAliveCell() || cellCount == region.getInitialAliveCell() - 1)
				this.cellState = MACellState.ALIVE;
			else
				this.cellState = cellState;
		}
		cellCount++;

	}

	/*
	 * Implementation is provided by extending classes (Rules)
	 */
	 public abstract MACellState getNextCellState();
	 
	 public abstract int[] getNextCellPos();
		
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
					if (this.getCellXPos() + i >= 0 && this.getCellXPos() + i < getRegion().getRegionRows()
							&& this.getCellYPos() + j >= 0 && this.getCellYPos() + j < getRegion().getRegionColumns()) {
						if (getRegion().getCellAt(this.getCellXPos() + i, this.getCellYPos() + j).getCellState()
								.compareTo(state) == 0) {
							if (!(i == 0 && j == 0)) // should not consider the current cell as neighbor
								desiredNeighbors++;
						}
					}
				}
			}

		} catch (Exception e) {
			log.severe("Exception occured while getting Neighbor Count : " + e.toString());
			desiredNeighbors = 0;
		}
		return desiredNeighbors;
	}

	// For TOPDOWNTREE
	protected int getTDNeighborsCount(MACellState state) {

		int desiredNeighbors = 0;
		try {
			for (int j = -1; j < 2; j++) {
				if (this.getCellXPos() - 1 >= 0 && this.getCellXPos() - 1 < getRegion().getRegionRows()
						&& this.getCellYPos() + j >= 0 && this.getCellYPos() + j < getRegion().getRegionColumns()) {
					if (getRegion().getCellAt(this.getCellXPos() - 1, this.getCellYPos() + j).getCellState()
							.compareTo(state) == 0) {
						if (j != 0)
							desiredNeighbors++;
					}
				}
			}

		} catch (Exception e) {
			log.severe("Exception occured while getting Neighbor Count : " + e.toString());
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
	 * @return the region
	 */
	public MARegion getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(MARegion region) {
		this.region = region;
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