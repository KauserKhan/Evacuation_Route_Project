package org.example.routing.controller;

import org.example.routing.RoutingApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = RoutingApplication.class)// <-- point to your @SpringBootApplication class
@AutoConfigureMockMvc
class ControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Test
        void testHealthEndpoint() throws Exception {
            mockMvc.perform(get("/api/evac/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("UP"))
                    .andExpect(jsonPath("$.vertices").exists())
                    .andExpect(jsonPath("$.edges").exists());
        }

        @Test
        void testRouteEndpointWithSeparateParams() throws Exception {
            mockMvc.perform(get("/api/evac/route")
                            .param("startLat", "51.1079")
                            .param("startLon", "17.0385")
                            .param("endLat", "51.1150")
                            .param("endLon", "17.0500"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.type").value("FeatureCollection"))
                    .andExpect(jsonPath("$.features").isArray());
        }

        @Test
        void testRouteEndpointWithCommaSeparatedParams() throws Exception {
            mockMvc.perform(get("/api/evac/route")
                            .param("start", "17.0385,51.1079")
                            .param("end", "17.0500,51.1150"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.type").value("FeatureCollection"))
                    .andExpect(jsonPath("$.features").isArray());
        }

        @Test
        void testRouteEndpointInvalidParams() throws Exception {
            mockMvc.perform(get("/api/evac/route")
                            .param("startLat", "51.0"))   // incomplete params
                    .andExpect(status().isBadRequest())   // now mapped to 400 by GlobalExceptionHandler
                    .andExpect(jsonPath("$.error")
                            .value("Provide either startLat/startLon/endLat/endLon OR start/end as 'lon,lat'"));
        }
}
