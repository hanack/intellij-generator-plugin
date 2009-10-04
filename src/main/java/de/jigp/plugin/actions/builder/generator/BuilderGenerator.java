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
    private Collection<PsiField> assertionPsiFields;


    public BuilderGenerator(DataContext dataContext, PsiClass targetClassSuffix, String annotatedClass) {
        super(dataContext, annotatedClass, targetClassSuffix, false, true);
    }

    @Override
    protected void beforeHandlingHook() {
        createBuilderConstructor();
    }

    private void createBuilderConstructor() {
        String sourceQualifiedName = sourceClassForGeneration.getQualifiedName();
        String builderConstructorMethodText = "public " + targetClassName() + "(){"
                + "instance = new " + sourceQualifiedName + "();"
                + "}";
        addOrReplaceMethod(builderConstructorMethodText);
    }

    @Override
    protected void handleMethod(PsiMethod psiMethod) {
    }

    @Override
    protected void handleField(PsiField psiField) {
        String setMethodText = "public " + sourceClassForGeneration.getQualifiedName() + "." + targetClassName() + " " + psiField.getName() + "(" + psiField.getTypeElement().getType().getCanonicalText() + " " + psiField.getName() + "){"
                + "instance." + psiField.getName() + "=" + psiField.getName() + ";"
                + "return this;"
                + "}";
        this.addOrReplaceMethod(setMethodText);

        if (isAssertionForFieldEnabled(psiField)) {
            String assertionText = GeneratorPluginContext.getConfiguration().builderAssertionExpression + "(instance." + psiField.getName() + ", \"Attribute: " + psiField.getName() + "\");";
            attributeAssertionTexts.add(assertionText);
        }


    }

    private boolean isAssertionForFieldEnabled(PsiField psiField) {
        return isWithAssertions && (assertionPsiFields == null || assertionPsiFields.contains(psiField));
    }

    @Override
    protected void afterHandlingHook() {
        StringBuilder buildMethodText = new StringBuilder("public ");
        buildMethodText.append(sourceClassForGeneration.getQualifiedName()).append(" build(){");

        if (isWithAssertions) {
            for (String attributeAssertionText : attributeAssertionTexts) {
                buildMethodText.append(attributeAssertionText);
            }
        }
        buildMethodText.append("return instance;}");

        this.addOrReplaceMethod(buildMethodText.toString());
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
        isWithAssertions = true;
        Collection<PsiField> selectedPsiFields = psiFieldFilterDialog.getSelectedPsiFields();
        if (psiFieldFilterDialog.getExitCode() == PsiFieldFilterDialog.CANCEL_EXIT_CODE) {
            throw new CancelActionException();
        }
        assertionPsiFields = psiFieldFilterDialog.getAssertionPsiFields();
        return selectedPsiFields;
    }

}
