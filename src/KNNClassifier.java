/**
 * Created by pooler.
 */
public class KNNClassifier extends Classifier {

    int k;

    public KNNClassifier(double[][] dataSet, int[] ClassLabels, int[] SampleCount, int k) {
        super(dataSet, ClassLabels, SampleCount);
        this.k = k;
    }

    @Override
    double execute() {
        return 0;
    }
}
