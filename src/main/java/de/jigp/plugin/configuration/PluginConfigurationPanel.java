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
        JLabel labelDto = new JLabel();
        JLabel labelWrapper = new JLabel();
        JLabel labelBuilder = new JLabel();
        JLabel labelAssertionMethod = new JLabel();
        dtoAnnotationName = new JTextField();
        wrapperAnnotationName = new JTextField();
        builderAnnotationName = new JTextField();
        builderAssertionName = new JTextField();

        {

            //TODO beautify current ugly layout
            jPanel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));

            labelDto.setText("Full qualified annotation to identify base for DTO classes: ");
            jPanel.add(labelDto, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));

            labelWrapper.setText("Full qualified annotation to identify base for Wrapper classes:");
            jPanel.add(labelWrapper, new GridConstraints(1, 0, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));

            labelBuilder.setText("Full qualified annotation to identify base for Builder classes:");
            jPanel.add(labelBuilder, new GridConstraints(2, 0, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));

            labelAssertionMethod.setText("Builder: full qualified name of static assertion method:");
            jPanel.add(labelAssertionMethod, new GridConstraints(3, 0, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
                    GridConstraints.SIZEPOLICY_FIXED,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));


            jPanel.add(dtoAnnotationName, new GridConstraints(0, 1, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            jPanel.add(wrapperAnnotationName, new GridConstraints(1, 1, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            jPanel.add(builderAnnotationName, new GridConstraints(2, 1, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            jPanel.add(builderAssertionName, new GridConstraints(3, 1, 1, 1,
                    GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                    GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                    GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));


        }
    }


}
