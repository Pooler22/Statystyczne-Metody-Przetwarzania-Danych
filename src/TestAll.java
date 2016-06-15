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
        out.append("Value Features Count: ").append(data.FeatureCount).append("\n");
        out.append("Value Features Names: ");
        for (String s : data.ClassNames) {
            out.append(s).append(" ");
        }
        out.append("\nFIsher selection ");

        data.fillFeatureMatrix();

        out.append(selection.selectFeatures(numberFisherDimensions, data));

        out.append(runClassifierExt(out));

        out.append("SFS: ").append(selection.selectFeaturesSFS(numberSFSDimensions, data)).append("\n");

        out.append(runClassifierExt(out));

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(pr_gui) == JFileChooser.APPROVE_OPTION) {
            //File file = fileChooser.getSelectedFile();
            try (FileWriter fw = new FileWriter(fileChooser.getSelectedFile() + ".txt")) {
                fw.write(out.toString());
                fw.close();
            }
        }

    }

    private StringBuilder runClassifierExt(StringBuilder out) {
//        String out = "";
        Classifier classifier;
        classifier = new KNNClassifier(data.FNew, data.ClassLabels,1);
        out.append(runClassifier(classifier, nValue, out));

        classifier = new NMClassifier(data.FNew, data.ClassLabels);
        out.append(runClassifier(classifier, nValue, out));

        classifier = new KNNClassifier(data.FNew, data.ClassLabels, kDimensions);
        out.append(runClassifier(classifier, nValue, out));

        classifier = new KNMClassifier(data.FNew, data.ClassLabels, kDimensions);
        out.append(runClassifier(classifier, nValue, out));
        return out;
    }

    private StringBuilder runClassifier(Classifier classifier, int nValue, StringBuilder out) {
//        String out = "";
        classifier.generateTrainingAndTestSets(treningPart);
        out.append("Classifie: ").append(classifier.execute()).append("\n");
        out.append("Cross-validation: ").append(classifier.crossValidation(nValue)).append("\n");
        out.append("Bootstrap: ").append(classifier.bootstrap(nValue)).append("\n");
        return out;
    }


}
