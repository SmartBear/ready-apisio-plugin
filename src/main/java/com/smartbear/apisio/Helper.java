package com.smartbear.apisio;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.support.StringUtils;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    private Helper() {
    }

    public interface Predicate<T> {
        public boolean execute(T value);
    }

    public static <T> T find(List<? extends T> list, Predicate<? super T> match) {
        for (T elem: list) {
            if (match.execute(elem))
                return elem;
        }
        return null;
    }

    public static <T> List<T> filter(List<? extends T> list, Predicate<? super T> match) {
        List<T> result = new ArrayList<>();
        for (T elem: list) {
            if (match.execute(elem))
                result.add(elem);
        }
        return result;
    }


    public interface EntityFactory<T> {
        public T create(JsonObject value);
    }

    public static <T> List<T> extractList(JsonArray array, EntityFactory<T> factory) throws IOException {
        ArrayList<T> result = new ArrayList<>();
        for (javax.json.JsonValue it : array) {
            if (it instanceof JsonObject) {
                T item = factory.create((JsonObject) it);
                result.add(item);
            }
        }
        return result;
    }

    public static URL stringToUrl(String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return null;
        }

        if (!s.toLowerCase().startsWith("http://") && !s.toLowerCase().startsWith("https://")) {
            s = "https://" + s;
        }

        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            SoapUI.logError(e);
            return null;
        }
    }
}