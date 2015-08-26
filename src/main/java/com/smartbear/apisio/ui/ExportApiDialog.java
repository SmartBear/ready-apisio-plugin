package com.smartbear.apisio.ui;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.rest.RestService;
import com.eviware.x.form.ValidationMessage;
import com.eviware.x.form.XFormDialog;
import com.eviware.x.form.XFormField;
import com.eviware.x.form.XFormFieldValidator;
import com.eviware.x.form.support.ADialogBuilder;
import com.eviware.x.form.support.AField;
import com.eviware.x.form.support.AForm;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.entities.export.Api;
import com.smartbear.apisio.entities.export.Domain;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportApiDialog implements AutoCloseable {
    private final String urlRegex = "^(http|https)://[\\w.-]+$";
    private final XFormDialog dialog;
    private final JComboBox definitionType;
    private final JTextField definitionUrl;

    public ExportApiDialog(RestService rest) {
        dialog = ADialogBuilder.buildDialog(Form.class);
        if (rest != null) {
            //DOMAIN section
            dialog.setValue(Form.NAME, rest.getName());
            dialog.setValue(Form.DESCRIPTION, rest.getDescription());
            dialog.setValue(Form.URL, extractDomainFromURL(rest.getBasePath()));

            //API section
            dialog.setValue(Form.API_NAME, rest.getName());
            dialog.setValue(Form.API_DESCRIPTION, rest.getDescription());
        }

        Date now = new Date();
        dialog.setValue(Form.CREATED, String.format("%tY-%tm-%td", now, now, now));
        dialog.setValue(Form.MODIFIED, String.format("%tY-%tm-%td", now, now, now));

        setRequiredFieldValidator(dialog.getFormField(Form.FOLDER_NAME), "folder", 0);
        setRequiredFieldValidator(dialog.getFormField(Form.NAME), "name", 5);
        setRequiredFieldValidator(dialog.getFormField(Form.DESCRIPTION), "description", 5);
        setUrlFieldValidator(dialog.getFormField(Form.URL), "URL");
        setDateFieldValidator(dialog.getFormField(Form.CREATED), "creation date");
        setDateFieldValidator(dialog.getFormField(Form.MODIFIED), "modification date");
        setTagsFieldValidator(dialog.getFormField(Form.TAGS));

        //API fields validation
        setRequiredFieldValidator(dialog.getFormField(Form.API_NAME), "API name", 5);
        setRequiredFieldValidator(dialog.getFormField(Form.API_DESCRIPTION), "API description", 5);
        setUrlFieldValidator(dialog.getFormField(Form.API_BASE_URL), "Base URL");
        setUrlFieldValidator(dialog.getFormField(Form.API_HUMAN_URL), "Human URL");

        definitionType = new JComboBox();
        definitionType.addItem("Swagger");
        definitionType.addItem("WADL");
        definitionType.addItem("RAML");
        definitionUrl = new JTextField();
        definitionType.setToolTipText("Definition type");
        definitionUrl.setToolTipText("Definition URL");

        JPanel component = new JPanel();
        BoxLayout layout = new BoxLayout(component, BoxLayout.Y_AXIS);
        component.setLayout(layout);
        component.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        component.add(definitionType);
        component.add(Box.createRigidArea(new Dimension(0, 5)));
        component.add(definitionUrl);
        component.add(Box.createRigidArea(new Dimension(0, 5)));

        dialog.getFormField(Form.DEFINITION).setProperty("preferredSize", new Dimension(445, 55));
        dialog.getFormField(Form.DEFINITION).setProperty("component", component);
        final Pattern urlPattern = Pattern.compile(urlRegex);
        dialog.getFormField(Form.DEFINITION).addFormFieldValidator(new XFormFieldValidator() {
            @Override
            public ValidationMessage[] validateField(XFormField xFormField) {
                String text = definitionUrl.getText().trim();
                if (!text.isEmpty() && !urlPattern.matcher(text).matches()) {
                    return new ValidationMessage[]{new ValidationMessage(String.format(Strings.ExportApiDialog.INVALID_URL_FORMAT, "Definition URL", urlRegex), xFormField)};
                }
                return new ValidationMessage[0];
            }
        });
    }

    public Result show() {
        return dialog.show() ? new Result() : null;
    }

    @Override
    public void close() {
        dialog.release();
    }

    private String extractDomainFromURL(String url) {
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")) {
            url = "http://" + url;
        }
        try {
            URI uri = new URI(url);
            return String.format("%s://%s", uri.getScheme(), uri.getHost());
        } catch (URISyntaxException e) {
            return url;
        }
    }

    private void setUrlFieldValidator(XFormField field, final String fieldName) {
        final Pattern p = Pattern.compile(urlRegex);
        field.addFormFieldValidator(new XFormFieldValidator() {
            @Override
            public ValidationMessage[] validateField(XFormField xFormField) {
                if (xFormField.getValue().isEmpty()) {
                    return new ValidationMessage[]{new ValidationMessage(String.format(Strings.ExportApiDialog.EMPTY_VALUE, fieldName), xFormField)};
                }
                Matcher m = p.matcher(xFormField.getValue());
                if (!m.matches()) {
                    return new ValidationMessage[]{new ValidationMessage(String.format(Strings.ExportApiDialog.INVALID_URL_FORMAT, fieldName, urlRegex), xFormField)};
                }
                return new ValidationMessage[0];
            }
        });
    }

    private void setRequiredFieldValidator(XFormField field, final String fieldName, final int minimumValueLength) {
        field.addFormFieldValidator(new XFormFieldValidator() {
            @Override
            public ValidationMessage[] validateField(XFormField xFormField) {
                if (xFormField.getValue().isEmpty()) {
                    return new ValidationMessage[]{new ValidationMessage(String.format(Strings.ExportApiDialog.EMPTY_VALUE, fieldName), xFormField)};
                } else if (xFormField.getValue().length() < minimumValueLength) {
                    return new ValidationMessage[]{new ValidationMessage(String.format(Strings.ExportApiDialog.TOO_SHORT_VALUE, fieldName), xFormField)};
                } else {
                    return new ValidationMessage[0];
                }
            }
        });
    }

    private void setDateFieldValidator(XFormField field, final String fieldName) {
        final String pattern = "^\\d\\d\\d\\d-\\d\\d-\\d\\d$";
        final Pattern p = Pattern.compile(pattern);
        field.addFormFieldValidator(new XFormFieldValidator() {
            @Override
            public ValidationMessage[] validateField(XFormField xFormField) {
                if (xFormField.getValue().isEmpty()) {
                    return new ValidationMessage[]{new ValidationMessage(String.format(Strings.ExportApiDialog.EMPTY_VALUE, fieldName), xFormField)};
                }
                Matcher m = p.matcher(xFormField.getValue());
                if (!m.matches()) {
                    return new ValidationMessage[]{new ValidationMessage(String.format(Strings.ExportApiDialog.INVALID_DATE_VALUE), xFormField)};
                }
                return new ValidationMessage[0];
            }
        });
    }

    private void setTagsFieldValidator(XFormField field) {
        final String pattern = "^[\\w\\-]+(,(\\s)?[\\w\\-]+)*$";
        final Pattern p = Pattern.compile(pattern);
        field.addFormFieldValidator(new XFormFieldValidator() {
            @Override
            public ValidationMessage[] validateField(XFormField xFormField) {
                String value = xFormField.getValue();
                Matcher m = p.matcher(value);
                if (!value.isEmpty() && !m.matches()) {
                    return new ValidationMessage[]{new ValidationMessage(String.format(Strings.ExportApiDialog.INVALID_TAGS_FORMAT), xFormField)};
                }
                return new ValidationMessage[0];
            }
        });
    }

    public class Result {
        public final String fileName;
        public final Domain domain;

        public Result() {
            this.domain = new Domain();
            domain.name = dialog.getValue(Form.NAME);
            domain.description = dialog.getValue(Form.DESCRIPTION);
            domain.url = dialog.getValue(Form.URL);
            domain.image = dialog.getValue(Form.IMAGE);
            domain.created = dialog.getValue(Form.CREATED);
            domain.modified = dialog.getValue(Form.MODIFIED);
            domain.maintainer = dialog.getValue(Form.MAINTAINER);
            for (String item: dialog.getValue(Form.TAGS).split(",")) {
                if (!item.trim().isEmpty()) {
                    domain.tags.add(item.trim());
                }
            }

            Api api = new Api();
            api.name = dialog.getValue(Form.API_NAME);
            api.description = dialog.getValue(Form.API_DESCRIPTION);
            api.image = dialog.getValue(Form.API_IMAGE);
            api.baseUrl = dialog.getValue(Form.API_BASE_URL);
            api.humanUrl = dialog.getValue(Form.API_HUMAN_URL);
            for (String item: dialog.getValue(Form.API_TAGS).split(",")) {
                if (!item.trim().isEmpty()) {
                    api.tags.add(item.trim());
                }
            }
            //TODO: not implemented
            //api.propertyType = dialog.getValue(Form.PROPERTY_TYPE);
            //api.propertyUrl = dialog.getValue(Form.PROPERTY_URL).trim();
            api.propertyType = definitionType.getSelectedItem().toString();
            api.propertyUrl = definitionUrl.getText();
            domain.apis.add(api);

            this.fileName = dialog.getValue(Form.FOLDER_NAME) + File.separator + domain.name.replaceAll("[\\\\:/]", "") + ".json";
        }
    }

    @AForm(name = Strings.ExportApiDialog.CAPTION, description = Strings.ExportApiDialog.DESCRIPTION)
    public interface Form {
        @AField(name = Strings.ExportApiDialog.FOLDER_NAME, description = Strings.ExportApiDialog.FOLDER_NAME_HINT, type = AField.AFieldType.FOLDER)
        public final static String FOLDER_NAME = Strings.ExportApiDialog.FOLDER_NAME;
        @AField(description = "", type = AField.AFieldType.SEPARATOR)
        public final static String FOLDER_UNDERLINE = "FolderUnderline";

        @AField(description = Strings.ExportApiDialog.DOMAIN_SECTION, type = AField.AFieldType.SEPARATOR)
        public final static String DOMAIN = Strings.ExportApiDialog.DOMAIN_SECTION;
        @AField(description = "", type = AField.AFieldType.SEPARATOR)
        public final static String DOMAIN_UNDERLINE = "DomainUnderline";
        @AField(name = Strings.ExportApiDialog.DOMAIN_NAME, description = Strings.ExportApiDialog.DOMAIN_NAME_HINT, type = AField.AFieldType.STRING)
        public final static String NAME = Strings.ExportApiDialog.DOMAIN_NAME;
        @AField(name = Strings.ExportApiDialog.DOMAIN_DESCRIPTION, description = Strings.ExportApiDialog.DOMAIN_DESCRIPTION_HINT, type = AField.AFieldType.STRING)
        public final static String DESCRIPTION = Strings.ExportApiDialog.DOMAIN_DESCRIPTION;
        @AField(name = Strings.ExportApiDialog.DOMAIN_URL, description = Strings.ExportApiDialog.DOMAIN_URL_HINT, type = AField.AFieldType.STRING)
        public final static String URL = Strings.ExportApiDialog.DOMAIN_URL;
        @AField(name = Strings.ExportApiDialog.DOMAIN_IMAGE, description = Strings.ExportApiDialog.DOMAIN_IMAGE_HINT, type = AField.AFieldType.STRING)
        public final static String IMAGE = Strings.ExportApiDialog.DOMAIN_IMAGE;
        @AField(name = Strings.ExportApiDialog.DOMAIN_MAINTAINER, description = Strings.ExportApiDialog.DOMAIN_MAINTAINER_HINT, type = AField.AFieldType.STRING)
        public final static String MAINTAINER = Strings.ExportApiDialog.DOMAIN_MAINTAINER;
        @AField(name = Strings.ExportApiDialog.DOMAIN_CREATED, description = Strings.ExportApiDialog.DOMAIN_CREATED_HINT, type = AField.AFieldType.STRING)
        public final static String CREATED = Strings.ExportApiDialog.DOMAIN_CREATED;
        @AField(name = Strings.ExportApiDialog.DOMAIN_MODIFIED, description = Strings.ExportApiDialog.DOMAIN_MODIFIED_HINT, type = AField.AFieldType.STRING)
        public final static String MODIFIED = Strings.ExportApiDialog.DOMAIN_MODIFIED;
        @AField(name = Strings.ExportApiDialog.DOMAIN_TAGS, description = Strings.ExportApiDialog.DOMAIN_TAGS_HINT, type = AField.AFieldType.STRING)
        public final static String TAGS = Strings.ExportApiDialog.DOMAIN_TAGS;

        @AField(description = Strings.ExportApiDialog.API_SECTION, type = AField.AFieldType.SEPARATOR)
        public final static String API = Strings.ExportApiDialog.API_SECTION;
        @AField(description = "", type = AField.AFieldType.SEPARATOR)
        public final static String API_UNDERLINE = "APIUnderline";
        @AField(name = Strings.ExportApiDialog.API_NAME, description = Strings.ExportApiDialog.API_NAME_HINT, type = AField.AFieldType.STRING)
        public final static String API_NAME = Strings.ExportApiDialog.API_NAME;
        @AField(name = Strings.ExportApiDialog.API_DESCRIPTION, description = Strings.ExportApiDialog.API_DESCRIPTION_HINT, type = AField.AFieldType.STRING)
        public final static String API_DESCRIPTION = Strings.ExportApiDialog.API_DESCRIPTION;

        @AField(name = Strings.ExportApiDialog.API_IMAGE, description = Strings.ExportApiDialog.API_IMAGE_HINT, type = AField.AFieldType.STRING)
        public final static String API_IMAGE = Strings.ExportApiDialog.API_IMAGE;
        @AField(name = Strings.ExportApiDialog.API_BASE_URL, description = Strings.ExportApiDialog.API_BASE_URL_HINT, type = AField.AFieldType.STRING)
        public final static String API_BASE_URL = Strings.ExportApiDialog.API_BASE_URL;
        @AField(name = Strings.ExportApiDialog.API_HUMAN_URL, description = Strings.ExportApiDialog.API_HUMAN_URL_HINT, type = AField.AFieldType.STRING)
        public final static String API_HUMAN_URL = Strings.ExportApiDialog.API_HUMAN_URL;
        @AField(name = Strings.ExportApiDialog.API_TAGS, description = Strings.ExportApiDialog.API_TAGS_HINT, type = AField.AFieldType.STRING)
        public final static String API_TAGS = Strings.ExportApiDialog.API_TAGS;

//        @AField(name = "Type", description = "", type = AField.AFieldType.COMBOBOX, values = {"Swagger", "WADL", "RAML"})
//        public final static String PROPERTY_TYPE = "Type";
//        @AField(name = "URL:___", description = "", type = AField.AFieldType.STRING)
//        public final static String PROPERTY_URL = "URL:___";
        @AField(name = "Definition", description = "", type = AField.AFieldType.COMPONENT)
        public final static String DEFINITION = "Definition";
    }
}
