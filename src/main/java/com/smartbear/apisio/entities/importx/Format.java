package com.smartbear.apisio.entities.importx;

import javax.json.JsonObject;

public final class Format {
    public enum Type {
        SWAGGER,
        RAML,
        WADL
    }

    public final Type type;
    public final String url;

    public Format(JsonObject src) {
        url = src.getString("url", "");
        type = Enum.valueOf(Type.class, src.getString("type").toUpperCase());
    }

    @Override
    public String toString() {
        return String.format("%s", type);
    }
}
