package com.henry.sort_chapter_02.advanced_algorithm.via_devide_and_conquer.merge_sort_algorithm.merge_top_to_down_01;

/*
任务描述：得到 “完整排序的数组” = 把 数组中的每一个元素 都 “排定到其正确的位置”

递归 可行性分析：~

术语定义：
    有序的子数组：子数组中的相邻元素 连续升序 或 降序排列；
    归并操作：能够 把 两个有序的数组，归并得到 一个更大的有序数组。

算法描述：

排序算法步骤：
    #1 对 左半区间 排序；
    #2 对 右半区间 排序；
    #3 执行 左半区间 与 右半区间 的归并操作，得到 “完全排序的数组”

归并算法步骤：
    #1 拷贝 原始数组的指定区间 到 辅助数组 中；
    #2 为 辅助数组的左右区间，添加 游标指针（用于元素间的大小比较）；
    #3 使用 左右区间中的较小元素 来 填充回 原始数组。

方法签名设计：~

执行过程的特征：先 在 左半区间 逐步归并（左-右-完整区间 区间逐渐扩大）出 一个完整的 有序子数组，
再 在右半区间 逐步归并（左-右-完整区间 区间逐渐扩大）出 一个完整的有序子数组；
最终，对 有序的左右子数组，一次性归并 得到“完全排序的数组”
 */
// 自顶向下的 归并排序 算法步骤：#1 使左半区间有序； #2 使右半区间有序； #3 使用归并操作 归并 两个有序的子数组，得到 完全排序的数组
public class MergeFromTopToDownTemplate {
    // 成员变量 - 好处：可以在 当前类的所有方法 中 使用它
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        // 🐖 初始化 辅助数组的大小
        int itemAmount = a.length;
        aux = new Comparable[itemAmount];

        // 对 数组的指定闭区间 进行排序 - 🐖 用例处 指定 全部区间，用于 对 整个数组 进行排序
        sortGivenRangeOf(a, 0, itemAmount - 1);
    }

    // 排序 数组的 指定闭区间 a[leftBar, rightBar]
    private static void sortGivenRangeOf(Comparable[] originalArr, int leftBar, int rightBar) {
        // 递归终结的条件：区间变窄为0 aka 数组 已经有序
        if (leftBar >= rightBar) return;

        // 计算 当前区间的中间位置
        int middleSpot = leftBar + (rightBar - leftBar) / 2;

        // 使 左半区间 有序
        sortGivenRangeOf(originalArr, leftBar, middleSpot);
        System.out.println("~~~ 左半部分" + showInStr(originalArr, leftBar, middleSpot) + "排序完成 ~~~");
        // 使 右半区间 有序
        sortGivenRangeOf(originalArr, middleSpot + 1, rightBar);
        System.out.println("+++ 右半部分" + showInStr(originalArr, middleSpot + 1, rightBar) + "排序完成 +++");

        // 有了两个 有序的子数组 后，使用 归并操作 得到一个 元素完全有序的数组
        mergeSortedRange(originalArr, leftBar, middleSpot, rightBar);
        System.out.println("--- 元素序列" + showInStr(originalArr, leftBar, rightBar) + "已经完全有序！ ---");
        System.out.println();
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

    // 归并 a[leftBar, middle] 与 a[middle+1, rightBar] - 特征：两个子区间 都已经是 有序的 了
    private static void mergeSortedRange(Comparable[] originalArr,
                                         int leftBar,
                                         int middleSpot,
                                         int rightBar) {
        // #1 拷贝 区间[leftBar, rightBar](🐖 闭区间) 之间的元素 到 aux
        copyItemsToAux(originalArr, leftBar, rightBar);

        // #2 找到 辅助数组 左右区间中的 较小元素，写回到 原始数组 来 得到 完全排序的数组
        writeCorrectItemBack(originalArr, leftBar, middleSpot, rightBar);
    }

    private static void writeCorrectItemBack(Comparable[] originalArr,
                                             int leftBar,
                                             int middleSpot,
                                             int rightBar) {
        // #1 准备 序列1（左区间）待比较元素的指针 与 序列2（右区间）待比较元素的指针 - 用于比较元素，得到“正确的元素”
        int cursorOfItemToCompareInSeq1 = leftBar;
        int cursorOfItemToCompareInSeq2 = middleSpot + 1;

        // #2 对于 原始数组中的“当前待排定的位置”...
        for (int currentSpotToArrange = leftBar; currentSpotToArrange <= rightBar; currentSpotToArrange++) { // for body无法抽取成一个方法，因为它做了不止一件事：排定元素 + 移动指针
            System.out.println("@@@ 当前待排定的位置是：" + currentSpotToArrange + " @@@");
            /* 比较 辅助数组 中，左右指针 所指向的元素。然后 把 “较小的元素” 绑定到 原始数组“待排定的位置” 上 */
            if (cursorOfItemToCompareInSeq1 > middleSpot) { // 左半部分元素 用尽
                System.out.println("### 当前 左半部分元素耗尽，依次拷贝 右半部分元素 到 当前待排定位置" + currentSpotToArrange + " ###");
                originalArr[currentSpotToArrange] = aux[cursorOfItemToCompareInSeq2++];
            } else if (cursorOfItemToCompareInSeq2 > rightBar) { // 右半部分元素 用尽
                System.out.println("$$$ 当前 右半部分元素耗尽，依次拷贝 左半部分元素 到 当前待排定位置" + currentSpotToArrange + " $$$");
                originalArr[currentSpotToArrange] = aux[cursorOfItemToCompareInSeq1++];
            } else if (less(aux[cursorOfItemToCompareInSeq1], aux[cursorOfItemToCompareInSeq2])) { // 比较 待比较的元素,
                // ① 拷贝 其较小值 到原数组中；② 并 移动指针 到下一位置
                System.out.println("%%% 把 两个待比较元素(" + aux[cursorOfItemToCompareInSeq1] + ", " + aux[cursorOfItemToCompareInSeq2] + ")中的 较小元素"
                        + aux[cursorOfItemToCompareInSeq1] + " 填充到 当前待排定位置 " + currentSpotToArrange + " 上 %%%");
                originalArr[currentSpotToArrange] = aux[cursorOfItemToCompareInSeq1++];
            } else {
                System.out.println("^^^ 把 两个待比较元素(" + aux[cursorOfItemToCompareInSeq1] + ", " + aux[cursorOfItemToCompareInSeq2] + ")中的较小元素 "
                        + aux[cursorOfItemToCompareInSeq2] + " 填充到 当前待排定位置 " + currentSpotToArrange + " 上 ^^^");
                originalArr[currentSpotToArrange] = aux[cursorOfItemToCompareInSeq2++];
            }
        }
    }

    private static void copyItemsToAux(Comparable[] originalArr, int leftBar, int rightBar) {
        System.out.println("!!! 拷贝 原始序列闭区间[" + leftBar + "," + rightBar + "]中的元素 到 辅助数组中 !!!");
        for (int currentSpot = leftBar; currentSpot <= rightBar; currentSpot++) {
            aux[currentSpot] = originalArr[currentSpot];
        }
    }

    private static boolean less(Comparable itemV, Comparable itemW) {
        return itemV.compareTo(itemW) < 0;
    }

    private static void show(Comparable[] a) {
        int N = a.length;
        for (int currentSpot = 0; currentSpot < N; currentSpot++) {
            System.out.print(a[currentSpot] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String[] a = {"M", "E", "R", "G", "E", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
//        String[] a = StdIn.readAllStrings();
        sort(a);

        show(a);
    }
}
