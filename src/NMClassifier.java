/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author pooler
 */
class NMClassifier extends NNClassifier {

    private double[] mA, mB;

    NMClassifier(double[][] FNew, int[] ClassLabels, int[] SampleCount) {
        super(FNew, ClassLabels, SampleCount);
    }

    @Override
    double execute() {
        int match = 0;
        double distances[];

        computeMean(TrainingSet);

        for (int[] elementTestSet : TestSet) {
            distances = distance(dataSet[elementTestSet[0]]);
            if (distances[0] < distances[1]) {
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

    private void computeMean(int[][] trainSet) {
        mA = new double[trainSet[0].length];
        mB = new double[trainSet[0].length];
        int countA = 0;
        int countB = 0;
        for (int i = 0; i < mA.length; i++) {
            mA[i] = mB[i] = 0;
        }
        for (int j = 0; j < mA.length; j++) {
            countA = countB = 0;
            for (int[] aTrainSet : trainSet) {
                if (ClassLabels[aTrainSet[j]] == 0) {
                    mA[j] += dataSet[aTrainSet[j]][j];
                    countA++;
                } else {
                    mB[j] += dataSet[aTrainSet[j]][j];
                    countB++;
                }
            }
        }
        trainCountA = countA;
        trainCountB = countB;
        for (int i = 0; i < mA.length; i++) {
            mA[i] /= countA;
            mB[i] /= countB;
        }
    }

    private double[] distance(double[] point) {
        double distanceA, distanceB;
        double[] result = new double[2];

        distanceA = distanceB = 0;

        for (int i = 0; i < point.length; i++) {
            distanceA += Math.pow(mA[i] - point[i], 2);
            distanceB += Math.pow(mB[i] - point[i], 2);
        }

        result[0] = Math.sqrt(distanceA);
        result[1] = Math.sqrt(distanceB);
        return result;
    }
}