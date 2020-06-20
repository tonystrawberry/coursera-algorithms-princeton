/* *****************************************************************************
 *  Name:              Tony Duong
 *  Coursera User ID:  eea37f35417cfac1532e29ff8d6dc855
 *  Last modified:     19/06/2020
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF wqu;
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
        this.wqu = new WeightedQuickUnionUF(n * n + 2);
    }

    // get the 1D index for a site in the grid
    public int index1D(int row, int col) {
        return row * this.gridSize + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (this.grid[row][col]) {
            // Already opened
            return;
        }


        boolean hasTop = true;
        boolean hasBot = true;
        boolean hasLeft = true;
        boolean hasRight = true;

        this.grid[row][col] = true;
        this.numberOfOpenSites += 1;

        if (row == 0)
            hasTop = false;
        else if (row == this.gridSize - 1)
            hasBot = false;


        if (col == 0)
            hasLeft = false;
        else if (col == this.gridSize - 1)
            hasRight = false;

        if (hasTop && this.grid[row - 1][col]) {
            this.wqu.union(index1D(row, col), index1D(row - 1, col));
        }

        if (hasBot && this.grid[row + 1][col]) {
            this.wqu.union(index1D(row, col), index1D(row + 1, col));
        }

        if (hasLeft && this.grid[row][col - 1]) {
            this.wqu.union(index1D(row, col), index1D(row, col - 1));
        }

        if (hasRight && this.grid[row][col + 1]) {
            this.wqu.union(index1D(row, col), index1D(row, col + 1));
        }

        if (!hasTop) {
            this.wqu.union(index1D(row, col), this.virtualTopSite);
        }

        // if the site to open is in the bottom row, connect it to the virtual bottom node
        if (!hasBot) {
            this.wqu.union(index1D(row, col), this.virtualBotSite);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return this.grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return this.wqu.connected(index1D(row, col), this.virtualTopSite);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.wqu.connected(this.virtualBotSite, this.virtualTopSite);
    }
}
