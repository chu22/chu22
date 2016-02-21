import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class HighScores {
	
	private ArrayList<Score> scores;
	private JTable scoreTable;
	private DefaultTableModel model;
	
	private final int MAX = 10;
	private final String[] COL_NAMES = {"  ","Name", "Score"};
	
	private static final String SCORE_FILE = "scores.dat";
	 
	ObjectOutputStream out = null;
	ObjectInputStream in = null;
	
	public HighScores(){
		scores = new ArrayList<Score>(MAX);
		scoreTable = new JTable();
		loadScoreFile();
		updateTable();
	}
	
	public boolean isTopTen(Score s){
		return scores.size()<MAX||s.greaterThan(scores.get(scores.size()-1));
	}
	
	public void addScore(Score s){	
		loadScoreFile();
		if(scores.size()<MAX){
			scores.add(s);
		}
		else{
			scores.remove(scores.size()-1);
			scores.add(s);
		}
		Collections.sort(scores);
		updateScoreFile();
	}
	
	private void loadScoreFile() {
        try {
            in = new ObjectInputStream(new FileInputStream(SCORE_FILE));
            scores = (ArrayList<Score>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("FNF Error: " + e.getMessage());
            
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("CNF Error: " + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println("IO Error: " + e.getMessage());
            }
        }
	}
	
	private void updateScoreFile() {
        try {
            out = new ObjectOutputStream(new FileOutputStream(SCORE_FILE));
            out.writeObject(scores);
            updateTable();
        } catch (FileNotFoundException e) {
            System.out.println("FNF Error: " + e.getMessage() + ",the program will try and make a new file");
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
	}
	
	public void resetScoreFile(){
		scores = new ArrayList<Score>(MAX);
		updateScoreFile();
	}
	
	
	public String printScores(){
		String str = "     Name\t\tScore\n";
		int ctr = 1;
		for(Score s : scores){
			str = str + ctr + ". " + s.getName() + "\t\t" + s.getScore() + "\n";
			ctr++;
		}
		while(ctr<10){
			str = str + ctr +".\n";
			ctr++;
		}
		return str;
	}
	
	private void updateTable(){
		String[][] scoreGrid = new String[10][3];
		int ctr = 0;
		for(Score s : scores){
			scoreGrid[ctr][0] = ctr+1 + ".";
			scoreGrid[ctr][1] = s.getName();
			scoreGrid[ctr][2] = s.getScore() + "";
			ctr++;
		}
		while(ctr<10){
			scoreGrid[ctr][0] = ctr+1 + ".";
			scoreGrid[ctr][1] = "";
			scoreGrid[ctr][2] = "";
			ctr++;
		}
		model = new DefaultTableModel(scoreGrid, COL_NAMES);
		scoreTable.setModel(model);
		scoreTable.getColumnModel().getColumn(0).setPreferredWidth(25);
		scoreTable.getColumnModel().getColumn(1).setPreferredWidth(235);
		scoreTable.getColumnModel().getColumn(2).setPreferredWidth(40);
	}
	
	public JTable getTable(){
		return scoreTable;
	}
	
}
