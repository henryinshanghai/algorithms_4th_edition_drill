package com.henry.sort_chapter_02.primary_algorithm_01.selection_sort_01;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/*
选择排序算法描述：逐一排定数组中的元素 ———— 找到/选择到未排定区间中的最小元素，并排定它

测试用例：
 *  % more tiny.txt
 *  S O R T E X A M P L E
 *
 *  % java Selection < tiny.txt
 *  A E E L M O P R S T X                 [ one string per line ]
 *
 *  % java Selection < words3.txt
 *  all bad bed bug dad ... yes yet zoo    [ one string per line ]
 */
public class SelectionSort_01 {

    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 参考具体的算法实现
        int itemAmount = a.length;
        for (int anchorCursor = 0; anchorCursor < itemAmount; anchorCursor++) {
            // 手段：并将之与当前元素进行交换
            // 特征：每次遍历，都会有一个元素被放到了正确的位置
            /*
                手段：
                    1 遍历未排定区间中的所有元素；
                    2 判断有没有比“当前最小值”更小的元素
                    3 如果有的话，更新最小值所在的位置min
             */
            int cursorToMinItem = anchorCursor; // 记录最小值的位置
            for (int dynamicCursor = anchorCursor+1; dynamicCursor < itemAmount; dynamicCursor++) {
                if (less(a[dynamicCursor], a[cursorToMinItem])){
                    cursorToMinItem = dynamicCursor;
                }
            }

            exch(a, anchorCursor, cursorToMinItem); // 把 最小值元素 与 当前元素 换个位置

            // 断言 从a[0]到a[anchorCursor]区间内的所有元素都已经是有序的
            assert isSorted(a, 0, anchorCursor);
        }

        // 断言 整个数组是有序的
        assert isSorted(a);
    }

    /* Check if array is sorted - 对于调试来说非常有用，以为它提供了验证阶段性预期的方法 */

    // 1 判断整个数组是不是已经有序了
    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    // 2 判断数组在a[lo] to a[hi]的区间内是不是已经有序了
    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            // 当前元素 是否大于 它的前一个元素
            if (less(a[i], a[i-1])) return false;
        return true;
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     * 手段： 借助中间元素
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
