package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

// #1 partition the array into subGroup via 'itemAmountOfSubGroup';
// #2 iterate each item after the item(startPointOfDisorder), then using 'insert sort' to sort each item sequence
// #3 downhill the subGroup till 1.
public class ShellSortDrill {

    public static void sort(Comparable[] a) {

        int itemAmount = a.length;
        int itemAmountOfSubGroup = 1;

        while (itemAmountOfSubGroup < itemAmount / 3) {
            itemAmountOfSubGroup = itemAmountOfSubGroup * 3 + 1;
        }

        int count = 0;
        while (itemAmountOfSubGroup >= 1) {
            int startPointOfDisorder = itemAmountOfSubGroup;

            for (int currentItemToInsert = startPointOfDisorder; currentItemToInsert < itemAmount; currentItemToInsert++) {
                for (int backwardsCursor = currentItemToInsert; backwardsCursor >= itemAmountOfSubGroup && less(a, backwardsCursor, backwardsCursor - itemAmountOfSubGroup) ; backwardsCursor-=itemAmountOfSubGroup) {
                    exch(a, backwardsCursor, backwardsCursor - itemAmountOfSubGroup);
                }
            }
            System.out.println("第" + (++count) + "次循环");
            System.out.println("当前 itemAmountOfSubGroup的值为： " + itemAmountOfSubGroup);
            System.out.println("当前数组元素为：");
            show(a);
            System.out.println("--------------");
            itemAmountOfSubGroup = itemAmountOfSubGroup / 3;
        }
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable[] a, int i, int j) {
        return a[i].compareTo(a[j]) < 0;
    }

    private static void show(Comparable[] a) {
        for (int cursor = 0; cursor < a.length; cursor++) {
            System.out.print(a[cursor] + " ");
        }

        System.out.println();
    }

    public static void main(String[] args) {
        String[] a = {"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);

        show(a);
    }
}
