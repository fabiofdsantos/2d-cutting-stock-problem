package project;

import ga.Problem;

import java.io.File;
import java.io.IOException;

/**
 * @author fabio
 * @author andre
 */
public class Project implements Problem<ProjectIndividual> {

    public static final int SIMPLE_FITNESS = 0;
    public static final int PENALTY_FITNESS = 1;

    private project.Piece[] pieces;
    private int rawMaterialLines;
    private int rawMaterialColumns;
    private int fitnessType = SIMPLE_FITNESS;

    public Project(project.Piece[] pieces, int rawMaterialLines, int rawMaterialColumns) {
        if (pieces == null) {
            throw new IllegalArgumentException();
        }
        this.pieces = new project.Piece[pieces.length];
        System.arraycopy(pieces, 0, this.pieces, 0, pieces.length);
        this.rawMaterialLines = rawMaterialLines;
        this.rawMaterialColumns = rawMaterialColumns;
    }

    public static Project buildProject(File file) throws IOException {
        int rawMaterialLines = 0;
        int rawMaterialColumns;
        int numberOfPieces;
        int pieceLines;
        int pieceColumns;

        java.util.Scanner f = new java.util.Scanner(file);

        while (true) {
            if (f.hasNextInt()) {
                numberOfPieces = f.nextInt();
                break;
            } else {
                f.next();
            }
        }

        while (true) {
            if (f.hasNextInt()) {
                rawMaterialColumns = f.nextInt();
                break;
            } else {
                f.next();
            }
        }

        Piece[] pieces = new Piece[numberOfPieces];

        for (int i = 0; i < pieces.length; i++) {
            while (true) {
                if (f.hasNextInt()) {
                    break;
                } else {
                    f.next();
                }
            }

            pieceLines = f.nextInt();
            pieceColumns = f.nextInt();

            int[][] matrix = new int[pieceLines][pieceColumns];

            for (int l = pieceLines - 1; l >= 0; l--) {
                for (int c = 0; c < pieceColumns; c++) {
                    matrix[l][c] = f.nextInt();
                }
            }

            if (pieceLines > pieceColumns) {
                rawMaterialLines += pieceLines;
            } else {
                rawMaterialLines += pieceColumns;
            }

            pieces[i] = new Piece(i + 1, pieceLines, pieceColumns, matrix, rawMaterialColumns);
        }

        return new Project(pieces, rawMaterialLines, rawMaterialColumns);
    }

    public ProjectIndividual getNewIndividual() {
        return new ProjectIndividual(this);
    }

    public int getNumPieces() {
        return pieces.length;
    }

    public Piece getPiece(int index) {
        return (index >= 0 && index < pieces.length) ? pieces[index] : null;
    }

    public int getRawMaterialLines() {
        return rawMaterialLines;
    }

    public int getRawMaterialColumns() {
        return rawMaterialColumns;
    }

    public int getFitnessType() {
        return fitnessType;
    }

    public void setFitnessType(int fitnessType) {
        this.fitnessType = fitnessType;
    }

    public String getColor(int i) {
        return Color.colors[i];
    }

}