package com.henry.sort_chapter_02.quick_sort_algorithm_03.basic_quick_sort_01;

import edu.princeton.cs.algs4.StdRandom;

/*
任务描述：完整地排序数组 = 把数组中的每一个元素都排定到其正确的位置
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
快速排序算法描述：
    1 排定一个元素，并使得 排定元素左边的元素都小于等于排定元素，排定元素右边的元素都 大于等于排定元素；
    2 对 左侧的元素序列 + 右侧的元素序列 分别进行快速排序；

切分算法的过程：
    过程 - 手段 - 难点；
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

    // 排定切分元素 并 使数组满足： [左区间] + 排定元素 + [右区间] 整体有序
    /*
        过程：
            1 选定切分元素；
                手段：直接选择 左区间上的元素作为 切分元素；
            2 遍历数组，把比切分元素小的元素放到切分元素左侧，把比切分元素大的元素放到切分元素右侧；
                1 交换需要交换的元素；
                    - 定义两个游标指针：左指针初始化指在左边界的位置上， 右指针初始化指在右边界的下一个位置上；
                    - 左指针向右扫描，并在遇到 大于pivot元素时停下； 右指针向左扫描,并在遇到小于pivot的元素时停下；
                    - 交换 左右指针所指向的元素；
                    - 继续移动左右指针，直到左右指针相遇 -> 这说明左区间 + 右区间中的元素都已经满足大小要求
                2 排定基准元素；
                    - 把基准元素 与 左区间的最后一个元素 交换，并返回最后一个元素的位置（此时右指针指向此位置）
     */
    private static int partition(Comparable[] a, int leftBar, int rightBar) {
        // 设置左右游标
        int leftCursor = leftBar;
        int rightBackwardsCursor = rightBar + 1;

        // 设置基准元素
        Comparable pivot = a[leftBar];

        // 用游标扫描数组中的元素 - 把小于基准元素的集合放在左边，大于基准元素的集合放在右边
        while (true) {
            while(less(a[++leftCursor], pivot)) if(leftCursor == rightBar) break; // 这种情况说明：所有其他元素都小于 基准元素 - 不需要交换元素，可以直接排定基准元素
            while(less(pivot, a[--rightBackwardsCursor])) if(rightBackwardsCursor == leftBar) break; // 所有其他元素都小于 基准元素

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
