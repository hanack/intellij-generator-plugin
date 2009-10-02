package de.jigp.plugin.configuration;

public class TypeToTextFactory {

    public static TypeToTextMapping createDefaultVariableInitialization() {
        return new TypeToTextMapping()
                .put("java.util.Collection", "java.util.Collections.$<>$emptySet()")
                .put("java.util.Set", "java.util.Collections.$<>$emptySet()")
                .put("java.util.List", "java.util.Collections.$<>$emptyList()")
                .put("java.util.HashSet", "new java.util.HashSet$<>$()")
                .put("java.util.ArrayList", "new java.util.ArrayList$<>$()")
                .put("java.util.SortedSet", "new java.util.TreeSet$<>$()")
                .put("java.util.HashMap", "new java.util.HashMap$<>$()")
                .put("java.util.LinkedHashSet", "new java.util.LinkedHashSet$<>$()");

    }
}
