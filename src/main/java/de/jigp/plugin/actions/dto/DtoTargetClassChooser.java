package de.jigp.plugin.actions.dto;

import com.intellij.openapi.actionSystem.DataContext;
import de.jigp.plugin.actions.menu.DetermineTargetClassChooser;

public class DtoTargetClassChooser extends DetermineTargetClassChooser {

    public DtoTargetClassChooser(DataContext dataContext) {
        super(new String[]{"Dto", "Data", "DataTransferObject"}, dataContext);
    }


}
