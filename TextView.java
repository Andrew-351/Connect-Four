/**
 * This class contains all the messages that may be displayed on the screen during the game.
 *
 * @author s2013670
 */
public final class TextView
{
	public TextView()
	{
	
	}

	public final void displayGreeting()
	{
		System.out.println("\n---------------------- HELLO! ----------------------\n");
	}

	public final char askNewOrResume()
	{
		System.out.print("Please input \"R\" to resume your last game or \"N\" to start a new one: ");
		return InputUtil.readCharFromUser();
	}

	public final void displayNewGameMessage()
	{
		System.out.println("\n----------------- NEW GAME STARTED -----------------\n");
		System.out.println("Please input the desired number of rows and columns.");
	}

	public final void displayGameLoaded()
	{
		System.out.println("\n----------------- LAST GAME LOADED -----------------\n");
	}

	public final int askForNumberOfPlayers()
	{
		System.out.print("\nPlease input the number of players (1 or 2): ");
		return InputUtil.readIntFromUser();
	}

	public final int askForDifficulty()
	{
		System.out.println("\nPlease select the level of difficulty (from 1 to 3).");
		System.out.println("1: Easy.\n2: Medium.\n3: Hard");
		System.out.print("Difficulty: ");
		return InputUtil.readIntFromUser();
	}

	public final int askForRows()
	{
		System.out.print("Rows (min - " + Model.MIN_ROWS + ", max - " + Model.MAX_ROWS + "): ");
		return InputUtil.readIntFromUser();
	}

	public final int askForColumns()
	{
		System.out.print("Columns (min - " + Model.MIN_COLS + ", max - " + Model.MAX_COLS + "): ");
		return InputUtil.readIntFromUser();
	}

	// Asking for how many pieces in a row (X) are needed to win.
	public final int askForX (Model model)
	{
		System.out.print("\nPlease input how many pieces in a row are needed to win ");
		System.out.print("(min - " + Model.MIN_COLS + ", max - " + Math.max(model.getNrCols(), model.getNrRows()) + "): ");
		return InputUtil.readIntFromUser();
	}

	public final void displayInstructions (Model model)
	{
		System.out.println("\nTo make your move, please input a value from 1 to " + model.getNrCols() + ".");
		System.out.println("To concede, please input 0 (zero).\n");
	}

	public final int askForMove(Model model)
	{
		System.out.print("Player " + model.getPlayerTurn() + ", please select a free column: ");
		return InputUtil.readIntFromUser();
	}

	public final void displayAIMove (int move)
	{
		System.out.printf("AI has made its move: %d\n", move);
	}
	
	public final void displayBoard(Model model)
	{
		int nrRows = model.getNrRows();
		int nrCols = model.getNrCols();

		String rowDivider = ("|" + "-".repeat(5)).repeat(nrCols) + "|";
		
		// A StringBuilder is used to assemble longer Strings more efficiently.
		StringBuilder sb = new StringBuilder();


		sb.append(rowDivider + "\n");
		for (int i = 0; i < nrRows; i++){
			for (int j = 0; j < nrCols; j++){
				char board = model.getBoard(i, j);
				sb.append("|  " + board + "  ");
			}
			sb.append("|\n");
			sb.append(rowDivider + "\n");
		}
		sb.append("|" + "-".repeat(6 * nrCols - 1) + "|\n");
		for (int i = 0; i < nrCols; i++) {
			if (i < 9) sb.append("|  " + (i+1) + "  ");
			else sb.append("|  " + (i+1) + " ");
		}
		sb.append("|\n");
		sb.append("|" + "=".repeat(6 * nrCols - 1) + "|\n");
		
		// Then print out the assembled String.
		System.out.println(sb);
	}

	public final void displayWinner(Model model, int players)
	{
		System.out.println("\n" + "#".repeat(6 * model.getNrCols() + 1));
		if (players == 2) System.out.println("Player " + model.getPlayerTurn() + " wins. Congratulations!\n");
		else {
			if (model.getPlayerTurn() == 1) System.out.println("You won! Congratulations!\n");
			else System.out.println("You lost. Better luck next time!\n");
		}
	}

	public final void displayGameEndMessage()
	{
		System.out.println("-------------------- GAME OVER! --------------------\n\n");
	}

	public final void displayGoodbye()
	{
		System.out.println("Thanks for playing! \nSee you later!\n");
	}

	public final String newGameOffer()
	{
		System.out.print("Would you like to start a new game? Please input \"yes\" or \"no\": ");
		return InputUtil.readStringFromUser();
	}


}
