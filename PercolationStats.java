/* *****************************************************************************
 *  Name:    Xavier Rahman
 *  NetID:   xrahman
 *  Precept: P00
 *
 *  Description:  Models the stats for the Percolation System
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private static final double CO_EFF = 1.96;

    private int trials;
    private double[] percFractions;
    private double elapsedTime;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        Stopwatch stopwatch = new Stopwatch();

        percFractions = new double[trials];
        this.trials = trials;

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int x = StdRandom.uniformInt(1, n + 1);
                int y = StdRandom.uniformInt(1, n + 1);

                perc.open(y, x);
            }

            double openSites = perc.numberOfOpenSites();
            double totSites = (n * n);
            percFractions[i] = openSites / totSites;
        }

        elapsedTime = stopwatch.elapsedTime();
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percFractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percFractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = mean();
        double stddev = stddev();

        double res = mean - (CO_EFF * stddev) / Math.sqrt(trials);
        return res;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double stddev = stddev();

        double res = mean + (CO_EFF * stddev) / Math.sqrt(trials);
        return res;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percStat = new PercolationStats(n, trials);

        StdOut.printf("mean                    = %.6f\n", percStat.mean());
        StdOut.printf("stddev                  = %.6f\n", percStat.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", percStat.confidenceLo(),
                      percStat.confidenceHi());
        // StdOut.printf("elapsed time     = %.3f\n", percStat.elapsedTime);
    }
}
