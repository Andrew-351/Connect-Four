import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 * This class contains the methods to save the game after each move and to load the last game to continue it.
 * When the game is finished, the file with its copy is deleted.
 *
 * @author s2013670
 */
public class FileHandling {

    private final Model model;

    public FileHandling(Model model)
    {
        this.model = model;
    }

    private final File file = new File("LastGame.txt");

    public void saveGame() throws FileNotFoundException
    {
        PrintWriter writer = new PrintWriter(file);
        writer.println(model.getNrRows());
        writer.println(model.getNrCols());
        writer.println(model.getPiecesInRow());
        writer.println(model.getPlayerTurn());
        writer.println(model.getDifficulty());

        for (int i = 0; i < model.getNrRows(); i++) {
            for (int j = 0; j < model.getNrCols(); j++) {
                char c = model.getBoard(i, j);
                writer.print(c);
            }
            writer.println();
        }
        writer.close();
    }

    public boolean loadGame() throws FileNotFoundException
    {
        if (!file.exists()) {
            System.out.println("You have finished your last game.\n");
            return false;
        }
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("");

        String temp = scanner.nextLine();
        int rows = Integer.parseInt(temp.trim());
        temp = scanner.nextLine();
        int cols = Integer.parseInt(temp.trim());
        temp = scanner.nextLine();
        int x = Integer.parseInt(temp.trim());
        temp = scanner.nextLine();
        int turn = Integer.parseInt(temp.trim());
        temp = scanner.nextLine();
        int difficulty = Integer.parseInt(temp.trim());

        char[][] board = new char[rows][cols];

        int rowIndex = 0;
        int colIndex = 0;
        while (scanner.hasNext()) {
            char c = scanner.next().charAt(0);
            if (c == '\n') {
                rowIndex++;
                colIndex = 0;
                continue;
            }

            if (rowIndex < rows && colIndex < cols) board[rowIndex][colIndex] = c;
            colIndex++;
        }

        model.setDimensions(rows, cols);
        model.setX(x);
        model.setTurn(turn);
        if (difficulty == 0) model.setNumberOfPlayers(2);
        else {
            model.setNumberOfPlayers(1);
            model.setDifficulty(difficulty);
        }
        model.setBoard(board);
        scanner.close();
        return true;
    }

    public void deleteFile()
    {
        file.delete();
    }
}
