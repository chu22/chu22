import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class TimePanel extends JPanel{
	private Image hund;
	private Image tens;
	private Image ones;
	
	private int dh;
	private int dt;
	private int d1;
	

	
	public TimePanel(){
		hund = Digit.d[0];
		tens = Digit.d[0];
		ones = Digit.d[0];
		dh = 0;
		dt = 0;
		d1 = 0;
		
	}
	
	public void incTime(){
		if(d1==9){
			if(dt==9){
				if(dh==9){
					return;
				}
				d1 = 0;
				dt = 0;
				dh++;
				setTime(dh,dt,d1);
				return;
			}
			d1 = 0;
			dt++;
			setTime(dh,dt,d1);
			return;
		}
		d1++;
		setTime(dh,dt,d1);
	}
	
	private void setTime(int h, int t, int o){
		hund = Digit.d[h];
		tens = Digit.d[t];
		ones = Digit.d[o];
	}
	
	public int getTime(){
		return 100*dh+10*dt+d1;
	}
	
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(hund, 0, 0, null);
		g.drawImage(tens, Digit.width, 0, null);
		g.drawImage(ones, 2*Digit.width, 0, null);
	}
}
