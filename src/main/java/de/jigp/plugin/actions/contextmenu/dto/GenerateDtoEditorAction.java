package de.jigp.plugin.actions.contextmenu.dto;

import de.jigp.plugin.actions.contextmenu.GeneratorEditorAction;

public class GenerateDtoEditorAction extends GeneratorEditorAction {

    protected GenerateDtoEditorAction() {
        super(new GenerateDtoEditorActionHandler());
    }
}