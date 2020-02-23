package ga.geneticOperators;

import ga.Individual;

public class RecombinationUniform<I extends Individual> extends Recombination<I> {

    public RecombinationUniform(double probability) {
        super(probability);
    }

    public void run(Individual ind1, Individual ind2) {
        /*int indSize = ind1.getNumGenes();

        for (int g = 0; g < indSize; g++) {
            if (GeneticAlgorithm.random.nextInt(2) == 0) {
                ind1.swapGenes(ind2, g);
            }
        }*/
    }

    @Override
    public String toString() {
        return "uniform recombination (" + probability + ")";
    }
}