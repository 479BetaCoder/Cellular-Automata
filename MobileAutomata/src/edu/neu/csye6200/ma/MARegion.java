/**
 * 
 */
package edu.neu.csye6200.ma;

import java.util.logging.Logger;

/**
 * @author RaviKumar ClassName : MARegion Description : Holds 2D Array of MACell
 *         and contains methods to create next regions. Valuable Output :
 *         Creates future MARegions by triggering methods in MARule using
 *         current MARegion.
 * 
 */
public class MARegion {

	private RuleNames ruleName; // Holds RuleNames Enum passed by the user he wishes to see.
	protected MACell[][] arrCells; // Holds MACells which contain MACellState.
	private int regionRows; // Dimensions of the Region passed by the user.
	private int regionColumns; // Dimensions of the Region passed by the user.
	private int initialAliveCell; // To start the region generation, initially we are making the center cells
									// alive.
	private int cellDirection;
	private boolean zipDir; // for goldWinner direction

	// used only for LOCKME (active cell tracking only for Mobile Automata)
	private int activeCellXPos = 0;
	private int activeCellYPos = 0;
	private boolean isLocked;

	// For Logging application process to the console.
	private static Logger log = Logger.getLogger(MAutomataDriver.class.getName());

	public MARegion(RuleNames rule, int regionRows, int regionColumns, int initialAliveCell) {

		this.activeCellXPos = 0;
		this.activeCellYPos = 0;
		this.isLocked = false;
		this.zipDir = true;
		this.cellDirection = 1;
		this.ruleName = rule;
		this.regionRows = regionRows;
		this.regionColumns = regionColumns;
		this.arrCells = new MACell[regionRows][regionColumns];
		this.setInitialAliveCell(initialAliveCell);
		regionInitialize();

	}

	/**
	 * @return the ruleName
	 */
	public RuleNames getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(RuleNames ruleName) {
		this.ruleName = ruleName;
	}

	public MARegion() {

	}

	/*
	 * Function to instantiate Cell Objects in a 2D array. Each cell reference is of
	 * type MACell but the object is of MARule which is a child of MACell, whose
	 * behavior is dynamic based on the rule.
	 */
	private void regionInitialize() {
		for (int x = 0; x < regionRows; x++) {
			for (int y = 0; y < regionColumns; y++) {
				MACell ma = new MARule(this.ruleName, this, MACellState.DEAD);
				ma.setCellXPos(x);
				ma.setCellYPos(y);
				ma.setRegion(this);
				this.arrCells[x][y] = ma;
			}

		}

	}

	// Creating a MARegion object using the values of the previous region.
	public MARegion(MARegion previousRegion) {
		ruleName = previousRegion.ruleName; // Copying the same rule used by the instance
		regionRows = previousRegion.regionRows;
		regionColumns = previousRegion.regionColumns;
		arrCells = new MACell[regionRows][regionColumns];
		isLocked = previousRegion.isLocked;
		// For Gold Winner Rule
		if (previousRegion.cellDirection == 1) {
			cellDirection = -1;
		} else {
			cellDirection = 1;
		}

		activeCellXPos = previousRegion.getActiveCellXPos();
		activeCellYPos = previousRegion.getActiveCellYPos();

		regionInitialize();

	}

	/*
	 * Function which is responsible to create Next Region based on previous Regions
	 * MACellState
	 */
	public MARegion createNextRegion() {
		// Creating a copy of the present region.
		MARegion newRegion = new MARegion(this);
		MACellState[][] newCellStates;

		try {

			// Only for mobile Automata for tracking the activeCell
			if (newRegion.ruleName.compareTo(RuleNames.LOCKME) == 0) {

				int[] newActivePos = nextActivePos();

				if (newActivePos[0] == -1 && newActivePos[1] == -1) {
					for (int i = 0; i < getRegionRows(); i++) {
						for (int j = 0; j < getRegionColumns(); j++) {

							newRegion.getCellAt(i, j).setState(this.getCellAt(i, j).getCellState());
							}
					}
					newRegion.setLocked(true);
				} else {

					newRegion.activeCellXPos = newActivePos[0];
					newRegion.activeCellYPos = newActivePos[1];

					for (int i = 0; i < getRegionRows(); i++) {
						for (int j = 0; j < getRegionColumns(); j++) {

							if (i == newActivePos[0] && j == newActivePos[1]) {
								newRegion.getCellAt(i, j).setState(MACellState.ALIVE);
							} else {
								newRegion.getCellAt(i, j).setState(this.getCellAt(i, j).getCellState());
							}

						}
					}
				}

			} else {
				// Calling nextCellStates using previousRegion Object to determine current
				// state.
				newCellStates = nextCellStates();

				/*
				 * Looping through each cell to determine the neighbors state and deciding the
				 * cell's state based on the comboRules.
				 */
				for (int i = 0; i < getRegionRows(); i++) {
					for (int j = 0; j < getRegionColumns(); j++) {
						newRegion.getCellAt(i, j).setState(newCellStates[i][j]);
					}
				}
			}
		} catch (Exception e) {
			log.severe("Exception occured while creating next Region : " + e.toString());

		}

		return newRegion;
	}

	// Helper Routine to get the cell at a specific location using the co-ordinates
	public MACell getCellAt(int row, int col) {
		if ((row < 0) || (row >= getRegionRows())) {
			throw new RuntimeException("The referenced cell at " + row + "is not valid row in the current region.");
		}
		if ((col < 0) || (col >= getRegionColumns())) {
			throw new RuntimeException("The referenced cell at " + col + "is not valid column in the current region.");
		}
		return arrCells[row][col];
	}

	/*
	 * Next Cell States are calculated looping through each cell in the region, then
	 * getting the live/dead/dying neighbors and based on the rule, the state is
	 * decided.
	 */

	public MACellState[][] nextCellStates() {

		MACellState[][] nextStates = new MACellState[getRegionRows()][getRegionColumns()];

		for (int i = 0; i < getRegionRows(); i++) {
			for (int j = 0; j < getRegionColumns(); j++) {
				nextStates[i][j] = getCellAt(i, j).getNextCellState();

			}
		}

		return nextStates;
	}

	// This is to determine the mobile cell state
	public int[] nextActivePos() {
		return getCellAt(getActiveCellXPos(), getActiveCellYPos()).getNextCellPos();
	}

	// Getters and Setters

	/**
	 * @return the comboRows
	 */
	public int getRegionRows() {
		return regionRows;
	}

	/**
	 * @param comboRows the comboRows to set
	 */
	public void setRegionRows(int regionWidth) {
		this.regionRows = regionWidth;
	}

	/**
	 * @return the regionColumns
	 */
	public int getRegionColumns() {
		return regionColumns;
	}

	/**
	 * @param regionColumns the regionColumns to set
	 */
	public void setRegionColumns(int regionHeight) {
		this.regionColumns = regionHeight;
	}

	/**
	 * @return the initialAliveCell
	 */
	public int getInitialAliveCell() {
		return initialAliveCell;
	}

	/**
	 * @param initialAliveCell the initialAliveCell to set
	 */
	public void setInitialAliveCell(int initialAliveCell) {
		this.initialAliveCell = initialAliveCell;
	}

	/**
	 * @return the cellDirection
	 */
	public int getCellDirection() {
		return cellDirection;
	}

	/**
	 * @param cellDirection the cellDirection to set
	 */
	public void setCellDirection(int cellDirection) {
		this.cellDirection = cellDirection;
	}

	/**
	 * @return the zipDir
	 */
	public boolean isZipDir() {
		return zipDir;
	}

	/**
	 * @param zipDir the zipDir to set
	 */
	public void setZipDir(boolean zipDir) {
		this.zipDir = zipDir;
	}

	/**
	 * @return the activeCellXPos
	 */
	public int getActiveCellXPos() {
		return activeCellXPos;
	}

	/**
	 * @param activeCellXPos the activeCellXPos to set
	 */
	public void setActiveCellXPos(int activeCellXPos) {
		this.activeCellXPos = activeCellXPos;
	}

	/**
	 * @return the activeCellYPos
	 */
	public int getActiveCellYPos() {
		return activeCellYPos;
	}

	/**
	 * @param activeCellYPos the activeCellYPos to set
	 */
	public void setActiveCellYPos(int activeCellYPos) {
		this.activeCellYPos = activeCellYPos;
	}

	/**
	 * @return the isLocked
	 */
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * @param isLocked the isLocked to set
	 */
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

}
