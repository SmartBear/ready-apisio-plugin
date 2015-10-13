package com.smartbear.apisio.actions;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.plugins.ActionConfiguration;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.support.AbstractSoapUIAction;
import com.smartbear.ActionGroups;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.entities.export.Api;
import com.smartbear.apisio.entities.export.Domain;
import com.smartbear.apisio.ui.export.ExportApiListDialog;
import com.smartbear.utils.JsonFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@ActionConfiguration(actionGroup = ActionGroups.REST_SERVICE_ACTIONS, afterAction = "ExportWadlAction", separatorBefore = false)
public class ExportApisJsonAction extends AbstractSoapUIAction<RestService> {
    public ExportApisJsonAction() {
        super(Strings.ExportApiAction.NAME, Strings.ExportApiAction.DESCRIPTION);
    }

    @Override
    public void perform(RestService restService, Object o) {
        Domain domain = new Domain();
        domain.name = restService.getName();
        domain.description = restService.getDescription();
        Api api = domain.addApi(restService.getName(), restService.getDescription());
        if (restService.getEndpoints().length > 0) {
            api.baseUrl = restService.getEndpoints()[0];
            api.humanUrl= restService.getEndpoints()[0];
        }
        ExportApiListDialog.Result dialogResult;
        try (ExportApiListDialog dlg = new ExportApiListDialog(domain)) {
            dialogResult = dlg.show();
        }

        if (dialogResult == null) {
            return;
        }

        File f = new File(dialogResult.fileName);
        try (FileWriter fw = new FileWriter(f)) {
            JsonFormatter jsonFormatter = new JsonFormatter();
            fw.write(jsonFormatter.prettyPrint(dialogResult.domain.toJson().toString()));
        } catch (IOException e) {
            SoapUI.logError(e);
            UISupport.showErrorMessage(e);
        }
    }
}
