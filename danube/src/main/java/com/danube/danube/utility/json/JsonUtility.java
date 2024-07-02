package com.danube.danube.utility.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;

public interface JsonUtility {
    void validateJson(String json, String schemaName) throws IOException;
    <T> T validateJson(String json, Class<T> classToMapTo) throws IOException;
    <T> T validateJson(String json, TypeReference<T> classToMapTo) throws IOException;
    <T> T mapJsonToObject(String json, Class<T> classToMapTo) throws JsonProcessingException;
}
