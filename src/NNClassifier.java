/**
 * @author pooler
 */

class NNClassifier extends KNNClassifier {
    NNClassifier(double[][] FNew, int[] ClassLabels) {
        super(FNew, ClassLabels, 1);
    }
}
