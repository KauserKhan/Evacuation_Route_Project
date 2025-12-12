package org.example.routing.utility;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeoJasonRouteBuilderTest {
    @Test
    void testBuildRouteFeature() {
        List<Coordinate> coords = List.of(
                new Coordinate(17.0, 51.0),
                new Coordinate(17.1, 51.1)
        );

        Map<String, Object> feature = GeoJsonRouteBuilder.buildRouteFeature(coords);

        assertEquals("Feature", feature.get("type"));
        assertTrue(feature.containsKey("geometry"));
        assertTrue(feature.containsKey("properties"));
    }
}
