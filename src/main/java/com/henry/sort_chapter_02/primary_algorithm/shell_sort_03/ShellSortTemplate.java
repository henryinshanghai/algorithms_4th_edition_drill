package com.henry.sort_chapter_02.primary_algorithm.shell_sort_03;

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
        #2 按照当前 blockSize 分组后，得到“分隔有序的元素序列”
            手段：从 startPointOfDisorder 开始到原始数组的最后一个元素为止，对每个元素，把它插入到“其对应的序列”
        #3 把当前“待插入的元素”插入到其所对应的序列中；aka 把 a[i] 插入到 a[i-toMoveStepsToEndGridWithoutObstacles], a[i-2*toMoveStepsToEndGridWithoutObstacles]...中
            手段：插入排序；
        #4 缩小 blockSize，来 最终得到 “完全排序的数组”。

    有意义的变量名：
        1 按照特定的size对原始数组进行分组 - blockSize
        2 指向 开始执行插入操作的起始元素 的静态指针 - startPointOfDisorder（起始指针 ∈希尔排序）
        3 指向 待插入到有序区中的元素 的动态指针 - cursorOfItemToInsert（待插入元素的指针 ∈插入排序）
        4 从后往前遍历有序序列时 的游标指针 - backwardsCursor（倒序指针 ∈插入排序）

    严格的边界条件：
        1 从序列的第二个元素开始，到数组的最后一个元素为止；
        2 执行交换时，backwardsCursor的边界位置在第二个元素上
 */
// for every item, using insertion sort with a big span size(degressive) to move it to its arranged spot step by step.
public class ShellSortTemplate {

    public static void sort(Comparable[] a) {
        System.out.println("在任何操作之前，原始的序列元素为： ");
        show(a);
        System.out.println("====================");

        // #0 先把segmentSize初始化成为 一个小于itemAmount的较大值
        int itemAmount = a.length;
        int gapSize = initABigGapSize(itemAmount); // segment、block、unit

        /*  实现对数组中所有元素的完全排序 */
        while (gapSize >= 1) { // 🐖 当 gapSize=1（gap的尺寸为1）时，整个数组排序完成
            System.out.println("+++ 当前的gapSize大小为：" + gapSize + ", 因此会得到" + gapSize + "个分组 +++");

            // #1 对于 当前的gapSize，处理 它所产生的 无序区中的元素，把 它们 移动到 离排定位置更近的地方
            moveItemsInTheRestZoneCloserToItsArrangedSpot(a, gapSize);

            // #2 缩小 gapSize，来 让元素离它的排定位置更近。
            gapSize = gapSize / 3;
        }
    }

    private static void moveItemsInTheRestZoneCloserToItsArrangedSpot(Comparable[] a, int gapSize) {
        System.out.println("~~~ gapSize为：" + gapSize + "时，对 剩余区域中的所有元素 执行 其间隔分组中的插入操作 ~~~");
        int itemAmount = a.length;
        // #1 把 a[gapSize, itemAmount-1] 区间 视为 剩余区域
        int startPointOfTheRestZone = gapSize;
        System.out.println("@@@ 当前剩余区域的起点位置为: " + startPointOfTheRestZone + " @@@");
        for (int anchorOfItemToInsert = startPointOfTheRestZone; anchorOfItemToInsert < itemAmount; anchorOfItemToInsert++) {
            // #2 对于 剩余区中的 每一个元素，使用 跨度为gapSize的插入排序 来 把 元素 移动到 更靠近其排定位置的地方
            // 🐖 虽然元素 没有被排定，但是 离排定位置更近了
            insertItemWithStepPitch(a, anchorOfItemToInsert, gapSize);
            System.out.println("当前位置" + anchorOfItemToInsert + " 的插入操作完成后，元素序列为👇");
            show(a);
            System.out.println();
        }

        System.out.println("^^^ 当前剩余区域中的 所有位置的 插入操作 完成后，元素序列为↓ ^^^");
        show(a);
        System.out.println();
    }

    // 以stepPitch作为步距，对原始数组中 指定位置上的元素 执行 其插入排序
    private static void insertItemWithStepPitch(Comparable[] originalArr,
                                                int anchorOfItemToInsert,
                                                int stepPitch) {
        System.out.println("### 对 位置" + anchorOfItemToInsert + "上的元素" +
                originalArr[anchorOfItemToInsert] + " 执行 步距为" + stepPitch + "的插入排序 ###");
        for (int backwardsCursor = anchorOfItemToInsert; backwardsCursor >= stepPitch; backwardsCursor -= stepPitch) {
            // 🐖 比较 与 交换的单位都是 stepPitch（而不是1），这就是 shellsort 高效的原因
            if (less(originalArr[backwardsCursor], originalArr[backwardsCursor - stepPitch])) {
                System.out.println("$$$ 对 位置" + backwardsCursor + "上的元素" + originalArr[backwardsCursor] +
                        " 与 位置" + (backwardsCursor - stepPitch) + "上的元素" + originalArr[backwardsCursor - stepPitch] + " 进行交换 $$$");
                exch(originalArr, backwardsCursor, backwardsCursor - stepPitch);
            }
        }
    }

    private static int initABigGapSize(int itemAmount) {
        // 按照一个公式，生成一个比较大的N值（小于itemAmount） 用于分割原始数组为子数组
        int blockSize = 0;
        while (blockSize < itemAmount / 3) {
            blockSize = 3 * blockSize + 1; // h序列：1, 4, 13, 40, 121, 364, 1093...
        }

        // 循环结束时，h是一个比较大的值...
        return blockSize;
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable itemV, Comparable itemW) {
        return itemV.compareTo(itemW) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     *  @param a
     * @param spotI
     * @param spotJ
     */
    private static void exch(Comparable[] a, int spotI, int spotJ) {
        Comparable t = a[spotI];
        a[spotI] = a[spotJ];
        a[spotJ] = t;
    }

    private static void show(Comparable[] a) {
        // 在单行中打印数组
        for (int currentSpot = 0; currentSpot < a.length; currentSpot++) {
            StdOut.print(a[currentSpot] + " ");
        }
        System.out.println();
    }

    public static boolean isSorted(Comparable[] a) {
        // 测试数组中的元素是否有序
        for (int currentSpot = 1; currentSpot < a.length; currentSpot++) {
            if (less(a[currentSpot], a[currentSpot - 1])) {
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
        System.out.println("=== 最终的排序结果 👇 ===");
        show(a);
    }
}
