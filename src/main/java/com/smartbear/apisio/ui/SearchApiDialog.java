package com.smartbear.apisio.ui;

import com.eviware.soapui.support.StringUtils;
import com.eviware.x.form.ValidationMessage;
import com.eviware.x.form.XFormDialog;
import com.eviware.x.form.XFormField;
import com.eviware.x.form.XFormFieldListener;
import com.eviware.x.form.XFormFieldValidator;
import com.eviware.x.form.support.ADialogBuilder;
import com.eviware.x.form.support.AField;
import com.eviware.x.form.support.AForm;
import com.smartbear.apisio.PluginConfig;
import com.smartbear.apisio.Strings;
import com.smartbear.apisio.entities.importx.Api;
import com.smartbear.rapisupport.Service;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class SearchApiDialog implements AutoCloseable {
    private final XFormDialog dialog;
    private final SearchControl searchControl;
    private List<Api> apis = new ArrayList<>();

    private final GridUI apiListComponent = new GridUI();

    public static SearchApiDialog buildNewProjectDialog() {
        SearchApiDialog result = new SearchApiDialog(ADialogBuilder.buildDialog(NewProjectForm.class));
        result.dialog.getFormField(NewProjectForm.PROJECT_NAME).addFormFieldValidator(new XFormFieldValidator() {
            @Override
            public ValidationMessage[] validateField(XFormField xFormField) {
                if (StringUtils.isNullOrEmpty(xFormField.getValue())) {
                    return new ValidationMessage[]{new ValidationMessage(Strings.NewProjectDialog.EMPTY_PROJECT_WARNING, xFormField)};
                }
                return new ValidationMessage[0];
            }
        });
        return result;
    }

    public static SearchApiDialog buildAppApiDialog() {
        SearchApiDialog result = new SearchApiDialog(ADialogBuilder.buildDialog(AddApiForm.class));
        return result;
    }

    private SearchApiDialog(XFormDialog dialog) {
        this.dialog = dialog;
        this.searchControl = new SearchControl();

        dialog.getFormField(AddApiForm.SEARCH).setProperty("component", this.searchControl);
        dialog.getFormField(AddApiForm.SEARCH).setProperty("preferredSize", new Dimension(500, 24));

        dialog.getFormField(AddApiForm.NAME).setProperty("component", new JScrollPane(apiListComponent.getComponent()));
        dialog.getFormField(AddApiForm.NAME).setProperty("preferredSize", new Dimension(500, 150));

        tuneProjectOptions();

        dialog.getFormField(AddApiForm.NAME).addFormFieldValidator(new XFormFieldValidator() {
            @Override
            public ValidationMessage[] validateField(XFormField xFormField) {
                List<Api> selected = apiListComponent.getSelected();
                if (selected == null || selected.size() == 0) {
                    return new ValidationMessage[]{new ValidationMessage(Strings.BaseDialog.NOTHING_SELECTED_WARNING, xFormField)};
                } else {
                    return new ValidationMessage[0];
                }
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

    public class Result {
        public final String projectName;
        public final List<Api> selectedAPIs;
        public final Set<Service> entities = EnumSet.noneOf(Service.class);

        public Result() {
            selectedAPIs = apiListComponent.getSelected();

            XFormField name = dialog.getFormField(NewProjectForm.PROJECT_NAME);
            projectName = name != null ? name.getValue() : null;

            if (dialog.getBooleanValue(BaseForm.TEST_SUITE)) {
                entities.add(Service.TEST_SUITE);
            }
            if (dialog.getBooleanValue(BaseForm.LOAD_TEST)) {
                entities.add(Service.LOAD_TEST);
            }
            if (dialog.getBooleanValue(BaseForm.VIRT)) {
                entities.add(Service.VIRT);
            }
            if (dialog.getBooleanValue(BaseForm.SECUR_TEST)) {
                entities.add(Service.SECUR_TEST);
            }
        }
    }

    public interface BaseForm {
        @AField(description = "Search", type = AField.AFieldType.COMPONENT)
        public final static String SEARCH = "Search";

        @AField(description = "API Name", type = AField.AFieldType.COMPONENT)
        public final static String NAME = "API";

        @AField(description = Strings.BaseDialog.DESCRIPTION_LABEL, type = AField.AFieldType.INFORMATION)
        public final static String DESCRIPTION = "Description";

        @AField(description = "", type = AField.AFieldType.SEPARATOR)
        public final static String SEPERATOR = "Separator";

        @AField(name = "###GenerateTestSuite", description = Strings.BaseDialog.GEN_TEST_SUITE, type = AField.AFieldType.BOOLEAN)
        public final static String TEST_SUITE = "###GenerateTestSuite";

        @AField(name = "###GenerateLoadTest", description = Strings.BaseDialog.GEN_LOAD_TEST, type = AField.AFieldType.BOOLEAN)
        public final static String LOAD_TEST = "###GenerateLoadTest";

        @AField(name = "###GenerateSecurTest", description = Strings.BaseDialog.GEN_SECUR_TEST, type = AField.AFieldType.BOOLEAN)
        public final static String SECUR_TEST = "###GenerateSecurTest";

        @AField(name = "###GenerateVirt", description = Strings.BaseDialog.GEN_VIRT_HOST, type = AField.AFieldType.BOOLEAN)
        public final static String VIRT = "###GenerateVirt";
    }

    @AForm(name = Strings.NewProjectDialog.CAPTION, description = Strings.NewProjectDialog.DESCRIPTION)
    public interface NewProjectForm extends BaseForm {
        @AField(name = Strings.NewProjectDialog.PROJECT_LABEL, description = Strings.NewProjectDialog.PROJECT_DESCRIPTION, type = AField.AFieldType.STRING)
        public final static String PROJECT_NAME = Strings.NewProjectDialog.PROJECT_LABEL;

        @AField(description = "", type = AField.AFieldType.SEPARATOR)
        public final static String SEPERATOR2 = "Separator2";
    }

    @AForm(name = Strings.SearchApiDialog.CAPTION, description = Strings.SearchApiDialog.DESCRIPTION)
    public interface AddApiForm extends BaseForm {
    }

    private void tuneProjectOptions() {
        final XFormField checkLoadTest = dialog.getFormField(BaseForm.LOAD_TEST);
        final XFormField checkSecurTest = dialog.getFormField(BaseForm.SECUR_TEST);
        checkLoadTest.setEnabled(false);
        checkSecurTest.setEnabled(false);
        dialog.getFormField(BaseForm.TEST_SUITE).addFormFieldListener(new XFormFieldListener() {
            @Override
            public void valueChanged(XFormField xFormField, String s, String s1) {
                boolean enabled = dialog.getBooleanValue(BaseForm.TEST_SUITE);
                checkLoadTest.setEnabled(enabled);
                checkSecurTest.setEnabled(enabled);
                if (!enabled) {
                    checkLoadTest.setValue("false");
                    checkSecurTest.setValue("false");
                }
            }
        });
    }

    private void searchApi() {
        ApiListLoader.Result res = ApiListLoader.downloadList(this.searchControl.getText());
        if (res.canceled) {
            return;
        }
        apis.clear();
        apis.addAll(res.apis);
        apiListComponent.updateUI();
    }

    private class SearchControl extends JPanel {
        private final JTextField searchField;
        private final JButton searchButton;

        public SearchControl() {
            this.searchField = new JTextField();
            this.searchField.setToolTipText(Strings.SearchApiDialog.SEARCH_HINT);
            URL url = PluginConfig.class.getResource("/com/smartbear/apisio/icons/toolbar_find.png");
            this.searchButton = new JButton(new ImageIcon(url));

            this.setLayout(new BorderLayout(5, 0));
            this.add(this.searchField, BorderLayout.CENTER);
            this.add(this.searchButton, BorderLayout.EAST);

            KeyListener keyListener = new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        e.consume();
                        searchApi();
                    }
                }
            };
            this.searchField.addKeyListener(keyListener);
            this.searchButton.addKeyListener(keyListener);
            this.searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    searchApi();
                }
            });
        }

        public String getText() {
            return this.searchField.getText().trim();
        }
    }

    private class GridUI
    {
        private class CustomTable extends JTable {
            private final TableCellRenderer renderer;

            public CustomTable(TableModel model) {
                super(model);
                this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        int[] rows = CustomTable.this.getSelectedRows();
                        dialog.setValue(AddApiForm.DESCRIPTION, rows.length == 1 ? apis.get(rows[0]).description : "");
                    }
                });
                renderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        c.setForeground(apis.get(row).formats.size() > 0 ? Color.black : Color.gray);
                        return this;
                    }
                };

                final JComboBox comboBox = new JComboBox(new DefaultComboBoxModel());

                TableColumn col = this.getColumnModel().getColumn(1);
                col.setCellEditor(new FormatCellEditor(comboBox, apis));
            }

            @Override
            public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
                return renderer;
            }
        }

        private JTable apiTable = new CustomTable(new ApiTableModel(apis));

        public JComponent getComponent() {
            return  this.apiTable;
        }

        public void updateUI() {
            this.apiTable.updateUI();
            if (apis.size() > 0) {
                this.apiTable.setRowSelectionInterval(0, 0);
            }
        }

        public List<Api> getSelected() {
            List<Api> result = new ArrayList<>();
            int[] selected = this.apiTable.getSelectedRows();
            for (int index: selected) {
                if (apis.get(index).formats.size() > 0) {
                    result.add(apis.get(index));
                }
            }
            return result;
        }
    }
}
