package de.jigp.plugin.configuration;

import com.intellij.psi.PsiType;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeToTextMapping {
    private Map<String, String> mapping = new HashMap<String, String>();
    private boolean isMappingActive = true;
    private static final String GENERICS_PLACEHOLDER = "\\$<>\\$";

    public TypeToTextMapping put(String type, String text) {
        mapping.put(type, text);
        return this;
    }

    public boolean containsMapping(String type) {
        return mapping.containsKey(type);
    }

    public boolean containsMapping(PsiType type) {
        return containsMapping(getTypeWithoutGenerics(type));
    }

    private String getTypeWithoutGenerics(PsiType type) {
        String canonicalTypeName = type.getCanonicalText();
        String typeWithoutGenerics = canonicalTypeName.replaceAll("<.*>", "");
        return typeWithoutGenerics;
    }

    private String replaceGenericPlaceholder(PsiType type, String text) {
        String canonicalTypeName = type.getCanonicalText();
        Pattern pattern = Pattern.compile("<.*>");
        Matcher matcher = pattern.matcher(canonicalTypeName);
        matcher.find();
        String genericsText = matcher.group();

        return text.replaceAll(GENERICS_PLACEHOLDER, genericsText);
    }

    public String getText(String type) {
        if (isMappingActive && containsMapping(type)) {
            return mapping.get(type);
        }
        return null;

    }

    public String getText(PsiType type) {
        String textWithGenericsPlaceholder = getText(getTypeWithoutGenerics(type));
        if (textWithGenericsPlaceholder != null) {
            return replaceGenericPlaceholder(type, textWithGenericsPlaceholder);
        } else {
            return null;
        }
    }

    public boolean isMappingActive() {
        return isMappingActive;
    }

    public void setMappingActive(boolean mappingActive) {
        isMappingActive = mappingActive;
    }

    public boolean isEmpty() {
        return mapping.isEmpty();
    }
}
