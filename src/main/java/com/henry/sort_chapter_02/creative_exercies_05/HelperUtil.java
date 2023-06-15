package com.henry.sort_chapter_02.creative_exercies_05;

import java.util.Random;

public class HelperUtil {

    public static void shuffle(int[] a) {
        int itemAmount = a.length;
        for (int currentSpot = 0; currentSpot < itemAmount; currentSpot++) {
            int spotToSwap = currentSpot + uniform(itemAmount - currentSpot);

            int temp = a[currentSpot];
            a[currentSpot] = a[spotToSwap];
            a[spotToSwap] = temp;
        }
    }

    private static int uniform(int N) {
        Random random = new Random();
        return random.nextInt(N);
    }

    public static <Item extends Comparable<Item>> int getInversionNumber(Item[] a) {
        // with help of merge-sort
        int itemAmount = a.length;
        Item[] arrayToSort = a.clone();
        Item[] aux = a.clone();
        return getInversionNumber(a, arrayToSort, aux, 0, itemAmount - 1);
    }

    private static <Key extends Comparable<Key>> int getInversionNumber(Key[] a, Key[] arrayToSort, Key[] aux, int leftBar, int rightBar) {
        int inversionNumber = 0;

        if(leftBar >= rightBar) return 0;
        int middle = leftBar + (rightBar - leftBar) / 2;

        inversionNumber += getInversionNumber(a, arrayToSort, aux, leftBar, middle);
        inversionNumber += getInversionNumber(a, arrayToSort, aux, middle + 1, rightBar);
        inversionNumber +=  merge(arrayToSort, aux, leftBar, middle, rightBar);

        assert inversionNumber == brute(a, leftBar, rightBar);
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

        for (int currentSpot = leftBar; currentSpot <= rightBar; currentSpot++) {
            aux[currentSpot] = arrayToSort[currentSpot];
        }

        int inversionNumber = 0;
        int leftHalfCursor = leftBar;
        int rightHalfCursor = middle + 1;

        for (int currentSpot = leftBar; currentSpot <= rightBar; currentSpot++) {
            if (leftHalfCursor > middle) arrayToSort[currentSpot] = aux[rightHalfCursor++];
            else if(rightHalfCursor > rightBar) arrayToSort[currentSpot] = aux[leftHalfCursor++];
            else if (less(aux[rightHalfCursor], aux[leftHalfCursor])) { // 右侧指针指向的元素更小时，说明出现了逆序对
                arrayToSort[currentSpot] = aux[rightHalfCursor++];
                inversionNumber += (middle - leftHalfCursor + 1);
            } else {
                arrayToSort[currentSpot] = aux[leftHalfCursor++];
            }
        }

        return inversionNumber;
    }

    private static <Item extends Comparable<Item>> boolean less(Item i, Item j) {
        return i.compareTo(j) < 0;
    }
}
