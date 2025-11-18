package com.interns.internscopeapp;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JobTagDeserializer implements JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<String> tags = new ArrayList<>();

        try {
            if (json.isJsonArray()) {
                JsonArray array = json.getAsJsonArray();
                for (JsonElement element : array) {
                    tags.add(element.getAsString());
                }
            } else if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                String jsonStr = json.getAsString();
                JsonParser parser = new JsonParser(); // For older Gson
                JsonArray array = parser.parse(jsonStr).getAsJsonArray();
                for (JsonElement element : array) {
                    tags.add(element.getAsString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tags;
    }
}
