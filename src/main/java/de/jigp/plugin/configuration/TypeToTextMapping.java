package de.jigp.plugin.configuration;

import com.intellij.psi.PsiType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeToTextMapping implements Serializable {
    public HashMap<String, String> mapping = new HashMap<String, String>();
    public boolean isMappingActive = true;
    public static final String GENERICS_PLACEHOLDER = "$<>$";
    public static final String REGEXP_GENERICS_PLACEHOLDER = "\\$<>\\$";

    public TypeToTextMapping put(String type, String text) {
        mapping.put(type, text);
        return this;
    }

    private boolean containsMapping(String type) {
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

        return text.replaceAll(REGEXP_GENERICS_PLACEHOLDER, genericsText);
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

    public void setMappingActive(boolean isMappingActive) {
        this.isMappingActive = isMappingActive;
    }

    public boolean isEmpty() {
        return mapping.isEmpty();
    }

    public int size() {
        return mapping.size();
    }

    public List<Entry> entries() {
        ArrayList<Entry> entries = new ArrayList<Entry>();

        for (String type : mapping.keySet()) {
            entries.add(new Entry(type, mapping.get(type)));
        }

        return entries;
    }

    public static class Entry {
        public final String text;
        public final String type;

        public Entry(String type, String text) {
            this.type = type;
            this.text = text;

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeToTextMapping that = (TypeToTextMapping) o;

        if (isMappingActive != that.isMappingActive) return false;
        if (!mapping.equals(that.mapping)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mapping.hashCode();
        result = 31 * result + (isMappingActive ? 1 : 0);
        return result;
    }
}
