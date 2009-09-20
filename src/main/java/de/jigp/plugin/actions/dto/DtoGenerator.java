package de.jigp.plugin.actions.dto;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import de.jigp.plugin.actions.generator.AbstractGenerator;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DtoGenerator extends AbstractGenerator {


    private String copyConstructorMethodText = "";
    private List<String> collectionClassNames;


    public DtoGenerator(DataContext dataContext, PsiClass annotatedClass, String targetClassSuffix) {
        super(dataContext, targetClassSuffix, annotatedClass, true, false);
        collectionClassNames = new ArrayList<String>();
        collectionClassNames.add(java.util.Collection.class.getName());
    }


    protected void addNewFields() {
    }

    protected Collection<PsiMethod> filterMethodsToHandle(PsiMethod[] psiMethods) {
        return filterGetterMethods(psiMethods);
    }

    protected Collection<PsiField> filterFieldsToHandle(PsiField[] psiFields) {
        return null;
    }

    protected void afterHandlingHook() {
        copyConstructorMethodText += "}";
        addOrReplaceMethod(copyConstructorMethodText);
        addDtoDefaultConstructor();
    }

    protected void beforeHandlingHook() {

        String sourceQualifiedName = sourceClassForGeneration.getQualifiedName();
        copyConstructorMethodText += "public " + targetClassName() + "(" + sourceQualifiedName + " original){";
    }

    protected void handleField(PsiField psiField) {

    }

    protected void handleMethod(PsiMethod psiMethod) {
        com.intellij.psi.PsiType fieldType = psiMethod.getReturnType();
        String fieldName = determineFieldNameFromGetterMethod(psiMethod);
        String fieldTypeName = fieldType.getCanonicalText();

        createField(fieldName, fieldTypeName);
        createGetter(fieldName, fieldTypeName);
        createSetter(fieldName, fieldTypeName);
        addConsturctorTexts(fieldName);
        addReturnTypeToImportList(psiMethod);
    }

    private void addReturnTypeToImportList(PsiMethod psiMethod) {
        PsiClass importClass = psiFacade.findClass(psiMethod.getReturnType().getCanonicalText(), globalSearchScope);
        super.appendClassToImportList(importClass);
    }

    private void createField(String fieldName, String fieldTypeName) {
        PsiField field = psiElementFactory.createFieldFromText("private " + fieldTypeName + " "
                + fieldName + ";", null);
        this.addField(field);
    }

    private void createSetter(String fieldName, String fieldTypeName) {
        String setMethodText = "public void set" +
                StringUtils.capitalize(fieldName) + "(" + fieldTypeName + " " + fieldName + ") {" +
                "this." + fieldName + "=" + fieldName + ";}";
        addOrReplaceMethod(setMethodText);
    }

    private void createGetter(String fieldName, String fieldTypeName) {
        String methodText = "public " + fieldTypeName + " get" +
                StringUtils.capitalize(fieldName) + "() {" +
                "return this." + fieldName + ";}";
        PsiMethod getterMethod = this.addOrReplaceMethod(methodText);

        addReturnTypeToImportList(getterMethod);
    }

    private void addConsturctorTexts(String fieldName) {
        addConstructorTextForType(fieldName);
    }


    private void addDtoDefaultConstructor() {
        String defaultConstructorMethodText = "public " + targetClassName() + "(){}";
        addOrReplaceMethod(defaultConstructorMethodText);
    }

    private void addConstructorTextForType(String fieldName) {
        copyConstructorMethodText += "this." + fieldName + "=original.get" + StringUtils.capitalize(fieldName) + "();";

    }
}

