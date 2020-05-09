package com.example.brunovsiq.guarana_test.service;

import android.service.autofill.UserData;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PlaceDeserializerJson implements JsonDeserializer<JsonElement> {


    @Override
    public JsonArray deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return json.getAsJsonObject().get("response").getAsJsonObject().get("groups").getAsJsonArray().get(0).getAsJsonObject().get("items").getAsJsonArray();
    }
}
