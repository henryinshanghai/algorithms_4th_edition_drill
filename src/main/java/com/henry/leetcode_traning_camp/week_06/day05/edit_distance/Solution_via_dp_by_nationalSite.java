package com.henry.leetcode_traning_camp.week_06.day05.edit_distance;

// 验证：对于从字符串1变化到字符串2的问题，可以使用
// 动态规划 currentComboToItsRequiredMinEditDistance[]
// 递推公式#1：dp[i][j] = dp[i-1][j-1] // 最后一个字符相同时
// 递推公式#2：dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) // 最后一个字符不相同时
public class Solution_via_dp_by_nationalSite {
    public static void main(String[] args) {
        String str1 = "horse";
        String str2 = "ros";

        int operationTimes = minEditDistanceFromAToB(str1, str2); // 3
        System.out.println("从 " + str1 + "变化得到 " + str2 + "所需要的最少操作数为：" + operationTimes);
    }


    private static int minEditDistanceFromAToB(String str1, String str2) {
        /* 〇 把字符串转化成为字符数组   方便得到子字符序列 */
        char[] str1Arr = str1.toCharArray();
        char[] str2Arr = str2.toCharArray();

        /* Ⅰ 准备一个dpTable[]  这里有两个需要比较的字符串，所以dpTable[][] */
        int[][] currentComboToItsMinRequiredEditDistance = new int[str1Arr.length + 1][str2Arr.length + 1]; // 多出一个位置用来列举""空字符串的情况

        /* Ⅱ 初始化dpTable[][]中的一些位置 */
        for (int currentRow = 1; currentRow < currentComboToItsMinRequiredEditDistance.length; currentRow++) {
            currentComboToItsMinRequiredEditDistance[currentRow][0] = currentRow;
        } // 第一行 insert all the way up to the end

        for (int currentColumn = 1; currentColumn < currentComboToItsMinRequiredEditDistance[0].length; currentColumn++) {
            currentComboToItsMinRequiredEditDistance[0][currentColumn] = currentColumn;
        } // 第一列 insert all the way up to the end

        /* Ⅲ 使用 已经初始化的元素值 + 不同规模子问题之间的递推关系 来 填充dpTable[][] */
        for (int str1Cursor = 1; str1Cursor <= str1Arr.length; str1Cursor++) {
            for (int str2Cursor = 1; str2Cursor <= str2Arr.length; str2Cursor++) {
                // 如果 两个子字符串 最后一个位置的字符相等,说明 最后一个位置无需进行任何编辑。则：
                if (str1Arr[str1Cursor - 1] == str2Arr[str2Cursor - 1]) {
                    // dp[i][j] = dp[i-1][j-1]
                    currentComboToItsMinRequiredEditDistance[str1Cursor][str2Cursor]
                            = currentComboToItsMinRequiredEditDistance[str1Cursor - 1][str2Cursor - 1];
                } else {
                    // 否则，找出回退得到的子问题的最小解
                    currentComboToItsMinRequiredEditDistance[str1Cursor][str2Cursor] = 1 + // 针对以下的三种情况，这里的+1可以代表不同的操作
                            Math.min(
                                    currentComboToItsMinRequiredEditDistance[str1Cursor - 1][str2Cursor], // 删除字符的操作(移除字符y) + (henr -> mahendra)
                                    Math.min(currentComboToItsMinRequiredEditDistance[str1Cursor][str2Cursor - 1], // (henry -> mahendr) + 添加字符(a)的操作
                                            currentComboToItsMinRequiredEditDistance[str1Cursor - 1][str2Cursor - 1]) // (henr -> mahendr) + 替换字符的操作(y -> a)
                            );
                }
            }
        }

        /* Ⅳ 返回正确的combo 所对应的 最少编辑距离 */
        return currentComboToItsMinRequiredEditDistance[str1Arr.length][str2Arr.length];
    }
}
