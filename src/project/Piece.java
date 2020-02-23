package project;

/**
 * @author fabio
 * @author andre
 */
public class Piece {

    public int number;
    public int lines;
    public int columns;
    public int[][] matrix;
    public int[] possibleRotations;

    public Piece(int number, int lines, int columns, int[][] matrix, int rawMaterialColumns) {
        this.number = number;
        this.lines = lines;
        this.columns = columns;
        this.matrix = matrix;
        this.possibleRotations = possibleRotations(rawMaterialColumns);
    }

    private Piece(Piece original, int rawMaterialColumns) {
        this.number = original.number;
        this.lines = original.lines;
        this.columns = original.columns;
        this.matrix = original.matrix;
        this.possibleRotations = possibleRotations(rawMaterialColumns);
    }

    public Piece clone(int rawMaterialColumns) {
        return new Piece(this, rawMaterialColumns);
    }

    /*
     * Calculate the possible rotations for each piece.
     */
    private int[] possibleRotations(int rawMaterialColumns) {
        boolean flag_blankSpace = false;

        // Search for at least one ZERO as value
        for (int l = 0; l < lines; l++) {
            for (int c = 0; c < columns; c++) {
                if (matrix[l][c] == 0) {
                    flag_blankSpace = true;
                    break;
                }
            }
        }

        // Possible rotations: 0 (e.g. square)
        if (lines == columns && flag_blankSpace == false) {
            return new int[]{0};
        }

        // Possible rotations: 2 or less (e.g. rectangle) 
        if (lines != columns && flag_blankSpace == false) {

            // Check if piece not fits on horizontal
            if (columns > rawMaterialColumns) {
                return new int[]{1};
            }

            if (lines > rawMaterialColumns) {
                return new int[]{0};
            }

            return new int[]{0, 1};
        }

        // Possible rotations: 4 or less (e.g. square's matrix with at least one zero as value)
        // Check if piece not fits on horizontal
        if (columns > rawMaterialColumns) {
            return new int[]{1, 3};
        }

        return new int[]{0, 1, 2, 3};
    }

    /*
     * Rotate a piece clockwise 90 degrees.
     */
    public void rotate() {

        int newColumns = lines;
        int newLines = columns;

        // Replace columns by lines and vice versa
        int[][] newMatrix = new int[newLines][newColumns];

        // Rotation example:
        //    0 1 0   -->   1 0   -->   1 1 1   -->   0 1
        //    1 1 1         1 1         0 1 0         1 1
        //                  1 0                       0 1
        for (int l = 0; l < lines; l++) {
            for (int c = 0; c < columns; c++) {
                newMatrix[c][lines - 1 - l] = matrix[l][c];
            }
        }

        this.matrix = newMatrix;
        this.columns = newColumns;
        this.lines = newLines;
    }

    @Override
    public String toString() {
        return "\n P" + number + "\t" + lines + "\t" + columns;
    }
}
