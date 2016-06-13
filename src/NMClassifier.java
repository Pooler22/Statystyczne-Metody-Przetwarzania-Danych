/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author pooler
 */
class NMClassifier extends NNClassifier {

    private double[] meanA, meanQ;

    NMClassifier(double[][] FNew, int[] ClassLabels) {
        super(FNew, ClassLabels);
    }

    @Override
    double execute() {
        int match = 0;

        computeMean();

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

    private void computeMean() {
        int countA = 0;
        int countQ = 0;
        int selectedFeatures = dataSet[0].length;

        meanA = new double[selectedFeatures];
        meanQ = new double[selectedFeatures];

        for (int i = 0; i < selectedFeatures; i++) {
            meanA[i] = meanQ[i] = 0;
            countA = countQ = 0;
            for (int[] elementTrainSet : TrainingSet) {
                if (ClassLabels[elementTrainSet[i]] == 0) {
                    meanA[i] += dataSet[elementTrainSet[i]][i];
                    countA++;
                } else {
                    meanQ[i] += dataSet[elementTrainSet[i]][i];
                    countQ++;
                }
            }
        }
        trainCountA = countA;
        trainCountB = countQ;

        for (int i = 0; i < selectedFeatures; i++) {
            meanA[i] /= countA;
            meanQ[i] /= countQ;
        }
    }

    private boolean shortDistanceToClassA(double[] point) {
        double distanceA = 0.0;
        double distanceQ = 0.0;

        for (int i = 0; i < point.length; i++) {
            distanceA += Math.pow(meanA[i] - point[i], 2);
            distanceQ += Math.pow(meanQ[i] - point[i], 2);
        }

        return Math.sqrt(distanceA) < Math.sqrt(distanceQ);
    }
}