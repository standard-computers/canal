package org.Canal.UI.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePicker extends JPanel {

    private JTextField dateField;
    private JButton calendarButton;
    private JPopupMenu calendarPopup;
    private JSpinner monthSpinner;
    private JSpinner yearSpinner;
    private JPanel daysPanel;
    private JLabel monthYearLabel;
    private Date selectedDate;

    private final String[] months = new SimpleDateFormat().getDateFormatSymbols().getMonths();

    public DatePicker() {
        dateField = new JTextField(10);
        dateField.setEditable(false); // Disable manual text input
        calendarButton = new JButton("▼");
        calendarPopup = new JPopupMenu();
        initializeCalendarPopup();
        setLayout(new BorderLayout());
        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);
        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendarPopup.show(dateField, 0, dateField.getHeight());
            }
        });
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
        selectedDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        dateField.setText(dateFormat.format(selectedDate));
        calendarPopup.setVisible(false);
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        monthSpinner.setValue(months[calendar.get(Calendar.MONTH)]);
        yearSpinner.setValue(calendar.get(Calendar.YEAR));
        selectDate(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Date Picker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        DatePicker datePicker = new DatePicker();
        frame.add(datePicker);
        frame.setVisible(true);
    }
}