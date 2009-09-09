package de.jigp.plugin.actions.contextmenu.dto;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import de.jigp.plugin.actions.dto.DtoGenerator;
import de.jigp.plugin.actions.dto.DtoTargetClassChooser;
import de.jigp.plugin.actions.menu.PsiInfrastructureHolder;

class DtoGeneratorComputable extends PsiInfrastructureHolder implements Computable<PsiClass> {
    private PsiClass selectedInterface;

    public DtoGeneratorComputable(DataContext dataContext, PsiClass selectedInterface) {
        super(dataContext);
        this.selectedInterface = selectedInterface;
    }

    public PsiClass compute() {
        String targetClassSuffix = new DtoTargetClassChooser(dataContext).invoke();
        if (targetClassSuffix != null) {
            return new DtoGenerator(dataContext, selectedInterface, targetClassSuffix).build();
        } else {
            return null;
        }
    }

}