import Jama.Matrix;

/**
 * Created by pooler on 04.05.2016.
 */
public class Data {
    public static double[][] projectSamples(Matrix FOld, Matrix TransformMat) {
        return (FOld.transpose().times(TransformMat)).transpose().getArrayCopy();
    }

    public static double[][] centerAroundMean(double[][] M) {
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

    public static Matrix computeCovarianceMatrix(double[][] m) {
        Matrix M = new Matrix(m);
        Matrix MT = M.transpose();
        Matrix C = M.times(MT);
        return C;
    }
}
