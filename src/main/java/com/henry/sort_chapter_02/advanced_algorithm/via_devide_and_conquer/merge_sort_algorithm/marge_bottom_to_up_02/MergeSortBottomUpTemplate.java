package com.henry.sort_chapter_02.advanced_algorithm.via_devide_and_conquer.merge_sort_algorithm.marge_bottom_to_up_02;

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
        #1 按照特定大小的unit对原始数组进行分割 - itemAmountOfSubGroup/blockSize/unitSize
        #2 当前参与归并的两个子数组所组成的对 - currentPair
        #2 参与归并的区间左边界 - leftBarOfPair
        #3 参与归并的区间右边界 - rightBarOfPair
        #4 原始数组的元素数量 - itemAmount
        #5 归并区间的中间位置 - middle
 */
// 自底向上的归并算法的步骤：#1 使用一个小的unitSize来成对数组元素；#2 对得到的pair序列从左到右地逐个执行归并操作； #3 倍增unitSize，直到对左半、右半进行归并 - 数组被完全排序
public class MergeSortBottomUpTemplate {
    private static Comparable[] aux;

    // 使用自底向上的方式 来 实现对数组完全排序
    public static void sort(Comparable[] originalArr) {
        int itemAmount = originalArr.length;
        aux = new Comparable[itemAmount];

        // #1 初始化unitSize为1，并 以此为单位对数组中的元素成对
        for (int unitSize = 1; unitSize < itemAmount; unitSize = unitSize * 2) { // #3 更新（倍增）unitSize，重复#1、#2，直到（unitSize >= 原始数组的元素数量）得到“完全排序的数组”
            // #2 对数组中的元素对序列 从左到右 执行两两归并；
            mergeUnitsByPairTillEnd(originalArr, itemAmount, unitSize);
        }
    }

    private static void mergeUnitsByPairTillEnd(Comparable[] originalArr, int itemAmount, int unitSize) {
        // 通过 成组地移动指针(左指针、中间指针、右指针) 来 对每一个pair进行归并排序 直到最后一个pair👇
        for (int leftBarOfCurrentPair = 0; leftBarOfCurrentPair < itemAmount - unitSize; leftBarOfCurrentPair += (unitSize * 2)) { // ① 移动左指针
            // 🐖 随着currentPair被不断更新，rightBarCursor可能会超出原始数组的边界。因此这里使用min()
            mergeUnitsInPair(originalArr, leftBarOfCurrentPair, leftBarOfCurrentPair + unitSize - 1,
                    Math.min((leftBarOfCurrentPair + unitSize * 2) - 1, itemAmount - 1)); // ② 计算中间指针与右指针
        }
    }

    // 归并数组中指定闭区间中的元素
    // 特征：a[leftBar, middle] 与 a[middle+1, rightBar] - 均为闭区间
    private static void mergeUnitsInPair(Comparable[] originalArr, int leftBarOfPair, int middleOfPair, int rightBarOfPair) {
        // #1 把原始数组中“指定区间”中的元素，拷贝到辅助数组aux中
        copyItemToAux(originalArr, leftBarOfPair, rightBarOfPair);

        // #2 把元素写回到原始数组中 来 得到完全有序的数组
        writeItemToGetThemSorted(originalArr, leftBarOfPair, middleOfPair, rightBarOfPair);
    }

    private static void writeItemToGetThemSorted(Comparable[] originalArr, int leftBarOfPair, int middleOfPair, int rightBarOfPair) {
        // #1 准备左右游标指针 - 用于比较元素，得到“正确的元素”
        int leftHalfCursor = leftBarOfPair;
        int rightHalfCursor = middleOfPair + 1;

        // #2 对于原始数组中的“当前待排定的位置”...
        for (int currentSpotToArrange = leftBarOfPair; currentSpotToArrange <= rightBarOfPair; currentSpotToArrange++) {
            // 比较辅助数组中，左右指针所指向的元素。然后把“较小的元素” 绑定到 原始数组“待排定的位置”上
            if (leftHalfCursor > middleOfPair) originalArr[currentSpotToArrange] = aux[rightHalfCursor++];
            else if (rightHalfCursor > rightBarOfPair) originalArr[currentSpotToArrange] = aux[leftHalfCursor++];
            else if (less(aux[leftHalfCursor], aux[rightHalfCursor])) originalArr[currentSpotToArrange] = aux[leftHalfCursor++];
            else originalArr[currentSpotToArrange] = aux[rightHalfCursor++];
        }
    }

    private static void copyItemToAux(Comparable[] originalArr, int leftBarOfPair, int rightBarOfPair) {
        for (int currentSpotCursor = leftBarOfPair; currentSpotCursor <= rightBarOfPair; currentSpotCursor++) {
            aux[currentSpotCursor] = originalArr[currentSpotCursor];
        }
    }

    private static boolean less(Comparable itemV, Comparable itemW) {
        return itemV.compareTo(itemW) < 0;
    }

    public static void printItems(Comparable[] a) {
        int itemAmount = a.length;

        for (int currentSpotCursor = 0; currentSpotCursor < itemAmount; currentSpotCursor++) {
            System.out.print(a[currentSpotCursor] + " ");
        }

        System.out.println();
    }

    // 单元测试 - 测试sort()方法是否实现了预期的功能
    public static void main(String[] args) {
        String[] a = new String[]{"M", "E", "R", "G", "E", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);
        printItems(a);
    }
}
