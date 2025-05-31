package com.henry.leetcode_traning_camp.week_03.day04.combination;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// 验证：对于“所有可能构成的组合”的问题，可以使用  回溯算法 来 在选择当前元素后，再撤销“对当前元素的选择”。最终得到完整的决策树 aka 所有可能的组合
// 🐖 #1 回溯算法对应的问题模型是一个 “多叉树”，因此会需要 在循环中使用递归(也就是多次调用递归)；
// #2 既然回溯本身由递归实现，那么就必然满足：子问题的解 能够帮助解决原始问题[这个是递归的性质]。因此回溯所能解决的问题 也会有自己的“子问题”
// 子问题是如何帮助解决原始问题的呢？答：对于拼凑类型的问题，子问题的解 就是原始问题的解的一部分！
public class Solution_findGivenSizeCombinations_via_backtrack {
    public static void main(String[] args) {
        int availableMaxItem = 4; // 原始集合的大小
        int combinationItemAmount = 2; // 满足条件的组合的大小

        // 获取到 所有的组合的列表
        List<List<Integer>> combinationList = getCombinationListFrom(availableMaxItem, combinationItemAmount);
        // 逐个打印 所有组合
        print(combinationList);
    }

    private static void print(List<List<Integer>> combinationList) {
        for (int currentCombinationIndex = 0; currentCombinationIndex < combinationList.size(); currentCombinationIndex++) {
            // 获取到 当前组合
            List<Integer> currentCombination = combinationList.get(currentCombinationIndex);
            // 打印 当前组合
            printSingle(currentCombination);
            System.out.println();
        }
    }

    private static void printSingle(List<Integer> currentCombination) {
        for (int currentItemIndex = 0; currentItemIndex < currentCombination.size(); currentItemIndex++) {
            // 获取到当前元素
            Integer currentItem = currentCombination.get(currentItemIndex);
            // 打印当前元素
            System.out.print(currentItem + "->");
        }
    }

    // 全局变量
    // 准备一个元素为列表的列表  用于封装最终结果(组合的列表)进行返回
    private static List<List<Integer>> combinationList = new ArrayList<>();
    // 准备一个列表   用于作为当前构造出的合法组合
    private static Deque<Integer> currentGeneratedCombination = new LinkedList<>();

    private static List<List<Integer>> getCombinationListFrom(int availableMaxItem, int expectedCombinationsItemAmount) {
        // #0 健壮性代码 对传入的参数进行判断
        if (expectedCombinationsItemAmount <= 0 || expectedCombinationsItemAmount > availableMaxItem) {
            return combinationList;
        }

        // #1 利用回溯算法 来 得到“所有可能的组合的集合”
        constructGivenSizeCombinationFrom(availableMaxItem, expectedCombinationsItemAmount, 1);

        // #2 返回 该组合的集合
        return combinationList;
    }

    // #0 递归方法作用：在包含有N个自然数的序列中，由指定的位置开始，挑选出K个元素构成“大小为K的组合”；
    private static void constructGivenSizeCombinationFrom(int availableMaxItem, int expectedCombinationItemAmount,
                                                          int dynamicStartAnchorItem) {
        // #1 递归终结条件 & 递归终结时需要做的事情
        // 如果 当前生成的组合的大小 == 预期的组合大小，说明 找到了一个合法的组合，则：
        if (currentGeneratedCombination.size() == expectedCombinationItemAmount) {
            // 把此组合 添加到 组合列表中
            List<Integer> legitCombination = new ArrayList<>(currentGeneratedCombination);
            combinationList.add(legitCombination);
            // 结束本级调用，返回上一级调用
            return;
        }

        // #2 本级递归要做的事情
        // 对于每一个当前元素，构造出 由它作为起始元素的合法组合
        for (int currentItem = dynamicStartAnchorItem; currentItem <= availableMaxItem; currentItem++) {
            // #2-1 向“当前组合”中添加“当前元素” {1} | {2} | {3} | {4}
            currentGeneratedCombination.addLast(currentItem);
            // #2-2 递归地 从剩余的(N-1)个元素中，挑选出剩余的所需元素  以此来 补齐组合所需的其他元素 👇
            // 子问题：在包含有(N-1)个自然数的序列中，挑选出(K-1)个元素 构成“大小为(K-1)的组合” 最终使得组合的元素数量 == 预期的组合尺寸
            // 子问题的解 就是原始问题的解的一部分
            constructGivenSizeCombinationFrom(availableMaxItem, expectedCombinationItemAmount, currentItem + 1); // 注意这里的代码是i + 1,而不是anchorItem + 1
            // #2-3 回溯掉”当前的选择“，以便 算法能够”重新选择“后继的元素
            currentGeneratedCombination.removeLast();
        }
    }
}
