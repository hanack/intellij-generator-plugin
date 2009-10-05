package de.jigp.plugin.actions.contextmenu.dto;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import de.jigp.plugin.GeneratorPluginContext;
import de.jigp.plugin.actions.dto.DtoGenerator;
import de.jigp.plugin.actions.dto.DtoTargetClassChooser;
import de.jigp.plugin.actions.generator.CancelActionException;
import de.jigp.plugin.actions.menu.DetermineTargetClassChooser;
import de.jigp.plugin.actions.menu.PsiInfrastructureHolder;
import de.jigp.plugin.configuration.Configuration;

class DtoGeneratorComputable extends PsiInfrastructureHolder implements Computable<PsiClass> {
    private PsiClass selectedInterface;

    public DtoGeneratorComputable(DataContext dataContext, PsiClass selectedInterface) {
        super(dataContext);
        this.selectedInterface = selectedInterface;
    }

    public PsiClass compute() {
        Configuration configuration = GeneratorPluginContext.getConfiguration();
        String targetClassSuffix = null;
        if (configuration.isSuffixQuestionSupressed) {
            targetClassSuffix = configuration.dtoSuffix;
        } else {
            DetermineTargetClassChooser chooser = new DtoTargetClassChooser(dataContext);
            try {
                targetClassSuffix = chooser.invoke("Dto suffix");
            } catch (CancelActionException e) {
                return null;
            }
        }
        if (targetClassSuffix != null) {
            return new DtoGenerator(dataContext, selectedInterface, targetClassSuffix).build();
        } else {
            return null;
        }
    }

}