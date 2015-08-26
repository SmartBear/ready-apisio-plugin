package com.smartbear.apisio.ui;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.support.UISupport;
import com.eviware.x.dialogs.Worker;
import com.eviware.x.dialogs.XProgressDialog;
import com.eviware.x.dialogs.XProgressMonitor;
import com.smartbear.apisio.ApiRequest;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.entities.importx.Api;

import java.util.ArrayList;
import java.util.List;

public class ApiImporter implements Worker {
    private boolean canceled = false;
    private final XProgressDialog waitDialog;
    private final List<Api> apis;
    private final WsdlProject wsdlProject;
    private final List<RestService> addedServices = new ArrayList<>();
    private final StringBuilder errors = new StringBuilder();

    private ApiImporter(XProgressDialog waitDialog, List<Api> apis, WsdlProject wsdlProject) {
        this.waitDialog = waitDialog;
        this.apis = apis;
        this.wsdlProject = wsdlProject;
    }

    public static List<RestService> importServices(List<Api> apis, WsdlProject wsdlProject) {
        ApiImporter worker = new ApiImporter(UISupport.getDialogs().createProgressDialog(Strings.Executing.IMPORT_PROGRESS, 100, "", true), apis, wsdlProject);
        try {
            worker.waitDialog.run(worker);
        } catch (Exception e) {
            UISupport.showErrorMessage(e.getMessage());
            SoapUI.logError(e);
        }

        return worker.addedServices;
    }

    @Override
    public Object construct(XProgressMonitor xProgressMonitor) {
        for (Api api: apis) {
            try {
                RestService service = ApiRequest.importAPItoProject(api.getFormat(), wsdlProject);
                addedServices.add(service);
            } catch (Throwable e) {
                SoapUI.logError(e);
                errors.append(String.format(Strings.Executing.IMPORT_ERROR, api.name, e.getMessage()));
            }
        }
        return null;
    }

    @Override
    public void finished() {
        if (canceled) {
            return;
        }
        waitDialog.setVisible(false);
        if (errors.length() > 0) {
            UISupport.showErrorMessage(errors.toString());
        }
    }

    @Override
    public boolean onCancel() {
        canceled = true;
        waitDialog.setVisible(false);
        return true;
    }
}
