package com.henry.leetcode_traning_camp.week_02.day1;

public class tableSizeForDemo {
    final static Integer MAXIMUM_CAPACITY = 1000;

    public static void main(String[] args) {
        int givenCapacity = 10;
        int generateCapacity = tableSizeFor(givenCapacity);
        System.out.println("方法返回的整数为： " + generateCapacity);

    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
