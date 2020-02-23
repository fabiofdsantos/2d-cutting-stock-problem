package statistics;

import experiments.ExperimentEvent;
import ga.*;
import utils.Maths;

public class StatisticBestAverage<E extends Individual, P extends Problem<E>> implements GAListener {

    private final double[] values;
    private int run;

    public StatisticBestAverage(int numRuns) {
        values = new double[numRuns];
    }

    @Override
    public void generationEnded(GAEvent e) {
    }

    @Override
    public void runEnded(GAEvent e) {
        GeneticAlgorithm<E, P> ga = e.getSource();
        values[run++] = ga.getBestInRun().getFitness();
    }

    @Override
    public void experimentEnded(ExperimentEvent e) {

        double average = Maths.average(values);
        double sd = Maths.standardDeviation(values, average);

        utils.FileOperations.appendToTextFile("statistic_average_fitness.xls", e.getSource() + "\t" + average + "\t" + sd + "\r\n");
    }
}
