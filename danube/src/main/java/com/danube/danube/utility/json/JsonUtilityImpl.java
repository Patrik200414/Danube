package com.danube.danube.utility.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtilityImpl implements JsonUtility{
    private final ObjectMapper objectMapper;

    public JsonUtilityImpl() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void validateJson(String json, String schemaName) throws IOException {
        validate(json, schemaName);
    }

    @Override
    public <T> T validateJson(String json, Class<T> classToMapTo) throws IOException {
        validate(json, classToMapTo.getSimpleName());
        return mapJsonToObject(json, classToMapTo);
    }

    @Override
    public <T> T mapJsonToObject(String json, Class<T> classToMapTo) throws JsonProcessingException {
        return objectMapper.readValue(json, classToMapTo);
    }

    @Override
    public <T> T validateJson(String json, TypeReference<T> classToMapTo) throws IOException {
        return objectMapper.readValue(json, classToMapTo);
    }

    private void validate(String json, String schemaName) throws IOException {
        InputStream schemaStream = new ClassPathResource("schemas/" + schemaName + ".json").getInputStream();
        JSONObject rawSchema = new JSONObject(new JSONTokener(schemaStream));
        Schema schema = SchemaLoader.load(rawSchema);

        schema.validate(new JSONObject(json));
    }
}
