
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author pooler
 */
class NNClassifier extends Classifier {

    NNClassifier(double[][] FNew, int[] ClassLabels, int[] SampleCount) {
        super(FNew, ClassLabels, SampleCount);
    }

    @Override
    double execute() {
        int match = 0;

        double distances[];

        for (int[] i : TestSet) {
            distances = selectClass(dataSet[i[0]]);
            if (distances[0] < distances[1]) {
                if (ClassLabels[i[0]] == 0)
                    match++;
            } else {
                if (ClassLabels[i[0]] == 1)
                    match++;
            }
        }
        return percent * match / TestSet.length;
    }

    private double[] selectClass(double[] point) {
        double[] result;
        List<Double> distanceA, distanceQ;

        result = new double[2];
        distanceA = new ArrayList<>();
        distanceQ = new ArrayList<>();

        for (int[] elementTrainingSet : TrainingSet) {
            if (ClassLabels[elementTrainingSet[0]] == TRAIN_SET) {
                distanceA.add(euclidean(point, elementTrainingSet));
            } else {
                distanceQ.add(euclidean(point, elementTrainingSet));
            }
        }
        result[0] = Collections.min(distanceA);
        result[1] = Collections.min(distanceQ);
        return result;
    }

    private double euclidean(double[] element, int[] i) {
        double sum = 0;
        for (int j = 0; j < element.length; j++) {
            sum += Math.pow(dataSet[i[0]][j] - element[j], 2);
        }
        return Math.sqrt(sum);
    }
}
