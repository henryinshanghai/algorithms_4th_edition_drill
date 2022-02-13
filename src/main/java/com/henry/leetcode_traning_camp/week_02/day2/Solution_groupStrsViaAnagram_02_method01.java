package com.henry.leetcode_traning_camp.week_02.day2;

import java.util.*;

public class Solution_groupStrsViaAnagram_02_method01 {
    public static void main(String[] args) {
        String[] strs = new String[]{"eat", "tea", "tan", "ate", "nat", "bat"};
//        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};

        List<List<String>> res = groupAnagram(strs);

        for (int i = 0; i < res.size(); i++) {
            for (int j = 0; j < res.get(i).size(); j++) {
                System.out.print(res.get(i).get(j) + " ");
            }

            System.out.println();
        }

    }

    private static List<List<String>> groupAnagram(String[] strs) {
        if (strs == null || strs.length == 0) return new ArrayList<>();

        // 准备一个map对象    存储从字符串基准 -> 该字符串的所有字母异位词之间的映射关系
        Map<String, List<String>> map = new HashMap<>();

        // 准备一个循环   处理字符串数组中的每一个字符串
        for (String currStr : strs) {
            char[] charArr = currStr.toCharArray();
            Arrays.sort(charArr);
            String sortedCurrStr = String.valueOf(charArr);

            if (!map.containsKey(sortedCurrStr)) {
                map.put(sortedCurrStr, new ArrayList<>());
            }

            map.get(sortedCurrStr).add(currStr);
        }

        return new ArrayList<>(map.values());
    }
}
/*
expr:
    1 map对象的key集合中是否包含指定的key：map.containsKey(xxx)
    2 获取到map对象中的value的集合：map.values()
    3 使用一个集合来初始化list对象：new ArrayList<>(map.values());
 */
