package com.smartbear.apisio.ui;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.support.StringUtils;
import com.eviware.soapui.support.UISupport;
import com.eviware.x.dialogs.Worker;
import com.eviware.x.dialogs.XProgressDialog;
import com.eviware.x.dialogs.XProgressMonitor;
import com.smartbear.apisio.ApiRequest;
import com.smartbear.apisio.Helper;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.entities.importx.Api;
import com.smartbear.apisio.entities.importx.ApiList;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ApiListLoader implements Worker {

    public static class Result {
        public List<Api> apis = null;
        public String error = null;
        public boolean canceled = false;

        public void addError(String errorText) {
            apis = null;
            error = error == null ? errorText : error + "\n" + errorText;
        }

        public void cancel() {
            canceled = true;
            apis = null;
        }
    }

    private final String searchString;
    private final XProgressDialog waitDialog;
    public final Result result;

    private ApiListLoader(String searchString, XProgressDialog waitDialog) {
        this.searchString = searchString;
        this.waitDialog = waitDialog;
        this.result = new Result();
    }

    public static Result downloadList(String searchString) {
        ApiListLoader worker = new ApiListLoader(searchString, UISupport.getDialogs().createProgressDialog(Strings.Executing.QUERY_API_PROGRESS, 0, "", true));
        try {
            worker.waitDialog.run(worker);
        } catch (Exception e) {
            SoapUI.logError(e);
            worker.result.addError(e.getMessage());
        }

        return worker.result;
    }

    @Override
    public Object construct(XProgressMonitor xProgressMonitor) {
        try {
            ApiRequest request = new ApiRequest("http://apis.io/api");
            JsonObject json = request.search(this.searchString);
            ApiList list = new ApiList(json);
            result.apis = sort(list.apis);
        } catch (Throwable e) {
            SoapUI.logError(e);
            String error = e.getMessage();
            if (StringUtils.isNullOrEmpty(error)) {
                error = e.getClass().getName();
            }
            result.addError(error);
        }
        return null;
    }

    @Override
    public void finished() {
        if (result.canceled) {
            return;
        }
        waitDialog.setVisible(false);
    }

    @Override
    public boolean onCancel() {
        result.cancel();
        waitDialog.setVisible(false);
        return true;
    }

    private List<Api> sort(List<Api> source) {
        List<Api> result = new ArrayList<>();

        Comparator<Api> comp = new Comparator<Api>() {
            @Override
            public int compare(Api o1, Api o2) {
                return o1.name.compareTo(o2.name);
            }
        };

        Helper.Predicate<Api> withDescriptionPredicate = new Helper.Predicate<Api>() {
            @Override
            public boolean execute(Api value) {
                return value.formats.size() > 0;
            }
        };

        Helper.Predicate<Api> withoutDescriptionPredicate = new Helper.Predicate<Api>() {
            @Override
            public boolean execute(Api value) {
                return value.formats.size() == 0;
            }
        };

        List<Api> apisWithDescription = Helper.filter(source, withDescriptionPredicate);
        Collections.sort(apisWithDescription, comp);
        result.addAll(apisWithDescription);

        List<Api> apisWithoutDescription = Helper.filter(source, withoutDescriptionPredicate);
        Collections.sort(apisWithoutDescription, comp);
        result.addAll(apisWithoutDescription);

        return result;
    }
}
