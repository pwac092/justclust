/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package justclust.toolbar.microarrayheatmap;

import justclust.toolbar.heatmap.*;
import java.text.NumberFormat;

/**
 *
 * @author computer
 */
public final class MicroarrayHeatMapMatrix {

    private double a[][] = null;
    private int nrow = 0;
    private int ncol = 0;

    public MicroarrayHeatMapMatrix() {
    }

    public MicroarrayHeatMapMatrix(int _nrow, int _ncol) {
        setSize(_nrow, _ncol);
    }

    public MicroarrayHeatMapMatrix(double[][] matrix) {

        setSize(matrix.length, matrix[0].length);

        for (int r = 0; r < nrow; r++) {
            for (int c = 0; c < ncol; c++) {
                a[r][c] = matrix[r][c];
            }
        }

    }

    public MicroarrayHeatMapMatrix(MicroarrayHeatMapMatrix matrix) {
        copy(matrix);
    }

    public void copy(MicroarrayHeatMapMatrix matrix) {
        setSize(matrix.nrow, matrix.ncol);

        for (int r = 0; r < nrow; r++) {
            for (int c = 0; c < ncol; c++) {
                a[r][c] = matrix.get(r, c);
            }
        }
    }

    public double[][] getArray() {
        return a;
    }

    public void setSize(int _nrow, int _ncol) {
        nrow = _nrow;
        ncol = _ncol;

        a = new double[nrow][ncol];
    }

    public int getNumRows() {
        return nrow;
    }

    public int getNumCols() {
        return ncol;
    }

    public double get(int r, int c) {
        return a[r][c];
    }

    public double[] getRow(int r) {
        return a[r];
    }

    public void setRow(int row, double[] v) {
        for (int i = 0; i < ncol; i++) {
            a[row][i] = v[i];
        }
    }

    public void setRow(int row, double val) {
        for (int i = 0; i < ncol; i++) {
            a[row][i] = val;
        }
    }

    public void set(int r, int c, double num) {
        a[r][c] = num;
    }

    public void set(double num) {
        for (int r = 0; r < nrow; r++) {
            for (int c = 0; c < ncol; c++) {
                a[r][c] = num;
            }
        }
    }

    @Override
    public MicroarrayHeatMapMatrix clone() {
        return new MicroarrayHeatMapMatrix(this);
    }

    public MicroarrayHeatMapMatrix transpose() {
        MicroarrayHeatMapMatrix trans = new MicroarrayHeatMapMatrix(ncol, nrow);

        for (int row = 0; row < nrow; row++) {
            for (int col = 0; col < ncol; col++) {
                trans.set(col, row, a[row][col]);
            } // end for row
        } // end for col

        return trans;
    }

    public void print() {
        NumberFormat nf = NumberFormat.getInstance();

        nf.setMaximumFractionDigits(1);

        System.out.println("{");
        for (int r = 0; r < nrow; r++) {
            System.out.print("{");
            for (int c = 0; c < ncol; c++) {
                System.out.print(nf.format(a[r][c]) + ",");
            }
            System.out.print("},");
            System.out.println();
        }
        System.out.println("}");


    }

    public static boolean equal(MicroarrayHeatMapMatrix a, MicroarrayHeatMapMatrix b) {
        for (int row = 0; row < a.getNumRows(); row++) {
//            if (!MathUtils.vectorsEqual(a.getRow(row), b.getRow(row), a.getNumCols())) {
            return false;
//            }
        }
        return true;
    }

    public static MicroarrayHeatMapMatrix multiply(MicroarrayHeatMapMatrix a, MicroarrayHeatMapMatrix b) {

        MicroarrayHeatMapMatrix m = new MicroarrayHeatMapMatrix(a.getNumRows(), b.getNumCols());

        for (int row = 0; row < a.getNumRows(); row++) {
            for (int col = 0; col < b.getNumCols(); col++) {
                double sum = 0;
                for (int i = 0; i < a.getNumCols(); i++) {
                    sum += (a.get(row, i) * b.get(i, col));
                } // end for i
                m.set(row, col, sum);
            } // end for col
        } // end for row

        return m;
    } // end multiply

    public static MicroarrayHeatMapMatrix subtract(MicroarrayHeatMapMatrix a, MicroarrayHeatMapMatrix b) {

        MicroarrayHeatMapMatrix m = new MicroarrayHeatMapMatrix(a.getNumRows(), a.getNumCols());

        for (int row = 0; row < a.getNumRows(); row++) {
            for (int col = 0; col < a.getNumCols(); col++) {
                double diff = a.get(row, col) - b.get(row, col);
                m.set(row, col, diff);
            } // end for col
        } // end for row

        return m;
    }
}
