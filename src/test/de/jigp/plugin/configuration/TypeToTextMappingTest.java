package de.jigp.plugin.configuration;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeToTextMappingTest {
    private static final String GENERICS_PLACEHOLDER = "\\$<>\\$";


    @Test
    public void getTypeWithoutGenerics() {
        String canonicalTypeName = "java.util.List<List<List<String>>>";
        String typeWithoutGenerics = canonicalTypeName.replaceAll("<.*>", "");
        assertEquals(typeWithoutGenerics, "java.util.List");
    }

    @Test
    public void replaceGenericPlaceholder() {
        String canonicalTypeName = "java.util.List<List<List<String>>>";
        Pattern pattern = Pattern.compile("<.*>");
        Matcher matcher = pattern.matcher(canonicalTypeName);
        matcher.find();
        String genericsText = matcher.group();

        String initializerText = "new java.util.Collection.$<>$emptyList($<>$)";
        String initializer = initializerText.replaceAll(GENERICS_PLACEHOLDER, genericsText);
        assertEquals("new java.util.Collection.<List<List<String>>>emptyList(<List<List<String>>>)", initializer);

    }
}
