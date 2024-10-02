package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.smallest_K_item.via_heap;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

// 验证：可以使用”大顶堆“这种数据结构 来 解决”元素序列中最小的K个元素“的问题
// 为了得到 最小的K个元素 - 需要一个数据结构能够不断 把”当前的最大值“移除，也就是 ”大顶堆“ - 使用JDK的优先队列 + 降序的比较规则 实现
public class Solution_getLeastNumbers_via_maxHeap {
    public static void main(String[] args) {
        int[] itemSequence = {3, 2, 1, 10, 2, 6, 8, 5, 15, 22, 0};
        int wantedItemAmount = 5;

        int[] leastKNumbers = getLeastKNumbers(itemSequence, wantedItemAmount);
        print(leastKNumbers); // {0, 1, 2, 2, 3} 更可能是乱序的
    }

    private static void print(int[] leastKNumbers) {
        for (int i = 0; i < leastKNumbers.length; i++) {
            System.out.print(leastKNumbers[i] + ", ");
        }
    }

    private static int[] getLeastKNumbers(int[] itemSequence, int wantedItemAmount) {
        // #0 健壮性代码
        if (wantedItemAmount == 0 || itemSequence.length == 0) {
            return new int[0];
        }

        // #1 创建一个堆对象    手段：使用JDK中提供的PriorityQueue类型
        // 注：我们需要的是”大顶堆“ - JDK提供的PQ默认是”小顶堆“，为了实现”大顶堆“需要手动传入一个 自定义规则的比较器。
        Comparator<Integer> myComparator = (item1, item2) -> item2 - item1; // 降序规则
        Queue<Integer> maxItemHeap = new PriorityQueue<>(myComparator); // 得到一个”大顶堆“ <=> 降序规则的优先队列??

        // #2 遍历数组中的元素，然后依次 ”按需添加到“ maxItemHeap对象中 来 得到”包含最小的K个元素的大顶堆“
        for (int currentItem : itemSequence) {
            // 如果 大顶堆中当前的元素数量 小于 想要的元素数量，说明 大顶堆尚未满员，则：
            if (maxItemHeap.size() < wantedItemAmount) {
                // 把”当前数组元素“添加到堆中   手段：优先队列的offer()方法
                maxItemHeap.offer(currentItem);
            // 否则 说明堆中元素的数量 已经是我们想要的元素的数量，则：
            } else if (currentItem < maxItemHeap.peek()) {
                // 如果 当前元素 小于 堆顶元素，说明 找到了一个 最小K个元素的”新的candidate“，则：
                /* 用它来更新最大堆 👇 */
                // ① 先 获取并移除 大顶堆的堆顶元素 aka 最大元素   手段：poll()方法
                maxItemHeap.poll();
                // ② 再把”当前数组元素“添加到堆对象中  手段：offer()方法 VS. add()方法
                maxItemHeap.offer(currentItem);
            }
        }

        // #3 使用数组 来 收集”包含最小的K个元素的大顶堆“中的所有元素    手段：把堆中元素的值绑定到数组中
        int[] spotToHeapItemSequence = transformItemsInArrFormat(maxItemHeap);
        // #4 然后返回 ”包含最小的K个元素的大顶堆“的数组形式
        return spotToHeapItemSequence;
    }

    private static int[] transformItemsInArrFormat(Queue<Integer> maxItemHeap) {
        int[] spotToHeapItemSequence = new int[maxItemHeap.size()];
        int currentSpot = 0;
        // 特征：堆的底层数据结构 本质上是一个队列（线性数据结构），所以可以直接用”增强for循环“
        for (int currentHeapItem : maxItemHeap) {
            // 逐一绑定
            spotToHeapItemSequence[currentSpot++] = currentHeapItem;
        }
        return spotToHeapItemSequence;
    }
}
