import Jama.Matrix;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author pooler
 */
abstract class Classifier {

    int TRAIN_SET = 0, TEST_SET = 1;
    int trainCountA, trainCountB;
    int[] ClassLabels;
    int[] Count;
    int[][] TrainingSet, TestSet;
    double[][] dataSet;
    final double percent = 100;

    public Classifier(double[][] dataSet, int[] ClassLabels, int[] SampleCount) {
        this.dataSet = new Matrix(dataSet).transpose().getArray();
        this.ClassLabels = ClassLabels;
        this.Count = SampleCount;
    }

    void generateTrainingAndTestSets(String TrainSetSize) {
        int[] Index = new int[dataSet.length];
        int TrainCount = 0, TestCount = 0;
        double Th = Double.parseDouble(TrainSetSize) / 100.0;

        for (int i = 0; i < dataSet.length; i++)
            if (Math.random() <= Th) {
                Index[i] = TRAIN_SET;
                TrainCount++;
            } else {
                Index[i] = TEST_SET;
                TestCount++;
            }
        TrainingSet = new int[TrainCount][dataSet[0].length];
        TestSet = new int[TestCount][dataSet[0].length];
        TrainCount = 0;
        TestCount = 0;

        for (int i = 0; i < Index.length; i++) {
            for (int j = 0; j < dataSet[0].length; j++) {
                if (Index[i] == TRAIN_SET) {
                    TrainingSet[TrainCount][j] = i;
                } else
                    TestSet[TestCount][j] = i;
            }
            if (Index[i] == TRAIN_SET) {
                TrainCount++;
            } else {
                TestCount++;
            }
        }
    }

    abstract double execute();
}