package de.jigp.plugin.actions.generator;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiImportList;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReferenceList;
import de.jigp.plugin.actions.menu.PsiInfrastructureHolder;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public abstract class AbstractGenerator extends PsiInfrastructureHolder {
    protected final String targetClassSuffix;
    protected final PsiClass sourceClassForGeneration;

    private PsiImportList targetImportList;
    private PsiClass targetClass;
    private PsiReferenceList targetImplementsList;
    private PsiReferenceList targetExtendsList;
    private boolean shouldTargetClassInheritingSource;
    private boolean isTargetInnerClass;


    public AbstractGenerator(DataContext dataContext, String targetClassSuffix, PsiClass sourceClass, boolean isTargetClassImplementingSource, boolean isTargetInnerClass) {
        super(dataContext);
        this.targetClassSuffix = targetClassSuffix;
        this.sourceClassForGeneration = sourceClass;
        this.shouldTargetClassInheritingSource = isTargetClassImplementingSource;
        this.isTargetInnerClass = isTargetInnerClass;
    }

    public PsiClass build() {
        try {
            return generate();
        } catch (CancelActionException e) {
            return null;
        }
    }

    private PsiClass generate() throws CancelActionException {
        initOrCreateTargetClass();
        modifyTargetClass();
        addTargetClassPhysically();
        return targetClass;
    }

    private void initOrCreateTargetClass() {
        if (this.isTargetInnerClass) {
            createEmptyInnerTargetClass();
        } else {
            createEmptyTargetClass();
        }
        initTargetClassData();
    }

    private PsiClass modifyTargetClass() throws CancelActionException {
        beforeHandlingHook();
        handleMethods();
        handleFields();
        afterHandlingHook();
        addClassInheritance();
        addNewFields();
        reformatCode();
        return targetClass;
    }

    private void addTargetClassPhysically() {
        if (this.isTargetInnerClass) {
            PsiClass innerClassByName = sourceClassForGeneration.findInnerClassByName(this.targetClassName(), false);
            if (innerClassByName == null) {
                sourceClassForGeneration.add(targetClass);
            }
        } else {
            // not inner classes are allready created and updated
        }
    }

    private void createEmptyInnerTargetClass() {
        targetClass = sourceClassForGeneration.findInnerClassByName(this.targetClassName(), false);
        if (targetClass == null) {
            targetClass = psiElementFactory.createClass(targetClassName());
            targetClass.getModifierList().setModifierProperty("static", true);
        }
    }

    private void addClassInheritance() {
        if (shouldTargetClassInheritingSource) {
            if (sourceClassForGeneration.isInterface()) {
                addImplementsSourceInterface(targetImplementsList);
            } else {
                addImplementsSourceInterface(targetExtendsList);
            }
        }
    }

    private void reformatCode() {
        if (codeStyleManager != null) {
            codeStyleManager.reformat(targetClass);
        }
    }


    private void addImplementsSourceInterface(PsiReferenceList targetList) {
        PsiJavaCodeReferenceElement newImportListType = psiElementFactory.createReferenceElementByType(psiElementFactory.createType(sourceClassForGeneration));
        PsiJavaCodeReferenceElement[] referenceElements = targetList.getReferenceElements();

        boolean implementsNecessary = true;
        for (PsiJavaCodeReferenceElement referenceElement : referenceElements) {
            if (referenceElement.getQualifiedName().equals(newImportListType.getQualifiedName())) {
                implementsNecessary = false;
                break;
            }
        }
        if (implementsNecessary) {
            targetList.add(newImportListType);
        }
    }

    private void handleMethods() {
        PsiMethod[] psiMethods = sourceClassForGeneration.getAllMethods();
        Collection<PsiMethod> filterdMethods = filterMethodsToHandle(psiMethods);
        if (filterdMethods == null) {
            return;
        }
        for (PsiMethod psiMethod : filterdMethods) {
            handleMethod(psiMethod);
        }
    }

    protected List<PsiMethod> filterGetterMethods(PsiMethod[] psiMethods) {
        List<PsiMethod> getterMethods = new ArrayList<PsiMethod>();
        for (PsiMethod psiMethod : psiMethods) {
            if (isGetter(psiMethod)) {
                getterMethods.add(psiMethod);
            }
        }
        return getterMethods;
    }

    private void handleFields() throws CancelActionException {
        PsiField[] psiFields = sourceClassForGeneration.getAllFields();
        Collection<PsiField> filteredFields = filterFieldsToHandle(psiFields);
        if (filteredFields == null) {
            return;
        }
        for (PsiField psiField : filteredFields) {
            handleField(psiField);
        }
    }

    protected void appendReturnTypeToImportList(PsiMethod psiMethod) {
        PsiClass importClass = psiFacade.findClass(psiMethod.getReturnType().getCanonicalText(), globalSearchScope);
        appendClassToImportList(importClass);
    }

    protected String determineFieldNameFromGetterMethod(PsiMethod psiMethod) {

        String attributeName = StringUtils.lowerCase(psiMethod.getName().substring(3, 4)) + psiMethod.getName().substring(4);
        attributeName = JavaLanguageSupport.transformToValidAttributeName(attributeName);
        return attributeName;
    }

    protected PsiMethod addOrReplaceMethod(String methodText) {
        return addOrReplaceMethod(targetClass, methodText);
    }

    protected PsiMethod addOrReplaceMethod(PsiClass target, String methodText) {
        PsiMethod newMethod = psiElementFactory.createMethodFromText(methodText, null);
        PsiMethod existingMethod = target.findMethodBySignature(newMethod, false);
        if (existingMethod != null) {
            existingMethod.replace(newMethod);
        } else {
            target.add(newMethod);
        }
        return newMethod;
    }

    protected void appendClassToImportList(PsiClass importClass) {
        if (importClass == null) {
            return;
        }
        PsiImportStatement newImportStatement = psiElementFactory.createImportStatement(importClass);

        boolean isImportNew = true;
        for (PsiImportStatement importStatement : targetImportList.getImportStatements()) {
            if (importStatement.getText().equals(newImportStatement.getText())) {
                isImportNew = false;
            }
        }
        if (isImportNew) {
            targetImportList.add(newImportStatement);
        }
    }

    private boolean isGetter(PsiMethod psiMethod) {
        return isGetter(psiMethod.getName());
    }

    public boolean isGetter(String methodName) {
        boolean startsWithGet = Pattern.matches("get[A-Z].*", methodName);
        return startsWithGet && !"getClass".equals(methodName);
    }

    protected void addField(PsiField newField) {
        PsiField existingField = targetClass.findFieldByName(newField.getName(), false);
        if (existingField != null) {
            existingField.replace(newField);
        } else {
            targetClass.add(newField);
        }
    }

    private void createEmptyTargetClass() {
        targetClass = psiFacade.findClass(qualifiedTargetClassName(), globalSearchScope);
        if (targetClass == null) {
            PsiDirectory sourceClassDirectory = sourceClassForGeneration.getContainingFile().getContainingDirectory();
            targetClass = JavaDirectoryService.getInstance().createClass(sourceClassDirectory, targetClassName());
        }
    }

    private void initTargetClassData() {
        PsiJavaFile targetJavaFile = (PsiJavaFile) targetClass.getContainingFile();
        targetImportList = targetJavaFile.getImportList();
        targetImplementsList = targetClass.getImplementsList();
        targetExtendsList = targetClass.getExtendsList();
    }


    public String qualifiedTargetClassName() {
        return sourceClassForGeneration.getQualifiedName() + targetClassSuffix;
    }

    public String targetClassName() {
        return createTargetClassName(sourceClassForGeneration.getName());
    }

    public String createTargetClassName(String name) {
        return name + targetClassSuffix;
    }

    protected abstract Collection<PsiMethod> filterMethodsToHandle(PsiMethod[] psiMethods);

    protected abstract Collection<PsiField> filterFieldsToHandle(PsiField[] psiFields) throws CancelActionException;

    protected abstract void beforeHandlingHook();

    protected abstract void handleMethod(PsiMethod psiMethod);

    protected abstract void handleField(PsiField psiField);

    protected abstract void afterHandlingHook();

    protected abstract void addNewFields();

    protected String determineGetterMethodNameFromGetterMethod(PsiMethod psiMethod) {
        return psiMethod.getName();
    }

    protected String determineSetterMethodNameFromGetterMethod(PsiMethod psiMethod) {
        String setterName = determineGetterMethodNameFromGetterMethod(psiMethod).replaceFirst("g", "s");
        return setterName;
    }

    protected String determineFieldTypeNameFromGetterMethod(PsiMethod psiMethod) {
        return psiMethod.getReturnType().getCanonicalText();
    }

    protected String convertFieldTypeToNonPrimitive(PsiMethod psiMethod) {
        return JavaLanguageSupport.convertToNonPrimitive(psiMethod.getReturnType());
    }
}
