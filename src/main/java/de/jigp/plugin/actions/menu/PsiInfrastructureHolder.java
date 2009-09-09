package de.jigp.plugin.actions.menu;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;

public class PsiInfrastructureHolder {

    protected final Project project;
    protected final JavaPsiFacade psiFacade;
    protected final GlobalSearchScope globalSearchScope;
    protected final PsiElementFactory psiElementFactory;
    protected final CodeStyleManager codeStyleManager;
    protected final DataContext dataContext;
    protected final Application application;


    public PsiInfrastructureHolder(DataContext dataContext) {
        this.project = DataKeys.PROJECT.getData(dataContext);
        this.psiFacade = JavaPsiFacade.getInstance(project);
        this.globalSearchScope = GlobalSearchScope.allScope(project);
        this.psiElementFactory = psiFacade.getElementFactory();
        this.codeStyleManager = PsiManager.getInstance(project).getCodeStyleManager();
        this.dataContext = dataContext;
        this.application = ApplicationManager.getApplication();
    }
}
