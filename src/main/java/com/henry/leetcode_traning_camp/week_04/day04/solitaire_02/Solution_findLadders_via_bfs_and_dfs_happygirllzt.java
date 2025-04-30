package com.henry.leetcode_traning_camp.week_04.day04.solitaire_02;

import java.util.*;

// 验证：可以使用BFS + DFS的手段 来 得到由“起始单词”经由“有效的单词变体 中间结果”得到“目标单词”的 所有具体转换序列
// 原理：BFS（递归实现） 用于 构建图中 word -> itsValidWordVariants 的映射关系；
// DFS（递归实现）+回溯 用于 图中所存在的“到目标单词的转换序列”。
// 🐖 DFS/BFS的基本作用是：找到图中与指定顶点相连通的所有顶点
public class Solution_findLadders_via_bfs_and_dfs_happygirllzt {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> availableWordVariantList =
                new ArrayList<>(
                        Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")
                );

        List<List<String>> allShortestTransformSequences = findLadders(beginWord, endWord, availableWordVariantList);
        System.out.println("最终的结果为：" + allShortestTransformSequences);
    }

    private static List<List<String>> findLadders(String beginWord,
                                                  String endWord,
                                                  List<String> availableValidWordVariantList) {
        // 准备一个 元素为list的list对象 - 用于存放所有找到的 “最短转换路径”
        List<List<String>> allWantedPathList = new ArrayList<>();

        // 把wordList转化成为一个set对象 用于更快速地判断 是否存在重复元素
        Set<String> availableWordVariantSet = new HashSet<>(availableValidWordVariantList);

        // 鲁棒性代码 - endWord一定要出现在 可选的单词变体集合中，否则 不存在任何的转换序列，则：
        if (!availableWordVariantSet.contains(endWord)) {
            // 直接return空列表
            return allWantedPathList;
        }

        /* #1 使用bfs 来 构建出图/(word -> itsTransformedWordVariants 一对多映射关系) */
        Map<String, List<String>> wordToItsTransformedVariantsMap = new HashMap<>();
        // 准备一个集合(用于存储所有的“起始单词”), 然后把当前的beginWord放进去
        Set<String> startWordSet = new HashSet<>();
        startWordSet.add(beginWord);

        // 构建出 对应的无权无向图
        generateTheMapViaBFS(startWordSet,
                endWord,
                wordToItsTransformedVariantsMap, // map参数的作用：在方法执行的过程中被构建
                availableWordVariantSet);

        /* #2 查看下map对象|图 是否 “按照预期被创建” */
        printThe(wordToItsTransformedVariantsMap);

        /* #3 使用DFS+回溯 来 从图中得到 所有可能的“到目标单词的转换序列” */
        // 准备一个path（用于 收集“到目标单词的转换序列”中的所有单词）然后把beginWord放进去
        List<String> pathTowardsEndWord = new ArrayList<>();
        pathTowardsEndWord.add(beginWord);

        // 使用dfs来生成结果
        generatePathListViaDFS(beginWord,
                endWord,
                wordToItsTransformedVariantsMap,
                pathTowardsEndWord,
                allWantedPathList
        );

        return allWantedPathList;

    }

    // 打印出所有 word -> its transformed variants的映射关系
    private static void printThe(Map<String, List<String>> wordToItsTransformedWordVariants) {
        if (wordToItsTransformedWordVariants == null) return;

        for (Map.Entry<String, List<String>> currentWordToItsTransformedWordVariants : wordToItsTransformedWordVariants.entrySet()) {
            String currentWord = currentWordToItsTransformedWordVariants.getKey();
            List<String> itsTransformedWordVariants = currentWordToItsTransformedWordVariants.getValue();

            System.out.print("Key - " + currentWord + " | ");
            System.out.println("Value - " + itsTransformedWordVariants);
        }
    }

    /**
     * 在构建好的图中，找到 所需要的路径 pathTowardsEndWord，并把它添加到 pathList中
     */
    private static void generatePathListViaDFS(String beginWord,
                                               String endWord,
                                               Map<String, List<String>> wordsToItsTransformedVariantsMap,
                                               List<String> pathTowardsEndWord,
                                               List<List<String>> allWantedPathList) {
        // Ⅰ 如果起始单词 与 目标单词 相同，说明
        // #1 要么 不需要进行任何转换 就得到了目标单词(特殊情况) OR
        // #2 要么 转换序列终于转换到了 目标单词(一般情况)，则：
        if (beginWord.equals(endWord)) {
            // 把 当前到endWord的路径（而不是目标单词本身） 添加到 结果列表中
            allWantedPathList.add(new ArrayList(pathTowardsEndWord)); // 因为path本身会被回溯而变化，所以我们使用的是一个新的副本
            // 即刻返回 以 停止当前级方法的继续调用，返回到上一级
            return;
        }

        // Ⅱ 如果“当前起始单词” 不存在 任何“有效的单词变体”，说明 由当前起始单词经“有效单词变体” 无法转变成为“目标单词”，则：
        if (wordsToItsTransformedVariantsMap.get(beginWord) == null) {
            // 直接return 以 停止当前级方法的调用
            return;
        }

        // Ⅲ 如果“当前起始单词” 存在有 一堆的“有效的单词变体”，说明 由当前起始单词 经“有效单词变体” 有可能转变得到“目标单词”，则：
        // 遍历每一个“有效的单词变体”
        for (String currentTransformedWordVariant : wordsToItsTransformedVariantsMap.get(beginWord)) {
            // ① 把 当前“有效的单词变体” 添加到 “由起始单词转变到目标单词的路径”中
            pathTowardsEndWord.add(currentTransformedWordVariant);

            // ② 调用dfs 以 得到完整的“到目标单词的路径”
            generatePathListViaDFS(currentTransformedWordVariant, // 子问题：此参数发生了变化
                    endWord,
                    wordsToItsTransformedVariantsMap,
                    pathTowardsEndWord, // path参数也发生了变化
                    allWantedPathList);

            // ③ 回溯当前选择 以便选择下一个“有效的单词变体” 来 构造“到目标单词的路径”
            pathTowardsEndWord.remove(pathTowardsEndWord.size() - 1);
        }
    }

    /**
     * 每一个level的单词会越来越多，所以使用一个startSet作为参数
     * 一个endWord参数
     * 还需要一个映射关系map：描述一个单词所能转换到的其他有效单词
     * 特征：这个BFS并没有使用到Queue
     */
    private static void generateTheMapViaBFS(Set<String> startWordSet,
                                             String endWord,
                                             Map<String, List<String>> wordToItsTransformedVariantsMap,
                                             Set<String> availableWordVariantSet) {
        /* 〇 为正确地扩展出图map做一些准备工作 */
        // 如果startSet的size为0，说明 图已经没有下一层顶点了（aka 图已经创建完成），则：
        if (startWordSet.size() == 0) {
            // 直接return 跳出方法
            return;
        }

        // 准备一个tempSet  用于存储序列中下一个位置可能的单词 | 图中下一层的所有结点（aka 下一层顶点集合）
        Set<String> startWordSetOnNextLevel = new HashSet<>();

        // 准备一个布尔值  用于判断转换序列是不是已经到达了结尾 | 图是否已经构建完成
        boolean isTransformFinished = false;

        // Ⅰ 先从 “可用的单词变体集合”中，移除 当前的“起始单词集合” 来 避免图中出现环??
        availableWordVariantSet.removeAll(startWordSet);

        /* Ⅱ 准备一个循环 在当前level上对图进行扩展 */
        // 遍历startWordSet中的每一个单词，找到其 所有可用的转换结果 #1 添加到下一层中；#2 并构建图map；/或者直接返回
        for (String currentStartWordStr : startWordSet) {
            // 列举当前单词可能转换到的 所有可用的转换结果
            char[] currentStartWordCharacterArr = currentStartWordStr.toCharArray();

            // 对于“当前起始单词”中的每一个位置...
            for (int currentSpotToReplace = 0; currentSpotToReplace < currentStartWordCharacterArr.length; currentSpotToReplace++) {
                // 获取到 当前位置上的字符
                char originalCharacter = currentStartWordCharacterArr[currentSpotToReplace];

                // 尝试使用[a-z]范围内的所有字符 对其逐一替换，以得到“所有可能的单词变体”
                for (char replacedCharacterOption = 'a'; replacedCharacterOption <= 'z'; replacedCharacterOption++) {
                    // 替换字符
                    currentStartWordCharacterArr[currentSpotToReplace] = replacedCharacterOption;
                    // 得到一个“替换字符后的单词变体”
                    String replacedCharacterWordVariant = new String(currentStartWordCharacterArr);

                    // 如果“替换字符后的字符串”存在于“有效的单词变体集合”中，说明 找到了一个 结果序列中的“转换中间结果”，则：
                    // 🐖 这就是为什么我们需要 先移除startWordSet，否则 我们无法据此判断是不是找到了一个“转换中间结果”
                    if (availableWordVariantSet.contains(replacedCharacterWordVariant)) {
                        /* #1 把当前“转换中间结果” 添加到 下一层的“起始单词”集合中 */
                        // 如果当前“替换字符后的单词变体” 就是 “目标单词”/“结束单词”，说明 已经得到了完整的转换序列，不需要继续转换，则：
                        if (endWord.equals(replacedCharacterWordVariant)) {
                            // 把 标记转换是否结束的变量 设置为true
                            isTransformFinished = true;
                        } else { // 否则，说明转换序列还没有结束，则：
                            // 把 这个“转换中间结果” 添加到 表示“图中下一层结点”的set对象中 来 为扩展图的下一层做准备
                            startWordSetOnNextLevel.add(replacedCharacterWordVariant);
                        }

                        /* #2 把 “当前startWord” -> 当前“转换中间结果” 的映射 添加到 用于存储转换映射关系的map对象 中 */
                        // #2-① 如果缺失条目，则先新增条目
                        // 🐖 computeIfAbsent() 用来 替换 “if(null) {}”的语句块
                        wordToItsTransformedVariantsMap.computeIfAbsent(currentStartWordStr, key -> new ArrayList<>());
                        // #2-② 再向映射关系的该条目中，添加其所能转换到item
                        wordToItsTransformedVariantsMap.get(currentStartWordStr).add(replacedCharacterWordVariant);
                    }
                }

                // 恢复当前位置上的字符，以便在其他位置继续尝试通过转换 来 得到其他的中间结果或最终结果
                currentStartWordCharacterArr[currentSpotToReplace] = originalCharacter;
            }
        }

        /* Ⅲ 判断图/Map是否已经扩展完成，如果没有，则：使用bfs继续进行下一层的扩展 */
        // 经过上述循环后，如果finish仍旧为false，说明 仍旧没有找到endWord，则：
        if (!isTransformFinished) {
            // 继续构建图 / map（当查找到endWord时，图就建立完成了）
            generateTheMapViaBFS(startWordSetOnNextLevel, // 🐖 此时 startWordSet 的值就是 startWordSetOnNextLevel了 - 也就是图中 下一个level的所有结点
                    endWord,
                    wordToItsTransformedVariantsMap,
                    availableWordVariantSet);
        }
        // 如果finish为true，则：方法结束，图已经创建完成
    }
}