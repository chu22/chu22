/* 
 * FlagPanel.java 
 * 
 * This class is the panel that shows the number of flags "available" in the game
 * Due to lack of a suitable sprite image, the counter will stop at zero
 * even if the "available" flags is negative
 * 
 */

/********************************
* Lawrence Chu [chu22]
* Kevin Tang [ktang20]
* U. of Illinois at Chicago
* CS342 - Project 2 (Minesweeper)
*********************************/

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

public class FlagPanel extends JPanel{
	private Image tens;
	private Image ones;
	
	public FlagPanel(){
		tens = Digit.d[1];
		ones = Digit.d[0];
	}
	
	public void setCount(int n){
		if(n<=0){				//if less than 0, will display 0, since we do not have a negative symbol sprite
			ones = Digit.d[0];
			tens = Digit.d[0];
		}
		else if(n==10){
			tens = Digit.d[1];
			ones = Digit.d[0];
		}
		else{
			tens = Digit.d[0];
			ones = Digit.d[n];
		}
	}

	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(tens, 0, 0, null);
		g.drawImage(ones, Digit.width, 0, null);
	}
}
