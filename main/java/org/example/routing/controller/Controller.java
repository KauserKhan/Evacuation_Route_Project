package org.example.routing.controller;

import org.example.routing.service.RoutingService;
import org.example.routing.utility.GeoJsonLoader;
import org.example.routing.utility.GeoJsonRouteBuilder;
import org.example.routing.utility.GraphBuilder;
import org.example.routing.utility.SnapUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/evac")
public class Controller {

        private final RoutingService routingService;
        private final Graph<Coordinate, DefaultWeightedEdge> graph;

    public Controller() {
        // Roads: only LineString/MultiLineString
        List<org.locationtech.jts.geom.Geometry> roads =
                GeoJsonLoader.loadGeometriesFromClasspath("/data/roads.geojson",
                        List.of("LineString", "MultiLineString"));

        // Floods: only Polygon/MultiPolygon
        List<org.locationtech.jts.geom.Geometry> floods =
                GeoJsonLoader.loadGeometriesFromClasspath("/data/floods.geojson",
                        List.of("Polygon", "MultiPolygon"));

        this.graph = GraphBuilder.buildGraph(roads, floods);
        this.routingService = new RoutingService(graph);

        System.out.println("Graph loaded with " + graph.vertexSet().size() +
                " vertices and " + graph.edgeSet().size() + " edges");
    }

        // Health check endpoint
        @GetMapping("/health")
        public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "timestamp", System.currentTimeMillis(),
                "vertices", graph.vertexSet().size(),
                "edges", graph.edgeSet().size()
        );
    }

        // Route endpoint
        @GetMapping("/route")
        public Map<String, Object> getRoute(
            @RequestParam(required = false) Double startLat,
            @RequestParam(required = false) Double startLon,
            @RequestParam(required = false) Double endLat,
            @RequestParam(required = false) Double endLon,
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end) {

        Coordinate startCoord;
        Coordinate endCoord;

        if (start != null && end != null) {
            // Accept comma-separated format: "lon,lat"
            String[] startParts = start.split(",");
            String[] endParts = end.split(",");
            if (startParts.length != 2 || endParts.length != 2) {
                throw new IllegalArgumentException("Invalid start/end format. Use 'lon,lat'.");
            }
            double startLonParsed = Double.parseDouble(startParts[0].trim());
            double startLatParsed = Double.parseDouble(startParts[1].trim());
            double endLonParsed = Double.parseDouble(endParts[0].trim());
            double endLatParsed = Double.parseDouble(endParts[1].trim());

            startCoord = new Coordinate(startLonParsed, startLatParsed);
            endCoord = new Coordinate(endLonParsed, endLatParsed);

        } else if (startLat != null && startLon != null && endLat != null && endLon != null) {
            // Accept separate lat/lon parameters
            startCoord = new Coordinate(startLon, startLat);
            endCoord = new Coordinate(endLon, endLat);

        } else {
            throw new IllegalArgumentException(
                    "Provide either startLat/startLon/endLat/endLon OR start/end as 'lon,lat'");
        }

        // Snap to nearest graph nodes
        Coordinate snappedStart = SnapUtil.findNearestNode(graph, startCoord);
        Coordinate snappedEnd = SnapUtil.findNearestNode(graph, endCoord);

        if (snappedStart == null || snappedEnd == null) {
            throw new IllegalArgumentException("No nearby graph nodes found for given coordinates");
        }

        List<Coordinate> route = routingService.findRoute(snappedStart, snappedEnd);

        Map<String, Object> feature = GeoJsonRouteBuilder.buildRouteFeature(route);

        return Map.of(
                "type", "FeatureCollection",
                "features", List.of(feature)
        );
    }
    }