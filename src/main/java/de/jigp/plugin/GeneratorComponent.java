package de.jigp.plugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import de.jigp.plugin.configuration.Configuration;
import de.jigp.plugin.configuration.ConfigurationPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GeneratorComponent implements ProjectComponent, Configurable {
    private Configuration configuration = new Configuration();
    private ConfigurationPanel configurationPanel;

    @NotNull
    public String getComponentName() {
        return "Generate DTO, wrapper or builder for Interface with Getters or a given class";
    }

    public void initComponent() {
    }

    public void disposeComponent() {
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    public void apply() throws ConfigurationException {
        configuration = configurationPanel.getConfiguration();
        GeneratorContext.setConfiguration(configuration);
    }

    public void reset() {
        configurationPanel.setConfiguration(configuration);
    }

    public JComponent createComponent() {
        return configurationPanel = new ConfigurationPanel(configuration);
    }

    public void disposeUIResources() {
        configurationPanel = null;
    }

    public boolean isModified() {
        return !configuration.equals(configurationPanel.getConfiguration());
    }

    public String getDisplayName() {
        return "Generate Dto, Wrapper or Builder";
    }

    public String getHelpTopic() {
        return null;
    }

    public Icon getIcon() {
        return null;
    }
}
