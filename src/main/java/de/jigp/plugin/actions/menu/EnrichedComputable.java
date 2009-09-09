package de.jigp.plugin.actions.menu;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;

public abstract class EnrichedComputable extends PsiInfrastructureHolder implements Computable {
    public EnrichedComputable(DataContext dataContext) {
        super(dataContext);
    }

    public abstract PsiClass compute();
}