package org.example;

/**
 * Saves the settings
 *
 * @param rows number of rows (M) to generate the matrix MxN
 * @param cols number of columns (N) to generate the matrix MxN
 * @param mines number of mines to place on the board
 */
public record Settings(int rows, int cols, int mines) {
}
