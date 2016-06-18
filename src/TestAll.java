import javax.swing.*;
import java.io.FileWriter;

/**
 * Created by pooler on 15.06.2016.
 */
class TestAll {
    private double treningPart;
    private Data data;
    private int nValue;
    int kDimensions;

    void init(int numberFisherDimensions, int numberSFSDimensions,
              double treningPart, int kDimensions, int nValue, int loopExecute,
              Data data, PR_GUI pr_gui) throws Exception {
        this.treningPart = treningPart;
        this.nValue = nValue;
        this.data = data;
        this.kDimensions = kDimensions;
        Selection selection = new Selection();
        StringBuilder out = new StringBuilder();


        data.readDataSet(pr_gui);
        data.getDatasetParameters();
        out.append("Fisher selection ");

        data.fillFeatureMatrix();

        out.append(selection.selectFeatures(numberFisherDimensions, data));

        out.append(runClassifierExt(out, loopExecute));

        out.append(" SFS: ").append(selection.selectFeaturesSFS(numberSFSDimensions, data));

        out.append(runClassifierExt(out, loopExecute));

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(pr_gui) == JFileChooser.APPROVE_OPTION) {
            //File file = fileChooser.getSelectedFile();
            try (FileWriter fw = new FileWriter(fileChooser.getSelectedFile() + ".txt")) {
                fw.write(out.toString());
                fw.close();
            }
        }

    }

    private StringBuilder runClassifierExt(StringBuilder out, int loop) {
        Classifier classifier;

        out.append("NN ");
        for (int i = 0; i < loop; i++) {

            classifier = new KNNClassifier(data.FNew, data.ClassLabels, 1);
            out.append(runClassifier(classifier, nValue, out));
        }
        out.append(" NM ");
        for (int i = 0; i < loop; i++) {

            classifier = new NMClassifier(data.FNew, data.ClassLabels);
            out.append(runClassifier(classifier, nValue, out));
        }
        out.append(" KNN ");
        for (int i = 0; i < loop; i++) {

            classifier = new KNNClassifier(data.FNew, data.ClassLabels, kDimensions);
            out.append(runClassifier(classifier, nValue, out));
        }
        out.append(" KNM ");
        for (int i = 0; i < loop; i++) {
            classifier = new KNMClassifier(data.FNew, data.ClassLabels, kDimensions);
            out.append(runClassifier(classifier, nValue, out));
        }
        return out.append("\n");
    }

    private StringBuilder runClassifier(Classifier classifier, int nValue, StringBuilder out) {
        classifier.generateTrainingAndTestSets(treningPart);
        out.append(classifier.execute()).append(" ");
        out.append(classifier.crossValidation(nValue)).append(" ");
        out.append(classifier.bootstrap(nValue));
        return out;
    }


}
