package com.henry.leetcode_traning_camp.week_04.day03.solitaire_02;

import java.util.*;

public class Solution_findLadders_via_liweiwei {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> availableWordVariants =
                new ArrayList<>(
                        Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")
                );

        List<List<String>> allShortestPathList = findLadders(beginWord, endWord, availableWordVariants);
        System.out.println("最终的结果为：" + allShortestPathList);
    }

    private static List<List<String>> findLadders(String beginWord,
                                                  String endWord,
                                                  List<String> availableWordVariants) {
        // 先将 wordList 放到哈希表里，便于判断某个单词是否在 wordList 里
        Set<String> availableWordSet = new HashSet<>(availableWordVariants);
        List<List<String>> allShortestPathList = new ArrayList<>();

        if (availableWordSet.size() == 0 || !availableWordSet.contains(endWord)) {
            return allShortestPathList;
        }

        // 第 1 步：使用广度优先遍历得到后继结点列表 successors
        // key：字符串; value：广度优先遍历过程中 key 的后继结点列表
        Map<String, Set<String>> successors = new HashMap<>();
        boolean foundEndWord = buildTheMapViaBFS(beginWord, endWord, availableWordSet, successors);
        if (!foundEndWord) {
            return allShortestPathList;
        }

        // 第 2 步：基于后继结点列表 successors ，使用回溯算法得到所有最短路径列表
        Deque<String> path = new ArrayDeque<>();
        path.addLast(beginWord);
        dfs(beginWord, endWord, successors, path, allShortestPathList);
        return allShortestPathList;
    }

    private static void dfs(String beginWord, String endWord, Map<String, Set<String>> successors, Deque<String> path, List<List<String>> res) {
        if (beginWord.equals(endWord)) {
            res.add(new ArrayList<>(path));
            return;
        }

        if (!successors.containsKey(beginWord)) {
            return;
        }

        Set<String> successorWords = successors.get(beginWord);
        for (String nextWord : successorWords) {
            path.addLast(nextWord);
            dfs(nextWord, endWord, successors, path, res);
            path.removeLast();
        }
    }

    private static boolean buildTheMapViaBFS(String beginWord,
                                             String endWord,
                                             Set<String> availableWordSet,
                                             Map<String, Set<String>> wordToItsSuccessorsMap) {
        Queue<String> beginWordsOnCurrentLevel = new LinkedList<>();
        beginWordsOnCurrentLevel.offer(beginWord);

        // 标准写法，记录方法问过的单词
        Set<String> visitedVertexSet = new HashSet<>();
        visitedVertexSet.add(beginWord);

        boolean doesEndWordFound = false;
        int beginWordLength = beginWord.length();

        // 当前层访问过的结点，当前层全部遍历完成以后，再添加到总的 visitedVertexSet 集合里
        Set<String> nextLevelVisited = new HashSet<>(); // 为什么要设置这个变量,具体什么作用?? 不好理解... 😳

        while (!beginWordsOnCurrentLevel.isEmpty()) {
            int wordAmountOnCurrentLevel = beginWordsOnCurrentLevel.size();

            for (int currentWordCursor = 0; currentWordCursor < wordAmountOnCurrentLevel; currentWordCursor++) {
                String currentBeginWord = beginWordsOnCurrentLevel.poll();
                char[] currentBeginWordArr = currentBeginWord.toCharArray();

                for (int currentCharacterCursor = 0; currentCharacterCursor < beginWordLength; currentCharacterCursor++) {
                    char originCharacter = currentBeginWordArr[currentCharacterCursor];

                    for (char optionalChar = 'a'; optionalChar <= 'z'; optionalChar++) {
                        if (currentBeginWordArr[currentCharacterCursor] == optionalChar) {
                            continue;
                        }
                        // 使用 可选的字符 来 替换原始字符
                        currentBeginWordArr[currentCharacterCursor] = optionalChar;
                        // 得到替换后的字符串
                        String transformedWord = new String(currentBeginWordArr);
                        // 如果 转换后的字符串 存在于 可用的字符变体集合中，说明 得到了一个 转换的中间单词
                        if (availableWordSet.contains(transformedWord)) {
                            // 如果 该中间单词还没有 被访问过，说明 ???，则：
                            if (!visitedVertexSet.contains(transformedWord)) {
                                // 判断 该中间单词 是不是 与目标单词相等，如果是，说明 找到了目标单词，则：
                                if (transformedWord.equals(endWord)) {
                                    // 更新flag变量的值
                                    doesEndWordFound = true;
                                }

                                // 避免下层元素重复加入队列，这里感谢 https://leetcode-cn.com/u/zhao-da-ming/ 优化了这个逻辑
                                if (!nextLevelVisited.contains(transformedWord)) {
                                    beginWordsOnCurrentLevel.offer(transformedWord);
                                    nextLevelVisited.add(transformedWord);
                                }

                                // 维护 successors 的定义
                                wordToItsSuccessorsMap.computeIfAbsent(currentBeginWord, a -> new HashSet<>());
                                wordToItsSuccessorsMap.get(currentBeginWord).add(transformedWord);
                            }
                        }
                    }
                    currentBeginWordArr[currentCharacterCursor] = originCharacter;
                }
            }

            if (doesEndWordFound) {
                break;
            }
            visitedVertexSet.addAll(nextLevelVisited);
            nextLevelVisited.clear();
        }
        return doesEndWordFound;
    }
} // 感觉上weiwei的解法还是有点复杂 关键没有配套视频说明
// 做法：熟悉一下图的数据结构表示————邻接表。以及创建图的BFS & 遍历图的DFS
