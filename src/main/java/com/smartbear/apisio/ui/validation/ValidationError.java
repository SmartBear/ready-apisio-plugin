package com.smartbear.apisio.ui.validation;

import javax.swing.JTextField;

public final class ValidationError {
    public final String message;
    public final JTextField field;

    public ValidationError(String message, JTextField field) {
        this.message = message;
        this.field = field;
    }
}
