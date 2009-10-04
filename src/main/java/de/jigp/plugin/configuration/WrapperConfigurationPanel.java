package de.jigp.plugin.configuration;

import javax.swing.*;
import java.awt.*;

public class WrapperConfigurationPanel extends JPanel {
    private JTextField annotation;
    private JTextField suffix;
    private GridBagConstraints constraints;


    public WrapperConfigurationPanel() {
        initPanel();
    }

    private void initPanel() {
        addBorder();
        annotation = new JTextField();
        suffix = new JTextField();

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
        add(new JLabel("Annotation (full qualified)"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        add(annotation, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0;
        add(new JLabel("Suffix"), constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weighty = 1.0;
        add(suffix, constraints);

    }

    private void addBorder() {
        setBorder(BorderFactory.createTitledBorder("Wrapper configuration"));

    }

    public void fillConfiguration(Configuration configuration) {
        configuration.wrapperAnnotation = annotation.getText();
        configuration.wrapperSuffix = suffix.getText();
    }

    public void setConfiguration(Configuration configuration) {
        annotation.setText(configuration.wrapperAnnotation);
        suffix.setText(configuration.wrapperSuffix);
    }
}
