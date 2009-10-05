package de.jigp.plugin.actions.menu;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import de.jigp.plugin.actions.generator.CancelActionException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public abstract class DetermineTargetClassChooser extends PsiInfrastructureHolder {
    private List<String> defaultSuffixes;

    public DetermineTargetClassChooser(String[] defaultSuffixes, DataContext dataContext) {
        super(dataContext);
        this.defaultSuffixes = Arrays.asList(defaultSuffixes);
    }

    public String invoke(String headlineText) throws CancelActionException {
        InputValidator validator = new InputValidator() {

            public boolean checkInput(String suffix) {
                return canClose(suffix);
            }

            public boolean canClose(String suffix) {
                return Pattern.matches("\\w+?", suffix);
            }
        };

        String[] suffixes = (String[]) defaultSuffixes.toArray(new String[0]);

        String targetClassSuffix = Messages.showEditableChooseDialog(headlineText + "\nEnter suffix for generated classes.",
                "Suffix",
                Messages.getQuestionIcon(),
                suffixes,
                defaultSuffixes.get(0),
                validator);
        if (targetClassSuffix == null){
            throw new CancelActionException();
        }
        return targetClassSuffix;
    }

    public abstract String getDefaultTargetClassSuffix();


}
