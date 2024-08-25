package com.henry.leetcode_traning_camp.week_01.day3_dp_theme.biggest_sub_arr;

// 验证：对于 在原始数组中求取 加和结果最大的子数组的要求，可以使用动态规划的方式 来 得到最大的加和结果
public class Solution_biggest_sum_of_consecutiveSubArr_via_dynamic_programming {
    public static void main(String[] args) {
        int[] itemSequence = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        int maxSumOfSubArr = maxSubArr(itemSequence);

        System.out.println("原始数组中所能找到的 <元素值加和最大的子数组的加和值为>: " + maxSumOfSubArr);
    }

    /*
        作用：找到数组nums中“元素加和值最大的子数组”，并求出该子数组的元素加和值结果
        特征：这是一个求最值的问题，因此可能可以使用动态规划DP
        验证：这个问题符合{最优子结构}吗？  aka 子问题之间相互独立   一头雾水，我甚至看不出来子问题是什么？为什么会需要子问题？

        元素加和值最大且元素连续的子数组，怎么找？
        xxx

        这里假设我们已经认定了可以使用DP来解决这个问题了。然后DP用法的SOP（DP五部曲）：
        #1 根据需要解决的问题来定义一个dp[]数组；
            原则：1 dp[i]代表着特殊的含义（而且与nums[]数组有关）； 2 需要能够通过dp[i]推导出dp[i+1](这样才能使用数学归纳法来得到状态转移方程)
            针对“找到元素加和值最大且元素连续的子数组”这个问题，我们定义这样的dp[]数组：
                dp[i]表示：以<数组元素nums[i]为结尾>的子数组集合中（以元素A结尾的子数组可能会有多个）,<元素加和值最大的那个子数组>的元素加和值；
                这样的话，dp[]数组中的最大值就是我们要找到的“biggest_sub_arr_sum”
        #2 根据dp[]数组的定义，列出状态转移方程：
            ....,B,A
            以元素A结尾的最大子数组的元素加和值 与 以元素B结尾的最大子数组的元素加和值 之间的具体关系是什么呢？
            aka dp[i+1] 与 dp[i]之间的关系
            其实简单了，就是看看A对最终结果有没有贡献/A能够做出的选择；
                case1：A把自己连到B的最大子数组上（以元素B结束），这样会得到一个更大的子数组；
                case2：A完全抛掉B的最大子数组(因为B的最大子数组的元素加和值也可能为负数)，这样元素A自己就会作为一个新的子数组；
            这两种选择并不一定谁就绝对地大，所以取其中的最大值绑定到dp[i+1]上
            // 要么自成一派，要么和前面的子数组合并
            dp[i] = Math.max(nums[i], nums[i] + dp[i - 1]);
        #3 对dp[]数组的元素 进行必要的初始化，来支持递推的操作；
        #4 确定递推所需要的遍历顺序：
        #5 确认当前的dp[]是否正确；
     */
    private static int maxSubArr(int[] itemSequence) {
        // #0 鲁棒性代码
        int itemAmount = itemSequence.length;
        if (itemAmount == 0) return 0;

        // #1 准备一个dp[]数组     dp[i] 表示 以元素nums[i]为结尾的子数组集合中，加和值最大的那个子数组的加和值
        int[] dp = new int[itemAmount];

        // 1-1 状态转移方程的base case - dp[0] 表示 以nums[0]为结尾的子数组集合中，加和值最大的子数组
        dp[0] = itemSequence[0]; // 第一个元素前面没有子数组

        // 1-2 使用状态转移方程来求出dp[]数组中每一项的值
        for (int currentSpot = 1; currentSpot < itemAmount; currentSpot++) {
            int itemOnCurrentSpot = itemSequence[currentSpot];
            // #1 要么重新开始计算子数组; #2 要么把当前元素计入当前子数组
            dp[currentSpot] = Math.max(itemOnCurrentSpot, itemOnCurrentSpot + dp[currentSpot - 1]);
        }

        // 2 在计算出dp[]数组所有元素的值之后，求出dp[]数组中的最大元素      aka     nums[]数组中的<元素加和值最大的子数组>的元素加和值
        // 得到 nums 的最大子数组
        int maxSum = Integer.MIN_VALUE;
        for (int currentSpot = 0; currentSpot < itemAmount; currentSpot++) {
            maxSum = Math.max(maxSum, dp[currentSpot]);
        }

        return maxSum;
    }
}
