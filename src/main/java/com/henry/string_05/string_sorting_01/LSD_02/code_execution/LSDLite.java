package com.henry.string_05.string_sorting_01.LSD_02.code_execution;

// 原理：键索引计数法的稳定性 + 归纳法； 组间排序 & 组内排序
// 基础原理：使用字符 作为键 时，字符 在字符表中的顺序 是有序的。
// 手段：把 字符串中的某个位置上的字符 来 作为“键索引计数法”中的“键”（组号），而把 字符串本身 作为“待排列的元素”（学生名字）
// 适用情况：序列中的 所有字符串元素的长度 都相等的情况（比如车牌号 plate number）
public class LSDLite {

    // 使用 低位优先 的方式，对 “定长字符串”的集合 进行排序
    public static void sortStringsWithFixedLength(String[] plateStrArr, int plateLength) {
        // 通过 前W个字符，把a[]排序
        int plateAmount = plateStrArr.length;
        int biggestGroupNo = 256; // 使用“字符”作为“键”时，键的可能选项数量为255（因为 ASCII码表中 有256个字符）

        String[] aux = new String[plateAmount];

        for (int characterBackwardsCursor = plateLength - 1; characterBackwardsCursor >= 0; characterBackwardsCursor--) {
            /* #1  统计 各个组号出现的频率 来 得到 groupNoPlus1ToGroupSizeArr[] */
            // 使用第 characterBackwardsCursor 个字符，来 作为 “键索引计数法排序中的groupNo/键”
            int[] groupNoToItsStartSpotInResultSequence = new int[biggestGroupNo + 1]; // 键->索引的数组初始大小为 最大组号/key + 1 + 1

            for (int currentSpot = 0; currentSpot < plateAmount; currentSpot++) {
                // 获取到 待排序元素的key - 使用“当前车牌号”的“当前位置上的字符” 来 作为 key
                char groupNoOfCurrentPlate = plateStrArr[currentSpot].charAt(characterBackwardsCursor);
                // 统计 当前组号中元素的总数量，并 把结果 放置在 groupNo+1的位置上 - 为了能够 方便地 在#2中 把频率转化为索引
                groupNoToItsStartSpotInResultSequence[groupNoOfCurrentPlate + 1]++;
            }

            // #2 把 “组号出现的频率” 转化为 “该组号在最终排序结果中的起始索引”, 得到 groupNoToItsStartSpotInResultSequence[]
            for (int currentGroupNo = 0; currentGroupNo < biggestGroupNo; currentGroupNo++) {
                // 递推公式：当前元素的值 = 当前元素的“当前值” + “其前一个元素”的值
                groupNoToItsStartSpotInResultSequence[currentGroupNo + 1] += groupNoToItsStartSpotInResultSequence[currentGroupNo];
            }

            // #3 得到 有序的 辅助数组aux
            for (int currentSpot = 0; currentSpot < plateAmount; currentSpot++) {
                // #1 获取到 “当前元素” 在“结果序列”中 的“应该被排定到的正确位置”
                // “原始序列”中 当前位置上的plate，应该 放置在 “结果序列”的什么位置 上呢？ 答：startIndex上
                char groupNoOfCurrentPlate = plateStrArr[currentSpot].charAt(characterBackwardsCursor);
                int startSpotOfCurrentGroup = groupNoToItsStartSpotInResultSequence[groupNoOfCurrentPlate];

                // #2 获取到 “组中的第一个元素” 应该被排定到的位置👆后，为 该位置 绑定“当前元素”
                aux[startSpotOfCurrentGroup] = plateStrArr[currentSpot];

                // #3 更新 该组元素“应该被排定到的正确位置” 来 正确排定下一个“该组中的元素”
                groupNoToItsStartSpotInResultSequence[groupNoOfCurrentPlate]++;
            }

            // #4 把辅助数组中的元素，回写到原始数组中
            for (int currentSlot = 0; currentSlot < plateAmount; currentSlot++) {
                plateStrArr[currentSlot] = aux[currentSlot];
            }
        }
    }

    public static void main(String[] args) {
        String[] plateNumberArr = {
                "4PGC398",
                "2IYE230",
                "3CIO720",
                "1ICK750",
                "1OHV845",
                "4JZY524",
                "1ICK750",
                "3CIO720",
                "1OHV845",
                "1OHV845",
                "2RLA629",
                "2RLA629",
                "3ATW723"
        };

        // 对车牌号进行排序，排序的结果是 字符串 被按照字母表顺序来排序
        sortStringsWithFixedLength(plateNumberArr, 7);

        for (String plateNumber : plateNumberArr) {
            System.out.println(plateNumber);
        } // 结果正确
    }
}
