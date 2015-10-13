package com.smartbear.apisio.actions;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.ModelItem;
import com.eviware.soapui.plugins.ActionConfiguration;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.support.AbstractSoapUIAction;
import com.smartbear.ActionGroups;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.entities.export.Domain;
import com.smartbear.apisio.ui.export.ExportApiListDialog;
import com.smartbear.utils.JsonFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@ActionConfiguration(actionGroup = ActionGroups.OPEN_PROJECT_ACTIONS, afterAction = "ExportWadlAction", separatorBefore = false)
public class ExportProjectApisJsonAction extends AbstractSoapUIAction<WsdlProject> {
    public ExportProjectApisJsonAction() {
        super(Strings.ExportApiAction.NAME, Strings.ExportApiAction.DESCRIPTION);
    }

    @Override
    public void perform(WsdlProject wsdlProject, Object o) {
        Domain domain = new Domain();
        domain.name = wsdlProject.getName();
        domain.description = wsdlProject.getDescription();
        for (ModelItem item: wsdlProject.getInterfaceList()) {
            if (item instanceof RestService) {
                RestService service = (RestService)item;
                domain.addApi(service.getName(), service.getDescription());
            }
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
