/**
 * This class contains the method of automatic game win detection.
 *
 * @author s2013670
 */

public final class WinDetector
{

    private final Model model;

    public WinDetector(Model model)
    {
        this.model = model;
    }


    private int nrRows;
    private int nrCols;
    private int x;
    private char[][] board;

    public boolean isGameWon()
    {
        nrRows = model.getNrRows();
        nrCols = model.getNrCols();
        x = model.getPiecesInRow();


        board = new char[nrRows][nrCols];
        for (int i = 0; i < nrRows; i++){
            for (int j = 0; j < nrCols; j++){
                board[i][j] = model.getBoard(i, j);
            }
        }

        for (int i = 0; i < nrRows; i++) {
            for (int j = 0; j < nrCols; j++) {
                if ((board[i][j] == 'X' || board[i][j] == 'O') && checkAllDirections(i, j)) return true;
            }
        }

        return false;
    }

    public boolean checkAllDirections(int row, int col)
    {
        return (checkRight(row, col) || checkDown(row, col) ||
                checkDownDiagonalRight(row, col) || checkDownDiagonalLeft(row, col));
    }

    // ===========================================================================================
    // ===================  Functions checking four possible directions where  ===================
    // ===================            X pieces in a row may occur.             ===================
    // ===========================================================================================

    public boolean checkRight(int row, int col)
    {
        if (col + x <= nrCols){
            for (int i = col + 1; i < col + x; i++) {
                if (board[row][i] != board[row][col]) return false;
            }
            return true;
        }
        else return false;
    }

    public boolean checkDown(int row, int col)
    {
        if (row + x <= nrRows) {
            for (int i = row + 1; i < row + x; i++) {
                if (board[i][col] != board[row][col]) return false;
            }
            return true;
        }
        else return false;
    }

    public boolean checkDownDiagonalRight(int row, int col)
    {
        if (row + x <= nrRows && col + x <= nrCols) {
            int i = row + 1;
            int j = col + 1;
            while (i < row + x && j < col + x) {
                if (board[i][j] != board[row][col]) return false;
                i++;
                j++;
            }
            return true;
        }
        else return false;
    }

    public boolean checkDownDiagonalLeft(int row, int col)
    {
        if (row + x <= nrRows && col >= x - 1) {
            int i = row + 1;
            int j = col - 1;
            while (i < row + x && j > col - x) {
                if (board[i][j] != board[row][col]) return false;
                i++;
                j--;
            }
            return true;
        }
        else return false;
    }

}
