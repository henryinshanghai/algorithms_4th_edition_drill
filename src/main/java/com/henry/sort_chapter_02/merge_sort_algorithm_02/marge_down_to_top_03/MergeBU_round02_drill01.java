package com.henry.sort_chapter_02.merge_sort_algorithm_02.marge_down_to_top_03;

/*
    分治思想的方式：用更小规模的问题的解 来解决原始问题。

    自顶向下： 大问题 -> 拆解成为小问题 -> 解决小问题 -> 聚合小问题的解，以解决大问题
    自底向上： 小问题 -> 聚合小问题的解，解决大问题。

    自底向上的算法描述：
        先归并这些个微型数组，然后再 成对地归并所得到的子数组；

        2 如何在当前的 groupSize下，实现对子数组的两两归并？
    算法过程：
        1 创建不同的size, 并根据size 来 对两组元素进行归并；
        2 分组大小从size=1开始，逐渐变大; 下一组的分组大小比起上一组翻倍,直到 组中元素的size >= N
            难点：如何 实现不同的groupSize?
            手段：for循环 + groupSize的初始化与更新
        3 在当前的groupSize下，连续执行 两个子数组的归并操作
            难点：如何更新两个子数组的区间范围？
            手段：
                1 初始化leftBarCursor + 使用groupSize来计算 rightBarCursor;
                2 更新leftBarCursor到下一批的两个子数组
 */
public class MergeBU_round02_drill01 {
    private static Comparable[] aux;

    // 使用自底向上的方式 来 实现对数组完全排序
    public static void sort(Comparable[] a) {
        int N = a.length;
        aux = new Comparable[N];

        /*
            手段：
                1 创建不同的size, 并根据size来对原始数组进行 对两组元素进行归并；
                2 分组大小从size=1开始，逐渐变大; 下一组的分组大小比起上一组翻倍,直到 组中元素的size >= N
         */
        for (int groupSize = 1; groupSize < N; groupSize = groupSize * 2) {
            // 不断向右移动游标，完成 当前size下，对所有分组的两两归并
            /*
                手段：连续执行 对当前size下两个子组的归并操作
                特征：
                    1 指针初始化：左指针指向左子组的第一个元素，右指针指向右子组的最后一个元素；
                    2 排序区间：
                        - 左指针的起始位置：0； 右指针的位置：左指针的位置 + 子组大小 * 2 - 1; - 根据图示可推算   注：这种计算方式可能会导致游标超界限，所以需要取较小值
                        - middle的位置(这是merge操作所需要的参数)： leftBar + size - 1
                    3 排序区间的更新 & 终止：
                        - 左指针游标的下一个位置H：当前位置 + 分组大小*2 ———— 这里使用游标来命名边界，可以说明它是一个动态变化的边界
                        - 什么时候不再需要更新排序区间 / 执行merge操作 / 更新leftBarCursor 呢？
                            答：当遇到最后两个子数组时。这时候会出现如下情况：
                                - 如果右指针刚好指在数组的最后一个元素，有：leftBar + groupSize * 2 - 1 = N - 1
                                    则：leftBar + groupSize * 2 = N
                                - 最后一次归并时的两个子数组，右子数组要比左子数组小。
                                    极端情况：右子数组为空，此时 - leftBar + groupSize - 1 = N - 1 => leftBar = N - groupSize
                                如果 leftBar更小的话，说明还有剩余的子数组对没有被归并 - 否则直接使用剩余的最子数组就可以了（它一定已经是有序的了）
                                demo:
                                    groupSize = 2时，
                                        1 2 3 4 5 6 就不再需要继续归并 leftBar + groupSize = N
                                        1 2 3 4 5 6 7 就还需要再做一次归并操作 - 因为剩余元素的数量 > groupSize => leftBar + groupSize < N
             */
            for (int leftBarCursor = 0; leftBarCursor < N - groupSize; leftBarCursor += (groupSize * 2)) {
                merge(a, leftBarCursor, leftBarCursor + groupSize - 1, Math.min((leftBarCursor + groupSize*2) - 1, N - 1));
            }
        }
    }

    // merge a[leftBar, middle] 与 a[middle+1, rightBar] - 均为闭区间
    // 特征：merge操作本身的代码 与自顶向下的方式完全相同
    private static void merge(Comparable[] a, int leftBar, int middle, int rightBar) {
        int leftHalfCursor = leftBar;
        int rightHalfCursor = middle + 1;

        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            aux[cursor] = a[cursor];
        }

        for (int cursor = leftBar; cursor <= rightBar; cursor++) { // pay attention to <=
            if(leftHalfCursor > middle) a[cursor] = aux[rightHalfCursor++];
            else if(rightHalfCursor > rightBar) a[cursor] = aux[leftHalfCursor++];
            else if(less(aux[leftHalfCursor], aux[rightHalfCursor])) a[cursor] = aux[leftHalfCursor++];
            else a[cursor] = aux[rightHalfCursor++]; // pay attention to "copy what to whom"
        }
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    public static void printItems(Comparable[] a) {
        int N = a.length;

        for (int cursor = 0; cursor < N; cursor++) {
            System.out.print(a[cursor] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String[] a = new String[]{"M", "E", "R", "G", "E", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);
        printItems(a);
    }
}
