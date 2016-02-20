import java.awt.Image;

import javax.swing.ImageIcon;

public class Digit {
	public static Image[] d;
	public static final int height = 23;
	public static final int width = 13;
	private static String counter_names[] = {"countdown_0.gif", "countdown_1.gif", "countdown_2.gif", "countdown_3.gif", "countdown_4.gif",
			"countdown_5.gif", "countdown_6.gif", "countdown_7.gif", "countdown_8.gif", "countdown_9.gif"};

	
	static {
		d = new Image[10];
		for(int i = 0;i<10;i++){
			d[i] = new ImageIcon(counter_names[i]).getImage();
		}
	}
}
