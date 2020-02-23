package ga;

import java.util.ArrayList;
import java.util.List;

public class Population<I extends Individual, P extends Problem<I>> {

    private final List<I> individuals;
    private I best;

    public Population(int size) {
        individuals = new ArrayList<I>(size);
    }

    public Population(int size, P problem) {
        individuals = new ArrayList<I>(size);
        for (int i = 0; i < size; i++) {
            individuals.add(problem.getNewIndividual());
        }
    }

    public I evaluate() {
        best = getIndividual(0);
        for (I individual : individuals) {
            best = (best.getFitness() < individual.computeFitness()) ? individual : best;
        }
        return best;
    }

    public double getAverageFitness() {
        double fitnessSum = 0;
        for (I individual : individuals) {
            fitnessSum += individual.getFitness();
        }
        return fitnessSum / individuals.size();
    }

    public void addIndividual(I individual) {
        individuals.add(individual);
    }

    public I getIndividual(int index) {
        return individuals.get(index);
    }

    public I getBest() {
        return best;
    }

    public int getSize() {
        return individuals.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (I individual : individuals) {
            sb.append(individual);
        }
        return sb.toString();
    }
}
