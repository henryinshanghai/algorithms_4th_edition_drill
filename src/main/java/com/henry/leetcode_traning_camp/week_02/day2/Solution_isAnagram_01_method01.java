package com.henry.leetcode_traning_camp.week_02.day2;

public class Solution_isAnagram_01_method01 {
    public static void main(String[] args) {
        String s = "anagram";
        String t = "nagaram";

        if (isAnagram(s, t)) {
            System.out.println("字符串s与字符串t互为字母异位词");
        } else {
            System.out.println("they are not!");
        }
    }

    private static boolean isAnagram(String s, String t) {
        if (s == null || t == null)
            return false;

        int[] counts = new int[26];
        for (int i = 0; i < s.length(); i++) {
            counts[s.charAt(i) - 'a']++;
            counts[t.charAt(i) - 'a']--;
        }

        for (int count : counts) {
            if (count != 0) {
                return false;
            }
        }

        return true;
    }
}
