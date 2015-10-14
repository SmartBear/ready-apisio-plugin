package com.smartbear.apisio.ui.validation;

import com.smartbear.apisio.Strings;

import javax.swing.JTextField;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FieldValidator {
    private static final Pattern datePattern = Pattern.compile("^\\d\\d\\d\\d-\\d\\d-\\d\\d$");
    private static final Pattern urlPattern = Pattern.compile("^(http|https)://[\\w.-]+(:\\d+)?(/[\\w.-]+)*(/?)$");
    private static final Pattern tagsPattern = Pattern.compile("^[\\w\\-]+(,(\\s)?[\\w\\-]+)*$");

    public ValidationError validateRequired(JTextField field, String fieldName, int minimumValueLength) {
        if (field.getText().isEmpty()) {
            return new ValidationError(String.format(Strings.FieldValidator.EMPTY_VALUE, fieldName), field);
        } else if (field.getText().length() < minimumValueLength) {
            return new ValidationError(String.format(Strings.FieldValidator.TOO_SHORT_VALUE, fieldName), field);
        } else {
            return null;
        }
    }

    public ValidationError validateDate(JTextField field, String fieldName) {
        if (field.getText().isEmpty()) {
            return new ValidationError(String.format(Strings.FieldValidator.EMPTY_VALUE, fieldName), field);
        }
        Matcher m = datePattern.matcher(field.getText());
        if (!m.matches()) {
            return new ValidationError(String.format(Strings.FieldValidator.INVALID_DATE_VALUE, fieldName), field);
        }
        return null;
    }

    public ValidationError validateUrl(JTextField field, String fieldName) {
        if (field.getText().isEmpty()) {
            return new ValidationError(String.format(Strings.FieldValidator.EMPTY_VALUE, fieldName), field);
        }
        Matcher m = urlPattern.matcher(field.getText());
        if (!m.matches()) {
            return new ValidationError(String.format(Strings.FieldValidator.INVALID_URL_FORMAT, fieldName, urlPattern.pattern()), field);
        }
        return null;
    }

    public ValidationError validateTags(JTextField field, String fieldName) {
        String value = field.getText();
        Matcher m = tagsPattern.matcher(value);
        if (!value.isEmpty() && !m.matches()) {
            return new ValidationError(Strings.FieldValidator.INVALID_TAGS_FORMAT, field);
        }
        return null;
    }

    public ValidationError validateFolderName(JTextField field, String fieldName) {
        if (field.getText().isEmpty()) {
            return new ValidationError(String.format(Strings.FieldValidator.EMPTY_VALUE, fieldName), field);
        }
        File f = new File(field.getText());
        if (!f.exists()) {
            return new ValidationError(Strings.FieldValidator.FOLDER_NOT_EXISTS, field);
        }

        return null;
    }
}
