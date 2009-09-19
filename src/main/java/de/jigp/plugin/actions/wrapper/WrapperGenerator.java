package de.jigp.plugin.actions.wrapper;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import de.jigp.plugin.actions.generator.AbstractGenerator;
import org.apache.commons.lang.StringUtils;

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
        String fieldTypeName = psiMethod.getReturnType().getCanonicalText();
        String fieldNameCaptilized = StringUtils.upperCase(fieldName.substring(0, 1)) + fieldName.substring(1);

        String getterName = "get" + fieldNameCaptilized;
        String getterCallText = getterName + "()";

        String getterMethodText = "public " + fieldTypeName + " " + getterCallText +
                "{ if (modifiedAttributes.containsKey(\"" + fieldName + "\")){ return (" + fieldTypeName + ") modifiedAttributes.get(\"" + fieldName + "\");" +
                "}else { return delegate." + getterCallText + ";}}";
        newMethods.add(getterMethodText);
    }

    protected void createSetterMethod(PsiMethod psiMethod) {
        String fieldName = determineFieldNameFromGetterMethod(psiMethod);
        String fieldTypeName = psiMethod.getReturnType().getCanonicalText();
        String fieldNameCaptilized = StringUtils.upperCase(fieldName.substring(0, 1)) + fieldName.substring(1);
        String setterName = "set" + fieldNameCaptilized;

        String setMethodText = "public void " + setterName + "(" + fieldTypeName + " " + fieldName + ")" +
                "{ modifiedAttributes.put(\"" + fieldName + "\"," + fieldName + ");}";
        newMethods.add(setMethodText);

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
                psiElementFactory.createFieldFromText("private java.util.HashMap<Object,Object> modifiedAttributes = new java.util.HashMap<Object,Object>();", null);
        addField(field);
    }
}