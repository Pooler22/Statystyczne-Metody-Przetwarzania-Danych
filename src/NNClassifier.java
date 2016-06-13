
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author pooler
 */

class NNClassifier extends Classifier {

    NNClassifier(double[][] FNew, int[] ClassLabels) {
        super(FNew, ClassLabels);
    }

    @Override
    double execute() {
        int match = 0;

        for (int[] elementTestSet : TestSet) {
            if (shortDistanceToClassA(dataSet[elementTestSet[0]])) {
                if (ClassLabels[elementTestSet[0]] == 0) {
                    match++;
                }
            } else {
                if (ClassLabels[elementTestSet[0]] == 1) {
                    match++;
                }
            }
        }
        return percent * match / TestSet.length;
    }

    private boolean shortDistanceToClassA(double[] point) {
        List<Double> distanceA = new ArrayList<>();
        List<Double> distanceQ = new ArrayList<>();

        for (int[] elementTrainingSet : TrainingSet) {
            if (ClassLabels[elementTrainingSet[0]] == 0) {
                distanceA.add(euclidean(point, elementTrainingSet));
            } else {
                distanceQ.add(euclidean(point, elementTrainingSet));
            }
        }
        return Collections.min(distanceA) < Collections.min(distanceQ);
    }
}
