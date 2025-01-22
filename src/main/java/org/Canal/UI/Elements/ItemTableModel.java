package org.Canal.UI.Elements;

import org.Canal.Models.SupplyChainUnits.Item;
import org.Canal.Models.SupplyChainUnits.Location;
import org.Canal.Utils.Engine;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemTableModel extends AbstractTableModel {

    private final List<Item> items;
    private final String[] columnNames = {
            "Item Name",
            "Item",
            "Vendor",
            "Vendor Name",
            "Quantity",
            "Price",
            "Total",
            "Calc. Weight",
            "wUOM"
    };
    private final Class<?>[] columnTypes = {
            String.class, Item.class, String.class, String.class, Integer.class, Double.class, Double.class, Double.class, String.class
    };
    private final List<Object[]> data;

    public ItemTableModel(List<Item> items) {
        this.items = items;
        data = new ArrayList<>();
        for (Item item : items) {
            Location v = Engine.getLocation(item.getVendor(), "VEND");
            data.add(new Object[]{item.getName(), item, item.getVendor(), v.getName(), 1, item.getPrice(), item.getPrice(), item.getWeight(), item.getWeightUOM()});
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            Item selectedItem = (Item) value;
            Location vendor = Engine.getLocation(selectedItem.getVendor(), "VEND");
            data.get(rowIndex)[columnIndex] = selectedItem;
            data.get(rowIndex)[0] = selectedItem.getName();
            data.get(rowIndex)[2] = vendor.getId();
            data.get(rowIndex)[3] = vendor.getName();
            data.get(rowIndex)[5] = selectedItem.getPrice();
            double price = Double.parseDouble(data.get(rowIndex)[5].toString());
            double quantity = Double.parseDouble(data.get(rowIndex)[4].toString());
            data.get(rowIndex)[6] = price * quantity;
        } else if (columnIndex == 4) {
            double quantity = Double.parseDouble(value.toString());
            data.get(rowIndex)[4] = quantity;
            double price = Double.parseDouble(data.get(rowIndex)[5].toString());
            data.get(rowIndex)[6] = price * quantity;
        } else {
            data.get(rowIndex)[columnIndex] = value;
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    public void addRow(Item item) {
        Location v = Engine.getLocation(item.getVendor(), "VEND");
        data.add(new Object[]{item.getName(), item, item.getVendor(), v.getName(), 1, item.getPrice(), item.getPrice(), item.getWeight(), item.getWeightUOM()});
        fireTableRowsInserted(data.size() - 1, data.size() - 1);
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            data.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public String getTotalPrice() {
        double total = 0.0;
        for (Object[] row : data) {
            total += Double.parseDouble(row[6].toString());
        }
        return new DecimalFormat("#.00").format(total);
    }
}