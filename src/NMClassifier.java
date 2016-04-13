/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pooler
 */
class NMClassifier extends Classifier {
    
    @Override
    protected void trainClissifier(double[][] TrainSet){
 //       super.trainClissifier(TrainSet);
    }
    
    public NMClassifier(int[] ClassLabels, int[] SampleCount) {
        super(ClassLabels, SampleCount);
    }

    private double[] computeDistance(double[] point){
        double distanceA, distanceB;
        double[] result = new double[2];
        if (mA.length != point.length)
            return null;
        distanceA = distanceB = 0;
        for(int i=0; i<mA.length; i++){
            distanceA += (mA[i]-point[i])*(mA[i]-point[i]);
            distanceB += (mB[i]-point[i])*(mB[i]-point[i]);
        }
        distanceA = Math.sqrt(distanceA);
        distanceB = Math.sqrt(distanceB);
        result[0] = distanceA;
        result[1] = distanceB;
        return result;
    }
    @Override
    double cauculate() {
        double distances[];
        int match =0;
        computeMean(TrainingSet);
        for(double[] i : TestSet) {
            distances = computeDistance(Dataset[(int)i[0]]);
            if(distances[0] < distances[1]){
                if(ClassLabels[(int)i[0]] == 0)
                    match++;
            } else {
                if(ClassLabels[(int)i[0]] == 1)
                    match++;
            }
        }
        double result = 100.0 * match/TestSet.length;
        return result;
    }
}