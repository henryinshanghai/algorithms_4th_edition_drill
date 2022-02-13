package com.henry.leetcode_traning_camp.week_04.day03;

import java.util.*;

public class Solution_findLadders_03_method01_backtrack_happygirllzt_drill01_with_optimization {
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
        // build the graph
        List<List<String>> res = new ArrayList<>();
        Set<String> dict = new HashSet<>(wordList);

        if (!dict.contains(endWord)) {
            return res;
        }

        // 做bfs
        // 准备一个map  用于存储图中节点的映射关系
        Map<String, List<String>> map = new HashMap<>();

        // 准备startSet
        Set<String> startSet = new HashSet<>();
        startSet.add(beginWord);

        /* 优化：使用两个Set来减少建立映射关系的操作 */
        Set<String> endSet = new HashSet<>();
        endSet.add(endWord);

        // 编写完成bfs之后，再在此处完成实际参数的填入
//        bfs(startSet, endWord, map, dict);
        // 优化后，传入到bfs的参数为endSet，而不是endWord
        bfs(startSet, endSet, map, dict, false); // 添加一个新的参数 用于表示是否reverse当前的startSet与endSet


        System.out.println("--------- 创建得到的图为 ----------" );
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }

        /* map/graph创建完成后，使用map来生成结果 */

        // 准备一个list     用于存储beginWord
        List<String> list = new ArrayList<>(); // 为什么这里使用的是list而不是set呢？
        list.add(beginWord);

        dfs(res, list, endWord, beginWord, map);

        return res;
     }

    private static void dfs(List<List<String>> res,
                            List<String> list,
                            String endWord,
                            String word,
                            Map<String, List<String>> map) { // 这里的参数可能不完整，所以边写边调整
        if (word.equals(endWord)) {
            res.add(new ArrayList(list)); // 注：添加当前list的副本
            return;
        }

        // 如果map中，当前单词已经没有可以转化的单词了 说明已经到了图的末端节点 则：直接返回
        if (map.get(word) == null) return;
        for (String next : map.get(word)) {
            list.add(next); // 这里的list就是待查找的路径
            dfs(res, list, endWord, next, map);
            list.remove(list.size() - 1);
        }
    } // 方法完成后，回到方法的调用处传入实际参数

    /**
        Set<String> startSet,
        String endWord,
        Map<String, List<String>> map,
        Set<String> dict      //旧的参数列表
     * @param startSet
     * @param endSet
     * @param map
     * @param dict
     */
    private static void bfs(Set<String> startSet,
                            Set<String> endSet,
                            Map<String, List<String>> map,
                            Set<String> dict,
                            boolean reverse) {
        // startSet为空，说明xxx，则：直接返回
        if (startSet.size() == 0) return;

        // 如果startSet中的单词个数比endSet中的单词个数要大
        if (startSet.size() > endSet.size()) {
            // 则：1 交换startSet 与 endSet的传参位置； 2 传入正确的reverse标识 
            bfs(endSet, startSet, map, dict, !reverse);
            return; // 执行完成后，返回上一级调用
        }

        // 准备一个set  用于存储下一个level中的单词集合
        Set<String> tmp = new HashSet<>();

        // at the very beginning of bfs, you gotta remove startSet from dict to avoid unexpected reuse of words
        dict.removeAll(startSet); // 否则会出现StackOverFlow的错误

        // 准备一个布尔类型变量   用于判断是否已经到结尾？？？
        boolean finish = false;

        for (String s : startSet) {
            char[] chs = s.toCharArray();
            for (int i = 0; i < chs.length; i++) {
                char old = chs[i];

                for (char c = 'a'; c <= 'z'; c++) {
                    chs[i] = c;
                    String word = new String(chs);

                    if (dict.contains(word)) {
                        if (endSet.contains(word)) { // endWord.equals(word)
                            finish = true;
                        } else {
                            tmp.add(word); // 图中的下一层
                        }

                        /* endSet 与 startSet调换后，单词间的映射关系也发生了一些变化 */
                        // 如果reverse为true,则：映射关系中的key = 根据当前单词找到的转换结果
                        // 如果reverse为false，则：映射关系中的key = 当前单词
                        String key = reverse ? word : s;
                        String val = reverse ? s : word;

//                        // 图中还没有存储过s节点
//                        if (map.get(s) == null) {
//                            map.put(s, new ArrayList<>());
//                        }
//                        // 把s节点到word的映射关系存储到图中
//                        map.get(s).add(word);

                        if (map.get(key) == null) {
                            map.put(key, new ArrayList<>());
                        }
                        map.get(key).add(val);
                    }
                }

                // 恢复字符串当前位置的字符
                chs[i] = old;
            }
        }

        // 如果没有结束，就继续进行bfs
        if (!finish) {
            // 此时的startSet是tmp 因为已经进入到下一个level
//            bfs(tmp, endWord, map, dict);
            bfs(tmp, endSet, map, dict, reverse);
        }
    }
}
