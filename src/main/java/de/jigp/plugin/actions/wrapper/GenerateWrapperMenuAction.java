package de.jigp.plugin.actions.wrapper;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.PsiClass;
//import de.hypoport.ef2.codegeneration.dto.DtoBaseType;
import de.jigp.plugin.actions.menu.DetermineTargetClassChooser;
import de.jigp.plugin.actions.menu.HandleAnnotatedClassAction;


public class GenerateWrapperMenuAction extends HandleAnnotatedClassAction {


    protected void handleClass(DataContext dataContext, PsiClass annotatedClass, String targetClassSuffix) {
        new WrapperGenerator(dataContext, annotatedClass, targetClassSuffix).build();
    }

    protected DetermineTargetClassChooser getTargetClassChooser(DataContext dataContext) {
        return new WrapperTargetClassChooser(dataContext);
    }

    protected String getAnnotationName() {
        //TODO Make it configurable via plugin settings, or input dialog
        return "de.hypoport.ef2.codegeneration.dto.DtoBaseType";//DtoBaseType.class.getName();
    }

}