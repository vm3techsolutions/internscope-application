package com.interns.internscopeapp;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JobPostDeserializer implements JsonDeserializer<List<JobResponse>> {
    @Override
    public List<JobResponse> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        List<JobResponse> jobList = new ArrayList<>();

        if (json.isJsonArray()) {
            for (JsonElement element : json.getAsJsonArray()) {
                JobResponse job = context.deserialize(element, JobResponse.class);
                jobList.add(job);
            }
        } else if (json.isJsonObject()) {
            JobResponse job = context.deserialize(json, JobResponse.class);
            jobList.add(job);
        }

        return jobList;
    }
}
