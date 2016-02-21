import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable{

	private int score;
	private String name;
	
	private static final long serialVersionUID = 1L;
	
	public Score(int x){
		score = x;
		name = "Anonymous";
	}
	
	public void addName(String n){
		name = n;
	}
	
	public boolean lessThan(Score s){
		return score<s.score;
	}
	
	public boolean greaterThan(Score s){
		return score>s.score;
	}
	
	public String getName(){
		return name;
	}
	
	public int getScore(){
		return score;
	}

	@Override
	public int compareTo(Score o) {
		return score - o.score;
	}
}
