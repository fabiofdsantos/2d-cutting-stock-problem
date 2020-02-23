/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.geneticOperators;

import ga.Individual;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author fabio
 * @author andre
 */
public class CycleCrossover<I extends Individual> extends Recombination<I> {

    public CycleCrossover(double probability) {
        super(probability);
    }

    public void run(I ind1, I ind2) {


        // get the total of genes
        int size = ind1.getNumGenes();

        // get genes
        int[][] ind1Genes = ind1.getPieces();
        int[][] ind2Genes = ind2.getPieces();

        // random number of genes to copy
        //int randNum = GeneticAlgorithm.random.nextInt(size);
        // create initial children
        int[][] child1 = new int[ind1Genes.length][ind1Genes[0].length];
        for (int i = 0; i < size; i++) {
            child1[i][0] = -1;
            child1[i][1] = -1;
        }

        // create initial children2
        int[][] child2 = new int[ind2Genes.length][ind2Genes[0].length];
        for (int i = 0; i < size; i++) {
            child2[i][0] = -1;
            child2[i][1] = -1;
        }

        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        Collections.shuffle(list);

        for (int i = 0; i < 5; i++) {
            System.arraycopy(ind1Genes, list.get(i), child1, list.get(i), 1);
            System.arraycopy(ind2Genes, list.get(i), child2, list.get(i), 1);
        }

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

        System.arraycopy(child2, 0, ind1Genes, 0, ind1Genes.length);
        System.arraycopy(child1, 0, ind2Genes, 0, ind2Genes.length);
    }

    @Override
    public String toString() {
        return "Cycle Crossover (" + probability + ")";
    }
}
