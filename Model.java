/**
 * This class contains the model of the game board.
 * It also has representation of all actions that can be performed on the board.
 *
 * @author s2013670
 */
public final class Model
{
	// ===========================================================================
	// ================================ CONSTANTS ================================
	// ===========================================================================
	// The most common version of Connect Four has 7 rows and 6 columns.
	public static final int DEFAULT_NR_ROWS = 6;
	public static final int DEFAULT_NR_COLS = 7;
	public static final int DEFAULT_X = 4;

	// The minimum size of the board.
	public static final int MIN_ROWS = 4;
	public static final int MIN_COLS = 4;

	// The maximum size of the board.
	public static final int MAX_ROWS = 10;
	public static final int MAX_COLS = 20;

	
	// ========================================================================
	// ================================ FIELDS ================================
	// ========================================================================
	// The size of the board.
	private int nrRows;
	private int nrCols;

	private int piecesInRow;	// The number 'X' for the Connect X version.

	private int nrPlayers;		// 1 if playing against the AI, 2 if human vs human.

	private char[][] board;		// Board representation.
	private int playerTurn;		// The indicator showing whose turn it is. Can be 1 or 2.
	private int[] colHeight;	// Current height of each of nrCols columns.

	private int difficulty;

	// =============================================================================
	// ================================ CONSTRUCTOR ================================
	// =============================================================================
	public Model()
	{
		// Initialise the board size to its default values.
		nrRows = DEFAULT_NR_ROWS;
		nrCols = DEFAULT_NR_COLS;
		piecesInRow = DEFAULT_X;


		// Board represented by an 'nrRow x nrCols' array.
		board = new char[nrRows][nrCols];

		playerTurn = 1;
		colHeight = new int[nrCols];
		difficulty = 0;
	}

	// =========================================================================
	// ================================ SETTERS ================================
	// =========================================================================

	// Re-initialise the board size to the user-inputted values.
	public void setDimensions(int rows, int cols)
	{
		nrRows = rows;
		nrCols = cols;
		board = new char[rows][cols];
		colHeight = new int[cols];
	}

	// Change 'X' to the user-inputted value.
	public void setX(int x)
	{
		piecesInRow = x;
	}

	public void setTurn(int turn)
	{
		playerTurn = turn;
	}

	public void setDifficulty(int level)
	{
		difficulty = level;
	}

	public void setNumberOfPlayers(int players)
	{
		nrPlayers = players;
	}

	public void setBoard(char[][] boardFromFile)
	{
		for (int i = 0; i < nrRows; i++) {
			for (int j = 0; j < nrCols; j++) {
				board[i][j] = boardFromFile[i][j];
				if ((board[i][j] == 'X' || board[i][j] == 'O') && colHeight[j] == 0) colHeight[j] = nrRows - i;
			}
		}
	}

	// ====================================================================================
	// ================================ MODEL INTERACTIONS ================================
	// ====================================================================================
	
	public void makeMove(int move)
	{
		if (move != 0) {
			if (playerTurn == 1) board[nrRows - colHeight[move - 1] - 1][move - 1] = 'X';
			else board[nrRows - colHeight[move - 1] - 1][move - 1] = 'O';

			colHeight[move-1]++;
		}
	}

	public void removePiece(int piece)
	{
		colHeight[piece-1]--;
		board[nrRows - colHeight[piece - 1] - 1][piece-1] = ' ';
	}

	// Checking if the board still have free positions.
	public boolean boardFull()
	{
		boolean isFull = true;
		for (int i = 0; i < nrCols; i++) {
			if (colHeight[i] < nrRows) {
				isFull = false;
				break;
			}
		}
		return isFull;
	}

	// Switching turns (from 1 to 2 or from 2 to 1).
	public void switchTurn()
	{
		if (playerTurn == 1) playerTurn = 2;
		else playerTurn = 1;
	}
	
	// =========================================================================
	// ================================ GETTERS ================================
	// =========================================================================
	public int getNrRows()
	{
		return nrRows;
	}
	
	public int getNrCols()
	{
		return nrCols;
	}

	public int getPiecesInRow()
	{
		return piecesInRow;
	}

	public int getNrPlayers()
	{
		return nrPlayers;
	}

	public char getBoard(int i, int j)
	{
		return board[i][j];
	}

	public int getPlayerTurn()
	{
		return playerTurn;
	}

	public int getDifficulty()
	{
		return difficulty;
	}


	// =========================================================================
	// ============================ INPUT VALIDATORS ===========================
	// =========================================================================
	public boolean rowsValid (int rows)
	{
		if (MIN_ROWS <= rows && rows <= MAX_ROWS) return true;
		else return false;
	}

	public boolean colsValid (int cols)
	{
		if (MIN_COLS <= cols && cols <= MAX_COLS) return true;
		else return false;
	}

	public boolean isMoveValid(int move)
	{
		// If a player wants to concede, this is a valid move.
		if (move == 0) return true;

			// 1) Checking if a player had entered a valid number between 1 and the number of columns.
		else if (1 <= move && move <= nrCols) {

			// 2) Checking if not all the 'nrRows' places in this column are already taken.
			if (colHeight[move-1] < nrRows) return true;
			else return false;
		}
		else return false;
	}

	// Connect X can only be won when 'X' is less than or equal to the biggest of two dimensions of the board.
	public boolean isXValid (int pieces)
	{
		if (MIN_COLS <= pieces && pieces <= Math.max(nrRows, nrCols)) return true;
		else return false;
	}

	public boolean isNrPlayersValid (int players)
	{
		if (players == 1 || players == 2) return true;
		else return false;
	}

	public boolean isDifficultyValid (int dif)
	{
		if (1 <= dif && dif <= 3) return true;
		else return false;
	}
}
