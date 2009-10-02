package de.jigp.plugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import de.jigp.plugin.configuration.Configuration;
import de.jigp.plugin.configuration.PluginConfigurationPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@State(
        name = "GeneratorPluginComponent",
        storages = {
                @Storage(id = "idGeneratorPluginComponent",
                        file = "$PROJECT_FILE$")})
public class GeneratorPluginComponent implements ProjectComponent, Configurable,
        PersistentStateComponent<Configuration> {
    private PluginConfigurationPanel configurationPanel;
    private Configuration configuration;

    @NotNull
    public String getComponentName() {
        return "Generate DTO, wrapper or builder for Interface with Getters or a given class";
    }

    public void initComponent() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        GeneratorPluginContext.setConfiguration(configuration);
    }

    public void disposeComponent() {
    }

    public void projectOpened() {
    }

    public void projectClosed() {
    }

    public void apply() throws ConfigurationException {
        configuration = configurationPanel.getConfiguration();
        GeneratorPluginContext.setConfiguration(configuration);
    }

    public void reset() {
        configurationPanel.setConfiguration(configuration);
    }

    public JComponent createComponent() {
        if (configurationPanel == null) {
            configurationPanel = new PluginConfigurationPanel();
        }
        return configurationPanel.getJPanel();

    }

    public void disposeUIResources() {
        configurationPanel = null;
    }

    public boolean isModified() {
        return !this.configuration.equals(configurationPanel.getConfiguration());
    }

    public String getDisplayName() {
        return "Generate Dto, Wrapper or Builder";
    }

    public String getHelpTopic() {
        return "This Plugin generates 3 types of classes for a given Java Interface or Java Class.";
    }

    public Icon getIcon() {
        return null;
    }


    public Configuration getState() {
        return configuration;
    }

    public void loadState(Configuration pluginConfiguration) {
        configuration = new Configuration();
        XmlSerializerUtil.copyBean(pluginConfiguration, configuration);
        configuration.reinitNullValues();
        GeneratorPluginContext.setConfiguration(configuration);
    }
}

