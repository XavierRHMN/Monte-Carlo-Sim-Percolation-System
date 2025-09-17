/* *****************************************************************************
 *  Name:    Xavier Rahman
 *  NetID:   xrahman
 *  Precept: P00
 *
 *  Description: Models an n-by-n Percolation system using Weighted Quick UF
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // Percolation system modelled by a 2d int array
    private boolean[][] system;

    // Side length of percolation system
    private int sideLength;

    // Represents the int of the virtual topSite
    private int topSite;

    // Represents the int of the virtual bottomSite
    private int bottomSite;

    // Represents the total site count
    private int siteCount;

    // Weighted Quick UF for percolates() (contains top/bottom site)
    private WeightedQuickUnionUF wqf1;

    // Weighted Quick UF for isFull() (contains topSite)
    private WeightedQuickUnionUF wqf2;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        checkBounds(n);

        system     = new boolean[n][n];
        sideLength = n;

        int oneSite = 1;
        int twoSites = 2;

        wqf1 = new WeightedQuickUnionUF(n * n + twoSites);
        wqf2 = new WeightedQuickUnionUF(n * n + oneSite);

        topSite    = n * n;        // virtual top site at idx = n^2
        bottomSite = n * n + 1;    // virtual bottom site at idx = n^2 + 1
    }

    // Opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkBounds(row, col);

        boolean site   = system[row][col];

        // site 1d indices
        int middle = getIdx(row, col);
        int left   = getIdx(row, col - 1);
        int right  = getIdx(row, col + 1);
        int up     = getIdx(row - 1, col);
        int down   = getIdx(row + 1, col);

        if (!site) {
            system[row][col] = true;

            if (left != -1 && isOpen(row, col - 1)) {
                wqf1.union(left, middle);
                wqf2.union(left, middle);
            }
            if (right != -1 && isOpen(row, col + 1)) {
                wqf1.union(right, middle);
                wqf2.union(right, middle);
            }
            if (up != -1 && isOpen(row - 1, col)) {
                wqf1.union(up, middle);
                wqf2.union(up, middle);
            }
            if (down != -1 && isOpen(row + 1, col)) {
                wqf1.union(down, middle);
                wqf2.union(down, middle);
            }
            if (row == 0) {
                wqf1.union(topSite, middle);
                wqf2.union(topSite, middle);
            }
            if (row == sideLength - 1) wqf1.union(bottomSite, middle);

            siteCount++;
        }
    }

    // Is the site at (row, col) open?
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        boolean site = system[row][col];
        return site;
    }

    // Is the specified site at (row, col) full?
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        int siteIdx = getIdx(row, col);
        return wqf2.find(topSite) == wqf2.find(siteIdx);
    }

    // Returns the number of open sites
    public int numberOfOpenSites() {
        return siteCount;
    }

    // Checks if the system percolates
    public boolean percolates() {
        return wqf1.find(topSite) == wqf1.find(bottomSite);
    }

    // Checks the bounds of the row and column when indexing the system
    private void checkBounds(int row, int col) {
        if (row < 0 || col < 0 || row >= sideLength || col >= sideLength)
            throw new IllegalArgumentException();
    }

    // Checks if n is non-negative, otherwise throws IllegalArgumentException
    private void checkBounds(int n) {
        if (n < 0) throw new IllegalArgumentException();
    }

    // Returns the index of a 1d int array given x and y coordinates
    private int getIdx(int y, int x) {
        if (y < 0 || x < 0 || y >= sideLength || x >= sideLength) return -1;
        return y * sideLength + x;
    }

    // Creates a string representation of the percolation system
    @Override
    public String toString() {

        // Creates the top row (ie. 0 1 2 3 ... n)
        String strSite = "    ";
        for (int i = 0; i < sideLength; i++) strSite += Integer.toString(i) + " ";
        strSite += "\n    ";

        // Creates the seperator (ie. - - - - - ...)
        for (int i = 0; i < sideLength; i++) strSite += "- ";
        strSite += "\n";

        // Builds a representation of the percolation system using strings

        for (int y = 0; y < sideLength; y++) {
            /* Creates the column
               0 |
               1 |
               2 |
               3 |
               ...
               n |
            */
            strSite += Integer.toString(y) + " | ";

            for (int x = 0; x < sideLength; x++) {
                int site = system[y][x] ? 1 : 0; // Converts boolean to integer
                strSite += Integer.toString(site) + " ";
            }
            strSite += "\n";
        }
        return strSite;
    }

    // unit testing (required)
    public static void main(String[] args) {

        int sideLength = 5;
        Percolation perc = new Percolation(5);

        perc.open(1, 4);
        perc.open(0, 4);
        perc.open(1, 1);
        perc.open(3, 4);
        perc.open(4, 4);
        perc.open(2, 4);
        perc.open(2, 2);
        perc.open(2, 3);
        perc.open(2, 1);

        StdOut.printf("\nPercolation system of %dx%d sites:\n\n",
                      sideLength,
                      sideLength);

        StdOut.println(perc);

        StdOut.printf("Is full at (%d, %d) = %b\n", 4, 4, perc.isFull(4, 4));
        StdOut.printf("Is open at (%d, %d) = %b\n", 4, 4, perc.isOpen(4, 4));
        StdOut.println("System Percolates = " + perc.percolates());
        StdOut.println("# of open sites   = " + perc.numberOfOpenSites());
    }
}
