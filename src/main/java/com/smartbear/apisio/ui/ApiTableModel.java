package com.smartbear.apisio.ui;

import com.smartbear.apisio.entities.importx.Format;
import com.smartbear.apisio.entities.importx.Api;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

public class ApiTableModel implements TableModel {
    private List<Api> apis;
    public ApiTableModel(List<Api> apis) {
        this.apis = apis;
    }

    @Override
    public int getRowCount() {
        return apis.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Name";
        } else if (columnIndex == 1) {
            return "Format";
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else if (columnIndex == 1) {
            return Format.class;
        } else {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1 && apis.get(rowIndex).formats.size() > 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return apis.get(rowIndex).name;
        } else if (columnIndex == 1) {
            return apis.get(rowIndex).getFormat();
        } else {
            return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1 && aValue instanceof Format) {
            apis.get(rowIndex).setFormat((Format)aValue);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
