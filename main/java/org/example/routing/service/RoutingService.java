package org.example.routing.service;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;

import java.util.List;

public class RoutingService {
    private final Graph<Coordinate, DefaultWeightedEdge> graph;

    public RoutingService(Graph<Coordinate, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public List<Coordinate> findRoute(Coordinate start, Coordinate end) {
        DijkstraShortestPath<Coordinate, DefaultWeightedEdge> dijkstra =
                new DijkstraShortestPath<>(graph);

        GraphPath<Coordinate, DefaultWeightedEdge> path = dijkstra.getPath(start, end);

        if (path == null) {
            throw new IllegalArgumentException("No path found between given coordinates");
        }

        return path.getVertexList();
    }

}
