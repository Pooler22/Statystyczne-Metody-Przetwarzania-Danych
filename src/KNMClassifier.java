import Jama.Matrix;

import static java.lang.Math.*;

/**
 * Created by pooler.
 */
public class KNMClassifier extends Classifier {

    int k;
    private double[] mA, mB;
    double[][] macierz_z_probkami_A;
    double[][] macierz_z_probkami_B;

    public KNMClassifier(double[][] dataSet, int[] ClassLabels, int[] SampleCount, int k) {
        super(dataSet, ClassLabels, SampleCount);
        this.k = k;
    }

    @Override
    double execute() {

        int match = 0;
        double distances[];

        computeMean(TrainingSet);
        //TestSet
//        for (int[] elementTestSet : TestSet) {
//            //distances = distance(dataSet[elementTestSet[0]]);
////            if (distances[0] < distances[1]) {
////                if (ClassLabels[elementTestSet[0]] == 0) {
////                    match++;
////                }
////
////            } else {
////                if (ClassLabels[elementTestSet[0]] == 1) {
////                    match++;
////                }
////            }
//        }

       // return percent * match / TestSet.length;

        return 0.0;
    }

    private void computeMean(int[][] trainSet) {
        mA = new double[trainSet[0].length];
        mB = new double[trainSet[0].length];
        int countA = 0;
        int countB = 0;
        for (int i = 0; i < mA.length; i++) {
            mA[i] = mB[i] = 0;
        }
        for (int j = 0; j < mA.length; j++) {
            countA = countB = 0;
            for (int[] aTrainSet : trainSet) {
                if (ClassLabels[aTrainSet[j]] == 0) {
                    mA[j] += dataSet[aTrainSet[j]][j];
                    countA++;
                } else {
                    mB[j] += dataSet[aTrainSet[j]][j];
                    countB++;
                }
            }
        }
        trainCountA = countA;
        trainCountB = countB;
        for (int i = 0; i < mA.length; i++) {
            mA[i] /= countA;
            mB[i] /= countB;
        }

        macierz_z_probkami_A = new double[mA.length][countA];
        macierz_z_probkami_B = new double[mA.length][countB];

        for (int j = 0; j < mA.length; j++) {
            countA = countB = 0;
            for (int kolumny = 0; kolumny < trainSet.length; kolumny++) {
                //for (int[] aTrainSet : trainSet) {
                if (ClassLabels[trainSet[kolumny][j]] == 0) {
                    macierz_z_probkami_A[j][countA] = dataSet[trainSet[kolumny][j]][j];
                    countA++;
                } else {
                    macierz_z_probkami_B[j][countB] = dataSet[trainSet[kolumny][j]][j];
                    countB++;
                }
            }
        }

        countA = countB = 0;
        for (int kolumny = 0; kolumny < TestSet.length; kolumny++) {
            if (ClassLabels[TestSet[kolumny][0]] == 0) {
                //macierz_z_probkami_A[0][countA] = dataSet[TestSet[kolumny][0]][0];
                countA++;
            } else {
                //macierz_z_probkami_B[0][countB] = dataSet[TestSet[kolumny][0]][0];
                countB++;
            }
        }


        Matrix CovA = Data.computeCovarianceMatrix(macierz_z_probkami_A);
        Matrix CovB = Data.computeCovarianceMatrix(macierz_z_probkami_B);


        double[][] xA = new double[mA.length][countA];
        double[][] xQ = new double[mA.length][countB];

        for (int wiersz = 0; wiersz < mA.length; wiersz++) {
            countA = countB = 0;
            for (int kolumny = 0; kolumny < TestSet.length; kolumny++) {
                //for (int[] aTrainSet : trainSet) {
                if (ClassLabels[TestSet[kolumny][wiersz]] == 0) {
                    xA[wiersz][countA] = dataSet[TestSet[kolumny][wiersz]][wiersz];
                    countA++;
                } else {
                    xQ[wiersz][countB] = dataSet[TestSet[kolumny][wiersz]][wiersz];
                    countB++;
                }
            }
        }


        for (int kolumny = 0; kolumny < countA; kolumny++) {
            for (int wiersz = 0; wiersz < mA.length; wiersz++) {
                xA[wiersz][kolumny] = xA[wiersz][kolumny] - mA[wiersz];
            }
        }
        for (int kolumny = 0; kolumny < countB; kolumny++) {
            for (int wiersz = 0; wiersz < mA.length; wiersz++) {
                xQ[wiersz][kolumny] = xQ[wiersz][kolumny] - mB[wiersz];
            }
        }
        int colCovA = CovA.getColumnDimension();
        int rowCovA = CovA.getRowDimension();
        int colCovQ = CovB.getColumnDimension();
        int rowCovQ = CovB.getRowDimension();

        Matrix iCovA = new Matrix(1,1);
        Matrix iCovQ = new Matrix(1,1);
        if ((colCovA == 1 && rowCovA == 1) || (colCovQ == 1 && rowCovQ == 1)) {
            if(CovA.get(0,0) == 0 || CovB.get(0,0) == 0){
                System.out.print("nic nie poradze, nie dziel przez 0");
            }
            else {
                iCovA.set(0,0, 1/CovA.get(0,0));
                iCovQ.set(0,0, 1/CovA.get(0,0));
            }
        }
        else{
            iCovA = CovA.inverse();
            iCovQ = CovB.inverse();
        }

        //double test = xA[0][2];
        for (int i = 0; i < mA.length; i++) {
            double[][] xA1 = new double[mA.length][countA];
            double[][] xQ1 = new double[mA.length][countB];
            for (int j = 0; j < countA; j++) {
                xA1[i][j] = xA[i][j];
            }
            for (int j = 0; j < countB; j++) {
                xQ1[i][j] = xQ[i][j];
            }

            Matrix mA = new Matrix(xA1);// new Matrix(xA[i]);
            Matrix mQ = new Matrix(xQ1);
            double a = sqrt(mA.transpose().times(CovA).times(mA).get(0, 0));
            double q = sqrt(mA.transpose().times(CovA).times(mA).get(0, 0));
        }
    }

}
