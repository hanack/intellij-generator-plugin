package de.jigp.plugin.actions.menu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;

public abstract class HandleAnnotatedClassAction extends AnAction {
    private DataContext dataContext;

    public void actionPerformed(AnActionEvent actionEvent) {
        dataContext = actionEvent.getDataContext();
        Project project = DataKeys.PROJECT.getData(dataContext);
        final Application application = ApplicationManager.getApplication();

        Runnable runnable = new Runnable() {
            public void run() {
                Computable<PsiClass> psiClassComputable = createComputable(dataContext);
                application.runWriteAction(psiClassComputable);
            }
        };
        CommandProcessor.getInstance().executeCommand(project, runnable, null, null);

    }

    protected Computable<PsiClass> createComputable(DataContext dataContext) {
        return new FindAndHandleClassComputable(dataContext, this, getAnnotationName(), getTargetClassChooser(dataContext));
    }

    protected abstract String getAnnotationName();

    protected abstract void handleClass(DataContext dataContext, PsiClass annotatedClass, String targetClassSuffix);

    protected abstract DetermineTargetClassChooser getTargetClassChooser(DataContext dataContext);
}
