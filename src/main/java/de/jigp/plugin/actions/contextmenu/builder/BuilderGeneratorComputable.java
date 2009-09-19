package de.jigp.plugin.actions.contextmenu.builder;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import de.jigp.plugin.actions.builder.generator.BuilderGenerator;
import de.jigp.plugin.actions.menu.PsiInfrastructureHolder;

class BuilderGeneratorComputable extends PsiInfrastructureHolder implements Computable<PsiClass> {
    private PsiClass selectedInterface;

    public BuilderGeneratorComputable(DataContext dataContext, PsiClass selectedInterface) {
        super(dataContext);
        this.selectedInterface = selectedInterface;
    }

    public PsiClass compute() {
        String targetClassSuffix = "Builder";
        return new BuilderGenerator(dataContext, selectedInterface, targetClassSuffix).build();
    }
}