package de.jigp.plugin;

import de.jigp.plugin.GeneratorPluginComponent.Configuration;


public class GeneratorPluginContext {

    private static Configuration configuration;

    public static Configuration getConfiguration() {
        return GeneratorPluginContext.configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        GeneratorPluginContext.configuration = configuration;
    }
}
