package de.jigp.plugin.actions.generator;

import com.intellij.psi.PsiType;

import java.util.Arrays;
import java.util.List;

public class KeywordHandler {


    private static final List<String> keywords = Arrays.asList(
            "abstract", "continue", "for", "new", "switch",
            "assert", "default", "goto", "package", "synchronized",
            "boolean", "do", "if", "private", "this",
            "break", "double", "implements", "protected", "throw",
            "byte", "else", "import", "public", "throws",
            "case", "enum", "instanceof", "return", "transient",
            "catch", "extends", "int", "short", "try",
            "char", "final", "interface", "static", "void",
            "class", "finally", "long", "strictfp", "volatile",
            "const", "float", "native", "super", "while");

    public static String transformToValidAttributeName(String attributeName) {
        if (isKeyword(attributeName)) {
            return attributeName + "_";
        } else {
            return attributeName;
        }
    }

    public static boolean isKeyword(String attributeName) {
        return keywords.contains(attributeName);
    }


    public static String convertToNonPrimitive(PsiType type) {
        return new NonPrimitiveTypeDetermination(type).getType();
    }


    public static boolean isPrimitiveType(PsiType type) {
        if (type.getCanonicalText().startsWith("java")) {
            return false;
        }

        if (type.isAssignableFrom(PsiType.BOOLEAN) ||
                type.isAssignableFrom(PsiType.BYTE) ||
                type.isAssignableFrom(PsiType.CHAR) ||
                type.isAssignableFrom(PsiType.DOUBLE) ||
                type.isAssignableFrom(PsiType.FLOAT) ||
                type.isAssignableFrom(PsiType.INT) ||
                type.isAssignableFrom(PsiType.LONG) ||
                type.isAssignableFrom(PsiType.SHORT)) {
            return true;
        }

        PsiType subType = type.getDeepComponentType();
        if (subType.isAssignableFrom(PsiType.BOOLEAN) ||
                subType.isAssignableFrom(PsiType.BYTE) ||
                subType.isAssignableFrom(PsiType.CHAR) ||
                subType.isAssignableFrom(PsiType.DOUBLE) ||
                subType.isAssignableFrom(PsiType.FLOAT) ||
                subType.isAssignableFrom(PsiType.INT) ||
                subType.isAssignableFrom(PsiType.LONG) ||
                subType.isAssignableFrom(PsiType.SHORT)) {
            return true;
        }

        return false;
    }

    public static class NonPrimitiveTypeDetermination {
        private PsiType type;
        private String convertedType = null;

        public NonPrimitiveTypeDetermination(PsiType type) {
            this.type = type;
        }

        public String getType() {
            if (!type.getCanonicalText().startsWith("java")) {
                convertTypeIfPossible(PsiType.BOOLEAN, "Boolean");
                convertTypeIfPossible(PsiType.INT, "Integer");
                convertTypeIfPossible(PsiType.DOUBLE, "Double");
                convertTypeIfPossible(PsiType.FLOAT, "Float");
                convertTypeIfPossible(PsiType.CHAR, "Character");
                convertTypeIfPossible(PsiType.BYTE, "Byte");
                convertTypeIfPossible(PsiType.SHORT, "Short");
                convertTypeIfPossible(PsiType.LONG, "Long");
            }
            if (convertedType == null) {
                convertedType = type.getCanonicalText();
            }
            return convertedType;
        }

        private void convertTypeIfPossible(PsiType primitiveType, String nonPrimitiveTypeName) {
            if (convertedType == null && primitiveType.equals(type)) {
                convertedType = nonPrimitiveTypeName;
            }
        }
    }

}
