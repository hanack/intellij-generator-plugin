package de.jigp.plugin.configuration;

import com.intellij.psi.PsiType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeToTextMapping implements Serializable {
    public HashMap<String, Entry> mapping = new HashMap<String, Entry>();
    public boolean isMappingActive = true;
    public static final String GENERICS_PLACEHOLDER = "$<>$";
    public static final String REGEXP_GENERICS_PLACEHOLDER = "\\$<>\\$";

    public TypeToTextMapping put(String type, String text, boolean isAddRemoveMethodRequested) {
        Entry entry = new Entry(type, text, isAddRemoveMethodRequested);
        mapping.put(type, entry);
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
        String genericsText = getElementTypeWithBrackets(type);

        return text.replaceAll(REGEXP_GENERICS_PLACEHOLDER, genericsText);
    }

    private Entry getEntry(String type) {
        if (isMappingActive && containsMapping(type)) {
            return mapping.get(type);
        }
        return null;

    }

    public String getText(PsiType type) {
        String typeWithoutGenerics = getTypeWithoutGenerics(type);
        Entry entry = getEntry(typeWithoutGenerics);
        if (entry != null && entry.text != null) {
            return replaceGenericPlaceholder(type, entry.text);
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
            entries.add(mapping.get(type));
        }

        return entries;
    }

    public boolean isAddRemoveRequested(PsiType type) {
        if (containsMapping(type)) {
            return getEntry(getTypeWithoutGenerics(type)).isAddRemoveMethodRequested;
        }
        return false;
    }

    public String getElementTypeWithBrackets(PsiType type) {
        String canonicalTypeName = type.getCanonicalText();
        Pattern pattern = Pattern.compile("<.*>");
        Matcher matcher = pattern.matcher(canonicalTypeName);
        boolean isGroupExisting = matcher.find();
        if (isGroupExisting) {
            return matcher.group();
        }
        return canonicalTypeName;

    }

    public String getElementType(PsiType type) {
        String genericsTextWithBrackets = getElementTypeWithBrackets(type);
        if (genericsTextWithBrackets.startsWith("<")) {
            return genericsTextWithBrackets.substring(1, genericsTextWithBrackets.length() - 1);
        } else {
            return genericsTextWithBrackets;
        }

    }

    public static class Entry {
        public String text;
        public String type;
        public Boolean isAddRemoveMethodRequested;

        public Entry() {
        }

        public Entry(String type, String text, Boolean isAddRemoveMethodRequested) {
            this.type = type;
            this.text = text;
            this.isAddRemoveMethodRequested = isAddRemoveMethodRequested;

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
