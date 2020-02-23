package project;

import experiments.Experiment;
import experiments.ExperimentListener;
import experiments.ExperimentsFactory;
import ga.GAListener;
import ga.GeneticAlgorithm;
import ga.geneticOperators.*;
import ga.selectionMethods.RouletteWheel;
import ga.selectionMethods.SelectionMethod;
import ga.selectionMethods.Tournament;
import statistics.StatisticBestAverage;
import statistics.StatisticBestInRun;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author fabio
 * @author andre
 */
public class ProjectExperimentsFactory extends ExperimentsFactory {

    private int populationSize;
    private int maxGenerations;
    private SelectionMethod<ProjectIndividual, Project> selection;
    private Recombination<ProjectIndividual> recombination;
    private Mutation<ProjectIndividual> mutation;
    private Project project;
    private Experiment<ProjectExperimentsFactory, Project> experiment;

    public ProjectExperimentsFactory(File configFile) throws IOException {
        super(configFile);
    }

    public Experiment buildExperiment() throws IOException {
        numRuns = Integer.parseInt(getParameterValue("Runs"));
        populationSize = Integer.parseInt(getParameterValue("Population size"));
        maxGenerations = Integer.parseInt(getParameterValue("Max generations"));

        //SELECTION
        if (getParameterValue("Selection").equals("tournament")) {
            int tournamentSize = Integer.parseInt(getParameterValue("Tournament size"));
            selection = new Tournament<ProjectIndividual, Project>(populationSize, tournamentSize);
        } else if (getParameterValue("Selection").equals("roulette wheel")) {
            selection = new RouletteWheel<ProjectIndividual, Project>(populationSize);
        }

        //RECOMBINATION
        double recombinationProbability = Double.parseDouble(getParameterValue("Recombination probability"));
        if (getParameterValue("Recombination").equals("one_cut")) {
            recombination = new OrderedCrossover<ProjectIndividual>(recombinationProbability);
        } else if (getParameterValue("Recombination").equals("two_cuts")) {
            recombination = new CycleCrossover<ProjectIndividual>(recombinationProbability);
        } else if (getParameterValue("Recombination").equals("uniform")) {
            recombination = new RecombinationUniform<ProjectIndividual>(recombinationProbability);
        }

        //MUTATION
        double mutationProbability = Double.parseDouble(getParameterValue("Mutation probability"));
        if (getParameterValue("Mutation").equals("binary")) {
            mutation = new PieceMutation<ProjectIndividual>(mutationProbability);
        }

        //PROBABILITY OF 1S AND FITNESS TYPE
        double probabilityOf1s = Double.parseDouble(getParameterValue("Probability of 1s"));
        int fitnessType = Integer.parseInt(getParameterValue("Fitness type"));

        //PROBLEM 
        project = Project.buildProject(new File(getParameterValue("Problem file")));
        project.setFitnessType(fitnessType);

        String textualRepresentation = buildTextualExperiment();

        experiment = new Experiment<ProjectExperimentsFactory, Project>(this, numRuns, project, textualRepresentation);

        statistics = new ArrayList<ExperimentListener>();
        for (String statisticName : statisticsNames) {
            ExperimentListener statistic = buildStatistic(statisticName);
            statistics.add(statistic);
            experiment.addExperimentListener(statistic);
        }

        return experiment;
    }

    @Override
    public GeneticAlgorithm generateGAInstance(int seed) {
        GeneticAlgorithm<ProjectIndividual, Project> ga;

        ga = new GeneticAlgorithm<ProjectIndividual, Project>(
                populationSize,
                maxGenerations,
                selection,
                recombination,
                mutation,
                new Random(seed));

        for (ExperimentListener statistic : statistics) {
            ga.addGAListener((GAListener) statistic);
        }

        return ga;
    }

    private ExperimentListener buildStatistic(String statisticName) {
        if (statisticName.equals("BestIndividual")) {
            return new StatisticBestInRun();
        }
        if (statisticName.equals("BestAverage")) {
            return new StatisticBestAverage(numRuns);
        }
        return null;
    }

    private String buildTextualExperiment() {
        StringBuilder sb = new StringBuilder();
        sb.append("Population size:").append(populationSize).append("\t");
        sb.append("Max generations:").append(maxGenerations).append("\t");
        sb.append("Selection:").append(selection).append("\t");
        sb.append("Recombination:").append(recombination).append("\t");
        sb.append("Mutation:").append(mutation).append("\t");
        sb.append("Fitness type:").append(project.getFitnessType());
        return sb.toString();
    }

}
