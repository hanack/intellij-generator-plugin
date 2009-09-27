package de.jigp.plugin.actions.builder;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiField;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class PsiFieldFilterDialog extends DialogWrapper implements ActionListener {

    private PsiField[] psiFields;

    private Object[][] rowData;
    private TableModel tableModel;
    private JButton attributesSelection;
    private JButton assertionsSelection;
    private PsiFieldComparator psiFieldComparator = new PsiFieldComparator();
    private JPanel panel;
    private JTable table;

    private static final int COL_INDEX_ATTRIBUTESELECTION = 0;
    private static final int COL_INDEX_ASSERTIONSELECTION = 1;
    private boolean isAllAttributesSelected = true;
    private boolean isAllAssertionsSelected = true;
    private String attributesAllText = "all attributes";
    private String attributesNonText = "no attributes";
    private String assertionsAllText = "all assertions";
    private String assertionsNonText = "no assertions";

    public PsiFieldFilterDialog(PsiField[] psiFields) {
        super(true);
        this.psiFields = psiFields;
        setTitle("Attribute selection");
        setResizable(true);
        init();
    }

    protected JComponent createCenterPanel() {
        panel = new JPanel();

        rowData = new Object[psiFields.length][3];
        int i = 0;
        Arrays.sort(psiFields, psiFieldComparator);
        for (PsiField psiField : psiFields) {
            rowData[i++] = createRowEntry(psiField);
        }

        tableModel = createTableModel();

        table = new JTable(tableModel);

        table.getColumnModel().getColumn(COL_INDEX_ATTRIBUTESELECTION).setPreferredWidth(80);
        table.getColumnModel().getColumn(COL_INDEX_ASSERTIONSELECTION).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(350);
        table.getColumnModel().getColumn(3).setPreferredWidth(350);

        JScrollPane pane = new JScrollPane(table);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(pane);

        addSelectionButtons();

        return panel;
    }

    private Object[] createRowEntry(PsiField psiField) {
        return new Object[]{true, true, psiField.getName(), psiField.getTypeElement().getType().getPresentableText()};
    }

    protected void addSelectionButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        attributesSelection = new JButton(attributesNonText);
        attributesSelection.addActionListener(this);

        assertionsSelection = new JButton(assertionsNonText);
        assertionsSelection.addActionListener(this);

        buttonPanel.add(attributesSelection);
        buttonPanel.add(assertionsSelection);
        panel.add(buttonPanel);
    }

    protected TableModel createTableModel() {
        return new AbstractTableModel() {
            String[] columnNames = new String[]{"attribute", "with assertion", "name", "type"};

            public String getColumnName(int col) {
                return columnNames[col];
            }

            public int getRowCount() {
                return rowData.length;
            }

            public int getColumnCount() {
                return columnNames.length;
            }

            public Object getValueAt(int row, int col) {
                return rowData[row][col];
            }

            public boolean isCellEditable(int row, int col) {
                return isColumnSelectable(col);
            }

            public void setValueAt(Object value, int row, int col) {
                rowData[row][col] = value;
                fireTableCellUpdated(row, col);
            }

            @Override
            public Class<?> getColumnClass(int i) {
                if (isColumnSelectable(i)) {
                    return Boolean.class;
                }
                return super.getColumnClass(i);
            }
        };
    }

    private boolean isColumnSelectable(int col) {
        return col == COL_INDEX_ATTRIBUTESELECTION || col == COL_INDEX_ASSERTIONSELECTION;
    }

    public Collection<PsiField> getSelectedPsiFields() {
        return getSelectedFieldsForColumn(COL_INDEX_ATTRIBUTESELECTION);
    }

    public Collection<PsiField> getAssertionPsiFields() {
        return getSelectedFieldsForColumn(COL_INDEX_ASSERTIONSELECTION);
    }

    private Collection<PsiField> getSelectedFieldsForColumn(int column) {
        Collection<PsiField> selectedPsiFields = new ArrayList<PsiField>();
        int row = COL_INDEX_ATTRIBUTESELECTION;
        for (PsiField psiField : psiFields) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(row, column);
            row++;
            if (isSelected) {
                selectedPsiFields.add(psiField);
            }
        }
        return selectedPsiFields;
    }

    public void actionPerformed(ActionEvent e) {
        if (attributesSelection.equals(e.getSource())) {
            attributesSelection.setText(toggledAttributeSelection());
            updateSelectionColumn(COL_INDEX_ATTRIBUTESELECTION, isAllAttributesSelected);
            if (!isAllAttributesSelected) {
                assertionsSelection.setText(assertionsAllText);
                isAllAssertionsSelected = false;
                updateSelectionColumn(COL_INDEX_ASSERTIONSELECTION, isAllAssertionsSelected);
            }

        } else if (assertionsSelection.equals(e.getSource())) {
            assertionsSelection.setText(toggledAssertionSelection());
            updateSelectionColumn(COL_INDEX_ASSERTIONSELECTION, isAllAssertionsSelected);

        }
        this.table.updateUI();
    }

    private String toggledAssertionSelection() {
        if (isAllAssertionsSelected) {
            isAllAssertionsSelected = !isAllAssertionsSelected;
            return assertionsAllText;
        } else {
            isAllAssertionsSelected = !isAllAssertionsSelected;
            return assertionsNonText;
        }

    }

    private String toggledAttributeSelection() {
        if (isAllAttributesSelected) {
            isAllAttributesSelected = !isAllAttributesSelected;
            return attributesAllText;
        } else {
            isAllAttributesSelected = !isAllAttributesSelected;
            return attributesNonText;
        }
    }

    private void updateSelectionColumn(int colIndex, boolean isSelected) {
        for (Object[] row : rowData) {
            row[colIndex] = isSelected;
        }
    }


    public static class PsiFieldComparator implements Comparator<PsiField> {

        public int compare(PsiField psiField, PsiField psiField1) {
            if (psiField == null || psiField1 == null) {
                return -1;
            }
            return psiField.getName().compareTo(psiField1.getName());
        }
    }
}