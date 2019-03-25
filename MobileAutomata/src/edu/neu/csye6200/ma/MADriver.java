/**
 * 
 */
package edu.neu.csye6200.ma;

import java.util.Scanner;

/**
 * @author RaviKumar ClassName : MADriver 
 * Description : Currently used to print the automata using Unicode. 
 * After 5b the functionality will be shifted to display in a UI using User Inputs 
 * Valuable Outcome : Can be used to test the current rules and initialize the alive cells to start with.
 */
public class MADriver {

	// For testing
	static final char DEAD_CELL_SYMBOL = '\u25a1'; // unicode for white square
	static final char ALIVE_CELL_SYMBOL = '\u25a0'; // unicode for black square
	static final char DYING_CELL_SYMBOL = '\u25a3'; // unicode for white with black square

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int rows, cols, ruleNumber, generationLimit, middleCell, initAliveCell; // for testing (can be generated using
																				// UI in 5b)

		MAFrame maGeneration; // MAFrames for setting up the automata
		MAFrameSet maGenSet; // MAFrameSet which holds a Map of MAFrames

		/*
		 * For testing printing the 2D Frame using Unicode symbols and will be visually
		 * enhanced once we switch back to UI As of now taking inputs through console.
		 */
		try (Scanner scnObj = new Scanner(System.in)) {
			System.out.println("Welcome to Mobile Automata");
			System.out.println("********************************************************");
			System.out.println("Please enter rows you would like to see in the frame : ");
			rows = scnObj.nextInt();
			System.out.println("Please enter columns you would like to see in the frame : ");
			cols = scnObj.nextInt();
			System.out.println("Please enter your rule choice : \nEnter 1 for " + RuleNames.DEADALIVERULE
					+ "\nEnter 2 for " + RuleNames.BRIANSBRAIN);
			ruleNumber = scnObj.nextInt();
			System.out.println("Please enter Generation Limit Count : ");
			generationLimit = scnObj.nextInt();

			middleCell = Math.round((rows * cols) / 2); // for testing
			initAliveCell = middleCell - Math.round(cols / 2); // for testing

			/*
			 * For Testing but we can take inputs from user using drop down box/combo box in
			 * Swing UI. InitAliveCell can be taken as input through selecting a cell in the
			 * UI or passing it as a value.
			 */
			if (ruleNumber == 1) {
				maGeneration = new MAFrame(RuleNames.DEADALIVERULE, rows, cols, initAliveCell);
			} else {
				maGeneration = new MAFrame(RuleNames.BRIANSBRAIN, rows, cols, initAliveCell);
			}

			// Creating the Initial FrameSet with Initial Frame.
			maGenSet = new MAFrameSet(maGeneration, generationLimit);
			// Starting the Mobile Automata
			maGenSet.run();

			// Print Routine to display the frames generated and pushed to the MAP.
			maPrintRoutine(maGenSet, rows, cols);

			System.out.println("********** Other features in the simulation *********");
			System.out.println(
					"Any Generation you would like to see again ? If so please enter the generation number : ");

			/*
			 * As we are using a MAP (TreeMap), user is powered to view any generation he
			 * wishes to see once the automata is processed
			 */
			int userChoice = scnObj.nextInt();
			System.out.println("Generation : " + userChoice);

			// Print Routine to display the frame corresponding to a particular generation.
			maPrintRoutine(maGenSet, rows, cols, userChoice);

			System.out.println("Mobile Automata Simulation completed successfully for the mentioned generation !!");

			/*
			 * Other features include, User can wish to see the mobile automata in a reverse
			 * order, also we use this routine to display the automata once it reaches the
			 * boundaries i.e., creating a wave backwards
			 */
			System.out.println("Do you want to print generations in reverse order?(y/n) : ");
			String choice = scnObj.next();
			if (choice.equalsIgnoreCase("y")) {
				// Print routine to display the frames in reverse order of generations
				maReverseRoutine(maGenSet, rows, cols, generationLimit);
			}
		} catch (Exception e) {
			System.out.println("An exception occurred while testing automata. Details : " + e.toString());
		}
	}

	// Print Routine to display the frames generated and pushed to the MAP. 
	public static void maPrintRoutine(MAFrameSet maGenSet, int frameRows, int frameCols) {
		int count = 1;
		for (int gen : maGenSet.getMaFrameRecord().keySet()) {
			System.out.println("Generation : " + count);
			for (int i = 0; i < frameRows; i++) {
				for (int j = 0; j < frameCols; j++) {
					if (maGenSet.getMaFrameRecord().get(gen).getCellAt(i, j).getCellState() == MACellState.ALIVE) {
						System.out.print(ALIVE_CELL_SYMBOL);
					} else if (maGenSet.getMaFrameRecord().get(gen).getCellAt(i, j)
							.getCellState() == MACellState.DEAD) {
						System.out.print(DEAD_CELL_SYMBOL);
					} else {
						System.out.print(DYING_CELL_SYMBOL);
					}

				}
				System.out.println();
			}

			System.out.println("************************************");
			count++;

		}
		System.out.println("Mobile Automata Simulation completed successfully !!");
	}

	
	// Print Routine to display the frame corresponding to a particular generation.
	public static void maPrintRoutine(MAFrameSet maGenSet, int frameRows, int frameCols, int userChoice) {
		for (int i = 0; i < frameRows; i++) {
			for (int j = 0; j < frameCols; j++) {
				if (maGenSet.getMaFrameRecord().get(userChoice).getCellAt(i, j).getCellState() == MACellState.ALIVE) {
					System.out.print(ALIVE_CELL_SYMBOL);
				} else if (maGenSet.getMaFrameRecord().get(userChoice).getCellAt(i, j)
						.getCellState() == MACellState.DEAD) {
					System.out.print(DEAD_CELL_SYMBOL);
				} else {
					System.out.print(DYING_CELL_SYMBOL);
				}

			}
			System.out.println();
		}
	}

	// Print routine to display the frames in reverse order of generations
	public static void maReverseRoutine(MAFrameSet maGenSet, int frameRows, int frameCols, int generationLimit) {
		int genCount = generationLimit;
		for (int gen : maGenSet.getMaFrameRecord().keySet()) {
			System.out.println("Generation : " + genCount);
			for (int i = 0; i < frameRows; i++) {
				for (int j = 0; j < frameCols; j++) {
					if (maGenSet.getMaFrameRecord().get(generationLimit - gen + 1).getCellAt(i, j)
							.getCellState() == MACellState.ALIVE) {
						System.out.print(ALIVE_CELL_SYMBOL);
					} else if (maGenSet.getMaFrameRecord().get(generationLimit - gen + 1).getCellAt(i, j)
							.getCellState() == MACellState.DEAD) {
						System.out.print(DEAD_CELL_SYMBOL);
					} else {
						System.out.print(DYING_CELL_SYMBOL);
					}

				}
				System.out.println();
			}

			System.out.println("************************************");
			genCount--;

		}
		System.out.println("Mobile Automata Simulation in reverse order completed successfully !!");
	}

}
