package de.jigp.plugin.actions.menu;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMember;
import com.intellij.psi.search.searches.AnnotatedMembersSearch;
import com.intellij.util.Query;
import de.jigp.plugin.GeneratorPluginContext;
import de.jigp.plugin.actions.generator.CancelActionException;

import java.util.Collection;

public class FindAndHandleClassComputable extends PsiInfrastructureHolder implements Computable<PsiClass> {

    private String annotationName;
    private HandleAnnotatedClassAction menuAction;
    public DetermineTargetClassChooser targetClassChooser;

    public FindAndHandleClassComputable(DataContext dataContext,
                                        HandleAnnotatedClassAction menuAction,
                                        String annotationName,
                                        DetermineTargetClassChooser targetClassChooser) {
        super(dataContext);
        this.menuAction = menuAction;
        this.annotationName = annotationName;
        this.targetClassChooser = targetClassChooser;
    }

    public PsiClass compute() {
        if (isPreconditionViolated()) return null;
        handleClasses();
        return null;
    }

    private void handleClasses() {
        Collection<PsiMember> classNames = searchClassNamesToHandle();
        if (classNames != null && !classNames.isEmpty()) {
            String targetClassSuffix = targetClassChooser.getDefaultTargetClassSuffix();
            if (!GeneratorPluginContext.getConfiguration().isSuffixQuestionSupressed) {
                String text = "Generate elements for " + classNames.size() + " sources annotated with: " + annotationName + "\n";
                try {
                    targetClassSuffix = targetClassChooser.invoke(text);
                } catch (CancelActionException e) {
                    return;
                }
            }
            for (PsiMember member : classNames) {
                PsiClass psiClass = (PsiClass) member.getOriginalElement();
                //TODO add progress bar
                menuAction.handleClass(dataContext, psiClass, targetClassSuffix);
            }
            Messages.showInfoMessage("Amount of generated or modified Elements : " + classNames.size(), "Success");
        } else {
            Messages.showInfoMessage("No annotated Interfaces found. No Elements generated or modified." + annotationName, "Success");
        }
    }


    private Collection<PsiMember> searchClassNamesToHandle() {
        Query<PsiMember> query = AnnotatedMembersSearch.search(psiFacade.findClass(annotationName, globalSearchScope));
        Collection<PsiMember> memberCollection = query.findAll();
        return memberCollection;
    }


    private boolean isPreconditionViolated() {
        PsiClass dtoBaseTypeAnnotation = psiFacade.findClass(annotationName, globalSearchScope);
        if (dtoBaseTypeAnnotation == null) {
            Messages.showErrorDialog("Project needs following annotation in classpath: " + annotationName + ".\n Add the annotation to your projects classpath, or configure the plugin with the correct annotation name.", "Error missing annotation.");
            return true;
        }
        return false;
    }
}