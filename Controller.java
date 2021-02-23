import java.io.FileNotFoundException;

/**
 * This class controls the flow of the game.
 * It brings together the features of Model and TextView.
 *
 * @author s2013670
 */
public final class Controller
{
	private final Model model;
	private final TextView view;
	private final WinDetector detector;
	private final NPC npc;
	private final FileHandling file;
	
	public Controller(Model model, TextView view, WinDetector detector, NPC npc, FileHandling file)
	{
		this.model = model;
		this.view = view;
		this.detector = detector;
		this.npc = npc;
		this.file = file;
	}


	public void startSession(boolean isNewSession) throws FileNotFoundException {
		char startNewOrResume = 'N';

		// If there is a saved game that hasn't been finished and the user chooses to resume it,
		// the game will be loaded, and appropriate updates will be made to the model.
		if (isNewSession) {
			view.displayGreeting();
			while (true) {
				startNewOrResume = view.askNewOrResume();
				if (startNewOrResume == 'R') {
					boolean ableToLoad = file.loadGame();
					if (ableToLoad) view.displayGameLoaded();
					else startNewOrResume = 'N';
					break;
				}
				else if (startNewOrResume == 'N') break;
			}
		}

		// =======================================================
		// ================== SETTING THE RULES ==================
		// ======== (if the last game has been finished) =========
		// =======================================================

		if (startNewOrResume == 'N') {
			int numRows = 0, numCols = 0;

			// The "universal" game is called Connect X. X represents the number of pieces in a row to win.
			int x = 0;

			// The number of players.
			int players = 0;

			// Difficulty represents the depth of the MinMax algorithm.
			int difficulty = 0;

			// Tell the user that the game has started.
			view.displayNewGameMessage();

			while (!model.rowsValid(numRows)) numRows = view.askForRows();
			while (!model.colsValid(numCols)) numCols = view.askForColumns();
			model.setDimensions(numRows, numCols);


			while (!model.isXValid(x)) x = view.askForX(model);
			model.setX(x);

			while (!model.isNrPlayersValid(players)) players = view.askForNumberOfPlayers();
			model.setNumberOfPlayers(players);

			model.setTurn(1);

			if (players == 1) {
				while (!model.isDifficultyValid(difficulty)) difficulty = view.askForDifficulty();
				if (difficulty == 3) difficulty++;
				model.setDifficulty(difficulty);
			}
		}

		view.displayInstructions(model);
		view.displayBoard(model);


		// =======================================================
		// ====================== GAME LOOP ======================
		// =======================================================

		int move = -1;
		while (!model.boardFull() && move != 0 && !detector.isGameWon()) {
			move = view.askForMove(model);

			if (model.isMoveValid(move)) {
				if (move == 0) break;

				model.makeMove(move);
				model.switchTurn();
			}
			else {
				view.displayInstructions(model);
				continue;
			}

			view.displayBoard(model);
			file.saveGame();


			// If the user us playing against the AI, this piece of code will be run.
			// Here the 'NPC' makes its move and we switch the turn back to the user.

			if (model.getNrPlayers() == 1) {	// In this comparison, the local variable 'players'
												// is not used because if we were loading the last game,
												// it has left being equal to 0.

				if (detector.isGameWon() || model.boardFull()) break;

				int moveAI = npc.minimax(model, model.getDifficulty(), "AI")[1];
				view.displayAIMove(moveAI);

				model.makeMove(moveAI);
				model.switchTurn();

				view.displayBoard(model);
				file.saveGame();
			}
		}


		// The winner is only declared if their opponent had conceded or if a win is detected.
		// It is possible not to have a winner - if the board is full but no one has X in a row.

		if (move == 0 || detector.isGameWon()) {
			model.switchTurn();
			view.displayWinner(model, model.getNrPlayers());
		}

		// Tell the user that the game is over.
		view.displayGameEndMessage();
		file.deleteFile();

		String newGame = "";
		while (!newGame.equals("yes") && !newGame.equals("no")) {
			newGame = view.newGameOffer();

			if (newGame.equals("no")) view.displayGoodbye();
			else if (newGame.equals("yes")) startSession(false);
		}
	}
}
