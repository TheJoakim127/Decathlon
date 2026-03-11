package com.example.decathlon.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import com.example.decathlon.deca.*;
import com.example.decathlon.heptathlon.*;
import com.example.decathlon.excel.ExcelPrinter;

public class MainGUI {

    private JTextField nameField;
    private JTextField resultField;
    private JComboBox<String> disciplineBox;

    private JTable table;
    private DefaultTableModel model;

    private Map<String, Map<String,Integer>> results = new HashMap<>();

    private String[] disciplines = {

            "Dec 100m (s)",
            "Dec 400m (s)",
            "Dec 1500m (min)",
            "Dec 110m Hurdles (s)",
            "Dec Long Jump (cm)",
            "Dec High Jump (cm)",
            "Dec Pole Vault (cm)",
            "Dec Discus Throw (m)",
            "Dec Javelin Throw (m)",
            "Dec Shot Put (m)",

            "Hep 200m (s)",
            "Hep 800m (s)",
            "Hep 100m Hurdles (s)",
            "Hep High Jump (cm)",
            "Hep Long Jump (cm)",
            "Hep Shot Put (m)",
            "Hep Javelin Throw (m)"
    };

    public static void main(String[] args) {
        new MainGUI().createAndShowGUI();
    }

    private void createAndShowGUI() {

        JFrame frame = new JFrame("Track and Field Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        nameField = new JTextField(20);
        panel.add(new JLabel("Enter Competitor's Name:"));
        panel.add(nameField);

        disciplineBox = new JComboBox<>(disciplines);
        panel.add(new JLabel("Select Discipline:"));
        panel.add(disciplineBox);

        resultField = new JTextField(10);
        panel.add(new JLabel("Enter Result:"));
        panel.add(resultField);

        JButton calculateButton = new JButton("Calculate Score");
        calculateButton.addActionListener(new CalculateButtonListener());
        panel.add(calculateButton);

        JButton exportButton = new JButton("Export to Excel");
        exportButton.addActionListener(new ExportButtonListener());
        panel.add(exportButton);

        String[] columns = new String[disciplines.length + 2];
        columns[0] = "Name";

        for(int i=0;i<disciplines.length;i++){
            columns[i+1] = disciplines[i];
        }

        columns[columns.length-1] = "Total";

        model = new DefaultTableModel(columns,0);
        table = new JTable(model);

        JScrollPane tableScroll = new JScrollPane(table);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(tableScroll, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private class CalculateButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            String name = nameField.getText();
            String discipline = (String) disciplineBox.getSelectedItem();

            try{

                double result = Double.parseDouble(resultField.getText());

                int score = 0;

                switch(discipline){

                    case "Dec 100m (s)" -> score = new Deca100M().calculateResult(result);
                    case "Dec 400m (s)" -> score = new Deca400M().calculateResult(result);
                    case "Dec 1500m (min)" -> score = new Deca1500M().calculateResult(result);
                    case "Dec 110m Hurdles (s)" -> score = new Deca110MHurdles().calculateResult(result);
                    case "Dec Long Jump (cm)" -> score = new DecaLongJump().calculateResult(result);
                    case "Dec High Jump (cm)" -> score = new DecaHighJump().calculateResult(result);
                    case "Dec Pole Vault (cm)" -> score = new DecaPoleVault().calculateResult(result);
                    case "Dec Discus Throw (m)" -> score = new DecaDiscusThrow().calculateResult(result);
                    case "Dec Javelin Throw (m)" -> score = new DecaJavelinThrow().calculateResult(result);
                    case "Dec Shot Put (m)" -> score = new DecaShotPut().calculateResult(result);

                    case "Hep 200m (s)" -> score = new Hep200M().calculateResult(result);
                    case "Hep 800m (s)" -> score = new Hep800M().calculateResult(result);
                    case "Hep 100m Hurdles (s)" -> score = new Hep100MHurdles().calculateResult(result);
                    case "Hep High Jump (cm)" -> score = new HeptHightJump().calculateResult(result);
                    case "Hep Long Jump (cm)" -> score = new HeptLongJump().calculateResult(result);
                    case "Hep Shot Put (m)" -> score = new HeptShotPut().calculateResult(result);
                    case "Hep Javelin Throw (m)" -> score = new HeptJavelinThrow().calculateResult(result);
                }

                results.putIfAbsent(name,new HashMap<>());
                results.get(name).put(discipline,score);

                updateTable();

            }catch(NumberFormatException ex){

                JOptionPane.showMessageDialog(null,"Invalid number");

            }

        }

    }

    private void updateTable(){

        model.setRowCount(0);

        for(String name : results.keySet()){

            Object[] row = new Object[disciplines.length + 2];

            row[0] = name;

            int total = 0;

            for(int i=0;i<disciplines.length;i++){

                Integer value = results.get(name).get(disciplines[i]);

                if(value != null){

                    row[i+1] = value;
                    total += value;

                }else{

                    row[i+1] = "";

                }

            }

            row[row.length-1] = total;

            model.addRow(row);
        }
    }

    private class ExportButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e){

            try{

                ExcelPrinter printer = new ExcelPrinter("gui");

                Object[][] data = new Object[model.getRowCount()+1][model.getColumnCount()];

                for(int c=0;c<model.getColumnCount();c++){

                    data[0][c] = model.getColumnName(c);

                }

                for(int r=0;r<model.getRowCount();r++){

                    for(int c=0;c<model.getColumnCount();c++){

                        data[r+1][c] = model.getValueAt(r,c);

                    }

                }

                printer.add(data,"Results");

                printer.write();

                JOptionPane.showMessageDialog(null,"Excel exported");

            }catch(Exception ex){

                JOptionPane.showMessageDialog(null,"Excel export failed");

            }

        }

    }

}