this project uses two types of design patterns: the factory pattern, 
which is used to create the 7 types of tetronimoes that extend the base class,
and the signleton pattern, which is used 8 times to store each of the images
required for the game.

The factory pattern is implemented in the TetronimoFactory.java class, which has
the static method, getTetronimo, that chooses which tetronimo type to create 
based on an input int:

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

The singleton pattern is implemented in the cellCol enum, as enums extend very
naturally to the singleton pattern. There are 8 enums, 

	BLACK(Images.BLACK), CYAN(Images.CYAN), BLUE(Images.BLUE), ORANGE(Images.ORANGE), YELLOW(Images.YELLOW), GREEN(Images.GREEN), PURPLE(Images.PURPLE), RED(Images.RED); 

each of which can be considered as an implementation of the singleton pattern. Each enum
stores an Image through a private constructor,

	private Image color;
	
	private cellCol(Image c){
		 color = c;
	}

and once it is created, all enums by definition will instantiate the object only once, which
fits the signleton design pattern. This makes sense for this project since each of the
images used in this project only needs one instance; a new image does not need to be created
just to draw the same image in 2 different locations.

The getImage() function provides the getInstance() functionality for the singleton:

	public Image getImage(){
		return color;
	}