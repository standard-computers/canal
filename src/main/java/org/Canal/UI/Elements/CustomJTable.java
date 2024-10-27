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

    public CustomJTable(Object[][] data, String[] columnNames) {
        super(new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) return true; // First column as selector
                return editableColumns != null && editableColumns[column];
            }
        });

        // Initialize editable column array
        editableColumns = new boolean[columnNames.length];
        Arrays.fill(editableColumns, true);

        // Store row data for easy access
        rowData = new ArrayList<>(Arrays.asList(data));

        // Add checkbox to the first column
        TableColumn tc = getColumnModel().getColumn(0);
        tc.setCellEditor(new DefaultCellEditor(new JCheckBox()));

        // Set row click listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                if (row != -1) {
                    Object[] rowData = getRowData(row);
                    System.out.println("Row data: " + Arrays.toString(rowData));
                }
            }
        });
    }

    // Toggle editing for a specific column
    public void setColumnEditable(int column, boolean isEditable) {
        if (column >= 0 && column < editableColumns.length) {
            editableColumns[column] = isEditable;
            repaint();
        }
    }

    // Export table data to XLSX file
    public void exportToXLSX(String filePath) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Table Data");
            DefaultTableModel model = (DefaultTableModel) getModel();

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < model.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(model.getColumnName(i));
            }

            // Populate data rows
            for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                for (int colIndex = 0; colIndex < model.getColumnCount(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    Object cellValue = model.getValueAt(rowIndex, colIndex);
                    cell.setCellValue(cellValue != null ? cellValue.toString() : "");
                }
            }

            // Write to file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                System.out.println("Exported to " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get data at a specific row
    public Object[] getRowData(int rowIndex) {
        return rowData.get(rowIndex);
    }
}