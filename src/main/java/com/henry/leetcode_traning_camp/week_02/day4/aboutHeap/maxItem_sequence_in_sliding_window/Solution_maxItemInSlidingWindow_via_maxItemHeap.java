package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.maxItem_sequence_in_sliding_window;

import java.util.Comparator;
import java.util.PriorityQueue;

// 验证：可以借助 优先队列的 #1 支持添加元素; #2 支持删除指定元素；#3 支持获取最大元素 的特性 来 解决“滑动窗口的最大值”问题
public class Solution_maxItemInSlidingWindow_via_maxItemHeap {
    public static void main(String[] args) {
        int[] itemSequence = {1, 3, -1, -3, 5, 3, 6, 7}; //  和 slidingWindowSize = 3
        int slidingWindowSize = 3;

        int[] maxItemSequence = maxSlidingWindow(itemSequence, slidingWindowSize);
        print(maxItemSequence);
    }

    private static void print(int[] maxItemSequence) {
        for (int currentMaxItem : maxItemSequence) {
            System.out.print(currentMaxItem + "->");
        }
    }

    private static int[] maxSlidingWindow(int[] itemSequence, int slidingWindowSize) {
        // #0 健壮性代码
        if (itemSequence.length == 0 || slidingWindowSize == 0) {
            return new int[0];
        }

        /* 准备需要的变量👇 */
        // 准备 表示结果序列的数组 -> 序列大小 = 滑动窗口的数量 = (元素数量 - 滑动窗口尺寸 + 1)
        int itemAmount = itemSequence.length;
        int slidingWindowAmount = itemAmount - slidingWindowSize + 1;
        int[] slidingWindowNoToItsMaxItemArr = new int[slidingWindowAmount];

        // 准备”优先队列“ - 用于：① 动态存储(添加”当前元素“+移除”窗口开始位置的元素“)”当前滑动窗口“中的元素； ② 获取到 当前滑动窗口中的最大元素；
        // 🐖 默认得到的是小顶堆，为了得到大顶堆 需要传入”降序“的比较器参数 - 使用lambda表达式
        Comparator<Integer> myComparator = (o1, o2) -> (o2 - o1);
        PriorityQueue<Integer> itemPriorityQueue = new PriorityQueue<>(myComparator);

        // 对于 元素序列中的当前位置...
        for (int currentItemSpot = 0; currentItemSpot < itemAmount; currentItemSpot++) {
            // #1 根据需要 删除”滑动窗口的起始元素“
            // 如果当前位置 已经大于 滑动窗口的size，说明 已经需要从优先队列中移除 滑动窗口的首元素了，则：
            int startSpotOfCurrentWindow = currentItemSpot - slidingWindowSize;
            if (startSpotOfCurrentWindow >= 0) {
                // 从优先队列中删除”滑动窗口起始位置的元素“[①] - 手段：remove(<item_on_start_spot>)方法
                int itemOnStartSpotOfCurrentWindow = itemSequence[startSpotOfCurrentWindow];
                itemPriorityQueue.remove(itemOnStartSpotOfCurrentWindow);
            }

            // #2 向 优先队列 中 添加”当前元素“[①] - 手段: offer(<item_on_spot>)方法
            int itemOnCurrentSpot = itemSequence[currentItemSpot];
            itemPriorityQueue.offer(itemOnCurrentSpot);

            // #3 （添加完成队尾元素后）如果 优先队列/大顶堆的大小 == 滑动窗口的大小，说明 大顶堆已经满员了，则：
            if (itemPriorityQueue.size() == slidingWindowSize) {
                // 把 ”最大元素“ 绑定到 其”在结果序列的对应位置“上 👇
                // Ⅰ 从优先队列中获取到(但不移除) 其最大元素(也就是 当前滑动窗口中的最大元素)[②]
                Integer maxItemOfCurrentPQ = itemPriorityQueue.peek();
                // Ⅱ 计算出 在结果序列中，当前最大元素 所需要被添加的位置
                int slidingWindowNo = currentItemSpot - slidingWindowSize + 1;
                slidingWindowNoToItsMaxItemArr[slidingWindowNo] = maxItemOfCurrentPQ; // 这里之所以会使用PQ这种数据类型，是因为使用它能够O(1)时间获取到一组数中的最大值
            }
        }

        return slidingWindowNoToItsMaxItemArr;
    }
}
