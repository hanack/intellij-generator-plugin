package de.jigp.plugin.actions.contextmenu;

import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

public abstract class GeneratorEditorAction extends EditorAction {

    protected GeneratorEditorAction(EditorActionHandler actionHandler) {
        super(actionHandler);
    }
}