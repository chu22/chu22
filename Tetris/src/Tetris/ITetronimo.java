/*
 * 
 * ITetronimo.java
 * This simple class implements the information needed to create an ITetronimo
 * It holds the tetronimo's color and hardcoded rotation array, which it then passes
 * to the base Tetronimo class' constructor.
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

public class ITetronimo extends Tetronimo{
	
	private static final cellCol color = cellCol.CYAN;
	private static final Point[] rotation1 = {new Point(1,0), new Point(1,1), new Point(1,2), new Point(1,3)};
	private static final Point[] rotation2 = {new Point(0,2), new Point(1,2), new Point(2,2), new Point(3,2)};
	private static final Point[] rotation3 = {new Point(2,0), new Point(2,1), new Point(2,2), new Point(2,3)};
	private static final Point[] rotation4 = {new Point(0,1), new Point(1,1), new Point(2,1), new Point(3,1)};
	private static final Point[][] rotations = {rotation1, rotation2, rotation3, rotation4};
	
	
	public ITetronimo(Point init){
		super(init, rotations, color);
	}
	

}
