package com.example.demo2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ConnectFour extends Application {

    private static final char[][] board = new char[6][7];
    private static char currentPlayer = 'X';
    private Button[][] buttons;

    public ConnectFour() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initializeBoard();
        createGUI(primaryStage);
    }

    private void initializeBoard() {
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 7; ++j) {
                board[i][j] = ' ';
            }
        }
    }

    private void createGUI(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5.0);
        gridPane.setVgap(5.0);

        buttons = new Button[6][7];
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 7; ++j) {
                buttons[i][j] = createButton(i, j);
                gridPane.add(buttons[i][j], j, i);
            }
        }

        Scene scene = new Scene(gridPane, 400.0, 350.0);
        primaryStage.setTitle("Connect Four");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createButton(int row, int col) {
        Button button = new Button();
        button.setMinSize(50.0, 50.0);
        button.setStyle("-fx-background-color: Blue;");
        button.setOnAction(event -> handleMove(row, col));
        return button;
    }
    private Circle createTokenGraphic() {
        Circle circle = new Circle(20.0);
        circle.setFill(currentPlayer == 'X' ? Color.BLACK : Color.GREEN);
        return circle;
    }


    private void handleMove(int row, int col) {
        int dropRow = dropToken(col);
        if (dropRow != -1) {
            updateGUI(dropRow, col);
            if (isWinner(dropRow, col)) {
                System.out.println("Player " + currentPlayer + " wins");
                resetGameboard();
                resetGame();
            } else if (isBoardFull()) {
                System.out.println("It's a draw");
                resetGame();
            } else {
                switchPlayer();
            }
        } else {
            System.out.println("Row " + row + ", Column " + col + " is full. Try again.");
        }
    }


    private int dropToken(int col) {
        for (int i = 5; i >= 0; --i) {
            if (board[i][col] == ' ') {
                board[i][col] = currentPlayer;
                return i;
            }
        }
        return -1;
    }

    private void updateGUI(int row, int col) {
        Button button = buttons[row][col];
        Circle circle = createTokenGraphic();
        button.setGraphic(circle);
        if (isWinner(row, col)) {
            resetGameboard();
        }
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
    }

    private boolean isWinner(int row, int col) {
        return checkConsecutiveTokens(row, col) || checkDiagonals(row, col);
    }
    private boolean checkConsecutiveTokens(int row, int col) {
        return checkDirection(row, col, 0, 1) || checkDirection(row, col, 1, 0) || checkDirection(row, col, 1, 1) || checkDirection(row, col, -1, 1);
    }

    private boolean checkDiagonals(int row, int col) {
        return checkDirection(row, col, 1, 1) || checkDirection(row, col, -1, 1);
    }

    private boolean checkDirection(int row, int col, int rowChange, int colChange) {
        int count = 1;
        char current = board[row][col];

        for (int i = 1; i < 4; ++i) {
            int newRow = row + i * rowChange;
            int newCol = col + i * colChange;
            if (isValidPosition(newRow, newCol) || board[newRow][newCol] != current) {
                break;
            }
            ++count;
        }

        for (int i = 1; i < 4; ++i) {
            int newRow = row - i * rowChange;
            int newCol = col - i * colChange;
            if (isValidPosition(newRow, newCol) || board[newRow][newCol] != current) {
                break;
            }
            ++count;
        }

        return count >= 4;
    }

    private boolean isValidPosition(int row, int col) {
        return row < 0 || row >= 6 || col < 0 || col >= 7;
    }


    private boolean isBoardFull() {
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 7; ++j) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        initializeBoard();
        currentPlayer = 'X';
        resetGameboard();
    }

    private void resetGameboard() {
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 7; ++j) {
                buttons[i][j].setGraphic(null);
            }
        }
    }
}
