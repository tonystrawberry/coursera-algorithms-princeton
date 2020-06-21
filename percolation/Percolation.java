/* *****************************************************************************
 *  Name:              Tony Duong
 *  Coursera User ID:  eea37f35417cfac1532e29ff8d6dc855
 *  Last modified:     19/06/2020
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF percolation, fullness;
    private int virtualTopSite;
    private int virtualBotSite;
    private int gridSize;
    private int numberOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0.");
        }
        this.gridSize = n;
        this.grid = new boolean[n][n];

        virtualTopSite = n * n;
        virtualBotSite = n * n + 1;

        // Initialize with number of sites + virtual top and bottom sites
        this.percolation = new WeightedQuickUnionUF(n * n + 2);
        this.fullness = new WeightedQuickUnionUF(n * n + 2);
    }

    // get the 1D index for a site in the grid
    private int index1D(int row, int col) {
        return row * this.gridSize + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > this.gridSize || col > this.gridSize) {
            throw new IllegalArgumentException("row and col should be greater than 0.");
        }
        int rowBase0 = row - 1;
        int colBase0 = col - 1;

        if (this.grid[rowBase0][colBase0]) {
            // Already opened
            return;
        }


        boolean hasTop = true;
        boolean hasBot = true;
        boolean hasLeft = true;
        boolean hasRight = true;

        this.grid[rowBase0][colBase0] = true;
        this.numberOfOpenSites += 1;

        if (rowBase0 == 0)
            hasTop = false;
        if (rowBase0 == this.gridSize - 1)
            hasBot = false;


        if (colBase0 == 0)
            hasLeft = false;
        if (colBase0 == this.gridSize - 1)
            hasRight = false;

        if (hasTop && this.grid[rowBase0 - 1][colBase0]) {
            this.percolation.union(index1D(rowBase0, colBase0), index1D(rowBase0 - 1, colBase0));
            this.fullness.union(index1D(rowBase0, colBase0), index1D(rowBase0 - 1, colBase0));
        }

        if (hasBot && this.grid[rowBase0 + 1][colBase0]) {
            this.percolation.union(index1D(rowBase0, colBase0), index1D(rowBase0 + 1, colBase0));
            this.fullness.union(index1D(rowBase0, colBase0), index1D(rowBase0 + 1, colBase0));
        }

        if (hasLeft && this.grid[rowBase0][colBase0 - 1]) {
            this.percolation.union(index1D(rowBase0, colBase0), index1D(rowBase0, colBase0 - 1));
            this.fullness.union(index1D(rowBase0, colBase0), index1D(rowBase0, colBase0 - 1));
        }

        if (hasRight && this.grid[rowBase0][colBase0 + 1]) {
            this.percolation.union(index1D(rowBase0, colBase0), index1D(rowBase0, colBase0 + 1));
            this.fullness.union(index1D(rowBase0, colBase0), index1D(rowBase0, colBase0 + 1));
        }

        if (!hasTop) {
            this.percolation.union(index1D(rowBase0, colBase0), this.virtualTopSite);
            this.fullness.union(index1D(rowBase0, colBase0), this.virtualTopSite);
        }

        if (!hasBot) {
            this.percolation.union(index1D(rowBase0, colBase0), this.virtualBotSite);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > this.gridSize || col > this.gridSize) {
            throw new IllegalArgumentException("row and col should be greater than 0.");
        }
        return this.grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > this.gridSize || col > this.gridSize) {
            throw new IllegalArgumentException("row and col should be greater than 0.");
        }
        return this.fullness.find(index1D(row - 1, col - 1)) == this.fullness.find(this.virtualTopSite) && this.isOpen(row, col);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.percolation.find(this.virtualBotSite) == this.percolation.find(this.virtualTopSite);
    }

    public static void main(String[] args) {
        Percolation system = new Percolation(1);
        system.open(1, 1);

    }
}
