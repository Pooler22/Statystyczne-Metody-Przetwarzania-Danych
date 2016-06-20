import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by pooler on 13.06.2016.
 */

class Selection {
    String selectFeatures(int numberOfDimensions, Data data) {
        int[] selectedIDs = new int[numberOfDimensions];
        String last = "";

        if (numberOfDimensions == 1) {
            selectedIDs[0] = Fisher1D(selectedIDs[0], data);
        } else {
            double tmp;
            Map<Double, String> mapWithIDsAndResults;
            Generator<Integer> vector;
            double[][] G = new double[numberOfDimensions][];

            mapWithIDsAndResults = new HashMap<>();
            vector = countCombinations(numberOfDimensions, data);

            for (ICombinatoricsVector<Integer> combination : vector) {

                for (int i = 0; i < combination.getSize(); i++) {
                    G[i] = data.F[combination.getValue(i)];
                    selectedIDs[i] = combination.getValue(i);
                }
                tmp = computeFisherMD(G, data);
                mapWithIDsAndResults.put(tmp, intArrayToString(selectedIDs));
            }

            Set set = new TreeMap<>(mapWithIDsAndResults).entrySet();
            for (Object elementOfSet : set) {
                last = ((Map.Entry) elementOfSet).getValue().toString();
            }

            selectedIDs = Arrays.asList(last.split("\n"))
                    .stream()
                    .map(String::trim)
                    .mapToInt(Integer::parseInt).toArray();
        }

        updateFNew(numberOfDimensions, selectedIDs, data);

        return intArrayToString(selectedIDs);
    }

    String selectFeaturesSFS(int numberOfDimensions, Data data) {
        int selectedIDS[] = new int[numberOfDimensions];
        double G[][] = new double[numberOfDimensions][];
        double FLD, tmp;

        selectedIDS[0] = Integer.parseInt(selectFeatures(1, data).trim());
        G[0] = data.F[selectedIDS[0]];

        for (int i = 1; i < numberOfDimensions; i++) {
            double tmpG[][] = new double[i + 1][];
            FLD = 0;

            System.arraycopy(G, 0, tmpG, 0, i); // src, posSrc, dest, posDest, length

            for (int j = 0; j < data.FeatureCount; j++) {
                final int finalI = j;
                if (!IntStream.of(selectedIDS).anyMatch(x -> x == finalI)) {
                    tmpG[i] = data.F[j];
                    tmp = computeFisherMD(tmpG, data);
                    if (tmp > FLD) {
                        FLD = tmp;
                        selectedIDS[i] = j;
                    }
                }
            }
            G[i] = data.F[selectedIDS[i]];
        }

        updateFNew(numberOfDimensions, selectedIDS, data);

        return intArrayToString(selectedIDS);
    }


    private void updateFNew(int dimension, int[] id, Data data) {
        data.FNew = new double[dimension][];
        for (int j = 0; j < dimension; j++) {
            data.FNew[j] = data.F[id[j]];
        }
    }

    private String intArrayToString(int[] id) {
        String str = "";
        for (int i : id) {
            str += i + "\n";
        }
        return str;
    }

    private int Fisher1D(int maxInd, Data data) {
        double FLD = 0, tmp;
        for (int i = 0; i < data.FeatureCount; i++) {
            tmp = computeFisherLD(data.F[i], data);
            if (tmp > FLD) {
                FLD = tmp;
                maxInd = i;
            }
        }
        return maxInd;
    }

    private double computeFisherMD(double[][] vec, Data data) {
        double result = 0;

        int countA;
        int countQ;

        double[] mA = new double[vec.length];
        double[] mQ = new double[vec.length];

        double[][] A = new double[vec.length][data.SampleCount[0]];
        double[][] Q = new double[vec.length][data.SampleCount[1]];

        for (int i = 0; i < vec.length; i++) {
            mA[i] = 0;
            mQ[i] = 0;
            countA = 0;
            countQ = 0;
            for (int j = 0; j < vec[i].length; j++) {
                if (data.ClassLabels[j] == 0) {
                    mA[i] += vec[i][j];
                    A[i][countA++] = vec[i][j];
                } else {
                    mQ[i] += vec[i][j];
                    Q[i][countQ++] = vec[i][j];
                }
            }

            mA[i] /= data.SampleCount[0];
            mQ[i] /= data.SampleCount[1];

            int k;
            k = 0;
            for (double e : A[i]) {
                A[i][k] = e - mA[i];
                k++;
            }
            k = 0;
            for (double e : Q[i]) {
                Q[i][k] = e - mQ[i];
                k++;
            }

            result += Math.pow((mQ[i] - mA[i]), 2);
        }

        return Math.abs((Math.sqrt(result)) / (Data.computeCovarianceMatrix(Q).det() + Data.computeCovarianceMatrix(A).det()));
    }

    private Generator<Integer> countCombinations(int n, Data data) {
        Vector<Integer> vector = new Vector<>();
        for (int i = 0; i < data.FeatureCount; i++) {
            vector.add(i);
        }
        return Factory.createSimpleCombinationGenerator(Factory.createVector(vector), n);
    }

    private double computeFisherLD(double[] vec, Data data) {
        double meanA = 0, meanQ = 0;
        double sA = 0, sQ = 0;

        for (int i = 0; i < vec.length; i++) {
            if (data.ClassLabels[i] == 0) {
                meanA += vec[i];
                sA += Math.pow(vec[i], 2);
            } else {
                meanQ += vec[i];
                sQ += Math.pow(vec[i], 2);
            }
        }

        meanA /= data.SampleCount[0];
        meanQ /= data.SampleCount[1];
        sA = sA / data.SampleCount[0] - Math.pow(meanA, 2);
        sQ = sQ / data.SampleCount[1] - Math.pow(meanQ, 2);

        return Math.abs(meanA - meanQ) / (Math.sqrt(sA) + Math.sqrt(sQ));
    }
}
