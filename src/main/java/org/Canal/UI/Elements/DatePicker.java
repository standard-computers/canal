package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;

public class DatePicker extends JPanel {

    private JTextField dateField;
    private JButton calendarButton;
    private JPopupMenu calendarPopup;
    private JSpinner monthSpinner;
    private JSpinner yearSpinner;
    private JPanel daysPanel;
    private JLabel monthYearLabel;
    private JTextField timeInputField; // Time input in the popup panel
    private Date selectedDate;

    private final String[] months = new SimpleDateFormat().getDateFormatSymbols().getMonths();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DatePicker() {
        dateField = Elements.input(15);
        dateField.setText(dateFormat.format(new Date())); // Default to the current date and time
        dateField.setEditable(true); // Allow manual editing of the date

        calendarButton = new JButton("▼");
        calendarPopup = new JPopupMenu();
        initializeCalendarPopup();

        setLayout(new BorderLayout());
        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);

        // Sync manual changes in dateField with the internal state
        dateField.addActionListener(e -> updateFromDateField());

        calendarButton.addActionListener(e -> calendarPopup.show(dateField, 0, dateField.getHeight()));
    }

    private void initializeCalendarPopup() {
        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new BorderLayout());
        JPanel controlsPanel = new JPanel(new FlowLayout());
        monthYearLabel = new JLabel();

        controlsPanel.add(monthYearLabel);

        monthSpinner = new JSpinner(new SpinnerListModel(months) {
            @Override
            public Object getNextValue() {
                return super.getNextValue() == null ? months[0] : super.getNextValue();
            }

            @Override
            public Object getPreviousValue() {
                return super.getPreviousValue() == null ? months[months.length - 2] : super.getPreviousValue();
            }
        });

        // Set default month to current month
        Calendar calendar = Calendar.getInstance();
        monthSpinner.setValue(months[calendar.get(Calendar.MONTH)]);

        monthSpinner.addChangeListener(e -> updateDaysPanel());
        yearSpinner = new JSpinner(new SpinnerNumberModel(Calendar.getInstance().get(Calendar.YEAR), 1900, 3000, 1));
        JSpinner.NumberEditor yearEditor = new JSpinner.NumberEditor(yearSpinner, "####");
        yearSpinner.setEditor(yearEditor);
        yearSpinner.addChangeListener(e -> updateDaysPanel());
        controlsPanel.add(monthSpinner);
        controlsPanel.add(yearSpinner);

        calendarPanel.add(controlsPanel, BorderLayout.NORTH);
        daysPanel = new JPanel();
        daysPanel.setLayout(new GridLayout(6, 7)); // 6 rows for weeks, 7 columns for days
        calendarPanel.add(daysPanel, BorderLayout.CENTER);

        // Add time input panel
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("Time (HH:mm:ss):"));
        timeInputField = Elements.input("00:00:00", 8); // Default to "00:00:00"
        timePanel.add(timeInputField);

        // Sync time input field with the selectedDate when changed
        timeInputField.addActionListener(e -> updateTimeFromInput());

        calendarPanel.add(timePanel, BorderLayout.SOUTH);

        calendarPopup.add(calendarPanel);
        updateDaysPanel(); // Initial update to show current month and year
    }

    private void updateDaysPanel() {
        daysPanel.removeAll(); // Clear previous days
        Calendar calendar = Calendar.getInstance();
        String selectedMonth = (String) monthSpinner.getValue();
        int monthIndex = getMonthIndex(selectedMonth);
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.YEAR, (int) yearSpinner.getValue());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Adjust for 0-based index
        monthYearLabel.setText(selectedMonth + " " + calendar.get(Calendar.YEAR));

        for (int i = 0; i < firstDayOfWeek; i++) {
            daysPanel.add(new JLabel(""));
        }

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            int finalDay = day;
            dayButton.addActionListener(e -> selectDate(finalDay));
            daysPanel.add(dayButton);
        }
        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private int getMonthIndex(String monthName) {
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(monthName)) {
                return i;
            }
        }
        return 0; // Default to January if not found
    }

    private void selectDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, getMonthIndex((String) monthSpinner.getValue()));
        calendar.set(Calendar.YEAR, (int) yearSpinner.getValue());
        calendar.set(Calendar.DAY_OF_MONTH, day);

        // Retain the time portion from the input
        try {
            Date time = new SimpleDateFormat("HH:mm:ss").parse(timeInputField.getText());
            calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
            calendar.set(Calendar.MINUTE, time.getMinutes());
            calendar.set(Calendar.SECOND, time.getSeconds());
        } catch (ParseException e) {
            // Default to 00:00:00 if the time input is invalid
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }

        selectedDate = calendar.getTime();
        updateDateField();
        calendarPopup.setVisible(false);
    }

    private void updateDateField() {
        dateField.setText(dateFormat.format(selectedDate));
    }

    private void updateFromDateField() {
        try {
            selectedDate = dateFormat.parse(dateField.getText());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            monthSpinner.setValue(months[calendar.get(Calendar.MONTH)]);
            yearSpinner.setValue(calendar.get(Calendar.YEAR));
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd HH:mm:ss.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTimeFromInput() {
        try {
            String[] parts = timeInputField.getText().split(":");
            if (parts.length == 3) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate);
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                calendar.set(Calendar.SECOND, Integer.parseInt(parts[2]));
                selectedDate = calendar.getTime();
                updateDateField();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid time format. Use HH:mm:ss.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date date) {
        selectedDate = date;
        updateDateField();
    }

    public String getSelectedDateString() {
        return dateFormat.format(selectedDate);
    }
}