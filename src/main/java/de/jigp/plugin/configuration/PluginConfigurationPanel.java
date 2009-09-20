package de.jigp.plugin.configuration;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import de.jigp.plugin.GeneratorPluginComponent;
import de.jigp.plugin.GeneratorPluginComponent.Configuration;

import javax.swing.*;
import java.awt.*;

public class PluginConfigurationPanel extends JComponent {
    private JPanel jPanel;
    private JTextField dtoAnnotationName;
    private JTextField wrapperAnnotationName;
    private JTextField builderAnnotationName;
    private JTextField builderAssertionName;
    private JTextField dtoSuffix;
    private JTextField wrapperSuffix;
    private Checkbox supressSuffix;

    public PluginConfigurationPanel() {
        initComponents();
    }

    public Configuration getConfiguration() {
        Configuration configuration = new GeneratorPluginComponent.Configuration();
        configuration.builderAnnotation = builderAnnotationName.getText();
        configuration.dtoAnnotation = dtoAnnotationName.getText();
        configuration.dtoSuffix = dtoSuffix.getText();
        configuration.wrapperAnnotation = wrapperAnnotationName.getText();
        configuration.wrapperSuffix = wrapperSuffix.getText();
        configuration.builderAssertionName = builderAssertionName.getText();
        configuration.supressSufix = supressSuffix.getState();

        return configuration;
    }


    public void setConfiguration(Configuration configuration) {
        builderAnnotationName.setText(configuration.builderAnnotation);
        builderAssertionName.setText(configuration.builderAssertionName);
        wrapperAnnotationName.setText(configuration.wrapperAnnotation);
        wrapperSuffix.setText(configuration.wrapperSuffix);
        dtoAnnotationName.setText(configuration.dtoAnnotation);
        dtoSuffix.setText(configuration.dtoSuffix);
        supressSuffix.setState(configuration.supressSufix);

    }

    public JPanel getJPanel() {
        return jPanel;
    }

    private void initComponents() {
        int amountRows = 7;
        int amountColumns = 2;
        initializePanel(amountRows, amountColumns);

        int row = 0;
        addLabelInFirstColumn(row, "Full qualified annotation to identify base for DTO classes: ");
        dtoAnnotationName = createTextFieldInSecondColumn(row);

        row++;
        addLabelInFirstColumn(row, "Suffix for DTO generation:");
        dtoSuffix = createTextFieldInSecondColumn(row);

        row++;
        addLabelInFirstColumn(row, "Full qualified annotation to identify base for Wrapper classes:");
        wrapperAnnotationName = createTextFieldInSecondColumn(row);

        row++;
        addLabelInFirstColumn(row, "Suffix for Wrapper generation:");
        wrapperSuffix = createTextFieldInSecondColumn(row);


        row++;
        addLabelInFirstColumn(row, "Full qualified annotation to identify base for Builder classes:");
        builderAnnotationName = createTextFieldInSecondColumn(row);

        row++;
        addLabelInFirstColumn(row, "Builder: full qualified name of static assertion method:");
        builderAssertionName = createTextFieldInSecondColumn(row);

        row++;
        addLabelInSecondColumn(row, "Supress suffix dialog");
        supressSuffix = new Checkbox();
        addElementInFirstColumn(row, supressSuffix);
    }

    private void initializePanel(int amountRows, int amountColumns) {
        //TODO beautify current ugly layout
        jPanel = new JPanel();
        jPanel.setLayout(new GridLayoutManager(amountRows, amountColumns, new Insets(0, 0, 0, 0), -1, -1));
    }

    private void addLabelInSecondColumn(int labelRow, String labelText) {
        JLabel label = new JLabel();
        label.setText(labelText);
        jPanel.add(label, new GridConstraints(labelRow, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null));
    }

    private void addLabelInFirstColumn(int labelRow, String labelText) {
        JLabel label = new JLabel();
        label.setText(labelText);
        addElementInFirstColumn(labelRow, label);
    }

    private void addElementInFirstColumn(int labelRow, Component component) {
        jPanel.add(component, new GridConstraints(labelRow, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                GridConstraints.SIZEPOLICY_FIXED,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null));
    }

    private JTextField createTextFieldInSecondColumn(int row) {
        JTextField textField = new JTextField();
        jPanel.add(textField, new GridConstraints(row, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null));
        return textField;
    }


}
