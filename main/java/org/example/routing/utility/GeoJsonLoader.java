package org.example.routing.utility;

import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.locationtech.jts.geom.Geometry;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GeoJsonLoader {

    /**
     * Load geometries from a GeoJSON file on the classpath, filtering by type.
     *
     * @param resourcePath path to file in resources (e.g. "/data/roads.geojson")
     * @param allowedTypes list of geometry types to keep (e.g. "LineString", "MultiLineString")
     */
    public static List<Geometry> loadGeometriesFromClasspath(String resourcePath, List<String> allowedTypes) {
        List<Geometry> geometries = new ArrayList<>();
        try (InputStream in = GeoJsonLoader.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            FeatureJSON fjson = new FeatureJSON();
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection =
                    (FeatureCollection<SimpleFeatureType, SimpleFeature>)
                            fjson.readFeatureCollection(new InputStreamReader(in));

            try (FeatureIterator<SimpleFeature> it = collection.features()) {
                while (it.hasNext()) {
                    SimpleFeature feature = it.next();
                    Geometry geom = (Geometry) feature.getDefaultGeometry();
                    if (geom != null && allowedTypes.contains(geom.getGeometryType())) {
                        geometries.add(geom);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return geometries;
    }
}