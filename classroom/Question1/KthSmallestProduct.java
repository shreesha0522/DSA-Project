package Question1;

import java.util.Arrays;

public class KthSmallestProduct {
    /**
     * Finds the kth smallest product of two sorted arrays.
     *
     * @param returns1 The first sorted array of investment returns.
     * @param returns2 The second sorted array of investment returns.
     * @param k The target index of the lowest combined return.
     * @return The kth smallest product.
     */
    public static long kthSmallestProduct(int[] returns1, int[] returns2, long k) {
        long left = (long) Math.min(
            Math.min(returns1[0] * (long)returns2[0], returns1[0] * (long)returns2[returns2.length - 1]),
            Math.min(returns1[returns1.length - 1] * (long)returns2[0], returns1[returns1.length - 1] * (long)returns2[returns2.length - 1])
        );
        long right = (long) Math.max(
            Math.max(returns1[0] * (long)returns2[0], returns1[0] * (long)returns2[returns2.length - 1]),
            Math.max(returns1[returns1.length - 1] * (long)returns2[0], returns1[returns1.length - 1] * (long)returns2[returns2.length - 1])
        );
        
        while (left < right) {
            long mid = left + (right - left) / 2;
            if (countSmallerOrEqual(returns1, returns2, mid) < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        return left;
    }
    
    /**
     * Counts the number of products smaller than or equal to the target.
     *
     * @param returns1 The first sorted array of investment returns.
     * @param returns2 The second sorted array of investment returns.
     * @param target The target value to compare against.
     * @return The count of products smaller than or equal to the target.
     */
    private static long countSmallerOrEqual(int[] returns1, int[] returns2, long target) {
        long count = 0;
        int n = returns2.length;
        for (int num : returns1) {
            if (num == 0) {
                if (target >= 0) count += n;
            } else if (num > 0) {
                count += upperBound(returns2, (double)target / num);
            } else { // num < 0
                count += n - upperBound(returns2, (double)target / num);
            }
        }
        return count;
    }

    /**
     * Finds the upper bound (rightmost position to insert) in a sorted array.
     *
     * @param arr The sorted array to search in.
     * @param target The target value.
     * @return The index of the upper bound.
     */
    private static int upperBound(int[] arr, double target) {
        int left = 0, right = arr.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    public static void main(String[] args) {
        // Test cases
        testKthSmallestProduct(new int[]{2, 5}, new int[]{3, 4}, 2, 8);
        testKthSmallestProduct(new int[]{-4, -2, 0, 3}, new int[]{2, 4}, 6, 0);
        
    }

    /**
     * Test method to validate the kthSmallestProduct function.
     *
     * @param returns1 The first sorted array of investment returns.
     * @param returns2 The second sorted array of investment returns.
     * @param k The target index of the lowest combined return.
     * @param expected The expected result.
     */
    private static void testKthSmallestProduct(int[] returns1, int[] returns2, long k, long expected) {
        long result = kthSmallestProduct(returns1, returns2, k);
        System.out.printf("Test case: returns1=%s, returns2=%s, k=%d\n", 
                          Arrays.toString(returns1), Arrays.toString(returns2), k);
        System.out.printf("Expected: %d, Actual: %d\n", expected, result);
        System.out.println(result == expected ? "PASSED" : "FAILED");
        System.out.println();
    }
}