package org.example.routing.utility;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

public class GraphBuilder {
    /**
     * Build a graph from road geometries, connecting every consecutive coordinate pair.
     * Flood intersections increase edge weights.
     */
    public static Graph<Coordinate, DefaultWeightedEdge> buildGraph(
            List<Geometry> roads, List<Geometry> floods) {

        Graph<Coordinate, DefaultWeightedEdge> graph =
                new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for (Geometry road : roads) {
            if (road == null || road.isEmpty()) continue;

            Coordinate[] coords = road.getCoordinates();
            if (coords == null || coords.length < 2) continue;

            // Connect every consecutive pair of coordinates
            for (int i = 0; i < coords.length - 1; i++) {
                Coordinate a = coords[i];
                Coordinate b = coords[i + 1];

                if (a.equals2D(b)) continue; // skip degenerate points

                graph.addVertex(a);
                graph.addVertex(b);

                if (!graph.containsEdge(a, b)) {
                    DefaultWeightedEdge edge = graph.addEdge(a, b);
                    if (edge != null) {
                        double length = a.distance(b);

                        boolean flooded = false;
                        for (Geometry flood : floods) {
                            if (flood != null && road.intersects(flood)) {
                                flooded = true;
                                break;
                            }
                        }

                        double weight = flooded ? length * 10.0 : length;
                        graph.setEdgeWeight(edge, weight);
                    }
                }
            }
        }

        System.out.println("Graph built with " + graph.vertexSet().size() +
                " vertices and " + graph.edgeSet().size() + " edges");

        return graph;
    }
}


