package net.waterfallflower.cursedinterpolatorplugin.table.listener;

import bspkrs.mmv.gui.TableColumnAdjuster;
import net.waterfallflower.cursedinterpolatorplugin.table.MappingsViewerToolWindow;
import net.waterfallflower.cursedinterpolatorplugin.table.TableModels;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClassTableSelectionListener implements ListSelectionListener {
    private final JTable table;
    private final MappingsViewerToolWindow window;

    public ClassTableSelectionListener(MappingsViewerToolWindow window, JTable table) {
        this.window = window;
        this.table = table;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && !table.getModel().equals(TableModels.classesDefaultModel)) {
            int i = table.getSelectedRow();
            if (i > -1) {
                window.savePrefs();
                String pkg = (String) table.getModel().getValueAt(table.convertRowIndexToModel(i), 0);
                String name = (String) table.getModel().getValueAt(table.convertRowIndexToModel(i), 1);
                window.TABLE_METHODS.setModel(window.CURRENT_INSTANCE.getMethodModel(pkg + "/" + name));
                window.TABLE_METHODS.setEnabled(true);
                window.TABLE_FIELDS.setModel(window.CURRENT_INSTANCE.getFieldModel(pkg + "/" + name));
                window.TABLE_FIELDS.setEnabled(true);
                new TableColumnAdjuster(window.TABLE_METHODS).adjustColumns();
                new TableColumnAdjuster(window.TABLE_FIELDS).adjustColumns();
                window.loadPrefs();
            } else {
                window.TABLE_METHODS.setModel(TableModels.methodsDefaultModel);
                window.TABLE_METHODS.setEnabled(false);
                window.TABLE_FIELDS.setModel(TableModels.fieldsDefaultModel);
                window.TABLE_FIELDS.setEnabled(false);
            }
        }
    }
}