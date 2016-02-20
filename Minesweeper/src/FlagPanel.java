import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class FlagPanel extends JPanel{
	private Image tens;
	private Image ones;
	
	public FlagPanel(){
		tens = Digit.d[1];
		ones = Digit.d[0];
	}
	
	public void setCount(int n){
		if(n<=0){
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
