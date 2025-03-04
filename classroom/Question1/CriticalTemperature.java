package Question1;

public class CriticalTemperature {
    /**
     * Finds the minimum number of measurements required to determine the critical temperature.
     *
     * @param k The number of identical samples of the material.
     * @param n The number of temperature levels.
     * @return The minimum number of measurements required.
     */
    public static int findMinMeasurements(int k, int n) {
        // Create a 2D array to store the minimum number of measurements
        int[][] dp = new int[k + 1][n + 1];
        
        // Initialize the base cases for 1 sample
        for (int j = 1; j <= n; j++) {
            dp[1][j] = j;
        }
        
        // Fill the dp table
        for (int i = 2; i <= k; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int x = 1; x <= j; x++) {
                    int worst = Math.max(dp[i - 1][x - 1], dp[i][j - x]);
                    dp[i][j] = Math.min(dp[i][j], 1 + worst);
                }
            }
        }
        
        return dp[k][n];
    }

    public static void main(String[] args) {
        // Test cases
        testFindMinMeasurements(1, 2, 2);
        testFindMinMeasurements(2, 6, 3);
        testFindMinMeasurements(3, 14, 4);
        testFindMinMeasurements(4, 20, 5);
    }

    /**
     * Test method to validate the findMinMeasurements function.
     *
     * @param k The number of samples.
     * @param n The number of temperature levels.
     * @param expected The expected result.
     */
    private static void testFindMinMeasurements(int k, int n, int expected) {
        int result = findMinMeasurements(k, n);
        System.out.printf("Test case: k=%d, n=%d\n", k, n);
        System.out.printf("Expected: %d, Actual: %d\n", expected, result);
        System.out.println(result == expected ? "PASSED" : "FAILED");
        System.out.println();
    }
}