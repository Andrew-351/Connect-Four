/**
 * This class contains the features of a Non-Player Character.
 * It provides the strategy for the AI to always make the best possible moves.
 * Implementation is based on the MinMax algorithm but also contains some additional features
 * (more on those features and the heuristics of MinMax in the report).
 *
 * @author s2013670
 */

public final class NPC {


    public NPC()
    {

    }

    private static final int INFINITY = 1000000000;

    public int[] minimax (Model model, int depth, String turn)
    {
        // The array 'move' contains data that we will return.
        // move[0] is the score that would be obtained after making this move.
        // move[1] is the number of the column (the move itself),
        // we will actually need it only for the top node to understand which move to make right now.

        int[] move = new int[2];

        move[0] = calculateScore (model);
        if (depth == 0 || model.boardFull() || Math.abs(move[0]) == INFINITY) return move;
        if (turn.equals("Player: Extra_check")) return move;

        if (turn.equals("AI")) {
            int[] maxScore = new int[model.getNrCols()+1];
            maxScore[0] = -INFINITY;
            int column = 0;
            int[] sameScoreIndices = new int[model.getNrCols()];
            int nrIndices = 0;

            for (int i = 1; i <= model.getNrCols(); i++) {
                if (model.isMoveValid(i)) {
                    model.makeMove(i);
                    model.switchTurn();
                    maxScore[i] = minimax(model, depth-1, "Player")[0];
                    model.switchTurn();

                    if (maxScore[i] > maxScore[i-1]) {
                        column = i;
                        sameScoreIndices[0] = column;
                        nrIndices = 1;
                        for (int k = 1; k < model.getNrCols(); k++) sameScoreIndices[k] = 0;
                    }
                    else if (maxScore[i] == maxScore[i-1]) {
                        sameScoreIndices[nrIndices] = i;
                        nrIndices++;
                    }

                    else if (maxScore[i] < maxScore[i-1]) {
                        maxScore[i] = maxScore[i-1];
                    }

                    model.removePiece(i);
                    if (maxScore[i] == INFINITY) break;
                }
                else maxScore[i] = maxScore[i-1];
            }


            // If there are several equal maximum scores at the depth right below the root:
            if (nrIndices > 1 && depth == model.getDifficulty()) {
                int max = -INFINITY;
                for (int i = 0; i < nrIndices; i++) {
                    model.makeMove(sameScoreIndices[i]);
                    model.switchTurn();
                    int value = minimax(model, depth-1, "Player: Extra_check")[0];
                    model.switchTurn();
                    model.removePiece(sameScoreIndices[i]);

                    if (value >= max) {
                        max = value;
                        column = sameScoreIndices[i];
                    }
                }
            }

            move[0] = maxScore[model.getNrCols()];
            move[1] = column;
        }


        else {
            int minScore = INFINITY;
            int column = 0;


            for (int i = 0; i < model.getNrCols(); i++) {
                if (model.isMoveValid(i+1)) {
                    model.makeMove(i+1);
                    model.switchTurn();
                    int newScore = minimax(model, depth-1, "AI")[0];
                    model.switchTurn();


                    if (newScore < minScore) {
                        minScore = newScore;
                        column = i + 1;
                    }

                    model.removePiece(i+1);
                    if (minScore == -INFINITY) break;
                }
            }

            move[0] = minScore;
            move[1] = column;
        }
        return move;
    }


    public int calculateScore (Model model)
    {
        int rows = model.getNrRows();
        int cols = model.getNrCols();
        int x = model.getPiecesInRow();

        int score = 0;

        char[][] board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = model.getBoard(i, j);
            }
        }

        if (x <= cols) {
            int scoreH1 = scoreHorizontal(board, rows, cols, x, 'O', 'X');
            int scoreH2 = scoreHorizontal(board, rows, cols, x, 'X', 'O');
            if (scoreH1 == INFINITY) return INFINITY;
            else if (scoreH2 == INFINITY) return -INFINITY;
            else score += (scoreH1 - scoreH2);
        }

        if (x <= rows) {
            int scoreV1 = scoreVertical(board, rows, cols, x, 'O', 'X');
            int scoreV2 = scoreVertical(board, rows, cols, x, 'X', 'O');
            if (scoreV1 == INFINITY) return INFINITY;
            else if (scoreV2 == INFINITY) return -INFINITY;
            else score += (scoreV1 - scoreV2);
        }

        if (x <= cols && x <= rows) {
            int scoreDDown1 = scoreDiagonalDown(board, rows, cols, x, 'O', 'X');
            int scoreDDown2 = scoreDiagonalDown(board, rows, cols, x, 'X', 'O');
            if (scoreDDown1 == INFINITY) return INFINITY;
            else if (scoreDDown2 == INFINITY) return -INFINITY;
            else score += (scoreDDown1 - scoreDDown2);

            int scoreDUp1 = scoreDiagonalUp(board, rows, cols, x, 'O', 'X');
            int scoreDUp2 = scoreDiagonalUp(board, rows, cols, x, 'X', 'O');
            if (scoreDUp1 == INFINITY) return INFINITY;
            else if (scoreDUp2 == INFINITY) return -INFINITY;
            else score += (scoreDUp1 - scoreDUp2);
        }

        return score;
    }

    public int scoreHorizontal (char[][] board, int rows, int cols, int x, char symbol1, char symbol2)
    {
        double score = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= cols - x; j++) {
                int nrSymbols = 0;
                int additionalMoves = 0;
                for (int k = j; k < j + x; k++) {
                    if (board[i][k] == symbol1) nrSymbols++;
                    else if (board[i][k] == symbol2) {
                        nrSymbols = 0;
                        break;
                    }
                }

                if (nrSymbols >= 2) {
                    if (nrSymbols == x) return INFINITY;

                    for (int k = j; k < j + x; k++) {
                        if (board[i][k] != symbol1 && board[i][k] != symbol2) {
                            int free = i;
                            while (free < rows && board[free][k] != symbol1 && board[free][k] != symbol2) {
                                additionalMoves++;
                                free++;
                            }
                        }
                    }
                    score += (100 * (double) nrSymbols / (double) x) * (2 / (double) additionalMoves);
                }
            }
        }
        return (int) Math.round(score);
    }

    public int scoreVertical(char[][] board, int rows, int cols, int x, char symbol1, char symbol2)
    {
        double score = 0;

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j <= rows - x; j++) {
                int nrSymbols = 0;
                int additionalMoves = 0;
                for (int k = j; k < j + x; k++) {
                    if (board[k][i] == symbol1) nrSymbols++;
                    else if (board[k][i] == symbol2) {
                        nrSymbols = 0;
                        break;
                    }
                }

                if (nrSymbols >= 2) {
                    if (nrSymbols == x) return INFINITY;

                    additionalMoves = x - nrSymbols;

                    score += (100 * (double) nrSymbols / (double) x) * (2 / (double) additionalMoves);
                }
            }
        }
        return (int) Math.round(score);
    }

    // Direction: \
    public int scoreDiagonalDown(char[][] board, int rows, int cols, int x, char symbol1, char symbol2)
    {
        double score = 0;

        for (int i = 0; i <= rows - x; i++) {
            for (int j = 0; j <= cols - x; j++) {
                int nrSymbols = 0;
                int additionalMoves = 0;
                int g = i;
                int h = j;
                while (g < i + x && h < j + x) {
                    if (board[g][h] == symbol1) nrSymbols++;
                    else if (board[g][h] == symbol2) {
                        nrSymbols = 0;
                        break;
                    }
                    g++;
                    h++;
                }

                if (nrSymbols >= 2) {
                    if (nrSymbols == x) return INFINITY;

                    g = i;
                    h = j;
                    while (g < i + x && h < j + x) {
                        if (board[g][h] != symbol1 && board[g][h] != symbol2) {
                            int free = g;
                            while (free < rows && board[free][h] != symbol1 && board[free][h] != symbol2) {
                                additionalMoves++;
                                free++;
                            }
                        }
                        g++;
                        h++;
                    }
                    score += (100 * (double) nrSymbols / (double) x) * (2 / (double) additionalMoves);
                }
            }
        }
        return (int) Math.round(score);
    }

    // Direction: /
    public int scoreDiagonalUp(char[][] board, int rows, int cols, int x, char symbol1, char symbol2)
    {
        double score = 0;

        for (int i = x - 1; i < rows; i++) {
            for (int j = 0; j <= cols - x; j++) {
                int nrSymbols = 0;
                int additionalMoves = 0;
                int g = i;
                int h = j;
                while (g > i - x && h < j + x) {
                    if (board[g][h] == symbol1) nrSymbols++;
                    else if (board[g][h] == symbol2) {
                        nrSymbols = 0;
                        break;
                    }
                    g--;
                    h++;
                }

                if (nrSymbols >= 2) {
                    if (nrSymbols == x) return INFINITY;

                    g = i;
                    h = j;
                    while (g > i - x && g < j + x) {
                        if (board[g][h] != symbol1 && board[g][h] != symbol2) {
                            int free = g;
                            while (free < rows && board[free][h] != symbol1 && board[free][h] != symbol2) {
                                additionalMoves++;
                                free++;
                            }
                        }
                        g--;
                        h++;
                    }
                    score += (100 * (double) nrSymbols / (double) x) * (2 / (double) additionalMoves);
                }
            }
        }
        return (int) Math.round(score);
    }
}
