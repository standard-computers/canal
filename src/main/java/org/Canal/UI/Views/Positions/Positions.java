package org.Canal.UI.Views.Positions;

import org.Canal.Models.HumanResources.Position;
import org.Canal.UI.Elements.CustomTable;
import org.Canal.UI.Elements.Elements;
import org.Canal.UI.Elements.IconButton;
import org.Canal.UI.Elements.LockeState;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import org.Canal.Utils.RefreshListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * /HR/POS
 */
public class Positions extends LockeState implements RefreshListener {

    private DesktopState desktop;
    private CustomTable table;

    public Positions(DesktopState desktop) {

        super("Positions", "/HR/POS", true, true, true, true);
        setFrameIcon(new ImageIcon(Positions.class.getResource("/icons/positions.png")));
        this.desktop = desktop;

        JPanel tb = toolbar();
        JPanel holder = new JPanel(new BorderLayout());
        table = table();
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 700));
        holder.add(Elements.header("Positions", SwingConstants.LEFT), BorderLayout.CENTER);
        holder.add(tb, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(holder, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        Position p = Engine.getPosition(value);
                        desktop.put(new ViewPosition(p));
                    }
                }
            }
        });
    }

    private JPanel toolbar() {
        JPanel tb = new JPanel();
        tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
        IconButton export = new IconButton("Export", "export", "Export as CSV");
        IconButton importPositions = new IconButton("Import", "export", "Import as CSV");
        IconButton createPosition = new IconButton("New", "create", "Create a Position", "/HR/POS/NEW");
        IconButton postPosition = new IconButton("Post", "positions", "Post position as available", "/HR/POS/POST");
        IconButton modifyPosition = new IconButton("Modify", "modify", "Modify a Position", "/HR/POS/MOD");
        IconButton archivePosition = new IconButton("Archive", "archive", "Archive a Position", "/HR/POS/ARCHV");
        IconButton removePosition = new IconButton("Remove", "delete", "Delete a Position", "/HR/POS/DEL");
        IconButton findPosition = new IconButton("Find", "find", "Find by Values", "/HR/POS/F");
        IconButton refresh = new IconButton("Refresh", "refresh", "Refresh Data");
        tb.add(export);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(importPositions);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(createPosition);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(postPosition);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(modifyPosition);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(archivePosition);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(removePosition);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(findPosition);
        tb.add(Box.createHorizontalStrut(5));
        tb.add(refresh);
        tb.setBorder(new EmptyBorder(0, 5, 0, 5));
        export.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.exportToCSV();
            }
        });
        importPositions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fc = new JFileChooser();

            }
        });
        refresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refresh();
            }
        });
        super.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
                    findPosition.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        return tb;
    }

    private CustomTable table() {
        String[] columns = new String[]{
                "ID",
                "Org",
                "Name",
                "Department",
                "Name",
                "Description",
                "Assigned",
                "Compensation",
                "Hourly?",
                "Annum",
                "Bonus",
                "Commission",
                "Status",
                "Created",
        };
        ArrayList<Object[]> data = new ArrayList<>();
        for (Position position : Engine.getPositions()) {
            data.add(new Object[]{
                    position.getId(),
                    position.getOrganization(),
                    position.getName(),
                    position.getDepartment(),
                    position.getName(),
                    position.getDescription(),
                    0,
                    position.getCompensation(),
                    position.isHourly(),
                    (position.isHourly() ? (40 * 52 * position.getCompensation()) : position.getCompensation()),
                    position.isBonus(),
                    position.isCommission(),
                    position.getStatus(),
                    position.getCreated(),
            });
        }
        return new CustomTable(columns, data);
    }

    @Override
    public void refresh() {

        CustomTable newTable = table();
        JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
        scrollPane.setViewportView(newTable);
        table = newTable;
        scrollPane.revalidate();
        scrollPane.repaint();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    if (row != -1) {
                        String value = String.valueOf(target.getValueAt(row, 1));
                        desktop.put(Engine.router("/HR/POS/" + value, desktop));
                    }
                }
            }
        });
    }
}