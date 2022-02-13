package com.henry.sort_chapter_02.primary_algorithm_01; /******************************************************************************
 *  Compilation:  javac SortCompare.java
 *  Execution:    java SortCompare alg1 alg2 n trials
 *  Dependencies: StdOut.java Stopwatch.java
 *
 *  Sort n random real numbers, trials times using the two
 *  algorithms specified on the command line.
 *
 *  % java SortCompare Insertion Selection 1000 100 
 *  For 1000 random Doubles 
 *    Insertion is 1.7 times faster than Selection
 *
 *  Note: this program is designed to compare two sorting algorithms with
 *  roughly the same order of growth, e,g., insertion sort vs. selection
 *  sort or mergesort vs. quicksort. Otherwise, various system effects
 *  (such as just-in-time compiliation) may have a significant effect.
 *  One alternative is to execute with "java -Xint", which forces the JVM
 *  to use interpreted execution mode only.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.*;

import java.util.Arrays;

public class SortCompare {

    public static double time(String alg, Double[] a) { // 参数：算法名称、被排序的数组
        Stopwatch sw = new Stopwatch();
        if      (alg.equals("Insertion"))       Insertion.sort(a); // 这些个实现都封装在algs4.jar中
        else if (alg.equals("InsertionX"))      InsertionX.sort(a);
        else if (alg.equals("BinaryInsertion")) BinaryInsertion.sort(a);
        else if (alg.equals("Selection"))       Selection.sort(a);
//        else if (alg.equals("Bubble"))          Bubble.sort(a);
        else if (alg.equals("Shell"))           Shell.sort(a);
        else if (alg.equals("MergeFromWebsite"))           Merge.sort(a);
        else if (alg.equals("MergeX"))          MergeX.sort(a);
        else if (alg.equals("MergeBU"))         MergeBU.sort(a);
        else if (alg.equals("Quick_recursive"))           Quick.sort(a);
        else if (alg.equals("Quick3way"))       Quick3way.sort(a);
        else if (alg.equals("QuickX"))          QuickX.sort(a);
        else if (alg.equals("Heap"))            Heap.sort(a);
        else if (alg.equals("System"))          Arrays.sort(a);
        else throw new IllegalArgumentException("Invalid algorithm: " + alg);
        return sw.elapsedTime(); // 执行sort()方法所耗费的时间
    }

    // Use alg to sort trials random arrays of length n.
    // 使用算法来排序测试用的随机数组（长度为N）
    public static double timeRandomInput(String alg, int n, int trials)  {
        double total = 0.0;
        Double[] a = new Double[n];
        for (int t = 0; t < trials; t++) { // 多次执行sort()计算总耗时
            // Perform one experiment (generate and sort an array). 执行实验 （生成并排序数组）
            for (int i = 0; i < n; i++)
                a[i] = StdRandom.uniform(0.0, 1.0);
            total += time(alg, a); // 对数据进行排序，并返回排序的耗时t
        }
        return total;
    }

    // Use alg to sort trials random arrays of length n. 
    public static double timeSortedInput(String alg, int n, int trials) {
        double total = 0.0;
        Double[] a = new Double[n];
        // Perform one experiment (generate and sort an array).
        for (int t = 0; t < trials; t++) {
            for (int i = 0; i < n; i++)
                a[i] = 1.0 * i;
            total += time(alg, a);
        }
        return total;
    }

    public static void main(String[] args) {
        String alg1 = args[0]; // 算法1：插入排序算法
        String alg2 = args[1]; // 算法2：选择排序算法
        int n = Integer.parseInt(args[2]); // 数组的长度
        int trials = Integer.parseInt(args[3]); // 多次排序取平均值
        double time1, time2;
        if (args.length == 5 && args[4].equals("sorted")) {
            time1 = timeSortedInput(alg1, n, trials);   // Total for alg1. 
            time2 = timeSortedInput(alg2, n, trials);   // Total for alg2. 
        }
        else {
            time1 = timeRandomInput(alg1, n, trials);   // Total for alg1. 
            time2 = timeRandomInput(alg2, n, trials);   // Total for alg2. 
        }

        StdOut.printf("For %d random Doubles\n    %s is", n, alg1);
        StdOut.printf(" %.1f times faster than %s\n", time2/time1, alg2);
    }
}