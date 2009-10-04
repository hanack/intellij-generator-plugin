package de.jigp.plugin.configuration;

import javax.swing.*;
import java.awt.*;

public class DtoConfigurationPanel extends JPanel {
    private JTextField dtoAnnotationName;
    private JTextField dtoSuffix;
    private TypeToTextPanel variableInitializers;
    private GridBagConstraints constraints;


    public DtoConfigurationPanel() {
        initPanel();
    }


    public void setConfiguration(Configuration configuration) {
        dtoAnnotationName.setText(configuration.dtoAnnotation);
        dtoSuffix.setText(configuration.dtoSuffix);
        initVariableInitializers(configuration.variableInitializers);
    }

    private void initVariableInitializers(TypeToTextMapping variableInitializers) {
        this.variableInitializers.init(variableInitializers);
    }

    private void initPanel() {
        addBorder();
        dtoAnnotationName = new JTextField();
        dtoSuffix = new JTextField();

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;


        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(new JLabel("Annotation (full qualified)"), constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        this.add(dtoAnnotationName, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        this.add(new JLabel("Suffix"), constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        this.add(dtoSuffix, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 1.0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        variableInitializers = new TypeToTextPanel(null, "enable variable initialization");
        this.add(variableInitializers, constraints);

    }

    private void addBorder() {
        this.setBorder(BorderFactory.createTitledBorder("DTO configuration"));
    }

    public void fillConfiguration(Configuration configuration) {
        configuration.dtoAnnotation = dtoAnnotationName.getText();
        configuration.dtoSuffix = dtoSuffix.getText();
        configuration.variableInitializers = variableInitializers.getMapping();

    }
}
