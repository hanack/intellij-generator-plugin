package de.jigp.plugin.actions.wrapper;

import com.intellij.openapi.actionSystem.DataContext;
import de.jigp.plugin.actions.menu.DetermineTargetClassChooser;

public class WrapperTargetClassChooser extends DetermineTargetClassChooser {

    public WrapperTargetClassChooser(DataContext dataContext) {
        super(new String[]{"Wrapper", "Adapter"},dataContext);
    }


}