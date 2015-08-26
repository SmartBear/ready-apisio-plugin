package com.smartbear.apisio.entities.importx;

import com.smartbear.apisio.Helper;
import com.smartbear.apisio.Strings;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.Collections;
import java.util.List;

public final class ApiList {

    public final List<Api> apis;

    public ApiList(JsonObject src) throws Exception {
        String requestStatus = src.getString("status", "");
        if (!requestStatus.equalsIgnoreCase("success")) {
            throw new Exception(Strings.Executing.UNSUCCESSFUL_STATUS_ERROR);
        }
        JsonArray data = src.getJsonArray("data");
        if (data == null) {
            throw new Exception(Strings.Executing.UNEXPECTED_RESPONSE_FORMAT_ERROR);
        }

        List<Api> apis = Helper.extractList(data, new Helper.EntityFactory<Api>() {
            @Override
            public Api create(JsonObject value) {
                return new Api(value);
            }
        });

        this.apis= Collections.unmodifiableList(apis);
    }
}
