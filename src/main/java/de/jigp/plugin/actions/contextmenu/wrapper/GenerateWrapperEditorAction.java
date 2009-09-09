package de.jigp.plugin.actions.contextmenu.wrapper;

import de.jigp.plugin.actions.contextmenu.GeneratorEditorAction;

public class GenerateWrapperEditorAction extends GeneratorEditorAction {

    protected GenerateWrapperEditorAction() {
        super(new GenerateWrapperEditorActionHandler());
    }
}
