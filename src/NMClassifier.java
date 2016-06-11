/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pooler
 */
class NMClassifier extends NNClassifier {
    
    public NMClassifier(double[][] FNew,int[] ClassLabels, int[] SampleCount) {
        super(FNew, ClassLabels, SampleCount);
    }

    @Override
    double execute() {
        double distances[];
        int match =0;
        computeMean(TrainingSet);
        for(int i=0; i<TestSet.length; i++) {
            distances = distance(dataSet[TestSet[i][0]]);
            if(distances[0] < distances[1]){
                if(ClassLabels[TestSet[i][0]] == 0)
                    match++;
            } else {
                if(ClassLabels[TestSet[i][0]] == 1)
                    match++;
            }
        }
        double result = 100.0 * match/TestSet.length;
        return result;
    }
    void computeMean(int[][] trainSet){
        mA = new double[trainSet[0].length];
        mB = new double[trainSet[0].length];
        int countA = 0;
        int countB = 0;
        for (int i=0; i<mA.length; i++){
            mA[i] = mB[i] = 0;
        }
        for (int j=0; j<mA.length; j++){
            countA = countB=0;
            for (int i=0; i<trainSet.length; i++){
                if(ClassLabels[trainSet[i][j]]==0){
                    mA[j] += dataSet[trainSet[i][j]][j];
                    countA++;
                }
                else{
                    mB[j] += dataSet[trainSet[i][j]][j];
                    countB++;
                }
            }
        }
        trainCountA = countA;
        trainCountB = countB;
        for (int i=0; i<mA.length; i++){
            mA[i] /= countA;
            mB[i] /= countB;
        }
    }

    private double[] distance(double[] point){
        double distanceA, distanceB;
        double[] result = new double[2];
        
        distanceA = distanceB = 0;
        
        if (mA.length != point.length)
            return null;
        
        for(int i=0; i<point.length; i++){
            distanceA += Math.pow(mA[i]-point[i], 2);
            distanceB += Math.pow(mB[i]-point[i], 2);
        }

        result[0] = Math.sqrt(distanceA);
        result[1] = Math.sqrt(distanceB);
        return result;
    }    
}