package com.henry.sort_chapter_02.quick_sort_algorithm_03.improve_quick_sort_02;

import edu.princeton.cs.algs4.StdRandom;

/*

排序算法过程：
    #1 判断区间的左右边界是否相等 aka 数组是否已经有序了
    #2 排定等于某个值的元素序列；
        手段：分拣出 小于基准元素的元素序列、等于基准元素的元素序列、大于基准元素的元素序列
    #3 对小于区域、大于区域的元素序列进行排序；

分拣算法过程：
    #1 准备3个指针：lessZoneRightBarCursor、currentItemCursor、greaterZoneLeftBarCursor
    #2 指定基准元素；
    #3 把小于基准元素的元素分拣到左边，把大于基准元素的元素分拣到右边，把等于基准元素的元素分拣到中间。
        从currentItemCursor开始，逐个比较当前元素 与 基准元素， 根据比较结果 来 扩展小于区域、大于区域、等于区域，收窄不确定区域。
        当 currentItemCursor 与 greaterZoneLeftBarCursor相遇时，排定完成。
        特征：
            #1 在小于基准元素时，不需要处理交换过来的元素（因为交换过来的元素总是等于 pivot 或者说 lessZoneRightBoundary指针总是指向等于pivot的元素），所以可以后移指针
            #2 在大于基准元素时，需要处理交换过来的元素（因为其值大小不确定），所以不能后移当前指针；
            #3 最终，在 当前元素的指针 与 greaterZone的左边界指针 相遇后，会再执行一次比较（因为指针指向的元素大小不确定）

 */
public class QuickSort3WayTemplate {
    public static void sort(Comparable[] a){
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        if(rightBar <= leftBar) return;

        // #1 准备3个指针
        // lessZone section的右边界
        int lessZoneRightBoundary = leftBar;
        // 待与pivot比较的元素的指针（从左往右前进）
        int cursorOfItemToCompare = leftBar + 1;
        // greater Zone的左边界
        int greaterZoneLeftBoundary = rightBar;

        // #2 基准元素
        Comparable pivot = a[leftBar];

        // #3 排定与基准元素相等的元素序列 a[lessZoneRightBoundary, greaterZoneLeftBoundary]
        // 手段：比较当前元素 与 基准元素，根据比较结果 来 移动各个边界的指针 与 当前元素的指针
        while (cursorOfItemToCompare <= greaterZoneLeftBoundary) { // #3 当currentItemCursor = greaterZone时，排定结束
            // #1 比较当前元素与基准元素
            int compareResult = a[cursorOfItemToCompare].compareTo(pivot);

            // #2 根据比较结果，移动各个边界的指针 - a[lessZone, currentItem]的区间 都是值等于pivot的元素
            if (compareResult < 0) exch(a, lessZoneRightBoundary++, cursorOfItemToCompare++); // 交换得到的总是与pivot相等的元素
            else if(compareResult > 0) exch(a, cursorOfItemToCompare, greaterZoneLeftBoundary--);
            else cursorOfItemToCompare++;
        }

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
//        String[] a = new String[]{"R", "B", "W", "W", "R", "W", "B", "R", "R", "W", "B", "R"};
        String[] a = new String[]{"H", "E", "N", "R", "Y", "A", "N", "D", "A", "L", "I", "C", "I", "A", "L", "I", "V", "E", "I", "N", "S", "H"};

        sort(a);

        printItems(a);
    }


}
