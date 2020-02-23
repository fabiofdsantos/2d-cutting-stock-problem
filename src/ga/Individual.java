package ga;

public abstract class Individual<P extends Problem, I extends Individual> {

    protected double fitness;
    protected P problem;

    public Individual(P problem) {
        this.problem = problem;
    }

    public Individual(Individual<P, I> original) {
        this.problem = original.problem;
        this.fitness = original.fitness;
    }

    public abstract double computeFitness();

    public abstract int getNumGenes();

    public abstract void setRandomRotation(int g);

    public abstract void setRandomPosition(int g);

    public abstract int[][] getPieces();

    public abstract String toStringWithColors();

    public double getFitness() {
        return fitness;
    }

    @Override
    public abstract I clone();
}
