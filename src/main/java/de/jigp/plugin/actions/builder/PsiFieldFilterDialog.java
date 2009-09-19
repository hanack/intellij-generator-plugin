package de.jigp.plugin.actions.builder;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiField;
import de.jigp.plugin.GeneratorPluginContext;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class PsiFieldFilterDialog extends DialogWrapper {

    private PsiField[] psiFields;

    private Object[][] rowData;
    private TableModel tableModel;
    private JCheckBox checkboxWithAssertions;
    private PsiFieldComparator psiFieldComparator = new PsiFieldComparator();

    public PsiFieldFilterDialog(PsiField[] psiFields) {
        super(true);
        this.psiFields = psiFields;
        setTitle("Attribute Selection");
        setResizable(true);
        init();
    }

    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();

        rowData = new Object[psiFields.length][2];
        int i = 0;
        Arrays.sort(psiFields, psiFieldComparator);
        for (PsiField psiField : psiFields) {
            rowData[i++] = new Object[]{true, psiField.getName(), psiField.getTypeElement().getType().getPresentableText()};
        }


        tableModel = createTableModel();

        JTable table;
        table = new JTable(tableModel);

        table.getColumnModel().getColumn(0).setPreferredWidth(10);

        JScrollPane pane = new JScrollPane(table);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(pane);

        checkboxWithAssertions = new JCheckBox("Create assertions for selected attributes in builder.\nThe configured assertion Method is " + GeneratorPluginContext.getConfiguration().builderAssertionName, false);
        panel.add(checkboxWithAssertions);
        return panel;
    }

    protected TableModel createTableModel() {
        return new AbstractTableModel() {
            String[] columnNames = new String[]{"", "Attribute", "Type"};

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
                return col == 0;
            }

            public void setValueAt(Object value, int row, int col) {
                rowData[row][col] = value;
                fireTableCellUpdated(row, col);
            }

            @Override
            public Class<?> getColumnClass(int i) {
                if (i == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(i);
            }
        };
    }

    public Collection<PsiField> getSelectedPsiFields() {
        Collection<PsiField> selectedPsiFields = new ArrayList<PsiField>();
        int row = 0;
        for (PsiField psiField : psiFields) {
            Boolean isSelected = (Boolean) tableModel.getValueAt(row, 0);
            row++;
            if (isSelected) {
                selectedPsiFields.add(psiField);
            }
        }
        return selectedPsiFields;
    }

    public boolean isWithAssertions() {
        return checkboxWithAssertions.isSelected();
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