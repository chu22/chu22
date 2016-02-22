/* 
 * Minesweeper.java 
 *
 * The main minesweeper class/frame
 * This class contains all event handlers and 
 * controls communications between the separate panels and menu options.
 */

/********************************
* Lawrence Chu [chu22]
* Kevin Tang [ktang20]
* U. of Illinois at Chicago
* CS342 - Project 2 (Minesweeper)
*********************************/

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class Minesweeper extends JFrame {
	private Grid g;
	private TimePanel t;
	private FlagPanel f;
	private ResetPanel r;
	private Menu m;
	private HighScores h;
	
	private Timer time;
	private boolean firstClick;
	
	JTable scoresTable;
	JScrollPane scoresPane;
	
	private final int FRAME_WIDTH = 170;
	private final int FRAME_HEIGHT = 250;
	private final int DELAY = 1000;
	
	public Minesweeper(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle("Minesweeper");
		setLayout(null);
		setLocationRelativeTo(null);
		
		
		g = new Grid();
		t = new TimePanel();
		f = new FlagPanel();
		r = new ResetPanel();
		m = new Menu();
		h = new HighScores();
		time = new Timer(DELAY, new TimerHandler());
		scoresPane= new  JScrollPane();
		scoresTable = h.getTable();
		firstClick = true;
		
		
		g.addMouseListener(new gridHandler());
		r.addMouseListener(new ResetHandler());
		m.getItem(0).addActionListener(new resetMenuHandler());
		m.getItem(1).addActionListener(new scoresMenuHandler());
		m.getItem(2).addActionListener(new exitMenuHandler());
		m.getItem(3).addActionListener(new helpMenuHandler());
		m.getItem(4).addActionListener(new aboutMenuHandler());
		m.getItem(5).addActionListener(new resetScoresMenuHandler());
		
		scoresPane.setPreferredSize(new Dimension(300, 220));
		
		
		add(f);
		add(g);
		add(t);
		add(r);
		setJMenuBar(m);
		
		
		f.setBounds(0, 0, Digit.width*2, Digit.height);
		g.setBounds(0,ResetPanel.height + 5,Grid.width,Grid.height);
		t.setBounds(FRAME_WIDTH-Digit.width*4, 0, Digit.width*4, Digit.height);
		r.setBounds(FRAME_WIDTH/2-ResetPanel.width,0,ResetPanel.width,ResetPanel.height);
		
		setResizable(false);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Minesweeper window = new Minesweeper();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
	public void reset(){
		remove(f);
		remove(g);
		remove(t);
		time.stop();
		g = new Grid();
		t = new TimePanel();
		f = new FlagPanel();
		firstClick = true;
		g.addMouseListener(new gridHandler());
		add(f);
		add(g);
		add(t);
		f.setBounds(0, 0, Digit.width*2, Digit.height);
		g.setBounds(0,ResetPanel.height + 5,Grid.width,Grid.height);
		t.setBounds(FRAME_WIDTH-Digit.width*4, 0, Digit.width*4, Digit.height);
	}
	
	//Event Handlers
	
	
	private class gridHandler implements MouseListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			g.mousePressed(e);
			f.setCount(g.getnFlags());
			g.repaint();
			f.repaint();
			if(!g.gameOver()){
				r.gridPressed();
				r.repaint();
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			g.mouseReleased(e);
			g.repaint();
			if(firstClick&&!g.firstClicked()){
				time.start();
				firstClick = false;
			}
			if(g.gameOver()){
				time.stop();
				f.setCount(g.getnFlags());
				f.repaint();
				if(g.gameWon()){
					r.gameWon();
					Score s = new Score(t.getTime());
					if(h.isTopTen(s)){
						String name = (String)JOptionPane.showInputDialog(
			                    Minesweeper.this,
			                    "Congratualations! You have a new high score!\n"
			                    + "Enter your name:",
			                    "New High Score", JOptionPane.PLAIN_MESSAGE, null, null, null);
						if(name!=null){
							s.addName(name);
							h.addScore(s);
						}
						
						
					}
				}
				else{
					r.gameLost();
				}
			}
			else{
				r.mouseReleased();
			}
			r.repaint();
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class ResetHandler implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			r.resetPressed();
			r.repaint();
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			r.mouseReleased();
			r.repaint();
			reset();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class TimerHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			t.incTime();
			t.repaint();
			
		}
		
	}
	
	private class resetMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			r.mouseReleased();
			reset();
		}
		
	}
	
	private class scoresMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			scoresPane.getViewport().remove( scoresTable );
			scoresTable = h.getTable();
			scoresPane.getViewport().add( scoresTable );
			
			JOptionPane.showMessageDialog(Minesweeper.this,
				    scoresPane,
				    "High Scores",
				    JOptionPane.PLAIN_MESSAGE);
			
			
		}
		
	}
	
	private class resetScoresMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			h.resetScoreFile();
		}
		
	}
	
	private class exitMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}

	 public String printHelp(){
		   String str = "MINESWEEPER: HOW TO PLAY\n\n" +
		     "The Objective:\n" +
		     "    - The goal of the game is to reveal all empty squares while avoiding the mines.\n" +
		     "    - The faster you are able to do so, the better your score will be.\n\n" +
		     
		     "The Board:\n" +
		     "    - The number on the top left indicates the number valid markers available.\n" +
		     "    - The Smiley Face is a 'New Game' button.\n" +
		     "    - The number on the top right indicates the amount of time that has passed.\n" +
		     "    - There are 100 tiles and 10 mines in total.\n\n" +
		     
		     "Controls:\n" +
		     "    - Left clicking on a tile will reveal either a number(s) or a mine.\n" +
		     "         - If a mine is uncovered, you lose and the game is over.\n" +
		     "         - If a number is revealed, it indicates the number of mines there are in the surrounding 8 tiles.\n" +
		     "    - Right clicking on a blank, unrevealed tile will mark the mine with a flag. This should be done when\n" +
		     "      you suspect a mine is under the tile.\n" + 
		     "         - If you are unsure, right clicking a marked tile will turn it into a question mark.\n" +
		     "         - Note that there can be as many flags on the board as there are tiles, but the game will only finish\n" +
		     "           if there are 10 or less flags on the board (having more than 10 means you marked a tile with no mine).\n" +
		     "     - Clicking the Smiley Face with any mouse button will start a new game.\n";
		   return str;
		 }

	
	private class helpMenuHandler implements ActionListener{

		@Override
		  public void actionPerformed(ActionEvent e) {
		   String p = printHelp();
		   JOptionPane.showMessageDialog(Minesweeper.this,
		        p,
		        "Help",
		        JOptionPane.PLAIN_MESSAGE);
		  }

		
	}
	private class aboutMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(Minesweeper.this,
					 "MINESWEEPER [U. of Illinois at Chicago: CS342 - Project2]\n" +
						        "Version 1.0 (2/21/2016)\n" +
						        "Developed by :\n    Lawrence Chu [chu22@uic.edu]\n    Kevin Tang [ktang20@uic.edu]",
						        "About",
						        JOptionPane.PLAIN_MESSAGE);
		
		}

	}
}
