package org.Canal.UI.Elements;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;
import ca.odell.glazedlists.swing.EventTableModel;
import org.Canal.UI.ColorUtil;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomTable extends JTable {

    private static final Color HIGHLIGHT_COLOR = new Color(207, 159, 0);
    private EventList<Object[]> rows;
    private String[] headers;

    public CustomTable(String[] headers, ArrayList<Object[]> data) {

        // Original setup code
        this.headers = new String[headers.length + 1];
        this.headers[0] = "Select";
        System.arraycopy(headers, 0, this.headers, 1, headers.length);

        this.rows = new BasicEventList<>();
        for (Object[] row : data) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = Boolean.FALSE;
            System.arraycopy(row, 0, newRow, 1, row.length);
            this.rows.add(newRow);
        }

        setModel(createTableModel());
        setCellSelectionEnabled(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set custom checkbox renderer for the first column
        TableColumn selectColumn = getColumnModel().getColumn(0);
        selectColumn.setCellRenderer(new CheckboxRenderer());
        selectColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));

        // Apply custom row renderer to all other columns
        for (int i = 1; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(new CustomRowRenderer());
        }

        autoResizeColumns();
    }

    private EventTableModel<Object[]> createTableModel() {
        return new EventTableModel<>(rows, new TableFormat<Object[]>() {
            @Override
            public int getColumnCount() {
                return headers.length;
            }

            @Override
            public String getColumnName(int column) {
                return headers[column];
            }

            @Override
            public Object getColumnValue(Object[] baseObject, int column) {
                return baseObject[column];
            }
        }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the "Select" column editable
                return column == 0 || column > 0; // Allow all columns, including the first one
            }

            @Override
            public void setValueAt(Object value, int row, int column) {
                if (row >= 0 && row < rows.size() && column >= 0 && column < headers.length) {
                    Object[] rowData = rows.get(row);
                    rowData[column] = value;
                    fireTableCellUpdated(row, column); // Notify the table of the change
                } else {
                    throw new UnsupportedOperationException("Invalid row or column index");
                }
            }
        };
    }

    public void setRowData(ArrayList<Object[]> data) {
        rows.clear();
        for (Object[] row : data) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = Boolean.FALSE; // Default checkbox state is unchecked
            System.arraycopy(row, 0, newRow, 1, row.length); // Copy the rest of the row
            rows.add(newRow);
        }
        autoResizeColumns();
    }

    public void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {

                // Write headers
                for (int col = 0; col < headers.length; col++) {
                    writer.write(headers[col]);
                    if (col < headers.length - 1) {
                        writer.write(",");
                    }
                }
                writer.newLine();

                // Write data rows
                for (Object[] row : rows) {
                    for (int col = 0; col < row.length; col++) {
                        writer.write(row[col].toString());
                        if (col < row.length - 1) {
                            writer.write(",");
                        }
                    }
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "Exported successfully to " + fileToSave.getAbsolutePath(), "Export", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting to CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);

        int leadRow = getSelectionModel().getLeadSelectionIndex();

        if (c instanceof JComponent) {
            if (row == leadRow && getSelectedColumn() != -1) {
                // Highlight row outline + padding
                ((JComponent) c).setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(1, 1, 1, 1, HIGHLIGHT_COLOR), // row outline
                                BorderFactory.createEmptyBorder(5, 12, 5, 5)                  // padding
                        )
                );
            } else {
                // Normal padding
                ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 5));
            }
        }

        return c;
    }

    private static class CheckboxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckboxRenderer() {
            setHorizontalAlignment(CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected(value != null && (Boolean) value);
            if (isSelected) {
                setBackground(HIGHLIGHT_COLOR);
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    private static class CustomRowRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Font defaultFont = UIManager.getFont("defaultFont");
            c.setFont(defaultFont.deriveFont(Engine.getConfiguration().getFontSize() + 2));
            if (!isSelected) {
                Color background = (row % 2 == 0) ? UIManager.getColor("Table.background") : UIManager.getColor("Table.alternateRowColor");
                if (background == null) {
                    background = ColorUtil.adjustBrightness(UIManager.getColor("Panel.background"), 0.90f);
                }
                c.setBackground(background);
                if (value instanceof Number) {
                    double numericValue = ((Number) value).doubleValue();
                    if (numericValue > 0) {
                        c.setForeground(new Color(9, 143, 14));
                    } else if (numericValue < 0) {
                        c.setForeground(new Color(243, 55, 55)); // Light red
                    }
                }
                if (value instanceof Boolean) {
                    if ((boolean) value) {
                        c.setForeground(new Color(9, 143, 14));
                    } else {
                        c.setForeground(new Color(243, 55, 55)); // Light red
                    }
                }
            } else {
                c.setBackground(table.getSelectionBackground());
            }
            if (c instanceof JComponent) {
                ((JComponent) c).setBorder(
                        BorderFactory.createCompoundBorder(
                                ((JComponent) c).getBorder(),
                                BorderFactory.createEmptyBorder(5, 10, 5, 10) // top, left, bottom, right padding
                        )
                );
            }
            return c;
        }
    }

    public void autoResizeColumns() {
        for (int col = 0; col < getColumnCount(); col++) {
            TableColumn column = getColumnModel().getColumn(col);
            int maxWidth = 0;
            Component headerRenderer = getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(this, column.getHeaderValue(), false, false, -1, col);
            maxWidth = headerRenderer.getPreferredSize().width;
            for (int row = 0; row < getRowCount(); row++) {
                Component cellRenderer = getCellRenderer(row, col)
                        .getTableCellRendererComponent(this, getValueAt(row, col), false, false, row, col);
                maxWidth = Math.max(maxWidth, cellRenderer.getPreferredSize().width);
            }
            column.setPreferredWidth(maxWidth + 10);
        }
    }

    private static class TextFieldRenderer extends JTextField implements TableCellRenderer {
        public TextFieldRenderer() {
            setBorder(BorderFactory.createEmptyBorder()); // No border for cleaner look
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : ""); // Set the cell's value as text
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    private static class TextFieldEditor extends DefaultCellEditor {
        private final JTextField textField;

        public TextFieldEditor() {
            super(new JTextField());
            textField = (JTextField) getComponent();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            textField.setText(value != null ? value.toString() : ""); // Set initial text
            return textField;
        }

        @Override
        public Object getCellEditorValue() {
            return textField.getText(); // Return the updated value
        }
    }

    public void setEditable(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) {
            throw new IllegalArgumentException("Invalid column index: " + columnIndex);
        }
        TableColumn column = getColumnModel().getColumn(columnIndex);
        column.setCellRenderer(new TextFieldRenderer());
        column.setCellEditor(new TextFieldEditor());
    }
}