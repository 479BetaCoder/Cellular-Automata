/**
 * 
 */
package edu.neu.csye6200.ma;

/**
 * @author RaviKumar
 *
 */
public class MARule extends MACell {

	private String ruleName;

	public MARule(String ruleName) {
		super();
		this.ruleName = ruleName;
	}

	@Override
	public MACellState getNextCellState() {
		if (getRuleName().equalsIgnoreCase("DeadAliveRule")) {
			if (getNeighborsCount(MACellState.ALIVE) >= 2) {
				// If the cell has at least 2 neighboring cells
				// that is alive, this cell should become alive.
				return MACellState.ALIVE;
			} else {
				// Otherwise, the cell maintains its previous state.
				return getCellState();
			}
		}
		else {
			return getCellState();
		}
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

}
