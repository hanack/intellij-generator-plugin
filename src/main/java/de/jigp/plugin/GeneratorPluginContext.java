package de.jigp.plugin;

import de.jigp.plugin.configuration.Configuration;


public class GeneratorPluginContext {

    private static Configuration configuration = new Configuration();

    public static Configuration getConfiguration() {
        return GeneratorPluginContext.configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        GeneratorPluginContext.configuration = configuration;
    }
}
