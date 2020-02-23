package ga;

public class GAEvent {

    GeneticAlgorithm source;
    private boolean stopped;

    public GAEvent(GeneticAlgorithm source) {
        this.source = source;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public GeneticAlgorithm getSource() {
        return source;
    }
}
