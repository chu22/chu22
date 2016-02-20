import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Minesweeper extends JFrame {
	private Grid g;
	private TimePanel t;
	private FlagPanel f;
	
	private Timer time;
	private boolean firstPress;
	
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
		time = new Timer(DELAY, new TimerHandler());
		firstPress = true;
		g.addMouseListener(new gridHandler());
		add(f);
		add(g);
		add(t);
		f.setBounds(0, 0, Digit.width*2, Digit.height);
		g.setBounds(0,Digit.height,170,170);
		t.setBounds(FRAME_WIDTH-Digit.width*4, 0, Digit.width*4, Digit.height);
		
		
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
		g = new Grid();
		t = new TimePanel();
		f = new FlagPanel();
		time = new Timer(DELAY, new TimerHandler());
		firstPress = true;
		g.addMouseListener(new gridHandler());
		add(f);
		add(g);
		add(t);
		f.setBounds(0, 0, Digit.width*2, Digit.height);
		g.setBounds(0,Digit.height,170,170);
		t.setBounds(FRAME_WIDTH-Digit.width*4, 0, Digit.width*4, Digit.height);
	}
	
	
	private class gridHandler implements MouseListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(firstPress){
				time.start();
				firstPress = false;
			}
			g.mousePressed(e);
			int n = g.getnFlags();
			f.setCount(n);
			g.repaint();
			f.repaint();
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			g.mouseReleased(e);
			g.repaint();
			if(g.gameOver()){
				time.stop();
			}
			
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

}
