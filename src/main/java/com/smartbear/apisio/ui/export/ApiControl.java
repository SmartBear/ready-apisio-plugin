package com.smartbear.apisio.ui.export;

import com.eviware.soapui.support.StringUtils;
import com.eviware.soapui.support.UISupport;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.entities.export.Api;
import com.smartbear.apisio.ui.validation.FieldValidator;
import com.smartbear.apisio.ui.validation.ValidationError;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Font;

public final class ApiControl {
    private final JPanel container = new JPanel();
    private final MigLayout layout = new MigLayout("wrap 2", "[][grow, fill]");
    private final FieldValidator fieldValidator = new FieldValidator();

    private final JLabel title              = new JLabel(Strings.ApiControl.TITLE);
    private final JTextField name           = new JTextField();
    private final JTextField description    = new JTextField();
    private final JTextField image          = new JTextField();
    private final JTextField baseUrl        = new JTextField();
    private final JTextField humanUrl       = new JTextField();
    private final JTextField tags           = new JTextField();
    private final JTextField definition     = new JTextField();
    private final JComboBox definitionType  = new JComboBox();

    public ApiControl() {
        container.setLayout(layout);

        Font oldFont = title.getFont();
        Font newFont = new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2);
        title.setFont(newFont);

        container.add(title, "span 2, gapbottom 20");
        container.add(new JLabel(Strings.ApiControl.NAME));
        container.add(name);
        container.add(new JLabel(Strings.ApiControl.DESCRIPTION));
        container.add(description);
        container.add(new JLabel(Strings.ApiControl.IMAGE));
        container.add(image);
        container.add(new JLabel(Strings.ApiControl.BASE_URL));
        container.add(baseUrl);
        container.add(new JLabel(Strings.ApiControl.HUMAN_URL));
        container.add(humanUrl);
        container.add(new JLabel(Strings.ApiControl.TAGS));
        container.add(tags);
        container.add(new JLabel(Strings.ApiControl.DEFINITION));
        container.add(definition, "split 2, growx");

        definitionType.addItem("Swagger");
        definitionType.addItem("WADL");
        definitionType.addItem("RAML");
        container.add(definitionType);

        setToolTips();
    }

    public Component getComponent() {
        return container;
    }

    public void updateFrom(Api api) {
        this.title.setText(api.name + " description");
        this.name.setText(api.name);
        this.description.setText(api.description);
        this.image.setText(api.image);
        this.baseUrl.setText(api.baseUrl);
        this.humanUrl.setText(api.humanUrl);
        this.tags.setText(StringUtils.join(api.tags.toArray(new String[]{}), ","));
        this.definition.setText(api.propertyUrl);
        this.definitionType.setSelectedItem(api.propertyType);
    }

    public void applyTo(Api api) {
        api.name        = this.name.getText();
        api.description = this.description.getText();
        api.image       = this.image.getText();
        api.baseUrl     = this.baseUrl.getText();
        api.humanUrl    = this.humanUrl.getText();
        api.propertyUrl = this.definition.getText();
        api.propertyType = this.definitionType.getSelectedItem().toString();

        for (String item: this.tags.getText().split(",")) {
            if (!item.trim().isEmpty()) {
                api.tags.add(item.trim());
            }
        }
    }

    public boolean validate() {
        ValidationError error = fieldValidator.validateRequired(name, "Name", 5);
        if (error == null) {
            error = fieldValidator.validateRequired(description, "Description", 5);
        }
        if (error == null) {
            error = fieldValidator.validateUrl(baseUrl, "Base URL");
        }
        if (error == null) {
            error = fieldValidator.validateUrl(humanUrl, "Human URL");
        }
        if (error == null) {
            error = fieldValidator.validateTags(tags, "tags");
        }

        if (error != null) {
            UISupport.showErrorMessage(error.message);
            error.field.requestFocus();
        }

        return error == null;
    }

    private void setToolTips() {
        name.setToolTipText(Strings.ApiControl.NAME_HINT);
        description.setToolTipText(Strings.ApiControl.DESCRIPTION_HINT);
        image.setToolTipText(Strings.ApiControl.IMAGE_HINT);
        baseUrl.setToolTipText(Strings.ApiControl.BASE_URL_HINT);
        humanUrl.setToolTipText(Strings.ApiControl.HUMAN_URL_HINT);
        tags.setToolTipText(Strings.ApiControl.TAGS_HINT);
        definition.setToolTipText(Strings.ApiControl.DEFINITION_HINT);
    }
}