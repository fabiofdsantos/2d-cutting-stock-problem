package ga.selectionMethods;

import ga.GeneticAlgorithm;
import ga.Individual;
import ga.Population;
import ga.Problem;

public class RouletteWheel <I extends Individual, P extends Problem<I>> extends SelectionMethod <I, P> {

    double[] accumulated;

    public RouletteWheel(int popSize) {
        super(popSize);
        accumulated = new double[popSize];
    }

    public Population<I, P> run(Population<I, P> original) {
        Population<I, P> result = new Population<I, P>(original.getSize());
        accumulated[0] = original.getIndividual(0).getFitness();
        for (int i = 1; i < popSize; i++) {
            accumulated[i] = accumulated[i - 1] + original.getIndividual(i).getFitness();
        }

        double fitnessSum = accumulated[popSize - 1];
        for (int i = 0; i < popSize; i++) {
            accumulated[i] /= fitnessSum;
        }

        for (int i = 0; i < popSize; i++) {
            result.addIndividual(roulette(original));
        }
        
        return result;
    }

    private I roulette(Population<I, P> population) {
        double probability = GeneticAlgorithm.random.nextDouble();

        for (int i = 0; i < popSize; i++) {
            if (probability <= accumulated[i]) {
                return (I) population.getIndividual(i).clone();
            }
        }

        //For the case where all individuals have fitness 0
        return (I) population.getIndividual(GeneticAlgorithm.random.nextInt(popSize)).clone();
    }
    
    @Override
    public String toString(){
        return "Roulette wheel";
    }    
}
