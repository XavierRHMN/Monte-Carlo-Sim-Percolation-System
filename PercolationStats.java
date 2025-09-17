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
    private int trials;
    private double[] percFractions;
    private double elapsedTime;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        Stopwatch stopwatch = new Stopwatch();

        percFractions = new double[trials];
        this.trials = trials;

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int x = StdRandom.uniform(0, n);
                int y = StdRandom.uniform(0, n);

                perc.open(x, y);
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
    public double confidenceLow() {
        double mean = mean();
        double stddev = stddev();
        double coeff = 1.96;

        double res = mean - (coeff * stddev) / Math.sqrt(trials);
        return res;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        double mean = mean();
        double stddev = stddev();
        double coeff = 1.96;

        double res = mean + (coeff * stddev) / Math.sqrt(trials);
        return res;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percStat = new PercolationStats(n, trials);

        StdOut.printf("mean()           = %.6f\n", percStat.mean());
        StdOut.printf("stddev()         = %.6f\n", percStat.stddev());
        StdOut.printf("confidenceLow()  = %.6f\n", percStat.confidenceLow());
        StdOut.printf("confidenceHigh() = %.6f\n", percStat.confidenceHigh());
        StdOut.printf("elapsed time     = %.3f\n", percStat.elapsedTime);
    }
}
