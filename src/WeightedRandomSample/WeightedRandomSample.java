package WeightedRandomSample;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Administrator on 7/2/2017.
 */
public class WeightedRandomSample {

    public static void main(String[] args) {
        double[] w = new double[]{0.4, 0.5, 0.1};
        int[] result = weightedRandomSample(w, 100);
        int[] color = new int[3];
        for(int i : result) {
            color[i]++;
        }
        System.out.println("red: " + color[0]);
        System.out.println("yellow: " + color[1]);
        System.out.println("blue: " + color[2]);
        System.out.println(Arrays.toString(result));
    }

    private static int[] weightedRandomSample(double[] w, int n) {
        int[] result = new int[n];
        double[] CDF = new double[w.length];
        double sumOfW = 0;
        Random random = new Random(System.currentTimeMillis());
        for(int i=0; i<w.length; i++) {
            sumOfW += w[i];
            CDF[i] = sumOfW;
        }
        for(int i=0; i<n; i++) {
            double currRandom = random.nextDouble() * sumOfW;
            for(int j=0; j<w.length; j++) {
                if(CDF[j] >= currRandom) {
                    result[i] = j;
                    break;
                }
            }
        }
        return result;
    }
}
