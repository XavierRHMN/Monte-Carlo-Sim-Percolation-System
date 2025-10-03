/* *****************************************************************************
 *  Name:    Xavier Rahman
 *  NetID:   xrahman
 *  Precept: P00
 *
 *  Description: Models an n-by-n Percolation system using Weighted Quick UF
 *
 **************************************************************************** */

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

        system = new boolean[n][n];
        sideLength = n;

        int oneSite = 1;
        int twoSites = 2;

        wqf1 = new WeightedQuickUnionUF(n * n + twoSites);
        wqf2 = new WeightedQuickUnionUF(n * n + oneSite);

        topSite = n * n;        // virtual top site at idx = n^2
        bottomSite = n * n + 1;    // virtual bottom site at idx = n^2 + 1
    }

    // Opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkBounds(row, col);

        int x = --col;
        int y = --row;

        boolean site = system[y][x];

        // site 1d indices
        int middle = getIdx(row, col);
        int left = getIdx(row, col - 1);
        int right = getIdx(row, col + 1);
        int up = getIdx(row - 1, col);
        int down = getIdx(row + 1, col);

        if (!site) {
            system[y][x] = true;

            if (left != -1 && isOpenHelper(y, x - 1)) {
                wqf1.union(left, middle);
                wqf2.union(left, middle);
            }
            if (right != -1 && isOpenHelper(y, x + 1)) {
                wqf1.union(right, middle);
                wqf2.union(right, middle);
            }
            if (up != -1 && isOpenHelper(y - 1, x)) {
                wqf1.union(up, middle);
                wqf2.union(up, middle);
            }
            if (down != -1 && isOpenHelper(y + 1, x)) {
                wqf1.union(down, middle);
                wqf2.union(down, middle);
            }
            if (y == 0) {
                wqf1.union(topSite, middle);
                wqf2.union(topSite, middle);
            }
            if (y == sideLength - 1) wqf1.union(bottomSite, middle);

            siteCount++;
        }
    }

    // Is the site at (row, col) open?
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return isOpenHelper(row - 1, col - 1);
    }

    private boolean isOpenHelper(int row, int col) {
        boolean site = system[row][col];
        return site;
    }

    // Is the specified site at (row, col) full?
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        int siteIdx = getIdx(row - 1, col - 1);
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
        if (row < 1 || col < 1 || row > sideLength || col > sideLength)
            throw new IllegalArgumentException();
    }

    // Checks if n is non-negative, otherwise throws IllegalArgumentException
    private void checkBounds(int n) {
        if (n <= 0) throw new IllegalArgumentException();
    }

    // Returns the index of a 1d int array given x and y coordinates
    private int getIdx(int y, int x) {
        if (y < 0 || x < 0 || y >= sideLength || x >= sideLength) return -1;
        return y * sideLength + x;
    }
}
