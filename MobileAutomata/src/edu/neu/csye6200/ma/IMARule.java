package edu.neu.csye6200.ma;

/**
 * @author RaviKumar
 * Interface : IMARule
 * Description : Provides Contract for MACell to implement required methods.
 *
 */
public interface IMARule {
	 public MACellState getNextCellState();
	 public int[] getNextCellPos();
}
