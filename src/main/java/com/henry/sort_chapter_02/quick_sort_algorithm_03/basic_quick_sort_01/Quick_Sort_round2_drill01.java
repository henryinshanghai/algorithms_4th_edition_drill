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
public class Quick_Sort_round2_drill01 {

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);

        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        // 递归终结条件：左右区间重合
        if (rightBar <= leftBar) return;

        int arrangedPosition = partition(a, leftBar, rightBar);
        sort(a, leftBar, arrangedPosition - 1);
        sort(a, arrangedPosition + 1, rightBar);
    }

    // 排定切分元素 并 使数组满足： 左区间 排定元素 右区间 整体有序
    private static int partition(Comparable[] a, int leftBar, int rightBar) {
        // 设置左右游标
        int leftCursor = leftBar;
        int rightBackwardsCursor = rightBar + 1;

        // 设置基准元素
        Comparable pivot = a[leftBar];

        // 用游标扫描数组中的元素 - 把小于基准元素的集合放在左边，大于基准元素的集合放在右边
        while (true) {
            while(less(a[++leftCursor], pivot)) if(leftCursor == rightBar) break;
            while(less(pivot, a[--rightBackwardsCursor])) if(rightBackwardsCursor == leftBar) break;

            // 结束标志：游标已经相遇
            if(rightBackwardsCursor <= leftCursor) break;
            exch(a, leftCursor, rightBackwardsCursor);
        }

        // 排定基准元素
        exch(a, leftBar, rightBackwardsCursor);

        // 返回排定元素的下标
        int arrangedPosition = rightBackwardsCursor;
        return arrangedPosition;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable v1, Comparable v2) {
        return v1.compareTo(v2) < 0;
    }

    public static void printItems(Comparable[] a) {
        int N = a.length;
        for (int i = 0; i < N; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = new String[]{"Q", "U", "I", "C", "K", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);

        printItems(a); // 预期：A C E E I K L M O P Q R S T U X
    }
}
