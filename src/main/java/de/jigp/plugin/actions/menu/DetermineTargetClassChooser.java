package de.jigp.plugin.actions.menu;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;

import java.util.regex.Pattern;

public abstract class DetermineTargetClassChooser extends PsiInfrastructureHolder {
    private String[] defaultSuffixes;

    public DetermineTargetClassChooser(String[] defaultSuffixes, DataContext dataContext) {
        super(dataContext);
        this.defaultSuffixes = defaultSuffixes;
    }

    public String invoke() {
        InputValidator validator = new InputValidator() {

            public boolean checkInput(String suffix) {
                return canClose(suffix);
            }

            public boolean canClose(String suffix) {
                return Pattern.matches("\\w+?", suffix);
            }
        };
        String targetClassSuffix = Messages.showEditableChooseDialog("Enter suffix for generated classes.",
                "Suffix",
                Messages.getQuestionIcon(),
                defaultSuffixes,
                defaultSuffixes[0],
                validator);
        return targetClassSuffix;
    }


}
