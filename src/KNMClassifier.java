/**
 * Created by pooler.
 */
public class KNMClassifier extends Classifier {

    int k;

    public KNMClassifier(double[][] dataSet, int[] ClassLabels, int[] SampleCount, int k) {
        super(dataSet, ClassLabels, SampleCount);
        this.k = k;
    }

    @Override
    double execute() {
        return 0;
    }
}
