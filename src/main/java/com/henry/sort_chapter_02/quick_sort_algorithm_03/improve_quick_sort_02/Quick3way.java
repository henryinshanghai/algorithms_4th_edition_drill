package com.henry.sort_chapter_02.quick_sort_algorithm_03.improve_quick_sort_02;

import edu.princeton.cs.algs4.StdRandom;

public class Quick3way {
    public static void sort(Comparable[] a){
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        if(rightBar <= leftBar) return;

        // lessZone的右边界
        int lessZoneBoundary = leftBar;
        // equalZone的右边界
        int equalZoneBoundary = leftBar + 1;
        // greaterZone的左边界
        int greaterZoneBoundary = rightBar;

        // 基准元素
        Comparable pivot = a[leftBar];

        /*
            维护出 a[lo..lt-1] < v=a[lt..gt] < a[gt..hi]的有序序列
            手段：
                比较当前元素 与 基准元素：
                    如果更大，把当前元素交换到greaterZone中 - 手段：交换 + 向左移动边界游标
                    如果更小，把当前元素交换到lessZone中 - 手段：交换 + 向有移动边界游标 这里的i为什么要++？
                        因为所交换过来的元素的值一定是 pivot，那是不是没有++也能工作呢？ seems so
                    如果相等，只移动 equalZone的边界游标
                        相当于为equalZone添加了一个元素

            维护结果：
                1 a[lo..lt-1] < v;
                2 a[lt..i-1] = v;
                3 a[i..gt] 待定;
                4 a[gt+1..hi] > v.

            疑问：
                1 为什么lt指针指向的元素 总是能够等于 pivot元素？
                答：由于lt指针总是落后于 i指针（表示当前正在处理的元素），所以当lt指针++时，它所指向的元素其实是被处理过的
                因此可以肯定 它所指向的元素 是 pivot元素
         */
        while (equalZoneBoundary <= greaterZoneBoundary) {
            int compareResult = a[equalZoneBoundary].compareTo(pivot);

            if (compareResult < 0) exch(a, lessZoneBoundary++, equalZoneBoundary++);
            else if(compareResult > 0) exch(a, equalZoneBoundary, greaterZoneBoundary--);
            else equalZoneBoundary++;
        }

        sort(a, leftBar, lessZoneBoundary - 1);
        sort(a, greaterZoneBoundary + 1, rightBar);
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void printItems(Comparable[] a) {
        int N = a.length;

        for (int cursor = 0; cursor < N; cursor++) {
            System.out.print(a[cursor] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = new String[]{"R", "B", "W", "W", "R", "W", "B", "R", "R", "W", "B", "R"};

        sort(a);

        printItems(a);
    }


}
