package com.henry.leetcode_traning_camp.week_02.day5.aboutHeap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// 验证：可以借助 优先队列的”可以指定元素的排序规则/优先级“ + ”可以高效获取并移除 优先级最小元素“的特性 + map的记录映射关系的特性 来
// 解决 ”序列中出现频率最高的K个元素“的问题（比如 得票最高的前5位成员）
public class Solution_mostFrequentKItem_via_minPQ {
    public static void main(String[] args) {
        int[] itemSequence = {1, 1, 2, 2, 2, 2, 4, 4, 9, 2, 5, 3, 3, 3};
        int wantedItemAmount = 3;

        int[] resultItemSequence = topFrequentKItems(itemSequence, wantedItemAmount);
        print(resultItemSequence);
    }

    private static void print(int[] resultItemSequence) {
        for (int currentItem : resultItemSequence) {
            System.out.print(currentItem + " -> ");
        }
        System.out.println();
    }

    private static int[] topFrequentKItems(int[] itemSequence, int wantedItemAmount) {
        if (itemSequence.length == 0 || wantedItemAmount == 0) {
            return new int[0];
        }

        Map<Integer, Integer> itemToItsFrequencyMap = calculateItemsFrequency(itemSequence);
        PriorityQueue<Integer> itemPQ = constructWantedPQ(itemToItsFrequencyMap, wantedItemAmount);
        return getTopKArrFrom(itemPQ, wantedItemAmount);
    }

    private static PriorityQueue<Integer> constructWantedPQ(Map<Integer, Integer> itemToItsFrequencyMap, int wantedItemAmount) {
        // 创建一个优先队列，其中的元素 按照“map中key映射的value值大小(元素在序列中的出现频率)”进行排序
        // 定义比较器的手段 - 调用 Comparator静态类的comparingInt(<comparator>)方法
        // 作用：接受一个 能够”从类型T中抽取一个int的排序键“的方法，并 返回一个比较器（此比较器使用 排序键进行比较）；
        // 特征：通常情况下，PriorityQueue中元素是“按照自然顺序排列”的，或者是按照“某个特定的比较器”排列的。
        // 说明：这里使用 ”方法引用“的手段 来 指定 使用”map的key所映射的value(元素出现次数)??“ 来 作为”比较器的排序键“（元素的排序依据）
        PriorityQueue<Integer> itemPQ = new PriorityQueue<>(Comparator.comparingInt(itemToItsFrequencyMap::get));

        // 使用优先队列 来 记录下 词频最高的K个元素
        // 这里需要处理的是元素本身 而不是统计次数的数值
        for (int currentItem : itemToItsFrequencyMap.keySet()) { // 键集合 aka 元素的集合
            // #1 向优先队列中“添加”当前元素 - 随着元素被不断添加进优先队列，PQ会按照“比较器指定的规则” 来 对这些元素进行排序(频率由低到高)
            itemPQ.add(currentItem);

            // #2 如果“优先队列中的元素数量” 超过 “想要的元素数量”，说明 当前优先队列中包含过量的元素，则：
            if (itemPQ.size() > wantedItemAmount) {
                // 获取并高效移除 当前优先队列中”频率最低的元素“ 来 使得优先队列中 只维护“频率最高的K个元素”
                /*
                    手段：pq.poll();
                    作用:获取队列中的第一个元素(队列中的元素 已经按照比较器规则进行排序)，然后将其从队列中删除。
                    特征：这个特殊方法的实现非常高效，在时间复杂度上仅仅为O(log n)；
                 */
                itemPQ.poll();
            }
        }

        return itemPQ;
    }

    // 获取到 元素序列中，元素 -> 元素出现次数的map对象
    private static Map<Integer, Integer> calculateItemsFrequency(int[] itemSequence) {
        Map<Integer, Integer> itemToItsFrequencyMap = new HashMap<>();

        for (int currentItemSpot = 0; currentItemSpot < itemSequence.length; currentItemSpot++) {
            // 当前位置上的元素
            int currentItem = itemSequence[currentItemSpot];
            // 获取到 此元素在map容器中所映射到的 出现次数 - 如果在map中不存在，则 返回0
            Integer itemCurrentFrequency = itemToItsFrequencyMap.getOrDefault(currentItem, 0);
            // 元素 -> 出现次数 的映射关系
            itemToItsFrequencyMap.put(currentItem, itemCurrentFrequency + 1);
        }

        return itemToItsFrequencyMap;
    }

    // 从优先队列中，获取到 出现频率最高的K个元素
    private static int[] getTopKArrFrom(PriorityQueue<Integer> itemPQ, int wantedItemAmount) {
        int[] topKItemArr = new int[wantedItemAmount];

        // 使用优先队列的元素 来 填充数组的每一个位置
        int currentSpot = 0;
        for (int currentItem : itemPQ) {
            topKItemArr[currentSpot++] = currentItem;
        }
        // 返回 元素序列/数组
        return topKItemArr;
    }
}
