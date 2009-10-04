package de.jigp.plugin.configuration;

import javax.swing.*;
import java.awt.*;

public class BuilderConfigurationPanel extends JPanel {

    private JTextField annotation;

    private JTextField assertionExpression;
    private GridBagConstraints constraints;


    public BuilderConfigurationPanel() {
        initPanel();
    }

    private void initPanel() {
        addBorder();
        annotation = new JTextField();
        assertionExpression = new JTextField();

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
        this.add(annotation, constraints);


        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        this.add(new JLabel("Static assertion method (full qualified)"), constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(assertionExpression, constraints);

    }

    private void addBorder() {
        this.setBorder(BorderFactory.createTitledBorder("Builder configuration"));
    }

    public void fillConfiguration(Configuration configuration) {
        configuration.builderAnnotation = annotation.getText();
        configuration.builderAssertionExpression = assertionExpression.getText();
    }

    public void setConfiguration(Configuration configuration) {
        annotation.setText(configuration.builderAnnotation);
        assertionExpression.setText(configuration.builderAssertionExpression);
    }


}
