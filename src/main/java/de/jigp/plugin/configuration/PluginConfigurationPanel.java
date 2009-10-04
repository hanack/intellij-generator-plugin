package de.jigp.plugin.configuration;

import javax.swing.*;
import java.awt.*;

public class PluginConfigurationPanel extends JComponent {
    private JPanel jPanel;
    private DtoConfigurationPanel dtoConfigurationPanel;
    private WrapperConfigurationPanel wrapperConfigurationPanel;
    private BuilderConfigurationPanel builderConfigurationPanel;
    private GeneralConfigurationPanel generalConfigurationPanel;
    private GridBagConstraints constraints;


    public PluginConfigurationPanel() {
        initComponents();
    }

    public Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        dtoConfigurationPanel.fillConfiguration(configuration);
        wrapperConfigurationPanel.fillConfiguration(configuration);
        builderConfigurationPanel.fillConfiguration(configuration);
        generalConfigurationPanel.fillConfiguration(configuration);

        return configuration;
    }


    public void setConfiguration(Configuration configuration) {
        dtoConfigurationPanel.setConfiguration(configuration);
        wrapperConfigurationPanel.setConfiguration(configuration);
        builderConfigurationPanel.setConfiguration(configuration);
        generalConfigurationPanel.setConfiguration(configuration);
    }


    public JPanel getJPanel() {
        return jPanel;
    }

    private void initComponents() {
        initializePanel();

        int row = 0;
        generalConfigurationPanel = new GeneralConfigurationPanel();
        add(row, generalConfigurationPanel);

        row++;
        dtoConfigurationPanel = new DtoConfigurationPanel();
        add(row, dtoConfigurationPanel);

        row++;
        wrapperConfigurationPanel = new WrapperConfigurationPanel();
        add(row, wrapperConfigurationPanel);

        row++;
        builderConfigurationPanel = new BuilderConfigurationPanel();
        add(row, builderConfigurationPanel);

        row++;
        addLast(row);

    }

    private void add(int row, Component component) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0.0;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        jPanel.add(component, constraints);
    }

    private void addLast(int row) {
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.BOTH;
        jPanel.add(new Panel(), constraints);
    }

    private void initializePanel() {
        jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
    }

}
