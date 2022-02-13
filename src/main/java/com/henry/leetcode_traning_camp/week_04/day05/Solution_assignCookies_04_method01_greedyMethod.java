package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.Arrays;

public class Solution_assignCookies_04_method01_greedyMethod {
    public static void main(String[] args) {
        int[] children = {1, 2};
        int[] cookies = {1, 2, 3};

        int contentChildrenNumber = findContentChildren(children, cookies);

        System.out.println("当前饼干集合最多能够满足的孩子数量为： " + contentChildrenNumber);

    }

    private static int findContentChildren(int[] children, int[] cookies) {
        Arrays.sort(children);
        Arrays.sort(cookies);

        int contentChildren = 0;

        // 准备pointer
        int i = children.length - 1;
        int j = cookies.length - 1;

        while (i >= 0 && j >= 0) {
            if (cookies[j] >= children[i]) {
                contentChildren++;
                i--;
                j--;
            } else {
                i--; // 移到下一个胃口大的小孩
            }
        }

        return contentChildren;
    }
}
// 参考：Kevin Naughton Jr.
