package com.henry.leetcode_traning_camp.week_01.day1.arrays.sum_3item_to_0;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// 验证：#1 对于随机数组，可能通过对数组排序 来 使得原始问题被简化；
// #2 对于元素加和结果为0的要求，可以使用 锚指针 + 两个前后指针 的手段 来 找到所有满足条件的元素组合；
// #3 为了避免重复元素 所导致的重复组合，在遇到重复元素（不管是🐱指针还是动指针）时，都需要跳过重复元素
public class Solution_three_sum {
    public static void main(String[] args) {
        // 准备数组...
        int[] randomItemSequence = {-1, 0, 1, 2, -1, -4};

        // 获取到数组中所有满足条件的(a,b,c)元组的集合
        List<List<Integer>> validCombos = threeItemComboSumTo0(randomItemSequence);

        // 遍历集合中的每一个元组，并打印元组中的数字
        printResults(validCombos);
    }

    private static void printResults(List<List<Integer>> res) {
        for (int i = 0; i < res.size(); i++) {
            System.out.println("满足（a+b+c）的三个数字为： " + res.get(i).get(0) + "," +
                    res.get(i).get(1) + "," + res.get(i).get(2));
        }
    }

    private static List<List<Integer>> threeItemComboSumTo0(int[] radomItemArr) {
        // 0 鲁棒性代码
        List<List<Integer>> validComboList = new LinkedList<>();
        if (radomItemArr == null || radomItemArr.length < 3) return validComboList;

        // 1 对数组元素进行排序
        Arrays.sort(radomItemArr);

        // 2 使用 定指针 + 两个动指针的方式 来 找到 所有满足条件的三个元素
        // 对于当前锚指针...
        for (int anchor = 0; anchor < radomItemArr.length - 2; anchor++) {
            // 对特殊情况进行处理：如果最小元素就已经大于0了，说明 不会存在有 满足条件的元素元组，可以直接break出for循环；
            int itemOnAnchorSpot = radomItemArr[anchor];
            if (itemOnAnchorSpot > 0) break;

            // 处理 锚指针元素重复的情况👇
            // 如果当前元素 与 它的前一个元素相等，说明遇到了重复元素，则：跳过当前循环，继续处理下一个元素（因为我们不想得到重复的三元组）
            // 因为已经将 包含有前一个元素的所有组合 都加入到结果中了，再次进行双指针搜索 只会得到重复组合。
            if (anchor > 0 && itemOnAnchorSpot == radomItemArr[anchor - 1]) continue; // 直到遇到新的元素

            // 准备两个动指针
            int forwardCursor = anchor + 1;
            int backwardsCursor = radomItemArr.length - 1;

            // 在两个动指针之间 查找所有满足条件的(b+c)
            while (forwardCursor < backwardsCursor) {
                int itemOnForwardCursor = radomItemArr[forwardCursor];
                int itemOnBackwardsCursor = radomItemArr[backwardsCursor];
                int sumForCurrentCombo = itemOnAnchorSpot + itemOnForwardCursor + itemOnBackwardsCursor;

                if (sumForCurrentCombo == 0) { // 如果当前组合满足条件: 加和结果为0，
                    // 则：把当前元素组合 (以列表的形式)添加到 结果集合中
                    List<Integer> currentValidCombo = Arrays.asList(itemOnAnchorSpot, itemOnForwardCursor, itemOnBackwardsCursor);
                    validComboList.add(currentValidCombo);

                    // expr：处理动指针指向的元素 重复的情况
                    // 手段：移动指针到下一个位置上
                    while (forwardCursor < backwardsCursor && itemOnForwardCursor == radomItemArr[forwardCursor + 1])
                        forwardCursor++;
                    while (forwardCursor < backwardsCursor && itemOnBackwardsCursor == radomItemArr[backwardsCursor - 1])
                        backwardsCursor--;

                    // 分别移动指针 到下一个有效的位置，以 继续查找当前anchor 满足条件的所有可能的组合
                    forwardCursor++;
                    backwardsCursor--;
                } else if (sumForCurrentCombo < 0) { // 如果当前组合的加和结果小于0，说明 forwardCursor指向的元素太小了，则
                    // 移动 forward指针到下一个位置
                    forwardCursor++;
                } else {
                    // 移动 backwards指针到上一个位置
                    backwardsCursor--;
                }
            }
        }

        return validComboList;
    }
}
