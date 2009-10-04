package de.jigp.plugin.configuration;

import de.jigp.plugin.GeneratorPluginContext;
import de.jigp.plugin.configuration.TypeToTextMapping.Entry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

public class TypeToTextPanel extends JPanel implements ActionListener, ItemListener {
    private Object[][] initializerRowData;
    private JTable tableVariableInitializers;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton removeButton;
    private JCheckBox isEnabled;
    private boolean isEnabledState = true;
    private GridBagConstraints constraints;

    public TypeToTextPanel(TypeToTextMapping mapping, String activationText) {
        setLayout(new GridBagLayout());
        createInitializerTableRowData(mapping);
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;

        isEnabled = new JCheckBox(activationText);
        isEnabled.setEnabled(mapping != null ? mapping.isMappingActive : isEnabledState);
        isEnabled.addItemListener(this);


        tableModel = createTableModel();
        tableVariableInitializers = new JTable(tableModel);

        JScrollPane tablePane = new JScrollPane(tableVariableInitializers);
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("new");
        addButton.addActionListener(this);
        removeButton = new JButton("remove selected");
        removeButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(isEnabled, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(tablePane, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        this.add(buttonPanel, constraints);

        this.updateUI();
    }


    private void createInitializerTableRowData(TypeToTextMapping mapping) {
        if (mapping == null) {
            //TODO wrong initialization
            mapping = GeneratorPluginContext.getConfiguration().variableInitializers;
        }

        initializerRowData = new Object[mapping.size()][2];
        int i = 0;
//         TODO sort it
        for (Entry entry : mapping.entries()) {
            initializerRowData[i++] = new Object[]{entry.type, entry.text};
        }
    }

    protected DefaultTableModel createTableModel() {
        String[] columNames = {"type (full qualified)", "initializer expression"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(initializerRowData, columNames) {
            public boolean isCellEditable(int row, int col) {
                return true;
            }

            @Override
            public String toString() {
                String str = "";

                for (int i = 0; i < this.getRowCount(); i++) {
                    str += i + " " + this.getValueAt(i, 0) + " : " + getValueAt(i, 1) + "\n";
                }
                return str;
            }
        };

        return defaultTableModel;
    }

    public TypeToTextMapping getMapping() {
        TypeToTextMapping mapping = new TypeToTextMapping();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            mapping.put((String) tableModel.getValueAt(i, 0), (String) tableModel.getValueAt(i, 1));
        }
        mapping.setMappingActive(isEnabledState);
        return mapping;
    }

    private void deleteRows(int[] rows) {
        Arrays.sort(rows);
        for (int i = rows.length - 1; i >= 0; i--) {
            tableModel.removeRow(rows[i]);
        }
    }

    private void addNewRow() {
        tableModel.insertRow(0, new String[]{"new type", "new initializer expression"});
    }


    public void init(TypeToTextMapping typeToTextMapping) {
        createInitializerTableRowData(typeToTextMapping);
        tableModel = createTableModel();
        tableVariableInitializers.setModel(tableModel);
        tableVariableInitializers.updateUI();
        isEnabledState = typeToTextMapping.isMappingActive;
        isEnabled.setSelected(isEnabledState);

    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(addButton)) {
            addNewRow();
        } else if (actionEvent.getSource().equals(removeButton)) {
            deleteRows(tableVariableInitializers.getSelectedRows());
        }


    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getItemSelectable().equals(isEnabled)) {
            isEnabledState = itemEvent.getStateChange() == ItemEvent.SELECTED;
        }
    }
}
