package de.jigp.plugin.actions.wrapper;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import de.jigp.plugin.GeneratorPluginContext;
import de.jigp.plugin.actions.generator.AbstractGenerator;
import de.jigp.plugin.actions.generator.JavaLanguageSupport;

import java.util.Collection;

public class WrapperGenerator extends AbstractGenerator {
    protected String targetCopyConstructorText = "";

    public WrapperGenerator(DataContext dataContext, PsiClass annotatedClass, String targetClassSuffix) {
        super(dataContext, targetClassSuffix, annotatedClass, true, false);
    }

    protected void afterHandlingHook() {
        targetCopyConstructorText += "}";
        addOrReplaceMethod(targetCopyConstructorText);
    }

    protected void beforeHandlingHook() {
        targetCopyConstructorText += "public " + targetClassName() + "(" + sourceClassForGeneration.getQualifiedName() + " original){ this.delegate = original;";
    }

    protected void handleField(PsiField psiField) {

    }

    protected void handleMethod(PsiMethod psiMethod) {
        createGetterMethod(psiMethod);
        createSetterMethod(psiMethod);

        appendReturnTypeToImportList(psiMethod);
    }


    protected void createGetterMethod(PsiMethod psiMethod) {
        String fieldName = determineFieldNameFromGetterMethod(psiMethod);
        String fieldTypeName = determineFieldTypeNameFromGetterMethod(psiMethod);
        String getterName = determineGetterMethodNameFromGetterMethod(psiMethod);
        String getterCallText = getterName + "()";
        String mapValueType = fieldTypeName;
        if (JavaLanguageSupport.isPrimitiveType(psiMethod.getReturnType())) {
            mapValueType = convertFieldTypeToNonPrimitive(psiMethod);
        }

        String overrideOrNot = "";
        if (GeneratorPluginContext.getConfiguration().isGetterUsingOverride) {
            overrideOrNot = "@Override ";
        }
        String getterMethodText = overrideOrNot + "public " + fieldTypeName + " " + getterCallText +
                "{ if (modifiedAttributes.containsKey(\"" + fieldName + "\")){ return (" + mapValueType + ") modifiedAttributes.get(\"" + fieldName + "\");" +
                "}else { return delegate." + getterCallText + ";}}";
        addOrReplaceMethod(getterMethodText);
    }

    protected void createSetterMethod(PsiMethod psiMethod) {
        String fieldName = determineFieldNameFromGetterMethod(psiMethod);
        String fieldTypeName = determineFieldTypeNameFromGetterMethod(psiMethod);
        String setterName = determineSetterMethodNameFromGetterMethod(psiMethod);
        String mapValue = fieldName;
        if (JavaLanguageSupport.isPrimitiveType(psiMethod.getReturnType())) {
            mapValue = "new " + convertFieldTypeToNonPrimitive(psiMethod) + "(" + fieldName + ")";
        }
        String setMethodText = "public void " + setterName + "(" + fieldTypeName + " " + fieldName + ")" +
                "{ modifiedAttributes.put(\"" + fieldName + "\"," + mapValue + ");}";
        addOrReplaceMethod(setMethodText);

    }

    protected void addNewFields() {
        addDelegateField();
        addModifiedAttributesField();
    }

    protected Collection<PsiMethod> filterMethodsToHandle(PsiMethod[] psiMethods) {
        return filterGetterMethods(psiMethods);
    }

    protected Collection<PsiField> filterFieldsToHandle(PsiField[] psiFields) {
        return null;
    }

    private void addDelegateField() {
        PsiField delegateField =
                psiElementFactory.createFieldFromText("private " + sourceClassForGeneration.getQualifiedName() + " delegate;", null);
        addField(delegateField);
    }

    private void addModifiedAttributesField() {
        PsiField field =
                psiElementFactory.createFieldFromText("private java.util.HashMap<String,Object> modifiedAttributes = new java.util.HashMap<String,Object>();", null);
        addField(field);
    }
}