package com.henry.sort_chapter_02.quick_sort_algorithm_03.quick_sort_for_repeatItem_03; /***************************************************************************
 *  Compilation:  javac Quick3wayBars.java
 *  Execution:    java Quick3wayBars m n
 *  Dependencies: StdDraw.java
 *
 *  Sort n random real numbers between 0 and 1 using 3-way quicksort.
 *
 *  Visualize the results by ploting bars with heights proportional
 *  to the values.
 *
 *  % java Quick3wayBars 6 100
 *
 *  Comments
 *  --------
 *   - if image is too large, it may not display properly but you can
 *     still save it to a file
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.TreeSet;

public class Quick3SectionBars {
    private static int rows;
    private static int row = 0;
    private static final int VERTICAL = 70;

    public static void sort(double[] a) {
        // StdRandom.shuffle(a);
        show(a, 0, 0, -1, a.length-1);
        sort(a, 0, a.length - 1);
        show(a, 0, 0, -1, a.length-1);
    }

    private static void sort(double[] a, int leftBar, int rightBar) {
        if (rightBar == leftBar) show(a, leftBar, leftBar, leftBar, leftBar);
        if (rightBar <= leftBar) return;
        int lessZoneRightBoundary = leftBar, greaterZoneLeftBoundary = rightBar;
        double v = a[leftBar];
        int cursorOfItemToCompare = leftBar;
        while (cursorOfItemToCompare <= greaterZoneLeftBoundary)
            if      (less(v, a[cursorOfItemToCompare])) exch(a, cursorOfItemToCompare, greaterZoneLeftBoundary--);
            else if (less(a[cursorOfItemToCompare], v)) exch(a, lessZoneRightBoundary++, cursorOfItemToCompare++);
            else                    cursorOfItemToCompare++;

        show(a, leftBar, lessZoneRightBoundary, greaterZoneLeftBoundary, rightBar);
        sort(a, leftBar, lessZoneRightBoundary - 1);
        sort(a, greaterZoneLeftBoundary + 1, rightBar);
    }

    private static boolean less(double v, double w) {
        return v < w;
    }

    private static void exch(double[] a, int i, int j) {
        double t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // draw one row of trace
    private static void show(double[] a, int lo, int lt, int gt, int hi) {
        double y = rows - row - 1;
        for (int k = 0; k < a.length; k++) {
            if      (k < lo)             StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            else if (k > hi)             StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            else if (k >= lt && k <= gt) StdDraw.setPenColor(StdDraw.BOOK_RED);
            else                         StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(k, y + a[k] * 0.25, 0.25, a[k] * 0.25);
        }
        row++;
    }

    public static void main(String[] args) {
        int m = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        double[] a = new double[n];
        for (int i = 0; i < n; i++) {
            a[i] = (1 + StdRandom.uniform(m)) / (double) m;
        }

        // how many distinct values
        TreeSet<Double> set = new TreeSet<Double>();
        for (int i = 0; i < n; i++)
            set.add(a[i]);
        rows = set.size() + 2;

        // number of rows should be number of distinct entries + 2
        StdDraw.setCanvasSize(800, rows*VERTICAL);
        StdDraw.enableDoubleBuffering();
        StdDraw.square(0.5, 0.5, 0.5);
        StdDraw.setXscale(-1, n);
        StdDraw.setYscale(-0.5, rows);
        StdDraw.show();
        sort(a);
        StdDraw.show();
    }
}
