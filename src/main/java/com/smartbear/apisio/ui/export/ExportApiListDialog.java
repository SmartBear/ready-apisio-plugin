package com.smartbear.apisio.ui.export;

import com.eviware.x.form.support.AField;
import com.eviware.x.form.support.AForm;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.entities.export.Domain;
import com.smartbear.apisio.ui.WizardDialogBase;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.io.File;

public final class ExportApiListDialog extends WizardDialogBase {

    public static class Result {
        public final Domain domain;
        public final String fileName;

        public Result(Domain domain, String folderName) {
            this.domain = domain;
            this.fileName = folderName + File.separator + domain.name.replaceAll("[\\\\:/]", "") + ".json";
        }
    }

    private final JPanel container;
    private final CardLayout cardLayout;
    private final DomainControl domainControl;
    private final ApiControl apiControl;
    private final SelectFolderControl folderControl;
    private final Domain domain;
    private String folderName;

    public ExportApiListDialog(Domain domain) {
        super(Form.class, domain.apis.size() + 2);

        this.domain = domain;
        this.domainControl = new DomainControl();
        this.apiControl = new ApiControl();
        this.folderControl = new SelectFolderControl();

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);
        container.add(this.domainControl.getComponent(), "DOMAIN");
        container.add(this.apiControl.getComponent(), "API");
        container.add(this.folderControl.getComponent(), "FOLDER");

        super.dialog.getFormField(Form.CONTAINER).setProperty("component", container);
        super.dialog.getFormField(Form.CONTAINER).setProperty("preferredSize", new Dimension(500, 300));

        updatePageContent(0);
    }

    public Result show() {
        return super.showDialog() ? new Result(domain, folderName) : null;
    }

    @Override
    public void updatePageContent(int pageIndex) {
        if (pageIndex == 0) {
            this.domainControl.updateFrom(domain);
            cardLayout.show(container, "DOMAIN");
        } else if (pageIndex == domain.apis.size() + 1) {
            cardLayout.show(container, "FOLDER");
        } else {
            this.apiControl.updateFrom(domain.apis.get(pageIndex - 1));
            cardLayout.show(container, "API");
        }
    }

    @Override
    public void readPageContent(int pageIndex) {
        if (pageIndex == 0) {
            this.domainControl.applyTo(domain);
        } else if (pageIndex == domain.apis.size() + 1) {
            folderName = folderControl.getFolderName();
        } else {
            this.apiControl.applyTo(domain.apis.get(pageIndex - 1));
        }
    }

    @Override
    public boolean validate(int pageIndex) {
        if (pageIndex == 0) {
            return domainControl.validate();
        } else if (pageIndex == domain.apis.size() + 1) {
            return folderControl.validate();
        } else {
            return apiControl.validate();
        }
    }

    @AForm(name = Strings.ExportApiListDialog.CAPTION, description = Strings.ExportApiListDialog.DESCRIPTION, helpUrl = "https://github.com/SmartBear/ready-apis-plugin.git")
    public interface Form {
        @AField(name = " ", description = "", type = AField.AFieldType.COMPONENT)
        public static final String CONTAINER = " ";
    }
}
