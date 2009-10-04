package de.jigp.plugin.actions.dto;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import de.jigp.plugin.GeneratorPluginContext;
import de.jigp.plugin.actions.generator.AbstractGenerator;
import de.jigp.plugin.configuration.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DtoGenerator extends AbstractGenerator {


    private String copyConstructorMethodText = "";
    private List<String> collectionClassNames;
    private PsiType fieldType;
    private String fieldName;
    private String fieldTypeName;
    private PsiMethod psiMethod;
    private Configuration configuration;


    public DtoGenerator(DataContext dataContext, PsiClass annotatedClass, String targetClassSuffix) {
        super(dataContext, targetClassSuffix, annotatedClass, true, false);
        collectionClassNames = new ArrayList<String>();
        collectionClassNames.add(java.util.Collection.class.getName());
        configuration = GeneratorPluginContext.getConfiguration();
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
        this.psiMethod = psiMethod;
        fieldType = psiMethod.getReturnType();
        fieldName = determineFieldNameFromGetterMethod(psiMethod);
        fieldTypeName = fieldType.getCanonicalText();

        createField();
        createGetter();
        createSetter();
        addConstructorTextForType();
        addReturnTypeToImportList(psiMethod);
    }

    private void addReturnTypeToImportList(PsiMethod psiMethod) {
        PsiClass importClass = psiFacade.findClass(psiMethod.getReturnType().getCanonicalText(), globalSearchScope);
        super.appendClassToImportList(importClass);
    }

    private void createField() {
        PsiField field = psiElementFactory.createFieldFromText("private " + fieldTypeName + " "
                + fieldName + ";", null);
        setInitializer(field);
        this.addField(field);
    }

    private void setInitializer(PsiField field) {
        String initializerText = configuration.variableInitializers.getText(field.getType());
        PsiExpression initializer = null;
        if (initializerText != null) {
            initializer = psiElementFactory.createExpressionFromText(initializerText, null);
        }
        field.setInitializer(initializer);
    }

    private void createSetter() {
        String setterMethodName = determineSetterMethodNameFromGetterMethod(psiMethod);
        String setMethodText = "public void " + setterMethodName + "(" + fieldTypeName + " " + fieldName + ") {" +
                "this." + fieldName + "=" + fieldName + ";}";
        addOrReplaceMethod(setMethodText);
    }

    private void createGetter() {
        String getterMethodName = determineGetterMethodNameFromGetterMethod(psiMethod);
        String overrideOrNot = "";
        if (configuration.isGetterUsingOverride) {
            overrideOrNot = "@Override";
        }
        String methodText = overrideOrNot + " public " + fieldTypeName + " " + getterMethodName + "() {" +
                "return this." + fieldName + ";}";
        PsiMethod getterMethod = this.addOrReplaceMethod(methodText);

        addReturnTypeToImportList(getterMethod);
    }


    private void addDtoDefaultConstructor() {
        String defaultConstructorMethodText = "public " + targetClassName() + "(){}";
        addOrReplaceMethod(defaultConstructorMethodText);
    }

    private void addConstructorTextForType() {
        String getterMethodName = determineGetterMethodNameFromGetterMethod(psiMethod);
        copyConstructorMethodText += "this." + fieldName + "=original." + getterMethodName + "();";

    }
}

