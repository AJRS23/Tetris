package com.tec.alvaroramirez.tetristarea2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Point;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private final int rowCount = 17;
    private final int columnCount = 15;
    private GridLayout layoutBoard;
    private Game game;
    private Pieces piecess;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutBoard = findViewById(R.id.layoutBoard);
        layoutBoard.setRowCount(rowCount);
        layoutBoard.setColumnCount(columnCount);
        layoutBoard.setBackgroundColor(Color.BLACK);

        game = new Game();
        game.init();

    }


    public class Game {

        private Matrix pieceOrigin; //Holds the initial place of each piece. This is the location where each piece will start falling from.
        private int currentPiece; //Indexes the current falling piece.
        private int rotation; //Represents the current angle of the current falling piece.
        private ArrayList<Integer> nextPieces; //Acts as a queue in which all of the following pieces are placed in order.

        private long score; //Holds the score as a long value in case the player's score increases over the maximum that an integer can hold.
        private BoardGame[][] board; //Holds the colors of the well in an array for easy access (will also be used to check for gaps).
        private boolean isGameOver;
        private Runnable runnable;
        private final Handler handler = new Handler();

        /*3 dimensional array to hold the 7 Tetris pieces. Each piece can be referenced by an index ranging from 0 to 6.
	  There exists the following pieces: I, J, L, O, S, T, Z. They are placed in this array alphabetically.
	  These are the shapes that will be falling from top to bottom in the game.
	  Using such an array simplifies the accessibility of each piece throughout the program.*/
        private final Matrix[][][] Tetraminos = {
                // I-Piece
                {
                        {new Matrix(0, 1), new Matrix(1, 1), new Matrix(2, 1), new Matrix(3, 1)},
                        {new Matrix(1, 0), new Matrix(1, 1), new Matrix(1, 2), new Matrix(1, 3)},
                        {new Matrix(0, 1), new Matrix(1, 1), new Matrix(2, 1), new Matrix(3, 1)},
                        {new Matrix(1, 0), new Matrix(1, 1), new Matrix(1, 2), new Matrix(1, 3)}
                },

                // J-Piece
                {
                        {new Matrix(0, 1), new Matrix(1, 1), new Matrix(2, 1), new Matrix(2, 0)},
                        {new Matrix(1, 0), new Matrix(1, 1), new Matrix(1, 2), new Matrix(2, 2)},
                        {new Matrix(0, 1), new Matrix(1, 1), new Matrix(2, 1), new Matrix(0, 2)},
                        {new Matrix(1, 0), new Matrix(1, 1), new Matrix(1, 2), new Matrix(0, 0)}
                },

                // L-Piece
                {
                        {new Matrix(0, 1), new Matrix(1, 1), new Matrix(2, 1), new Matrix(2, 2)},
                        {new Matrix(1, 0), new Matrix(1, 1), new Matrix(1, 2), new Matrix(0, 2)},
                        {new Matrix(0, 1), new Matrix(1, 1), new Matrix(2, 1), new Matrix(0, 0)},
                        {new Matrix(1, 0), new Matrix(1, 1), new Matrix(1, 2), new Matrix(2, 0)}
                },

                // O-Piece
                {
                        {new Matrix(0, 0), new Matrix(0, 1), new Matrix(1, 0), new Matrix(1, 1)},
                        {new Matrix(0, 0), new Matrix(0, 1), new Matrix(1, 0), new Matrix(1, 1)},
                        {new Matrix(0, 0), new Matrix(0, 1), new Matrix(1, 0), new Matrix(1, 1)},
                        {new Matrix(0, 0), new Matrix(0, 1), new Matrix(1, 0), new Matrix(1, 1)}
                },

                // S-Piece
                {
                        {new Matrix(1, 0), new Matrix(2, 0), new Matrix(0, 1), new Matrix(1, 1)},
                        {new Matrix(0, 0), new Matrix(0, 1), new Matrix(1, 1), new Matrix(1, 2)},
                        {new Matrix(1, 0), new Matrix(2, 0), new Matrix(0, 1), new Matrix(1, 1)},
                        {new Matrix(0, 0), new Matrix(0, 1), new Matrix(1, 1), new Matrix(1, 2)}
                },

                // T-Piece
                {
                        {new Matrix(1, 0), new Matrix(0, 1), new Matrix(1, 1), new Matrix(2, 1)},
                        {new Matrix(1, 0), new Matrix(0, 1), new Matrix(1, 1), new Matrix(1, 2)},
                        {new Matrix(0, 1), new Matrix(1, 1), new Matrix(2, 1), new Matrix(1, 2)},
                        {new Matrix(1, 0), new Matrix(1, 1), new Matrix(2, 1), new Matrix(1, 2)}
                },

                // Z-Piece
                {
                        {new Matrix(0, 0), new Matrix(1, 0), new Matrix(1, 1), new Matrix(2, 1)},
                        {new Matrix(1, 0), new Matrix(0, 1), new Matrix(1, 1), new Matrix(0, 2)},
                        {new Matrix(0, 0), new Matrix(1, 0), new Matrix(1, 1), new Matrix(2, 1)},
                        {new Matrix(1, 0), new Matrix(0, 1), new Matrix(1, 1), new Matrix(0, 2)}
                }
        };

        //private final int[] tetraminoColors = {
          //      Color.valueOf(Color.CYAN), Color.valueOf(Color.BLUE), Color.valueOf(Color.BLACK), Color.valueOf(Color.YELLOW), Color.valueOf(Color.GREEN), Color.valueOf(Color.MAGENTA), Color.valueOf(Color.RED)
        //};

        /*Again, using an array to hold the colors of the pieces simplifies the accessibility of the colors
	  throughout the program, as they can be referenced by an index ranging from 0 to 6.
	  Each of these colors is given the same index number as the shape that they are assigned to.*/
        private final int[] tetraminoColors = {Color.parseColor("#d48600"), Color.parseColor("#d40000"), Color.parseColor("#7f00d4"),
                Color.parseColor("#0018d4"), Color.parseColor("#00d478"), Color.parseColor("#ada482"), Color.parseColor("#d0d400")};


        //Creates a black and gray border around the well and initializes a new falling piece.
        private void init() {

            layoutBoard.removeAllViews();
            nextPieces = new ArrayList<>();
            isGameOver = false;
            int squareSize = 70;
            board = new BoardGame[rowCount][columnCount];

            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {

                    BoardGame wellView = new BoardGame(getApplicationContext());
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = squareSize; params.height = squareSize;
                    params.setMargins(1, 1, 1, 1);

                    wellView.setLayoutParams(params);
                    layoutBoard.addView(wellView);

                    if (i == 0 || i == rowCount - 1 || j == 0 || j == columnCount - 1) {
                        wellView.setBackgroundColor(Color.parseColor("#4f4d45"));
                    } else {
                        wellView.setBackgroundColor(Color.GRAY);
                    }

                    board[i][j] = wellView;
                }
            }
            newPiece();
            runDropDown();

        }


        /*By setting the initial position, all new pieces will be placed at that point first before falling.
	  Whenever the queue is empty, new pieces are added to the queue with an angle of 0 degrees.
	  Those pieces are shuffled upon placement to ensure randomness.
	  As a piece is placed on the screen, it is removed from that queue, which eventually will make it empty.*/
        private void newPiece() {

            pieceOrigin = new Matrix(1, (columnCount / 2) - 1 );
            rotation = 0;

            if (nextPieces.isEmpty()) {
                Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
                Collections.shuffle(nextPieces);
            }
            currentPiece = nextPieces.get(0);
            nextPieces.remove(0);


            if (collidesAt(pieceOrigin.x , pieceOrigin.y, rotation)) {
                isGameOver = true;
                handler.removeCallbacksAndMessages(runnable);
                GameOver();
            }
            else {
                drawPiece();
            }
        }




        /*Merely a collision test for falling pieces and the well to be used throughout the program.
   It it made private as it is only used by other methods and not by any external actors.*/
        private boolean collidesAt(int i, int j, int rotation) {
            for (Matrix p : Tetraminos[currentPiece][rotation]) {
                if (board[p.x + i][p.y + j].getColor() != Color.GRAY) {
                    return true;
                }
            }
            return false;
        }


        public void rotate(int r) {
            int newRotation = (rotation + r) % 4;
            if (newRotation < 0) {
                newRotation = 3;
            }
            repaint();
            if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation)) {
                rotation = newRotation;
            }
            drawPiece();
        }

        public void move(int j) {
            repaint();
            if (!collidesAt(pieceOrigin.x, pieceOrigin.y + j, rotation)) {
                pieceOrigin.y += j;
            }
            drawPiece();
        }

        public void dropDown() {
            repaint();
            if (!collidesAt(pieceOrigin.x + 1, pieceOrigin.y, rotation)) {
                pieceOrigin.x += 1;
                drawPiece();
            } else {
                fixToWell();
                newPiece();
            }
        }

        private void fixToWell() {
            drawPiece();
            for (Matrix p : Tetraminos[currentPiece][rotation]) {
                board[pieceOrigin.x + p.x][pieceOrigin.y + p.y].setBackgroundColor(tetraminoColors[currentPiece]);
            }
            clearRows();
        }


        private void deleteRow(int row) {
            for (int i = row - 1; i > 0; i--) {
                for (int j = 1; j < columnCount - 1; j++) {
                    board[i + 1][j].setBackgroundColor(board[i][j].getColor());
                }
            }
        }


        private void clearRows() {
            boolean gap;
            int numClears = 0;

            for (int i = 1; i < rowCount - 1; i++) {
                gap = false;
                for (int j = 1; j < columnCount - 1; j++) {
                    if (board[i][j].getColor() == Color.GRAY) {
                        gap = true;
                        break;
                    }
                }
                if (!gap) {
                    deleteRow(i);
                    numClears++;
                }
            }

        }


        private void repaint() {
            for (Matrix p : Tetraminos[currentPiece][rotation]) {
                board[p.x + pieceOrigin.x][p.y + pieceOrigin.y].setBackgroundColor(Color.GRAY);
            }
        }


        private void drawPiece() {

            /*for (Matrix p : Tetraminos[currentPiece][rotation]) {
                Log.d("infooooo",Integer.toString(checkTheoreticalPos()));
                board[p.x + pieceOrigin.x][p.y + checkTheoreticalPos()].setBackgroundColor(Color.parseColor("#f7f5ef"));
            }*/
            for (Matrix p : Tetraminos[currentPiece][rotation]) {
                board[p.x + pieceOrigin.x][p.y + pieceOrigin.y].setBackgroundColor(tetraminoColors[currentPiece]);
            }
        }

        private void runDropDown() {
            runnable = new Runnable() {

                public void run() {

                    if (!isGameOver){

                        game.dropDown();
                        handler.postDelayed(this, 1000);
                    }
                }
            };

            handler.postDelayed(runnable, 1000);
        }

        public int checkTheoreticalPos(){
            ArrayList<Integer> theor = new ArrayList<>();
            for (Matrix p : Tetraminos[currentPiece][rotation]) {
                int theorVal = 0;
                for(int j = p.y + pieceOrigin.y + 1; j < rowCount; j++){
                    if(board[p.x + pieceOrigin.x][j].getColor()==Color.GRAY){
                        theorVal++;
                    }else break;
                }
                theor.add(theorVal);
            }
            return Collections.min(theor) + pieceOrigin.y;
        }

    }

    public void onLeft(View view) {
        game.move(-1);
    }

    public void onRight(View view) {
        game.move(1);
    }

    public void onDown(View view) {
        game.dropDown();
    }

    public void onRotate(View view) {
        game.rotate(1);
    }

    public void GameOver() {

        AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
        dialog.setTitle("GAME OVER");
        dialog.setCancelable(false);

        dialog.setPositiveButton("NEW GAME",
                new DialogInterface.OnClickListener() {
                    //Juego nuevo
                    public void onClick(DialogInterface dialog, int which) {
                        game = new Game();
                        game.init();
                    }
                });
        dialog.create().show();
    }


}
