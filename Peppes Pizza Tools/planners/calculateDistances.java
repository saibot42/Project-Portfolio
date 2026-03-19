package planners;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import Structures.Address;

import java.util.Comparator;

import graph.*;

public class calculateDistances {
    private WeightedGraph<V, Integer> graph;
    private V source;
    
    /**
	 * Calculates the distance from the first node to the next
	 */
    public calculateDistances(WeightedGraph<V, Integer> graph, V source) {
        this.graph = graph;
        this.source = source;
        Map<V, Integer> distances = shortestPath();
        Map<String, Integer> data = formulateData(distances);
        System.out.println(data);
    }

    
    /**
     * Uses djikstras algorithm to find the shortest path from source node
     * Aka, find the shortest path from the restaurant to the delivery spot
     * @return a map of the the shortest path from the restaraunt to all the deliveries
     */
    private Map<V, Integer> shortestPath() {
        //Step 1: Create a distance array
        Map<V, Integer> dist = new HashMap<>();
        dist.put(source, 0); //Step 1a: Set the distance from source node to Zero

        //Step 2: Create a priority queue 
        PriorityQueue<Map.Entry<V, Integer>> minHeap = new PriorityQueue<>(
            Comparator.comparingInt(Map.Entry::getValue) //Step 2a: Make it comparable so we sort the queue with the minimum value at the top
        );

        //Step 3: Add source node to queue with a distance of 0
        minHeap.offer(Map.entry(source, 0));

        //Step 4: While the priority queue is not empty
        while(!minHeap.isEmpty()) {
            //Remove the node with the smallest distance, which is to basically pick that node
            Map.Entry<V, Integer> current = minHeap.poll();
            V currentNode = current.getKey();
            int currentDistance = current.getValue();

            //Explore nedges of the current node with a for loop
            for (Edge<V> edge : graph.adjacentEdges(currentNode)) {
                V neighbor = edge.b;
                Integer weight = graph.getWeight(edge);

                Integer newDist = currentDistance + weight;
                
                if (!dist.containsKey(neighbor) || newDist < dist.get(neighbor)) {
                    dist.put(neighbor, newDist);
                    minHeap.offer(Map.entry(neighbor, newDist));
                }
            }

        }

        return dist;
    }

    private Map<String, Integer> formulateData(Map<V, Integer> distances) {
        Map<String, Integer> data = new HashMap<>();
        for (Map.Entry<V, Integer> entry : distances.entrySet()) {
            V vertex = entry.getKey();
            Integer distance = entry.getValue();
            Address address = vertex.getAddress();
            String addressname = new String(address.getStreetName() + " " + address.getStreetNumber());
            data.put(addressname, distance);
        }
        return data;
    }
    
}
