package ga.geneticOperators;


public abstract class GeneticOperator {

    protected double probability;

    public GeneticOperator(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}