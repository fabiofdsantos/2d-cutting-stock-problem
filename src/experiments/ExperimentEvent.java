package experiments;

public class ExperimentEvent {

    Experiment source;

    public ExperimentEvent(Experiment source) {
        this.source = source;
    }

    public Experiment getSource() {
        return source;
    }
}
