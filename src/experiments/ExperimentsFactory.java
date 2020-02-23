package experiments;

import ga.GeneticAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * This class can easily be made generic (not specific to genetic algorithms).
 * We maintain it like this so that students are not overwhelmed with POO stuff
 * more than they are already.
 */
public abstract class ExperimentsFactory {

    protected int numRuns;
    protected HashMap<String, Parameter> parameters;
    protected Parameter[] orderedParametersVector;
    protected List<String> statisticsNames;
    protected List<ExperimentListener> statistics;

    public ExperimentsFactory(File configFile) throws IOException {
        readParametersFile(configFile);
        readStatisticsFile(configFile);
    }

    protected abstract Experiment buildExperiment() throws IOException;

    public abstract GeneticAlgorithm generateGAInstance(int seed);

    public boolean hasMoreExperiments() {
        return orderedParametersVector[0].activeValueIndex < orderedParametersVector[0].getNumberOfValues();
    }

    public Experiment nextExperiment() throws IOException {
        if (hasMoreExperiments()) {
            Experiment experiment = buildExperiment();
            indicesManaging(orderedParametersVector.length - 1);
            return experiment;
        }
        return null;
    }

    public void indicesManaging(int i) {
        orderedParametersVector[i].activeValueIndex++;
        if (i != 0 && orderedParametersVector[i].activeValueIndex >= orderedParametersVector[i].getNumberOfValues()) {
            orderedParametersVector[i].activeValueIndex = 0;
            indicesManaging(--i);
        }
    }

    private void readParametersFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);

        List<String> lines = new ArrayList<String>(100);

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (!s.equals("") && !s.startsWith("//") && !s.startsWith("Statistic")) {
                lines.add(s);
            }
        }

        parameters = new HashMap<String, Parameter>(lines.size() * 2);
        orderedParametersVector = new Parameter[lines.size()];
        int i = 0;
        for (String line : lines) {
            String[] tokens = line.split(":|,");
            String[] parameterValues = new String[tokens.length - 1];
            String parameterName = tokens[0];
            for (int j = 1; j < tokens.length; j++) {
                parameterValues[j - 1] = tokens[j].trim();
            }

            Parameter parameter = new Parameter(parameterName, parameterValues);
            parameters.put(parameterName, parameter);
            orderedParametersVector[i++] = parameter;
        }

//        for (i = 0; i < orderedParametersVector.length; i++) {
//            System.out.print(orderedParametersVector[i].name + ": ");
//            for (int j = 0; j < orderedParametersVector[i].getNumberOfValues(); j++) {
//                System.out.print(orderedParametersVector[i].values[j]);
//                if(j < orderedParametersVector[i].getNumberOfValues() - 1){
//                    System.out.print(", ");
//                }
//            }
//            System.out.println();
//        }
    }

    private void readStatisticsFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);

        statisticsNames = new ArrayList<String>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("Statistic")) {
                statisticsNames.add(line.substring(11));
            }
        }
    }

    protected String getParameterValue(String name) {
        return parameters.get(name).getActiveValue();
    }
}
