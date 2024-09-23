package com.henry.leetcode_traning_camp.week_02.day2.group_anagram_together;

import java.util.*;

// 验证：对于字母异位词，可以使用 “排序后的基准字符串” 与 pivotStrToItsAnagramsMap的方式 来 得到各组的 字母异位词；
public class Solution_groupStringsViaAnagram {
    public static void main(String[] args) {
        String[] strArr = new String[]{"eat", "tea", "tan", "ate", "nat", "bat"};
//        String[] strArr = {"eat", "tea", "tan", "ate", "nat", "bat"};

        List<List<String>> groupedAnagramList = groupAnagramsIn(strArr);

        printAnagramsPerGroup(groupedAnagramList);

    }

    private static void printAnagramsPerGroup(List<List<String>> groupedAnagramList) {
        for (int currentGroupNo = 0; currentGroupNo < groupedAnagramList.size(); currentGroupNo++) {

            List<String> currentGroup = groupedAnagramList.get(currentGroupNo);
            for (int currentSpot = 0; currentSpot < currentGroup.size(); currentSpot++) {
                String currentAnagramStr = currentGroup.get(currentSpot);
                System.out.print(currentAnagramStr + " ");
            }

            // 换行，准备打印下一组
            System.out.println();
        }
    }

    private static List<List<String>> groupAnagramsIn(String[] strArr) {
        if (strArr == null || strArr.length == 0) return new ArrayList<>();

        // 准备一个map对象    存储从 ”基准字符串“ -> ”该字符串的所有字母异位词“ 之间的映射关系
        Map<String, List<String>> strToItsGroupMap = new HashMap<>();

        // 准备一个循环   处理字符串数组中的每一个字符串
        for (String currentStr : strArr) {
            // 对”当前字符串“中的字符 进行排序，得到一个 ”字符升序的字符串“
            String sortedCurrentStr = sort(currentStr);

            // 如果 ”字符升序的字符串“ 在map中不存在，说明 map中还没有记录该基准字符串的映射关系，则：
            if (isNewKey(strToItsGroupMap, sortedCurrentStr)) {
                // 将其映射到一个 空列表上
                strToItsGroupMap.put(sortedCurrentStr, new ArrayList<>());
            }

            // 把 ”当前的字符串“ 添加到 ”其基准字符串“所对应的pair中
            List<String> belongedGroup = strToItsGroupMap.get(sortedCurrentStr);
            belongedGroup.add(currentStr);
        }

        // 获取map中的value视图?? 并封装到一个list中 作为返回值
        return new ArrayList<>(strToItsGroupMap.values());
    }

    private static boolean isNewKey(Map<String, List<String>> strToItsGroupMap, String sortedCurrentStr) {
        return !strToItsGroupMap.containsKey(sortedCurrentStr);
    }

    private static String sort(String currentStr) {
        char[] currentCharacterArr = currentStr.toCharArray();
        Arrays.sort(currentCharacterArr);
        String sortedCurrentStr = String.valueOf(currentCharacterArr);
        return sortedCurrentStr;
    }
}
/*
expr:
    1 map对象的key集合中是否包含指定的key：map.containsKey(xxx)
    2 获取到map对象中的value的集合：map.values()
    3 使用一个集合来初始化list对象：new ArrayList<>(map.values());
 */
