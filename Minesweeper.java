import java.io.File;
import java.util.Scanner;
import java.util.Random;
import java.io.FileNotFoundException;

public class Minesweeper {

	Scanner keyboard = new Scanner(System.in);
	Scanner seedScanner;
	Random random = new Random();
	int rows, cols, roundsCompleted = 0, mines = 0, minesFound = 0, score = 0, flagsLeft = 0, marksPlaced = 0;
	int c1 = 0, c2 = 0, c3 = 0, c4 = 0, c5 = 0, c6 = 0, c7 = 0, c8 = 0;
	String command = "";
	String[][] grid;
	boolean[][] mineGrid;
	File seedFile, file;
    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * information provided in <code>seedFile</code>. 
     *
     * @param seedFile the seed file used to construct the game
     */
    public Minesweeper(File seedFile) {
    	this.seedFile = seedFile;
    	int rows, cols, mines;
    	try {
    		Scanner seedScanner = new Scanner(seedFile);
    		this.rows = seedScanner.nextInt();
    		this.cols = seedScanner.nextInt();
    		this.mines = seedScanner.nextInt();
    		this.grid = new String[this.rows][this.cols + 1];
    		this.mineGrid = new boolean[this.rows][this.cols];
    		while (seedScanner.hasNextInt()) {
    			this.c1 = seedScanner.nextInt();
    			this.c2 = seedScanner.nextInt();
    			this.c3 = seedScanner.nextInt();
    			this.c4 = seedScanner.nextInt();
    			this.c5 = seedScanner.nextInt();
    			this.c6 = seedScanner.nextInt();
    			this.c7 = seedScanner.nextInt();
    			this.c8 = seedScanner.nextInt();

    		}

    	}
    	catch (FileNotFoundException nfe) {
    		System.out.println("Cannot create game with FILENAME, because it is not formatted correctly.");
    	}
    	
    } // Minesweeper


    /**
     * Constructs an object instance of the {@link Minesweeper} class using the
     * <code>rows</code> and <code>cols</code> values as the game grid's number
     * of rows and columns respectively. Additionally, One third (rounded up) 
     * of the squares in the grid will be assigned mines, randomly.
     *
     * @param rows the number of rows in the game grid
     * @param cols the number of cols in the game grid
     */
    public Minesweeper(int rows, int cols) {
    	this.rows = rows;
    	this.cols = cols;
       	this.grid = new String[this.rows][this.cols + 1]; //the String array that is displayed in the game
    	this.mineGrid = new boolean[this.rows][this.cols]; //an array where the mines are placed randomly; not shown to the player
    } // Minesweeper
    

    /**
     * Starts the game and execute the game loop.
     */
    public void run() {
    	System.out.println("        _");
    	System.out.println("  /\\/\\ (_)_ __   ___  ______      _____  ___ _ ___   ___ _ __");
		System.out.println(" /    \\| | '_ \\ / _ \\/  __\\ \\ /\\ / / _ \\/ _ \\ '   \\ / _ \\ '__|");
		System.out.println("/ /\\/\\ \\ | | | |  __/\\__  \\\\ v  v /  __/  __/ |_)  |  __/ |");
		System.out.println("\\/    \\/_|_| |_|\\___||____/ \\_/\\_/ \\___|\\___| -___/ \\___|_|");
		System.out.println("                                      ALPHA |_| EDITION");
		System.out.println();
		System.out.println();
		if ((rows > 10||rows < 0)||(cols > 10||cols < 0)) { //if the # of rows and columns are greater than 10 or less than 0, an error prints out and ends the game
    		System.out.println("Cannot create a mine field with that many rows and/or columns!");
    		System.exit(0);
    	}
		System.out.println();
		System.out.println("Rounds Completed: " + roundsCompleted);
    	System.out.println();
    	createField(grid, mineGrid);
    	if (!(seedFile == null)) {
    		addMinesSeed(grid, mineGrid);
    	}
    	else {
    		addMines(grid, mineGrid);
    	}
    	printField(grid, mineGrid);
    	System.out.println();
    	while (!(command.equalsIgnoreCase("q"))||!(command.equalsIgnoreCase("quit"))) { //while loop for the entire game
	    	askCommand();
	    	//if-else statement used for each command by the user
        	if (command.equalsIgnoreCase("h")||command.equalsIgnoreCase("help")) { //prints out the available commands to the user
        		System.out.println("Commands Available...");
        		System.out.println(" - Reveal: r/reveal row col");
        		System.out.println(" -   Mark: m/mark   row col");
        		System.out.println(" -  Guess: g/guess  row col");
        		System.out.println(" -   Help: h/help");
        		System.out.println(" -   Quit: q/quit");
        		System.out.println();
        		roundsCompleted++;
        		System.out.println("Rounds Completed: " + roundsCompleted);
        		System.out.println();
        		printField(grid, mineGrid);
        		System.out.println();
        		continue;
        	}
        	else if (command.equalsIgnoreCase("m")||command.equalsIgnoreCase("mark")) { //marks a place on the grid as definitely containing a mine
        		roundsCompleted++;
        		addMark(grid, mineGrid);
        		continue;
        	}
        	else if (command.equalsIgnoreCase("g")||command.equalsIgnoreCase("guess")) { //marks a place on the grid as possibly containing a mine
        		roundsCompleted++;
        		addFlag(grid, mineGrid);
        		continue;
        	}
        	else if (command.equalsIgnoreCase("r")||command.equalsIgnoreCase("reveal")) { //reveals a place on the grid
        		roundsCompleted++;
        		revealSquare(grid, mineGrid);
        		continue;
        	}
        	else if (command.equalsIgnoreCase("q")||command.equalsIgnoreCase("quit")) { //exits the game
        		System.out.println("Bye!");
        		System.exit(0);
        	}
        	else { //consumes one round and continues with the game
        		System.out.println("Command not recognized!");
        		System.out.println();
        		roundsCompleted++;
        		System.out.println("Rounds Completed: " + roundsCompleted);
        		printField(grid, mineGrid);
        		System.out.println();
        		continue;
        	}
		}
    	
    } // run

    /*
	 * creates the String array and the boolean array
	 * in the String array, each value is represented as "| "
	 * in the boolean array, each value is represented as "true" until
	 * mines are placed randomly, which replaces some "true" values as "false"
	 */
	public void createField(String[][] g, boolean[][] m) {
    	for (int row = 0; row < g.length; row++) {
    		for (int col = 0; col < g[row].length; col++) {
    			g[row][col] = "|  ";
    		}
    	}
    	for (int row = 0; row < m.length; row++) {
    		for (int col = 0; col < m[row].length; col++) {
    			m[row][col] = true;
    		}
    	}
	}
	/*
	 * prints the String array to the player
	 */
	public void printField(String[][] g, boolean[][] m) {
		int count = 0;
    	for (int row = 0; row < g.length; row++) {
    		System.out.print(count);
    		for (int col = 0; col < g[row].length; col++) {
    			System.out.print(g[row][col] + "  ");
    		}
    		System.out.println();
    		count++;
    	}        
    	String[] gridNumbers = new String[cols];
    	int count2 = 0;
    	for (int i = 0; i < gridNumbers.length; i++) {
    		gridNumbers[i] = count2 + "";
    		count2++;
    		System.out.print("    " + gridNumbers[i]);
    	}
    	System.out.println();
    	System.out.println();
	}
	/*
	 * prints "minesweeper-alpha$ " before every command
	 * the command variable uses the Scanner keyboard variable
	 */
	public void askCommand() {
		System.out.print("minesweeper-alpha$ ");
    	command = keyboard.next();
    	System.out.println();
	}
	/*
	 * method used when the user types in the "m/mark" command
	 * places a "F" on the designated coordinates
	 */
	public void addMark(String[][] g, boolean[][] m) {
		int guess1, guess2;
		guess1 = keyboard.nextInt();
		guess2 = keyboard.nextInt();
		if (guess1 < 0 || guess1 > rows || guess2 < 0 || guess2 > cols) {
			System.out.println("Command not recognized");
			System.out.println();
    		System.out.println("Rounds Completed: " + roundsCompleted);
    		printField(g, m);
    		System.out.println();
		}
		else {
			if (m[guess1][guess2] == false) {
				marksPlaced++;
				minesFound++;
			}
			if (m[guess1][guess2] == true)
				marksPlaced++;
			g[guess1][guess2] = "| F";
			System.out.println("Rounds Completed: " + roundsCompleted);
			if ((marksPlaced == minesFound) && (minesFound == mines) && (flagsLeft == 0)) {
				score = (rows * cols) - mines - roundsCompleted;
				System.out.println("CONGRATULATIONS!");
				System.out.println("YOU HAVE WON!");
				System.out.println("Score: " + score);
				System.exit(0);
			}
			printField(g, m);
			System.out.println();
		}
	}
	/*
	 * method used when the user types in the "g/guess" command
	 * places a "?" on the designated coordinates
	 */
	public void addFlag(String[][] g, boolean[][] m) {
		int guess1, guess2;
		guess1 = keyboard.nextInt();
		guess2 = keyboard.nextInt();
		if (guess1 < 0 || guess1 > rows || guess2 < 0 || guess2 > cols) {
			System.out.println("Command not recognized");
			System.out.println();
    		System.out.println("Rounds Completed: " + roundsCompleted);
    		printField(g, m);
    		System.out.println();
		}
		else {
			g[guess1][guess2] = "| ?";
			System.out.println("Rounds Completed: " + roundsCompleted);
			flagsLeft++;
			if ((marksPlaced == minesFound) && (minesFound == mines) && (flagsLeft == 0)) {
				score = (rows * cols) - mines - roundsCompleted;
				System.out.println("CONGRATULATIONS!");
				System.out.println("YOU HAVE WON!");
				System.out.println("Score: " + score);
				System.exit(0);
			}
			printField(g, m);
			System.out.println();
		}
	}
	/*
	 * reveals whether a spot on the grid is a mine or not
	 * if it's a mine, the "game over" sign is printed and the game exits
	 * if it's not a mine, then the number of mines around it is printed
	 */
	public void revealSquare(String[][] g, boolean[][] m) {
		int guess1, guess2;
		guess1 = keyboard.nextInt();
		guess2 = keyboard.nextInt();
		if (guess1 < 0 || guess1 > rows || guess2 < 0 || guess2 > cols) {
			System.out.println("Command not recognized");
			System.out.println();
    		System.out.println("Rounds Completed: " + roundsCompleted);
    		printField(g, m);
    		System.out.println();
		}
		else {
			if (m[guess1][guess2] == false) {
				System.out.println("Oh no.. You revealed a mine!");
				System.out.println();
				System.out.println("   __ _  __ _ _ __ ___   ___     _____   _____ _ __");
				System.out.println("  / _` |/  ` | '_ ` _  \\/ _ \\   / _ \\ \\ / / _ \\' __|");
				System.out.println(" | (_| | (_| | | | | | |  __/  | (_) \\ v /  __/ |");
				System.out.println("  \\__, |\\__,_|_| |_| |_|\\___|   \\___/ \\_/ \\___|_|");
				System.out.println("  |___/");
				System.exit(0);
			}
			else if (m[guess1][guess2] == true) {
				if (g[guess1][guess2].equals("| ?")) {
					flagsLeft--;
				}
				if (g[guess1][guess2].equals("| F")) {
					marksPlaced--;
				}
				int adjMines = 0;
				int n = guess1 - 1;
				int s = guess1 + 1;
				int e = guess2 - 1;
				int w = guess2 + 1;
				if (n >= 0 && m[n][guess2] == false)
					adjMines++;
				if (s < rows && m[s][guess2] == false)
					adjMines++;
				if (w >= 0 && w < cols && m[guess1][w] == false)
					adjMines++;
				if (e < cols && e >= 0 && m[guess1][e] == false)
					adjMines++;
				if (n >= 0 && w >= 0 && w < cols && m[n][w] == false)
					adjMines++;
				if (n >= 0 && e < cols && e >= 0 && m[n][e] == false)
					adjMines++;
				if (s < rows && e < cols && e >= 0 && m[s][e] == false)
					adjMines++;
				if (s < rows && w >= 0 && w < cols && m[s][w] == false)
					adjMines++;
				g[guess1][guess2] = "| " + adjMines;
				System.out.println("Rounds Completed: " + roundsCompleted);
				System.out.println();
				if ((marksPlaced == minesFound) && (minesFound == mines) && (flagsLeft == 0)) {
					score = (rows * cols) - mines - roundsCompleted;
					System.out.println("CONGRATULATIONS!");
					System.out.println("YOU HAVE WON!");
					System.out.println("Score: " + score);
					System.exit(0);
				}
				printField(g, m);
				System.out.println();
			}
		}
	}
	/*
	 * randomly places mines on the boolean array
	 * each mine is represented on the array as "false"
	 * approximately 1/3 of the array is "false"
	 */
	public void addMines(String[][] g, boolean[][] m) {
		int minesPlaced = 0;
		mines = (rows * cols) / 3;
		while (minesPlaced < mines) {
			int x = random.nextInt(cols);
			int y = random.nextInt(rows);
			if (m[y][x] != false) {
				m[y][x] = false;
				minesPlaced++;
			}
		}
	}
	
	public void addMinesSeed(String[][] g, boolean[][] m) {
		m[c1][c2] = false;
		m[c3][c4] = false;
		m[c5][c6] = false;
		m[c7][c8] = false;
	}
	
    /**
     * The entry point into the program. This main method does implement some
     * logic for handling command line arguments. If two integers are provided
     * as arguments, then a Minesweeper game is created and started with a 
     * grid size corresponding to the integers provided and with 1/3 (rounded
     * up) of the squares containing mines, placed randomly. If a single word 
     * string is provided as an argument then it is treated as a seed file and 
     * a Minesweeper game is created and started using the information contained
     * in the seed file. If none of the above applies, then a usage statement
     * is displayed and the program exits gracefully. 
     *
     * @param args the shell arguments provided to the program
     */
    public static void main(String[] args) {
	/*
	  The following switch statement has been designed in such a way that if
	  errors occur within the first two cases, the default case still gets
	  executed. This was accomplished by special placement of the break
	  statements.
	*/

	Minesweeper game = null;

	switch (args.length) {

        // random game
	case 2: 

	    int rows, cols;

	    // try to parse the arguments and create a game
	    try {
		rows = Integer.parseInt(args[0]);
		cols = Integer.parseInt(args[1]);
		game = new Minesweeper(rows, cols);
		break;
	    } catch (NumberFormatException nfe) { 
		// line intentionally left blank
	    } // try

	// seed file game
	case 1: 

	    String filename = args[0];
	    File file = new File(filename);

	    if (file.isFile()) {
		game = new Minesweeper(file);
		break;
	    } // if
    
        // display usage statement
	default:

	    System.out.println("Usage: java Minesweeper [FILE]");
	    System.out.println("Usage: java Minesweeper [ROWS] [COLS]");
	    System.exit(0);

	} // switch

	// if all is good, then run the game
	game.run();

    } // main


} // Minesweeper