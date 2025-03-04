package Question2;

import java.util.Arrays;

public class ClosestPoints {
    /**
     * Finds the lexicographically smallest pair of points with the smallest Manhattan distance.
     *
     * @param xCoords The array of x-coordinates of the points.
     * @param yCoords The array of y-coordinates of the points.
     * @return The indices of the closest pair of points.
     */
    public static int[] findClosestPair(int[] xCoords, int[] yCoords) {
        int n = xCoords.length;
        int[] result = new int[]{0, 1};  // Initialize with first two indices
        int minDistance = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int distance = Math.abs(xCoords[i] - xCoords[j]) + Math.abs(yCoords[i] - yCoords[j]);
                
                if (distance < minDistance || (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // Test cases
        testFindClosestPair(
            new int[]{1, 2, 3, 2, 4},
            new int[]{2, 3, 1, 2, 3},
            new int[]{0, 3}
        );
        testFindClosestPair(
            new int[]{1, 1, 1},
            new int[]{1, 1, 1},
            new int[]{0, 1}
        );
        testFindClosestPair(
            new int[]{1, 2, 3},
            new int[]{4, 5, 6},
            new int[]{0, 1}
        );
    }

    /**
     * Test method to validate the findClosestPair function.
     *
     * @param xCoords The array of x-coordinates of the points.
     * @param yCoords The array of y-coordinates of the points.
     * @param expected The expected indices of the closest pair.
     */
    private static void testFindClosestPair(int[] xCoords, int[] yCoords, int[] expected) {
        int[] result = findClosestPair(xCoords, yCoords);
        System.out.printf("Test case: xCoords=%s, yCoords=%s\n", 
                          Arrays.toString(xCoords), Arrays.toString(yCoords));
        System.out.printf("Expected: %s, Actual: %s\n", 
                          Arrays.toString(expected), Arrays.toString(result));
        System.out.println(Arrays.equals(result, expected) ? "PASSED" : "FAILED");
        System.out.println();
    }
}