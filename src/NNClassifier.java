import java.util.*;

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
            if (isShortenDistanceToClassA(dataSet[elementTestSet[0]])) {
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

    private boolean isShortenDistanceToClassA(double[] pointFromTestSet) {
        Collection<Double> distanceA = new ArrayList<>();
        Collection<Double> distanceQ = new ArrayList<>();

        for (int[] elementTrainingSet : TrainingSet) {
            Double euclidean = euclidean(pointFromTestSet, elementTrainingSet);
            if (ClassLabels[elementTrainingSet[0]] == 0) {
                distanceA.add(euclidean);
            } else {
                distanceQ.add(euclidean);
            }
        }

        for (int i = 0; i < distanceA.size(); i++) {
            Double minA = Collections.min(distanceA);
            Double minQ = Collections.min(distanceQ);
            if (Objects.equals(minA, minQ)) {
                distanceA.remove(minA);
                distanceA.remove(minQ);
            } else {
                return minA < minQ;
            }
        }

        return new Random().nextBoolean();
    }
}
