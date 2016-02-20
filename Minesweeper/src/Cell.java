import java.awt.Image;
import javax.swing.ImageIcon;

public class Cell {
	private int r; //row
	private int c; //col
	private int val; //number of adjacent mines, or -1 if it is a mine
	private int state; 
	public static int count = 0;
	private Image label;
	
	private static String val_button_names[] = {"button_pressed_v.gif", "button_1.gif", "button_2.gif", "button_3.gif", "button_4.gif", 
			"button_5.gif", "button_6.gif", "button_7.gif", "button_8.gif"};	
	private static final Image NORM =  new ImageIcon("button_normal.gif").getImage();                         //state 0
	private static final Image NORM_P = new ImageIcon("button_pressed.gif").getImage();						//state 1
	private static final Image FLAG = new ImageIcon("button_flag.gif").getImage();							//state 2
	private static final Image QUESTION = new ImageIcon("button_question.gif").getImage();					//state 3
	private static final Image QUESTION_P = new ImageIcon("button_question_pressed.gif").getImage();			//state 4

	private Image BOMB;																					//state 5
	private Image VAL;																					//state 6
	
	public static final int height = 16;
	public static final int width = 16;
	
	public Cell(int row, int col){
		r = row;
		c = col;
		val = -2;  //initializing to -2 just to mark that the square value hasn't been calculated yet 
		label = NORM;
		state = 0;
	}

	public void setVal(int v){
		val = v;
		if(val>-1&&val<9){
			ImageIcon i6 = new ImageIcon("button_bomb_x.gif");
			BOMB = i6.getImage();
			ImageIcon i7 = new ImageIcon(val_button_names[val]);
			VAL = i7.getImage();
		}
		else if(val==-1){
			ImageIcon i6 = new ImageIcon("button_bomb_pressed.gif");
			BOMB = i6.getImage();
			ImageIcon i7 = new ImageIcon("button_bomb_blown.gif");
			VAL = i7.getImage();
		}
	}
	
	public boolean isAdjacent(int row, int col){
		if(row<0||row<r-1||row>r+1||row>9){
			return false;
		}
		if(col<0||col<c-1||col>c+1||col>9){
			return false;
		}
		if(row==r&&col==c){
			return false;
		}
		return true;
	}
	
	public boolean isBomb(){
		return val==-1;
	}
	
	public boolean isZero(){
		return val==0;
	}
	
	public void LPressed(){
		if(label==NORM){
			label = NORM_P;
			state = 1;
		}
		else if(label==QUESTION){
			label = QUESTION_P;
			state = 4;
		}
	}
	
	//change label based on left click
	public void LReleasedSame(){
		if(label==QUESTION_P){
			label = QUESTION;
			state = 3;
		}
		else if(label==NORM_P){
				label = VAL;
				state = 6;
				count++;
		}
	}
	
	public void LReleasedOther(){
		if(label==QUESTION_P){
			label = QUESTION;
			state = 3;
		}
		else if(label==NORM_P){
			label = NORM;
			state = 0;
		}
		
	}
	
	public void RPressed(){
		if(label == NORM){
			label = FLAG;
			state = 2;
		}
		else if(label == FLAG){
			label = QUESTION;
			state = 3;
		}
		else if(label == QUESTION){
			label = NORM;
			state = 0;
		}
	}
	
	//change label to flagged, this method will only be called by the markBombs method, which is only called when the user wins the game.
	public void flagBomb(){
		label = FLAG;
	}
	
	public void showBomb(){
		if(val==-1&&label!=FLAG){
			label = BOMB;
		}
		if(val!=-1&&label==FLAG){
			label = BOMB;
		}
	}
	
	public void showVal(){
		label = VAL;
		state = 6;
		count++;
	}
	
	public int getState(){
		return state;
	}
	
	public Image getImage(){
		return label;
	}
}
