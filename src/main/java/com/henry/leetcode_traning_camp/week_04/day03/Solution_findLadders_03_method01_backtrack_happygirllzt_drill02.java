package com.henry.leetcode_traning_camp.week_04.day03;

import java.util.*;

public class Solution_findLadders_03_method01_backtrack_happygirllzt_drill02 {
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

    private static List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
//        第 1 步：使用广度优先遍历找到终点单词，并且记录下沿途经过的所有结点，以邻接表形式存储；
//        第 2 步：通过邻接表，使用回溯算法得到所有从起点单词到终点单词的路径
        List<List<String>> res = new ArrayList<>();
        Set<String> validTempWordList = new HashSet(wordList);

        if (!validTempWordList.contains(endWord)) {
            return res;
        }

        /* 执行bfs，建立从beginWord到endWord的图 */
        Map<String, List<String>> map = new HashMap();
        Set<String> startSet = new HashSet<>();
        startSet.add(beginWord);
        bfs(startSet, endWord, map, validTempWordList);

        printMap(map);
        System.out.println("-----------------");

        // 创建完成图后，从图中找出从beginWord到endWord的最短路径
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
        // base case    goal
        if (addInWord.equals(endWord)) {
            res.add(new ArrayList(path));
            return;
        }

        // is this necessary? if it is, why?
        // 这里需要对图遍历是否已经结束进行判断，否则get()方法可能会出现空指针异常
        // 判断图的遍历是否已经结束了
        if (map.get(addInWord) == null) return;

        // 从图中找当前顶点的所有邻居    用来创建path    make your choice
        for (String next : map.get(addInWord)) {
            path.add(next);

            dfs(res, path, next, endWord, map);

            path.remove(path.size() - 1);
        }

    }

    private static void printMap(Map<String, List<String>> map) {
        if (map == null) return;

        for (Map.Entry<String, List<String>> curr : map.entrySet()) {
            System.out.print("Key - " + curr.getKey() + " | ");
            System.out.println("Value - " + curr.getValue());
        }
    }

    /**
     * 使用BFS来创建一幅图
     * @param startSet  作为扩展过程中的起点的顶点
     * @param endWord   作为扩展过程中的终点的顶点（Vertex）
     * @param map   最终创建得到的图对象
     * @param validTempWordList 有效的顶点的集合
     */
    private static void bfs(Set<String> startSet,
                            String endWord,
                            Map<String, List<String>> map,
                            Set<String> validTempWordList) {
        /* 为扩展图做一些准备 */
        // 1 图扩展结束的标识
        if (startSet.size() == 0) return; // Mark:this seems not necessary

        // 2 准备一个新的集合   用于存储图中下一层的顶点
        Set<String> temp = new HashSet<>();

        // 3 准备一个boolean类型的变量   用于标识图的扩展是否已经结束
        boolean finish = false;

        // 4 在新的一轮调用之前，先把startSet从validTempWordList中移除  防止出现路径回头的情况
        validTempWordList.removeAll(startSet);

        /* 准备一个循环，从startSet开始，在循环中扩展图 */
        for (String currWord : startSet) {
            // 把字符串转化为字符数组，方便对字符串进行转换得到新的字符串
            char[] currCharArr =  currWord.toCharArray();
            for (int i = 0; i < currCharArr.length; i++) {
                char currChar = currCharArr[i];

                for (char ch = 'a'; ch <= 'z'; ch++) {
                    currCharArr[i] = ch;
                    String transferWord = new String(currCharArr);

                    if (validTempWordList.contains(transferWord)) {
                        if (endWord.equals(transferWord)) {
                            finish = true;
                        } else { // 否则，把transferWord添加到tmp集合中 作为图的下一层顶点
                            temp.add(transferWord);

                            /* EXPR：扩展图的操作在else之外 */
                        }

                        // 并使用当前单词来扩展图  只要找到了邻居，就扩展图！
                        if (map.get(currWord) == null) {
                            map.put(currWord, new ArrayList<>());
                        }
                        map.get(currWord).add(transferWord);
                    }
                }

                // 转换结束后，恢复当前位置的字符 进行下一个字符的转换
                currCharArr[i] = currChar;
            }
        }

        /* 扩展图的当前层之后，判断图有没有扩展结束 如果没有，继续在当前基础上进行扩展 */
        if (!finish) {
            bfs(temp, endWord, map, validTempWordList); // temp、map与validTempWordList都发生了变化
        }
    }
} // 内容不少 完全打字就需要15min
