package de.jigp.plugin.configuration;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMember;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.util.IncorrectOperationException;
import de.jigp.plugin.PsiHelper;

public class InsertAtCursorPolicy implements InsertionPolicy {
    private static final InsertAtCursorPolicy INSTANCE = new InsertAtCursorPolicy();

    public static InsertAtCursorPolicy getInstance() {
        return INSTANCE;
    }

    private InsertAtCursorPolicy() {
    }

    public boolean insertNewMethod(PsiClass clazz, PsiMethod newMethod) throws IncorrectOperationException {

        PsiHelper psiHelper = new PsiHelper(clazz.getProject());

        PsiElement cursorPosition = psiHelper.findElementAtCursorPosition();

        if (beforeLeftBrace(cursorPosition, clazz)) {
            clazz.addAfter(newMethod, clazz.getLBrace());
        } else if (beforeRightBrace(cursorPosition, clazz)) {
            PsiElement spot = findBestSpotToInsert(cursorPosition);
            if (spot != null) {
                clazz.addAfter(newMethod, spot);
            } else {
                clazz.addAfter(newMethod, cursorPosition);
            }
        } else {
            clazz.addBefore(newMethod, clazz.getRBrace());
        }

        return true;
    }

    private static PsiElement findBestSpotToInsert(PsiElement elem) {
        if (elem instanceof PsiWhiteSpace) {
            PsiMethod method = PsiHelper.findParentMethod(elem);
            return method == null ? elem : method;
        } else if (elem instanceof PsiMethod) {
            return elem;
        } else if (elem instanceof PsiMember && !(elem instanceof PsiClass)) {
            return elem;
        } else if (elem instanceof PsiJavaFile) {
            return null;
        }

        PsiElement parent = elem.getParent();
        if (parent != null) {
            return findBestSpotToInsert(parent);
        }
        return null;
    }

    private static boolean beforeRightBrace(PsiElement elem, PsiClass clazz) {
        return clazz == null ||
                clazz.getRBrace() == null ||
                elem.getTextOffset() < clazz.getRBrace().getTextOffset();
    }

    private static boolean beforeLeftBrace(PsiElement elem, PsiClass clazz) {
        return clazz == null ||
                clazz.getLBrace() == null ||
                elem.getTextOffset() < clazz.getLBrace().getTextOffset();
    }

    public String toString() {
        return "At cursor";
    }
}
