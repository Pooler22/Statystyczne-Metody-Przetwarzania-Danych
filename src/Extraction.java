import Jama.Matrix;

/**
 * Created by pooler on 13.06.2016.
 */
public class Extraction {
    public static Matrix extractFeatures(Matrix C, double Ek, int k) {

        Matrix evecs, evals;
        // compute eigen values and eigen vectors
        evecs = C.eig().getV();
        evals = C.eig().getD();

        // PM: projection matrix that will hold a set dominant eigenvectors
        Matrix PM;
        if (k > 0) {
            // preset dimension of new feature space
//            PM = new double[evecs.getRowDimension()][k];
            PM = evecs.getMatrix(0, evecs.getRowDimension() - 1,
                    evecs.getColumnDimension() - k, evecs.getColumnDimension() - 1);
        } else {
            // dimension will be determined based on scatter energy
            double TotEVal = evals.trace(); // total energy
            double EAccum = 0;
            int m = evals.getColumnDimension() - 1;
            while (EAccum < Ek * TotEVal) {
                EAccum += evals.get(m, m);
                m--;
            }
            PM = evecs.getMatrix(0, evecs.getRowDimension() - 1, m + 1, evecs.getColumnDimension() - 1);
        }

        /*            System.out.println("Eigenvectors");
            for(int i=0; i<r; i++){
                for(int j=0; j<c; j++){
                    System.out.print(evecs[i][j]+" ");
                }
                System.out.println();
            }
            System.out.println("Eigenvalues");
            for(int i=0; i<r; i++){
                for(int j=0; j<c; j++){
                    System.out.print(evals[i][j]+" ");
                }
                System.out.println();
            }
         */
        return PM;
    }
}
