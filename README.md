 ***************Evacuation Routing Service*************
A Spring Boot application that computes safe evacuation routes using road and flood GeoJSON data.  
It builds a weighted graph of roads, increases edge weights for flooded areas, and exposes REST endpoints to query routes in **GeoJSON** format.
---
## Features

- Load road and flood geometries from GeoJSON files (`/data/roads.geojson`, `/data/floods.geojson`).
- Build a weighted graph with JGraphT.
- Snap start/end coordinates to nearest road nodes.
- Compute shortest path avoiding flooded areas.
- Return route as a **GeoJSON FeatureCollection**.
- Health check endpoint for monitoring.
- Global exception handling with structured JSON error responses and logging.

---

## Tech Stack
- Java 17+
- Spring Boot
- JGraphT
- GeoTools / JTS (geometry handling)
- Maven

---

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/evacuation-routing.git
   cd evacuation-routing
----------------------------------------------------------------------------------------------------------------------------------

**Build the project**:
mvn clean install

**Run the application:**
mvn spring-boot:run

**Place your GeoJSON files in src/main/resources/data/:**

roads.geojson → LineString/MultiLineString features

floods.geojson → Polygon/MultiPolygon features

-----------------------------------------------------------------------------------------------------------------------------------

**API Endpoints Test URL**

**Health Check**
GET /api/evac/health

**Test Route (separate params)**

GET /api/evac/route?startLat=51.1079&startLon=17.0385&endLat=51.1150&endLon=17.0500

**Test Route (comma-separated params)**

GET /api/evac/route?start=17.0385,51.1079&end=17.0500,51.1150
