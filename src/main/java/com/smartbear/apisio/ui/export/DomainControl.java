package com.smartbear.apisio.ui.export;

import com.eviware.soapui.support.StringUtils;
import com.eviware.soapui.support.UISupport;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.entities.export.Domain;
import com.smartbear.apisio.ui.validation.FieldValidator;
import com.smartbear.apisio.ui.validation.ValidationError;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Font;

public final class DomainControl {
    private final JPanel container = new JPanel();
    private final MigLayout layout = new MigLayout("wrap 2", "[][grow, fill]");
    private final FieldValidator fieldValidator = new FieldValidator();

    private final JLabel title              = new JLabel(Strings.DomainControl.TITLE);
    private final JTextField name           = new JTextField();
    private final JTextField description    = new JTextField();
    private final JTextField url            = new JTextField();
    private final JTextField image          = new JTextField();
    private final JTextField maintainer     = new JTextField();
    private final JTextField created        = new JTextField();
    private final JTextField modified       = new JTextField();
    private final JTextField tags           = new JTextField();

    public DomainControl() {
        container.setLayout(layout);
        Font oldFont = title.getFont();
        Font newFont = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2);
        title.setFont(newFont);
        container.add(title, "span 2, gapbottom 20");
        container.add(new JLabel(Strings.DomainControl.NAME));
        container.add(name);
        container.add(new JLabel(Strings.DomainControl.DESCRIPTION));
        container.add(description);
        container.add(new JLabel(Strings.DomainControl.URL));
        container.add(url);
        container.add(new JLabel(Strings.DomainControl.IMAGE));
        container.add(image);
        container.add(new JLabel(Strings.DomainControl.MAINTAINER));
        container.add(maintainer);
        container.add(new JLabel(Strings.DomainControl.CREATED));
        container.add(created);
        container.add(new JLabel(Strings.DomainControl.MODIFIED));
        container.add(modified);
        container.add(new JLabel(Strings.DomainControl.TAGS));
        container.add(tags);

        setToolTips();
    }

    public Component getComponent() {
        return container;
    }

    public void updateFrom(Domain domain) {
        this.name.setText(domain.name);
        this.description.setText(domain.description);
        this.url.setText(domain.url);
        this.image.setText(domain.image);
        this.maintainer.setText(domain.maintainer);
        this.created.setText(domain.created);
        this.modified.setText(domain.modified);
        this.tags.setText(StringUtils.join(domain.tags.toArray(new String[]{}), ","));
    }

    public void applyTo(Domain domain) {
        domain.name = this.name.getText();
        domain.description = this.description.getText();
        domain.url = this.url.getText();
        domain.image = this.image.getText();
        domain.maintainer = this.maintainer.getText();
        domain.created = this.created.getText();
        domain.modified = this.modified.getText();
        for (String item: this.tags.getText().split(",")) {
            if (!item.trim().isEmpty()) {
                domain.tags.add(item.trim());
            }
        }
    }

    public boolean validate() {
        ValidationError error = fieldValidator.validateRequired(name, "Name", 5);
        if (error == null) {
            error = fieldValidator.validateRequired(description, "Description", 5);
        }
        if (error == null) {
            error = fieldValidator.validateUrl(url, "URL");
        }
        if (error == null) {
            error = fieldValidator.validateDate(created, "Created");
        }
        if (error == null) {
            error = fieldValidator.validateDate(modified, "Modified");
        }
        if (error == null) {
            error = fieldValidator.validateTags(tags, "Tags");
        }

        if (error != null) {
            UISupport.showErrorMessage(error.message);
            error.field.requestFocus();
        }

        return error == null;
    }

    private void setToolTips() {
        name.setToolTipText(Strings.DomainControl.NAME_HINT);
        description.setToolTipText(Strings.DomainControl.DESCRIPTION_HINT);
        url.setToolTipText(Strings.DomainControl.URL_HINT);
        image.setToolTipText(Strings.DomainControl.IMAGE_HINT);
        maintainer.setToolTipText(Strings.DomainControl.MAINTAINER_HINT);
        created.setToolTipText(Strings.DomainControl.CREATED_HINT);
        modified.setToolTipText(Strings.DomainControl.MODIFIED_HINT);
        tags.setToolTipText(Strings.DomainControl.TAGS_HINT);
    }
}
