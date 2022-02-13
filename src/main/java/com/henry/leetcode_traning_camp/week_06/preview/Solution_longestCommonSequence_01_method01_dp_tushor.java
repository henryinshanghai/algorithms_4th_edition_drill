package com.henry.leetcode_traning_camp.week_06.preview;

public class Solution_longestCommonSequence_01_method01_dp_tushor {
    public static void main(String[] args) {
        String str1 = "ABCDGHLQR";
        String str2 = "AEDPHR"; // ADHR  公共子序列的长度为4

        int result = lcsDynamic(str1.toCharArray(), str2.toCharArray());

        System.out.println(str1 + " 与 " + str2 + "之间的最大公共序列的长度为：" + result);

    }

    public static int lcsDynamic(char str1[], char str2[]) {

        /* 〇 准备一些DP所需要的基础变量 */
        // 准备一个临时的二维数组  用于作为动态规划所需要的计算空间
        int temp[][] = new int[str1.length + 1][str2.length + 1];
        // 准备一个整型变量     用于存储公共子序列的最大长度
        int max = 0;


        /* Ⅰ 准备两层的嵌套循环    在循环中，填充二维数组中的每一个位置 */
        for (int i = 1; i < temp.length; i++) {
            for (int j = 1; j < temp[i].length; j++) {
                // EXPR：递推公式
                // 1 如果两个指针指向的字符相同，说明：这个字符可以用于构成最长公共子串
                if (str1[i - 1] == str2[j - 1]) {
                    // 则：当前位置的元素值 = 左上角位置的元素值 + 1
                    temp[i][j] = temp[i - 1][j - 1] + 1;
                } else {
                    // 2 如果两个指针指向的字符不相同，说明当前字符不能用来构成最长公共子串
                    // 则：当前位置的元素值(公共子序列的长度) = Max(上一行（同一列）的元素值, 同一行(上一列)的元素值)
                    temp[i][j] = Math.max(temp[i][j - 1], temp[i - 1][j]);
                }


                // 3 使用当前计算得到的元素值来更新max
                if (temp[i][j] > max) {
                    max = temp[i][j];
                }
            }
        }

        /* Ⅱ 返回最大值 */
        return max;
    }
}
