package project;

import ga.GeneticAlgorithm;
import ga.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author fabio
 * @author andre
 */
public class ProjectIndividual extends Individual<Project, Individual> {

    private final int rawMaterialLines;
    private final int rawMaterialColumns;
    private int[][] rawMaterialMatrix;

    // pieces[i][0] == number
    // pieces[i][1] == rotation
    private int[][] pieces;

    public ProjectIndividual(Project problem) {
        super(problem);
        this.rawMaterialLines = problem.getRawMaterialLines();
        this.rawMaterialColumns = problem.getRawMaterialColumns();
        this.rawMaterialMatrix = new int[rawMaterialLines][rawMaterialColumns];

        // Generate a random two-dimensional array of problem's pieces
        // Each piece is identified by its number and rotation
        initPieces(problem.getNumPieces());
    }

    public ProjectIndividual(ProjectIndividual original) {
        super(original);
        this.rawMaterialLines = original.rawMaterialLines;
        this.rawMaterialColumns = original.rawMaterialColumns;
        this.rawMaterialMatrix = original.rawMaterialMatrix;

        this.pieces = new int[original.pieces.length][original.pieces[0].length];
        System.arraycopy(original.pieces, 0, pieces, 0, pieces.length);
    }

    @Override
    public double computeFitness() {
        HashMap<Integer, ArrayList<Integer>> cuts = new HashMap<>();

        fillRawMaterial();

        this.fitness = this.rawMaterialColumns * this.rawMaterialLines;

        // count blank cells and cuts
        for (int i = 0; i < this.rawMaterialLines; i++) {

            int numEmptyCells = 0;

            for (int j = 0; j < this.rawMaterialColumns; j++) {

                // count blank cells
                if (this.rawMaterialMatrix[i][j] == 0) {
                    numEmptyCells++;
                } else {
                    // count cuts if the current cell is not empty
                    // and if the top/bottom/right/left cell is from other piece 
                    for (int c = 0; c < 4; c++) {

                        int current = this.rawMaterialMatrix[i][j];
                        int top;
                        int bottom;
                        int right;
                        int left;

                        if (i == 0) {
                            // (i+1, j)
                            top = this.rawMaterialMatrix[i + 1][j];

                            if (top != current) {
                                if (cuts.containsKey(current)) {
                                    cuts.get(current).add(top);
                                } else {
                                    ArrayList<Integer> list = new ArrayList<>();
                                    list.add(top);
                                    cuts.put(current, list);
                                }
                            }
                        } else if (i == (this.rawMaterialLines - 1)) {
                            // (i-1, j)
                            bottom = this.rawMaterialMatrix[i - 1][j];

                            if (bottom != current) {
                                if (cuts.containsKey(current)) {
                                    cuts.get(current).add(bottom);
                                } else {
                                    ArrayList<Integer> list = new ArrayList<>();
                                    list.add(bottom);
                                    cuts.put(current, list);
                                }
                            }
                        } else {
                            // (i+1, j)
                            top = this.rawMaterialMatrix[i + 1][j];

                            // (i-1, j)
                            bottom = this.rawMaterialMatrix[i - 1][j];

                            if ((top != current) || (bottom != current)) {
                                if (cuts.containsKey(current)) {
                                    if (top != current) {
                                        cuts.get(current).add(top);
                                    }
                                    if (bottom != current) {
                                        cuts.get(current).add(bottom);
                                    }
                                } else {
                                    ArrayList<Integer> list = new ArrayList<>();
                                    if (top != current) {
                                        list.add(top);
                                    }
                                    if (bottom != current) {
                                        list.add(bottom);
                                    }
                                    cuts.put(current, list);
                                }
                            }
                        }

                        // check if the current column is the first, last or else
                        if (j == 0) {

                            // (i, j+1)
                            right = this.rawMaterialMatrix[i][j + 1];

                            if (right != current) {
                                if (cuts.containsKey(current)) {
                                    cuts.get(current).add(right);
                                } else {
                                    ArrayList<Integer> list = new ArrayList<>();
                                    list.add(right);
                                    cuts.put(current, list);
                                }
                            }
                        } else if (j == (this.rawMaterialColumns - 1)) {

                            // (i, j-1)
                            left = this.rawMaterialMatrix[i][j - 1];

                            if (left != current) {
                                if (cuts.containsKey(current)) {
                                    cuts.get(current).add(left);
                                } else {
                                    ArrayList<Integer> list = new ArrayList<>();
                                    list.add(left);
                                    cuts.put(current, list);
                                }
                            }
                        } else {

                            // (i, j+1)
                            right = this.rawMaterialMatrix[i][j + 1];

                            // (i, j-1)
                            left = this.rawMaterialMatrix[i][j - 1];

                            if ((right != current) || (left != current)) {
                                if (cuts.containsKey(current)) {
                                    if (right != current) {
                                        cuts.get(current).add(right);
                                    }
                                    if (left != current) {
                                        cuts.get(current).add(left);
                                    }
                                } else {
                                    ArrayList<Integer> list = new ArrayList<>();
                                    if (right != current) {
                                        list.add(right);
                                    }
                                    if (left != current) {
                                        list.add(left);
                                    }
                                    cuts.put(current, list);
                                }
                            }
                        }
                    }
                }
            }

            // check if last line contains only blank cells
            if (numEmptyCells == this.rawMaterialColumns) {
                break;
            } else {
                // subtract blank cells
                this.fitness -= numEmptyCells;
            }
        }

        // subtract cuts
        int[] occurrences = new int[0];

        for (int p = 1; p <= this.getNumGenes(); p++) {

            int neighbors = cuts.get(p).size();
            occurrences = new int[neighbors];

            for (int n = 0; n < neighbors; n++) {
                int neighbor = cuts.get(p).get(n);

                if (neighbor == 0) {
                    this.fitness--;
                } else {
                    occurrences[n]++;
                }
            }
        }

        for (int i = 0; i < occurrences.length; i++) {
            while (occurrences[i] > 0) {
                this.fitness--;
                occurrences[i] -= 2;
            }
        }

        // prevent negative fitness
        if (this.fitness < 0) {
            this.fitness = 0;
        }

        return this.fitness;
    }

    @Override
    public ProjectIndividual clone() {
        return new ProjectIndividual(this);
    }

    @Override
    public int getNumGenes() {
        return this.problem.getNumPieces();
    }

    @Override
    public int[][] getPieces() {
        return this.pieces;
    }

    @Override
    public void setRandomRotation(int g) {
        int[] possibleRotations = this.problem.getPiece(this.pieces[g][0]).possibleRotations;

        if (possibleRotations.length == 1) {
            this.pieces[g][1] = possibleRotations[0];
        } else {
            this.pieces[g][1] = possibleRotations[GeneticAlgorithm.random.nextInt(possibleRotations.length)];
        }
    }

    @Override
    public void setRandomPosition(int g) {
        int randomG = g;

        while (g == randomG) {
            randomG = GeneticAlgorithm.random.nextInt(getNumGenes() - 1);
        }

        int[] auxPiece = this.pieces[g];

        this.pieces[g] = this.pieces[randomG];
        this.pieces[randomG] = auxPiece;
    }

    private void initPieces(int numPieces) {
        this.pieces = new int[numPieces][2];

        List<Integer> tempPieces = new ArrayList();

        for (int i = 0; i < numPieces; i++) {
            tempPieces.add(i);
        }

        Collections.shuffle(tempPieces);

        for (int i = 0; i < tempPieces.size(); i++) {
            this.pieces[i][0] = tempPieces.get(i);

            // Random rotation
            int[] possibleRotations = this.problem.getPiece(tempPieces.get(i)).possibleRotations;

            if (possibleRotations.length == 1) {
                this.pieces[i][1] = possibleRotations[0];
            } else {
                this.pieces[i][1] = possibleRotations[GeneticAlgorithm.random.nextInt(possibleRotations.length)];
            }
        }
    }

    private void fillRawMaterial() {
        int rawBlankCells;
        int pieceNonBlankCells;

        this.rawMaterialMatrix = new int[this.rawMaterialLines][this.rawMaterialColumns];

        for (int i = 0; i < this.problem.getNumPieces(); i++) {

            // Get piece
            Piece piece = this.problem.getPiece(pieces[i][0]).clone(this.rawMaterialColumns);

            // Rotate piece
            for (int j = 0; j < pieces[i][1]; j++) {
                piece.rotate();
            }

            // Count how many non-blank cells exists on the last line of piece
            pieceNonBlankCells = 0;
            for (int pieceColumn = 0; pieceColumn < piece.columns; pieceColumn++) {
                if (piece.matrix[0][pieceColumn] != 0) {
                    pieceNonBlankCells++;
                }
            }

            int rawLine = 0;
            int rawColumn;

            // Count how many blank cells exists in each line of raw material
            while (rawLine < this.rawMaterialLines) {
                rawBlankCells = 0;
                for (rawColumn = 0; rawColumn < this.rawMaterialColumns; rawColumn++) {
                    if (this.rawMaterialMatrix[rawLine][rawColumn] == 0) {
                        rawBlankCells++;
                    }
                }

                if ((rawBlankCells >= pieceNonBlankCells) && (rawBlankCells != 0)) {
                    if (placePiece(piece, rawLine)) {
                        break;
                    }
                }
                rawLine++;
            }
        }
    }

    private boolean placePiece(Piece piece, int rawLine) {
        int rawColumn = 0;
        int pieceLine;
        int pieceColumn;
        boolean flag_columnNotEmpty;

        while (rawColumn < this.rawMaterialColumns) {
            flag_columnNotEmpty = false;

            for (pieceLine = 0; pieceLine < piece.lines; pieceLine++) {
                for (pieceColumn = 0; pieceColumn < piece.columns; pieceColumn++) {

                    // Check if the current column is not empty
                    if (this.rawMaterialMatrix[rawLine][rawColumn] > 0) {
                        flag_columnNotEmpty = true;
                        break;
                    } else {

                        if (canBePlaced(piece, rawColumn, rawLine)) {

                            int pieceLineBlankCells = 0;
                            boolean flag_nonBlankCell = false;

                            for (pieceLine = 0; pieceLine < piece.lines; pieceLine++) {
                                for (pieceColumn = 0; pieceColumn < piece.columns; pieceColumn++) {

                                    if ((piece.matrix[0][pieceColumn] == 0) && (flag_nonBlankCell == false)) {
                                        pieceLineBlankCells++;
                                    } else {
                                        flag_nonBlankCell = true;
                                    }

                                    // Write piece on raw material matrix
                                    if (piece.matrix[pieceLine][pieceColumn] != 0) {
                                        this.rawMaterialMatrix[rawLine + pieceLine][(rawColumn + pieceColumn) - pieceLineBlankCells] = piece.number;
                                    }
                                }
                            }
                            return true;
                        } else {
                            flag_columnNotEmpty = true;
                            break;
                        }
                    }
                }
                if (flag_columnNotEmpty) {
                    break;
                }
            }
            rawColumn++;
        }

        return false;
    }

    private boolean canBePlaced(Piece piece, int rawColumn, int rawLine) {
        int pieceLineBlankCells = 0;
        boolean flag_nonBlankCell = false;

        for (int pieceLine = 0; pieceLine < piece.lines; pieceLine++) {
            for (int pieceColumn = 0; pieceColumn < piece.columns; pieceColumn++) {

                if ((piece.matrix[0][pieceColumn] == 0) && (flag_nonBlankCell == false)) {
                    pieceLineBlankCells++;
                } else {
                    flag_nonBlankCell = true;
                }

                if (piece.matrix[pieceLine][pieceColumn] != 0) {

                    if (((rawColumn + pieceColumn) - pieceLineBlankCells) < 0) {
                        return false;
                    }

                    if ((rawColumn + pieceColumn) - pieceLineBlankCells >= this.rawMaterialColumns) {
                        return false;
                    }

                    if (this.rawMaterialMatrix[rawLine + pieceLine][(rawColumn + pieceColumn) - pieceLineBlankCells] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toStringWithColors() {

        String text = "<strong>Genome (piece|rotation):</strong><br>";

        for (int[] piece : this.pieces) {
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    text += "<font color=" + problem.getColor(piece[j]) + ">P";
                    text += Integer.toString(piece[j] + 1);
                } else {
                    text += "|" + Integer.toString(piece[j]) + "</font>&nbsp;&nbsp;&nbsp;";
                }
            }

        }

        text += "<br><br><strong>Fitness:</strong> " + this.fitness;

        text += "<br><br><strong>Graphic representation:</strong><br>";

        for (int rawLine = 0; rawLine < rawMaterialLines; rawLine++) {
            String tempLine = "";
            int numEmptyCells = 0;

            for (int rawColumn = 0; rawColumn < this.rawMaterialColumns; rawColumn++) {
                if (this.rawMaterialMatrix[rawLine][rawColumn] == 0) {
                    numEmptyCells++;
                    tempLine += "<font color=#FFFFFF>\u25a1</font>";
                } else {
                    tempLine += "<font color=" + this.problem.getColor(this.rawMaterialMatrix[rawLine][rawColumn] - 1) + ">\u25a0</font>";
                }
            }

            if (numEmptyCells == this.rawMaterialColumns) {
                break;
            } else {
                text += "|" + tempLine + "|<br>";
            }

        }
        return text;
    }

    @Override
    public String toString() {

        String text = "Genome (piece|rotation):";

        for (int[] piece : this.pieces) {
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    text += "P" + Integer.toString(piece[j] + 1);
                } else {
                    text += "|" + Integer.toString(piece[j]) + " ";
                }
            }

        }

        text += "\n\nFitness: " + this.fitness;

        text += "\n\nGraphic representation:\n";

        for (int rawLine = 0; rawLine < rawMaterialLines; rawLine++) {
            String tempLine = "";
            int numEmptyCells = 0;

            for (int rawColumn = 0; rawColumn < this.rawMaterialColumns; rawColumn++) {

                if (this.rawMaterialMatrix[rawLine][rawColumn] == 0) {
                    numEmptyCells++;
                }
                tempLine += Integer.toString(this.rawMaterialMatrix[rawLine][rawColumn]) + " ";
            }

            if (numEmptyCells == this.rawMaterialColumns) {
                break;
            } else {
                text += tempLine + "\n";
            }
        }
        return text;
    }
}
