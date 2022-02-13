package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

import edu.princeton.cs.algs4.StdOut;

public class ShellSort_03 {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 希尔排序
        // 把a[]中的元素按照升序排列
        int N = a.length;
        int h = 1;
        while(h < N/3){
            h = 3*h + 1; // h序列：1, 4, 13, 40, 121, 364, 1093...
        } // 循环结束时，h是一个比较大的值...

        // 这里每一次的局部排序是分开的 虽然算法描述是对每一个子数据序列进行局部排序，但是实际实现时局部排序是分开进行的
        // 不清楚循环会执行多少次时，使用while循环
        while(h >= 1){
            // 把数组变成局部有序 h有序    h值每改变一次，就会对数组进行一次 h- 排序
            /*
                手段：
                1 根据h的值来挑选出待排序的几组子数据序列（对于N=16 h=13的数组来说，这样的子数据序列就只有N-h个）；
                    待排序的子数据序列就只有（N-h）组      作用：用作外循环的次数
                2 对“当前子数据序列”进行插入排序（参见drill02）
                    1 记录当前元素；   [0, h-1] [h, 2h-1]... 子序列中的当前元素是h   手段：j=i
                    2 比较当前元素与当前元素的前一个元素（根据情况决定是交换还是终止循环）；
                    3 根据需要把比较向前推进
                    4 考虑i=0与比较的边界情况：j > 0
                3 把同样的过程向前推进
             */
            for(int i = h; i < N; i++){ // 内循环的次数
                // 每次内循环：完成一组待排序子序列的排序工作
                // 但其实每次内循环就只是向有序区添加了一个元素而已     这能保证待排序子序列的有序性吗？
                /*
                    可以的。虽然内循环其实就只是向有序区添加了一个元素，但是由于h与N的关系，其实得到的子数据序列就只有两个元素而已
                 */
                // 对当前子数据序列完成排序
                // 把a[i]插入到a[i-h],a[i-2*h],a[i-3*h...之中
                for(int j=i; j>=h && less(a[j], a[j-h]); j -= h){ // 每次循环会处理所有子数据序列的有序区
                    // h=13 i=13 j=13 j-h=0 a[0] a[13] 比较/交换 跳出内循环
                    // h=13 i=14 j=14 j-h=1 a[1] a[14] 比较/交换 j=14-13=1 不满足条件，退出内循环
                    // h=13 i=15 j=15 j-h=2 a[2] a[15] 比较/交换 j=2 不满足条件，退出内循环


                    // h=4 i=4 j=4 j-h=0 a[0] a[4] 比较/交换 j=0 跳出循环   第一个子数据序列的第一次CAS操作
                    // h=4 i=5 j=5 j-h=1 a[1] a[5] 比较/交换 j=1 跳出内循环
                    // ...
                    // h=4 i=8 j=8 j-h=4 a[4] a[8] 比较/交换 j=4 a[0] a[4] 比较/交换    第一个子数据序列的第二次CAS操作

                    // 随着i的增大，j会随之增大，也就能像插入排序一样，从后往前把元素插入到有序队列中
                    exch(a, j, j - h);
                }
            }
            // 两层循环结束后，h值所产生的各个子数据序列都是有序的
            h = h/3;
        }
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

    public static boolean isSorted(Comparable[] a){
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
