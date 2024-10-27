package org.Canal.UI.Elements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FormFrame extends JInternalFrame {

    private JPanel content;
    private JLabel status;
    private GridBagConstraints gbc;
    private Button commit;

    public FormFrame(){
        setLayout(new BorderLayout());
        JPanel statusBar = new JPanel(new BorderLayout());
        JLabel trLabel = new JLabel("OK [200]");
        status = new JLabel("/");
        status.setFont(new Font(UIManager.getFont("Label.font").getFamily(), Font.BOLD, 12));
        status.setForeground(new Color(40, 149, 4));
        trLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        status.setBorder(new EmptyBorder(5, 5, 5, 5));
        content = new JPanel(new GridBagLayout());
        statusBar.add(trLabel, BorderLayout.WEST);
        statusBar.add(status, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);
        prepContentPane();
        JPanel mc = new JPanel(new BorderLayout());
        commit = new Button("Commit");
        commit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onCommit(e);
            }
        });
        mc.add(content, BorderLayout.CENTER);
        mc.add(commit, BorderLayout.SOUTH);
        add(mc, BorderLayout.CENTER);
        setResizable(false);
        registerEscapeKey();
    }

    protected void onCommit(MouseEvent e) {
        System.out.println("Trickle");
    }

    private void prepContentPane(){
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 0.3;
        gbc.gridy = 0;
    }

    private void registerEscapeKey() {
        JRootPane rootPane = this.getRootPane();
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "closeWindow");
        rootPane.getActionMap().put("closeWindow", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });
    }

    public void addInput(Component label, Component value) {
        gbc.gridx = 0;
        content.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        content.add(value, gbc);
        gbc.gridy++;
    }

    public void setTransactionCode(String currentTCode) {
        this.status.setText(currentTCode);
        repaint();
        revalidate();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public Component add(Component c){
        content.add(c);
        repaint();
        return c;
    }
}