/**
 * 
 */
package edu.neu.csye6200.ma;

import java.util.logging.Logger;

/**
 * @author RaviKumar 
 * ClassName : MARegion 
 * Description : Holds 2D Array of MACell and contains methods to create next regions.
 * Valuable Output : Creates future MARegions by triggering methods in MARule using current MARegion.
 * 
 */
public class MARegion { 

	private RuleNames ruleName; // Holds RuleNames Enum passed by the user he wishes to see.
	protected MACell[][] arrCells; // Holds MACells which contain MACellState.
	private int regionRows; // Dimensions of the Region passed by the user.
	private int regionColumns; // Dimensions of the Region passed by the user.
	private int initialAliveCell = 0; // To start the region generation, initially we are making the center cells alive. (This can be modified once UI is done)
	private int cellDirection;
	private boolean zipDir; // for goldWinner direction
	
	// For Logging application process to the console.
		private static Logger log = Logger.getLogger(MAutomataDriver.class.getName());
	
	public MARegion(RuleNames rule, int regionRows, int regionColumns, int initialAliveCell) {
		
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
				MACell ma = new MARule(this.ruleName,this,MACellState.DEAD);
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
		if(previousRegion.cellDirection == 1) {
		cellDirection = -1;
		}else {
			cellDirection = 1;
		}
		regionInitialize();

	}

	/*
	 * Function which is responsible to create Next Region based on previous Regions
	 * MACellState
	 */
	public MARegion createNextRegion() {
		 // Creating a copy of the present region.
		MARegion newRegion = new MARegion(this);
		try {
		// Calling nextCellStates using previousRegion Object to determine current state.
		MACellState[][] newCellStates = nextCellStates();

		/*
		 * Looping through each cell to determine the neighbors state and deciding the
		 * cell's state based on the rules.
		 */
		for (int i = 0; i < getRegionRows(); i++) {
			for (int j = 0; j < getRegionColumns(); j++) {
				newRegion.getCellAt(i, j).setState(newCellStates[i][j]);
			}
		}
		}catch(Exception e) {
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
	

	// Getters and Setters

	/**
	 * @return the regionRows
	 */
	public int getRegionRows() {
		return regionRows;
	}

	/**
	 * @param regionRows the regionRows to set
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

}