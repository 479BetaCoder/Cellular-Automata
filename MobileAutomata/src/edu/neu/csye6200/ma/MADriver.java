/**
 * 
 */
package edu.neu.csye6200.ma;

/**
 * @author RaviKumar
 *
 */
public class MADriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// For testing
		 final char DEAD_CELL_SYMBOL = '\u25a1'; // unicode for white square
		 final char ALIVE_CELL_SYMBOL = '\u25a0'; // unicode for black square
		 final char DYING_CELL_SYMBOL = '\u25a3'; // unicode for white with black square
		 //new MAFrameSet().run();
		
		 	MAFrame maGeneration = new MAFrame(RuleNames.BRIANSBRAIN,3, 3);
		 	MAFrameSet maGenSet = new MAFrameSet(maGeneration);
		 //	maGenSet.initialFrame(maGeneration);
		 	maGenSet.run();
		 	int count = 1;
		 	for(MAFrame f : maGenSet.getLstMaFrames())
	        {
		 		System.out.println("Generation : " + count );
	        	for(int i=0;i<3;i++) {
	        		for(int j=0;j<3;j++) {
	        			if(f.getCellAt(i, j).getCellState()== MACellState.ALIVE)
	        			{
	        				System.out.print(ALIVE_CELL_SYMBOL);
	        			}
	        			else if(f.getCellAt(i, j).getCellState()== MACellState.DEAD)
	        			{
	        				System.out.print(DEAD_CELL_SYMBOL);
	        			}
	        			else {
	        				System.out.print(DYING_CELL_SYMBOL);
	        			}
	        			
	        		}
	        		System.out.println();
	        	}
	        	
	        	System.out.println("************************************");
	        	count++;
	        	
	        }
			System.out.println("Done!!");
		
	}

}
