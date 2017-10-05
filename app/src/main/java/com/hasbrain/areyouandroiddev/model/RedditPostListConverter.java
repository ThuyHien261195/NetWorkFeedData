package com.hasbrain.areyouandroiddev.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by thuyhien on 10/4/17.
 */

public class RedditPostListConverter extends EasyDeserializer<List<RedditPost>> {
    @Override
    public List<RedditPost> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json != null && json.isJsonObject()) {
            JsonElement jsonElement = json.getAsJsonObject().get("data");

            if (jsonElement != null && jsonElement.isJsonObject()) {
                JsonElement childJsonElement = jsonElement.getAsJsonObject().get("children");

                if (childJsonElement != null && childJsonElement.isJsonArray()) {
                    JsonArray redditPostsJson = childJsonElement.getAsJsonArray();
                    Type type = new TypeToken<List<RedditPost>>() {
                    }.getType();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
                    Gson gson = gsonBuilder.create();
                    return gson.fromJson(redditPostsJson, type);
                }
            }
        }
        return null;
    }
}
