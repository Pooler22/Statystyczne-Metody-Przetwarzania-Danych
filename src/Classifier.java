
import Jama.Matrix;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pooler
 */
abstract class Classifier {

    double[][] TrainingSet, TestSet;
    int[] ClassLabels;
    final int TRAIN_SET = 0, TEST_SET = 1;

    int[] Count;
    int trainCount, testCount;
    double[][] Dataset;
    double[] mA, mB;

    public Classifier(int[] ClassLabels, int[] SampleCount) {
        this.ClassLabels = ClassLabels;
        this.Count = SampleCount;
    }

    abstract double cauculate();

    void generateTraining_and_Test_Sets(double[][] Dataset, String TrainSetSize) {
        this.Dataset = (new Matrix(Dataset)).transpose().getArray();

        int[] Index = new int[Dataset[0].length];
        double Th = Double.parseDouble(TrainSetSize) / 100.0;
        int TrainCount = 0, TestCount = 0;
        for (int i = 0; i < Dataset[0].length; i++) {
            if (Math.random() <= Th) {
                Index[i] = TRAIN_SET;
                TrainCount++;
            } else {
                Index[i] = TEST_SET;
                TestCount++;
            }
        }
        TrainingSet = new double[Dataset[0].length][TrainCount];
        TestSet = new double[Dataset[0].length][TestCount];
        TrainCount = 0;
        TestCount = 0;
        // label vectors for training/test sets
        for (int i = 0; i < Index.length; i++) {
            for (int j = 0; j < this.Dataset[0].length; j++) {
                if (Index[i] == TRAIN_SET) {
                    TrainingSet[TrainCount++][j] = i;
                } else {
                    TestSet[TestCount++][j] = i;
                }
            }
        }
    }

    void computeMean(double[][] trainSet) {
        mA = new double[trainSet[0].length];
        mB = new double[trainSet[0].length];
        int countA = 0, countB = 0;

        for (int i = 0; i < mA.length; i++) {
            mA[i] = mB[i] = 0;
        }
        for (int j = 0; j < mA.length; j++) {
            countA = countB = 0;
            for (double[] i : trainSet) {
                if (ClassLabels[(int) i[j]] == 0) {
                    mA[j] += Dataset[(int) i[j]][j];
                    countA++;
                } else {
                    mB[j] += Dataset[(int) i[j]][j];
                    countB++;
                }
            }
        }
        for (int i = 0; i < mA.length; i++) {
            mA[i] /= countA;
            mB[i] /= countB;
        }
    }

    abstract protected void trainClissifier(double[][] TrainSet);
}
