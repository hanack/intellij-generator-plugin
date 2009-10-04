package de.jigp.plugin.configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class GeneralConfigurationPanel extends JPanel implements ItemListener {

    private JCheckBox supressSuffix;
    private boolean isSuffixSuppressed;
    private JCheckBox override;
    private boolean isOverrideAnnotation;
    private GridBagConstraints constraints;

    public GeneralConfigurationPanel() {
        initPanel();
    }

    private void initPanel() {
        setBorder(BorderFactory.createTitledBorder("General configuration"));
        supressSuffix = new JCheckBox("Supress suffix dialog");
        supressSuffix.addItemListener(this);
        override = new JCheckBox("Generated getter use @Override annotation");
        override.addItemListener(this);

        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;

        constraints.gridx = 0;
        constraints.gridy = 0;

        add(supressSuffix, constraints);

        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(override, constraints);
    }

    public void setConfiguration(Configuration configuration) {
        supressSuffix.setSelected(configuration.isSuffixQuestionSupressed);
        override.setSelected(configuration.isGetterUsingOverride);
    }


    public void fillConfiguration(Configuration configuration) {
        configuration.isGetterUsingOverride = isOverrideAnnotation;
        configuration.isSuffixQuestionSupressed = isSuffixSuppressed;
    }


    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getItemSelectable().equals(supressSuffix)) {
            isSuffixSuppressed = itemEvent.getStateChange() == ItemEvent.SELECTED;
        } else if (itemEvent.getItemSelectable().equals(override)) {
            isOverrideAnnotation = itemEvent.getStateChange() == ItemEvent.SELECTED;
        }

    }

}
