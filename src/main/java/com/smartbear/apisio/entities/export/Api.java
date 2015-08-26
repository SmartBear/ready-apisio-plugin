package com.smartbear.apisio.entities.export;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.List;

public final class Api {
    public String name;
    public String description;
    public String image;
    public String baseUrl;
    public String humanUrl;
    public final List<String> tags = new ArrayList<>();
    public String contactName = "";     //TODO: not supported in the GUI
    public String contactEmail = "";    //TODO: not supported in the GUI
    public String propertyType = "";
    public String propertyUrl = "";

    public JsonObject toJson() {
        JsonObjectBuilder builder =  Json.createObjectBuilder();
        builder.add("name", name);
        builder.add("description", description);
        builder.add("image", image);
        builder.add("baseURL", baseUrl);
        builder.add("humanURL", humanUrl);
        builder.add("tags", getTagsJson());
        builder.add("contact", getContactJson());
        builder.add("properties", getPropertiesJson());
        return builder.build();
    }

    private JsonArrayBuilder getTagsJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String tag: tags) {
            arrayBuilder.add(tag);
        }
        return arrayBuilder;
    }

    private JsonArrayBuilder getContactJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        if (!contactName.isEmpty()) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("FN", contactName);
            objectBuilder.add("email", contactEmail);
            arrayBuilder.add(objectBuilder);
        }
        return arrayBuilder;
    }

    private JsonArrayBuilder getPropertiesJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        if (!propertyUrl.isEmpty()) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("type", propertyType);
            objectBuilder.add("url", propertyUrl);
            arrayBuilder.add(objectBuilder);
        }
        return arrayBuilder;
    }
}
