/*
 * 
 * TetronimoFactory.java
 * This simple class creates a tetronimo type based on the integer input.
 * It is an implementation fo the factory design pattern.
 * 
 * Written by Lawrence Chu, Grieldo Lulaj and Daia Elsalaymeh
 * 
 */
package Tetris;

public class TetronimoFactory {

	public static Tetronimo getTetronimo(int t){
		Tetronimo tet;
		switch(t){
		case 0:
			tet = new ITetronimo(new Point(0,1));
			break;
		case 1:
			tet = new JTetronimo(new Point(1,1));
			break;
		case 2:
			tet = new LTetronimo(new Point(1,1));
			break;
		case 3:
			tet = new OTetronimo(new Point(1,1));
			break;
		case 4:
			tet = new STetronimo(new Point(1,1));
			break;
		case 5:
			tet = new TTetronimo(new Point(1,1));
			break;
		case 6:
			tet = new ZTetronimo(new Point(1,1));
			break;
		default:
			tet = null;
			break;
		}
		return tet;
	}
}
