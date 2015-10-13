package com.smartbear.apisio.entities.export;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//http://apisjson.org/format.html
//http://apisjson.org/format/apisjson_0.14.txt
public final class Domain {

    public String name;
    public String description;
    public String url;
    public String image;
    public String maintainer;
    public String created;
    public String modified;
    public final String specificationVersion = "0.14";
    public final List<String> tags = new ArrayList<>();
    public final List<Api> apis = new ArrayList<>();

    public Domain() {
        name = "";
        description = "";
        url = "";
        image = "";
        maintainer = "";
        Date now = new Date();
        created = String.format("%tY-%tm-%td", now, now, now);
        modified = created;
    }

    public Api addApi(String name, String description) {
        Api api = new Api();
        api.name = name;
        api.description = description;
        apis.add(api);
        return api;
    }

    public JsonObject toJson() {
        JsonObjectBuilder builder =  Json.createObjectBuilder();
        builder.add("name", name);
        builder.add("description", description);
        builder.add("url", url);
        builder.add("image", image);
        builder.add("tags", getTagsJson());
        builder.add("maintainers", getMaintainerJson());
        builder.add("created", created);
        builder.add("modified", modified);
        builder.add("specificationVersion", specificationVersion);
        builder.add("apis", getApisJson());
        return builder.build();
    }

    private JsonArrayBuilder getTagsJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String tag: tags) {
            arrayBuilder.add(tag);
        }
        return arrayBuilder;
    }

    private JsonArrayBuilder getMaintainerJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        arrayBuilder.add(maintainer);
        return arrayBuilder;
    }

    private JsonArrayBuilder getApisJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Api api: apis) {
            arrayBuilder.add(api.toJson());
        }
        return arrayBuilder;
    }
}
