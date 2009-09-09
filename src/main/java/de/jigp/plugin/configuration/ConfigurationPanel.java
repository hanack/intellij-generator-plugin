package de.jigp.plugin.configuration;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConfigurationPanel extends JPanel {
    private final Border etched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

    private final JCheckBox moveCursorToFirstMethod = new JCheckBox("Move cursor to first method");

    private JRadioButton[] initialValueForInsertionDialog;

    public ConfigurationPanel(Configuration config) {
        init();
        setConfiguration(config);
    }

    private void init() {
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("Settings", initSettingPanel());
        add(pane);
    }

    private JPanel initSettingPanel() {
        GridBagConstraints constraint = new GridBagConstraints();
        JPanel outer = new JPanel();
        outer.setLayout(new GridBagLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(etched, "Settings"));

        Container innerPanel = Box.createHorizontalBox();
        innerPanel.add(moveCursorToFirstMethod);
        innerPanel.add(Box.createHorizontalGlue());
        panel.add(innerPanel);

        constraint.gridwidth = GridBagConstraints.REMAINDER;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.gridx = 0;
        constraint.gridy = 0;
        outer.add(panel, constraint);


        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        InsertionPolicy[] options = new InsertionPolicy[]{InsertAtCursorPolicy.getInstance(), InsertLastPolicy.getInstance()};
        initialValueForInsertionDialog = new JRadioButton[options.length];
        ButtonGroup insertGroup = new ButtonGroup();
        for (int i = 0; i < options.length; i++) {
            initialValueForInsertionDialog[i] = new JRadioButton(new InsertionOptionAction(options[i]));
            insertGroup.add(initialValueForInsertionDialog[i]);
        }
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(etched, "Insert With Method Policy"));
        for (JRadioButton anInitialValueForInsertionDialog : initialValueForInsertionDialog) {
            panel.add(anInitialValueForInsertionDialog);
        }
        constraint.gridx = 0;
        constraint.gridy = 2;
        outer.add(panel, constraint);

        return outer;
    }

    private class InsertionOptionAction extends AbstractAction {
        public final InsertionPolicy option;

        InsertionOptionAction(InsertionPolicy option) {
            super(option.toString());
            this.option = option;
        }

        public void actionPerformed(ActionEvent e) {
        }
    }

    public final void setConfiguration(Configuration configuration) {
        moveCursorToFirstMethod.setSelected(configuration.isMoveCursorToFirstMethod());

        InsertionPolicy option = configuration.getInsertionPolicy();
        for (JRadioButton anInitialValueForNewMethodDialog : initialValueForInsertionDialog) {
            if (anInitialValueForNewMethodDialog.getText().equals(option.toString())) {
                anInitialValueForNewMethodDialog.setSelected(true);
            }
        }
    }

    public final Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setMoveCursorToFirstMethod(moveCursorToFirstMethod.isSelected());

        for (JRadioButton button : initialValueForInsertionDialog) {
            if (button.isSelected()) {
                configuration.setInsertionPolicy(((InsertionOptionAction) button.getAction()).option);
            }
        }
        return configuration;
    }
}