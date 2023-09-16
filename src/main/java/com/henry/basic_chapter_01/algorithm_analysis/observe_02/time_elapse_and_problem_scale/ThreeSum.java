package com.henry.basic_chapter_01.algorithm_analysis.observe_02.time_elapse_and_problem_scale;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// 验证：指定程序中的命令行参数的手段 - edit configuration -> program argument
// 特征：如果 working directory具体到当前文件所在的文件夹，则：可以直接用文件名作为参数
// 结果：随着文件不断变大，程序的运行时间显著变长
/**
 *  % java ThreeSum 1Kints.txt
 *  70
 *
 *  % java ThreeSum 2Kints.txt
 *  528
 *
 *  % java ThreeSum 4Kints.txt
 *  4039
 */
public class ThreeSum {
    public static void main(String[] args) {
        // 统计文件中 所有 和为0的元素 的数量
        int[] numberArray = In.readInts(args[0]);
        StdOut.println(count(numberArray));
    }


    public static int count(int[] numberArray) {
        int numberAmount = numberArray.length;
        int legitTripleCount = 0;

        for (int firstCursor = 0; firstCursor < numberAmount; firstCursor++) {
            for (int secondCursor = firstCursor + 1; secondCursor < numberAmount; secondCursor++) {
                for (int thirdCursor = secondCursor + 1; thirdCursor < numberAmount; thirdCursor++) {
                    if (numberArray[firstCursor] + numberArray[secondCursor] + numberArray[thirdCursor] == 0) {
                        legitTripleCount++;
                    }
                }
            }
        }

        return legitTripleCount;
    }
}
