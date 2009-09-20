package de.jigp.plugin.actions.contextmenu.wrapper;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import de.jigp.plugin.GeneratorPluginContext;
import de.jigp.plugin.actions.menu.PsiInfrastructureHolder;
import de.jigp.plugin.actions.wrapper.WrapperGenerator;

class WrapperGeneratorComputable extends PsiInfrastructureHolder implements Computable<PsiClass> {
    private PsiClass selectedInterface;

    public WrapperGeneratorComputable(DataContext dataContext, PsiClass selectedInterface) {
        super(dataContext);
        this.selectedInterface = selectedInterface;
    }

    public PsiClass compute() {
        String targetClassSuffix = GeneratorPluginContext.getConfiguration().wrapperSuffix;
        if (targetClassSuffix != null) {
            return new WrapperGenerator(dataContext, selectedInterface, targetClassSuffix).build();
        } else {
            return null;
        }
    }


}