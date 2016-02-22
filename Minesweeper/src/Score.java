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
	
	private static final long serialVersionUID = 1L;  //allows Object Input/Output streams to read/write object data
	
	/* CONSTRUCTOR/SETTERS/GETTERS */
	
	public Score(int x){
		score = x;
	}
	
	public void addName(String n){
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	public int getScore(){
		return score;
	}

	/* COMPARISON FUNCTIONS */
	
	public boolean lessThan(Score s){
		return score<s.score;
	}
	
	@Override
	public int compareTo(Score o) {
		return score - o.score;
	}
}
