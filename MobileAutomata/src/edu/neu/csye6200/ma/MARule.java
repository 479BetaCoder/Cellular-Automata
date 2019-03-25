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
 * new Rule when required without much code modification. 
 * Check : User can't mention a rule which does not appear here.
 */

enum RuleNames {
	DEADALIVERULE, BRIANSBRAIN; // Currently going with these rules
}

/*
 * IMARule is an interface/contract which forces MARule to specify
 * getNextCellState() so it can override base class method
 */
public class MARule extends MACell implements IMARule {

	private RuleNames ruleName; // holds the rule Name specified by the user.

	public MARule(RuleNames ruleName,MAFrame frame,MACellState initCellState) {
		super(frame,initCellState);
		this.ruleName = ruleName;
	}

	// Function which determines which rule to go for in deciding the next cell State.
	@Override
	public MACellState getNextCellState() {

		if (ruleName.equals(RuleNames.DEADALIVERULE)) {
			return getDeadAliveState();

		} else if (ruleName.equals(RuleNames.BRIANSBRAIN)) {
			return getBriansBrainState();

		} else {
			return getCellState();
		}

	}

	/*
	 * RuleName --> DeadAlive 
	 * Rule Description --> If the cell has at least 2
	 * neighboring cells that are alive, cell should become alive.
	 * Pattern : For same number of rows and columns (both even), 
	 * if center cells are initialized to alive, entire frame becomes alive exactly at a generation Count equal to row Count
	 * For example: 10*10 frame becomes alive when generationCount is 10.
	 */
	private MACellState getDeadAliveState() {
		if (getNeighborsCount(MACellState.ALIVE) >= 2)
			return MACellState.ALIVE;
		else
			return getCellState();
	}
	
	/*
	 * RuleName --> BRIANSBRAIN 
	 * Rule Description --> 
	 * Case1 - If the cell is dead and has 2 alive neighbors, the cell becomes alive. 
	 * Case2 - If the cell is alive it goes to dying state.
	 * Case3 - Any Dying Cell goes to dead state.
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
}
