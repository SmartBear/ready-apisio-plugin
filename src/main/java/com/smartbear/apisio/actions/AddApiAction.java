package com.smartbear.apisio.actions;

import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.plugins.ActionConfiguration;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.support.AbstractSoapUIAction;
import com.smartbear.ActionGroups;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.ui.ApiImporter;
import com.smartbear.apisio.ui.SearchApiDialog;
import com.smartbear.rapisupport.ServiceFactory;

import java.util.List;

@ActionConfiguration(actionGroup = ActionGroups.OPEN_PROJECT_ACTIONS, separatorBefore = true)
public class AddApiAction extends AbstractSoapUIAction<WsdlProject>{
    public AddApiAction() {
        super(Strings.AddApiAction.NAME, Strings.AddApiAction.DESCRIPTION);
    }

    @Override
    public void perform(WsdlProject wsdlProject, Object o) {
        SearchApiDialog.Result dialogResult = null;
        try (SearchApiDialog dlg = SearchApiDialog.buildAppApiDialog()) {
            dialogResult = dlg.show();
        }

        if (dialogResult == null) {
            return;
        }
        if (dialogResult.selectedAPIs.size() == 0) {
            return;
        }

        List<RestService> services = ApiImporter.importServices(dialogResult.selectedAPIs, wsdlProject);
        ServiceFactory.Build(wsdlProject, services, dialogResult.entities);
        if (services.size() > 0) {
            UISupport.select(services.get(0));
        }
    }
}
