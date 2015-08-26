package com.smartbear.apisio.ui;

import com.smartbear.apisio.entities.importx.Format;
import com.smartbear.apisio.entities.importx.Api;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import java.awt.Component;
import java.util.List;

public class FormatCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final JComboBox comboBox;
    private final List<Api> apis;

    public FormatCellEditor(JComboBox comboBox, List<Api> apis) {
        this.comboBox = comboBox;
        this.apis = apis;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comboBox.removeAllItems();
        for (Format format: this.apis.get(row).formats) {
            comboBox.addItem(format);
        }
        comboBox.setSelectedItem(value);
        return comboBox;
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }
}
