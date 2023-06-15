package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

import edu.princeton.cs.algs4.StdOut;

/*
    算法思路描述：
        #1 对原始数组，以N为单位，将之分割成为 若干个大小为N的子数组（剩余数量不足N的单独成一组）
        #2 对于#1中分割得到的M个子数组，对M个子数组中的第一个元素进行排序、第二个元素进行排序...第N个元素进行排序 - 得到“分隔有序”的元素序列
        #3 把N调小（直到为1）- 进而得到完全排序的数组。
    原理：
        N比较大（子组的数量M比较小）时，只需要 对少量的数据交换很少的次数，就能把元素放到 离“最终排定位置”很近的位置上；
        N比较小（子组的数量M比较大）时，需要 对大量的数据交换很少的次数（因为它们离自己的最终位置其实很近了）。
        综合来说，比起直接使用插入排序，交换的次数要少很多

    术语定义：
        “分隔有序的元素序列” - 指各个子数组中“特定位置上的元素”所构成的序列 是有序的；
        “最终排定位置” - 当数组元素在完全排序状态时，元素在数组中的位置；
        “完全排序的数组” - 数组中的所有元素按照升序或降序排定；

    算法过程：
        #1 按照一个公式，生成一个比较大的N值（小于并接近itemAmount） 用于分割原始数组为子数组
        #2 按照当前 itemAmountOfSubGroup 分组后，得到“分隔有序的元素序列”
            手段：从 startPointOfDisorder 开始到原始数组的最后一个元素为止，对每个元素，把它插入到“其对应的序列”
        #3 把当前“待插入的元素”插入到其所对应的序列中；aka 把 a[i] 插入到 a[i-toMoveStepsToEndGridWithoutObstacles], a[i-2*toMoveStepsToEndGridWithoutObstacles]...中
            手段：插入排序；
        #4 缩小 itemAmountOfSubGroup，来 最终得到 “完全排序的数组”。

    有意义的变量名：
        1 按照特定的size对原始数组进行分组 - itemAmountOfSubGroup
        2 指向 开始执行插入操作的起始元素 的静态指针 - startPointOfDisorder（起始指针 ∈希尔排序）
        3 指向 待插入到有序区中的元素 的动态指针 - cursorOfItemToInsert（待插入元素的指针 ∈插入排序）
        4 从后往前遍历有序序列时 的游标指针 - backwardsCursor（倒序指针 ∈插入排序）

    严格的边界条件：
        1 从序列的第二个元素开始，到数组的最后一个元素为止；
        2 执行交换时，backwardsCursor的边界位置在第二个元素上
 */
public class ShellSortTemplate {

    public static void sort(Comparable[] a) {
        // 先把序列元素更新到 最大元素
        int itemAmount = a.length;
        int itemAmountOfSubGroup = 1;

        // #1 按照一个公式，生成一个比较大的N值（小于itemAmount） 用于分割原始数组为子数组
        while (itemAmountOfSubGroup < itemAmount / 3) {
            itemAmountOfSubGroup = 3 * itemAmountOfSubGroup + 1; // h序列：1, 4, 13, 40, 121, 364, 1093...
        } // 循环结束时，h是一个比较大的值...

        // 完成对数组中所有元素的排序
        // 手段：#1 对于当前的itemAmountOfSubGroup, 得到“分隔有序的元素序列”； #2 调整当前的itemAmountOfSubGroup，得到 “完全有序的元素序列”
        while (itemAmountOfSubGroup >= 1) { // 当N=1（子数组尺寸为1）时，整个数组排序完成
            // #2 按照当前itemAmountOfSubGroup分组后，得到“分隔有序的元素序列”
            // 手段：从 startPointOfDisorder 开始到原始数组的最后一个元素为止，对每个元素，把它插入到“其对应的序列”
            int startPointOfDisorder = itemAmountOfSubGroup;
            for (int anchorOfItemToInsert = startPointOfDisorder; anchorOfItemToInsert < itemAmount; anchorOfItemToInsert++) { // 内循环的次数
                // #3 把a[anchorOfItemToInsert]插入到a[anchorOfItemToInsert-itemAmountOfSubGroup],a[anchorOfItemToInsert-2*itemAmountOfSubGroup],a[anchorOfItemToInsert-3*itemAmountOfSubGroup]...之中
                // 手段：插入排序
                for (int backwardsCursor = anchorOfItemToInsert;
                     backwardsCursor >= startPointOfDisorder && less(a[backwardsCursor], a[backwardsCursor - itemAmountOfSubGroup]);
                     backwardsCursor -= itemAmountOfSubGroup) {
                    exch(a, backwardsCursor, backwardsCursor - itemAmountOfSubGroup);
                }
            }

            // 两层for循环结束后，就得到了 “分割有序的元素序列”
            // #4 缩小 itemAmountOfSubGroup，来 最终得到 “完全排序的数组”。
            itemAmountOfSubGroup = itemAmountOfSubGroup / 3;
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     *
     * @param a
     * @param i
     * @param j
     */
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void show(Comparable[] a) {
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }

    public static boolean isSorted(Comparable[] a) {
        // 测试数组中的元素是否有序
        for (int i = 0; i < a.length; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 从标准输入中读取字符串，然后把它们排序输出
//        String[] a = In.readStrings();
        String[] a = new String[]{"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
