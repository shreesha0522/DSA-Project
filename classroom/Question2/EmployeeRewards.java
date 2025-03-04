
package Question2;

import java.util.Arrays;

public class EmployeeRewards {
    /**
     * Determines the minimum number of rewards needed to distribute to employees.
     *
     * @param ratings The array of employee performance ratings.
     * @return The minimum number of rewards needed.
     */
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];
        Arrays.fill(rewards, 1);  // Initialize all rewards to 1

        // Forward pass: compare with left neighbor
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // Backward pass: compare with right neighbor
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Sum up all rewards
        return Arrays.stream(rewards).sum();
    }

    public static void main(String[] args) {
        // Test cases
        testMinRewards(new int[]{1, 0, 2}, 5);
        testMinRewards(new int[]{1, 2, 2}, 4);
        
        testMinRewards(new int[]{1, 2, 3, 4, 5}, 15);
    }

    /**
     * Test method to validate the minRewards function.
     *
     * @param ratings The array of employee performance ratings.
     * @param expected The expected minimum number of rewards.
     */
    private static void testMinRewards(int[] ratings, int expected) {
        int result = minRewards(ratings);
        System.out.printf("Test case: ratings=%s\n", Arrays.toString(ratings));
        System.out.printf("Expected: %d, Actual: %d\n", expected, result);
        System.out.println(result == expected ? "PASSED" : "FAILED");
        System.out.println();
    }
}