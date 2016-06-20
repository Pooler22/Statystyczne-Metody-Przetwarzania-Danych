/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author pooler
 */
class NMClassifier extends Classifier {

    double[] mA, mQ;

    NMClassifier(double[][] FNew, int[] ClassLabels) {
        super(FNew, ClassLabels);
    }

    @Override
    double execute() {
        int count = 0;

        mean();

        for (int[] elementTestSet : TestSet) {
            if (isShortenDistanceToClassA(dataSet[elementTestSet[0]])) {
                if (ClassLabels[elementTestSet[0]] == 0) {
                    count++;
                }
            } else {
                if (ClassLabels[elementTestSet[0]] == 1) {
                    count++;
                }
            }
        }
        return percent * count / TestSet.length;
    }

    private boolean isShortenDistanceToClassA(double[] point) {
        double distanceA = 0.0;
        double distanceQ = 0.0;

        for (int i = 0; i < point.length; i++) {
            distanceA += Math.pow(point[i] - mA[i], 2);
            distanceQ += Math.pow(point[i] - mQ[i], 2);
        }

        return Math.sqrt(distanceA) < Math.sqrt(distanceQ);
    }

    void mean() {
        int countA;
        int countQ;
        int selectedFeatures = dataSet[0].length;

        mA = new double[selectedFeatures];
        mQ = new double[selectedFeatures];

        for (int i = 0; i < selectedFeatures; i++) {
            mA[i] = mQ[i] = 0;
            countA = countQ = 0;
            for (int[] elementTrainSet : TrainingSet) {
                if (ClassLabels[elementTrainSet[i]] == 0) {
                    mA[i] += dataSet[elementTrainSet[i]][i];
                    countA++;
                } else {
                    mQ[i] += dataSet[elementTrainSet[i]][i];
                    countQ++;
                }
            }
            mA[i] /= countA;
            mQ[i] /= countQ;
        }
    }
}