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
    
    public NMClassifier(int[] ClassLabels, int[] SampleCount) {
        super(ClassLabels, SampleCount);
    }

    @Override
    double execute() {
        mean();
        return super.execute();
    }
    void mean() {
        mA = new double[TrainingSet[0].length];
        mB = new double[TrainingSet[0].length];
        int countA = 0, countB = 0;

        for (int i = 0; i < TrainingSet[0].length; i++) {
            mA[i] = mB[i] = 0;
        }
        for (int j = 0; j < TrainingSet[0].length; j++) {
            countA = countB = 0;
            for (double ts : TrainingSet[j]) {
                if (ClassLabels[(int) ts] == 0) {
                    mA[j] += dataSet[(int)ts][j];
                    countA++;
                } else {
                    mB[j] += dataSet[(int)ts][j];
                    countB++;
                }
            }
        }
        for (int i = 0; i < mA.length; i++) {
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