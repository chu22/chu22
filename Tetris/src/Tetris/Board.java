/*
 * 
 * Board.java
 * This class holds all the game logic required for the main game board, using the functionality of
 * the teronimo class methods. It also holds all inofrmation on the game state, such as game over, points scored,
 * and other details. 
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel {
	// grid item declarations
	private cellCol[][] grid;
	private Tetronimo current;
	private Tetronimo next;

	// private game state instance variables
	private int lines;
	private int score;
	private boolean gameOver;
	private boolean locked;

	// grid constants
	private final int N_ROWS = 22;
	private final int N_COLS = 10;
	private final int startX = 0;
	private final int startY = N_COLS / 2 - 2;
	private final int CELL_DIM = 20;

	// constructor initializes grid
	public Board(Tetronimo c, Tetronimo n) {
		grid = new cellCol[N_ROWS][N_COLS];
		for (int i = 0; i < N_ROWS; i++) {
			for (int j = 0; j < N_COLS; j++) {
				grid[i][j] = cellCol.BLACK;
			}
		}
		current = c;
		next = n;
		current.setStart(new Point(startX, startY));
		gameOver = false;
		score = 0;
		lines = 0;
		setBounds(0, 0, 200, 440);
	}

	// draw grid
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 2; i < N_ROWS; i++) {
			for (int j = 0; j < N_COLS; j++) {
				g.drawImage(grid[i][j].getImage(), j * CELL_DIM, i * CELL_DIM, null);
			}
		}
	}

	//check if a move causes a collision with an already dropped piece or with a border
	public boolean moveCollision() {
		Point[] locs = current.getLocations();
		for (Point p : locs) {
			if (p.getX() < 0 || p.getX() >= N_ROWS || p.getY() < 0 || p.getY() >= N_COLS) {
				return true;
			}
			if (grid[p.getX()][p.getY()] != cellCol.BLACK) {
				return true;
			}

		}
		return false;
	}

	//checks if a rotation causes a collision with a border
	//the game is set so that if it is the case, the rotated block will be shifted by
	//the Point offset returned by the function, after which moveCollision will be checked
	public Point rotateCollision() {
		int xOffset = 0;
		int yOffset = 0;
		int px, py;
		Point[] locs = current.getLocations();
		for (Point p : locs) {
			px = p.getX();
			py = p.getY();

			if (N_ROWS - 1 - px <= xOffset) {
				xOffset = N_ROWS - 1 - px;
			}
			if (-1 * py > yOffset) {
				yOffset = -1 * py;
			} else if (N_COLS - 1 - py <= yOffset) {
				yOffset = N_COLS - 1 - py;
			}
		}
		return new Point(xOffset, yOffset);

	}

	//checks if a line is full to be cleared
	private boolean lineFull(int row) {
		for (int i = 0; i < N_COLS; i++) {
			if (grid[row][i] == cellCol.BLACK) {
				return false;
			}
		}
		return true;
	}

	//clears a line
	private void deleteLine(int row) {
		for (int i = row; i > 1; i--) {
			for (int j = 0; j < N_COLS; j++) {
				grid[i][j] = grid[i - 1][j];
			}
		}
		lines++;
	}

	//updates the score
	public void updateScore(int l) {

		switch (l) {
		case 1:
			score += getLevel() * 40;
			break;
		case 2:
			score += getLevel() * 100;
			break;
		case 3:
			score += getLevel() * 300;
			break;
		case 4:
			score += getLevel() * 1200;
			break;
		default:
			break;
		}
	}

	//attempts to clear if possible once a block has been locked
	public void doClear(Point[] loc) {
		int simLines = 0;
		Set<Integer> rows = new TreeSet<Integer>();
		for (Point p : loc) {
			rows.add(p.getX());
		}
		for (Integer i : rows) {
			while (lineFull(i)) {
				deleteLine(i);
				simLines++;
			}

		}
		updateScore(simLines);
	}

	//executes gravity logic due to timer or hard/soft drop
	public void gravity() {
		if (locked) {
			return;
		}
		updateGrid(current.getLocations(), cellCol.BLACK);
		current.moveDown();
		if (moveCollision()) {
			current.moveUp();
			updateGrid(current.getLocations(), current.getColor());
			doClear(current.getLocations());
			locked = true;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < N_COLS; j++) {
					if (grid[i][j] != cellCol.BLACK) {
						gameOver = true;
					}
				}
			}
		} else {
			updateGrid(current.getLocations(), current.getColor());
		}
	}

	//sets input tetronimo to next and sets next tetronimo to current
	public void changeTetronimo(Tetronimo t) {
		current = next;
		next = t;
		current.setStart(new Point(startX, startY));
		locked = false;
	}

	//updates the grid for the given tetronimo
	public void updateGrid(Point[] loc, cellCol col) {
		for (Point p : loc) {
			grid[p.getX()][p.getY()] = col;
		}
	}

	//moves a tetronimo right, if it does not cause a collision
	public void moveRight() {
		if (locked) {
			return;
		}
		updateGrid(current.getLocations(), cellCol.BLACK);
		current.moveRight();
		if (moveCollision()) {
			current.moveLeft();
			updateGrid(current.getLocations(), current.getColor());
		} else {
			updateGrid(current.getLocations(), current.getColor());
		}
	}

	//moves a tetronimo left, if it does not cause a collision
	public void moveLeft() {
		if (locked) {
			return;
		}
		updateGrid(current.getLocations(), cellCol.BLACK);
		current.moveLeft();
		if (moveCollision()) {
			current.moveRight();
			updateGrid(current.getLocations(), current.getColor());
		} else {
			updateGrid(current.getLocations(), current.getColor());
		}
	}

	//rotates a tetronimo left, if it does not cause a collision with another tetronimo,
	//but the game will shift the tetronimo if it only collides with a border
	public void rotateLeft() {
		if (locked) {
			return;
		}
		updateGrid(current.getLocations(), cellCol.BLACK);
		current.rotateLeft();
		Point offset = rotateCollision();
		current.shiftVertical(offset.getX());
		current.shiftHorizontal(offset.getY());
		if (moveCollision()) {
			current.shiftVertical(-1 * offset.getX());
			current.shiftHorizontal(-1 * offset.getY());
			current.rotateRight();
			updateGrid(current.getLocations(), current.getColor());
		} else {
			updateGrid(current.getLocations(), current.getColor());
		}
	}

	//rotates a tetronimo left, if it does not cause a collision with another tetronimo,
	//but the game will attempt to shift the tetronimo if it only collides with a border
	public void rotateRight() {
		if (locked) {
			return;
		}
		updateGrid(current.getLocations(), cellCol.BLACK);
		current.rotateRight();
		Point offset = rotateCollision();
		current.shiftVertical(offset.getX());
		current.shiftHorizontal(offset.getY());
		if (moveCollision()) {
			current.shiftVertical(-1 * offset.getX());
			current.shiftHorizontal(-1 * offset.getY());
			current.rotateLeft();
			updateGrid(current.getLocations(), current.getColor());
		} else {
			updateGrid(current.getLocations(), current.getColor());
		}
	}

	//gets the level based on the number of lines cleared
	public int getLevel() {
		int level = lines / 10;
		if (level >= 24) {
			return 24;
		}
		return level + 1;
	}
	
	//check if tetronimo is locked/if the game should execute the necessary clearing logic
	public boolean isLocked() {
		return locked;
	}

	//check if game over, tetronimo is above the top border of the board
	public boolean gameOver() {
		return gameOver;
	}

	//gets the current score
	public int getScore() {
		return score;
	}
}
