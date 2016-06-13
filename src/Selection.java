import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by pooler on 13.06.2016.
 */

class Selection {
    String selectFeaturesSFS(int d, Data data) {
        int max[] = new int[d];
        double G[][] = new double[d][];
        String out = "";

        max[0] = selectFeatures(1, data)[0];
        G[0] = data.F[max[0]];

        for (int j = 1; j < d; j++) {
            double FLD = 0, tmp;
            double tmpG[][] = new double[j + 1][];

            System.arraycopy(G, 0, tmpG, 0, j);

            for (int i = 0; i < data.FeatureCount; i++) {
                final int finalI = i;
                if (!IntStream.of(max).anyMatch(x -> x == finalI)) {
                    tmpG[j] = data.F[i];
                    tmp = computeFisherMD(tmpG, data);
                    if (tmp > FLD) {
                        FLD = tmp;
                        max[j] = i;
                    }
                }
            }
            G[j] = data.F[max[j]];
        }

        updateFNew(d, max, data);

        for (int tmp : max) {
            out += tmp + " ";
        }
        return out;
    }

    int[] selectFeatures(int d, Data data) {
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
                map.put(tmp, idToString(id));
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
        System.out.println(last);

        if (d != 1) {
            numbers = Arrays.asList(last.split(" "))
                    .stream()
                    .map(String::trim)
                    .mapToInt(Integer::parseInt).toArray();
        }
        updateFNew(d, numbers, data);

        return numbers;
    }

    private void updateFNew(int d, int[] id, Data data) {
        data.FNew = new double[d][];
        for (int j = 0; j < d; j++) {
            data.FNew[j] = data.F[id[j]];
        }
    }

    private String idToString(int[] id) {
        String str = "";
        for (int i : id) {
            str += i + " ";
        }
        return str;
    }

    private int Fisher1D(int max_ind, Data data) {
        double FLD = 0, tmp;
        for (int i = 0; i < data.FeatureCount; i++) {
            if ((tmp = computeFisherLD(data.F[i], data)) > FLD) {
                FLD = tmp;
                max_ind = i;
            }
        }
        return max_ind;
    }

    private double computeFisherMD(double[][] vec, Data data) { //vec[0] vac[1] ... vec[n]
        int indexA;
        int indexB;
        double result = 0;
        double[] mA = new double[vec.length],
                mB = new double[vec.length];

        double[][]
                A = new double[vec.length][data.SampleCount[0]],
                B = new double[vec.length][data.SampleCount[1]];

        for (int a = 0; a < vec.length; a++) {
            mA[a] = 0;
            mB[a] = 0;
            indexA = 0;
            indexB = 0;
            for (int i = 0; i < vec[a].length; i++) {
                if (data.ClassLabels[i] == 0) {
                    mA[a] += vec[a][i];
                    A[a][indexA++] = vec[a][i];
                } else {
                    mB[a] += vec[a][i];
                    B[a][indexB++] = vec[a][i];
                }
            }

            mA[a] /= data.SampleCount[0];
            mB[a] /= data.SampleCount[1];

            int i;
            i = 0;
            for (double e : A[a]) {
                A[a][i] = e - mA[a];
                i++;
            }
            i = 0;
            for (double e : B[a]) {
                B[a][i] = e - mB[a];
                i++;
            }

            result += Math.pow((mB[a] - mA[a]), 2);
        }

        return Math.abs((Math.sqrt(result)) / (Data.computeCovarianceMatrix(B).det() + Data.computeCovarianceMatrix(A).det()));
    }

    private Generator<Integer> countCombinations(int n, Data data) {
        Vector<Integer> vector = new Vector<>();
        for (int i = 0; i < data.FeatureCount; i++) {
            vector.add(i);
        }
        ICombinatoricsVector<Integer> initialVector = Factory.createVector(vector);
        return Factory.createSimpleCombinationGenerator(initialVector, n);
    }

    private double computeFisherLD(double[] vec, Data data) {
        data.vec = vec;
        double mA = 0, mB = 0, sA = 0, sB = 0;
        for (int i = 0; i < vec.length; i++) {
            if (data.ClassLabels[i] == 0) {
                mA += vec[i];
                sA += vec[i] * vec[i];
            } else {
                mB += vec[i];
                sB += vec[i] * vec[i];
            }
        }
        mA /= data.SampleCount[0];
        mB /= data.SampleCount[1];
        sA = sA / data.SampleCount[0] - mA * mA;
        sB = sB / data.SampleCount[1] - mB * mB;
        return Math.abs(mA - mB) / (Math.sqrt(sA) + Math.sqrt(sB));
    }
}
