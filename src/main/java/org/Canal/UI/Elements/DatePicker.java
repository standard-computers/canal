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
    private JTextField timeInputField;
    private Date selectedDate;

    private final String[] months = new SimpleDateFormat().getDateFormatSymbols().getMonths();
    private final String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DatePicker() {
        dateField = Elements.input(15);
        dateField.setText(dateFormat.format(new Date()));
        dateField.setEditable(true);

        calendarButton = new JButton("▼");
        calendarPopup = new JPopupMenu();
        initializeCalendarPopup();

        setLayout(new BorderLayout());
        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);

        dateField.addActionListener(_ -> updateFromDateField());
        calendarButton.addActionListener(_ -> calendarPopup.show(dateField, 0, dateField.getHeight()));
        setSelectedDate(new Date());
    }

    private void initializeCalendarPopup() {
        JPanel calendarPanel = new JPanel(new BorderLayout());
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Fixed size spinners
        Dimension spinnerSize = new Dimension(90, 25);

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
        monthSpinner.setPreferredSize(spinnerSize);

        yearSpinner = new JSpinner(new SpinnerNumberModel(Calendar.getInstance().get(Calendar.YEAR), 1900, 3000, 1));
        JSpinner.NumberEditor yearEditor = new JSpinner.NumberEditor(yearSpinner, "####");
        yearSpinner.setEditor(yearEditor);
        yearSpinner.setPreferredSize(spinnerSize);

        Calendar calendar = Calendar.getInstance();
        monthSpinner.setValue(months[calendar.get(Calendar.MONTH)]);
        monthSpinner.addChangeListener(_ -> updateDaysPanel());
        yearSpinner.addChangeListener(e -> updateDaysPanel());

        controlsPanel.add(monthSpinner);
        controlsPanel.add(yearSpinner);

        calendarPanel.add(controlsPanel, BorderLayout.NORTH);

        // ---- NEW: split weekday headers and day buttons ----
        JPanel calendarBody = new JPanel(new BorderLayout());

        // Weekday headers
        JPanel headerPanel = new JPanel(new GridLayout(1, 7));
        for (String wd : weekDays) {
            JLabel lbl = new JLabel(wd, SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            headerPanel.add(lbl);
        }
        calendarBody.add(headerPanel, BorderLayout.NORTH);

        // Days panel (only day numbers go here)
        daysPanel = new JPanel(new GridLayout(6, 7));
        calendarBody.add(daysPanel, BorderLayout.CENTER);

        calendarPanel.add(calendarBody, BorderLayout.CENTER);

        // Time input
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timePanel.add(new JLabel("Time (HH:mm:ss):"));
        timeInputField = Elements.input("00:00:00", 8);
        timePanel.add(timeInputField);
        timeInputField.addActionListener(e -> updateTimeFromInput());
        calendarPanel.add(timePanel, BorderLayout.SOUTH);

        calendarPopup.add(calendarPanel);
        updateDaysPanel();
    }

    private void updateDaysPanel() {
        daysPanel.removeAll();

        Calendar calendar = Calendar.getInstance();
        String selectedMonth = (String) monthSpinner.getValue();
        int monthIndex = getMonthIndex(selectedMonth);
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.YEAR, (int) yearSpinner.getValue());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Normalize so Sunday=0, Monday=1, ... Saturday=6
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
        if (firstDayOfWeek < 0) {
            firstDayOfWeek += 7;
        }

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Add blanks before first day
        for (int i = 0; i < firstDayOfWeek; i++) {
            daysPanel.add(new JLabel(""));
        }

        // Add buttons for days
        for (int day = 1; day <= daysInMonth; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setMargin(new Insets(1, 1, 1, 1));
            int finalDay = day;
            dayButton.addActionListener(_ -> selectDate(finalDay));

            // Optional: highlight today's date
            Calendar today = Calendar.getInstance();
            if (day == today.get(Calendar.DAY_OF_MONTH)
                    && monthIndex == today.get(Calendar.MONTH)
                    && (int) yearSpinner.getValue() == today.get(Calendar.YEAR)) {
                dayButton.setBackground(new Color(207, 159, 0));
            }

            daysPanel.add(dayButton);
        }

        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private int getMonthIndex(String monthName) {
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(monthName)) return i;
        }
        return 0;
    }

    private void selectDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, getMonthIndex((String) monthSpinner.getValue()));
        calendar.set(Calendar.YEAR, (int) yearSpinner.getValue());
        calendar.set(Calendar.DAY_OF_MONTH, day);

        try {
            Date time = new SimpleDateFormat("HH:mm:ss").parse(timeInputField.getText());
            Calendar t = Calendar.getInstance();
            t.setTime(time);
            calendar.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, t.get(Calendar.SECOND));
        } catch (ParseException e) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }

        selectedDate = calendar.getTime();
        updateDateField();
        calendarPopup.setVisible(false);
    }

    private void updateDateField() {
        if (selectedDate != null)
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
        if (selectedDate == null) selectedDate = new Date();
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

    /**
     * Sets date by applying payment terms (in days) to current date.
     */
    public void setDateByPaymentTerms(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        selectedDate = calendar.getTime();
        updateDateField();
    }

    public String getSelectedDateString() {
        return selectedDate != null ? dateFormat.format(selectedDate) : "";
    }
}
