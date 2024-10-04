package com.henry.leetcode_traning_camp.week_02.day5.aboutHeap.priority_queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

// 验证：优先队列 只能保证 优先级最小/最大的元素 出现在队首，对于其他的元素...
// 在存储时，它只会保证父子结点间的数值关系,而不会保证 子结点按照优先级顺序排定;
// 原理：堆存储时，元素的不确定性
// 手段：使用for循环 打印出优先队列中的所有元素 - 得到的结果序列 并不是 按照优先级顺序排列的序列
public class priorityQueueSortByMapValueDemo {
    public static void main(String[] args) {
        Map<String, Integer> nameToVotesMap = new HashMap<>();
        constructMap(nameToVotesMap);
        printMapEntry(nameToVotesMap); // Map中 for循环打印元素的顺序??

        PriorityQueue<String> winnerNames = new PriorityQueue<String>(Comparator.comparing(nameToVotesMap::get));
        constructPriorityQueue(nameToVotesMap, winnerNames);
        // PQ中 for循环打印元素的顺序 - 不一定会是 按照优先级顺序；
        // 因为堆只是保证 结点与其子结点之间的大小关系，但不会保证 子结点一定是 当前(逻辑上)最小的元素
        printPQItem(winnerNames);

        // 按照优先级的顺序 打印出 优先队列中的元素 #1
        printPQItemInPriorityOrder(nameToVotesMap, winnerNames);
        // 按照优先级的顺序 打印出 优先队列中的元素 #2
        keepPolling(winnerNames); // poll的总是 PQ中的最小元素(逻辑上最小)
    }

    private static void printPQItemInPriorityOrder(Map<String, Integer> nameToVotesMap, PriorityQueue<String> winnerNames) {
        List<String> winnerNameList = new ArrayList<>(winnerNames);
        Collections.sort(winnerNameList, Comparator.comparing(nameToVotesMap::get));
        System.out.println("printed items in priority order👇");
        for (String winnerName : winnerNameList) {
            System.out.print(winnerName + " -> ");
        }
        System.out.println();
    }

    private static void printMapEntry(Map<String, Integer> nameToVotesMap) {
        System.out.println("printed map Entries 👇");
        for (Map.Entry<String, Integer> entry : nameToVotesMap.entrySet()) {
            System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
        }
        System.out.println();
    }

    private static void constructPriorityQueue(Map<String, Integer> nameToVotesMap, PriorityQueue<String> winnerNames) {
        for (String currentName : nameToVotesMap.keySet()) {
            winnerNames.add(currentName);
        }
    }

    private static void keepPolling(PriorityQueue<String> winnerNames) {
        System.out.println("printed polled item in PQ👇");
        while (!winnerNames.isEmpty()) {
            String poll_item = winnerNames.poll();
            System.out.print(poll_item + " -> ");
        }
        System.out.println();
    }

    private static void printPQItem(PriorityQueue<String> winnerNames) {
        System.out.println("printed PQ Items👇");
        for (String winnerName : winnerNames) {
            System.out.print(winnerName + " -> ");
        }
        System.out.println();
    }

    private static void constructMap(Map<String, Integer> nameToVotesMap) {
        nameToVotesMap.put("Ada", 25); // #5
        nameToVotesMap.put("Bob", 21); // #4
        nameToVotesMap.put("Cris", 18); // #3
        nameToVotesMap.put("Doug", 28); // #6
        nameToVotesMap.put("Eric", 13); // #1
        nameToVotesMap.put("Frank", 15); // #2
        nameToVotesMap.put("Greg", 33); // #7
    }
}
