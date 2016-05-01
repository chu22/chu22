/*
 * 
 * Score.java
 * This simple class contains the GUI elements for displaying the score
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScoreBox extends JPanel{
	private JLabel score;
	private JLabel label;
	
	public ScoreBox(){
		score = new JLabel("0");
		label = new JLabel("Score:");
		add(label);
		add(score);
		setBounds(220,120,40,40);
	}
	
	public void setScore(int s){
		score.setText(s + "");
		repaint();
	}
}
