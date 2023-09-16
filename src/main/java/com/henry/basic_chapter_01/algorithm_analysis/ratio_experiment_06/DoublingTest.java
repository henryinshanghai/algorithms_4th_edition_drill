package com.henry.basic_chapter_01.algorithm_analysis.ratio_experiment_06;

import com.henry.basic_chapter_01.algorithm_analysis.observe_02.elapsed_time.StopWatch;
import com.henry.basic_chapter_01.algorithm_analysis.observe_02.time_elapse_and_problem_scale.ThreeSum;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class DoublingTest {

    public static double timeTrial(int N) {
        // 对~进行⏲ - 处理N个随机的六位整数的ThreeSum.count()
        int MAX = 1000000;
        int[] a = new int[N];

        for (int cursor = 0; cursor < N; cursor++) {
            a[cursor] = StdRandom.uniform(-MAX, MAX);
        }

        StopWatch timer = new StopWatch();
        int count = ThreeSum.count(a);
        return timer.elapsedTime();
    }

    public static void main(String[] args) {
        // 打印出 关于运行时间的表格
        for (int N = 250; true; N += N) {
            // 打印问题规模为N时，程序运行的时长
            double time = timeTrial(N);
            StdOut.printf("%7d %5.1f\n", N, time);
        }
    }
}
