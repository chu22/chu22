/*
 * 
 * OTetronimo.java
 * This simple class implements the information needed to create an OTetronimo
 * It holds the tetronimo's color and hardcoded rotation array, which it then passes
 * to the base Tetronimo class' constructor.
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

public class OTetronimo extends Tetronimo{
	
	private final static cellCol color = cellCol.YELLOW;
	private final static Point[] rotation1 = {new Point(0,0), new Point(0,1), new Point(1,1), new Point(1,0)};
	private final static Point[][] rotations = {rotation1, rotation1, rotation1, rotation1};
	
	
	public OTetronimo(Point init){
		super(init, rotations,color);
	}
	

}
