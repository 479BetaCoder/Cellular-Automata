/**
 * 
 */
package edu.neu.csye6200.ma;

import java.util.logging.Logger;

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
	LOCKME, DEADALIVE, BRIANSBRAIN, TOPDOWNTREE, GOLDWINNER; // Currently going with these comboRules
}

/*
 * IMARule is an interface/contract which forces MARule to specify
 * getNextCellState() so it can override base class method
 */
public class MARule extends MACell {

	private RuleNames ruleName; // holds the rule Name specified by the user.
	int[] temp = new int[2]; // for storing next active cell co-ordinates.

	// For Logging application process to the console.
	private static Logger log = Logger.getLogger(MACell.class.getName());

	public MARule(RuleNames ruleName, MARegion region, MACellState initCellState) {
		super(region, initCellState);
		this.ruleName = ruleName;
	}

	// Function which determines which rule to go for in deciding the next cell
	// State.
	@Override
	public MACellState getNextCellState() {

		if (ruleName.equals(RuleNames.DEADALIVE)) {
			return getDeadAliveState();
		} else if (ruleName.equals(RuleNames.BRIANSBRAIN)) {
			return getBriansBrainState();
		} else if (ruleName.equals(RuleNames.TOPDOWNTREE)) {
			return getTopDownState();
		} else if (ruleName.equals(RuleNames.GOLDWINNER)) {
			return getGoldState();
		} else {
			return getCellState();
		}

	}

	// For active cell next Position
	public int[] getNextCellPos() {

		return getLockMeCood(MACellState.DEAD);
	}

	

	// Helper method to get the required neighbors for active cell movement
	private int[] getLockMeCood(MACellState state) {

		try {

			for (int i = 1; i >= -1; i--) {
				for (int j = 1; j >= -1; j--) {
					if (this.getCellXPos() + i >= 0 && this.getCellXPos() + i < getRegion().getRegionRows()
							&& this.getCellYPos() + j >= 0 && this.getCellYPos() + j < getRegion().getRegionColumns()) {
						if (getRegion().getCellAt(this.getCellXPos() + i, this.getCellYPos() + j).getCellState()
								.compareTo(state) == 0) {
							if (!(i == 0 && j == 0)) // should not consider the current cell as neighbor
							{
								temp[0] = this.getCellXPos() + i;
								temp[1] = this.getCellYPos() + j;
								return temp;
							}
						}
					}
				}

			}
			// This is done to show that you are locked up !!!
			temp[0] = -1;
			temp[1] = -1;
		} catch (Exception e) {
			log.severe("Exception occured while getting Neighbor Count : " + e.toString());

		}
		return temp;

	}

	/*
	 * RuleName --> DeadAlive Rule Description --> If the cell has at least 2
	 * neighboring cells that are alive, cell should become alive. Pattern : For
	 * same number of rows and columns (both even), if center cells are initialized
	 * to alive, entire region becomes alive exactly at a generation Count equal to
	 * row Count For example: 10*10 region becomes alive when generationCount is 10.
	 * Outcome: Due to 2 live cells every cell in the grid becomes alive.
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
	 * it goes to dying state. Case3 - Any Dying Cell goes to dead state. Outcome:
	 * Creates chaotic oscillation patterns and sometimes spaceships. After some
	 * steps, the spaceships fly-off and every cell becomes dead.
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

	private MACellState getTopDownState() {
		if (getTDNeighborsCount(MACellState.ALIVE) == 2) {
			return MACellState.DYING;
		} else if (getTDNeighborsCount(MACellState.ALIVE) == 1) {
			return MACellState.ALIVE;
		}
		return getCellState();
	}

	/*
	 * RULENAME: GOLDWINNER DESCRIPTION: An active cell leads to opening a zip of a
	 * gold bag and getting its owner fortune. Initially the zip is opened from
	 * bottom to top in a zig-zag pattern using the CellDirection flag Once the
	 * active cell reaches the top, it slowly unzips the bag (black in color) and
	 * displays the gold.
	 * 
	 * unzipping - (ALIVE --> BLACK, DEAD --> WHITE, DYING --> BLUE) unzipped -
	 * (ALIVE --> BLACK, DEAD --> GOLD, DYING --> BLUE)
	 * 
	 * Case1. Unzipping - 1a. When there is alive cell - Cells check the row below
	 * them and direction is determined alternatively by the CellDirection Flag. If
	 * the obtained cell is dying, make the alive cell state as dying. If the cell
	 * has a dying cell below and a dying cell at y-1, we set the state to dead
	 * (white).
	 * 
	 * 1b.If a dying cell has two alive neighbors surrounding it (i.e., y-1 and
	 * y+1), the cell dies.
	 * 
	 * Case2. Unzipped - (i.e., the active cell reached the top) 2a. A dying cell
	 * becomes dead. 2b. The alive cell is checked if there is any dead cells in the
	 * surrounding (i.e., y-1,y+1) If the cell has atleast 1 dead cell, it becomes
	 * dying cell.
	 * 
	 * In all other cases, the same state is returned.
	 * 
	 * OUTCOME: A black bag containing gold is unzipped and the user gets a full
	 * bounty!!!
	 * 
	 * 
	 */
	private MACellState getGoldState() {

		if (getRegion().isZipDir()) {
			if (getCellState().compareTo(MACellState.ALIVE) == 0) {
				if (this.getCellXPos() + 1 < getRegion().getRegionRows() && this.getCellYPos() - 1 >= 0
						&& this.getCellYPos() + 1 < getRegion().getRegionColumns()) {
					if (getRegion()
							.getCellAt(this.getCellXPos() + 1, this.getCellYPos() + getRegion().getCellDirection())
							.getCellState().compareTo(MACellState.DYING) == 0) {
						return MACellState.DYING;
					}
					if (getRegion().getCellAt(this.getCellXPos() + 1, this.getCellYPos()).getCellState()
							.compareTo(MACellState.DYING) == 0
							&& getRegion().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
									.compareTo(MACellState.DYING) == 0) // this is to constraint to a narrow region
						return MACellState.DEAD;

				}

			} else if (getCellState().compareTo(MACellState.DYING) == 0) {
				if (this.getCellYPos() - 1 >= 0 && this.getCellYPos() + 1 < getRegion().getRegionColumns()) {
					if ((getRegion().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
							.compareTo(MACellState.ALIVE) == 0)
							&& (getRegion().getCellAt(this.getCellXPos(), this.getCellYPos() + 1).getCellState()
									.compareTo(MACellState.ALIVE) == 0)) {
						return MACellState.DEAD;
					}
				}

			}

		} else {
			if (getCellState().compareTo(MACellState.DYING) == 0) {
				return MACellState.DEAD;
			} else if (getCellState().compareTo(MACellState.ALIVE) == 0) {
				if (this.getCellYPos() - 1 >= 0 && this.getCellYPos() + 1 < getRegion().getRegionColumns()) {
					if ((getRegion().getCellAt(this.getCellXPos(), this.getCellYPos() - 1).getCellState()
							.compareTo(MACellState.DEAD) == 0)
							|| (getRegion().getCellAt(this.getCellXPos(), this.getCellYPos() + 1).getCellState()
									.compareTo(MACellState.DEAD) == 0)) {
						return MACellState.DYING;
					}
				}
			}
		}
		return getCellState();
	}

}
