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
	private ResetPanel r;
	
	private Timer time;
	private boolean firstClick;
	
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
		time = new Timer(DELAY, new TimerHandler());
		firstClick = true;
		g.addMouseListener(new gridHandler());
		r.addMouseListener(new ResetHandler());
		add(f);
		add(g);
		add(t);
		add(r);
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
	
	
	private class gridHandler implements MouseListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			g.mousePressed(e);
			int n = g.getnFlags();
			f.setCount(n);
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
				if(g.gameWon()){
					r.gameWon();
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

}
