/**
 * 
 */
package edu.neu.csye6200.ma;

/**
 * @author RaviKumar ClassName : MARule Description : MARule determines the cell
 *         state based on the neighboring cell. Rule Name is specified by User.
 *         Valuable Outcome : MACellState of the next cell based on its
 *         neighbors and the rule which user wants to see.
 */

/*
 * ENUM RuleNames. Standardized the ruleName as other primitive types or Strings
 * can lead to poor maintainability as the code grows. Advantage : Can introduce
 * new Rule when required without much code modification. Check : User can't
 * mention a rule which does not appear here.
 */

enum RuleNames {
	DEADALIVERULE, BRIANSBRAIN, TOPDOWNTREE, MAZERUNNER, GOLDWINNER; // Currently going with these rules
}

/*
 * IMARule is an interface/contract which forces MARule to specify
 * getNextCellState() so it can override base class method
 */
public class MARule extends MACell implements IMARule {

	private RuleNames ruleName; // holds the rule Name specified by the user.
	

	public MARule(RuleNames ruleName, MAFrame frame, MACellState initCellState) {
		super(frame, initCellState);
		this.ruleName = ruleName;
	}

	// Function which determines which rule to go for in deciding the next cell
	// State.
	@Override
	public MACellState getNextCellState() {

		if (ruleName.equals(RuleNames.DEADALIVERULE)) {
			return getDeadAliveState();

		} else if (ruleName.equals(RuleNames.BRIANSBRAIN)) {
			return getBriansBrainState();

		} else if (ruleName.equals(RuleNames.TOPDOWNTREE)) {
			return getTopDownState();
		} else if (ruleName.equals(RuleNames.MAZERUNNER)) {
			return getMazeState();
		} else if (ruleName.equals(RuleNames.GOLDWINNER)) {
			return getZipState();
		} else {
			return getCellState();
		}

	}

	/*
	 * RuleName --> DeadAlive Rule Description --> If the cell has at least 2
	 * neighboring cells that are alive, cell should become alive. Pattern : For
	 * same number of rows and columns (both even), if center cells are initialized
	 * to alive, entire frame becomes alive exactly at a generation Count equal to
	 * row Count For example: 10*10 frame becomes alive when generationCount is 10.
	 */
	private MACellState getDeadAliveState() {
		if (getNeighborsCount(MACellState.ALIVE) >= 2)
			return MACellState.ALIVE;
		else
			return getCellState();
	}

	/*
	 * RuleName --> BRIANSBRAIN Rule Description --> Case1 - If the cell is dead and
	 * has 2 alive neighbors, the cell becomes alive. Case2 - If the cell is alive
	 * it goes to dying state. Case3 - Any Dying Cell goes to dead state.
	 */
	private MACellState getBriansBrainState() {
		if (getCellState().equals(MACellState.DEAD) && getNeighborsCount(MACellState.ALIVE) == 2)
			return MACellState.ALIVE;
		else if (getCellState().equals(MACellState.ALIVE)) {
			return MACellState.DYING;
		} else if (getCellState().equals(MACellState.DYING)) {
			return MACellState.DEAD;
		} else {
			return getCellState();
		}

	}

	/*
	 * RuleName --> TOPDOWNTREE RuleDescription --> Cell Neighbors considered here
	 * will be in the row above the cell we are looking at. (i.e., if we are at x,y
	 * the neighbors are x-1,y-1; x-1,y; x-1,y+1) Case 1. If there are 2 alive
	 * neighbors, the cell will be dying Case 2. If there is 1 alive neighbor, the
	 * cell will be alive. Outcome : A tree structure evolves as the active cells
	 * split as they grow
	 */

	// TOPDOWNTREE RULE
	private MACellState getTopDownState() {
		if (getTDNeighborsCount(MACellState.ALIVE) == 2) {
			return MACellState.DYING;
		} else if (getTDNeighborsCount(MACellState.ALIVE) == 1) {
			return MACellState.ALIVE;
		}
		return getCellState();
	}

	/*
	 * RuleName --> MAZERUNNER RuleDescription --> An ant (Black colored) tries to
	 * find its home which is diagonally opposite to where it starts. Needs to
	 * follow a dead path (white colored) in solving the maze. Restrictions : Ant
	 * always starts from (0,0) and travels only in Forward-Downward direction.
	 * (i.e., no turning backwards or top-wards)
	 * 
	 * Case 1. If there are 2 white cell in ant path, ant prefers forward move Case
	 * 2. If there are 2 blue cells in ant path, ant prefers downward move killing
	 * the blue (dying state cell) to reach home faster. Case 3. Irrespective of the
	 * cells, if the ant reaches the boundary, it moves in one direction towards the
	 * home as it is too hungry.
	 * 
	 * Outcome : Ant achieves the target of reaching home.
	 */
	// MAZERUNNER RULE
	private MACellState getMazeState() {

		if (getCellState().compareTo(MACellState.DEAD) == 0) {

			if (this.getCellXPos() >= 0 && this.getCellXPos() < getFrame().getFrameRows() && this.getCellYPos() - 1 >= 0
					&& this.getCellYPos() - 1 < getFrame().getFrameColumns()) {

				if (getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
						.compareTo(MACellState.ALIVE) == 0)
					return MACellState.ALIVE;
			}

			if (this.getCellXPos() - 1 >= 0 && this.getCellXPos() - 1 < getFrame().getFrameRows()
					&& this.getCellYPos() >= 0 && this.getCellYPos() < getFrame().getFrameColumns()) {

				if (getFrame().getCellAt(this.getCellXPos() - 1, this.getCellYPos()).getCellState()
						.compareTo(MACellState.ALIVE) == 0) {

					if (this.getCellXPos() - 1 >= 0 && this.getCellXPos() - 1 < getFrame().getFrameRows()
							&& this.getCellYPos() + 1 >= 0 && this.getCellYPos() + 1 < getFrame().getFrameColumns()) {

						if (getFrame().getCellAt(this.getCellXPos() - 1, this.getCellYPos() + 1).getCellState()
								.compareTo(MACellState.DYING) == 0)
							return MACellState.ALIVE;
					}

				}

			}

			// Last Row
			if (this.getCellXPos() == getFrame().getFrameRows() - 1) {
				if (this.getCellYPos() - 1 >= 0) {
					if (getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
							.compareTo(MACellState.ALIVE) == 0)
						return MACellState.ALIVE;
				}
				return getCellState();

			}
			// Last Column
			else if (this.getCellYPos() == getFrame().getFrameColumns() - 1) {
				if (this.getCellXPos() - 1 >= 0) {
					if (getFrame().getCellAt(this.getCellXPos() - 1, this.getCellYPos()).getCellState()
							.compareTo(MACellState.ALIVE) == 0)
						return MACellState.ALIVE;
				}
				return getCellState();
			}

			// LastRow AND LastColumn
			else if (this.getCellYPos() == getFrame().getFrameColumns() - 1
					&& this.getCellXPos() == getFrame().getFrameRows() - 1) {

				if ((getFrame().getCellAt(this.getCellXPos() - 1, this.getCellYPos()).getCellState()
						.compareTo(MACellState.ALIVE) == 0)
						|| (getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
								.compareTo(MACellState.ALIVE) == 0)) {
					return MACellState.ALIVE;
				}
				return getCellState();
			}

			// For Dying cells
		} else if (getCellState().compareTo(MACellState.DYING) == 0) {

			if (this.getCellXPos() - 1 >= 0 && this.getCellXPos() - 1 < getFrame().getFrameRows()
					&& this.getCellYPos() >= 0 && this.getCellYPos() < getFrame().getFrameColumns()) {

				if (getFrame().getCellAt(this.getCellXPos() - 1, this.getCellYPos()).getCellState()
						.compareTo(MACellState.ALIVE) == 0) {

					if (this.getCellXPos() - 1 >= 0 && this.getCellXPos() - 1 < getFrame().getFrameRows()
							&& this.getCellYPos() + 1 >= 0 && this.getCellYPos() + 1 < getFrame().getFrameColumns()) {

						if (getFrame().getCellAt(this.getCellXPos() - 1, this.getCellYPos() + 1).getCellState()
								.compareTo(MACellState.DYING) == 0)
							return MACellState.ALIVE;
					}
				}

			}

			// Last Row
			if (this.getCellXPos() == getFrame().getFrameRows() - 1) {
				if (this.getCellYPos() - 1 >= 0) {
					if (getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
							.compareTo(MACellState.ALIVE) == 0)
						return MACellState.ALIVE;
				}
				return getCellState();
			}
			// Last Column
			else if (this.getCellYPos() == getFrame().getFrameColumns() - 1) {
				if (this.getCellXPos() - 1 >= 0) {
					if (getFrame().getCellAt(this.getCellXPos() - 1, this.getCellYPos()).getCellState()
							.compareTo(MACellState.ALIVE) == 0)
						return MACellState.ALIVE;
				}
				return getCellState();
			}

			// LastRow AND LastColumn
			else if (this.getCellYPos() == getFrame().getFrameColumns() - 1
					&& this.getCellXPos() == getFrame().getFrameRows() - 1) {

				if ((getFrame().getCellAt(this.getCellXPos() - 1, this.getCellYPos()).getCellState()
						.compareTo(MACellState.ALIVE) == 0)
						|| (getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
								.compareTo(MACellState.ALIVE) == 0)) {
					return MACellState.ALIVE;
				}
				return getCellState();
			}

		} else if (getCellState().compareTo(MACellState.ALIVE) == 0)
			return MACellState.DEAD;
		return getCellState();
	}

	
	// GOLDWINNER RULE
	private MACellState getZipState() {

		

		if (getFrame().isZipDir()) {
			if (getCellState().compareTo(MACellState.ALIVE) == 0) {
				if (this.getCellXPos() >= 0 && this.getCellXPos() + 1 < getFrame().getFrameRows()
						&& this.getCellYPos() - 1 >= 0 && this.getCellYPos() + 1 < getFrame().getFrameColumns()) {
					if (getFrame().getCellAt(this.getCellXPos() + 1, this.getCellYPos() + getFrame().getCellDirection())
							.getCellState().compareTo(MACellState.DYING) == 0) {
						if (getFrame().getCellDirection() == 1) {
							getFrame().setCellDirection(-1);
						} else {
							getFrame().setCellDirection(1);
						}
						return MACellState.DYING;
					}
					if (getFrame().getCellAt(this.getCellXPos() + 1, this.getCellYPos()).getCellState()
							.compareTo(MACellState.DYING) == 0
							&& getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
									.compareTo(MACellState.DYING) == 0)
						return MACellState.DEAD;

				}

			} else if (getCellState().compareTo(MACellState.DYING) == 0) {
				if (this.getCellXPos() >= 0 && this.getCellXPos() + 1 < getFrame().getFrameRows()
						&& this.getCellYPos() - 1 >= 0 && this.getCellYPos() + 1 < getFrame().getFrameColumns()) {
					if ((getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
							.compareTo(MACellState.ALIVE) == 0)
							&& (getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() + 1).getCellState()
									.compareTo(MACellState.ALIVE) == 0)) {
						return MACellState.DEAD;
					}
				}

			}

		} else {
			if (getCellState().compareTo(MACellState.DYING) == 0) {
				return MACellState.DEAD;
			}else if(getCellState().compareTo(MACellState.ALIVE) == 0) {
				if (this.getCellXPos() >= 0 && this.getCellXPos() + 1 < getFrame().getFrameRows()
						&& this.getCellYPos() - 1 >= 0 && this.getCellYPos() + 1 < getFrame().getFrameColumns()) {
					if ((getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
							.compareTo(MACellState.DEAD) == 0)
							|| (getFrame().getCellAt(this.getCellXPos(), this.getCellYPos() + 1).getCellState()
									.compareTo(MACellState.DEAD) == 0)) {
						return MACellState.DYING;
					}
				}
			}
		}
		return getCellState();
	}

}
