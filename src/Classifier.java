import Jama.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    int[][] TrainingSet, TestSet;
    double[][] dataSet;
    final double percent = 100;

    public Classifier(double[][] dataSet, int[] ClassLabels) {
        this.dataSet = new Matrix(dataSet).transpose().getArray();
        this.ClassLabels = ClassLabels;
    }

    void generateTrainingAndTestSets(String TrainSetSizeInPercent) {

        int numberOfElements = dataSet.length;
        int selectedFeatures = dataSet[0].length;

        int[] Index = new int[numberOfElements];
        int TrainCount = 0, TestCount = 0;
        double Th = Double.parseDouble(TrainSetSizeInPercent) / percent;

        for (int i = 0; i < numberOfElements; i++) {
            if (Math.random() <= Th) {
                Index[i] = TRAIN_SET;
                TrainCount++;
            } else {
                Index[i] = TEST_SET;
                TestCount++;
            }
        }

        TrainingSet = new int[TrainCount][selectedFeatures];
        TestSet = new int[TestCount][selectedFeatures];
        TrainCount = 0;
        TestCount = 0;

        for (int i = 0; i < numberOfElements; i++) {
            if (Index[i] == TRAIN_SET) {
                for (int j = 0; j < selectedFeatures; j++) {
                    TrainingSet[TrainCount][j] = i;
                }
                TrainCount++;
            } else {
                for (int j = 0; j < selectedFeatures; j++) {
                    TestSet[TestCount][j] = i;
                }
                TestCount++;
            }
        }
    }

    abstract double execute();

    double euclidean(double[] element, int[] i) {
        double sum = 0;
        for (int j = 0; j < element.length; j++) {
            sum += Math.pow(dataSet[i[0]][j] - element[j], 2);
        }
        return Math.sqrt(sum);
    }

    double crossValidation(){
        return 0.0;
    }

    double bootstrap(){
        return 0.0;
    }
}