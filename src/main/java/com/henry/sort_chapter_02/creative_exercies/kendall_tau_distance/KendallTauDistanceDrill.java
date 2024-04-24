package com.henry.sort_chapter_02.creative_exercies.kendall_tau_distance;

public class KendallTauDistanceDrill {

    private static int getKendallTauDistanceBetween(int[] permutation01, int[] permutation02) {
        // 1 set permutation01 as 'base permutation' - “item -> spot”
        int itemAmount = permutation01.length;
        int[] itemToSpotArrayIn01 = new int[itemAmount];
        for (int currentSpot = 0; currentSpot < itemAmount; currentSpot++) {
            int itemOfCurrentSpot = permutation01[currentSpot];
            itemToSpotArrayIn01[itemOfCurrentSpot] = currentSpot;
        }

        // 2 transform permutation02 to permutation01, and calculate its inversions (equals to distance of p01 and p02).
        Integer[] spotIn02ToSpotIn01Array = new Integer[itemAmount];
        for (int spotIn02 = 0; spotIn02 < permutation02.length; spotIn02++) {
            int itemIn02 = permutation02[spotIn02];
            int spotIn01 = itemToSpotArrayIn01[itemIn02];
            spotIn02ToSpotIn01Array[spotIn02] = spotIn01;
        }

        // 3 get the inversion numbers of 'spotIn02ToSpotIn01Array'
        return ArrayUtil.getInversionNumber(spotIn02ToSpotIn01Array);
    }

    private static void show(int[] a) {
        for (int currentSpot = 0; currentSpot < a.length; currentSpot++) {
            System.out.print(a[currentSpot] + " ");
        }

        System.out.println();
    }

    private static int[] generatePermutation(int N) {
        int[] permutation = new int[N];
        for (int currentSpot = 0; currentSpot < N; currentSpot++) {
            permutation[currentSpot] = currentSpot;
        }

        ArrayUtil.shuffle(permutation);

        return permutation;
    }


    public static void main(String[] args) {
        int N = 4;
        // 生成随机的排列
//        int[] permutation01 = KendallTauDistanceDrill.generatePermutation(N);
//        int[] permutation02 = KendallTauDistanceDrill.generatePermutation(N);
        int[] permutation01 = {0, 1, 2, 3};
        int[] permutation02 = {2, 1, 0, 3};

        show(permutation01);
        show(permutation02);

        // 得到 两个排列的 Kendall Tau距离
        int distance = getKendallTauDistanceBetween(permutation01, permutation02);

        System.out.println("distance between " + permutation01 + " and " + permutation02 + " is: " + distance);
    }
}
