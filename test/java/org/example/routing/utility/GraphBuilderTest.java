package org.example.routing.utility;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphBuilderTest {
    @Test
    void testBuildGraphWithSimpleRoad() {
        GeometryFactory gf = new GeometryFactory();
        LineString road = gf.createLineString(new Coordinate[]{
                new Coordinate(17.0, 51.0),
                new Coordinate(17.1, 51.1)
        });

        var graph = GraphBuilder.buildGraph(List.of(road), List.of());

        assertEquals(2, graph.vertexSet().size());
        assertEquals(1, graph.edgeSet().size());
    }

}
