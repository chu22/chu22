/*
 * 
 * Point.java
 * This class is a simple representation of the a point on the tetris board,
 * used by the Tetronimo classes to represent each of their individual cells.
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

public class Point {

	private int x;
	private int y;
	
	public Point(int gx, int gy){
		x = gx;
		y = gy;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void shiftX(int s){
		x+=s;
	}
	
	public void shiftY(int s){
		y+=s;
	}
	
}
