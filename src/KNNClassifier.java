import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pooler.
 */

class KNNClassifier extends Classifier {

    private int k;

    KNNClassifier(double[][] dataSet, int[] ClassLabels, int k) {
        super(dataSet, ClassLabels);
        this.k = k;
    }

    @Override
    double execute() {
        int match = 0;

        for (int[] i : TestSet) {
            if (shortDistanceToClassA(dataSet[i[0]])) {
                if (ClassLabels[i[0]] == 0)
                    match++;
            } else {
                if (ClassLabels[i[0]] == 1)
                    match++;
            }
        }
        return percent * match / TestSet.length;
    }

    private boolean shortDistanceToClassA(double[] point) {
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

        double[] double_vector_A = new double[distanceA.size()];
        for (int i = 0; i < double_vector_A.length; i++) {
            double_vector_A[i] = distanceA.get(i);
        }

        double[] double_vector_Q = new double[distanceQ.size()];
        for (int i = 0; i < double_vector_Q.length; i++) {
            double_vector_Q[i] = distanceQ.get(i);
        }

        Arrays.sort(double_vector_A);
        Arrays.sort(double_vector_Q);

        double[] double_vector_A_to_compare = new double[k];
        double[] double_vector_Q_to_compare = new double[k];

        for (int i = 0; i < k; i++) {
            double_vector_A_to_compare[i] = double_vector_A[i];
            double_vector_Q_to_compare[i] = double_vector_Q[i];
        }

        int pointer_a = 0;
        int pointer_q = 0;

        for (double aDouble_vector_A_to_compare : double_vector_A_to_compare) {
            for (double aDouble_vector_Q_to_compare : double_vector_Q_to_compare) {
                if (aDouble_vector_A_to_compare > aDouble_vector_Q_to_compare) {
                    pointer_q++;
                } else {
                    pointer_a++;
                }
            }
        }

        double[] winners = new double[]{0.0, 1.0};

        if (pointer_q > pointer_a) {
            result[1] = winners[1];
        } else {
            result[0] = winners[0];
        }

        return result[0] < result[1];
    }
}