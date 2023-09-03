package com.henry.sort_chapter_02.merge_sort_algorithm_02.marge_bottom_to_up_03;

/*
    分治思想的方式：用更小规模的问题的解 来 解决原始问题。

    自顶向下： 大问题 -> 拆解成为小问题 -> 解决小问题 -> 聚合小问题的解，以解决大问题
    自底向上： 从小问题出发 -> 聚合小问题的解，解决大问题。

    自底向上的算法描述：
        #1 对原始数组，以N为单位，将之分割成为 若干个大小为N的子数组；
        #2 对于#1中得到的子数组，执行两两归并操作；
        #3 更新（倍增）N，重复#1、#2，直到归并操作获得“完全排序的数组”。

    原理：归并操作能够得到 “更大的有序数组”，所以只要从小到大逐步归并出“更大的有序数组”，就能够得到“完全排序的数组”

    排序算法过程：
        #1 设置N=1，用于把原始数组分割成为M个 子数组（其中只包含单一的元素）
        #2 按照当前 itemAmountOfSubGroup 分组后，对子数组执行两两归并；
            手段：从 第一个Pair的第一个子数组 开始到 最后一个Pair的第一个子数组 为止，对每个Pair执行归并操作，得到“局部有序的子数组”
            #1 初始化当前Pair的归并区间的左右边界指针；
                左边界指针 leftBarCursor = currentPair的最左边位置
                中间位置指针 middle = leftBarCursor + itemAmountOfSubGroup - 1
                右边界指针 rightBarCursor = (leftBarCursor + itemAmountOfSubGroup*2) - 1
            #2 执行归并操作；
            #3 更新当前Pair；
                左边界指针 leftBarCursor = (leftBarCursor + itemAmountOfSubGroup*2)
                中间位置指针 middle = leftBarCursor + itemAmountOfSubGroup - 1
                右边界指针 rightBarCursor = (leftBarCursor + itemAmountOfSubGroup*2) - 1
                    🐖：这种计算方式可能会导致游标超界限，所以需要取较小值
        #3 更新（倍增）N，重复#1、#2，直到（子组的元素数量 >= 原始数组的元素数量）得到“完全排序的数组”

    有意义的变量名：
        #1 按照特定的size对原始数组进行分割 - itemAmountOfSubGroup
        #2 当前参与归并的两个子数组所组成的对 - currentPair
        #2 参与归并的区间左边界 - leftBarCursor
        #3 参与归并的区间右边界 - rightBarCursor
        #4 原始数组的元素数量 - itemAmount
        #5 归并区间的中间位置 - middle
 */
public class MergeSortBottomUpTemplate {
    private static Comparable[] aux;

    // 使用自底向上的方式 来 实现对数组完全排序
    public static void sort(Comparable[] a) {
        int itemAmount = a.length;
        aux = new Comparable[itemAmount];

        // #3 更新（倍增）N，重复#1、#2，直到（子组的元素数量 >= 原始数组的元素数量）得到“完全排序的数组”
        for (int blockSize = 1; blockSize < itemAmount; blockSize = blockSize * 2) {
            // #2 按照当前 blockSize 分组后，对得到的子数组 从左到右 执行两两归并；
            // 手段：先对当前Pair执行归并操作，再更新指针，对新的Pair执行归并操作。直到 原始数组中的最后一个Pair
            for (int leftBarOfCurrentPair = 0; leftBarOfCurrentPair < itemAmount - blockSize; leftBarOfCurrentPair += (blockSize * 2)) {
                // 随着currentPair被不断更新，rightBarCursor可能会超出原始数组的边界。因此这里使用min()
                mergeBlocksInPair(a, leftBarOfCurrentPair, leftBarOfCurrentPair + blockSize - 1,
                        Math.min((leftBarOfCurrentPair + blockSize * 2) - 1, itemAmount - 1));
            }
        }
    }

    // 归并指定闭区间中的元素
    // 特征：a[leftBar, middle] 与 a[middle+1, rightBar] - 均为闭区间
    private static void mergeBlocksInPair(Comparable[] a, int leftBarOfPair, int middleOfPair, int rightBarOfPair) {
        // 1
        for (int cursor = leftBarOfPair; cursor <= rightBarOfPair; cursor++) {
            aux[cursor] = a[cursor];
        }

        // 2
        int leftHalfCursor = leftBarOfPair;
        int rightHalfCursor = middleOfPair + 1;

        // 3
        for (int cursor = leftBarOfPair; cursor <= rightBarOfPair; cursor++) {
            if (leftHalfCursor > middleOfPair) a[cursor] = aux[rightHalfCursor++];
            else if (rightHalfCursor > rightBarOfPair) a[cursor] = aux[leftHalfCursor++];
            else if (less(aux[leftHalfCursor], aux[rightHalfCursor])) a[cursor] = aux[leftHalfCursor++];
            else a[cursor] = aux[rightHalfCursor++];
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
