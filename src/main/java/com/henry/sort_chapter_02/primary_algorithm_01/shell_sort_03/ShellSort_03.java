package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

import edu.princeton.cs.algs4.StdOut;

/*
    算法思路描述：
        对原始数组，按照指定间隔分组，然后对每一个子组进行排序；
        把间隔调小直到1，重复以上行为，直到整个数组被完全排序。
    原理：
        分组间隔比较大时，只需要 对少量的数据交换很少的次数，就能把元素放到 离最终排定位置很近的位置上；
        分组间隔比较小时，需要 对大量的数据交换很少的次数（因为它们离自己的最终位置其实很近了）。
        综合来说，比起直接使用插入排序，交换的次数要少很多

    算法过程：
        1

    有意义的变量名：
        1 用来把原始数组 分组成为 元素子序列的跨距 - span
        2 指向 开始执行插入操作的元素 的指针 - secondItemInSequence
        3 指向 待插入到有序区中的元素 的指针 - cursorOfItemToInsert
        4 从后往前遍历有序序列时 所使用的指针 - backwardsCursor

    严格的边界条件：
        1 从序列的第二个元素开始，到数组的最后一个元素为止；
        2 执行交换时，backwardsCursor的边界位置在第二个元素上
 */
public class ShellSort_03 {

    public static void sort(Comparable[] a) {
        // 先把序列元素更新到 最大元素
        int itemAmount = a.length;
        int span = 1;
        while (span < itemAmount / 3) {
            span = 3 * span + 1; // h序列：1, 4, 13, 40, 121, 364, 1093...
        } // 循环结束时，h是一个比较大的值...

        // 完成对数组中所有元素的排序
        /*
            手段：
                1 对于当前的gap/span, 对得到的所有子数组进行排序；- aka 把数组变成局部有序(h有序)
                2 调整当前的gap/span, 并继续对得到的所有子数组进行排序；
         */
        while (span >= 1) { // 不清楚循环会执行多少次时，使用while循环
            // 把数组变成 span排序
            /*
                手段：
                    从 span位置上的元素开始，对 每一个子元素序列 进行排序 - 插入排序
             */
            int secondItemInSequence = span;
            for (int cursorOfItemToInsert = secondItemInSequence; cursorOfItemToInsert < itemAmount; cursorOfItemToInsert++) { // 内循环的次数
                // 作用：完成一组 待排序子序列的排序工作???
                /*
                    这里内循环的作用并不是对 当前子元素序列 完成排序.
                    而是在遍历数组元素的过程中，完成 对 每一个子元素序列 的排序。

                    原理：对于每一个当前元素，对于它所在的 子元素序列来说，都是 待插入的元素
                    特征：
                        1 当前元素的起始位置为 span - 也就是第一个子元素序列中的第二个元素
                        2 当前元素应在怎么命名？ cursorOfItemToInsert
                        3 这里通过 交换操作 来保证 交换发生在 预期的子元素序列中 - 这些子元素序列是逻辑上划分的，而没有物理上创建
                 */
                // 作用：把a[cursorOfItemToInsert]插入到a[cursorOfItemToInsert-span],a[cursorOfItemToInsert-2*span],a[cursorOfItemToInsert-3*span...之中
                /*
                    手段：按照 span的跨度，来把当前元素 插入到 它所属的有序序列中。直到最后一个元素也被插入到 它所属的序列中
                    特征：
                        1 随着 cursorOfItemToInsert的增大，循环的次数会变多；
                        2 循环终止的条件是：往回走的指针 落在了 元素序列的第二个元素上 - 因为这时候还能获取到有效的 "前一个元素"去交换
                        3 这里交换的步距是 span
                 */
                for (int backwardsCursor = cursorOfItemToInsert;
                     backwardsCursor >= secondItemInSequence && less(a[backwardsCursor], a[backwardsCursor - span]);
                     backwardsCursor -= span) {
                    exch(a, backwardsCursor, backwardsCursor - span);
                }
            }

            // 两层循环结束后，h值所产生的各个子数据序列都是有序的
            span = span / 3;
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
