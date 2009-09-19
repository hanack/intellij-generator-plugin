package de.jigp.plugin.actions.dto;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.GenericsUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.searches.AnnotatedMembersSearch;
import com.intellij.util.Query;
import de.jigp.plugin.actions.generator.AbstractGenerator;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DtoGenerator extends AbstractGenerator {


    private String copyConstructorMethodText = "";
    private String complexCopyConstructorMethodText = "";
    private String staticDeepCopyFuntion = "";
    private String annotationName;
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
        complexCopyConstructorMethodText += "}";
        addOrReplaceMethod(copyConstructorMethodText);
        addDtoDefaultConstructor();
//        constructorTexts.add(complexCopyConstructorMethodText);
//        super.addOrReplaceMethod(staticDeepCopyFuntion);
    }

    protected void beforeHandlingHook() {

        String sourceQualifiedName = sourceClassForGeneration.getQualifiedName();
        copyConstructorMethodText += "public " + targetClassName() + "(" + sourceQualifiedName + " original){";

        complexCopyConstructorMethodText += "private " + targetClassName() + "(" +
                sourceQualifiedName + " original, java.util.HashMap < Object, Object > allDtos){";

        staticDeepCopyFuntion += "public static " + sourceQualifiedName + " createDeepCopy(" + sourceQualifiedName +
                " original, java.util.HashMap < Object, Object > allDtos){" +
                " return new " + targetClassName() + "(original,allDtos);}";

//        addDtoDefaultConstructor();
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
        addConsturctorTexts(fieldType, fieldName);
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

    private void addConsturctorTexts(PsiType fieldType, String fieldName) {
        addConstructorTextForType(fieldName);
//        if (fieldType instanceof PsiClassType) {
//
//            PsiClassType classType = (PsiClassType) fieldType;
//            PsiClass psiFieldClass = classType.resolve();
//            if (isCollection(psiFieldClass)) {
//                handleCollection(psiFieldClass, classType);
//
//            } else {
//                handleType(psiFieldClass, fieldName);
//            }
//        }
    }


    private void addDtoDefaultConstructor() {
        String defaultConstructorMethodText = "public " + targetClassName() + "(){}";
        addOrReplaceMethod(defaultConstructorMethodText);
    }

    private void handleCollection(PsiClass psiFieldClass, PsiClassType psiClassType) {
        complexCopyConstructorMethodText += psiClassType + "   ";
        complexCopyConstructorMethodText += GenericsUtil.getVariableTypeByExpressionType(psiClassType) + "    ";
        complexCopyConstructorMethodText += psiClassType.getDeepComponentType() + "    ";


    }

    private void handleType(PsiClass psiFieldClass, String fieldName) {
        String getOriginalObject =
                "original.get" + StringUtils.capitalize(fieldName) + "()";
        String thisField = "this." + fieldName;

        Query<PsiMember> query = AnnotatedMembersSearch.search(psiFacade.findClass(annotationName, globalSearchScope));
        Collection<PsiMember> memberCollection = query.findAll();

        boolean isDeepCopyCall = false;
        String fieldTypeNamePresentable = psiFieldClass.getQualifiedName();
        if (psiFieldClass != null && memberCollection.contains(psiFieldClass)) {
            fieldTypeNamePresentable = createTargetClassName(psiFieldClass.getQualifiedName());
            isDeepCopyCall = true;
        }
        if (isDeepCopyCall) {
            complexCopyConstructorMethodText += "this(original);" +
                    "allDtos.put(original, this);" +
                    "if(allDtos.containsKey(" + getOriginalObject + ")){" +
                    "this." + fieldName + "= (" + fieldTypeNamePresentable + ")allDtos.get(" + getOriginalObject + ");" +
                    "} else {" + "this." + fieldName + "= " + fieldTypeNamePresentable + ".createDeepCopy(original.get"
                    + StringUtils.capitalize(fieldName) + "(),allDtos);" +

                    "allDtos.put(" + getOriginalObject + "," + thisField + ");}";
        } else {
            copyConstructorMethodText += "this." + fieldName + "=original.get" + StringUtils.capitalize(fieldName) + "();";
        }

    }

    private void addConstructorTextForType(String fieldName) {
        copyConstructorMethodText += "this." + fieldName + "=original.get" + StringUtils.capitalize(fieldName) + "();";

    }

    private boolean isCollection(PsiClass psiFieldClass) {
        for (String collectionClassName : collectionClassNames) {
            PsiClass collectionClass = psiFacade.findClass(collectionClassName, globalSearchScope);
            if (psiFieldClass != null && (psiFieldClass.isInheritor(collectionClass, true) || psiFieldClass.equals(collectionClass))) {
                return true;
            }
        }
        return false;
    }
}

