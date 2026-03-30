package com.example.decathlon.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import com.example.decathlon.excel.ExcelPrinter;

public class MainGUI {

    private JTextField nameField;
    private JTextField resultField;
    private JComboBox<String> disciplineBox;

    private JTable table;
    private DefaultTableModel model;

    private Map<String, Map<String,Integer>> results = new LinkedHashMap<>();

    private String[] disciplines = {
            "Dec 100m (s)",
            "Dec 400m (s)",
            "Dec 1500m (s)",
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
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;

        nameField = new JTextField(18);
        JButton addBtn = new JButton("Add Competitor");
        addBtn.addActionListener(new AddCompetitorButtonListener());

        disciplineBox = new JComboBox<>(disciplines);
        JButton calcBtn = new JButton("Calculate Score");
        calcBtn.addActionListener(new CalculateButtonListener());

        resultField = new JTextField(10);
        JButton exportBtn = new JButton("Export to Excel");
        exportBtn.addActionListener(new ExportButtonListener());

        gbc.gridx=0; gbc.gridy=0;
        formPanel.add(new JLabel("Enter Competitor's Name:"),gbc);
        gbc.gridx=1;
        formPanel.add(nameField,gbc);
        gbc.gridx=2;
        formPanel.add(addBtn,gbc);

        gbc.gridx=0; gbc.gridy=1;
        formPanel.add(new JLabel("Select Discipline:"),gbc);
        gbc.gridx=1;
        formPanel.add(disciplineBox,gbc);
        gbc.gridx=2;
        formPanel.add(calcBtn,gbc);

        gbc.gridx=0; gbc.gridy=2;
        formPanel.add(new JLabel("Enter Result:"),gbc);
        gbc.gridx=1;
        formPanel.add(resultField,gbc);
        gbc.gridx=2;
        formPanel.add(exportBtn,gbc);

        String[] columns = new String[disciplines.length + 2];
        columns[0] = "Name";
        for(int i=0;i<disciplines.length;i++){
            columns[i+1] = disciplines[i];
        }
        columns[columns.length-1] = "Total";

        model = new DefaultTableModel(columns,0){
            public boolean isCellEditable(int r,int c){ return false;}
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        setColumnWidths();

        JScrollPane scroll = new JScrollPane(table);

        mainPanel.add(formPanel,BorderLayout.NORTH);
        mainPanel.add(scroll,BorderLayout.CENTER);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private class AddCompetitorButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = normalize(nameField.getText());
            if(name.isEmpty()){
                JOptionPane.showMessageDialog(null,"Enter competitor name");
                return;
            }
            results.putIfAbsent(name,new HashMap<>());
            nameField.setText(name);
            updateTable();
        }
    }

    private class CalculateButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = normalize(nameField.getText());
            String discipline = (String) disciplineBox.getSelectedItem();

            if(!results.containsKey(name)){
                JOptionPane.showMessageDialog(null,"Add competitor first");
                return;
            }

            double value;
            try{
                value = Double.parseDouble(resultField.getText());
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null,"Invalid number");
                return;
            }

            Double min = getMin(discipline);
            Double max = getMax(discipline);

            if(min!=null && value<min){
                JOptionPane.showMessageDialog(null,"Value too low");
                return;
            }
            if(max!=null && value>max){
                JOptionPane.showMessageDialog(null,"Value too high");
                return;
            }

            int score = calculate(discipline,value);

            results.get(name).put(discipline,score);
            updateTable();
        }
    }

    private void updateTable(){
        model.setRowCount(0);

        for(String name:results.keySet()){
            Object[] row = new Object[disciplines.length+2];
            row[0]=name;

            int total=0;

            for(int i=0;i<disciplines.length;i++){
                Integer v = results.get(name).get(disciplines[i]);
                if(v!=null){
                    row[i+1]=v;
                    total+=v;
                }else row[i+1]="";
            }

            row[row.length-1]=total;
            model.addRow(row);
        }

        setColumnWidths();
    }

    private class ExportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e){
            try{
                ExcelPrinter printer = new ExcelPrinter("gui");

                Object[][] data = new Object[model.getRowCount()+1][model.getColumnCount()];

                for(int c=0;c<model.getColumnCount();c++){
                    data[0][c]=model.getColumnName(c);
                }

                for(int r=0;r<model.getRowCount();r++){
                    for(int c=0;c<model.getColumnCount();c++){
                        data[r+1][c]=model.getValueAt(r,c);
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

    private String normalize(String name){
        if(name==null)return "";
        String[] parts = name.trim().toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for(String p:parts){
            if(p.isEmpty())continue;
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    private void setColumnWidths(){
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        for(int i=1;i<table.getColumnCount()-1;i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(90);
        }
        table.getColumnModel().getColumn(table.getColumnCount()-1).setPreferredWidth(70);
    }

    private Double getMin(String d){
        return switch(d){
            case "Dec 100m (s)" -> 5.0;
            case "Dec 110m Hurdles (s)" -> 10.0;
            case "Dec 400m (s)" -> 20.0;
            case "Dec 1500m (s)" -> 150.0;
            case "Hep 100m Hurdles (s)" -> 10.0;
            case "Hep 200m (s)" -> 20.0;
            case "Hep 800m (s)" -> 70.0;
            default -> 0.0;
        };
    }

    private Double getMax(String d){
        return switch(d){
            case "Dec 100m (s)" -> 20.0;
            case "Dec 110m Hurdles (s)" -> 30.0;
            case "Dec 400m (s)" -> 100.0;
            case "Dec 1500m (s)" -> 400.0;
            case "Dec Discus Throw (m)" -> 85.0;
            case "Dec High Jump (cm)" -> 300.0;
            case "Dec Javelin Throw (m)" -> 110.0;
            case "Dec Long Jump (cm)" -> 1000.0;
            case "Dec Pole Vault (cm)" -> 1000.0;
            case "Dec Shot Put (m)" -> 30.0;
            case "Hep 100m Hurdles (s)" -> 30.0;
            case "Hep 200m (s)" -> 100.0;
            case "Hep 800m (s)" -> 250.0;
            case "Hep High Jump (cm)" -> 300.0;
            case "Hep Javelin Throw (m)" -> 110.0;
            case "Hep Long Jump (cm)" -> 1000.0;
            case "Hep Shot Put (m)" -> 30.0;
            default -> null;
        };
    }

    private int calculate(String d,double r){
        return switch(d){
            case "Dec 100m (s)" -> track(25.4347,18,1.81,r);
            case "Dec 400m (s)" -> track(1.53775,82,1.81,r);
            case "Dec 1500m (s)" -> track(0.03768,480,1.85,r);
            case "Dec 110m Hurdles (s)" -> track(5.74352,28.5,1.92,r);
            case "Dec Long Jump (cm)" -> field(0.14354,220,1.4,r);
            case "Dec High Jump (cm)" -> field(0.8465,75,1.42,r);
            case "Dec Pole Vault (cm)" -> field(0.2797,100,1.35,r);
            case "Dec Discus Throw (m)" -> field(12.91,4,1.1,r);
            case "Dec Javelin Throw (m)" -> field(10.14,7,1.08,r);
            case "Dec Shot Put (m)" -> field(51.39,1.5,1.05,r);
            case "Hep 200m (s)" -> track(4.99087,42.5,1.81,r);
            case "Hep 800m (s)" -> track(0.11193,254,1.88,r);
            case "Hep 100m Hurdles (s)" -> track(9.23076,26.7,1.835,r);
            case "Hep High Jump (cm)" -> field(1.84523,75,1.348,r);
            case "Hep Long Jump (cm)" -> field(0.188807,210,1.41,r);
            case "Hep Shot Put (m)" -> field(56.0211,1.5,1.05,r);
            case "Hep Javelin Throw (m)" -> field(15.9803,3.8,1.04,r);
            default -> 0;
        };
    }

    private int track(double a,double b,double c,double t){
        double x=b-t;
        if(x<=0)return 0;
        return (int)Math.floor(a*Math.pow(x,c));
    }

    private int field(double a,double b,double c,double d){
        double x=d-b;
        if(x<=0)return 0;
        return (int)Math.floor(a*Math.pow(x,c));
    }
}