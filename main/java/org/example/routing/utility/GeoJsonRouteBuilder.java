package org.example.routing.utility;

import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoJsonRouteBuilder {
    /**
     * Convert a list of coordinates into a GeoJSON LineString Feature.
     */
    public static Map<String, Object> buildRouteFeature(List<Coordinate> routeCoords) {
        Map<String, Object> feature = new HashMap<>();
        try {
            GeometryFactory gf = new GeometryFactory();
            LineString line = gf.createLineString(routeCoords.toArray(new Coordinate[0]));

            // Convert JTS geometry to GeoJSON string
            GeometryJSON gjson = new GeometryJSON();
            StringWriter writer = new StringWriter();
            gjson.write(line, writer);

            // Build Feature object
            feature.put("type", "Feature");
            feature.put("geometry", gjson.toString(line));
            feature.put("properties", Map.of(
                    "segments", routeCoords.size(),
                    "length", line.getLength()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feature;
    }
}