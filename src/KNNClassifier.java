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
        int match = 0;

        for (int[] elementTestSet : TestSet) {
            if (isShortenDistanceToClassA(dataSet[elementTestSet[0]], k)) {
                if (ClassLabels[elementTestSet[0]] == 0)
                    match++;
            } else {
                if (ClassLabels[elementTestSet[0]] == 1)
                    match++;
            }
        }
        return percent * match / TestSet.length;
    }

    private boolean isShortenDistanceToClassA(double[] pointFromTestSet, int k) {
        int countA = 0;
        int countB = 0;
        List<Double> distanceA = new ArrayList<>();
        List<Double> distanceQ = new ArrayList<>();

        for (int[] elementTrainingSet : TrainingSet) {
            Double euclidean = euclidean(pointFromTestSet, elementTrainingSet);
            if (ClassLabels[elementTrainingSet[0]] == 0) {
                distanceA.add(euclidean);
            } else {
                distanceQ.add(euclidean);
            }
        }

        for (int j = 0; j < k; j++) {
                Double minA = Collections.min(distanceA);
                Double minQ = Collections.min(distanceQ);
                if (Objects.equals(minA, minQ)) {
                    distanceA.remove(minA);
                    distanceA.remove(minQ);
                } else {
                    if(minA < minQ)
                    {
                        distanceA.remove(minA);
                        countA++;
                    }
                    else{
                        distanceQ.remove(minQ);
                        countB++;
                    }
                }
        }
        return countA > countB;
    }


    public void update(KNNClassifier another) {
        this.k = another.k;
        trainCountA = another.trainCountA;
        trainCountQ = another.trainCountQ;
        ClassLabels = another.ClassLabels;
        TrainingSet = another.TrainingSet;
        TestSet = another.TestSet;
    }
}