import Jama.Matrix;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pooler on 04.05.2016.
 */
class Data {

    String InData; // dataset from a text file will be placed here
    String[] classNames;
    int FeatureCount = 0;
    int[] ClassLabels, SampleCount;
    double[] vec;
    double[][] F, FNew; // original feature matrix and transformed feature matrix

    void getDatasetParameters() throws Exception {
        // based on data stored in InData determine: class count and names, number of samples
        // and number of features; set the corresponding variables
        String stmp = InData, saux;
        // analyze the first line and get feature count: assume that number of features
        // equals number of commas
        saux = InData.substring(InData.indexOf(',') + 1, InData.indexOf('$'));
        if (saux.length() == 0) {
            throw new Exception("The first line is empty");
        }
        // saux stores the first line beginning from the first comma
        int count = 0;
        while (saux.indexOf(',') > 0) {
            saux = saux.substring(saux.indexOf(',') + 1);
            count++;
        }
        FeatureCount = count + 1; // the first parameter
        // Determine number of classes, class names and number of samples per class
        boolean New;
        int index = -1;
        List<String> NameList = new ArrayList<>();
        List<Integer> CountList = new ArrayList<>();
        List<Integer> LabelList = new ArrayList<>();
        while (stmp.length() > 1) {
            saux = stmp.substring(0, stmp.indexOf(' '));
            New = true;
            index++; // new class index
            for (int i = 0; i < NameList.size(); i++) {
                if (saux.equals(NameList.get(i))) {
                    New = false;
                    index = i; // class index
                }
            }
            if (New) {
                NameList.add(saux);
                CountList.add(0);
            } else {
                CountList.set(index, CountList.get(index) + 1);
            }
            LabelList.add(index); // class index for current row
            stmp = stmp.substring(stmp.indexOf('$') + 1);
        }
        // based on results of the above analysis, create variables
        classNames = new String[NameList.size()];
        for (int i = 0; i < classNames.length; i++) {
            classNames[i] = NameList.get(i);
        }
        SampleCount = new int[CountList.size()];
        for (int i = 0; i < SampleCount.length; i++) {
            SampleCount[i] = CountList.get(i) + 1;
        }
        ClassLabels = new int[LabelList.size()];
        for (int i = 0; i < ClassLabels.length; i++) {
            ClassLabels[i] = LabelList.get(i);
        }
    }

    void fillFeatureMatrix() throws Exception {
        // having determined array size and class labels, fills in the feature matrix
        int n = 0;
        String saux, stmp = InData;
        for (int aSampleCount : SampleCount) {
            n += aSampleCount;
        }
        if (n <= 0) {
            throw new Exception("no samples found");
        }
        F = new double[FeatureCount][n]; // samples are placed column-wise
        for (int j = 0; j < n; j++) {
            saux = stmp.substring(0, stmp.indexOf('$'));
            saux = saux.substring(stmp.indexOf(',') + 1);
            for (int i = 0; i < FeatureCount - 1; i++) {
                F[i][j] = Double.parseDouble(saux.substring(0, saux.indexOf(',')));
                saux = saux.substring(saux.indexOf(',') + 1);
            }
            F[FeatureCount - 1][j] = Double.parseDouble(saux);
            stmp = stmp.substring(stmp.indexOf('$') + 1);
        }
    }

    String readDataSet(PR_GUI pr_gui) {
        String name = "";
        String s_tmp, s_out = "";
        JFileChooser jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(".."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Datasets - plain text files", "txt");
        jfc.setFileFilter(filter);
        if (jfc.showOpenDialog(pr_gui) == JFileChooser.APPROVE_OPTION) {
            try {
                try (BufferedReader br = new BufferedReader(new FileReader(jfc.getSelectedFile()))) {
                    while ((s_tmp = br.readLine()) != null) {
                        s_out += s_tmp + '$';
                    }
                }
                name = jfc.getSelectedFile().getName();
            } catch (Exception ignored) {
            }
        }
        InData = s_out;
        return name;
    }

    static double[][] projectSamples(Matrix FOld, Matrix TransformMat) {
        return (FOld.transpose().times(TransformMat)).transpose().getArrayCopy();
    }

    static double[][] centerAroundMean(double[][] M) {
        double[] mean = new double[M.length];
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                mean[i] += M[i][j];
            }
        }
        for (int i = 0; i < M.length; i++) {
            mean[i] /= M[0].length;
        }
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                M[i][j] -= mean[i];
            }
        }
        return M;
    }

    static Matrix computeCovarianceMatrix(double[][] m) {
        Matrix M = new Matrix(m);
        Matrix MT = M.transpose();
        return M.times(MT);
    }

}
