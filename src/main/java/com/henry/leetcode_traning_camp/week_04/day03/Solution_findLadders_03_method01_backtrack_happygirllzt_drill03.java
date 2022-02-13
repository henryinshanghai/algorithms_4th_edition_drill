package com.henry.leetcode_traning_camp.week_04.day03;

import java.util.*;

public class Solution_findLadders_03_method01_backtrack_happygirllzt_drill03 {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList =
                new ArrayList<>(
                        Arrays.asList("hot","dot","dog","lot","log","cog")
                );

        List<List<String>> res = findLadders(beginWord, endWord, wordList);
        System.out.println("最终的结果为：" + res);
    }

    private static List<List<String>> findLadders(String beginWord,
                                                  String endWord,
                                                  List<String> wordList) {
        /* 从beginWord开始来创建图 */
        List<List<String>> res = new ArrayList<>();
        Set<String> validTempWordList = new HashSet<>(wordList);

        if (!validTempWordList.contains(endWord)) {
            return res;
        }

        Map<String, List<String>> map = new HashMap<>();
        Set<String> startSet = new HashSet<>();
        startSet.add(beginWord);

        bfs(startSet, endWord, validTempWordList, map);

        printMap(map);

        /* map创建成功后，使用dfs来在图中查找路径 */
        List<String> path = new ArrayList<>();
        path.add(beginWord);
        dfs(res, path, beginWord, endWord, map);

        return res;
    }

    private static void dfs(List<List<String>> res,
                            List<String> path,
                            String addInWord,
                            String endWord,
                            Map<String, List<String>> map) {
        // goal
        if (addInWord.equals(endWord)) {
            res.add(new ArrayList(path));
            return;
        }

        // NPE
        if (map.get(addInWord) == null) return;

        // make choices and backtrack
        for (String neighbor : map.get(addInWord)) {
            path.add(neighbor);

            dfs(res, path, neighbor, endWord, map);

            path.remove(path.size() - 1);
        }
    }

    private static void printMap(Map<String, List<String>> map) {
        if (map == null) return;

        for (Map.Entry<String, List<String>> currEntry : map.entrySet()) {
            System.out.print("Key - " + currEntry.getKey() + " | ");
            System.out.println("Value - " + currEntry.getValue());
        }
    }

    private static void bfs(Set<String> startSet,
                            String endWord,
                            Set<String> validTempWordList,
                            Map<String, List<String>> map) {
        // Mark：这里没有添加鲁棒性代码 但似乎没有影响

        // 准备一个boolean类型的变量 用于标识图的扩展是否已经完成
        boolean isFinish = false;

        // 准备一个集合   用来标识图的下一层节点（这些节点会用来创建图的下一层）
        Set<String> nextLayer = new HashSet<>();

        // 每次调用时，都先把startSet从备选单词列表中清除
        validTempWordList.removeAll(startSet);

        // 从startSet开始来对图进行扩展
        for (String currWord : startSet) {
            char[] currCharArr = currWord.toCharArray();

            for (int i = 0; i < currCharArr.length; i++) {
                char currChar = currCharArr[i];

                for (char ch = 'a'; ch <= 'z'; ch++) {
                    currCharArr[i] = ch;
                    String transferWord = new String(currCharArr);

                    if (validTempWordList.contains(transferWord)) {
                        if (endWord.equals(transferWord)) {
                            isFinish = true;
                        } else {
                            // 把这个转换到的单词添加到下一层节点的集合中
                            nextLayer.add(transferWord);
                        }

                        // 创建图对象
                        if (map.get(currWord) == null) {
                            map.put(currWord, new ArrayList<>());
                        }
                        map.get(currWord).add(transferWord);
                    }
                }

                currCharArr[i] = currChar;
            }
        }

        if (!isFinish) {
            bfs(nextLayer, endWord, validTempWordList, map);
        }
    }
}
