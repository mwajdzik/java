package org.am061.java.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MarshallingTest {

    private static final String CAR_JSON = "{\"color\":\"yellow\",\"type\":\"Renault\"}";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testMarshalling() throws IOException {
        Car car = new Car("yellow", "Renault");

        Writer writer = new StringWriter();
        objectMapper.writeValue(writer, car);
        String asString = objectMapper.writeValueAsString(car);

        assertEquals(CAR_JSON, writer.toString());
        assertEquals(CAR_JSON, asString);
    }

    @Test
    public void testUnmarshalling() throws IOException {
        Car car = objectMapper.readValue(CAR_JSON, Car.class);

        assertEquals("yellow", car.getColor());
        assertEquals("Renault", car.getType());
    }

    @Test
    public void testJsonNode() throws IOException {
        JsonNode jsonNode = objectMapper.readTree(CAR_JSON);

        assertEquals("yellow", jsonNode.get("color").asText());
        assertEquals("Renault", jsonNode.get("type").asText());
    }

    @Test
    public void testUnmarshallingArraysAndMaps() throws IOException {
        String jsonCarArray = "[{ \"color\" : \"Black\", \"type\" : \"BMW\" }, { \"color\" : \"Red\", \"type\" : \"FIAT\" }]";
        List<Car> listCar = objectMapper.readValue(jsonCarArray, new TypeReference<List<Car>>() {
        });

        String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";
        Map<String, Object> map = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });

        assertEquals(2, listCar.size());
        assertEquals("BMW", map.get("type"));
    }

    @Test
    public void testParams() throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false);

        Car car = objectMapper.readValue(CAR_JSON, Car.class);
        assertEquals("yellow", car.getColor());
        assertEquals("Renault", car.getType());
    }
}
