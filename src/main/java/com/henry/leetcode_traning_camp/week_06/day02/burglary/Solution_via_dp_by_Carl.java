package com.henry.leetcode_traning_camp.week_06.day02.burglary;

// 验证：对于入室抢劫这种 不同选择会导致不同结果的问题，可以使用 在递推公式中列举所有option的手段 来 得到想要的结果
// 特征：像递推一样，不要纠结于 具体是怎么到达某状态的细节，而是 建立 能够到达某状态的信心即可。
// 过程：dp[]数组 => 递推公式 => dp[]数组元素初始化 => 原始数组的遍历顺序 => 打印dp[]数组是否符合预期
public class Solution_via_dp_by_Carl {
    public static void main(String[] args) {
//        int[] houseValueArr = {1, 2, 3, 1}; // 4
        int[] houseValueArr = {2, 7, 9, 3, 1}; // 12
        int maxBurglaryValue = getMaxBurglaryValueFrom(houseValueArr);
        System.out.println("从当前房屋序列中，能够抢劫得到的最大金额为： " + maxBurglaryValue);
    }

    private static int getMaxBurglaryValueFrom(int[] houseValueArr) {
        if (houseValueArr == null || houseValueArr.length == 0) return 0;
        if (houseValueArr.length == 1) return houseValueArr[0];

        // dp[]数组 当前house -> 到当前house为止所能抢劫的最大金额
        int[] currentHouseToMaxBurglaryValueUpToIt = new int[houseValueArr.length];

        // 初始化dp[]数组元素 来 使得递推有一个正确的开始
        currentHouseToMaxBurglaryValueUpToIt[0] = houseValueArr[0]; // 到第0个house位置所能抢劫的最大金额
        currentHouseToMaxBurglaryValueUpToIt[1] =
                Math.max(currentHouseToMaxBurglaryValueUpToIt[0], houseValueArr[1]); // 到第1个house位置所能抢劫到的最大金额

        // 确定遍历顺序
        for (int currentHouse = 2; currentHouse < houseValueArr.length; currentHouse++) {
            // 递推公式
            currentHouseToMaxBurglaryValueUpToIt[currentHouse] = // 抢劫进行到当前house时...
                    // #1 选择不抢劫当前house，说明抢劫得到的金额保持不变，则：dp[i] = dp[i-1]
                    Math.max(currentHouseToMaxBurglaryValueUpToIt[currentHouse - 1],
                            // #2 选择抢劫当前house，说明上一个house没有被抢劫，则：当前抢劫所得到的金额dp[i] = 抢劫到上上个house所得到的金额dp[i-2] + 当前house的价值
                            currentHouseToMaxBurglaryValueUpToIt[currentHouse - 2] + houseValueArr[currentHouse]);
        }

        // 返回到最后一个house位置 所能抢劫到的金额，就是 所有抢劫方案中所能抢劫得到的最大金额
        return currentHouseToMaxBurglaryValueUpToIt[houseValueArr.length - 1];
    }
}