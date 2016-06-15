import Jama.Matrix;

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

    final double percent = 100;
    int TRAIN_SET = 0, TEST_SET = 1;
    int trainCountA, trainCountQ;
    int[] ClassLabels;
    int[][] TrainingSet, TestSet;
    double[][] dataSet;

    Classifier(double[][] dataSet, int[] ClassLabels) {
        super();
        this.dataSet = new Matrix(dataSet).transpose().getArray();
        this.ClassLabels = ClassLabels;
    }

    void generateTrainingAndTestSets(double trainSetSize) {
        int numberOfElements = dataSet.length;
        int selectedFeatures = dataSet[0].length;
        int[] Index = new int[numberOfElements];
        int TrainCount = 0, TestCount = 0;
        double trainSetSizeInPercent = trainSetSize / percent;

        for (int i = 0; i < Index.length; i++) {
            if (Math.random() <= trainSetSizeInPercent) {
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

    double euclidean(double[] elements, int[] k) {
        double sum = 0.0;
        for (int i = 0; i < elements.length; i++) {
            sum += Math.pow(dataSet[k[0]][i] - elements[i], 2);
        }
        return Math.sqrt(sum);
    }

    double crossValidation(int parts) {
        double result = 0;

        for (int i = 0; i < parts; i++) {
            this.generateTrainingAndTestSetsExt(i, parts);
            result += this.execute();
        }

        return result / parts;
    }

    private void generateTrainingAndTestSetsExt(int part, int parts) {

        int numberOfElements = dataSet.length;
        int selectedFeatures = dataSet[0].length;

        int[] Index = new int[numberOfElements];
        int TrainCount = 0, TestCount = 0;
        int start = part * (numberOfElements / parts);
        int end = (part + 1) * (numberOfElements / parts);

        for (int i = 0; i < Index.length; i++) {
            if ((i < start) || (i > end)) {
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

    double bootstrap(int parts) {
        double result = 0;

        for (int i = 0; i < parts; i++) {
            this.generateTrainingAndTestSetsExtBootstrap(generateParts(parts), parts, i);
            result += this.execute();
        }

        return result / parts;
    }

    private int[] generateParts(int parts) {
        int partsTab[] = new int[parts];
        Random generator = new Random();
        for (int i = 0; i < parts; i++) {
            partsTab[i] = generator.nextInt(10);
        }
        return partsTab;
    }

    private void generateTrainingAndTestSetsExtBootstrap(int[] partTab, int parts, int part) {
        int numberOfElements = dataSet.length;
        int selectedFeatures = dataSet[0].length;

        int[] Index = new int[numberOfElements];
        int TestCount = 0;
        int start = part * (numberOfElements / parts);
        int end = (part + 1) * (numberOfElements / parts);

        for (int i = 0; i < Index.length; i++) {
            if ((i > start) || (i < end)) {
                Index[i] = TEST_SET;
                TestCount++;
            }
        }

        TestSet = new int[TestCount][selectedFeatures];
        TestCount = 0;

        for (int i = 0; i < numberOfElements; i++) {
            if (Index[i] == TEST_SET) {
                for (int j = 0; j < selectedFeatures; j++) {
                    TestSet[TestCount][j] = i;
                }
                TestCount++;
            }
        }

        int TrainCount = 0;

        TrainingSet = new int[numberOfElements][selectedFeatures];

        for (int i = 0; i < numberOfElements; i++) {
            for (int j = 0; j < selectedFeatures; j++) {
                int currentPart = (int) Math.floor(((double) i) / (numberOfElements / (parts - 1)));
                int partSize = (int) Math.floor((numberOfElements / parts));
                TrainingSet[TrainCount][j] =
                        (partTab[currentPart] * partSize) + (i % partSize);
            }
            TrainCount++;
        }
    }
}