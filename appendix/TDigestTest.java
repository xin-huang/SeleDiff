import com.tdunning.math.stats.ArrayDigest;
import com.tdunning.math.stats.TDigest;
import org.apache.commons.math3.stat.StatUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Class {@code TDigestTest} compares estimation of medians
 * from t-digest and quickselect.
 *
 * @author Xin Huang {@code <xin.huang07@gmail.com>}
 */
public class TDigestTest {

    public static void main(String[] args) {

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("tDigestMedians.txt"));
            for (int i = 0; i < 10000; i++) {
                double[] arr = generateRandomNums();
                double tMedian = tdigestMedian(arr);
                double qMedian = quickselectMedian(arr);
                bw.write(String.valueOf(tMedian-qMedian));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static double[] generateRandomNums() {

        Random r = new Random();
        double[] arr = new double[1000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = r.nextDouble();
        }

        return arr;

    }

    private static double tdigestMedian(double[] arr) {

        ArrayDigest digest = TDigest.createArrayDigest(100);
        for (double d:arr) {
            digest.add(d);
        }

        return digest.quantile(0.5d);

    }

    private static double quickselectMedian(double[] arr) {
        return StatUtils.percentile(arr, 50);
    }

}
