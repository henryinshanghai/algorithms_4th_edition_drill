package com.henry.leetcode_traning_camp.week_03.day04.combination;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// 验证：回溯算法的过程 会产生很多没有用的分支，对于这样的分支，可以通过代码来把它们剪枝，从而提高代码运行效率；
// 剪枝主要针对for循环的执行次数 - 我们想要执行“有效的循环”。对于组合问题，有效的循环的条件是：剩余可用的元素数量 + 1 >= 构造组合还需要的元素数量
// 🐖 +1是因为currentItem是从1开始的
public class Solution_findGivenSizeCombinations_via_backtrack_cutBranch {
    public static void main(String[] args) {
        int availableMaxItem = 4;
        int expectedCombinationSize = 2;

        List<List<Integer>> allLegitCombinations = combine(availableMaxItem, expectedCombinationSize);

        System.out.println(allLegitCombinations);
    }

    private static List<List<Integer>> combine(int availableMaxItem, int expectedCombinationSize) {
        if (expectedCombinationSize <= 0 || expectedCombinationSize > availableMaxItem) {
            return legitCombinationList;
        }

        dfs(availableMaxItem, expectedCombinationSize, 1);

        return legitCombinationList;
    }

    private static Deque<Integer> currentGeneratedCombination = new LinkedList<>();
    private static List<List<Integer>> legitCombinationList = new ArrayList<>();

    private static void dfs(int availableMaxItem, int expectedCombinationSize, int startAnchorItem) {
        if (currentGeneratedCombination.size() == expectedCombinationSize) {
            List<Integer> legitCombination = new ArrayList<>(currentGeneratedCombination);
            legitCombinationList.add(legitCombination);
            return;
        }

        // 有效的遍历条件：剩余可用的元素数量 >= 组合还需要的元素数量
        int requiredItemAmount = expectedCombinationSize - currentGeneratedCombination.size();
        // 剩余可用的元素数量 = availableMaxItem - currentItem;
        // 示例：最开始时 availableMaxItem=4, currentItem=1, requiredItemAmount=4。所以需要 在不等式右边+1
        for (int currentItem = startAnchorItem; currentItem <= availableMaxItem - requiredItemAmount + 1; currentItem++) {
            currentGeneratedCombination.addLast(currentItem);
            dfs(availableMaxItem, expectedCombinationSize, currentItem + 1);
            currentGeneratedCombination.removeLast();
        }
    }
}