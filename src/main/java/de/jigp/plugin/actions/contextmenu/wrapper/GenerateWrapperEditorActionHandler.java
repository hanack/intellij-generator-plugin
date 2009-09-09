package de.jigp.plugin.actions.contextmenu.wrapper;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import de.jigp.plugin.actions.contextmenu.GeneratorEditorActionHandler;

class GenerateWrapperEditorActionHandler extends GeneratorEditorActionHandler {

    protected Computable createGenerator(DataContext dataContext, PsiClass selectedInterface) {
        return new WrapperGeneratorComputable(dataContext, selectedInterface);
    }

}
