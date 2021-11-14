package net.waterfallflower.cursedinterpolatorplugin.table.listener;

import bspkrs.mmv.McpMappingLoader;
import bspkrs.mmv.gui.TableColumnAdjuster;
import immibis.bon.IProgressListener;
import immibis.bon.gui.Side;
import net.waterfallflower.cursedinterpolatorplugin.CursedInterpolatorSettingsStorage;
import net.waterfallflower.cursedinterpolatorplugin.table.MappingsViewerToolWindow;
import net.waterfallflower.cursedinterpolatorplugin.table.TableHelper;
import net.waterfallflower.cursedinterpolatorplugin.table.TableModels;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

public class RefreshActionListener implements ActionListener {

    private final MappingsViewerToolWindow window;

    public RefreshActionListener(MappingsViewerToolWindow window) {
        this.window = window;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (window.FRAME_THREAD_SECOND != null && window.FRAME_THREAD_SECOND.isAlive())
            return;

        final Side side = Objects.requireNonNull((Side) window.BOX_TABLE_SIDE.getSelectedItem());

        String error = null;

        if (!CursedInterpolatorSettingsStorage.getMappings().exists())
            error = "Cursed mapping have not been set up!";

        if (!new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION).isDirectory())
            error = "Folder not found (at " + CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION + ")";

        if (error != null) {
            JOptionPane.showMessageDialog(window, error, "MMV - Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        window.savePrefs();

        window.PANEL_PROGRESSBAR.setVisible(true);
        window.TABLE_CLASSES.setModel(TableModels.classesDefaultModel);
        window.TABLE_CLASSES.setEnabled(false);
        window.TABLE_METHODS.setModel(TableModels.methodsDefaultModel);
        window.TABLE_METHODS.setEnabled(false);
        window.TABLE_FIELDS.setModel(TableModels.fieldsDefaultModel);
        window.TABLE_FIELDS.setEnabled(false);

        window.FRAME_THREAD_SECOND = new Thread(() -> {
            boolean crashed = false;

            try {
                IProgressListener progress = new IProgressListener() {
                    private String currentText;

                    @Override
                    public void start(final int max, final String text) {
                        currentText = text.equals("") ? " " : text;
                        SwingUtilities.invokeLater(() -> {
                            window.BAR_PROGRESSBAR.setString(currentText);
                            if (max >= 0)
                                window.BAR_PROGRESSBAR.setMaximum(max);
                            window.BAR_PROGRESSBAR.setValue(0);
                        });
                    }

                    @Override
                    public void set(final int value) {
                        SwingUtilities.invokeLater(() -> window.BAR_PROGRESSBAR.setValue(value));
                    }

                    @Override
                    public void setMax(final int max) {
                        SwingUtilities.invokeLater(() -> window.BAR_PROGRESSBAR.setMaximum(max));
                    }
                };

                window.CURRENT_INSTANCE = new McpMappingLoader(side, new File(CursedInterpolatorSettingsStorage.getInstance().MCP_LOCATION), progress);
                window.TABLE_CLASSES.setModel(window.CURRENT_INSTANCE.getClassModel());
                window.TABLE_CLASSES.setEnabled(true);
                new TableColumnAdjuster(window.TABLE_CLASSES).adjustColumns();
                //                        TableRowSorter trs = (TableRowSorter) tblClasses.getRowSorter();
                //                        trs.setComparator(2, McpMappingLoader.OBF_COMPARATOR);
                window.loadPrefs();
            } catch (McpMappingLoader.CantLoadMCPMappingException e1) {
                String s = TableHelper.getStackTraceMessage("", e1);

                System.err.println(s);

                crashed = true;

                final String errMsg = s;
                SwingUtilities.invokeLater(() -> {
                    window.BAR_PROGRESSBAR.setString(" ");
                    window.BAR_PROGRESSBAR.setValue(0);

                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(errMsg), null);
                    JOptionPane.showMessageDialog(window, errMsg, "MMV - Error", JOptionPane.ERROR_MESSAGE);
                });
            } catch (Exception e1) {
                String s = TableHelper.getStackTraceMessage("An error has occurred - give calmilamsy this stack trace (which has been copied to the clipboard)\n", e1);

                System.err.println(s);

                crashed = true;

                final String errMsg = s;
                SwingUtilities.invokeLater(() -> {
                    window.BAR_PROGRESSBAR.setString(" ");
                    window.BAR_PROGRESSBAR.setValue(0);

                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(errMsg), null);
                    JOptionPane.showMessageDialog(window, errMsg, "MMV - Error", JOptionPane.ERROR_MESSAGE);
                });
            } finally {
                if (!crashed) {
                    SwingUtilities.invokeLater(() -> {
                        window.BAR_PROGRESSBAR.setString(" ");
                        window.BAR_PROGRESSBAR.setValue(0);
                        window.FIELD_TABLE_SEARCH.setEnabled(true);
                    });
                }
                window.PANEL_PROGRESSBAR.setVisible(false);
                window.BUTTON_TABLE_SEARCH.setEnabled(true);
                window.FIELD_TABLE_SEARCH.setEnabled(true);
            }
        });

        window.FRAME_THREAD_SECOND.start();
    }
}