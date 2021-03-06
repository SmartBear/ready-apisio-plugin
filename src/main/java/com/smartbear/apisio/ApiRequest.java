package com.smartbear.apisio;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.rest.RestServiceFactory;
import com.eviware.soapui.impl.rest.support.WadlImporter;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.PathUtils;
import com.eviware.soapui.support.ModelItemNamer;
import com.smartbear.apisio.entities.importx.Format;
import com.smartbear.soapui.raml.RamlImporter;
import com.smartbear.swagger.SwaggerImporter;
import com.smartbear.swagger.SwaggerUtils;

import javax.json.JsonObject;
import javax.json.stream.JsonParsingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class ApiRequest {
    private final String baseUrl;
    public ApiRequest(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public JsonObject getAll() throws IOException {
        return execute(String.format("%s/apis?limit=10000", baseUrl));
    }

    public JsonObject search(String value) throws IOException {
        return execute(String.format("%s/search?q=%s&limit=10000", baseUrl, value));
    }

    public static RestService[] importAPItoProject(Format apiFormat,  WsdlProject project) throws Exception {
        String path = apiFormat.url;
        if (apiFormat.type == Format.Type.SWAGGER) {
            SwaggerImporter importer = SwaggerUtils.createSwaggerImporter(path, project);
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(SwaggerUtils.class.getClassLoader());
            SoapUI.log("Importing Swagger from [" + path + "]");
            try {
                return importer.importSwagger(path);
            } finally {
                Thread.currentThread().setContextClassLoader(contextClassLoader);
            }
        } else if (apiFormat.type == Format.Type.RAML) {
            String expUrl = PathUtils.expandPath(path, project);

            // if this is a file - convert it to a file URL
            if (new File(expUrl).exists())
                expUrl = new File(expUrl).toURI().toURL().toString();
            RamlImporter importer = new RamlImporter(project);
            importer.setCreateSampleRequests(true);
            RestService rest = importer.importRaml(expUrl);
            return new RestService[] { rest };
        } else if (apiFormat.type == Format.Type.WADL) {
            RestService rest = (RestService) project
                    .addNewInterface(ModelItemNamer.createName("Service", project.getInterfaceList()), RestServiceFactory.REST_TYPE);
            WadlImporter importer = new WadlImporter(rest);
            importer.initFromWadl(Helper.stringToUrl(path).toString());
            return new RestService[] { rest };
        } else {
            throw new Exception("Unexpected format of the API description: " + apiFormat.type);
        }
    }

    private JsonObject execute(String urlString) throws IOException {
        URL url = new URL(urlString);

        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        try {
            connection.connect();
        } catch (UnknownHostException e) {
            throw new IOException(String.format(Strings.Executing.UNAVAILABLE_HOST_ERROR, url), e);
        }

        Reader reader;
        try {
            reader = new InputStreamReader(connection.getInputStream());
        } catch (FileNotFoundException e) {
            throw new IOException(String.format(Strings.Executing.UNAVAILABLE_DATA_ERROR, url), e);
        }

        try (javax.json.JsonReader jsonReader = javax.json.Json.createReader(reader)) {
            return jsonReader.readObject();
        } catch (JsonParsingException e) {
            throw new IOException(String.format(Strings.Executing.UNEXPECTED_RESPONSE_FORMAT_ERROR, url), e);
        }
    }
}
