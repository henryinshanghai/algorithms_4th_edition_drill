package com.henry.leetcode_traning_camp.week_02.day2.is_anagram;

public class Solution_isAnagram {
    public static void main(String[] args) {
        String s = "anagram";
        String t = "nagaram";

        if (isAnagram(s, t)) {
            System.out.println("字符串s与字符串t互为字母异位词");
        } else {
            System.out.println("they are not!");
        }
    }

    private static boolean isAnagram(String stringA, String stringB) {
        if (stringA == null || stringB == null)
            return false;

        int[] characterToItsCount = new int[26];
        for (int currentSpot = 0; currentSpot < stringA.length(); currentSpot++) {
            // 使用字符串A中的字符 来 对characterToItsCount[]数组元素 做累加
            accumulateCharacterAmountIn(stringA, characterToItsCount, currentSpot);

            // 然后使用字符串B中的字符 来 对characterToItsCount[]数组元素 做累减
            subtractCharacterAmountIn(stringB, characterToItsCount, currentSpot);
        }

        // 如果 characterToItsCount[]数组中的所有元素的值都会为0，说明 两个单词是字母异位词，则
        if (isExistingZeroItem(characterToItsCount)) {
            return false;
        }

        // 否则，说明这两个单词是字母异位词
        return true;
    }

    private static boolean isExistingZeroItem(int[] characterToItsCountArr) {
        for (int currentCount : characterToItsCountArr) {
            // 如果某个位置上的元素值不为0，则：不是 字母异位词
            if (currentCount != 0) {
                return true;
            }
        }
        return false;
    }

    private static void subtractCharacterAmountIn(String stringB, int[] characterToItsCount, int currentSpot) {
        char currentCharacterInB = stringB.charAt(currentSpot);
        int indexToRepresentCharacterInB = currentCharacterInB - 'a';
        characterToItsCount[indexToRepresentCharacterInB]--;
    }

    private static void accumulateCharacterAmountIn(String stringA, int[] characterToItsCount, int currentSpot) {
        // 获取到 当前位置上的字符
        char currentCharacterInA = stringA.charAt(currentSpot);
        // 计算 此字符 应该在数组中出现的位置；  手段：当前字符 - 'a' 会得到一个int值吗?
        int indexOfCurrentCharacterInA = currentCharacterInA - 'a';
        // 把数组元素+1 来 为此字符计数
        characterToItsCount[indexOfCurrentCharacterInA]++;
    }
}
