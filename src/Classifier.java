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

    int[][] TrainingSet, TestSet;
    int[] ClassLabels;
    final int TRAIN_SET = 0, TEST_SET = 1;
    int[] classMarks;
    int trainCountA, trainCountB;

    int[] Count;
    double[][] dataSet;
    double[] mA, mB;

    public Classifier(double[][] FNew, int[] ClassLabels, int[] SampleCount) {
        Matrix temp = new Matrix(FNew);
        this.dataSet = temp.transpose().getArray();
        this.ClassLabels = ClassLabels;
        this.Count = SampleCount;
    }

    void generateTrainingAndTestSets(String TrainSetSize) {
        //dataSet = Dataset;
        int[] Index = new int[dataSet.length];
        double Th = Double.parseDouble(TrainSetSize)/100.0;
        int TrainCount=0, TestCount=0;
        //for ponizej zliczba wielkosc zbioru treningowego i tesotwego oraz zapisuje w Index kolejno wartosci 0 lub 1 w
        // zaleznosci do ktorego zbioru nalezy element
        for(int i=0; i<dataSet.length; i++)
            if(Math.random()<=Th) {
                Index[i]=TRAIN_SET;
                TrainCount++;
            }
            else {
                Index[i]=TEST_SET;
                TestCount++;
            }
        TrainingSet = new int[TrainCount][dataSet[0].length];
        TestSet = new int[TestCount][dataSet[0].length];
        TrainCount=0;
        TestCount=0;
        // for po Indexie i kolejny po ilosci wybranych wymiarow k, w tym forze przypisuje sie wartosci zaleznie do
        // TrainingSet lub TestSet gdzie i jes numerem indezu z tablicy Index
        for(int i=0; i<Index.length; i++){
            for (int j=0; j <dataSet[0].length; j++){
                if(Index[i]==TRAIN_SET){
                    TrainingSet[TrainCount][j] = i;
                    //TrainCount++;
                }
                else
                    TestSet[TestCount][j] = i;
                    //TestCount++;
            }
            if(Index[i]==TRAIN_SET){
                TrainCount++;
            } else
                TestCount++;
        }
    }

    abstract double execute();
}