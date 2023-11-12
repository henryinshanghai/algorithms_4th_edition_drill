package com.henry.string_05.string_sorting_01.LSD_02;

// 原理：键索引计数法的稳定性 + 归纳法； 组间排序 & 组内排序
// 基础原理：使用字符作为键时，字符在字符表中的顺序是确定的。
public class LSDLite {

    // 使用低位优先的方式，对定长字符串的集合进行排序
    public static void sort(String[] plateArr, int plateLength) {
        // 通过前W个字符，把a[]排序
        int plateAmount = plateArr.length;
        int biggestGroupNoPlus1 = 256; // 最大组号为255

        String[] aux = new String[plateAmount];

        for (int backwardsCursor = plateLength - 1; backwardsCursor >= 0; backwardsCursor--) {
            /* #1  统计各个组号出现的频率,得到 groupNoPlus1ToGroupSizeArr[] */
            // 使用第 backwardsCursor 个字符，来 作为 “键索引计数法排序中的groupNo/键”
            int[] groupNoToItsStartIndexArr = new int[biggestGroupNoPlus1 + 1]; // 键->索引的数组初始大小为 最大组号/key + 1 + 1

            for (int currentSlot = 0; currentSlot < plateAmount; currentSlot++) {
                // 获取到待排序元素的groupNo - 使用：当前字符 来 作为groupNo
                char groupNoOfCurrentPlate = plateArr[currentSlot].charAt(backwardsCursor);
                // 统计 当前组号中元素的总数量，并把结果 放置在 groupNo+1的位置上 - 为了方便地在#2中把频率转化为索引
                groupNoToItsStartIndexArr[groupNoOfCurrentPlate + 1]++;
            }

            // #2 把“组号出现的频率”转化为“该组号在最终排序结果中的起始索引”, 得到 groupNoToItsStartIndexArr[]
            for (int currentGroupNo = 0; currentGroupNo < biggestGroupNoPlus1; currentGroupNo++) {
                groupNoToItsStartIndexArr[currentGroupNo + 1] += groupNoToItsStartIndexArr[currentGroupNo];
            }

            // #3 得到有序的辅助数组aux
            for (int currentSlot = 0; currentSlot < plateAmount; currentSlot++) {
                // 当前位置上的plate，应该放置在aux的什么位置上？ startIndex上
                char groupNoOfCurrentPlate = plateArr[currentSlot].charAt(backwardsCursor);
                int startIndexOfCurrentPlate = groupNoToItsStartIndexArr[groupNoOfCurrentPlate];
                aux[startIndexOfCurrentPlate] = plateArr[currentSlot];
                // 使用后，把startIndex+1
                groupNoToItsStartIndexArr[groupNoOfCurrentPlate]++;
            }

            // #4 把辅助数组中的元素，回写到原始数组中
            for (int currentSlot = 0; currentSlot < plateAmount; currentSlot++) {
                plateArr[currentSlot] = aux[currentSlot];
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
        sort(plateNumberArr, 7);

        for (String plateNumber : plateNumberArr) {
            System.out.println(plateNumber);
        } // 结果正确
    }
}
