/*
 * 
 * ControlsBox.java
 * This simple class contains the GUI elements and display logic for the showing the next block
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class NextBlockBox extends JPanel {

	private Tetronimo next;
	private cellCol[][] grid;

	private final int N_ROWS = 4;
	private final int N_COLS = 5;
	private final int CELL_DIM = 20;
	
	private JLabel label;

	public NextBlockBox(Tetronimo t) {
		label = new JLabel("NEXT BLOCK:");
		add(label);
		next = t;
		grid = new cellCol[N_ROWS][N_COLS];
		for (int i = 0; i < N_ROWS; i++) {
			for (int j = 0; j < N_COLS; j++) {
				grid[i][j] = cellCol.BLACK;
			}
		}
		updateGrid(next.getLocations(), next.getColor());
		setBounds(220,0, CELL_DIM*N_COLS,CELL_DIM*N_ROWS+20);
	}

	public void changeNext(Tetronimo t) {
		updateGrid(next.getLocations(), cellCol.BLACK);
		next = t;
		updateGrid(t.getLocations(), t.getColor());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < N_ROWS; i++) {
			for (int j = 0; j < N_COLS; j++) {
				g.drawImage(grid[i][j].getImage(),j * CELL_DIM, 20+i * CELL_DIM, null);
			}
		}
	}

	public void updateGrid(Point[] loc, cellCol col) {
		for (Point p : loc) {
			grid[p.getX()][p.getY()] = col;
		}
	}
}
