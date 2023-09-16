package com.henry.basic_chapter_01.algorithm_analysis.observe_02.data_analysis;

import com.henry.basic_chapter_01.algorithm_analysis.observe_02.elapsed_time.StopWatch;
import com.henry.basic_chapter_01.algorithm_analysis.observe_02.time_elapse_and_problem_scale.ThreeSum;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

// 验证：在输入规模倍增的情况下，ThreeSum的运行时长会增长地更多
// 手段：DoublingTest

/**
 *    250   0.0
 *    500   0.1
 *    1000   0.1
 *    2000   0.9
 *    4000   5.5
 *    8000  38.1
 */
public class DoublingTest {
    public static void main(String[] args) {
        // 逐步倍增问题的规模N
        for (int N = 250; true; N += N) {
            double time = timeTrial(N);
            StdOut.printf("%7d %5.1f\n", N, time);
        }
    }

    // ⏲ ThreeSum在处理 N个（问题规模）随机的六位整数时的耗时
    public static double timeTrial(int N) {
        int MAX = 1000000;
        int[] numArray = new int[N];

        // 构造出随机整数组成的数组
        for (int cursor = 0; cursor < N; cursor++) {
            numArray[cursor] = StdRandom.uniform(-MAX, MAX);
        }

        StopWatch timer = new StopWatch();
        // 对数组中满足条件的三元组进行计数
        int count = ThreeSum.count(numArray);

        // 返回计数工作所花费的时间
        return timer.elapsedTime();
    }
}
