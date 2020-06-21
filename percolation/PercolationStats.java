/* *****************************************************************************
 *  Name:              Tony Duong
 *  Coursera User ID:  eea37f35417cfac1532e29ff8d6dc855
 *  Last modified:     19/06/2020
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] stats;
    private double CONFIDENCE_CONSTANT = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0.");
        }

        this.stats = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation system = new Percolation(n);

            int numberOfOpenSites = 0;
            while (!system.percolates()) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;

                if (!system.isOpen(row, col)) {
                    numberOfOpenSites += 1;
                    system.open(row, col);
                }
            }

            this.stats[i] = numberOfOpenSites * 1.0 / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.stats);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.stats);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - this.CONFIDENCE_CONSTANT * this.stddev() / Math
                .sqrt(this.stats.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + this.CONFIDENCE_CONSTANT * this.stddev() / Math
                .sqrt(this.stats.length);

    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println(
                "95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi()
                        + "]");
    }

}
