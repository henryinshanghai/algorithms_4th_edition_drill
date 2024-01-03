package com.henry.string_05.string_sorting_01.LSD_02;

// 原理：键索引计数法的稳定性 + 归纳法； 组间排序 & 组内排序
// 基础原理：使用字符作为键时，字符在字符表中的顺序是有序的。
// 手段：把字符串中的某一个字符 来 作为“键索引计数法”中的“索引”（组号），而把 字符串本身 作为“键”（学生名字）
// 适用情况：序列中的各个字符串，长度都相等的情况（比如车牌号 plate number）
public class LSDLite {

    // 使用低位优先的方式，对“定长字符串”的集合进行排序
    public static void sortStringsWithFixedLength(String[] plateStrArr, int plateLength) {
        // 通过前W个字符，把a[]排序
        int plateAmount = plateStrArr.length;
        int biggestIndexPlus1 = 256; // 使用“字符”作为“索引”时，最大索引为255（ASCII码表中的256个字符）

        String[] aux = new String[plateAmount];

        for (int characterBackwardsCursor = plateLength - 1; characterBackwardsCursor >= 0; characterBackwardsCursor--) {
            /* #1  统计各个组号出现的频率,得到 groupNoPlus1ToGroupSizeArr[] */
            // 使用第 characterBackwardsCursor 个字符，来 作为 “键索引计数法排序中的groupNo/键”
            int[] indexToItsStartSpotInResultSequence = new int[biggestIndexPlus1 + 1]; // 键->索引的数组初始大小为 最大组号/key + 1 + 1

            for (int currentSpot = 0; currentSpot < plateAmount; currentSpot++) {
                // 获取到待排序元素的 index - 使用“当前车牌号”的“当前字符” 来 作为 index
                char indexOfCurrentPlate = plateStrArr[currentSpot].charAt(characterBackwardsCursor);
                // 统计 当前组号中元素的总数量，并把结果 放置在 groupNo+1的位置上 - 为了方便地在#2中把频率转化为索引
                indexToItsStartSpotInResultSequence[indexOfCurrentPlate + 1]++;
            }

            // #2 把“组号出现的频率”转化为“该组号在最终排序结果中的起始索引”, 得到 indexToItsStartSpotInResultSequence[]
            for (int currentIndex = 0; currentIndex < biggestIndexPlus1; currentIndex++) {
                // 递推公式：当前元素的值 = 当前元素的“当前值” + “其前一个元素”的值
                indexToItsStartSpotInResultSequence[currentIndex + 1] += indexToItsStartSpotInResultSequence[currentIndex];
            }

            // #3 得到有序的辅助数组aux
            for (int currentSpot = 0; currentSpot < plateAmount; currentSpot++) {
                // #1 获取到 “当前元素”在“结果序列”中的“正确的排定位置”
                // “原始序列”中 当前位置上的plate，应该放置在“结果序列”的什么位置上？ 答：startIndex上
                char indexOfCurrentPlate = plateStrArr[currentSpot].charAt(characterBackwardsCursor);
                int startSpotOfCurrentPlate = indexToItsStartSpotInResultSequence[indexOfCurrentPlate];

                // #2 获取到“索引中第一个元素”应该排定的位置后，为该位置绑定“当前元素”
                aux[startSpotOfCurrentPlate] = plateStrArr[currentSpot];

                // #3 更新“索引中的元素所应该排定的位置” 来 正确排定下一个“索引中的元素”
                indexToItsStartSpotInResultSequence[indexOfCurrentPlate]++;
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

        // 对车牌号进行排序
        sortStringsWithFixedLength(plateNumberArr, 7);

        for (String plateNumber : plateNumberArr) {
            System.out.println(plateNumber);
        } // 结果正确
    }
}
