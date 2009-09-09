package de.jigp.plugin.actions.contextmenu;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.pom.Navigatable;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import de.jigp.plugin.PsiHelper;

public abstract class GeneratorEditorActionHandler extends EditorActionHandler {
    public void execute(Editor editor, DataContext dataContext) {
        final PsiHelper psiHelper = new PsiHelper(dataContext);
        final PsiClass selectedInterface = psiHelper.getCurrentClass();
        final Project project = psiHelper.getProject();

//        if (selectedInterface != null && !selectedInterface.isInterface()) {
//            com.intellij.openapi.ui.Messages.showMessageDialog(project, "Generator works just for interfaces", "Warning", Messages.getWarningIcon());
//            return;
//        }

        JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
        psiHelper.setElementFactory(psiFacade.getElementFactory());

        Computable<PsiClass> computable = createGenerator(dataContext, selectedInterface);
        PsiClass builderClass = com.intellij.openapi.application.ApplicationManager.getApplication().runWriteAction(computable);

        if (builderClass != null) {
            jumpToGeneratoedClass(builderClass);
        }
    }

    protected abstract Computable<PsiClass> createGenerator(DataContext dataContext, PsiClass selectedInterface);

    private void jumpToGeneratoedClass(PsiClass psiClass) {
        Navigatable navigatable = com.intellij.ide.util.EditSourceUtil.getDescriptor(psiClass);
        if (navigatable != null) {
            navigatable.navigate(true);
        }
    }
}
