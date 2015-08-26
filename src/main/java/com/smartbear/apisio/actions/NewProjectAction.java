package com.smartbear.apisio.actions;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.WorkspaceImpl;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.plugins.auto.PluginImportMethod;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.support.AbstractSoapUIAction;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.ui.ApiImporter;
import com.smartbear.apisio.ui.SearchApiDialog;
import com.smartbear.rapisupport.ServiceFactory;

import java.util.List;

@PluginImportMethod(label = Strings.NewProjectAction.CAPTION)
public class NewProjectAction extends AbstractSoapUIAction<WorkspaceImpl>{
    public NewProjectAction() {
        super(Strings.NewProjectAction.NAME, Strings.NewProjectAction.DESCRIPTION);
    }

    @Override
    public void perform(WorkspaceImpl target, Object o) {
        SearchApiDialog.Result dialogResult = null;
        try (SearchApiDialog dlg = SearchApiDialog.buildNewProjectDialog()) {
            dialogResult = dlg.show();
        }

        if (dialogResult == null) {
            return;
        }
        if (dialogResult.selectedAPIs.size() == 0) {
            return;
        }

        WsdlProject wsdlProject;
        try {
            wsdlProject = target.createProject(dialogResult.projectName, null);
        } catch (Exception e) {
            SoapUI.logError(e);
            UISupport.showErrorMessage(String.format(Strings.NewProjectAction.UNABLE_CREATE_ERROR, e.getClass().getName(), e.getMessage()));
            return;
        }

        if (wsdlProject == null) {
            return;
        }

        List<RestService> services = ApiImporter.importServices(dialogResult.selectedAPIs, wsdlProject);
        ServiceFactory.Build(wsdlProject, services, dialogResult.entities);
        if (services.size() > 0) {
            UISupport.select(services.get(0));
        }
    }
}
