package com.henry.sort_chapter_02.creative_exercies.kendall_tau_distance;

import java.util.Random;

public class ArrayUtil {

    // 打乱数组中的元素（洗牌）
    public static void shuffle(int[] a) {
        int itemAmount = a.length;
        // 对于当前位置...
        for (int currentSpot = 0; currentSpot < itemAmount; currentSpot++) {
            // #1 为它找到一个随机的位置 作为交换位置 - 实现随机的手段：random.nextInt(N)
            int spotToSwap = currentSpot + uniform(itemAmount - currentSpot);

            // #2 把两个位置上的元素进行交换 来 实现打乱的操作
            int temp = a[currentSpot];
            a[currentSpot] = a[spotToSwap];
            a[spotToSwap] = temp;
        }
    }

    // 获取区间[0, N-1]中的一个随机整数
    private static int uniform(int N) {
        Random random = new Random();
        return random.nextInt(N);
    }

    // 获取到数组中存在的“逆序对”的数量
    // 手段：借助 归并排序；
    // 原理：归并排序的过程 其实就是 找到并消除 数组中存在的逆序对的过程
    public static <Item extends Comparable<Item>> int getInversionNumber(Item[] originalArray) {
        // with help of merge-sort
        int itemAmount = originalArray.length;
        Item[] arrayToSort = originalArray.clone(); // 被排序的数组 - 作用：防止原始数组被修改
        Item[] aux = originalArray.clone(); // 辅助数组 - 作用：用于找出“当前待排定位置”上，正确的元素
        return getInversionNumber(originalArray, arrayToSort, aux, 0, itemAmount - 1);
    }

    private static <Key extends Comparable<Key>> int getInversionNumber(Key[] originalArray, Key[] arrayToSort, Key[] aux, int leftBar, int rightBar) {
        int inversionNumber = 0;

        if(leftBar >= rightBar) return 0;
        int middle = leftBar + (rightBar - leftBar) / 2;

        /* 使用归并排序的方式 来 统计出原始数组中所有的逆序对的数量 */
        // 左半区间的逆序对数量
        inversionNumber += getInversionNumber(originalArray, arrayToSort, aux, leftBar, middle);
        // 右半区间的逆序对数量
        inversionNumber += getInversionNumber(originalArray, arrayToSort, aux, middle + 1, rightBar);
        // 左右两个区间之间的逆序对数量
        inversionNumber +=  merge(arrayToSort, aux, leftBar, middle, rightBar);

        assert inversionNumber == brute(originalArray, leftBar, rightBar);
        return inversionNumber;
    }

    private static <Key extends Comparable<Key>> int brute(Key[] a, int leftBar, int rightBar) {
        int inversionNumber = 0;
        for (int currentSpot = leftBar; currentSpot <= rightBar; currentSpot++) {
            for (int nextSpot = currentSpot + 1; nextSpot <= rightBar; nextSpot++) {
                if (less(a[nextSpot], a[currentSpot])) inversionNumber++;
            }
        }
        return inversionNumber;
    }

    private static <Key extends Comparable<Key>> int merge(Key[] arrayToSort, Key[] aux, int leftBar, int middle, int rightBar) {
        // #1 把“待排定数组”中的“待排定区间”中的元素 拷贝到 辅助数组中
        for (int currentSpot = leftBar; currentSpot <= rightBar; currentSpot++) {
            aux[currentSpot] = arrayToSort[currentSpot];
        }

        // #2 准备左右区间指针 作用：用于比较辅助数组中的元素 来 决定“用于排定当前位置”的“正确的元素”
        int inversionNumber = 0;
        int leftHalfCursor = leftBar;
        int rightHalfCursor = middle + 1;

        // #3 对于当前“待排定的位置”currentSpot，排定它   手段：通过比较左右指针所指向的元素的大小
        for (int currentSpot = leftBar; currentSpot <= rightBar; currentSpot++) {
            if (leftHalfCursor > middle) arrayToSort[currentSpot] = aux[rightHalfCursor++];
            else if(rightHalfCursor > rightBar) arrayToSort[currentSpot] = aux[leftHalfCursor++];
            else if (less(aux[rightHalfCursor], aux[leftHalfCursor])) { // 右侧指针指向的元素更小时，说明出现了逆序对
                arrayToSort[currentSpot] = aux[rightHalfCursor++];
                // 把(由于右指针指向的元素 比起 左指针指向的元素更小)产生的逆序对，累计到“逆序对总数量”中
                inversionNumber += (middle - leftHalfCursor + 1);
            } else {
                arrayToSort[currentSpot] = aux[leftHalfCursor++];
            }
        }

        return inversionNumber;
    }

    private static <Item extends Comparable<Item>> boolean less(Item itemI, Item itemJ) {
        return itemI.compareTo(itemJ) < 0;
    }
}
