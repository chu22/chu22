/* 
 * Score.java 
 *
 * This class contains the name and score of a particular game
 * It implements serializable and comparable in order to function
 * with object read/write streams and built in sorting functions
 * 
 */

/********************************
* Lawrence Chu [chu22]
* Kevin Tang [ktang20]
* U. of Illinois at Chicago
* CS342 - Project 2 (Minesweeper)
*********************************/

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable{

	private int score;
	private String name;
	
	private static final long serialVersionUID = 1L;
	
	public Score(int x){
		score = x;
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
