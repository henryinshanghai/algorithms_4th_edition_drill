package com.henry.leetcode_traning_camp.week_01.day3_dp_theme.biggest_sub_arr;

// 验证：对于 计算元素子序列的加和值的情况，可以 使用当前加和值的正负结果 来 重置子序列 的方式
public class Solution_biggest_sum_of_consecutiveSubArr_via_iteration {
    public static void main(String[] args) {
        int[] itemSequence = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int maxSumOfArr = maxSubArray(itemSequence);
        System.out.println("原始数组中所能找到的 <元素值加和最大的子数组的加和值为>: " + maxSumOfArr);
    }

    private static int maxSubArray(int[] itemSequence) {
        // #0 鲁棒性代码
        if (itemSequence == null || itemSequence.length == 0) return 0;

        // #1 变量准备
        // 准备一个int变量，用于记录当前子数组的元素加和值
        int sumOfCurrentSubArr = itemSequence[0]; // 初始化为数组的第一个元素
        // 准备一个int变量，用于实时更新子数组的元素加和值的最大值
        int maxSumOfSubArr = sumOfCurrentSubArr; // 初始化为当前子数组的元素加和值

        // #2 准备一个循环，迭代数组中的每一个元素
        // 作用：Ⅰ 查找以当前元素作为起始元素的所有有效子数组； Ⅱ 使用找到的有效子数组来更新sum_max的值
        for (int currentSpot = 1; currentSpot < itemSequence.length; currentSpot++) { // 从第二个元素开始，尝试把当前元素累加到子数组中...
            // 计算子数组的元素加和值
            int currentItem = itemSequence[currentSpot];
            /*
                规则：如果当前子数组的和大于0，就继续计入下一个元素；
                如果当前子数组的和小于0，则使用当前元素来重置子数组
             */
            sumOfCurrentSubArr = (sumOfCurrentSubArr > 0) ? sumOfCurrentSubArr + currentItem : currentItem;

            // 尝试用“当前子数组的加和值” 来 更新“子数组元素加和值的最大值”
            maxSumOfSubArr = Math.max(maxSumOfSubArr, sumOfCurrentSubArr);
        }

        // #3 返回老大哥
        return maxSumOfSubArr;
    }
} // 这种方法的时间复杂度是O(N)，因为它用来一层循环来处理数组中的节点
