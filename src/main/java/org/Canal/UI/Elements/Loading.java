package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;

public class Loading {

    private static JDialog dialog;

    public static void show(JFrame parentFrame, String message) {
        dialog = new JDialog(parentFrame, "Loading", true);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(parentFrame);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(label, BorderLayout.NORTH);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.CENTER);
        dialog.add(panel);
        dialog.setBackground(new Color(0, 0, 0, 0));
        SwingUtilities.invokeLater(() -> dialog.setVisible(true));
    }

    public static void hide() {
        if (dialog != null) {
            dialog.dispose();
        }
    }
}