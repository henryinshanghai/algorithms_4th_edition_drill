package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.smallest_K_item.via_partition;

import java.util.Arrays;

// 验证：可以使用”找到合适的切分位置 + 从切分得到的结果序列中拷贝片段“的方式 来 解决“元素序列中最小的K个元素”的问题
// 切分操作的相关概念：小于区指针、大于区指针、分拣、交换、排定元素、被排定的位置
public class Solution_getLeastKNumbers_via_partition {
    public static void main(String[] args) {
        int[] itemSequence = {3, 2, 1, 6, 8, 10, 6, 9, 3, 5, 11};
        int wantedItemAmount = 5;

        int[] leastKNumbers = getLeastKNumbers(itemSequence, wantedItemAmount);
        print(leastKNumbers);
    }

    private static void print(int[] leastKNumbers) {
        for (int currentSpot = 0; currentSpot < leastKNumbers.length; currentSpot++) {
            System.out.print(leastKNumbers[currentSpot] + ", ");
        }
    }

    /**
     * 获取到指定数组中最小的k个数字
     * 特征：获取到最小的k个数字并不需要对整个数组进行排序，也不要求这k个数字就是有序的；
     * 手段：利用快速排序的SOP
     *
     * @param originalItemSequence
     * @param wantedItemAmount
     * @return
     */
    private static int[] getLeastKNumbers(int[] originalItemSequence, int wantedItemAmount) {
        if (wantedItemAmount == 0 || originalItemSequence.length == 0) {
            return new int[0];
        }

        // 最后一个参数表示 我们要找的是 “下标为k-1的数”
        // 特征：相比于 快排模板，多了一个参数k
        return truncateLeastKItemViaArrangedSpot(originalItemSequence, 0, originalItemSequence.length - 1, wantedItemAmount - 1); // 对数组进行快速排序的方法
    }

    /**
     * 对指定数组进行快速排序
     * 手段：1 找到切分元素，并把切分元素放在正确的位置； 2 在子数组上递归调用函数本身对子数组进行排序
     *
     * @param originalItemSequence 待排序的数组
     * @param leftBar              本级调用的左边界
     * @param rightBar             本级调用的右边界
     * @param wantedItemAmount     预期切分元素的位置
     * @return
     */
    private static int[] truncateLeastKItemViaArrangedSpot(int[] originalItemSequence, int leftBar, int rightBar, int wantedItemAmount) {
        // #1 对“原始的元素序列” 执行一次“切分操作”，并得到 切分后“被排定的位置”
        int arrangedSpot = partition(originalItemSequence, leftBar, rightBar);

        // #2 如果“被排定的位置” == “想要获取到的元素数量”，说明此时 数组中的前k个元素 就已经是"最小的k个值(无序状态)"
        if (arrangedSpot == wantedItemAmount) {
            // 则：把“数组中的前k个元素”拷贝出来 返回即可
            return Arrays.copyOf(originalItemSequence, arrangedSpot + 1); // 这里需要的参数是长度，所以为(索引 + 1)
        }

        // #3 否则，根据 已排定位置 与 想要的元素数量 之间的大小关系 来 决定继续切分左段还是右段 以得到“合适的切分位置”
        // 如果 已排定位置 大于 想要的元素数量，说明 预期的排定位置 在左区间中的某个位置。则：继续对 数组的左区间 进行快排操作 来 得到下一个排定位置
        // 如果 已排定位置 不大于 想要的元素数量，说明 预期的排定位置 在右区间中的某个位置。则：继续对 数组的右区间 进行快排操作 来 得到下一个排定位置
        return arrangedSpot > wantedItemAmount
                ? truncateLeastKItemViaArrangedSpot(originalItemSequence, leftBar, arrangedSpot - 1, wantedItemAmount)
                : truncateLeastKItemViaArrangedSpot(originalItemSequence, arrangedSpot + 1, rightBar, wantedItemAmount);
    }

    /**
     * 为特定数组选择一个切分元素，调整数组，使得：
     * 1 切分元素左边的元素序列都小于切分元素 & 切分元素右边的元素序列都大于切分元素
     * 并返回切分元素最终的位置
     *
     * @param itemSequence
     * @param leftBar
     * @param rightBar
     * @return
     */
    private static int partition(int[] itemSequence, int leftBar, int rightBar) {
        // #1 随机选择一个元素作为“切分元素”
        int pivotItem = itemSequence[leftBar];

        // #2 准备两个指针 小于区的指针lessZoneBoundary、大于区的指针greaterZoneBoundary，初始化分别指向 数组的头元素与尾元素
        int lessZoneBoundary = leftBar,
            greaterZoneBoundary = rightBar + 1;

        // #3 准备一个循环 来 完成对除了”基准元素“以外的其他元素的”分拣“ - 小于者丢到小于区、大于者丢到大于区
        while (true) {
            // Ⅰ 驱动 小于区的边界指针 在“待交换的位置“(从左边开始，第一个比切分元素更大的元素)停下
            while (++lessZoneBoundary <= rightBar && itemSequence[lessZoneBoundary] < pivotItem) ;
            // Ⅱ 驱动 大于区的边界指针 在“待交换的位置”(从左边开始，第一个比切分元素更大的元素)停下
            while (--greaterZoneBoundary >= leftBar && itemSequence[greaterZoneBoundary] > pivotItem) ;

            // 特殊情况：当 小于区的右边界指针 与 大于区的左边界的指针 相遇时，说明所有元素都已经“分拣完成”，则：
            if (lessZoneBoundary >= greaterZoneBoundary) {
                // 不需要执行任何的交换操作 直接跳出循环
                break;
            }

            // Ⅲ （驱动指针停在正确的位置上后）交换两个指针所指向的元素 来 实现“分拣”动作
            exch(itemSequence, lessZoneBoundary, greaterZoneBoundary);
        }

        // #4 交换 leftBar所指向的位置 与 greaterZoneBoundary指针所指向的位置 上的元素 来 ”排定“基准元素
        // Ⅰ 使用 greaterZoneBoundary指针指向的元素 来 ”覆盖“/”更新“leftBar所指向的位置上的元素
        itemSequence[leftBar] = itemSequence[greaterZoneBoundary];
        // Ⅱ 使用 pivotItem 来 ”覆盖“/"更新" greaterZoneBoundary指针所指向的位置上的元素
        itemSequence[greaterZoneBoundary] = pivotItem;

        // #5 返回”元素被排定的位置“
        return greaterZoneBoundary;
    }

    // 交换 指定的元素序列中的指定的两个位置上的元素
    private static void exch(int[] itemSequence, int lessZoneBoundary, int greaterZoneBoundary) {
        int temp = itemSequence[greaterZoneBoundary];
        itemSequence[greaterZoneBoundary] = itemSequence[lessZoneBoundary];
        itemSequence[lessZoneBoundary] = temp;
    }
}
