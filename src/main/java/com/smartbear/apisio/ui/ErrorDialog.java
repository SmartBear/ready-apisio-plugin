package com.smartbear.apisio.ui;

import com.eviware.x.form.XFormDialog;
import com.eviware.x.form.support.ADialogBuilder;
import com.eviware.x.form.support.AField;
import com.eviware.x.form.support.AForm;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.Dimension;
import java.util.List;

public class ErrorDialog implements AutoCloseable {
    private final List<ErrorInfo> errors;
    private final XFormDialog dialog;
    private final JTable errorsTable;

    public ErrorDialog(List<ErrorInfo> errorsInfo) {
        this.errors = errorsInfo;
        this.dialog = ADialogBuilder.buildDialog(Form.class);
        this.errorsTable = new JTable(new ErrorTableModel(this.errors));

        this.errorsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] rows = errorsTable.getSelectedRows();
                dialog.setValue(Form.DESCRIPTION, rows.length == 1 ? errors.get(rows[0]).error : "");
            }
        });

        this.dialog.getFormField(Form.ERRORS).setProperty("component", new JScrollPane(errorsTable));
        this.dialog.getFormField(Form.ERRORS).setProperty("preferredSize", new Dimension(500, 150));
    }

    public static void showErrors(final List<ErrorInfo> errors) {
        try {
            if(SwingUtilities.isEventDispatchThread()) {
                try (ErrorDialog dlg = new ErrorDialog(errors)) {
                    dlg.show();
                }
            } else {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        try (ErrorDialog dlg = new ErrorDialog(errors)) {
                            dlg.show();
                        }
                    }
                });
            }
        } catch (Exception ex) {
        }
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void close() {
        dialog.release();
    }

    @AForm(name = "Error", description = "An error occured")
    public interface Form {
        @AField(name = "Errors", description = "", type = AField.AFieldType.COMPONENT)
        public static final String ERRORS = "Errors";
        @AField(name = "Description", description = "", type = AField.AFieldType.INFORMATION)
        public static final String DESCRIPTION = "Description";
    }

    public static class ErrorInfo {
        public final String api;
        public final String url;
        public final String error;

        public ErrorInfo(String api, String url, String error) {
            this.api = api;
            this.url = url;
            this.error = error;
        }

        @Override
        public String toString() {
            return String.format("%s, %s, %s", api, url, error);
        }
    }

    public static class ErrorTableModel implements TableModel {
        private final List<ErrorInfo> errors;

        public ErrorTableModel(List<ErrorInfo> errors) {
            this.errors = errors;
        }

        @Override
        public int getRowCount() {
            return errors.size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) {
                return "API";
            } else if (columnIndex == 1) {
                return "URL";
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return errors.get(rowIndex).api;
            } else if (columnIndex == 1) {
                return errors.get(rowIndex).url;
            }
            return null;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override
        public void addTableModelListener(TableModelListener l) {
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
        }
    }
}