package com.henry.leetcode_traning_camp.week_03.day04.permunation_with_repeated_item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// 验证：可以使用”排序 + 回溯 + 对重复使用的元素的剪枝操作“的方式 来 得到 含有重复元素的序列的所有全排列的集合
public class Solution_construct_permutations_via_cutBranch {
    public static void main(String[] args) {
        int[] numSequence = new int[]{1, 5, 2, 1};
        // #1 先对原始的元素序列 进行排序
        Arrays.sort(numSequence);
        // #2 再从排序后的元素序列中，求得所有的排列
        List<List<Integer>> permutationList = permuteUnique(numSequence);
        // #3 打印所有得到的排列
        print(permutationList);
    }

    private static void print(List<List<Integer>> permutationList) {
        for (List<Integer> currentPermutation : permutationList) {
            System.out.println(currentPermutation);
        }
    }

    // 准备一个元素为列表的列表 用于存放全排列的集合
    private static List<List<Integer>> uniquePermutationList = new ArrayList<>();
    private static Deque<Integer> currentConstructedPermutation = new LinkedList<>();

    private static List<List<Integer>> permuteUnique(int[] numSequence) {
        int numAmount = numSequence.length;
        if (numAmount == 0) {
            return uniquePermutationList;
        }

        boolean[] itemIndexToIsUsed = new boolean[numAmount];
        dfs(numSequence, itemIndexToIsUsed);

        return uniquePermutationList;
    }

    private static void dfs(int[] numSequence, boolean[] itemToIsUsedArr) {
        if (currentConstructedPermutation.size() == numSequence.length) {
            List<Integer> legitPermutation = new ArrayList<>(currentConstructedPermutation);
            uniquePermutationList.add(legitPermutation);
            return;
        }

        for (int currentItemIndex = 0; currentItemIndex < numSequence.length; currentItemIndex++) {
            if (itemToIsUsedArr[currentItemIndex]) {
                continue;
            }

            // 新增的剪枝条件
            if (compositeConditions(numSequence, itemToIsUsedArr, currentItemIndex)) {
                continue;
            }

            currentConstructedPermutation.addLast(numSequence[currentItemIndex]);
            itemToIsUsedArr[currentItemIndex] = true;

            dfs(numSequence, itemToIsUsedArr); // EXPR：这里的depth参数绑定 depth+1 或者是 i+1都没啥关系

            currentConstructedPermutation.removeLast();
            itemToIsUsedArr[currentItemIndex] = false;
        }
    }

    private static boolean compositeConditions(int[] numSequence, boolean[] itemToIsUsedArr, int currentItemIndex) {
        /* 对”重复元素“的判断 */
        // #1 当前元素 与 前一个元素相同； #2 前一个元素的当前状态是”未被使用“ - 按照构造排列的规则，这说明”当前在使用重复的元素“
        return isValidIndex(currentItemIndex)
                && itemRepeatWithPreviousOne(numSequence, currentItemIndex)
                && previousItemNotUsed(itemToIsUsedArr, currentItemIndex);
    }

    // 如果前一个元素的当前状态是“未使用”的话，说明这已经是第二次使用”相同的元素“来构造排列，则：这样的情况需要进行剪枝
    private static boolean previousItemNotUsed(boolean[] itemToIsUsedArr, int currentItemIndex) {
        return itemToIsUsedArr[currentItemIndex - 1] == false;
    }

    private static boolean itemRepeatWithPreviousOne(int[] numSequence, int currentItemIndex) {
        return numSequence[currentItemIndex] == numSequence[currentItemIndex - 1];
    }

    private static boolean isValidIndex(int currentItemIndex) {
        return currentItemIndex > 0;
    }
}
