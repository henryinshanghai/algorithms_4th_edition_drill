package com.henry.sort_chapter_02.primary_algorithm_01.insert_sort_02;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/*
算法思路描述：
    把无序区域的当前元素 插入到一个“有序区域”中，并在插入结束后保持“有序区域”的“元素有序性”

算法原理：
    连续比较 + 连续交换

术语定义：
    有序区域：区域中的元素满足连续升序或连续降序；
    元素有序性：序列中的元素满足连续升序 或 连续降序；

算法步骤：
    #1 把第一个元素视为一个 只有一个元素的有序区域；
    #2 获取到有序区域后面的一个元素，把它插入到 当前的有序区域中；
        手段：在有序区域中，使用一个从后往前的游标指针。通过不断地比较与交换，把待插入元素交换到正确的预期位置中(不是最终的排定位置)
        - 1 比较有序区域的最后一个元素 与 当前待插入的元素，如果当前待插入的元素更小，就交换它们；
        - 2 比较有序区域的倒数第二个元素 与 当前待插入的元素，如果当前待插入元素更小，就交换它们；
        如此往复，直到 待插入元素已经到达预期位置 或者 待插入元素已经到达数组的起始位置
    #3 对每一个 新的待插入元素，做2中同样的操作

有意义的变量名：
    指向待插入元素的指针 - cursorOfItemToInsert
    从后往前遍历有序区的游标指针 - backwardsCursor
    数组元素的数量 - itemAmount;

特征：使用扑克牌模拟排序过程时，无法有效地模拟比较操作，但是能够很好地模拟插入操作。
插入排序是真实世界中玩扑克牌会使用的排序方式。

严格的边界条件：
    循环条件的猜想与验证：
        猜想 - 当前场景的极限情况
        验证 - 使用猜想得到的边界条件会不会导致 下标越界
流程中的debug：
    assert <预期目标>
 */
public class InsertSortTemplate {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        int itemAmount = a.length;

        for (int cursorOfItemToInsert = 1; cursorOfItemToInsert < itemAmount; cursorOfItemToInsert++) {
            // #2 把有序区域后面的一个元素（无序区的第一个元素） 插入到 有序区合适的位置
            /*
                手段：比较（游标指针指向的元素 与 它的前一个元素） & 交换（如果当前元素更小，就执行交换）
                如果有必要（less & exch），则：比较的操作要向前推进(j--)
                如果没必要（less不成立），则：比较操作终止（for循环）
             */
            for (int backwardsCursor = cursorOfItemToInsert;
                 backwardsCursor > 0 && less(a[backwardsCursor], a[backwardsCursor-1]); backwardsCursor--) {
                exch(a, backwardsCursor, backwardsCursor-1);
            }

            // 断言 当前的有序区域是元素有序的
            assert isSorted(a, 0, cursorOfItemToInsert);
        }

        // 断言 整个数组已经是有序的了
        assert isSorted(a);
    }

    private static boolean isSorted(Comparable[] a, int leftBar, int rightBar) {
        for (int cursor = leftBar + 1; cursor <= rightBar; cursor++) {
            if(less(a[cursor], a[cursor - 1])) return false;
        }

        return true;
    }

    public static boolean isSorted(Comparable[] a){
        return isSorted(a, 0, a.length - 1);
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     * @param a
     * @param i
     * @param j
     */
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void show(Comparable[] a){
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }


    // 测试用例
    public static void main(String[] args) {
        // 从标准输入中读取字符串，然后把它们排序输出
        String[] a = In.readStrings();
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}