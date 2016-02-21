import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Minesweeper extends JFrame {
	private Grid g;
	private TimePanel t;
	private FlagPanel f;
	private ResetPanel r;
	private Menu m;
	private HighScores h;
	
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
		m = new Menu();
		h = new HighScores();
		time = new Timer(DELAY, new TimerHandler());
		firstClick = true;
		
		
		g.addMouseListener(new gridHandler());
		r.addMouseListener(new ResetHandler());
		m.getItem(0).addActionListener(new resetMenuHandler());
		m.getItem(1).addActionListener(new scoresMenuHandler());
		m.getItem(2).addActionListener(new exitMenuHandler());
		m.getItem(3).addActionListener(new helpMenuHandler());
		m.getItem(4).addActionListener(new aboutMenuHandler());
		m.getItem(5).addActionListener(new resetScoresMenuHandler());
		
		
		add(f);
		add(g);
		add(t);
		add(r);
		setJMenuBar(m);
		h.loadScoreFile();
		
		
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
	
	//Event Handlers
	
	
	private class gridHandler implements MouseListener{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			g.mousePressed(e);
			f.setCount(g.getnFlags());
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
				f.setCount(g.getnFlags());
				f.repaint();
				if(g.gameWon()){
					r.gameWon();
					Score s = new Score(t.getTime());
					if(h.isTopTen(s)){
						String name = (String)JOptionPane.showInputDialog(
			                    Minesweeper.this,
			                    "Congratualations! You have a new high score!\n"
			                    + "Enter your name:",
			                    "New High Score", JOptionPane.PLAIN_MESSAGE, null, null, null);
						if(name!=null){
							s.addName(name);
							h.addScore(s);
						}
						
						
					}
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
	
	private class resetMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			reset();
		}
		
	}
	
	private class scoresMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String p = h.printScores();
			JOptionPane.showMessageDialog(Minesweeper.this,
				    p,
				    "High Scores",
				    JOptionPane.PLAIN_MESSAGE);
			
			
		}
		
	}
	
	private class resetScoresMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			h.resetScoreFile();
		}
		
	}
	
	private class exitMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//saveScores();
			System.exit(0);
		}
		
	}
	private class helpMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(Minesweeper.this,
				    "Insert help info",
				    "Help",
				    JOptionPane.PLAIN_MESSAGE);
		}
		
	}
	private class aboutMenuHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(Minesweeper.this,
				    "Insert about info",
				    "About",
				    JOptionPane.PLAIN_MESSAGE);
		}
		
	}

}
