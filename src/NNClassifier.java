
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pooler
 */
class NNClassifier extends Classifier {

    public NNClassifier(int[] ClassLabels, int[] SampleCount) {
        super(ClassLabels, SampleCount);
    }

    @Override
    protected void trainClissifier(double[][] TrainSet) {
  //      super.trainClissifier(TrainSet);
    }

    private double[] computeDistance(double[] point) {
        List<Double> distanceA, distanceB;
        double[] result = new double[2];
        distanceA = new ArrayList<Double>();
        distanceB = new ArrayList<Double>();
        for (double[] i : TrainingSet) {
            if (ClassLabels[(int) i[0]] == 0) {
                double sumA = 0;
                for (int j = 0; j < point.length; j++) {
                    sumA += Math.pow(Dataset[(int) i[0]][j] - point[j], 2);
                }
                distanceA.add(Math.sqrt(sumA));
            } else {
                double sumB = 0;
                for (int j = 0; j < point.length; j++) {
                    sumB += Math.pow(Dataset[(int) i[0]][j] - point[j], 2);
                }
                distanceB.add(Math.sqrt(sumB));
            }
        }
        result[0] = Collections.min(distanceA);
        result[1] = Collections.min(distanceB);
        return result;
    }

    @Override
    double cauculate() {
        double distances[];
        int match = 0;
        for (double[] i : TestSet) {
            distances = computeDistance(Dataset[(int) i[0]]);
            if (distances[0] < distances[1]) {
                if (ClassLabels[(int) i[0]] == 0) {
                    match++;
                }
            } else if (ClassLabels[(int) i[0]] == 1) {
                match++;
            }
        }
        return 100.0 * match / TestSet.length;
    }
}
