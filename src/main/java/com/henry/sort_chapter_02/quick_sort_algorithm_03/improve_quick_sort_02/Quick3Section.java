package com.henry.sort_chapter_02.quick_sort_algorithm_03.improve_quick_sort_02;

import edu.princeton.cs.algs4.StdRandom;

/*
算法描述：
    1 从左到右遍历数组中的元素，然后通过维护指针的位置 来排定 所有值等于pivot的元素集合；
    2 再对剩余未排序的两个区间 分别进行快速排序；

算法难点：
    怎么能够排定所有 与pivot元素相同的元素集合？
    手段：
        使用一个指针遍历数组中的元素：
            对于当前元素，如果它比pivot小的话。则：
                1 把当前元素 与 lessZoneRightBoundary指向的元素交换 - 向lessZone中添加元素；
                2 移动当前指针 来 处理下一个元素；
                    特征： 这里不需要处理交换过来的元素，因为交换过来的元素总是等于 pivot 或者说 lessZoneRightBoundary指针总是指向等于pivot的元素
                3 移动 lessZoneRightBoundary 来扩展lessZone的边界；
            如果它比pivot大的话，则：
                1 把当前元素 与 greaterZoneLeftBoundary指向的元素交换 - 向greaterZone中添加元素；
                2 这里不移动当前指针，因为 交换过来的元素也需要进行比较处理；
                3 移动 greaterZoneLeftBoundary 来扩展greaterZone的边界；
            如果它和pivot一样大的话，则：
                移动指针到下一个元素； - 这会产生 a[lessZoneRightBoundary..cursorOfItemToCompare-1] = pivot 的结果
        最终，在 当前元素的指针 与 greaterZone的左边界指针 相遇后，会再执行一次比较（因为指针指向的元素大小不确定）
        循环至此结束，任务达成
 */
public class Quick3Section {
    public static void sort(Comparable[] a){
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        if(rightBar <= leftBar) return;

        // lessZone section的右边界
        int lessZoneRightBoundary = leftBar;
        // 待与pivot比较的元素的指针（从左往右前进）
        int cursorOfItemToCompare = leftBar + 1;
        // greater Zone的左边界
        int greaterZoneLeftBoundary = rightBar;

        // 基准元素
        Comparable pivot = a[leftBar];

        /*
            维护出 a[leftBar..lessZoneRightBoundary-1] < pivot=a[lessZoneRightBoundary..greaterZoneLeftBoundary] < a[greaterZoneLeftBoundary..hi]的有序序列
            手段：
                比较当前元素 与 基准元素：
                    如果更大，把当前元素交换到greaterZone中 - 手段：交换 + 向左移动边界游标
                    如果更小，把当前元素交换到lessZone中 - 手段：交换 + 向有移动边界游标 这里的i为什么要++？
                        因为所交换过来的元素的值一定是 pivot，那是不是没有++也能工作呢？ seems so
                    如果相等，只移动 equalZone的边界游标
                        相当于为equalZone添加了一个元素

            维护结果：
                1 a[leftBar..lessZoneRightBoundary-1] < pivot;
                2 a[lessZoneRightBoundary..cursorOfItemToCompare-1] = pivot; 
                3 a[cursorOfItemToCompare..greaterZoneLeftBoundary] 待定;
                4 a[greaterZoneLeftBoundary+1..hi] > pivot.

            疑问：
                1 为什么lessZoneRightBoundary指针指向的元素 总是能够等于 pivot元素？
                答：由于lessZoneRightBoundary指针总是落后于 i指针（表示当前正在处理的元素），所以当lessZoneRightBoundary指针++时，它所指向的元素其实是被处理过的
                因此可以肯定 它所指向的元素 是 pivot元素
         */
        while (cursorOfItemToCompare <= greaterZoneLeftBoundary) {
            int compareResult = a[cursorOfItemToCompare].compareTo(pivot);

            if (compareResult < 0) exch(a, lessZoneRightBoundary++, cursorOfItemToCompare++);
            else if(compareResult > 0) exch(a, cursorOfItemToCompare, greaterZoneLeftBoundary--);
            else cursorOfItemToCompare++;
        } // 最终的状态是：lessZoneRightBoundary指向了 相等section的第一个位置, greaterZoneLeftBoundary指向了 相等section的最后一个位置

        sort(a, leftBar, lessZoneRightBoundary - 1);
        sort(a, greaterZoneLeftBoundary + 1, rightBar);
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
