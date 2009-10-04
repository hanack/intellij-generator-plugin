package de.jigp.plugin.configuration;

import java.io.Serializable;

public class Configuration implements Serializable {
    public String dtoAnnotation;
    public String wrapperAnnotation;
    public String builderAnnotation;
    public String builderAssertionExpression;
    public String dtoSuffix;
    public String wrapperSuffix;
    public boolean isSuffixQuestionSupressed;
    public boolean isGetterUsingOverride = true;

    public TypeToTextMapping variableInitializers;

    public Configuration() {
//        reinitNullValues();
    }

    public void reinitNullValues() {
        dtoAnnotation = isEmpty(dtoAnnotation) ? "DtoAnnotationType" : dtoAnnotation;
        builderAnnotation = isEmpty(builderAnnotation) ? "BuilderAnnotationType" : builderAnnotation;
        builderAssertionExpression = isEmpty(builderAssertionExpression) ? "org.springframework.util.Assert.notNull" : builderAssertionExpression;
        dtoSuffix = isEmpty(dtoSuffix) ? "Dto" : dtoSuffix;
        wrapperSuffix = isEmpty(wrapperSuffix) ? "Wrapper" : wrapperSuffix;
        isGetterUsingOverride = true;
        //TODO loadState handling
        if (variableInitializers == null || variableInitializers.isEmpty()) {
            variableInitializers = TypeToTextFactory.createDefaultVariableInitialization();
        }
    }

    private boolean isEmpty(String string) {
        boolean isEmpty = string == null
                || string.trim().equals("");
        return isEmpty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (isGetterUsingOverride != that.isGetterUsingOverride) return false;
        if (isSuffixQuestionSupressed != that.isSuffixQuestionSupressed) return false;
        if (builderAnnotation != null ? !builderAnnotation.equals(that.builderAnnotation) : that.builderAnnotation != null)
            return false;
        if (builderAssertionExpression != null ? !builderAssertionExpression.equals(that.builderAssertionExpression) : that.builderAssertionExpression != null)
            return false;
        if (dtoAnnotation != null ? !dtoAnnotation.equals(that.dtoAnnotation) : that.dtoAnnotation != null)
            return false;
        if (dtoSuffix != null ? !dtoSuffix.equals(that.dtoSuffix) : that.dtoSuffix != null) return false;
        if (variableInitializers != null ? !variableInitializers.equals(that.variableInitializers) : that.variableInitializers != null)
            return false;
        if (wrapperAnnotation != null ? !wrapperAnnotation.equals(that.wrapperAnnotation) : that.wrapperAnnotation != null)
            return false;
        if (wrapperSuffix != null ? !wrapperSuffix.equals(that.wrapperSuffix) : that.wrapperSuffix != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dtoAnnotation != null ? dtoAnnotation.hashCode() : 0;
        result = 31 * result + (wrapperAnnotation != null ? wrapperAnnotation.hashCode() : 0);
        result = 31 * result + (builderAnnotation != null ? builderAnnotation.hashCode() : 0);
        result = 31 * result + (builderAssertionExpression != null ? builderAssertionExpression.hashCode() : 0);
        result = 31 * result + (dtoSuffix != null ? dtoSuffix.hashCode() : 0);
        result = 31 * result + (wrapperSuffix != null ? wrapperSuffix.hashCode() : 0);
        result = 31 * result + (isSuffixQuestionSupressed ? 1 : 0);
        result = 31 * result + (isGetterUsingOverride ? 1 : 0);
        result = 31 * result + (variableInitializers != null ? variableInitializers.hashCode() : 0);
        return result;
    }
}

