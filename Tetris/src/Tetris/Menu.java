/*
 * 
 * Menu.java
 * This simple class contains the GUI elements for the menu
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar{

	private JMenu Game;
	private JMenuItem[] items; //{reset,scores,exit,help,about}
	
	public Menu(){
		Game = new JMenu("Game");
		Game.setMnemonic(KeyEvent.VK_G);
		add(Game);
		items = new JMenuItem[4];
		items[0] = new JMenuItem("Start/Reset", KeyEvent.VK_R);
		Game.add(items[0]);
		items[1] = new JMenuItem("Exit", KeyEvent.VK_X);
		Game.add(items[1]);
		items[2] = new JMenuItem("Help", KeyEvent.VK_H);
		Game.add(items[2]);
		items[3] = new JMenuItem("About", KeyEvent.VK_A);
		Game.add(items[3]);
	}
	
	public JMenuItem getItem(int i){
		return items[i];
	}	
}
