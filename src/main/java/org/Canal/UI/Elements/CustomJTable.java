package org.Canal.UI.Elements;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CustomJTable extends JTable {

    private boolean[] editableColumns;
    private List<Object[]> rowData;
    private String[] columnNames;

    public CustomJTable(Object[][] data, String[] columnNames) {
        super();
        editableColumns = new boolean[columnNames.length];
        this.columnNames = columnNames;
        Arrays.fill(editableColumns, true);
        rowData = new ArrayList<>(Arrays.asList(data));
        setModel(new CustomTableModel(data, columnNames));
        TableColumn tc = getColumnModel().getColumn(0);
        tc.setCellEditor(new DefaultCellEditor(new JCheckBox()));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                if (row != -1) {
                    Object[] rowData = getRowData(row);
                }
            }
        });
    }
    public void setColumnEditable(int column, boolean isEditable) {
        if (column >= 0 && column < editableColumns.length) {
            editableColumns[column] = isEditable;
            repaint();
        }
    }
    public void exportToXLSX(String filePath) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Table Data");
            DefaultTableModel model = (DefaultTableModel) getModel();
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(model.getColumnName(i));
            }
            for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                for (int colIndex = 0; colIndex < model.getColumnCount(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    Object cellValue = model.getValueAt(rowIndex, colIndex);
                    cell.setCellValue(cellValue != null ? cellValue.toString() : "");
                }
            }
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                System.out.println("Exported to " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object[] getRowData(int rowIndex) {
        return rowData.get(rowIndex);
    }

    public void setRowData(Object[][] newRowData) {
        rowData = new ArrayList<>(Arrays.asList(newRowData));
        ((DefaultTableModel) getModel()).setDataVector(newRowData, getColumnIdentifiers().toArray());
        repaint();
    }

    public List<String> getColumnIdentifiers() {
        return List.of(columnNames);
    }

    private class CustomTableModel extends DefaultTableModel {
        public CustomTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 0) return true;
            return editableColumns[column];
        }
    }
}