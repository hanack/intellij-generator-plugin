package de.jigp.plugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import de.jigp.plugin.GeneratorPluginComponent.Configuration;
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
        return !this.equals(configurationPanel.getConfiguration());
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


    public Configuration getState() {
        return configuration;
    }

    public void loadState(Configuration pluginConfiguration) {
        configuration = new Configuration();
        XmlSerializerUtil.copyBean(pluginConfiguration, configuration);
        GeneratorPluginContext.setConfiguration(configuration);
    }

    public static class Configuration {
        public String dtoAnnotation = "DtoAnnotationType";
        public String wrapperAnnotation = "WrapperAnnotationType";
        public String builderAnnotation = "BuilderAnnotationType";
        public String builderAssertionName = "org.springframework.util.Assert.notNull";


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Configuration that = (Configuration) o;

            if (builderAnnotation != null ? !builderAnnotation.equals(that.builderAnnotation) : that.builderAnnotation != null)
                return false;
            if (dtoAnnotation != null ? !dtoAnnotation.equals(that.dtoAnnotation) : that.dtoAnnotation != null)
                return false;
            if (wrapperAnnotation != null ? !wrapperAnnotation.equals(that.wrapperAnnotation) : that.wrapperAnnotation != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (dtoAnnotation != null ? dtoAnnotation.hashCode() : 0);
            result = 31 * result + (wrapperAnnotation != null ? wrapperAnnotation.hashCode() : 0);
            result = 31 * result + (builderAnnotation != null ? builderAnnotation.hashCode() : 0);
            return result;
        }
    }

}

