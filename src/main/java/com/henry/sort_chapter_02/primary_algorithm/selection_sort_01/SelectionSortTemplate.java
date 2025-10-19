package com.henry.sort_chapter_02.primary_algorithm.selection_sort_01;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/*
选择排序算法描述：逐一排定数组中的元素 ———— 找到/选择到“未排定区间”中的最小元素，并“排定”它

有意义的变量名：
        定锚指针 - anchorCursor
        游标指针 - dynamicCursor
        指向最小元素的指针 - cursorToMinItem
        数组元素数量 - itemAmount

验证：选择排序执行过程中，每次游标指针指到数组的最后一个元素时，紧接着就会排定当前回合的最小元素。

术语定义：
“未排定区间”：已排定元素后的其他元素所组成的区间；
“排定”：把元素放置到与其大小相符的正确的位置；

算法步骤：
#1 找到未排定区间中的最小元素；
    手段：使用当前元素 与 当前最小元素（由cursorToMinItem指向）进行比较，更新cursorToMinItem使其指向最小元素；
#2 排定“最小元素”；
    手段：交换 “定锚指针”所指向的元素（记录当前回合要排定的位置） 与 “最小元素指针”所指向的元素（记录当前回合要排定的元素）。

特征：使用扑克牌模拟选择排序过程时，无法有效模拟比较的过程。只能模拟交换的过程
 */
// 验证：选择排序的算法是 对于当前“待排定的位置”，选择剩余元素中的最小元素 来排定/填充它。
public class SelectionSortTemplate {

    /**
     * 对数组中的元素进行排定 - 以此来实现排序
     *
     * @param a
     */
    public static void sort(Comparable[] a) {
        int itemAmount = a.length;

        // 对于“当前待排定的位置”...
        for (int currentSpotToArrange = 0; currentSpotToArrange < itemAmount; currentSpotToArrange++) {
            // #1 找到 “未排定区间”中的最小元素 - 确保cursorToMinItem指针指向的是最小元素
            int cursorToMinItem = moveCursorToMinItem(a, currentSpotToArrange);

            // #2 排定“最小元素”/当前待排定位置 - 手段：交换 定锚指针（待排定位置）, 最小元素指针（待排定元素所在的位置）所指向的元素
            arrangeItemOn(currentSpotToArrange, cursorToMinItem, a);

            // 断言 从[0, currentSpotToArrange] 区间内的所有元素都已经是有序的
            assert isSorted(a, 0, currentSpotToArrange);
        }

        // 断言 整个数组是有序的
        assert isSorted(a);
    }

    private static void arrangeItemOn(int currentSpotToArrange, int cursorToMinItem, Comparable[] a) {
        exch(a, currentSpotToArrange, cursorToMinItem);
    }

    // 手段：先以当前元素作为最小元素，并通过比较来持续更新最小元素
    private static int moveCursorToMinItem(Comparable[] a, int currentSpotToArrange) {
        int cursorToMinItem = currentSpotToArrange;

        int itemAmount = a.length;
        for (int cursorOfItemToCompare = currentSpotToArrange + 1; cursorOfItemToCompare < itemAmount; cursorOfItemToCompare++) {
            if (less(a[cursorOfItemToCompare], a[cursorToMinItem])) {
                cursorToMinItem = cursorOfItemToCompare;
            }
        }
        return cursorToMinItem;
    }

    // 1 判断整个数组是不是已经有序了 - 对于调试来说非常有用，以为它提供了验证阶段性预期的方法
    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    // 2 判断数组在a[lo] to a[hi]的区间内是不是已经有序了
    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int currentSpot = lo + 1; currentSpot <= hi; currentSpot++)
            // 当前元素 是否大于 它的前一个元素
            if (less(a[currentSpot], a[currentSpot - 1])) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable itemV, Comparable itemW) {
        return itemV.compareTo(itemW) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     * 手段： 借助中间元素
     *  @param a
     * @param spotI
     * @param spotJ
     */
    private static void exch(Comparable[] a, int spotI, int spotJ) {
        Comparable temp = a[spotI];
        a[spotI] = a[spotJ];
        a[spotJ] = temp;
    }

    private static void show(Comparable[] a) {
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }


    public static void main(String[] args) {
        // 从标准输入中读取字符串，然后把它们排序输出
        String[] a = In.readStrings();
//        String[] a = new String[] {"S", "E", "L", "E", "C", "T", "I", "O", "N", "S", "O", "R", "T"};
//        String[] a = new String[] {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);

        /* 在过程中断言，而不是在结果处断言(有点晚了) */

        show(a);
    }

}
/*
1 assert 关键字的用法？
    为了使assert关键字生效 需要手动开启它 - VM options: -ea
    https://www.cnblogs.com/jpfss/p/10973837.html
2 isSorted()方法应该怎么实现？
    - 针对特定区间有序的断言
    - 针对整个数组有序的断言
3 程序如何获取输入？
    - 手动硬编码字符串数组；
    - 使用 静态文件 来重定向 标准输入流 -> 程序会从文件中获取输入流，而不是等待控制台的输入。
        手段：借助 IDEA提供的 redirect input from... 功能
 */
