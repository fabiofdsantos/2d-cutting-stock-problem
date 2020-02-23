/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.geneticOperators;

import ga.GeneticAlgorithm;
import ga.Individual;

/**
 * @author fabio
 * @author andre
 */
public class PieceMutation<I extends Individual> extends Mutation<I> {

    public PieceMutation(double probability) {
        super(probability);
    }

    public void run(I ind) {
        int indSize = ind.getNumGenes();

        for (int g = 0; g < indSize; g++) {
            double d = GeneticAlgorithm.random.nextDouble();
            if (d < probability) {
                if (d < GeneticAlgorithm.random.nextDouble()) {
                    ind.setRandomRotation(g);
                } else {
                    ind.setRandomPosition(g);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Piece mutation (" + probability + ")";
    }
}
