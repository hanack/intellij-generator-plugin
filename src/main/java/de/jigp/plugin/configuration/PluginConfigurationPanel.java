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

    public PluginConfigurationPanel() {
        initComponents();
    }

    public Configuration getConfiguration() {
        Configuration configuration = new GeneratorPluginComponent.Configuration();
        configuration.builderAnnotation = builderAnnotationName.getText();
        configuration.dtoAnnotation = dtoAnnotationName.getText();
        configuration.wrapperAnnotation = wrapperAnnotationName.getText();
        configuration.builderAssertionName = builderAssertionName.getText();

        return configuration;
    }


    public void setConfiguration(Configuration configuration) {
        builderAnnotationName.setText(configuration.builderAnnotation);
        wrapperAnnotationName.setText(configuration.wrapperAnnotation);
        dtoAnnotationName.setText(configuration.dtoAnnotation);

    }

    public JPanel getJPanel() {
        return jPanel;
    }

    private void initComponents() {
        jPanel = new JPanel();
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        JLabel label3 = new JLabel();
        JLabel label4 = new JLabel();
        JLabel label5 = new JLabel();
        dtoAnnotationName = new JTextField();
        wrapperAnnotationName = new JTextField();
        builderAnnotationName = new JTextField();
        builderAssertionName = new JTextField();

        {

            jPanel.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));

            label1.setText("Dto");
            jPanel.add(label1, new GridConstraints(1, 0, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));

            label2.setText("Wrapper");
            jPanel.add(label2, new GridConstraints(2, 0, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));

            label3.setText("Builder");
            jPanel.add(label3, new GridConstraints(3, 0, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            label5.setText("Builder: Full qualified assertion name");
            jPanel.add(label3, new GridConstraints(4, 0, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            jPanel.add(dtoAnnotationName, new GridConstraints(1, 1, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            jPanel.add(wrapperAnnotationName, new GridConstraints(2, 1, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            jPanel.add(builderAnnotationName, new GridConstraints(3, 1, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            jPanel.add(builderAssertionName, new GridConstraints(4, 1, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));

            label4.setText("Full qualified Annotation names");
            jPanel.add(label4, new GridConstraints(0, 1, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
        }
    }


}
