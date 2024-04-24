package com.henry.sort_chapter_02.creative_exercies.kendall_tau_distance;

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
 *  定义：两种排列(permutation)之间的Kendall Tau距离；
 *  作用： 用于描述两个排列之间的相似程度；
 *  具体计算方式：计算出 两种排列之间 “顺序不同的数对” 的数量
 *  eg. 排列Ⅰ 0 3 1 6 2 5 4 与 排列Ⅱ 1 0 3 6 4 2 5
 *  排列Ⅰ中可以创建的“数对”（从左往右）：0-3 0-1[Mark] 0-6 0-2 0-5 0-4 | 3-1[Mark] 3-6 3-2 3-5 3-4 | 1-6 1-2 1-5 1-4 | 6-2 6-5 6-4 | 2-5 2-4[Mark] | 5-4[Mark]
 *  排列Ⅱ中可以创建的数对（从左往右）：0-3 0-6 0-4 0-2 0-5 | 3-6 3-4 3-2 3-5 | 1-0[Mark] 1-3[Mark] 1-6 1-4 1-2 1-5 | 6-4 6-2 6-5 | 2-5 | REST 4-2[mark] 4-5[mark]
 *  顺序不同的数对：0-1 3-1 2-4 5-4     共有4个。
 *  则：Kendall Tau distance(permutation1, permutation2) = 4；
 *
 *  作用：在线性对数时间内计算出两个排列之间的Kendall Tau距离
 *
 */
public class KendallTauDistanceTemplate {

    // 目标：计算并返回两个排列之间的 Kendall tau distance = 两个排列之间所存在的 不同数对的数量
    public static long distance(int[] permutation01, int[] permutation02) {
        if (permutation01.length != permutation02.length) {
            throw new IllegalArgumentException("Array dimensions disagree");
        }

        // #1 对permutation01，记录其 item -> spot的关系 - 相当于snapshot01
        int[] itemToItsSpotInPermu01 = constructItemToSpotArr(permutation01);

        // #2 记录 某个元素 “在排列02中的位置” -> “在排列01中的位置” 的关系 - 相当于从排列01，变换到排列02。
        Integer[] spotIn02ToSpotIn01Array = constructItemsSpotIn02ToItsSpotIn01Arr(permutation02, itemToItsSpotInPermu01);

        // #3 获取到 spot01ToSpot02Arr[] 数组中所存在的 逆序对(非自然数顺序)的数量 <=> 原始的两个排列之间 存在的不同数对的数量
        // 原理：当把某种排列视为“基准排列”时，元素的spot 与 元素本身 是一一对应的。因此 spot信息即可代表元素。
        // 推论：“目标排列” 相对于“基准排列”的“逆序对”数量 = “spot排列” 相对于 “标准位置排列（0, 1, 2...）”的“逆序对数量”
        // 等价简化：“spot排列” 相对于 “标准位置排列（0, 1, 2...）”的“逆序对数量” = spot排列中所存在的“逆序对”数量
        return getInversionAmountIn(spotIn02ToSpotIn01Array);
    }

    private static long getInversionAmountIn(Integer[] spotIn02ToSpotIn01Array) {
        // 由于这个过程步骤本身比较复杂，所以委托给一个工具类实现
        return Inversions.count(spotIn02ToSpotIn01Array);
    }

    // 构造 itemSpotIn01 -> itemSpotIn02 映射关系的数组
    private static Integer[] constructItemsSpotIn02ToItsSpotIn01Arr(int[] permutation02, int[] itemToItsSpotInPermu01) {
        // 原理：spot <-> item
        int itemAmount = permutation02.length;
        Integer[] spotIn02ToSpotIn01Array = new Integer[itemAmount];
        for (int currentSpotIn02 = 0; currentSpotIn02 < itemAmount; currentSpotIn02++){
            int itemIn02 = permutation02[currentSpotIn02];
            // itemIn02元素 “在排列02中的位置” 👇   itemIn02元素 “在排列01中的位置”👇
            spotIn02ToSpotIn01Array[currentSpotIn02] = itemToItsSpotInPermu01[itemIn02];
        }
        return spotIn02ToSpotIn01Array;
    }

    // 构造 记录item -> itsSpot映射关系的数组
    private static int[] constructItemToSpotArr(int[] permutation) {
        int itemAmount = permutation.length;
        int[] itemToItsSpotInPermu = new int[itemAmount];

        for (int currentSpot = 0; currentSpot < itemAmount; currentSpot++) {
            int itemOfCurrentSpot = permutation[currentSpot];
            itemToItsSpotInPermu[itemOfCurrentSpot] = currentSpot;
        }

        return itemToItsSpotInPermu;
    }

    // 返回大小为 itemAmount的随机排列（permutation）
    public static int[] generateRandomPermutationPer(int givenN) {
        int[] permutationArr = new int[givenN]; // 大小初始化
        for (int currentSpotCursor = 0; currentSpotCursor < givenN; currentSpotCursor++) {
            int associatedItem = currentSpotCursor;
            permutationArr[currentSpotCursor] = associatedItem; // 元素初始化
        }

        // 打乱数组元素，并返回打乱后的数组
        StdRandom.shuffle(permutationArr);
        return permutationArr;
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