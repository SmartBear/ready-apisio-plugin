package com.smartbear.apisio.entities.importx;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

public final class Api {
    public final String name;
    public final String description;
    public final String baseUrl;
    public final List<Format> formats = new ArrayList<>();

    private Format format;

    public Api(JsonObject src) {
        name = src.getString("name", "");
        description = src.getString("description", "");
        baseUrl = src.getString("baseURL", "");

        JsonArray properties = src.getJsonArray("properties");
        if (properties == null)
            return;
        for (int i = 0; i < properties.size(); i++) {
            JsonObject item = properties.getJsonObject(i);
            try {
                formats.add(new Format(item));
            } catch (IllegalArgumentException ex) {
                //expected exception due parse ApiFormat.type
            }
        }

        format = formats.size() == 0 ? null : formats.get(0);
    }

    @Override
    public String toString() {
        return String.format("%s, %s\r\n", name, formats);
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }
}
