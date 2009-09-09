package de.jigp.plugin;

import de.jigp.plugin.configuration.Configuration;

public class GeneratorContext {

    private static Configuration configuration;

    public static Configuration getConfiguration() {
        if (configuration==null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        GeneratorContext.configuration = configuration;
    }
}
