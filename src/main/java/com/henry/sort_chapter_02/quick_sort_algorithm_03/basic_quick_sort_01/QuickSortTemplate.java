package com.henry.sort_chapter_02.quick_sort_algorithm_03.basic_quick_sort_01;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/*
任务描述：完整地排序数组 = 把数组中的每一个元素都排定到其正确的位置

递归的可行性分析：
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

排序算法的步骤：
    1 排定切分元素到正确的位置；
    2 对左半部分进行排序；
    3 对右半部分进行排序

切分操作算法描述：
    #1 找到所有比切分元素小的元素，汇集到左半区间；
    #2 找到所有比切分元素大的元素，汇集到右半区间；
    #3 把切分元素 与 左半区间的最后一个元素互换位置 - 切分元素即被排定。

切分操作算法步骤：
    #1 准备左半区间（从左往右）与右半区间（从右往左）的指针；
    #2 准备基准元素
    #3 把所有其他元素分拣到左半区间、右半区间中；
    #4 排定基准元素，并返回排定的位置；

分拣元素的算法步骤：
    #1 让左指针停在 从左往右第一个比基准元素更大的元素上
        手段：在当前元素满足“比基准元素小”的条件时，使用while循环不断移动指针
        语法：while循环终止的两种方式 - #1 条件语句为false; #2 在语句块中出现了break;
    #2 让右指针停在 从右往左第一个比基准元素小的元素上
    #3 判断元素的分拣工作是否已经完成（左指针是不是已经大于右指针） 如果是，则：停止分拣工作
    #4 对元素进行分拣 - 把它们分别分拣到左半区间、右半区间中
 */
public class QuickSortTemplate {
    /**
     * 对数组中的元素进行排序
     *
     * @param a
     */
    public static void sort(Comparable[] a) {
        // 快速排序
        StdRandom.shuffle(a);
        sort(a, 0, a .length - 1);
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
        // 1 排序区间只有一个元素时，数组已经有序。这时应该return
        if (rightBar <= leftBar) {
            return;
        }

        // 2 选择一个切分元素，并把它放到正确的位置
        int arrangedPosition = partition(a, leftBar, rightBar); // 选取一个元素作为切分元素————把该元素放到正确的位置上

        // 3 对左半区间进行排序
        sort(a, leftBar, arrangedPosition - 1);
        // 对右半区间进行排序
        sort(a, arrangedPosition + 1, rightBar);
    }

    // 在数组的指定区间（闭区间）中，排定一个切分元素，并返回排定的位置
    private static int partition(Comparable[] a, int leftBar, int rightBar) {
        // #1 准备左右边界的指针
        int leftCursor = leftBar;
        int rightBackwardsCursor = rightBar + 1;

        // #2 准备基准元素
        Comparable pivot = a[leftBar];

        // #3 把剩余的其他元素（除了基准元素）分拣到左半区间、右半区间中  特征：死循环 + 执行体break
        while (true) {
            // #1 让左指针停在 从左往右第一个比基准元素更大的元素上
            // 🐖 如果所有元素都比基准元素小的话，则：左指针会一直向右移动，直到等于rightBar 这种情况下，需要停止移动指针
            while (less(a[++leftCursor], pivot)) if (leftCursor == rightBar) break;


            // #2 让右指针停在 从右往左第一个比基准元素小的元素上
            // 如果基准元素大于其他的任何元素，则：循环条件直接不成立，右指针不会向左移动
            while (less(pivot, a[--rightBackwardsCursor])) if (rightBackwardsCursor == leftBar) break;

            // #3 判断元素的分拣工作是否已经完成
            // 当左指针与右指针相遇时，说明元素分拣已经完成 - 小于基准元素的元素都在左侧区间、大于基准元素的元素都在右侧区间
            if (leftCursor >= rightBackwardsCursor) {
                break;
            }

            // #4 对元素进行分拣 手段：交换 两个位置上的元素
            // 特征：如果左右指针刚好遇到了两个 与pivot相同的元素，那么 这两个元素也会被交换 - 这其实是多余的操作
            exch(a, leftCursor, rightBackwardsCursor);
        }

        // #4 排定基准元素 aka 把基准元素放到它正确的位置上     手段: 把基准元素 与 左边数组的最后一个元素 交换位置
        exch(a, leftBar, rightBackwardsCursor);

        int arrangedPosition = rightBackwardsCursor;
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
