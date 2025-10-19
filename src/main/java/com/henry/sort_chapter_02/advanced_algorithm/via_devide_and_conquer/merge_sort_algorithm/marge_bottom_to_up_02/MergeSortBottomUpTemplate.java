package com.henry.sort_chapter_02.advanced_algorithm.via_devide_and_conquer.merge_sort_algorithm.marge_bottom_to_up_02;

/*
    分治思想的方式：用 更小规模的问题的解 来 解决 原始问题。

    自顶向下： 大问题 -> 拆解成为 小问题 -> 解决 小问题 -> 聚合 小问题的解，以 解决大问题
    自底向上： 从 小问题 出发 -> 聚合 小问题的解，解决 大问题。

    自底向上的算法描述：
        #1 对 原始数组，以N为单位，将之 分割成为 若干个 大小为N的 子数组；
        #2 对于 #1中得到的子数组，执行 两两归并操作；
        #3 更新（倍增）N，重复#1、#2，直到 归并操作 获得 “完全排序的数组”。

    原理：归并操作 能够得到 “更大的有序数组”，所以 只要 从小到大 逐步归并出 “更大的有序数组”，就能够得到 “完全排序的数组”

    排序算法过程：
        #1 设置N=1，用于 把 原始数组 分割成为 M个 子数组（其中 只包含 单一的元素）
        #2 按照 当前 itemAmountOfSubGroup 分组 后，对 子数组 执行两两归并；
            手段：从 第一个Pair的第一个子数组 开始到 最后一个Pair的第一个子数组 为止，对 每个Pair 执行 归并操作，得到 “局部有序的子数组”
            #1 初始化 当前Pair的 归并区间的 左右边界指针；
                左边界指针 leftBarCursor = currentPair的最左边位置
                中间位置指针 middle = leftBarCursor + itemAmountOfSubGroup - 1
                右边界指针 rightBarCursor = (leftBarCursor + itemAmountOfSubGroup*2) - 1
            #2 执行 归并操作；
            #3 更新 当前Pair；
                左边界指针 leftBarCursor = (leftBarCursor + itemAmountOfSubGroup*2)
                中间位置指针 middle = leftBarCursor + itemAmountOfSubGroup - 1
                右边界指针 rightBarCursor = (leftBarCursor + itemAmountOfSubGroup*2) - 1
                    🐖：这种计算方式 可能会导致 游标 超界限，所以 需要 取较小值
        #3 更新（倍增）N，重复#1、#2，直到（子组的元素数量 >= 原始数组的元素数量）得到“完全排序的数组”

    有意义的变量名：
        #1 按照 特定大小的unit 对 原始数组 进行分割 - itemAmountOfSubGroup/blockSize/unitSize
        #2 当前 参与归并的两个子数组 所组成的对 - currentPair
        #2 参与归并的 区间左边界 - leftBarOfPair
        #3 参与归并的 区间右边界 - rightBarOfPair
        #4 原始数组的元素数量 - itemAmount
        #5 归并区间的中间位置 - middle
 */
// 自底向上的 归并算法的步骤：#1 使用 一个小的unitSize 来 成对 数组元素；#2 对 得到的pair序列 从左到右地 逐个执行 归并操作；
// #3 倍增 unitSize，直到 对 左半、右半 进行归并 - 数组 被完全排序
public class MergeSortBottomUpTemplate {
    private static Comparable[] aux;

    // 使用 自底向上的方式 来 实现 对数组的完全排序
    public static void sort(Comparable[] originalArr) {
        int itemAmount = originalArr.length;
        aux = new Comparable[itemAmount];

        // #1 初始化unitSize为1，并 以此为单位 对数组中的元素成对
        for (int unitSize = 1; unitSize < itemAmount; unitSize = unitSize * 2) { // #3 更新（倍增）unitSize，重复#1、#2，直到（unitSize >= 原始数组的元素数量）得到“完全排序的数组”
            // #2 对 数组中的元素对序列 从左到右 执行两两归并；
            mergeUnitsByPairTillEnd(originalArr, unitSize);
        }
    }

    /**
     * 对于 指定的元素序列，以 指定的单元大小 从左往右地 对其中的元素 进行 成对操作，并 对 所有对中的两个单元 进行归并
     *
     * @param originalArr 原始的元素序列
     * @param unitSize    用于成对的单元大小
     */
    private static void mergeUnitsByPairTillEnd(Comparable[] originalArr,
                                                int unitSize) {
        System.out.println("~~~ 当前的unitSize大小为：" + unitSize + " ~~~");
        int itemAmount = originalArr.length;

        // 通过 整组地移动指针(左指针、中间指针、右指针) 来 对 每一个pair 进行归并排序 直到 最后一个pair👇
        for (int leftBarOfCurrentPair = 0; leftBarOfCurrentPair < itemAmount - unitSize; leftBarOfCurrentPair += (unitSize * 2)) { // ① 移动左指针
            // 🐖 随着currentPair 被不断更新，rightBarCursor 可能会 超出 原始数组的边界。因此 这里使用min() 来 避免超出边界
            int expectedRightBarOfCurrentPair
                    = (leftBarOfCurrentPair + unitSize * 2) - 1;
            int maxRightBar = itemAmount - 1;

            mergeUnitsInPair(originalArr,
                    leftBarOfCurrentPair,
                    leftBarOfCurrentPair + unitSize - 1,
                    Math.min(expectedRightBarOfCurrentPair, maxRightBar)); // ② 计算中间指针与右指针
            System.out.println();
        }
    }

    /**
     * 对 当前对中的两个单元（中的元素） 进行归并
     * 🐖 两个单元（a[leftBar, middle] 与 a[middle+1, rightBar]） - 都是 闭区间
     *
     * @param originalArr    原始的元素序列
     * @param leftBarOfPair  当前对的左边界
     * @param middleOfPair   当前对的中间位置
     * @param rightBarOfPair 当前对的右边界
     */
    private static void mergeUnitsInPair(Comparable[] originalArr,
                                         int leftBarOfPair,
                                         int middleOfPair,
                                         int rightBarOfPair) {
        System.out.println("--- 当前单元对的左边界为：" + leftBarOfPair + "，中间位置为：" + middleOfPair +
                "，右边界为：" + rightBarOfPair + " ---");
        // #1 把原始数组中“指定区间”中的元素，拷贝到辅助数组aux中
        copyItemToAux(originalArr, leftBarOfPair, rightBarOfPair);

        // #2 把元素写回到原始数组中 来 得到完全有序的数组
        writeCorrectItemBack(originalArr, leftBarOfPair, middleOfPair, rightBarOfPair);
    }

    private static void writeCorrectItemBack(Comparable[] originalArr,
                                             int leftBarOfPair,
                                             int middleOfPair,
                                             int rightBarOfPair) {
        // #1 准备左右游标指针 - 用于比较元素，得到“正确的元素”
        int cursorOfItemToCompareInUnit1 = leftBarOfPair;
        int cursorOfItemToCompareInUnit2 = middleOfPair + 1;

        // #2 对于 原始数组中的“当前待排定的位置”...
        for (int currentSpotToArrange = leftBarOfPair; currentSpotToArrange <= rightBarOfPair; currentSpotToArrange++) {
            System.out.println("@@@ 当前待排定的位置是：" + currentSpotToArrange + " @@@");

            // 比较 辅助数组中，左右指针 所指向的元素。然后 把 “较小的元素” 绑定到 原始数组“待排定的位置” 上
            if (cursorOfItemToCompareInUnit1 > middleOfPair) {
                System.out.println("### 当前 左半部分元素耗尽，依次拷贝 右半部分元素 到 当前待排定位置" + currentSpotToArrange + " ###");
                originalArr[currentSpotToArrange] = aux[cursorOfItemToCompareInUnit2++];
            } else if (cursorOfItemToCompareInUnit2 > rightBarOfPair) {
                System.out.println("$$$ 当前 右半部分元素耗尽，依次拷贝 左半部分元素 到 当前待排定位置" + currentSpotToArrange + " $$$");
                originalArr[currentSpotToArrange] = aux[cursorOfItemToCompareInUnit1++];
            } else if (less(aux[cursorOfItemToCompareInUnit1], aux[cursorOfItemToCompareInUnit2])) {
                System.out.println("%%% 把 两个待比较元素(" + aux[cursorOfItemToCompareInUnit1] + ", " + aux[cursorOfItemToCompareInUnit2] + ")中的 较小元素"
                        + aux[cursorOfItemToCompareInUnit1] + " 填充到 当前待排定位置 " + currentSpotToArrange + " 上 %%%");
                originalArr[currentSpotToArrange] = aux[cursorOfItemToCompareInUnit1++];
            } else {
                System.out.println("^^^ 把 两个待比较元素(" + aux[cursorOfItemToCompareInUnit1] + ", " + aux[cursorOfItemToCompareInUnit2] + ")中的较小元素 "
                        + aux[cursorOfItemToCompareInUnit2] + " 填充到 当前待排定位置 " + currentSpotToArrange + " 上 ^^^");
                originalArr[currentSpotToArrange] = aux[cursorOfItemToCompareInUnit2++];
            }
        }
    }

    private static void copyItemToAux(Comparable[] originalArr,
                                      int leftBarOfPair,
                                      int rightBarOfPair) {
        System.out.println("!!! 拷贝 原始序列闭区间[" + leftBarOfPair + "," + rightBarOfPair + "]中的元素： "
                + showInStr(originalArr, leftBarOfPair, rightBarOfPair) + " 到 辅助数组中 !!!");
        for (int currentSpotCursor = leftBarOfPair; currentSpotCursor <= rightBarOfPair; currentSpotCursor++) {
            aux[currentSpotCursor] = originalArr[currentSpotCursor];
        }
    }

    private static String showInStr(Comparable[] originalArr, int leftBar, int rightBar) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int currentSpot = leftBar; currentSpot <= rightBar; currentSpot++) {
            sb.append(originalArr[currentSpot]).append(", ");
        }
        sb.substring(0, sb.length() - 2);
        sb.append("}");

        return sb.toString();
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
