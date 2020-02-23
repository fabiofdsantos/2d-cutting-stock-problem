package ga.geneticOperators;

import ga.GeneticAlgorithm;
import ga.Individual;

/**
 * @author fabio
 * @author andre
 */
public class OrderedCrossover<I extends Individual> extends Recombination<I> {

    public OrderedCrossover(double probability) {
        super(probability);
    }

    public void run(I ind1, I ind2) {
        // get the total of genes
        int size = ind1.getNumGenes();

        // get genes
        int[][] ind1Genes = ind1.getPieces();
        int[][] ind2Genes = ind2.getPieces();

        // randNum1 must be != randNum2, so (size-1)
        int randNum1 = GeneticAlgorithm.random.nextInt(size - 1);
        int randNum2 = GeneticAlgorithm.random.nextInt(size);

        // define the smaller as the start position and the larger as the end position
        int startPos = Math.min(randNum1, randNum2);
        int endPos = Math.max(randNum1, randNum2);

        // create initial children
        int[][] child1 = new int[ind1Genes.length][ind1Genes[0].length];
        for (int i = 0; i < size; i++) {
            child1[i][0] = -1;
            child1[i][1] = -1;
        }

        int[][] child2 = new int[ind2Genes.length][ind2Genes[0].length];
        for (int i = 0; i < size; i++) {
            child2[i][0] = -1;
            child2[i][1] = -1;
        }

        // add the genes between the start and end points to the children
        System.arraycopy(ind1Genes, startPos, child1, startPos, endPos - startPos);
        System.arraycopy(ind2Genes, startPos, child2, startPos, endPos - startPos);

        int[] currentGeneInd1 = new int[2];
        int[] currentGeneInd2 = new int[2];

        for (int i = 0; i < size; i++) {

            System.arraycopy(ind1Genes[i], 0, currentGeneInd1, 0, ind1Genes[i].length);
            System.arraycopy(ind2Genes[i], 0, currentGeneInd2, 0, ind2Genes[i].length);

            // add gene if child1 not contains currentGeneInd2
            if (!containsGene(child1, size, currentGeneInd2[0])) {
                child1 = addGene(child1, size, currentGeneInd2);
            }

            // add gene if child2 not contains currentGeneInd1
            if (!containsGene(child2, size, currentGeneInd1[0])) {
                child2 = addGene(child2, size, currentGeneInd1);
            }
        }

        // replace parents with genes from the children
        System.arraycopy(child2, 0, ind1Genes, 0, ind1Genes.length);
        System.arraycopy(child1, 0, ind2Genes, 0, ind2Genes.length);

    }

    @Override
    public String toString() {
        return "Ordered Crossover (" + probability + ")";
    }

}
