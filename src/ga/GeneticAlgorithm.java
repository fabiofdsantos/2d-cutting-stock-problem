package ga;

import ga.geneticOperators.Mutation;
import ga.geneticOperators.Recombination;
import ga.selectionMethods.SelectionMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm<I extends Individual, P extends Problem<I>> {

    public static Random random;
    private final int populationSize;
    private final int maxGenerations;
    private final SelectionMethod<I, P> selection;
    private final Recombination<I> recombination;
    private final Mutation<I> mutation;
    private int t;
    private Population<I, P> population;
    private boolean stopped;
    private I bestInRun;

    public GeneticAlgorithm(
            int populationSize,
            int maxGenerations,
            SelectionMethod<I, P> selection,
            Recombination<I> recombination,
            Mutation<I> mutation,
            Random rand) {

        random = rand;
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.selection = selection;
        this.recombination = recombination;
        this.mutation = mutation;
    }

    public I run(P problem) {
        population = new Population<I, P>(populationSize, problem);
        t = 0;
        bestInRun = population.evaluate();
        fireGenerationEnded(new GAEvent(this));

        //System.out.println(population);

        while (!stopCondition(t)) {
            Population<I, P> populationAux = selection.run(population);
            recombination(populationAux);
            mutation(populationAux);
            population = generateNewPopulation(population, populationAux);
            t++;
            I bestInGen = population.evaluate();
            bestInRun = (bestInGen.getFitness() > bestInRun.getFitness()) ? (I) bestInGen.clone() : bestInRun;
            fireGenerationEnded(new GAEvent(this));
            //System.out.println(population);
        }
        fireRunEnded(new GAEvent(this));
        return bestInRun;
    }

    private boolean stopCondition(int t) {
        return stopped || t == maxGenerations;
    }

    private void recombination(Population<I, P> population) {
        for (int i = 0; i < populationSize; i += 2) {
            if (random.nextDouble() < recombination.getProbability()) {
                recombination.run(population.getIndividual(i), population.getIndividual(i + 1));
            }
        }
    }

    private void mutation(Population<I, P> population) {
        for (int i = 0; i < populationSize; i++) {
            mutation.run(population.getIndividual(i));
        }
    }

    public Population generateNewPopulation(Population<I, P> presentPopulation, Population<I, P> nextPopulation) {
        return nextPopulation;
    }

    public int getGeneration() {
        return t;
    }

    public I getBestInGeneration() {
        return population.getBest();
    }

    public double getAverageFitness() {
        return population.getAverageFitness();
    }

    public I getBestInRun() {
        return bestInRun;
    }

    public void stop() {
        stopped = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Population size:" + populationSize + "\n");
        sb.append("Max generations:" + maxGenerations + "\n");
        sb.append("Selection:" + selection + "\n");
        sb.append("Recombination:" + recombination + "\n");
        sb.append("Mutation:" + mutation + "\n");
        return sb.toString();
    }

    //Listeners
    private transient List<GAListener> listeners = new ArrayList<GAListener>(3);

    public synchronized void removeAGListener(GAListener listener) {
        if (listeners != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public synchronized void addGAListener(GAListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void fireGenerationEnded(GAEvent e) {
        for (GAListener listener : listeners) {
            listener.generationEnded(e);
        }
        if (e.isStopped()) {
            stop();
        }
    }

    public void fireRunEnded(GAEvent e) {
        for (GAListener listener : listeners) {
            listener.runEnded(e);
        }
    }
}
