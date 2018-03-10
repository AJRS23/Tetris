package com.tec.alvaroramirez.tetristarea2;



public class Pieces {
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
}
