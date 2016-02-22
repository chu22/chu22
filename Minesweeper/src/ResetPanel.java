/* 
 * ResetPanel.java 
 * 
 * This class is the panel that contains the reset/smiley button
 * 
*/

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ResetPanel extends JPanel{
	
	/* Image names */
	private static String smiley_names[] = {"smile_button.gif", "smile_button_pressed.gif", "head_o.gif", "head_glasses.gif", "head_dead.gif"};
	
	/* Smiley dimensions */
	public static final int height = 26;
	public static final int width = 26;
	
	/* Images */
	private static final Image SMILE =  new ImageIcon(smiley_names[0]).getImage();		//default smiley                  
	private static final Image SMILE_P = new ImageIcon(smiley_names[1]).getImage();		//pressed down smiley - set when mouse button is pressed on the reset button		
	private static final Image HEAD_O = new ImageIcon(smiley_names[2]).getImage();		//surprised face - set when a mouse button is pressed down on grid
	private static final Image WIN = new ImageIcon(smiley_names[3]).getImage();			//sunglasses face - set when game won
	private static final Image LOSE = new ImageIcon(smiley_names[4]).getImage();		//dead face - set when game lost
	
	private Image label;	//current image
	
	public ResetPanel(){
		label = SMILE;
	}
	
	public void resetPressed() {
		label = SMILE_P;
		
	}

	public void mouseReleased() {
		label = SMILE;
	}
	
	public void gridPressed(){
		label = HEAD_O;
	}
	
	public void gameWon(){
		label = WIN;
	}
	
	public void gameLost(){
		label = LOSE;
	}
	
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(label, 0, 0, null);
	}
	
}
