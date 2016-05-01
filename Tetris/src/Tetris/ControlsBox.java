/*
 * 
 * ControlsBox.java
 * This simple class contains the GUI elements for the button controls
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlsBox extends JPanel{
	private JButton softDrop;
	private JButton hardDrop;
	private JButton left;
	private JButton right;
	private JButton rotateR;
	private JButton rotateL;
	
	public ControlsBox(){
		softDrop = new JButton("Soft Drop");
		hardDrop = new JButton("Hard Drop");
		left = new JButton("Move Left");
		right = new JButton("Move Right");
		rotateR = new JButton("Rotate Right");
		rotateL = new JButton("Rotate Left");
		
		add(softDrop);
		add(hardDrop);
		add(left);
		add(right);
		add(rotateR);
		add(rotateL);
		
		setBounds(220,170,250,200);
	}
	
	public JButton[] getButtons(){
		JButton[] buttons = {softDrop,hardDrop,left,right,rotateR,rotateL};
		return buttons;
	}
}
