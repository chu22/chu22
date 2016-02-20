import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JPanel;

public class Grid extends JPanel{
	private Cell grid[][];
	private Cell bombs[];
	private int clicked;  //number of valid Cells clicked so far, game ends if this reaches 90
	private int nFlags;   //number for how many marked mines there are, will be used for the bomb display GUI element
	private boolean explode; //boolean for whether an exploded bomb was clicked or not
	private boolean firstClick;
	
	private int lastPressedr;
	private int lastPressedc;
	
	private final int CELL_DIM = 16;
	private final int N_ROWS = 10;
	private final int N_COLS = 10;
	private final int N_BOMBS = 10;
	
	public static final int height = 160;
	public static final int width = 160;
	
	public Grid(){
		grid = new Cell[N_ROWS][N_COLS];
		bombs = new Cell[N_BOMBS];
		clicked = 0;
		nFlags = 10;
		explode = false;
		firstClick = true;
		for(int i = 0;i<N_ROWS;i++){
			for(int j = 0;j<N_COLS;j++){
				grid[i][j]= new Cell(i,j);
			}
		}
		
	}
	//next 3 methods will set up the board for a new game
	
	private void init_board(int r, int c){
		setBombs(r,c);
		setBoard();
	}
	
	private void setBombs(int row, int col){
		int r;
		int c;
		int ctr = 0;
		Random rn = new Random();
		while(ctr<N_BOMBS){
			r = rn.nextInt(N_BOMBS);
			c = rn.nextInt(N_BOMBS);
			if(r!=row&&c!=col&&!grid[r][c].isBomb()){
				grid[r][c].setVal(-1);
				bombs[ctr]=grid[r][c];
				ctr++;
			}
		}
		
	}
	
	private void setBoard(){
		for(int r = 0;r<N_ROWS;r++){
			for(int c = 0;c<N_COLS;c++){
				if(!grid[r][c].isBomb()){
					grid[r][c].setVal(calcVal(r,c));
				}
			}
		}
	}
	
	private int calcVal(int r, int c){
		int v=0;
		for(int i = r-1;i<=r+1;i++){             //using a 3x3 for loop to check adjacency. Inefficient, but a lot less code clutter
			for(int j = c-1;j<=c+1;j++){
				if(grid[r][c].isAdjacent(i,j)&&grid[i][j].isBomb()){
					v++;
				}
			}
		}
		return v;
	}
	
	@Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                g.drawImage(grid[i][j].getImage(), j*CELL_DIM,
                    i*CELL_DIM, null);
            }
        }
    }
	
	public void mousePressed(MouseEvent e) {
		int c = convertPos(e.getX());
		int r = convertPos(e.getY());
		if(e.getButton() == MouseEvent.BUTTON1)
	    {
			if(!explode&&clicked<90&&validCoord(r,c)){
					lastPressedr = r;
					lastPressedc = c;
					grid[r][c].LPressed();
			}
	    }	    
	    else if(e.getButton() == MouseEvent.BUTTON3)
	    {
	    	if(!explode&&clicked<90&&validCoord(r,c)){
				Cell s = grid[r][c];
				s.RPressed();
				if(s.getState()==2){  //square was changed from blank to M, decrement flag ctr
					nFlags--;        
				}
				else if(s.getState()==3){  //square was changed from M to ?, increment flag ctr 
					nFlags++;
				}
	    	}
	    }
	}

	public void mouseReleased(MouseEvent e) {
		int r, c;
		if(e.getButton() == MouseEvent.BUTTON1)
	    {
			c = convertPos(e.getX());
			r = convertPos(e.getY());
			boolean over = lastPressedr==r&&lastPressedc==c;
			if(!explode&&clicked<90){   //will only work if the game hasn't ended, otherwise does nothing.
				if(over&&grid[r][c].getState()!=6){
					if(firstClick){
						init_board(r,c);
						firstClick = false;
					}
					Cell s = grid[r][c];
					s.LReleasedSame();
					if(s.getState()==6){
						if(s.isBomb()){
							explode = true;
							showBombs();
						}
						else{
							if(s.isZero()){          //if square has no adjacent mines, automatically and recursively click on each adjacent square
								for(int i = r-1;i<=r+1;i++){
									for(int j = c-1;j<=c+1;j++){
										if(grid[r][c].isAdjacent(i,j)){
											showAdj(i,j);
										}
									}
								}
							}
							clicked++;
							System.out.println("clicked: " + clicked);
							System.out.println("count: " + Cell.count);
							if(clicked==90){
								markBombs();
							}
						}
					}
				}
				else{
					grid[lastPressedr][lastPressedc].LReleasedOther();
				}
			}
		}
	}
	
	private void showAdj(int r, int c){
		Cell s = grid[r][c];
		if(s.getState()==0){
			s.showVal();
			clicked++;
			System.out.println("clicked: " + clicked);
			System.out.println("count: " + Cell.count);
			if(s.isZero()){
				for(int i = r-1;i<=r+1;i++){
					for(int j = c-1;j<=c+1;j++){
						if(grid[r][c].isAdjacent(i,j)){
							showAdj(i,j);
						}
					}
				}
			}
		}
	}
	
	
	//shows all the bombs on the grid, which happens when user loses by clicking on a bomb 
	private void showBombs(){
		for(int r = 0;r<N_ROWS;r++){
			for(int c = 0;c<N_COLS;c++){
				if(grid[r][c].getState()!=6){
					grid[r][c].showBomb();
				}
			}
		}
	}
	//automatically marks all the bombs, which happens when the user wins by clicking on all the valid squares
	private void markBombs(){
		for(int i = 0;i<N_BOMBS;i++){
			if(bombs[i].getState()!=2){
				nFlags--;
			}
			bombs[i].flagBomb();
		}
	}
	
	public int getState(int r, int c){
		return grid[r][c].getState();
	}
	
	public int getnFlags(){
		return nFlags;
	}
	
	public boolean firstClicked(){
		return firstClick;
	}
	
	public boolean gameOver(){
		return clicked==90||explode;
	}
	
	public boolean gameWon(){
		return clicked==90;
	}
	
	private int convertPos(int x){
		return x/CELL_DIM;
	}
	
	private boolean validCoord(int r, int c){
		return r<10&&r>=0&&c<10&&c>=0;
	}

}
