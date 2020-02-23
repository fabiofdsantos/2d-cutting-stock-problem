package ga.geneticOperators;

import ga.Individual;

public abstract class Recombination<I extends Individual> extends GeneticOperator {

    public Recombination(double probability) {
        super(probability);
    }

    public abstract void run(I ind1, I ind2);

    public boolean containsGene(int[][] pieces, int size, int g) {

        for (int i = 0; i < size; i++) {
            if (pieces[i][0] == g) {
                return true;
            }
        }
        return false;
    }

    public int[][] addGene(int[][] genes, int size, int[] gene) {

        for (int i = 0; i < size; i++) {
            if (genes[i][0] == -1) {
                System.arraycopy(gene, 0, genes[i], 0, gene.length);
                break;
            }
        }

        return genes;
    }
}
