package com.henry.leetcode_traning_camp.week_03.day06.majority_element_in_arr;

// 验证：可以使用 快速排序中的切分操作 来 找到”元素序列中出现次数最多的元素“(依据题设 其必然存在)
// 原理：只要切分操作中”被排定的位置“ > 元素序列的中间位置，就说明 该排定位置上的元素 就是 ”出现次数最多的元素“
// 切分操作的模板：#1 设置基准元素、小于区边界指针、大于区边界指针；#2 分拣小于区与大于区中的元素；
// #3 交换基准元素与大于区指针指向的元素； #4 返回大于区指针；
public class Solution_majorityElement_via_partition {
    public static void main(String[] args) {
        int[] itemSequence = {1, 1, 1, 2, 2, 2, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5};
        int resultSequence = majorityItemOf(itemSequence);
        System.out.println("数组中出现次数最多(超过一半)的元素是： " + resultSequence);
    }

    private static int majorityItemOf(int[] itemSequence) {
        // 🐖 这是快速排序模板的一个变体 - 因为我们需要比较 排定位置 与 中间位置，所以把中间位置作为参数传入
        return quickSearch(itemSequence, 0, itemSequence.length - 1, itemSequence.length / 2);
    }

    private static int quickSearch(int[] itemSequence, int leftBar, int rightBar, int wantedSpot) {
        // #1 执行切分操作，并得到 ”被排定的位置“
        int arrangedSpot = partition(itemSequence, leftBar, rightBar);

        // #2 如果 被排定的位置 == 预期的位置，说明“被排定的位置”上的元素 就是“多数元素”，则：
        if (arrangedSpot == wantedSpot) {
            // 返回该位置上的元素
            return itemSequence[arrangedSpot];
        }
        // #3 如果被排定的位置 > 想要的位置，说明 被排定的位置 靠后，则：
        return arrangedSpot > wantedSpot
                ? quickSearch(itemSequence, leftBar, arrangedSpot - 1, wantedSpot) // 在左区间再次进行切分操作，以排定一个新位置
                : quickSearch(itemSequence, arrangedSpot + 1, rightBar, wantedSpot); // 否则，在右区间 再次进行切分操作，以排定一个新位置
    }

    /**
     * 找到一个切分元素，并把切分元素放在正确的位置（被排定的位置/被排定的元素）
     * 🐖 这是切分操作的模板
     *
     * @param numSequence 元素序列
     * @param leftBar     区间的左边界
     * @param rightBar    区间的右边界
     * @return 被排定的位置 - 该位置上的元素 = 排序后的结果序列中该位置上的元素
     */
    private static int partition(int[] numSequence, int leftBar, int rightBar) {
        // #1 设定 基准元素 &&
        int pivotItem = numSequence[leftBar];
        // 初始化 小于区的右边界指针、大于区的左边界指针
        int lessZoneBoundary = leftBar, greaterZoneBoundary = rightBar + 1;

        // #2 设置一个循环，在循环中 完成对大于区与小于区中的元素的分拣
        while (true) {
            // #1 把小于区的指针 停在 左侧第一个大于基准元素的元素上
            while (++lessZoneBoundary <= rightBar && numSequence[lessZoneBoundary] < pivotItem) ;
            // #2 把大于区的指针 停在 右侧(往回走)第一个大于基准元素的元素上
            while (--greaterZoneBoundary >= leftBar && numSequence[greaterZoneBoundary] > pivotItem) ;

            // 🐖 如果 小于区边界指针 与 大于区边界指针 相遇，说明 小于区、大于区的元素 都已经分拣完成，则：
            if (lessZoneBoundary >= greaterZoneBoundary) {
                // 跳出循环，不再分拣/交换
                break;
            }

            // #3 交换两个边界指针所指向的元素 来 完成元素的分拣
            int temp = numSequence[greaterZoneBoundary];
            numSequence[greaterZoneBoundary] = numSequence[lessZoneBoundary];
            numSequence[lessZoneBoundary] = temp;
        }

        // #3 大于区、小于区中的元素分拣完成后，把 基准元素 与 大于区边界指针所指向的元素交换；
        numSequence[leftBar] = numSequence[greaterZoneBoundary];
        numSequence[greaterZoneBoundary] = pivotItem;

        // #4 返回交换后的基准元素所在的位置 - 这个位置就已经“被排定了”
        return greaterZoneBoundary;
    }

}
