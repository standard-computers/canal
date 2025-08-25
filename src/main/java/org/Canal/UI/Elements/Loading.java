package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;

public class Loading extends JFrame {

    private final JLabel statusLabel;
    private final JProgressBar progressBar;
    private final JTextArea console;
    private final JScrollPane consoleScroll;

    private int maxSteps;
    private int value = 0;
    private boolean consoleVisible = false;

    /** Create a loading window as a JFrame. */
    public Loading(String title, int maxSteps) {
        super(title == null ? "Loading" : title);
        this.maxSteps = Math.max(0, maxSteps);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCCCCCC)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        statusLabel = new JLabel("Working…", SwingConstants.LEFT);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 14f));
        panel.add(statusLabel, BorderLayout.NORTH);

        progressBar = new JProgressBar();
        if (this.maxSteps > 0) {
            progressBar.setIndeterminate(false);
            progressBar.setMinimum(0);
            progressBar.setMaximum(this.maxSteps);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            progressBar.setString("0 / " + this.maxSteps);
        } else {
            progressBar.setIndeterminate(true);
        }
        panel.add(progressBar, BorderLayout.CENTER);

        console = new JTextArea(8, 48);
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        consoleScroll = new JScrollPane(console);
        consoleScroll.setVisible(false); // hidden by default
        panel.add(consoleScroll, BorderLayout.SOUTH);

        setContentPane(panel);
        pack();
        setSize(new Dimension(480, getHeight())); // pleasant default width
        setLocationRelativeTo(null); // center on screen
        setAlwaysOnTop(true);
    }

    /* ---------- Lifecycle ---------- */

    /** Open the frame (non-blocking). */
    public void open() {
        runOnEdt(() -> setVisible(true));
    }

    /** Close and dispose the frame. */
    public void close() {
        runOnEdt(this::dispose);
    }

    /* ---------- Public API ---------- */

    /** Update the status text above the progress bar. */
    public void setMessage(String message) {
        runOnEdt(() -> statusLabel.setText(message == null ? "" : message));
    }

    /** Append a line to the console and auto-scroll. */
    public void append(String msg) {
        runOnEdt(() -> {
            if (console.getText().length() > 0) console.append("\n");
            console.append(msg);
            console.setCaretPosition(console.getDocument().getLength());
        });
    }

    /** Show/hide the console area. */
    public void setConsoleVisible(boolean visible) {
        runOnEdt(() -> {
            consoleVisible = visible;
            consoleScroll.setVisible(visible);
            pack();
            setSize(new Dimension(Math.max(480, getWidth()), getHeight()));
            validate();
        });
    }

    /** Toggle the console area. */
    public void toggleConsole() {
        setConsoleVisible(!consoleVisible);
    }

    /** Increment progress by 1 (ignored if indeterminate). */
    public void increment() {
        setProgress(value + 1);
    }

    /** Set the current progress (0..maxSteps). */
    public void setProgress(int newValue) {
        runOnEdt(() -> {
            if (maxSteps <= 0) return; // indeterminate mode
            value = Math.max(0, Math.min(maxSteps, newValue));
            progressBar.setValue(value);
            progressBar.setString(value + " / " + maxSteps);
        });
    }

    /* ---------- Helpers ---------- */
    private static void runOnEdt(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) r.run();
        else SwingUtilities.invokeLater(r);
    }

    /* ---------- Convenience ---------- */

    /** Quick-start helper. */
    public static Loading start(String message, int maxSteps) {
        Loading f = new Loading("Loading", maxSteps);
        f.setMessage(message);
        f.open();
        return f;
    }
}
