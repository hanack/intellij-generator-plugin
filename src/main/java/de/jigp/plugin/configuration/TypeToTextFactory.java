package de.jigp.plugin.configuration;

import static de.jigp.plugin.configuration.TypeToTextMapping.GENERICS_PLACEHOLDER;

public class TypeToTextFactory {

    public static TypeToTextMapping createDefaultVariableInitialization() {
        return new TypeToTextMapping()
                .put("java.util.Collection", "java.util.Collections." + GENERICS_PLACEHOLDER + "emptySet()")
                .put("java.util.Set", "java.util.Collections." + GENERICS_PLACEHOLDER + "emptySet()")
                .put("java.util.List", "java.util.Collections." + GENERICS_PLACEHOLDER + "emptyList()")
                .put("java.util.HashSet", "new java.util.HashSet" + GENERICS_PLACEHOLDER + "()")
                .put("java.util.ArrayList", "new java.util.ArrayList" + GENERICS_PLACEHOLDER + "()")
                .put("java.util.SortedSet", "new java.util.TreeSet" + GENERICS_PLACEHOLDER + "()")
                .put("java.util.HashMap", "new java.util.HashMap" + GENERICS_PLACEHOLDER + "()")
                .put("java.util.LinkedHashSet", "new java.util.LinkedHashSet" + GENERICS_PLACEHOLDER + "()");

    }
}
