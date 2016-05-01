/*
 * 
 * cellCol.java
 * This enum represents each of the colors of the grid used for the main tetris board, and holds the images required
 * for each color. It is also an implementation of the singleton pattern, where each enum is a singleton
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

import java.awt.Image;

import javax.swing.ImageIcon;

public enum cellCol {

	BLACK(Images.BLACK), CYAN(Images.CYAN), BLUE(Images.BLUE), ORANGE(Images.ORANGE), YELLOW(Images.YELLOW), GREEN(Images.GREEN), PURPLE(Images.PURPLE), RED(Images.RED); 
	
	private static class Images{
		private static final Image BLACK =  new ImageIcon("black20.jpg").getImage();
		private static final Image CYAN = new ImageIcon("cyan20.jpg").getImage();
		private static final Image BLUE = new ImageIcon("blue20.jpg").getImage();
		private static final Image ORANGE = new ImageIcon("orange20.jpg").getImage();
		private static final Image YELLOW = new ImageIcon("yellow20.jpg").getImage();
		private static final Image GREEN = new ImageIcon("green20.jpg").getImage();
		private static final Image PURPLE = new ImageIcon("magenta20.jpg").getImage();
		private static final Image RED = new ImageIcon("red20.jpg").getImage();

	}
	private Image color;
	
	private cellCol(Image c){
		 color = c;
	}
	
	public Image getImage(){
		return color;
	}
}
