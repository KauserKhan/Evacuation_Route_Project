package org.example.routing.utility;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import static org.junit.jupiter.api.Assertions.*;

class SnapUtilTest {

    @Test
    void testFindNearestNode() {
        var graph = new SimpleWeightedGraph<Coordinate, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        Coordinate a = new Coordinate(17.0, 51.0);
        Coordinate b = new Coordinate(17.1, 51.1);
        graph.addVertex(a);
        graph.addVertex(b);

        Coordinate target = new Coordinate(17.05, 51.05);
        Coordinate nearest = SnapUtil.findNearestNode(graph, target);

        assertNotNull(nearest);
        assertEquals(a, nearest); // closer to a
    }
}

