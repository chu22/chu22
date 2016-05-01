/*
 * 
 * Tetronimo.java
 * This class contains all the logic required for manipulating tetronimoes.
 * It is the base class from which the actual tetronimo types extend from
 * but is implemented so all the logic can be stored in this base class.
 * 
 * The tetronimo has an array of 4 points, which represents the squares location in an
 * imaginary 4x4 grid, with (0,0) at the top left corner. It also has a start point, which
 * is used in conjunction with the point array, to get the actual location of the tetronimo
 * cells on the grid.
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

public class Tetronimo {

	//tetronimo location/color variables
	private Point start;		//start point, used to translate relative position of the squares array into an absolute location on the actual game board
	private Point[] squares;	//holds tetronimo locations relative to a 4x4 grid
	private int rotationState;	//records which rotation state the tetronimo is in
	private cellCol color;
	private Point[][] rotations;//holds hardcoded rotations that make up possible relative locations of the tetronimo
	
	//constructor
	public Tetronimo(Point init, Point[][] rot, cellCol col){
		start = init;
		rotationState = 0;
		rotations = rot;
		squares = rotations[0];
		color = col;
	}
	
	//gets absolute tetronimo location based on start point and locations from the rotationstate
	public Point[] getLocations(){
		Point[] locations = new Point[4];
		int x, y;
		for(int i = 0;i<4;i++){
			x = start.getX()+squares[i].getX();
			y = start.getY()+squares[i].getY();
			locations[i] = new Point(x,y);
		}
		return locations;
	}
	
	//gets the color of the tetronimo
	public cellCol getColor(){
		return color;
	}
	
	//sets the starting point that acts as the offset to get the absolute location of the tetronimo
	public void setStart(Point p){
		start = p;
	}
	
	//rotates piece to the right
	public void rotateRight(){
		rotationState++; 
		if(rotationState>3){
			rotationState-=4;
		}
		squares = rotations[rotationState];
	}
	
	//rotates piece to the left
	public void rotateLeft(){
		
		rotationState--;
		if(rotationState<0){
			rotationState+=4;
		}
		squares = rotations[rotationState];
	}
	
	//shifts piece left 1 cell
	public void moveLeft(){
		start.shiftY(-1);
	}
	
	//shifts piece right 1 cell
	public void moveRight(){
		start.shiftY(1);
	}
	
	//moves piece down 1 cell
	public void moveDown(){
		start.shiftX(1);
	}
	
	//moves piece up 1 cell
	public void moveUp(){
		start.shiftX(-1);
	}
	
	//shifts piece horizontal by some offset
	public void shiftHorizontal(int y){
		start.shiftY(y);
	}
	
	//shifts piece vertically by some offset
	public void shiftVertical(int x){
		start.shiftX(x);
	}
}
