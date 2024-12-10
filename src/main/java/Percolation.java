import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/******************************************************************************
 *  Name:    Kevin Wayne
 *  Dependencies: StdIn.java StdRandom.java WeightedQuickUnionUF.java
 *  Description:  Modeling Percolation.
 ******************************************************************************/

public class Percolation {
    private final int n;
    private final boolean[] openSites;
    private final WeightedQuickUnionUF uf;       // For percolation
    private final WeightedQuickUnionUF ufFull;  // For fullness (without bottom)
    private final int virtualTop;
    private final int virtualBottom;
    private int openCount;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        int gridSize = n * n;
        openSites = new boolean[gridSize];
        uf = new WeightedQuickUnionUF(gridSize + 2); // Two virtual sites
        ufFull = new WeightedQuickUnionUF(gridSize + 1); // One virtual site
        virtualTop = gridSize;
        virtualBottom = gridSize + 1;
        openCount = 0;
    }

    public void open(int row, int col) {
        validateIndices(row, col);
        int index = getIndex(row, col);

        if (!openSites[index]) {
            openSites[index] = true;
            openCount++;

            if (row == 1) {
                uf.union(index, virtualTop);
                ufFull.union(index, virtualTop); // Connect to top in both UF structures
            }
            if (row == n) {
                uf.union(index, virtualBottom); // Only connect to bottom in percolation UF
            }

            connectIfOpen(row, col, row - 1, col); // Up
            connectIfOpen(row, col, row + 1, col); // Down
            connectIfOpen(row, col, row, col - 1); // Left
            connectIfOpen(row, col, row, col + 1); // Right
        }
    }

    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return openSites[getIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        int index = getIndex(row, col);
        return isOpen(row, col) && ufFull.find(index) == ufFull.find(virtualTop);
    }


    public int numberOfOpenSites() {
        return openCount;
    }

    public boolean percolates() {
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }

    private int getIndex(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    private void validateIndices(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
    }


    private void connectIfOpen(int row1, int col1, int row2, int col2) {
        if (row2 >= 1 && row2 <= n && col2 >= 1 && col2 <= n && isOpen(row2, col2)) {
            uf.union(getIndex(row1, col1), getIndex(row2, col2));
            ufFull.union(getIndex(row1, col1), getIndex(row2, col2));
        }
    }


}