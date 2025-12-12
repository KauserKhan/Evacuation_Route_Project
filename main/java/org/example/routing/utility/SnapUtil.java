package org.example.routing.utility;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;

public class SnapUtil {
    public static Coordinate findNearestNode(Graph<Coordinate, DefaultWeightedEdge> graph,
                                             Coordinate target) {
        Coordinate nearest = null;
        double minDist = Double.MAX_VALUE;

        for (Coordinate node : graph.vertexSet()) {
            double dx = node.x - target.x;
            double dy = node.y - target.y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            if (dist < minDist) {
                minDist = dist;
                nearest = node;
            }
        }
        return nearest;
    }
}
