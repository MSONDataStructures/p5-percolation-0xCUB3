import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/******************************************************************************
 *  Name:    Kevin Wayne
 *  Dependencies: StdIn.java StdRandom.java WeightedQuickUnionUF.java
 *  Description:  Modeling Percolation.
 ******************************************************************************/
public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96; // Added to make Coursera happy. Used to calculate the 95% confidence interval in a standard distribution
    private final int trials;
    private final double[] thresholds;
    private double mean = Double.NaN;
    private double stddev = Double.NaN;
    private double confidenceLo = Double.NaN;
    private double confidenceHi = Double.NaN;

    public PercolationStats(int n, int trials) {
        if (n <= 0) throw new IllegalArgumentException("n ≤ 0");
        if (trials <= 0) throw new IllegalArgumentException("trials ≤ 0");
        this.trials = trials;
        thresholds = new double[trials];
        for (int t = 0; t < trials; t++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniformInt(n) + 1;
                int col = StdRandom.uniformInt(n) + 1;
                if (!perc.isOpen(row, col)) {
                    perc.open(row, col);
                }
            }
            thresholds[t] = (double) perc.numberOfOpenSites() / (n * n);
        }
    }

    public double mean() {
        if (!Double.isNaN(mean)) return mean;
        mean = StdStats.mean(thresholds);
        return mean;
    }

    public double stddev() {
        if (!Double.isNaN(stddev)) return stddev;
        stddev = StdStats.stddev(thresholds);
        return stddev;
    }

    public double confidenceLo() {
        if (!Double.isNaN(confidenceLo)) return confidenceLo;
        confidenceLo = mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
        return confidenceLo;
    }

    public double confidenceHi() {
        if (!Double.isNaN(confidenceHi)) return confidenceHi;
        confidenceHi = mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
        return confidenceHi;
    }

    public static void main(String[] args) {
        if (args.length != 2) throw new IllegalArgumentException();
        int n = Integer.parseInt(args[0]);
        int numTrials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, numTrials);
        System.out.printf("mean                    = %.16f%n", stats.mean());
        System.out.printf("stddev                  = %.16f%n", stats.stddev());
        System.out.printf("95%% confidence interval = [%.16f, %.16f]%n",
                stats.confidenceLo(), stats.confidenceHi());
    }
}