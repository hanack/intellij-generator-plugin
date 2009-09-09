package de.jigp.plugin.actions.contextmenu.builder;

import de.jigp.plugin.actions.contextmenu.GeneratorEditorAction;

public class GenerateBuilderEditorAction extends GeneratorEditorAction {

    protected GenerateBuilderEditorAction() {
        super(new GenerateBuilderEditorActionHandler());
    }
}