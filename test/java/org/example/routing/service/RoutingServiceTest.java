package org.example.routing.service;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoutingServiceTest {
    @Test
    void testFindRoute() {
        var graph = new SimpleWeightedGraph<Coordinate, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        Coordinate a = new Coordinate(17.0, 51.0);
        Coordinate b = new Coordinate(17.1, 51.1);
        graph.addVertex(a);
        graph.addVertex(b);
        graph.addEdge(a, b);

        RoutingService service = new RoutingService(graph);
        List<Coordinate> route = service.findRoute(a, b);

        assertEquals(List.of(a, b), route);
    }

    @Test
    void testNoPathThrowsException() {
        var graph = new SimpleWeightedGraph<Coordinate, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        Coordinate a = new Coordinate(17.0, 51.0);
        Coordinate b = new Coordinate(17.1, 51.1);
        graph.addVertex(a);
        graph.addVertex(b);

        RoutingService service = new RoutingService(graph);

        assertThrows(IllegalArgumentException.class, () -> service.findRoute(a, b));
    }
}
