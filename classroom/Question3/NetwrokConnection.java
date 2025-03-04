package Question3;

import java.util.*;

/**
 * This class implements a solution to find the minimum total cost to connect all devices in a network.
 * It utilizes Kruskal's algorithm with Union-Find to construct a Minimum Spanning Tree (MST).
 */
class NetworkConnection {
    /**
     * Represents an edge in the network graph.
     */
    static class Edge {
        int u, v, cost;
        Edge(int u, int v, int cost) {
            this.u = u;
            this.v = v;
            this.cost = cost;
        }
    }
    
    /**
     * Union-Find (Disjoint Set) data structure for cycle detection in Kruskal's Algorithm.
     */
    static class UnionFind {
        int[] parent, rank;
        UnionFind(int n) {
            parent = new int[n + 1];
            rank = new int[n + 1];
            for (int i = 0; i <= n; i++) parent[i] = i; // Initialize parent array
        }
        
        /**
         * Finds the representative (root) of a set using path compression.
         */
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]); // Path compression for efficiency
            return parent[x];
        }
        
        /**
         * Unites two subsets if they are different, using union by rank.
         */
        boolean union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX == rootY) return false; // Already connected
            if (rank[rootX] > rank[rootY]) parent[rootY] = rootX;
            else if (rank[rootX] < rank[rootY]) parent[rootX] = rootY;
            else { parent[rootY] = rootX; rank[rootX]++; } // Merge and update rank
            return true;
        }
    }
    
    /**
     * Computes the minimum total cost to connect all devices.
     * 
     * @param n          Number of devices
     * @param modules    Cost of installing modules on each device
     * @param connections Array of connections, where each connection is [device1, device2, cost]
     * @return Minimum total cost to connect all devices
     */
    public static int minimumTotalCost(int n, int[] modules, int[][] connections) {
        List<Edge> edges = new ArrayList<>();
        for (int[] conn : connections) edges.add(new Edge(conn[0], conn[1], conn[2])); // Convert connections to Edge list
        
        edges.sort(Comparator.comparingInt(e -> e.cost)); // Sort edges by cost (Kruskal’s Algorithm)
        
        UnionFind uf = new UnionFind(n);
        int totalCost = 0, edgesUsed = 0;
        
        for (Edge edge : edges) {
            if (uf.union(edge.u, edge.v)) { // If devices are not already connected
                totalCost += edge.cost; // Add connection cost
                edgesUsed++;
                if (edgesUsed == n - 1) break; // Stop when all devices are connected
            }
        }
        
        // If not all devices are connected, install the cheapest module
        int minModuleCost = Arrays.stream(modules).min().orElse(0);
        if (edgesUsed < n - 1) totalCost += minModuleCost;
        
        return totalCost;
    }
    
    /**
     * Main function to test various cases.
     */
    public static void main(String[] args) {

        
        int n2 = 4;
        int[] modules2 = {5, 3, 2, 1};
        int[][] connections2 = {{1, 2, 4}, {2, 3, 1}, {3, 4, 3}, {1, 4, 2}};
        System.out.println("Test Case 2 - Expected: 6, Output: " + minimumTotalCost(n2, modules2, connections2));
    }
}

