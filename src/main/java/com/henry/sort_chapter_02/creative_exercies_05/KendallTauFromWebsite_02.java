package com.henry.sort_chapter_02.creative_exercies_05;

import edu.princeton.cs.algs4.Inversions;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;


/******************************************************************************
 *  Compilation:  javac KendallTauFromWebsite_02.java
 *  Execution:    java KendallTauFromWebsite_02 n
 *  Dependencies: StdOut.java Inversions.java
 *
 *  Generate two random permutations of size N and compute their
 *  Kendall tau distance (number of inversions).
 *  定义：两个数据序列之间的Kendall Tau距离————两个数据序列中顺序不同的数对的数量
 *  特征：1 两个数据序列的数据时相同的； 2 但是数据的顺序是不同的；
 *  eg. 0 3 1 6 2 5 4 与 1 0 3 6 4 2 5
 *  数组A中可以创建的数对（从左往右）：0-3 0-1[Mark] 0-6 0-2 0-5 0-4 | 3-1[Mark] 3-6 3-2 3-5 3-4 | 1-6 1-2 1-5 1-4 | 6-2 6-5 6-4 | 2-5 2-4[Mark] | 5-4[Mark]
 *  数组B中可以创建的数对（从左往右）：0-3 0-6 0-4 0-2 0-5 | 3-6 3-4 3-2 3-5 | 1-0[Mark] 1-3 1-6 1-4 1-2 1-5 | 6-4 6-2 6-5 | 2-5 | REST 4-2 4-5
 *  顺序不同的数对：0-1 3-1 2-4 5-4     共有4个。则两个数据序列之间的Kendall Tau距离为4；
 *
 *  作用：在线性对数时间内计算出两组数之间的Kendall Tau距离
 *
 *  疑问1：为什么a[]与b[]之间的Tau距离可以转化为bnew[]与标准排列之间的Tau距离？
 *  疑问2：计算bnew[]与标准排列之间的Tau距离的算法为什么是可行的？
 *  i can not make sense the codes, this is bugging
 ******************************************************************************/

public class KendallTauFromWebsite_02 {

    // return Kendall tau distance between two permutations（排列）
    public static long distance(int[] a, int[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array dimensions disagree");
        }
        int n = a.length;

        int[] ainv = new int[n];
        for (int i = 0; i < n; i++)
            // 1 ainv[]数组元素的初始化操作
            ainv[a[i]] = i; // 创建ainv[]数    qAA组   作用：存储指定数据在a数组中的索引;（ainv[数值] = 数值在a数组中的索引）

        Integer[] bnew = new Integer[n]; // 创建一个bnew[]数组 并对数组元素进行初始化
        for (int i = 0; i < n; i++)
            // 2 ainv[]数组元素的访问操作 + bnew[]元素的初始化操作
            bnew[i] = ainv[b[i]]; // 1 访问到b[]中的数值在ainv[]中对应存储的值（位置 = ainv[数值]; aka 数据b[i]在a[]中的索引值） 2 把访问到的结果绑定到bnew[]数组的元素上

        // 返回数组中倒置的数量
        return Inversions.count(bnew);
    }


    // return a random permutation of size n  返回大小为n的随机排列
    public static int[] permutation(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++)
            a[i] = i;
        StdRandom.shuffle(a);
        return a;
    }


    public static void main(String[] args) {

        // two random permutation of size n
//        int n = Integer.parseInt(args[0]);
//        int[] a = KendallTauFromWebsite_02.permutation(n);
//        int[] b = KendallTauFromWebsite_02.permutation(n);

        int[] a = {5, 0, 4, 1, 2, 3};
        int[] b = {4, 2, 3, 0, 5, 1};


        // print initial permutation(排列)
        for (int i = 0; i < a.length; i++)
            StdOut.println(a[i] + " " + b[i]);
        StdOut.println();

        StdOut.println("inversions = " + KendallTauFromWebsite_02.distance(a, b));
    }
}