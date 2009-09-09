package de.jigp.plugin.configuration;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.util.IncorrectOperationException;

public class InsertLastPolicy implements InsertionPolicy {

    private static final InsertLastPolicy INSTANCE = new InsertLastPolicy();

    public static InsertLastPolicy getInstance() {
        return INSTANCE;
    }

    private InsertLastPolicy() {
    }

    public boolean insertNewMethod(PsiClass clazz, PsiMethod newMethod) throws IncorrectOperationException {
        PsiElement last = clazz.getRBrace();
        clazz.addBefore(newMethod, last);
        return true;
    }

    public String toString() {
        return "Last";
    }
}
