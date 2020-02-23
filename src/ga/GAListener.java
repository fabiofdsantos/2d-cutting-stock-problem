package ga;

import experiments.ExperimentListener;

public interface GAListener extends ExperimentListener {

    void generationEnded(GAEvent e);

    void runEnded(GAEvent e);
}
