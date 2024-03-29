import java.util.*;

/**
 * Created by pooler.
 */

class KNNClassifier extends Classifier {

    private int k;

    KNNClassifier(double[][] dataSet, int[] ClassLabels, int k) {
        super(dataSet, ClassLabels);
        this.k = k;
    }

    @Override
    double execute() {
        int count = 0;

        for (int[] elementTestSet : TestSet) {
            if (isShortenDistanceToClassA(dataSet[elementTestSet[0]], k)) {
                if (ClassLabels[elementTestSet[0]] == 0)
                    count++;
            } else {
                if (ClassLabels[elementTestSet[0]] == 1)
                    count++;
            }
        }
        return percent * count / TestSet.length;
    }

    private boolean isShortenDistanceToClassA(double[] pointFromTestSet, int k) {
        int countA = 0;
        int countQ = 0;
        Double euclidean;
        List<Double> distanceA = new ArrayList<>();
        List<Double> distanceQ = new ArrayList<>();

        for (int[] elementTrainingSet : TrainingSet) {
            euclidean = euclidean(pointFromTestSet, elementTrainingSet);

            if (ClassLabels[elementTrainingSet[0]] == 0) {
                distanceA.add(euclidean);
            } else {
                distanceQ.add(euclidean);
            }
        }

        for (int j = 0; j < k; j++) {
            for (int i = 0; i < distanceA.size(); i++) {
                Double minA = Collections.min(distanceA);
                Double minQ = Collections.min(distanceQ);
                if (Objects.equals(minA, minQ)) {
                    distanceA.remove(minA);
                    distanceA.remove(minQ);
                } else {
                    if (minA < minQ) {
                        countA++;
                    } else {
                        countQ++;
                    }
                }
            }
        }
        return countA > countQ;
    }
}