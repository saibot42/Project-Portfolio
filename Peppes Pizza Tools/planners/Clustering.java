package planners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Structures.Address;
import graph.Edge;
import graph.V;
import graph.WeightedGraph;

public class Clustering {
    private WeightedGraph<V, Integer> graph;
    private V source;
    private final Integer threshold = 25; // Maximum amount of time a cluster can take to deliver all togheter
    private final Integer maxDeliveryTime = 60; // Maximum amount of time it can take for an order to be delivered
    private final Integer maxCluster = 2; // Maximum amount of trips a cluster can have
    
    public Clustering(WeightedGraph<V, Integer> graph, V source) {
        this.graph = graph;
        this.source = source;
        List<List<V>> clusters = createClusters();
        List<List<String>> formulateClusters = formulateData(clusters);
        System.out.println(formulateClusters);
    }

    private List<List<V>> createClusters() {
        Set<V> unassigned = new HashSet<>();
        for (V v : graph.vertices()) {
            if (!v.equals(source)) unassigned.add(v);
        }

        List<List<V>> clusters = new ArrayList<>();
        
        while(!unassigned.isEmpty()) {
            V seed = unassigned.iterator().next();
            unassigned.remove(seed);
            List<V> cluster = new ArrayList<>();
            cluster.add(seed);

            int clusterTime = graph.getWeight(source, seed);

            //Try adding nearest neighbors
            for (V candidate : new HashSet<>(unassigned)) {
                //if
                
                // int additionalTime = estimateAddedTime(cluster, candidate);
                // if (cluster.size() <= maxCluster && (clusterTime + additionalTime) < threshold) {
                //     cluster.add(candidate);
                //     unassigned.remove(candidate);
                // }
            }

            clusters.add(cluster);
        }

        return clusters;
    }

    private int estimateAddedTime(List<V> cluster, V candidate) {
        V last = cluster.get(cluster.size() - 1);
        return graph.getWeight(last, candidate) + graph.getWeight(candidate, source) - graph.getWeight(last, source);
    }

    private List<List<String>> formulateData(List<List<V>> clusters) {
        List<List<String>> data = new ArrayList<>();
        for (List<V> cluster : clusters) {
            List<String> clusterData = new ArrayList<>();
            for (V vertex : cluster) {
                Address address = vertex.getAddress();
                String addressname = new String(address.getStreetName() + " " + address.getStreetNumber());
                clusterData.add(addressname);
            }
            data.add(clusterData);
        }
        return data;
    }
}


