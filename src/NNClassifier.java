
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pooler
 */
class NNClassifier extends Classifier {

    public NNClassifier(double[][] FNew, int[] ClassLabels, int[] SampleCount) {
        super(FNew, ClassLabels, SampleCount);
    }

    //do klasyfikacji
    @Override
    double execute() {
        int match = 0;
        final double percent = 100;
        double distances[] = new double[10];

        //for po wszystkich elementach zbioru testowego
        for(int[] i : TestSet) {
            distances = distance(dataSet[(int)i[0]]);
            if(distances[0] < distances[1]){
                if(ClassLabels[(int)i[0]] == 0)
                    match++;
            } else {
                if(ClassLabels[(int)i[0]] == 1)
                    match++;
            }
        }
        return percent * match/TestSet.length;
    }

    //funkcja liczy odleglosc miedzy parametrem wejsciowy - elementem zbioru testowego, a wszystkimi kolejnymi elementami
    // zbioru treningowego, o czym swiadczy for ponizej
    //zaleznie od classlabels rozpoznana zostaje klasa, zwracana jest odleglosc od klasy A i od klasy Q
    private double[] distance(double[] point) {
        List<Double> distanceA,distanceB; 
        double[] result;

        result = new double[2];        
        distanceA = new ArrayList<>();
        distanceB = new ArrayList<>();
        for(int i=0; i<TrainingSet.length; i++){
            if(ClassLabels[TrainingSet[i][0]]==0){
                distanceA.add(euclidean(point,TrainingSet[i]));
            } else {
                distanceB.add(euclidean(point,TrainingSet[i]));
            }
        }
        result[0] = Collections.min(distanceA);
        result[1] = Collections.min(distanceB);
        return result;
    }
    
    double euclidean(double[] point, int[] i) {
        double sum = 0;
        for (int j = 0; j < point.length; j++) {
            sum += Math.pow(dataSet[(int) i[0]][j] - point[j], 2);
        }
        return Math.sqrt(sum);
    }
}
