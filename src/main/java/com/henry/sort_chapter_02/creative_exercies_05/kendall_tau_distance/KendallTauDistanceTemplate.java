package com.henry.sort_chapter_02.creative_exercies_05.kendall_tau_distance;

import edu.princeton.cs.algs4.Inversions;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;


/******************************************************************************
 *  Compilation:  javac KendallTauDistanceTemplate.java
 *  Execution:    java KendallTauDistanceTemplate n
 *  Dependencies: StdOut.java Inversions.java
 *
 *  生成两个任意的 大小为N的序列，并计算它们之间的 Kendall tau 距离（反转inversion的数量）
 ******************************************************************************/

/*
 *  定义：两种排列(permutation)之间的Kendall Tau距离————两种排列之间 “顺序不同的数对” 的数量
 *  eg. 0 3 1 6 2 5 4 与 1 0 3 6 4 2 5
 *  数组A中可以创建的“数对”（从左往右）：0-3 0-1[Mark] 0-6 0-2 0-5 0-4 | 3-1[Mark] 3-6 3-2 3-5 3-4 | 1-6 1-2 1-5 1-4 | 6-2 6-5 6-4 | 2-5 2-4[Mark] | 5-4[Mark]
 *  数组B中可以创建的数对（从左往右）：0-3 0-6 0-4 0-2 0-5 | 3-6 3-4 3-2 3-5 | 1-0[Mark] 1-3 1-6 1-4 1-2 1-5 | 6-4 6-2 6-5 | 2-5 | REST 4-2 4-5
 *  顺序不同的数对：0-1 3-1 2-4 5-4     共有4个。
 *  则：Kendall Tau distance(permutation1, permutation2) = 4；
 *
 *  作用：在线性对数时间内计算出两个排列之间的Kendall Tau距离
 *
 */
public class KendallTauDistanceTemplate {

    // 计算并返回两个排列之间的 Kendall tau distance
    public static long distance(int[] permutation01, int[] permutation02) {
        if (permutation01.length != permutation02.length) {
            throw new IllegalArgumentException("Array dimensions disagree");
        }
        int itemAmount = permutation01.length;

        // #1 对permutation01，记录其 item -> spot的关系 - 相当于snapshot01
        int[] itemToItsSpotInPermu01 = new int[itemAmount];
        for (int currentSpot = 0; currentSpot < itemAmount; currentSpot++) {
            int itemOfCurrentSpot = permutation01[currentSpot];
            itemToItsSpotInPermu01[itemOfCurrentSpot] = currentSpot;
        }

        // #2 记录 某个元素 “在排列02中的位置” -> “在排列01中的位置” 的关系 - 相当于从排列01，变换到排列02。
        // 原理：spot <-> item
        Integer[] spotIn02ToSpotIn01Array = new Integer[itemAmount];
        for (int currentSpotIn02 = 0; currentSpotIn02 < itemAmount; currentSpotIn02++){
            int itemIn02 = permutation02[currentSpotIn02];
            // itemIn02元素 “在排列02中的位置” 👇   itemIn02元素 “在排列01中的位置”👇
            spotIn02ToSpotIn01Array[currentSpotIn02] = itemToItsSpotInPermu01[itemIn02];
        }

        /* 返回 “从排列1 变化到 排列2”所产生的“逆序对”数量 */
        // 原理：当把某种排列视为“基准排列”时，元素的spot 与 元素本身 是一一对应的。因此 spot信息即可代表元素。
        // 推论：“目标排列” 相对于“基准排列”的“逆序对”数量 = “spot排列” 相对于 “标准位置排列（0, 1, 2...）”的“逆序对数量”
        // 等价简化：“spot排列” 相对于 “标准位置排列（0, 1, 2...）”的“逆序对数量” = spot排列中所存在的“逆序对”数量
        /*
            把排列02视为基准排列，则：排列01 与 排列02之间的 距离 = 从排列02 变化到 排列01时，所产生的逆序对的数量。
            #1 排列02的spot信息：            0, 1, 2, 3
            #2 对应元素在排列01中的spot信息:  2, 3, 1, 0
            显然，对于spot信息来说，逆序对的数量 = #2的序列中所存在的逆序对的数量。
         */
        // 返回“参数数组”中所存在的“逆序对”数量
        // 原理：inversion_num = left_inversion_num + right_inversion_num + merge_inversion_num
        // 特征：使用merge操作时，总归会堆数组进行排序 - 所以需要一个 arrayToSort， 一个辅助数组 aux
        return Inversions.count(spotIn02ToSpotIn01Array);
    }

    // 返回大小为 itemAmount的随机排列（permutation）
    public static int[] permutation(int itemAmount) {
        int[] a = new int[itemAmount]; // 大小初始化
        for (int i = 0; i < itemAmount; i++)
            a[i] = i; // 元素初始化
        StdRandom.shuffle(a); // 洗牌
        return a;
    }

    public static void show(int[] a) {
        for (int currentSpot = 0; currentSpot < a.length; currentSpot++) {
            System.out.print(a[currentSpot] + " ");;
        }

        System.out.println();
    }


    public static void main(String[] args) {

        // two random permutation of size itemAmount - 这样不好判断 计算的距离是不是对的
//        int itemAmount = Integer.parseInt(args[0]);
//        int[] permutation01 = KendallTauDistanceTemplate.permutation(itemAmount);
//        int[] permutation02 = KendallTauDistanceTemplate.permutation(itemAmount);
//
        int[] permutation01 = {0, 1 ,2, 3};
        int[] permutation02 = {2, 1, 0 ,3};


        // print initial permutation(排列)
        show(permutation01);
        show(permutation02);

        StdOut.println("inversions = " + KendallTauDistanceTemplate.distance(permutation01, permutation02));
    }
}