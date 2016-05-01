/*
 * 
 * LTetronimo.java
 * This simple class implements the information needed to create an LTetronimo
 * It holds the tetronimo's color and hardcoded rotation array, which it then passes
 * to the base Tetronimo class' constructor.
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;


public class LTetronimo extends Tetronimo{
	
	private final static cellCol color = cellCol.ORANGE;
	private final static Point[] rotation1 = {new Point(1,0), new Point(1,1), new Point(1,2), new Point(0,2)};
	private final static Point[] rotation2 = {new Point(0,1), new Point(1,1), new Point(2,1), new Point(2,2)};
	private final static Point[] rotation3 = {new Point(1,2), new Point(1,1), new Point(1,0), new Point(2,0)};
	private final static Point[] rotation4 = {new Point(2,1), new Point(1,1), new Point(0,1), new Point(0,0)};
	private final static Point[][] rotations = {rotation1, rotation2, rotation3, rotation4};
	
	
	public LTetronimo(Point init){
		super(init, rotations, color);
	}

}
