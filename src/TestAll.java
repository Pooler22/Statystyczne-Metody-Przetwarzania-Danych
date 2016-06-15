
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JFileChooser;

/**
 * Created by pooler on 15.06.2016.
 */
public class TestAll {
    int numberFisherDimensions;
    int numberSFSDimensions;
    double treningPart;
    int kDimensions;
    int loopExecute;
    Data data;
    Classifier classifier;
    Selection selection;
    int nValue;
    
    public void init(int numberFisherDimensions, int numberSFSDimensions, 
            double treningPart, int kDimensions, int nValue, int loopExecute,
            Data data, PR_GUI pr_gui) throws Exception{
        this.numberFisherDimensions = numberFisherDimensions;
        this.numberSFSDimensions = numberSFSDimensions;
        this.treningPart = treningPart;
        this.kDimensions = kDimensions;
        this.nValue = nValue; 
            this.loopExecute = loopExecute;
        this.data = data;
        selection = new Selection();
        StringBuilder out = new StringBuilder();
        
        
        
        data.readDataSet(pr_gui);
        data.getDatasetParameters();
        out.append("Value Features Count: " + data.FeatureCount + "\n");
        out.append("Value Features Names: ");
        for(String s : data.classNames){
            out.append(s + " ");
        }
        out.append("\nFIsher selection ");
        
        data.fillFeatureMatrix();        

        for (int i : selection.selectFeatures(numberFisherDimensions, data)) {
            out.append(i + " ");
        }
        out.append(runClassifierExt(out));
        
        out.append("SFS: " + selection.selectFeaturesSFS(numberSFSDimensions, data) + "\n");
        
        out.append(runClassifierExt(out));
        
//        PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
//        writer.println(out);
//        writer.close();
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(pr_gui) == JFileChooser.APPROVE_OPTION) {
          //File file = fileChooser.getSelectedFile();
          try(FileWriter fw = new FileWriter(fileChooser.getSelectedFile()+".txt")) {
            fw.write(out.toString());
            fw.close();
          }
        }
        
    }

    private StringBuilder runClassifierExt(StringBuilder out) {
//        String out = "";
        classifier = new NNClassifier(data.FNew, data.ClassLabels);        
        out.append(runClassifier(classifier, nValue,out));

//        classifier = new NMClassifier(data.FNew, data.ClassLabels);        
//        out += runClassifier(classifier, nValue);
//        
//        classifier = new KNNClassifier(data.FNew, data.ClassLabels, kDimensions);        
//        out += runClassifier(classifier, nValue);
//        
//        classifier = new KNMClassifier(data.FNew, data.ClassLabels, data.SampleCount, kDimensions);
//        runClassifier(classifier, nValue);  
        return out;
    }
    
    private StringBuilder runClassifier(Classifier classifier, int nValue, StringBuilder out) {
//        String out = "";
        classifier.generateTrainingAndTestSets(treningPart);
        out.append("Classifie: " + classifier.execute() + "\n");
        out.append("Cross-validation: " + classifier.crossValidation(nValue)+"\n");
        out.append("Bootstrap: " + classifier.bootstrap(nValue)+"\n");
        return out;
    }

    
}
