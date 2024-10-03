package com.henry.leetcode_traning_camp.week_02.day5.aboutHeap.priority_queue;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// todo 优先队列中的元素为什么没有像预想地 按照votes从小到大排序呢？
public class priorityQueueSortByMapValueDemo {
    public static void main(String[] args) {
        Map<String, Integer> nameToVotesMap = new HashMap<>();
        constructMap(nameToVotesMap);

        PriorityQueue<String> winnerNames = new PriorityQueue<String>(Comparator.comparing(nameToVotesMap::get));

        for (String currentName : nameToVotesMap.keySet()) {
            winnerNames.add(currentName);
        }

        print(winnerNames);
    }

    private static void print(PriorityQueue<String> winnerNames) {
        for (String winnerName : winnerNames) {
            System.out.println(winnerName + " -> ");
        }
        System.out.println();
    }

    private static void constructMap(Map<String, Integer> nameToVotesMap) {
        nameToVotesMap.put("Ada", 25);
        nameToVotesMap.put("Bob", 21);
        nameToVotesMap.put("Cris", 18);
        nameToVotesMap.put("Doug", 28);
        nameToVotesMap.put("Eric", 13); // #1
        nameToVotesMap.put("Frank", 15);
        nameToVotesMap.put("Greg", 33);
    }
}
