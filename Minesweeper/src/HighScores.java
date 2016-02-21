import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HighScores {
	
	private ArrayList<Score> scores;
	
	private final int MAX = 10;
	
	 private static final String SCORE_FILE = "scores.dat";
	 
	 ObjectOutputStream out = null;
	 ObjectInputStream in = null;
	
	public HighScores(){
		scores = new ArrayList<Score>(MAX);
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
	
	public void loadScoreFile() {
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
	
	public void updateScoreFile() {
        try {
            out = new ObjectOutputStream(new FileOutputStream(SCORE_FILE));
            out.writeObject(scores);
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
		String str = "     Name                       Score\n";
		int ctr = 1;
		for(Score s : scores){
			str = str + ctr + ". " + s.getName() + "                " + s.getScore() + "\n";
			ctr++;
		}
		while(ctr<10){
			str = str + ctr +".\n";
			ctr++;
		}
		return str;
	}
	
}
