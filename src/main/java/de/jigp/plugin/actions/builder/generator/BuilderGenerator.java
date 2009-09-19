package de.jigp.plugin.actions.builder.generator;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import de.jigp.plugin.GeneratorPluginContext;
import de.jigp.plugin.actions.builder.PsiFieldFilterDialog;
import de.jigp.plugin.actions.generator.AbstractGenerator;
import de.jigp.plugin.actions.generator.CancelActionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BuilderGenerator extends AbstractGenerator {
    private boolean isWithAssertions;
    private List<String> attributeAssertionTexts = new ArrayList<String>();


    public BuilderGenerator(DataContext dataContext, PsiClass targetClassSuffix, String annotatedClass) {
        super(dataContext, annotatedClass, targetClassSuffix, false, true);
    }

    @Override
    protected void beforeHandlingHook() {
        createBuilderConstructor();
    }

    private void createBuilderConstructor() {
        String sourceQualifiedName = sourceClassForGeneration.getQualifiedName();
        String builderConstructorMethodText = "private " + targetClassName() + "(){"
                + "instance = new " + sourceQualifiedName + "();"
                + "}";
        this.addConstructorText(builderConstructorMethodText);
    }

    @Override
    protected void handleMethod(PsiMethod psiMethod) {
    }

    @Override
    protected void handleField(PsiField psiField) {
        String setMethodText = "public " + targetClassName() + " " + psiField.getName() + "(" + psiField.getTypeElement().getType().getPresentableText() + " " + psiField.getName() + "){"
                + "instance." + psiField.getName() + "=" + psiField.getName() + ";"
                + "return this;"
                + "}";
        this.addOrReplaceMethod(setMethodText);

        String assertionText = GeneratorPluginContext.getConfiguration().builderAssertionName + "(instance." + psiField.getName() + ", \"Attribute: " + psiField.getName() + "\");";
        attributeAssertionTexts.add(assertionText);


    }

    @Override
    protected void afterHandlingHook() {
        String buildMethodText = "public " + sourceClassForGeneration.getQualifiedName() + " build(){";

        if (isWithAssertions) {
            for (String attributeAssertionText : attributeAssertionTexts) {
                buildMethodText += attributeAssertionText;
            }
        }
        buildMethodText +=
                "return instance;"
                        + "}";

        this.addOrReplaceMethod(buildMethodText);

        String builderFactoryMethodText = "public static " + targetClassName() + " new" + targetClassName() + "(){"
                + "return new " + targetClassName() + "();"
                + "}";
        this.addOrReplaceMethod(super.sourceClassForGeneration, builderFactoryMethodText);

    }

    @Override
    protected void addNewFields() {
        PsiField instanceField =
                psiElementFactory.createFieldFromText("private " + sourceClassForGeneration.getQualifiedName() + " instance;", null);
        addField(instanceField);
    }

    protected Collection<PsiMethod> filterMethodsToHandle(PsiMethod[] psiMethods) {
        return null;
    }

    protected Collection<PsiField> filterFieldsToHandle(PsiField[] psiFields) throws CancelActionException {

        if (psiFields == null) {
            return Collections.emptySet();
        }
        PsiFieldFilterDialog psiFieldFilterDialog = new PsiFieldFilterDialog(psiFields);
        psiFieldFilterDialog.show();
        isWithAssertions = psiFieldFilterDialog.isWithAssertions();
        Collection<PsiField> selectedPsiFields = psiFieldFilterDialog.getSelectedPsiFields();
        if (psiFieldFilterDialog.getExitCode() == PsiFieldFilterDialog.CANCEL_EXIT_CODE) {
            throw new CancelActionException();
        }
        return selectedPsiFields;
    }

}
