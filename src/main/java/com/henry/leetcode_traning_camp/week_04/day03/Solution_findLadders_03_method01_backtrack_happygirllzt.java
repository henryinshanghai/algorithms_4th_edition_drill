package com.henry.leetcode_traning_camp.week_04.day03;

import java.util.*;

public class Solution_findLadders_03_method01_backtrack_happygirllzt {
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

        // 准备一个res 用于存放所有找到的准换路径
        List<List<String>> res = new ArrayList<>();

        // 把wordList转化成为一个set对象 用于更快速地判断是否存在
        Set<String> dict = new HashSet<>(wordList);

        // 鲁棒性代码
        if (!dict.contains(endWord)) return res;

        // 使用bfs来遍历所有可能 从而找到需要的最短转换路径
        // 准备bfs调用所需要的实际参数
        Map<String, List<String>> map = new HashMap<>();
        Set<String> startSet = new HashSet<>();
        startSet.add(beginWord);
        bfs(startSet, endWord, map, dict);

        /* 查看下map是否按照预期被创建 */
//        System.out.println("使用bfs创建出的图映射关系为：" + map); // map建错了，说明bfs已经写错了
        printMap(map);

        /*  生成结果 */
        // 准备一个list     把beginWord放进去
        List<String> list = new ArrayList<>();
        list.add(beginWord);

        // 使用dfs来生成结果
        dfs(res, list, endWord, beginWord, map);

        return res;

    }

    private static void printMap(Map<String, List<String>> map) {
        if (map == null) return;

        for (Map.Entry<String, List<String>> curr : map.entrySet()) {
            System.out.print("Key - " + curr.getKey() + " | ");
            System.out.println("Value - " + curr.getValue());
        }
    }

    private static void dfs(List<List<String>> res,
                            List<String> list,
                            String endWord, String word,
                            Map<String, List<String>> map) {
        // base case
        if (word.equals(endWord)) {
            res.add(new ArrayList(list)); // 因为list会被回溯，所以我们使用的是一个新的副本
            return;
        }

        // 如果当前单词已经没有可以转化成其他单词的可能 aka 就已经遍历完成整张图了
        if (map.get(word) == null) return; // 直接return  is this redundant?

        // 如果当前单词能够转化成其他一堆的单词，则需要：
        // 遍历可以转换成的单词集合中的每一个单词
        for (String next : map.get(word)) {
            // 把“可以转换成的单词”添加到list中
            list.add(next);
            // 调用dfs,以便把合适的备选单词添加到list中
            dfs(res, list, endWord, next, map);

            // 回溯当前选择
            list.remove(list.size() - 1);
        }
    }

    /**
     * 每一个level的单词会越来越多，所以使用一个startSet作为参数
     * 一个endWord参数
     * 还需要一个映射关系map：描述一个单词所能转换到的其他有效单词
     * 特征：这个BFS并没有使用到Queue
     */
    private static void bfs(Set<String> startSet,
                            String endWord,
                            Map<String, List<String>> map,
                            Set<String> dict) {
        /* Ⅰ 为正确地扩展出图map做一些准备工作 */
        // 如果startSet的size为0，说明 图已经没有下一层顶点了（aka 图已经创建完成），则：直接return
        if (startSet.size() == 0) return;

        // 准备一个tempSet  用于存储序列中下一个位置可能的单词（aka 下一层顶点集合）
        Set<String> tmp = new HashSet<>();

        // 准备一个布尔值  判断转换序列是不是已经走到了结尾
        boolean finish = false;

        /* 把startSet从dict中移除 注：这个可能会被遗漏而导致错误 */
        dict.removeAll(startSet);

        /* Ⅱ 准备一个循环 在当前level上对图进行扩展 */
        // 遍历startSet中的每一个单词，找到其合法的转换结果，并添加到下一层中（并构建图map）/或者直接返回
        for (String s : startSet) {
            // 列举当前单词可能转换到的有效字符串
            char[] chs = s.toCharArray();
            for (int i = 0; i < chs.length; i++) {
                char old = chs[i];

                for (char c = 'a'; c <= 'z'; c++) {
                    chs[i] = c;
                    String word = new String(chs);

                    // 如果转换后的字符串存在于字典中
                    if (dict.contains(word)) {
                        // 如果当前单词就是endWord
                        if (endWord.equals(word)) {
                            finish = true;
                        } else { // 添加到转换序列中下一个位置的字符串集合中
                            tmp.add(word);
                        }

                        /* 使用当前的映射关系来扩展图（逐步构建图） */
                        // 如果当前单词的映射关系还没有存储到 map中...
                        if (map.get(s) == null) {
                            map.put(s, new ArrayList<>());
                        }

                        // 添加 s -> word的映射关系
                        map.get(s).add(word);
                    }
                }

                chs[i] = old;
            }
        }

        /* Ⅲ 判断图是否已经扩展完成，如果没有，使用bfs继续进行扩展 */
        // 如果上述循环中，finish仍旧为false aka 还没有找到endWord
        if (!finish) {
            // 则：继续做bfs来建立图（查找到endWord时，图就建立完成了）   此时的startSet的值就是tmp了 aka 图中的下一个level
            bfs(tmp, endWord, map, dict);
        }

        // 如果finish为true，则：方法结束，图已经创建完成
    }
} // this method failed, I don't know why. but let's skip to next method first
// super stupid：原因是因为传入的beginWord 与 endWord参数传成了空字符串