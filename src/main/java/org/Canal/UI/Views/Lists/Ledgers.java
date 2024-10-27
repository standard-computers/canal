package org.Canal.UI.Views.Lists;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.UI.Elements.Button;
import org.Canal.UI.Views.New.NewLedger;
import org.Canal.Utils.Constants;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Ledgers extends JInternalFrame {

    private DefaultListModel<Ledger> listModel;

    public Ledgers(DesktopState desktop) {
        setTitle("Ledgers");
        listModel = new DefaultListModel<>();
        JList<Ledger> list = new JList<>(listModel);
        list.setCellRenderer(new LedgerRenderer());
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = list.locationToIndex(e.getPoint());
                    if (selectedIndex != -1) {
                        Ledger l = listModel.getElementAt(selectedIndex);
                        if (l != null) {
                            Engine.router("/LGS/" + l.getId(), desktop);
                        } else {
                            JOptionPane.showMessageDialog(null, "Ledger Not Found");
                        }
                    }
                }
            }
        });
        JTextField direct = new JTextField();
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    System.out.println(inputText);
                    if (!inputText.isEmpty()) {
                        Engine.router("/LGS/" + inputText, desktop);
                    }
                }
            }
        });
        Button nla = new Button("Add");
        nla.color(Constants.colors[7]);
        nla.addActionListener(e -> new NewLedger());
        JPanel options = new JPanel();
        options.add(nla);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(direct, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(options, BorderLayout.SOUTH);
        add(mainPanel);
        loadLocations();
        setResizable(false);
    }

    private void loadLocations(){
        listModel.removeAllElements();
        Engine.load();
        for (Ledger loc : Engine.getLedgers()) {
            listModel.addElement(loc);
        }
    }

    class LedgerRenderer extends JPanel implements ListCellRenderer<Ledger> {

        private JLabel ccName;
        private JLabel ccId;
        private JLabel line1;

        public LedgerRenderer() {
            setLayout(new GridLayout(4, 1));
            ccName = new JLabel();
            ccId = new JLabel();
            line1 = new JLabel();
            ccName.setFont(new Font("Arial", Font.BOLD, 16));
            ccId.setFont(new Font("Arial", Font.PLAIN, 12));
            line1.setFont(new Font("Arial", Font.PLAIN, 12));
            add(ccName);
            add(ccId);
            add(line1);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Ledger> list, Ledger ledger, int index, boolean isSelected, boolean cellHasFocus) {
            ccName.setText(ledger.getName());
            ccId.setText(ledger.getId());
            line1.setText(ledger.getTransactions().size() + " Transactions");
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
}