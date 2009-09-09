package de.jigp.plugin.configuration;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.util.IncorrectOperationException;

public interface InsertionPolicy {
    boolean insertNewMethod(PsiClass clazz, PsiMethod newMethod) throws IncorrectOperationException;
}
