/**
 * 
 */
package edu.neu.csye6200.ma;

/**
 * @author RaviKumar 
 * ClassName : MAFrame 
 * Description : Holds 2D Array of MACell and contains methods to create next frames.
 * Valuable Output : Creates future MAFrames by triggering methods in MARule using current MAFrame.
 * 
 */
public class MAFrame {

	private RuleNames ruleName; // Holds RuleNames Enum passed by the user he wishes to see.
	protected MACell[][] arrCells; // Holds MACells which contain MACellState.
	private int frameRows; // Dimensions of the Frame passed by the user.
	private int frameColumns; // Dimensions of the Frame passed by the user.
	private int initialAliveCell = 0; // To start the frame generation, initially we are making the center cells alive. (This can be modified once UI is done)

	public MAFrame(RuleNames rule, int frameRows, int frameColumns, int initialAliveCell) {
		
		this.ruleName = rule;
		this.frameRows = frameRows;
		this.frameColumns = frameColumns;
		this.arrCells = new MACell[frameRows][frameColumns];
		this.setInitialAliveCell(initialAliveCell);
		frameInitialize();

	}

	public MAFrame() {

	}

	/*
	 * Function to instantiate Cell Objects in a 2D array. Each cell reference is of
	 * type MACell but the object is of MARule which is a child of MACell, whose
	 * behaviour is dynamic based on the rule.
	 */
	private void frameInitialize() {
		for (int x = 0; x < frameRows; x++) {
			for (int y = 0; y < frameColumns; y++) {
				MACell ma = new MARule(this.ruleName,this,MACellState.DEAD);
				ma.setCellXPos(x);
				ma.setCellYPos(y);
				ma.setFrame(this);
				this.arrCells[x][y] = ma;
			}

		}
		
	}

	// Creating a MAFrame object using the values of the previous frame.
	public MAFrame(MAFrame previousFrame) {
		ruleName = previousFrame.ruleName; // Copying the same rule used by the instance
		frameRows = previousFrame.frameRows;
		frameColumns = previousFrame.frameColumns;
		arrCells = new MACell[frameRows][frameColumns];

		frameInitialize();

	}

	/*
	 * Function which is responsible to create Next Frame based on previous Frames
	 * MACellState
	 */
	public MAFrame createNextFrame() {
		 // Creating a copy of the present frame.
		MAFrame newFrame = new MAFrame(this);
		try {
		// Calling nextCellStates using previousFrame Object to determine current state.
		MACellState[][] newCellStates = nextCellStates();

		/*
		 * Looping through each cell to determine the neighbors state and deciding the
		 * cell's state based on the rules.
		 */
		for (int i = 0; i < getFrameRows(); i++) {
			for (int j = 0; j < getFrameColumns(); j++) {
				newFrame.getCellAt(i, j).setState(newCellStates[i][j]);
			}
		}
		}catch(Exception e) {
			System.out.println("Exception occured while creating next Frame : " + e.toString());
			
		}

		return newFrame;
	}

	// Helper Routine to get the cell at a specific location using the co-ordinates
	public MACell getCellAt(int row, int col) {
		if ((row < 0) || (row > getFrameRows())) {
			throw new RuntimeException("The referenced cell at " + row + "is not valid row in the current frame.");
		}
		if ((col < 0) || (col > getFrameColumns())) {
			throw new RuntimeException("The referenced cell at " + col + "is not valid column in the current frame.");
		}
		return arrCells[row][col];
	}

	/*
	 * Next Cell States are calculated looping through each cell in the frame, then
	 * getting the live/dead/dying neighbors and based on the rule, the state is
	 * decided.
	 */

	public MACellState[][] nextCellStates() {
		MACellState[][] nextStates = new MACellState[getFrameRows()][getFrameColumns()];

		for (int i = 0; i < getFrameRows(); i++) {
			for (int j = 0; j < getFrameColumns(); j++) {
				nextStates[i][j] = getCellAt(i, j).getNextCellState();

			}
		}

		return nextStates;
	}
	

	// Getters and Setters

	/**
	 * @return the frameRows
	 */
	public int getFrameRows() {
		return frameRows;
	}

	/**
	 * @param frameRows the frameRows to set
	 */
	public void setFrameRows(int frameWidth) {
		this.frameRows = frameWidth;
	}

	/**
	 * @return the frameColumns
	 */
	public int getFrameColumns() {
		return frameColumns;
	}

	/**
	 * @param frameColumns the frameColumns to set
	 */
	public void setFrameColumns(int frameHeight) {
		this.frameColumns = frameHeight;
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

}
