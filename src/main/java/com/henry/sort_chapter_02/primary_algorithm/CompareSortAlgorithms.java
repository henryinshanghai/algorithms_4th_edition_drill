package com.henry.sort_chapter_02.primary_algorithm;

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

    // 获取到 使用指定排序算法 对指定数组进行完全排序的耗时
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

    // 使用排序算法 对随机数组进行完全排序操作
    // itemAmount - 数组的元素数量 trialTimes - 对“多少个数组”完全排序
    public static double timeRandomInput(String passedAlg, int itemAmount, int trialTimes)  {
        double totalTimeCost = 0.0;
        Double[] itemArr = new Double[itemAmount];

        for (int currentTrial = 0; currentTrial < trialTimes; currentTrial++) { // 多次执行sort()计算总耗时
            // 生成一个 指定大小的数组itemArr
            for (int currentItemCursor = 0; currentItemCursor < itemAmount; currentItemCursor++)
                itemArr[currentItemCursor] = StdRandom.uniform(0.0, 1.0);

            // 累计 对“当前随机数组”进行完全排序 的耗时
            totalTimeCost += timeCostToSortWith(passedAlg, itemArr);
        }

        return totalTimeCost;
    }

    // 使用排序算法 对有序数组进行排序操作
    // itemAmount - 数组的元素数量 trialTimes - 对“多少个数组”完全排序
    public static double timeSortedInput(String passedAlg, int itemAmount, int trialTimes) {
        double totalTimeCost = 0.0;
        Double[] a = new Double[itemAmount];
        // Perform one experiment (generate and sort an array).
        for (int currentTrial = 0; currentTrial < trialTimes; currentTrial++) {
            for (int itemCursor = 0; itemCursor < itemAmount; itemCursor++)
                a[itemCursor] = 1.0 * itemCursor; // 生成一个有序的数组

            totalTimeCost += timeCostToSortWith(passedAlg, a);
        }
        return totalTimeCost;
    }

    public static void main(String[] args) {
        String alg1 = args[0]; // 算法1：插入排序算法
        String alg2 = args[1]; // 算法2：选择排序算法

        int itemAmount = Integer.parseInt(args[2]); // 待排序数组的长度
        int trialTimes = Integer.parseInt(args[3]); // 进行多少次的完全排序
        double time1, time2;

        if (args.length == 5 && args[4].equals("sorted")) {
            time1 = timeSortedInput(alg1, itemAmount, trialTimes);   // Total for alg1.
            time2 = timeSortedInput(alg2, itemAmount, trialTimes);   // Total for alg2.
        }
        else {
            time1 = timeRandomInput(alg1, itemAmount, trialTimes);   // Total for alg1.
            time2 = timeRandomInput(alg2, itemAmount, trialTimes);   // Total for alg2.
        }

        // 计算出两种算法的执行时间的比率，并打印出来
        StdOut.printf("For %d random Doubles\n    %s is", itemAmount, alg1);
        StdOut.printf(" %.1f times faster than %s\n", time2/time1, alg2);
    }
}