
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
    double[][] dataSet;
    double[] mA, mB;

    public Classifier(int[] ClassLabels, int[] SampleCount) {
        this.ClassLabels = ClassLabels;
        this.Count = SampleCount;
    }

    void generateTrainingAndTestSets(double[][] Dataset, String TrainSetSize) {
        this.dataSet = (new Matrix(Dataset)).transpose().getArray();

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
        // label vectors for training/test sets
        TrainCount = 0;
        TestCount = 0;
        for (int i = 0; i < Index.length; i++) {

                if (Index[i] == TRAIN_SET) {
                    for (int j = 0; j < this.dataSet[0].length; j++) {
                        TrainingSet[j][TrainCount++] = i;
                    }
                } else {
                    for (int j = 0; j < this.dataSet[0].length; j++) {
                        TestSet[j][TestCount++] = i;
                    }
                }

        }
    }
    
    abstract double execute();
}
