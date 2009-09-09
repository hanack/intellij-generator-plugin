package de.jigp.plugin.actions.builder;

import com.intellij.openapi.actionSystem.DataContext;
import de.jigp.plugin.actions.menu.DetermineTargetClassChooser;

public class BuilderTargetClassChooser extends DetermineTargetClassChooser {

    public BuilderTargetClassChooser(DataContext dataContext) {
        super(new String[]{"Builder"}, dataContext);
    }


}