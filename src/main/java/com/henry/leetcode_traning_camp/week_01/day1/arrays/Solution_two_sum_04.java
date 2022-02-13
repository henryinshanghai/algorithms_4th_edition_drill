package com.henry.leetcode_traning_camp.week_01.day1.arrays;

public class Solution_two_sum_04 {
    public static void main(String[] args) {
        // 准备两个参数
        int[] numbers = {2, 7, 11, 15};
        int target = 9;

        int[] result = twoSum(numbers, target);
        System.out.println("数组中相加和为" + target + "的两个数的位置为：" + "(" + result[0] + "," + result[1] + ")");
    }

    private static int[] twoSum(int[] numbers, int target) {
        // 准备两个指针
        int front = 0;
        int back = numbers.length - 1;

        // 准备一个循环   在循环中找到满足条件的数字对
        while (front < back) {
            int sum = numbers[front] + numbers[back];

            if (sum == target) {
                return new int[]{front + 1, back + 1};
            } else if (sum < target) {
                front++;
            } else {
                back--;
            }
        }
        return new int[]{0, 0};
    }
}
