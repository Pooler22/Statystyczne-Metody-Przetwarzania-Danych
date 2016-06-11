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


        int featuresCount = Dataset.length;
        int allprobes = Dataset[0].length;
        //dodaje 1 do wiersza bo dojda oznaczenia
        TrainingSet = new double[featuresCount+1][TrainCount];
        TestSet = new double[featuresCount+1][TestCount];
        // label vectors for training/test sets


        //tutaj sobie zobacz jak wyglada Dataset :)
        double var1 = Dataset[0][0];
        double var2 = Dataset[0][1];
        double var3 = Dataset[0][2];
        double var4 = Dataset[0][3];

        double var0 = Dataset[1][0];
        double var6 = Dataset[1][1];
        double var7 = Dataset[1][2];
        double var8 = Dataset[1][3];

        int marker = 0;
        //Dodawanie oznaczen dla klas

        //featuresCount tutaj dodaje 1 zeby zmeisil sie oznacznik dla klasy
        double[][] Dataset_with_marks = new double[featuresCount+1][Dataset[0].length];

        for(int i =0 ; i<Dataset[0].length;i++){
            Dataset_with_marks[0][i]=(double)ClassLabels[i];
            for(int j=1;j<featuresCount+1;j++){
                Dataset_with_marks[j][i]=Dataset[j-1][i];
            }
        }
        TrainCount = 0;
        TestCount = 0;
        int column=0;
        for (int i = 0; i < Index.length; i++) {

            if (Index[i] == TRAIN_SET) {

                for (int j = 0; j < featuresCount+1; j++) {
                    TrainingSet[j][TrainCount] = Dataset_with_marks[j][column];
                }
                TrainCount++;
                column++;

            } else {

                for (int j = 0; j < featuresCount+1; j++) {
                    TestSet[j][TestCount] = Dataset_with_marks[j][column];
                }
                TestCount++;
                column++;
            }

        }
    }

    abstract double execute();
}