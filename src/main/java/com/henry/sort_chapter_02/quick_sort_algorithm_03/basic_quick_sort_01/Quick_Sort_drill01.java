package com.henry.sort_chapter_02.quick_sort_algorithm_03.basic_quick_sort_01;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/*
任务描述：完整地排序数组 = 把数组中的每一个元素都排定到其正确的位置
算法描述：xxx

------
    排序的任务能够 用递归的方式解决吗？
    递归的要素：
        1 原始任务 能够分解成为 规模更小的同类型任务；
        2 解决 规模更小的任务（的结果）能够帮助解决原始任务。

    对排序任务来说, 什么是规模更小的任务？
        排序数组的一部分

    排序数组的一部分，能够帮助 “完全排序整个数组”吗？
        答：不尽然，因为即使数组的局部被排序了 但元素本身离它的正确位置还会有一些距离
        局部排序的结果 有可能帮助 完全排序整个数组， 但是必然需要其他操作的支持 - 比如手中的牌是 JQKA 2345

    对于快速排序，这里的“其他操作”指的就是 切分操作。
    - 切分操作为元素大小添加了额外的限制，使得：当子数组有序时，整个数组也就被自然排定了。

------
递归调用的步骤：
    1 排定切分元素到正确的位置；
    2 对左半部分进行排序；
    3 对右半部分进行排序

 */
public class Quick_Sort_drill01 {
    /**
     * 对数组中的元素进行排序
     *
     * @param a
     */
    public static void sort(Comparable[] a) {
        // 快速排序
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
    }

    /**
     * 对数组进行排序
     * 手段：快速排序
     *
     * @param a
     * @param leftBar
     * @param rightBar
     */
    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        // 1 递归调用的基线条件
        if (rightBar <= leftBar) {
            return;
        }

        // 2 选择一个切分元素，并把它放到正确的位置
        int arrangedPosition = partition(a, leftBar, rightBar); // 选取一个元素作为切分元素————把该元素放到正确的位置上

        // 3 递归调用sort()对子数组进行排序
        // 对左半部分进行排序 a[leftBar...arrangedPosition-1]
        sort(a, leftBar, arrangedPosition - 1);
        // 对右半部分进行排序 a[arrangedPosition+1...rightBar]
        sort(a, arrangedPosition + 1, rightBar);
    }

    /**
     * 选择一个元素作为切分元素，并返回元素排序后的位置
     *
     * @param a
     * @param leftBar
     * @param rightBar
     * @return
     */
    private static int partition(Comparable[] a, int leftBar, int rightBar) {
        int leftCursor = leftBar;
        int rightBackwardsCursor = rightBar + 1;
        int arrangedPosition = -1;

        Comparable pivot = a[leftBar];

        // 这时候用while比用for就省事多了 不存在步长什么的
        while (true) {
            // 从左边查找比基准元素更大的元素所在的位置
            // a[leftCursor] < v时，循环表达式成立 当a[leftCursor] > pivot（基准元素）时，循环终止
            while (less(a[++leftCursor], pivot)) if (leftCursor == rightBar) break;


            // 从右边查找比基准元素更小的元素所在的位置
            while (less(pivot, a[--rightBackwardsCursor])) if (rightBackwardsCursor == leftBar) break;

            /* 到这里就已经得到所需要的 leftCursor、rightBackwardsCursor */
            // 判断 leftCursor、rightBackwardsCursor 是否已经碰头/交叉
            // note：左边数组的最后一个元素的位置是j，而不是i
            // 因为最后一次元素交换导致i与j的值发生了变化————现在j指向左侧数组的最后一个元素
            if (leftCursor >= rightBackwardsCursor) {
                break;
            }

            // 交换 两个位置上的元素
            /*
                特征：如果左右指针刚好遇到了两个 与pivot相同的元素，那么 这两个元素也会被交换 - 这其实是多余的操作
                解决手段： 维护三个区间的快速归并
             */
            exch(a, leftCursor, rightBackwardsCursor);
        }

        // 最后，把基准元素 与 左边数组的最后一个元素 交换位置
        exch(a, leftBar, rightBackwardsCursor);

        arrangedPosition = rightBackwardsCursor;
        return arrangedPosition;
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
        String[] a = new String[]{"Q", "U", "I", "C", "K", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"}; // 10
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
