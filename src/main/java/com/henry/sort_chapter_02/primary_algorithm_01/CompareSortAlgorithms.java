package com.henry.sort_chapter_02.primary_algorithm_01;

/******************************************************************************
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
 *  roughly the same order of growth, e,movedStepsFromStartGrid., insertion sort vs. selection
 *  sort or mergesort vs. quicksort. Otherwise, various system effects
 *  (such as just-in-time compiliation) may have a significant effect.
 *  One alternative is to execute with "java -Xint", which forces the JVM
 *  to use interpreted execution mode only.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.*;

import java.util.Arrays;

// 用于比较各个排序算法性能的用例模板：#1 生成一个指定大小的数组，并使用指定算法对它排序（这样的过程重复指定次数，用于取平均）； #2 比较两种算法执行时间的比率就能
// 得出 算法1比起算法2，平均要快多少（time2/time1）
public class CompareSortAlgorithms {

    public static double timeCostToSortWith(String passedAlg, Double[] passedArr) { // 参数：算法名称、被排序的数组
        // 开始计时 手段：创建一个StopWatch对象 - 专用于计算执行耗时的类
        Stopwatch sw = new Stopwatch();

        if      (passedAlg.equals("Insertion"))       Insertion.sort(passedArr); // 这些个实现都封装在algs4.jar中
        else if (passedAlg.equals("InsertionX"))      InsertionX.sort(passedArr);
        else if (passedAlg.equals("BinaryInsertion")) BinaryInsertion.sort(passedArr);
        else if (passedAlg.equals("Selection"))       Selection.sort(passedArr);
//        else if (alg.equals("Bubble"))          Bubble.sort(a);
        else if (passedAlg.equals("Shell"))           Shell.sort(passedArr);
        else if (passedAlg.equals("MergeFromWebsite"))           Merge.sort(passedArr);
        else if (passedAlg.equals("MergeX"))          MergeX.sort(passedArr);
        else if (passedAlg.equals("MergeBU"))         MergeBU.sort(passedArr);
        else if (passedAlg.equals("Quick_recursive"))           Quick.sort(passedArr);
        else if (passedAlg.equals("Quick3way"))       Quick3way.sort(passedArr);
        else if (passedAlg.equals("QuickX"))          QuickX.sort(passedArr);
        else if (passedAlg.equals("Heap"))            Heap.sort(passedArr);
        else if (passedAlg.equals("System"))          Arrays.sort(passedArr);
        else throw new IllegalArgumentException("Invalid algorithm: " + passedAlg);

        // 结束计时，得到执行sort()方法所耗费的时间     手段：调用对象的elapsedTime()方法
        return sw.elapsedTime();
    }

    // Use alg to sort trials random arrays of length n.
    // 使用算法来排序测试用的随机数组（长度为N）
    public static double timeRandomInput(String passedAlg, int itemAmount, int trialTimes)  {
        double totalTimeCost = 0.0;
        Double[] itemArr = new Double[itemAmount];

        for (int currentTrial = 0; currentTrial < trialTimes; currentTrial++) { // 多次执行sort()计算总耗时
            // 生成一个 指定大小的数组itemArr
            for (int currentItemSpot = 0; currentItemSpot < itemAmount; currentItemSpot++)
                itemArr[currentItemSpot] = StdRandom.uniform(0.0, 1.0);

            // 对生成的数组itemArr 进行排序 🐖 累计每次排序的耗时
            totalTimeCost += timeCostToSortWith(passedAlg, itemArr); // 对数据进行排序，并返回排序的耗时t
        }

        return totalTimeCost;
    }

    // Use alg to sort trials random arrays of length n. 
    public static double timeSortedInput(String alg, int n, int trials) {
        double total = 0.0;
        Double[] a = new Double[n];
        // Perform one experiment (generate and sort an array).
        for (int t = 0; t < trials; t++) {
            for (int i = 0; i < n; i++)
                a[i] = 1.0 * i;
            total += timeCostToSortWith(alg, a);
        }
        return total;
    }

    public static void main(String[] args) {
        String alg1 = args[0]; // 算法1：插入排序算法
        String alg2 = args[1]; // 算法2：选择排序算法

        int itemAmount = Integer.parseInt(args[2]); // 数组的长度
        int trialTimes = Integer.parseInt(args[3]); // 排序执行的次数
        double time1, time2;

        if (args.length == 5 && args[4].equals("sorted")) {
            time1 = timeSortedInput(alg1, itemAmount, trialTimes);   // Total for alg1.
            time2 = timeSortedInput(alg2, itemAmount, trialTimes);   // Total for alg2.
        }
        else {
            time1 = timeRandomInput(alg1, itemAmount, trialTimes);   // Total for alg1.
            time2 = timeRandomInput(alg2, itemAmount, trialTimes);   // Total for alg2.
        }

        // 打印两种算法的执行时间的比率
        StdOut.printf("For %d random Doubles\n    %s is", itemAmount, alg1);
        StdOut.printf(" %.1f times faster than %s\n", time2/time1, alg2);
    }
}