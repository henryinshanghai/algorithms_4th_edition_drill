package com.henry.sort_chapter_02.advanced_algorithm.via_devide_and_conquer.quick_sort_algorithm.improve_quick_sort_02;

import edu.princeton.cs.algs4.StdRandom;

import static com.henry.sort_chapter_02.advanced_algorithm.via_devide_and_conquer.quick_sort_algorithm.basic_quick_sort_01.QuickSortTemplate.showInStr;

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
// 三向快速排序的步骤：#1 排定“与基准元素相同的”所有元素(使用两个边界指针+一个当前元素指针); #2 排序小于基准元素的元素区域； #3 排序大于基准元素的元素区域；
public class QuickSort3WayTemplate {

    public static void sort(Comparable[] originalArr) {
        System.out.println("=== 原始的元素序列为：" + showInStr(originalArr, 0, originalArr.length - 1) + " ===");
        StdRandom.shuffle(originalArr);
        System.out.println("--- 打乱后的元素序列为：" + showInStr(originalArr, 0, originalArr.length - 1) + " ---");
        sortGivenRangeOf(originalArr, 0, originalArr.length - 1);
    }

    // 🐖 注释最好 分别打印（while循环中的注释 && 主函数步骤中的注释），太多的细节 会模糊主线
    private static void sortGivenRangeOf(Comparable[] originalArr, int leftBar, int rightBar) {
        if (rightBar <= leftBar) return;

        // #1 准备3个指针（两个边界指针、一个指向当前元素的游标指针）
        // 小于区 的右边界
        int lessZoneRightBar = leftBar;
        // 待 与pivot比较 的元素的 指针（从左往右前进）
        int cursorOfItemToCompare = leftBar + 1;
        // 大于区 的左边界
        int greaterZoneLeftBar = rightBar;

        // #2 准备 基准元素
        Comparable pivotItem = originalArr[leftBar];

        // #3 排定 “与基准元素相等的” 所有元素 a[lessZoneRightBar..greaterZoneLeftBar]
        // 手段：比较 当前元素 与 基准元素，根据 比较结果 来 移动 各个边界的指针 与 当前元素的指针
        while (cursorOfItemToCompare <= greaterZoneLeftBar) { // #3-③ 当currentItemCursor = greaterZone时，所有元素就已经都排定结束
            // #3-① 比较 “当前待比较元素” 与 基准元素,得到 比较结果（整数）
            Comparable itemToCompare = originalArr[cursorOfItemToCompare];
            int compareResult = itemToCompare.compareTo(pivotItem);
            System.out.println("~~~ 比较 待比较元素的指针" + cursorOfItemToCompare + " 所指向的元素 " + itemToCompare + " 与 基准元素 " + pivotItem + " ~~~");

            // #3-② 根据 比较结果，交换 指针指向的元素 & 移动 各个边界的指针 - a[lessZone, currentItem]的区间中 都是 值等于pivot的 元素
            if (compareResult < 0) { // 如果 “当前元素” 更小，说明 需要 把它交换到 “小于区的右边界”，
                // 则：① 进行交换；② 交换后，把 小于区的右边界指针 向后移动一个位置
                // 🐖 由于这里 交换得到的 总是 与pivot相等的元素，所以 “当前待比较元素”的指针 也 向后移动一个位置

                System.out.println("!!! 当前的待比较元素" + itemToCompare + " 比起 基准元素" + pivotItem + " 更小，因此需要 执行 ① + ② !!!");
                System.out.println("① 对 当前元素序列 " + showInStr(originalArr, leftBar, rightBar) + "，交换 待比较元素指针" + cursorOfItemToCompare + " 所指向的元素" + itemToCompare +
                        " 与 小于区右边界指针" + lessZoneRightBar + " 所指向的元素" + originalArr[lessZoneRightBar]);
                System.out.println("② 按需移动指针 - 交换前 待比较元素的指针位置是 " + cursorOfItemToCompare + ", 小于区右边界指针的位置是：" + lessZoneRightBar);
                exch(originalArr, lessZoneRightBar++, cursorOfItemToCompare++);
                System.out.println("交换后的元素序列为：" + showInStr(originalArr, leftBar, rightBar));
                System.out.println("按需移动指针 - 交换后 待比较元素的指针位置是 " + cursorOfItemToCompare + ", 小于区右边界指针的位置是" + lessZoneRightBar);
                System.out.println();
            } else if (compareResult > 0) { // 如果“当前元素”更大，说明需要把它交换到“大于区的左边界”，
                // 则：① 进行交换；② 交换后，把 大于区的左边界 向前移动一个位置
                // 🐖 由于 无法确定 交换过来的元素的大小，所以 保持 “当前待比较元素”的指针 不变 以继续 对其进行比较
                System.out.println("@@@ 当前的待比较元素" + itemToCompare + " 比起 基准元素" + pivotItem + " 更大，因此需要 执行 ① + ② @@@");
                System.out.println("① 对 当前元素序列 " + showInStr(originalArr, leftBar, rightBar) + "，交换 待比较元素指针" + cursorOfItemToCompare + " 所指向的元素" + itemToCompare +
                        " 与 大于区左边界指针" + greaterZoneLeftBar + " 所指向的元素 " + originalArr[greaterZoneLeftBar]);
                System.out.println("② 按需移动指针 - 交换前 待比较元素的指针位置是 " + cursorOfItemToCompare + ", 大于区左边界指针位置是 " + greaterZoneLeftBar);
                exch(originalArr, cursorOfItemToCompare, greaterZoneLeftBar--);
                System.out.println("交换后的元素序列为：" + showInStr(originalArr, leftBar, rightBar));
                System.out.println("按需移动指针 - 交换后 待比较元素的指针位置是 " + cursorOfItemToCompare + ", 大于区左边界指针位置是：" + greaterZoneLeftBar);
                System.out.println();
            } else { // 如果 “当前元素” 与 “基准元素” 相等，说明它 属于“等于区”，
                // 则：保持 它，并 把 “待比较元素”的指针 向后移动 一个位置
                System.out.println("### 当前的待比较元素" + itemToCompare + " 与 基准元素" + pivotItem + " 相等，因此需要 移动 待比较元素的指针 来 扩展 等于区 ###");
                System.out.println("当前 待比较元素指针的位置 是：" + cursorOfItemToCompare);
                cursorOfItemToCompare++;
                System.out.println("移动后 待比较元素指针的位置 是：" + cursorOfItemToCompare);
                System.out.println();
            }
        } // 循环结束 后，所有 “等于区”中的元素 就都已经 被排定完成了
        System.out.println("$$$ 所有 与 基准元素" + pivotItem + "相等的元素都 已经被排定到了 区间[" + lessZoneRightBar + ", " + greaterZoneLeftBar + "]中了，所有其他元素也都 被正确地分拣到 其对应区域，" +
                "当前元素序列为：" + showInStr(originalArr, leftBar, rightBar) + " $$$");


        // 排序 “小于区”中的元素
        System.out.println("%%% 对区间[" + (leftBar) + ", " + (lessZoneRightBar - 1) + "]中的元素"
                + showInStr(originalArr, leftBar, lessZoneRightBar - 1) + " 开始排序 %%%");
        sortGivenRangeOf(originalArr, leftBar, lessZoneRightBar - 1);
        System.out.println("%%% 对区间[" + (leftBar) + ", " + (lessZoneRightBar - 1) + "]中的元素"
                + showInStr(originalArr, leftBar, lessZoneRightBar - 1) + " 排序完成 %%%");

        // 排序 “大于区”中的元素
        System.out.println("^^^ 对区间[" + (greaterZoneLeftBar + 1) + ", " + rightBar + "]中的元素"
                + showInStr(originalArr, greaterZoneLeftBar + 1, rightBar) + " 开始排序 ^^^");
        sortGivenRangeOf(originalArr, greaterZoneLeftBar + 1, rightBar);
        System.out.println("^^^ 对区间[" + (greaterZoneLeftBar + 1) + ", " + rightBar + "]中的元素"
                + showInStr(originalArr, greaterZoneLeftBar + 1, rightBar) + " 完成排序 ^^^");
        System.out.println();

    }

    private static void exch(Comparable[] a, int spotI, int spotJ) {
        Comparable temp = a[spotI];
        a[spotI] = a[spotJ];
        a[spotJ] = temp;
    }

    public static void printItems(Comparable[] a) {
        int N = a.length;

        for (int currentSpot = 0; currentSpot < N; currentSpot++) {
            System.out.print(a[currentSpot] + " ");
        }
    }

    public static void main(String[] args) {
//        String[] a = new String[]{"R", "B", "W", "W", "R", "W", "B", "R", "R", "W", "B", "R"};
        String[] a = new String[]{"H", "E", "N", "R", "Y", "A", "N", "D", "A", "L", "I", "C", "I", "A", "L", "I", "V", "E", "I", "N", "S", "H"};

        sort(a);

        printItems(a);
    }
}
