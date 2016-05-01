/*
 * 
 * Tetris.java
 * This class controls the overall game flow and handles all event listeners
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Tetris extends JFrame {
	//GUI Element declarations
	private Board b;
	private ControlsBox c;
	private NextBlockBox n;
	private ScoreBox s;
	private Menu m;
	private Timer t;
	private JButton[] buttons;

	//JFrame dimensions.
	private final int FRAME_WIDTH = 500;
	private final int FRAME_HEIGHT = 500;

	// Timer delay
	private int delay;

	//Constructor
	public Tetris() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setTitle("Tetris");
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		//initialize components
		delay = 800;
		Tetronimo next = randomTetronimo();
		b = new Board(randomTetronimo(), next);
		n = new NextBlockBox(next);
		s = new ScoreBox();
		m = new Menu();
		t = new Timer(delay, new TimerHandler());
		c = new ControlsBox();

		//add event handlers
		m.getItem(0).addActionListener(new resetMenuHandler());
		m.getItem(1).addActionListener(new exitMenuHandler());
		m.getItem(2).addActionListener(new helpMenuHandler());
		m.getItem(3).addActionListener(new aboutMenuHandler());

		buttons = c.getButtons();

		for (JButton b : buttons) {
			b.addActionListener(new controlsHandler());
			b.setFocusable(false);
		}

		add(n);
		add(b);
		add(s);
		add(c);
		setJMenuBar(m);
		t.start();
		addKeyListener(new keyHandler());
	}

	/* Main */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tetris window = new Tetris();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//reset GUI elements for a new game
	public void reset() {
		delay = 800;
		remove(b);
		remove(n);
		t.stop();

		Tetronimo next = randomTetronimo();
		b = new Board(randomTetronimo(), next);
		n = new NextBlockBox(next);
		t = new Timer(delay, new TimerHandler());

		add(b);
		add(n);
		t.start();
		b.repaint();
		n.repaint();
		s.setScore(0);
	}

	//get random tetronimo
	//makes call to TetronimoFactory for factory design pattern implementation
	public Tetronimo randomTetronimo() {
		Random rand = new Random();
		int t = rand.nextInt(7);
		Tetronimo tet = TetronimoFactory.getTetronimo(t);
		return tet;
	}

	//executes logic for game once a block has been "locked", once gravity has failed to move it downwards.
	//It will create a new tetronimo and set it ot the next tetronimo, the n check for changes in level and score
	//possibly changing the timer delay as well.
	public void blockLocked() {
		Tetronimo next = randomTetronimo();
		n.changeNext(next);
		b.changeTetronimo(next);
		int tmp = 1000 * (50 - b.getLevel() * 2) / 60;
		if (tmp != delay) {
			System.out.println(b.getLevel());
			delay = tmp;
			t.stop();
			t = new Timer(delay, new TimerHandler());
			t.start();

		}
		s.setScore(b.getScore());
		b.repaint();
		n.repaint();
	}

	//the event listener required for the button controls
	//essentially the same logic as the key listener implementation
	private class controlsHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttons[0]) {			//soft drop
				b.gravity();
				if (b.gameOver()) {
					reset();
					return;
				}
				if (b.isLocked()) {
					blockLocked();
					t.restart();
				}
			} else if (e.getSource() == buttons[1]) {	//hard drop
				while (!b.isLocked()) { //executes gravity until can no longer be dropped
					b.gravity();
				}
				blockLocked();
			} else if (e.getSource() == buttons[2]) {	//move left
				b.moveLeft();
			} else if (e.getSource() == buttons[3]) {	//move right
				b.moveRight();
			} else if (e.getSource() == buttons[4]) {	//rotate right
				b.rotateRight();
				t.restart();	//resets timer on rotation
			} else if (e.getSource() == buttons[5]) {	//rotate left
				b.rotateLeft();
				t.restart();
			}
			b.repaint();

		}

	}

	private class keyHandler implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_LEFT) {								//move left
				b.moveLeft();
			}

			else if (key == KeyEvent.VK_RIGHT) {						//move right
				b.moveRight();
			}

			else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_X) {	//rotate right
				b.rotateRight();
				t.restart();
			} 
			
			else if (key == KeyEvent.VK_Z) {							//rotate left
				b.rotateLeft();
				t.restart();
			}

			else if (key == KeyEvent.VK_DOWN) {							//soft drop
				b.gravity();
				if (b.gameOver()) {
					reset();
					return;
				}
				if (b.isLocked()) {
					blockLocked();
					t.restart();
				}
			} else if (key == KeyEvent.VK_SPACE) {						//hard drop
				while (!b.isLocked()) {
					b.gravity();
				}
				blockLocked();
			}

			b.repaint();

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

	//action listener for the timer, executes check for gravity effects as well
	private class TimerHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (b.gameOver()) {
				reset();
				return;
			}
			b.gravity();
			b.repaint();
			if (b.isLocked()) {
				blockLocked();
			}
		}
	}

	//handles reset menu event
	private class resetMenuHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			reset();
		}
	}
	
	//holds string to be displayed in help menu
	public String printHelp() {
		String str = "About Tetris:"
				+ "The point of tetris is to get the highest score by clearing as many lines as possible.\n"
				+ "If multiple lines are cleared at the same time, more points are given.\n"
				+ "	o One line cleared: 40 points X current level\n"
				+ "	o Two lines cleared: 100 points X current level\n"
				+ "	o Three lines cleared: 300 points X current level\n"
				+ "	o Four lines cleared: 1200 points X current level\n\n" +

		"Controls:\n" + "    LEFT, RIGHT, and DOWN arrow keys move the tetromino by one cell in that direction, while"
				+ "    the UP arrow key turns the tetromino to the right.\n"
				+ "    The Z button turns the tertomino to the left and the X button turns the\n"
				+ "    tetromino to the right.\n" + "    The SPACE bar instantly drops the tetromino to the ground.\n\n"
				+

		"Game Menu:\n" + "The game menu has 4 options.\n" + "Start/Reset: Resets and starts a new game.\n "
				+ "Exit: Exit the game.\n" + "Help: Intsructions on how to play the game.\n "
				+ "About: Info about the people that colaborated on this project\n";
		return str;
	}

	//displays help box for help menu event
	private class helpMenuHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String p = printHelp();
			JOptionPane.showMessageDialog(Tetris.this, p, "Help", JOptionPane.PLAIN_MESSAGE);
		}
	}

	//displays about box for about menu event
	private class aboutMenuHandler implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(Tetris.this,
					"TETRIS [ U. of Illinois at Chicago: CS342 - Project5 ]\n" + "Version 1.0 (4/28/2016)\n"
							+ "Developed by :\n    Lawrence Chu [chu22@uic.edu]\n    Grieldo Lulaj [glulaj2@uic.edu]\n    Daia Elsalaymeh [Delsal2@uic.edu]",
					"About", JOptionPane.PLAIN_MESSAGE);

		}
	}

	//exits game for exit menu event
	private class exitMenuHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

}
