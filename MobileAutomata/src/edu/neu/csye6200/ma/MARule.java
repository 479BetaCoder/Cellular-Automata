/**
 * 
 */
package edu.neu.csye6200.ma;

/**
 * @author RaviKumar
 *
 */

enum RuleNames {
	DEADALIVERULE, BRIANSBRAIN;
}

public class MARule extends MACell implements IMARule {

	private RuleNames ruleName;

	public MARule(RuleNames ruleName) {
		super();
		this.ruleName = ruleName;
	}

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
	 * RuleName --> DeadAlive Rule Description --> If the cell has at least 2
	 * neighboring cells that are alive, cell should become alive.
	 */
	private MACellState getDeadAliveState() {
		if (getNeighborsCount(MACellState.ALIVE) >= 2)
			return MACellState.ALIVE;
		else
			return getCellState();
	}

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
