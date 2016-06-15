import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by pooler on 13.06.2016.
 */

class Selection {
    String selectFeaturesSFS(int dimensions, Data data) {
        int max[] = new int[dimensions];
        double G[][] = new double[dimensions][];
        double FLD, tmp;

        max[0] = Integer.parseInt(selectFeatures(1, data).trim());
        G[0] = data.F[max[0]];

        for (int i = 1; i < dimensions; i++) {
            double tmpG[][] = new double[i + 1][];
            FLD = 0;
            System.arraycopy(G, 0, tmpG, 0, i);

            for (int j = 0; j < data.FeatureCount; j++) {
                final int finalI = j;
                if (!IntStream.of(max).anyMatch(x -> x == finalI)) {
                    tmpG[i] = data.F[j];
                    tmp = computeFisherMD(tmpG, data);
                    if (tmp > FLD) {
                        FLD = tmp;
                        max[i] = j;
                    }
                }
            }
            G[i] = data.F[max[i]];
        }

        updateFNew(dimensions, max, data);

        return intArrayToString(max);
    }

    String selectFeatures(int d, Data data) {
        int[] id = new int[d];
        int[] numbers = new int[d];
        String last = "";
        if (d == 1) {
            numbers[0] = id[0] = Fisher1D(id[0], data);
        } else {
            double FLD = 0, tmp;
            Map<Double, String> map;
            map = new HashMap<>();

            Generator<Integer> vector = countCombinations(d, data);
            //k = 2
            for (ICombinatoricsVector<Integer> combination : vector) {
                double[][] G = new double[d][];
                for (int i = 0; i < combination.getSize(); i++) {
                    G[i] = data.F[combination.getValue(i)];
                    id[i] = combination.getValue(i);
                }
                tmp = computeFisherMD(G, data);
                map.put(tmp, intArrayToString(id));
                if (tmp > FLD) {
                    FLD = tmp;
                }
            }

            Map<Double, String> map1 = new TreeMap<>(map);
            Set set2 = map1.entrySet();
            for (Object aSet2 : set2) {
                Map.Entry me2 = (Map.Entry) aSet2;
                last = me2.getValue().toString();
            }
        }

        if (d != 1) {
            numbers = Arrays.asList(last.split("\n"))
                    .stream()
                    .map(String::trim)
                    .mapToInt(Integer::parseInt).toArray();
        }

        updateFNew(d, numbers, data);

        return intArrayToString(numbers);
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
            if ((tmp = computeFisherLD(data.F[i], data)) > FLD) {
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
        data.vec = vec;
        double mA = 0, mQ = 0;
        double sA = 0, sQ = 0;

        for (int i = 0; i < vec.length; i++) {
            if (data.ClassLabels[i] == 0) {
                mA += vec[i];
                sA += Math.pow(vec[i],2);
            } else {
                mQ += vec[i];
                sQ +=  Math.pow(vec[i],2);
            }
        }

        mA /= data.SampleCount[0];
        mQ /= data.SampleCount[1];
        sA = sA / data.SampleCount[0] - mA * mA;
        sQ = sQ / data.SampleCount[1] - mQ * mQ;

        return Math.abs(mA - mQ) / (Math.sqrt(sA) + Math.sqrt(sQ));
    }
}
