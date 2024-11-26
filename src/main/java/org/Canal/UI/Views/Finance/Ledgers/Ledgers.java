package org.Canal.UI.Views.Finance.Ledgers;

import org.Canal.Models.BusinessUnits.Ledger;
import org.Canal.UI.Elements.Elements;
import org.Canal.Utils.DesktopState;
import org.Canal.Utils.Engine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * /LGS
 */
public class Ledgers extends JInternalFrame {

    private DefaultListModel<Ledger> listModel;

    public Ledgers(DesktopState desktop) {
        super("Ledgers", false, true, false, true);
        setFrameIcon(new ImageIcon(Ledgers.class.getResource("/icons/ledgers.png")));
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
                            desktop.put(Engine.router("/LGS/" + l.getId(), desktop));
                        } else {
                            JOptionPane.showMessageDialog(null, "Ledger Not Found");
                        }
                    }
                }
            }
        });
        JTextField direct = Elements.input();
        direct.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = direct.getText().trim();
                    if (!inputText.isEmpty()) {
                        desktop.put(Engine.router("/LGS/" + inputText, desktop));
                    }
                }
            }
        });
        add(direct, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        for(Ledger l : Engine.getLedgers()){
            listModel.addElement(l);
        }
    }

    static class LedgerRenderer extends JPanel implements ListCellRenderer<Ledger> {

        private JLabel ledgerName;
        private JLabel ledgerId;
        private JLabel ledgerTransactionCount;

        public LedgerRenderer() {
            setLayout(new GridLayout(3, 1));
            ledgerName = new JLabel();
            ledgerId = new JLabel();
            ledgerTransactionCount = new JLabel();
            ledgerName.setFont(new Font("Arial", Font.BOLD, 16));
            ledgerId.setFont(new Font("Arial", Font.PLAIN, 12));
            ledgerTransactionCount.setFont(new Font("Arial", Font.PLAIN, 12));
            add(ledgerName);
            add(ledgerId);
            add(ledgerTransactionCount);
            setBorder(new EmptyBorder(5, 5, 5, 5));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Ledger> list, Ledger ledger, int index, boolean isSelected, boolean cellHasFocus) {
            ledgerName.setText(ledger.getName());
            ledgerId.setText(ledger.getId());
            ledgerTransactionCount.setText(ledger.getTransactions().size() + " Transactions");
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